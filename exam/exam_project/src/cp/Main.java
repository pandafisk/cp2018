package cp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final File dir = new File("C:\\Users\\Troels\\Documents\\GitHub\\cp2018-f\\exam\\data_example");

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
//        m1();
        m2();

        long end = (System.currentTimeMillis() - start);
        System.out.println("\ntotal time: " + end + " ms");
    }

    public static void m1() throws IOException {
        List<Result> list;
        Path p = dir.toPath();
        list = Exam.m1(p);
        list.forEach(i -> System.out.println(i.number() + " was the smallest in: " + i.path()));
    }

    public static void m2() throws IOException {
        Path p = dir.toPath();
        int min = 39347188;
        Result result = Exam.m2(p, min);
        System.out.println("\nFound the values combined on line " + result.number() + " from " + result.path() + " to be greater than " + min);
    }
}
