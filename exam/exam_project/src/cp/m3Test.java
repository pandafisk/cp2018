/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Troels
 */
public class m3Test {

    public static void main(String[] args) throws FileNotFoundException {

        final File dir = new File("C:\\Users\\Troels\\Documents\\GitHub\\cp2018-f\\exam\\data_example\\numbers_1.txt");
        final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<>();
        if (dir.isFile()) {

            String str = dir.toString();
            Scanner scan = new Scanner(new File(str));

            String nextString = scan.next();

//    splitting the strings by ","
            List<String> strArr = Arrays.asList(nextString.split(","));
            List<Integer> intArr = new ArrayList<>();

            for (String i : strArr) {
                if (i.length() > 0) {
                    map.putIfAbsent(i, new AtomicInteger(0));
                    map.get(i).incrementAndGet();

//                    System.out.println("Hello There");
                }
            }
        }
        for (final String k : map.keySet()) {
            System.out.println(k + ": " + map.get(k).get());
        }
    }
}
