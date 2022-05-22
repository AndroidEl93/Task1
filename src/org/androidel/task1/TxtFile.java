package org.androidel.task1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**Класс реализующий хранение информации о текстовых файлах и методы их обработки, в контексте 1-й задачи**/
class TxtFile {
    private String name;//Отображаемое в списках имя

    private TxtFile(String name) {
        this.name = name;
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
                    result.add(new TxtFile(fileName));
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
}