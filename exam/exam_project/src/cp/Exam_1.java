package cp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exam_1
{
    // initalizing executorservice and result-array
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static ExecutorService ex = Executors.newWorkStealingPool(PROCESSORS);
    private static final ArrayList RESULTS = new ArrayList<Result>();
    private static int highestNumber;

    // TASK 1
    public static class task1 implements Runnable {
        private final Path p;
        // setting up constructor
        private task1(Path dir) {
            this.p = dir;
        }
        @Override
        public void run() {
            try {
                highestNumber(p);
            } catch (IOException ex) {
            }
        }
    }
    public static List< Result > findAll( Path dir ) throws IOException {
        // creates new executor if its shut down.
       if (ex.isShutdown()) {
           ex = Executors.newWorkStealingPool(PROCESSORS);
       }
       // then calling visit() to recurvely search all folder and .txt-files
       visit(dir);
       ex.shutdown();

       while (RESULTS.isEmpty()) {
           ex.shutdown();
       }
       return RESULTS;
    }
        // recursive call through folder and subfolders
        // for all files with suffix .txt, run task1-executor
        private static void visit(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (Files.isDirectory(p)) {
                   visit(p);
                } else if (p.toString().endsWith(".txt")) {
                    Runnable run = new task1(p);
                    ex.execute(run);
                }
            }
        }
    }
        // searching for highest number in all files
    private static void highestNumber(Path dir) throws FileNotFoundException, IOException {
        String p = dir.toString();
        Scanner sc = new Scanner(new File(p));
        String nextString = sc.next();

        // splitting strings by ','
        List<String> arr = Arrays.asList(nextString.split(","));
        List<Integer> intArr = new ArrayList<>();

        // iterate through string-array converting into integer-array
        for(String s : arr) {
            intArr.add(Integer.valueOf(s));
        }
            // compare size of number untill highest is found
            highestNumber = intArr.get(0);
            for (int i = 0; i < intArr.size(); i++) {
                int number = intArr.get(i);
                if (number > highestNumber) {
                    highestNumber = number;
                }
        }
            // synchronizing the results
            synchronized(RESULTS) {
            final int number = highestNumber;
            RESULTS.add(new Result() {
                @Override
                public Path path() {
                    return dir;
                }
                @Override
                public int number() {
                   return number;
                }
            });
        }
    }
    // TASK2
    public static class task2 implements Runnable {
        private final Path p;
        private final int n;
        private final int min;
        // setting up constructor
        private task2(Path dir, int n, int min) {
            this.p = dir;
            this.n = n;
            this.min = min;
        }
        @Override
        public void run() {
            try {
                findAnyFile(p, n, min);
            } catch (IOException ex) {
            }
        }
    }
    public static Result findAny( Path dir, int n, int min ) throws IOException
    {
             if (ex.isShutdown()) {
                 ex = Executors.newWorkStealingPool(PROCESSORS);
             }
        visit2(dir, n, min);

        while (RESULTS.isEmpty()){
            ex.shutdown();
        }
        Result r = (Result) RESULTS.get(0);
        return r;
    }
    // recursive call through folder and subfolders
    // for all files with suffix .txt, run task2-executor
    private static void visit2(Path dir, int n, int min) 
            throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (Files.isDirectory(p)) {
                   visit2(p, n, min);
                } else if (p.toString().endsWith(".txt")) {
                    Runnable run = new task2(p, n, min);
                    ex.execute(run);
                }
            }
        }
    }
    private static void findAnyFile(Path dir, int n, int min)
            throws FileNotFoundException
    {
        String p = dir.toString();
        Scanner sc = new Scanner(new File(p));
        String nextString = sc.next();

        // splitting strings by ','
        List<String> arr = Arrays.asList(nextString.split(","));
        List<Integer> intArr = new ArrayList<>();
        // iterate through string-array converting into integer-array
        for(String s : arr){
            intArr.add(Integer.valueOf(s));
        }
        // finding any file that contains at most n numbers, where all
        // numbers are equal or greater than min
        if (intArr.size() <= n) {
            for (int i = 0; i < intArr.size(); i++) {
                int number = intArr.get(i);
                if (number <= min) {
                    return;
                } else {
                    // synchronizing the results
                    synchronized(RESULTS){
                        RESULTS.add(new Result(){
                            @Override
                            public Path path(){
                                return dir;
                            }
                            @Override
                            public int number() {
                                return number;
                            }
                        });
                    }
                }
            }
        }
    }
    // TASK 3 (not completed)
    public static Stats stats( Path dir ){
            throw new UnsupportedOperationException();
    }
}