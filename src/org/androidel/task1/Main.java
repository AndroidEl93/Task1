package org.androidel.task1;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    final static String OUT_FILE_NAME = "out.txt";

    public static void main(String[] args) {
        System.out.println("Задание #1");

        //Выбор корневого каталога
        String folder = System.getProperty("user.dir") + "\\folder";
        System.out.println("Корневой каталог:" + folder);
        System.out.println("...Для продолжения нажмите ввод (или укажите другой каталог)...");
        String input = new Scanner(System.in).nextLine();
        if (input.length() > 0) {
            folder = input;
        }

        try {
            //Получить список текстовых файлов
            LinkedList<TxtFile> files = TxtFile.getTxtFileNames(folder);
            System.out.println("Найденые текстовые файлы:");
            TxtFile.printList(files);

            //Сортировка файлов по имени
            files.sort(Comparator.comparing(TxtFile::getName));

            //Поиск зависимостей
            TxtFile.searchRequired(files);

            //Поиск циклических зависимостей в файлах
            TxtFile.checkCyclic(files);

            //Сортировка по зависимостям
            LinkedList<TxtFile> sortFiles = TxtFile.sortByRequired(files);
            System.out.println("Отсортированные по имени и зависимостям файлы:");
            TxtFile.printList(sortFiles);

            //Склейка файлов
            TxtFile.getConcatenationFile(sortFiles, OUT_FILE_NAME);
            System.out.println("Содержимое файлов из списка склеено в один текстовый файл: \"" + OUT_FILE_NAME + "\"");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}