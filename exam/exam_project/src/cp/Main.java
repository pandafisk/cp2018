package cp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final File dirt = new File("C:\\Users\\Troels\\Desktop");
    private static final File dir = new File("C:\\Users\\Troels\\Documents\\GitHub\\cp2018-f\\exam\\data_example");

    public static void main(String[] args) throws IOException, InterruptedException {

        long start = System.currentTimeMillis();
        m1();
        m2();
        m3();
        long end = (System.currentTimeMillis() - start);
        System.out.println("\ntotal time: " + end + " ms");
    }

    public static void m1() throws IOException {
        List<Result> list;
        Path p = dir.toPath();
        list = Exam.m1(p);
//        list.forEach(i -> System.out.println(i.number() + " was the smallest in: " + i.path()));
    }

    public static void m2() throws IOException, InterruptedException {
        Path p = dir.toPath();
        int min = 39347188;
        Result result = Exam.m2(p, min);
//        System.out.println("\nFound the values combined on line " + result.number() + " from " + result.path() + " to be greater than " + min);
    }

    public static void m3() throws IOException, InterruptedException {
        Path p = dir.toPath();
        Stats stat = Exam.m3(p);
//        List<Path> AList = stat.byTotals();
        int num = 123;
        stat.occurrences(num);
        stat.mostFrequent();
        stat.leastFrequent();
        stat.byTotals();

//        Exam.getDict();
//        System.out.println("The value '" + num + "' occurred " + stat.occurrences(num) + " times");
//        System.out.println("The most frequent number is: " + stat.mostFrequent());
//        System.out.println("The least frequent number is: " + stat.leastFrequent());
//        System.out.println("Files sorted by their combined total value: \n");
//        for (int i = 0; i<AList.size(); i++){
//            System.out.println(AList.get(i));
//        }
    }

}
