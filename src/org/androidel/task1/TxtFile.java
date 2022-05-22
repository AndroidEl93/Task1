package org.androidel.task1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Класс реализующий хранение информации о текстовых файлах и методы их обработки, в контексте 1-й задачи**/
class TxtFile {
    private String name;//Отображаемое в списках имя
    private String path;//Путь к файлу
    private List<TxtFile> required;//Список ссылок на зависимые файлы

    private TxtFile(String name, String path) {
        this.name = name;
        this.path = path;
        required = new LinkedList<>();
    }

    /**
     * Функция возвращает список всех текстовых файлов корневой папки и всех вложенных папок
     * @param folder Корневая папка
     * @return Список текстовых файлов
     */
    static LinkedList<TxtFile> getTxtFileNames(String folder) {
        LinkedList<TxtFile> fileNames = new LinkedList<>();
        searchTxtFiles(fileNames, new File(folder), folder);
        return fileNames;
    }

    /**
     * Функция для рекурсивного обхода папок в поиске текстовых файлов
     * @param result Список найденных файлов
     * @param folder Текущая папка обхода
     * @param root Имя корневой папки
     */
    private static void searchTxtFiles(List<TxtFile> result, File folder, String root) {
        for (File file : Objects.requireNonNull(folder.listFiles(),
                "Ошибка чтения каталога \"" + folder.getPath() + "\"")) {//Обходим все файлы папки folder
            if (file.isDirectory()) {//Если текйщий файл - папка, проверяем ее
                searchTxtFiles(result, file, root);
            } else {//Если файл...
                String filePath = file.getPath();//Относительный путь к файлу (Включая корневую папку)
                int index = filePath.lastIndexOf('.');//Индекс точки-разделителя расширения файла
                //Если файл имеет расширение текстового файла txt ...
                if (index != -1 && filePath.substring(index + 1).equalsIgnoreCase("txt")) {
                    //Формируем имя файла для списка - относительный путь без имени корневой папки и без расширения
                    String fileName = filePath.substring(0, index).substring(filePath.indexOf(root) + root.length() + 1);
                    //Пополняяем список найденый файлов
                    result.add(new TxtFile(fileName, filePath));
                }
            }
        }
    }

    /**
     * Вывод в консоль имен файлов из списка
     * @param list Список файлов
     */
    static void printList(LinkedList<TxtFile> list) {
        for (TxtFile file : list) {
            System.out.println("\t"+file.name);
        }
    }

    /**
     * Поиск директив зависимостей в файлах списка
     * @param list Список файлов
     */
    static void searchRequired(List<TxtFile> list) {
        for (TxtFile txtFile : list) {//Обход файлов списка
            try {
                //Чтение содержимого файла в stringBuilder
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(txtFile.path), StandardCharsets.UTF_8));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                //Поиск директив через регулярные выражения
                Matcher matcher = Pattern.compile("require '.+?'").matcher(stringBuilder);
                while (matcher.find()) {//Обход директив файла
                    String match = matcher.group();//Текст директивы
                    String fileName = match.split("'")[1];//Имя файлы из директивы

                    //Проверка этого файла на наличие в списке файлов
                    TxtFile fileFound = null;
                    for (TxtFile file : list) {
                        if (file.name.equals(fileName)) {
                            fileFound = file;
                            break;
                        }
                    }

                    //Если файл найден, добавялем в список зависимостей текущего файла
                    if (fileFound != null) {
                        txtFile.required.add(fileFound);
                    } else {//Если файл не найден, то информируем об ошибке
                        System.out.println("Ошибка в директиве файла \"" + txtFile.name + "\", " +
                                "указанный файл не найден (" + match + ")");
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка чтения файла \"" + txtFile.name + "\" (" + e.getMessage() + ")");
            }
        }
    }

    /**
     * Функция возвращает имя файла (Необходима для сортировки файлов по имени)
     * @return Имя файла
     */
    String getName() {
        return name;
    }
}