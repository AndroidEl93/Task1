package org.androidel.task1;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {

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

        //Получить список текстовых файлов
        LinkedList<TxtFile> files = TxtFile.getTxtFileNames(folder);
        System.out.println("Найденые текстовые файлы:");
        TxtFile.printList(files);
    }
}