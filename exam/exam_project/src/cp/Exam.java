package cp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Exam {

//    initalizing the executorservice and the final array, containning the results, along with other variables used
//    Multiple of both executirService and ResultList is created, in case all functions gets tested at the same time
    private static final int cores = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorServiceM1 = Executors.newWorkStealingPool(cores);
    private static ExecutorService executorServiceM2 = Executors.newWorkStealingPool(cores);
    private static ExecutorService executorServiceM3 = Executors.newWorkStealingPool(cores);

    private static final ArrayList ResultList = new ArrayList<Result>();
    private static final ArrayList ResultList2 = new ArrayList<Result>();
    public static final ArrayList ResultList3 = new ArrayList<Result>();

    private final static ConcurrentHashMap<Integer, Integer> dictionary = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Path, Integer> dictionary2 = new ConcurrentHashMap<>();

    private static int lownumber;
    private static int count;
    private static int total;

    /**
     * This method recursively visits a directory to find all the text files
     * contained in it and its subdirectories.
     *
     * You must consider only files ending with a ".txt" suffix. You are
     * guaranteed that they will be text files.
     *
     * You can assume that each text file contains a (non-empty) comma-separated
     * sequence of numbers. For example: 100,200,34,25 There won't be any new
     * lines, spaces, etc., and the sequence never ends with a comma. You are
     * guaranteed that each number will be at least or equal to 0 (zero), i.e.,
     * no negative numbers.
     *
     * The search is recursive: if the directory contains subdirectories, these
     * are also searched and so on so forth (until there are no more
     * subdirectories).
     *
     * This method returns a list of results. The list contains a result for
     * each text file that you find. Each {@link Result} stores the path of its
     * text file, and the lowest number (minimum) found inside of the text file.
     *
     * @param dir the directory to search
     * @return a list of results ({@link Result}), each giving the lowest number
     * found in a file
     */
//        TASK 1
    public static class assignment1 implements Runnable {

        private final Path p;

//         setting up a constructor
        private assignment1(Path dir) {
            this.p = dir;
        }

        @Override
        public void run() {
            try {
                lowestNumber(p);
            } catch (IOException ex) {
                Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static List< Result> m1(Path dir) throws IOException {
//         if the executor is shut down, creates a new one.
        if (executorServiceM1.isShutdown()) {
            executorServiceM1 = Executors.newWorkStealingPool(cores);
        }
//         calling dial() on the directory to search through the files
        dial(dir);
        executorServiceM1.shutdown();

        while (ResultList.isEmpty()) {
            executorServiceM1.shutdown();
        }
        return ResultList;
    }
//     going through all folders and text-files
//     running executor for task 1 if any file has a .txt suffix

    private static void dial(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (p.toString().endsWith(".txt")) {
                    Runnable run = new assignment1(p);
                    executorServiceM1.execute(run);

                } else if (Files.isDirectory(p)) {
                    dial(p);
                }
            }
        }
    }
//     finding the lowest number in all the files

    private static void lowestNumber(Path dir) throws FileNotFoundException, IOException {
        String str = dir.toString();
        Scanner scan = new Scanner(new File(str));
        String nextString = scan.next();

//    splitting the strings by ","
        List<String> strArr = Arrays.asList(nextString.split(","));
        List<Integer> intArr = new ArrayList<>();

//    converting the elements in the string-array to an int-array
        for (String s : strArr) {
            intArr.add(Integer.valueOf(s));
        }
//    comparing the numbers and finding the lowest
        lownumber = intArr.get(0);
        for (int i = 0; i < intArr.size(); i++) {
            int num = intArr.get(i);
            if (num < lownumber) {
                lownumber = num;
            }
        }
//    synchronizing the results gathered
        synchronized (ResultList) {
            final int num = lownumber;
            ResultList.add(new Result() {
                @Override
                public Path path() {
                    return dir;
                }

                @Override
                public int number() {
                    return num;
                }
            });
        }
    }

    /**
     * This method recursively visits a directory for text files with suffix
     * ".dat" (notice that it is different than the one before) contained in it
     * and its subdirectories.
     *
     * You must consider only files ending with a .dat suffix. You are
     * guaranteed that they will be text files.
     *
     * Each .dat file contains some lines of text, separated by the newline
     * character "\n". You can assume that each line contains a (non-empty)
     * comma-separated sequence of numbers. For example: 100,200,34,25
     *
     * This method looks for a .dat file that contains a line whose numbers,
     * when added together (total), amount to at least (>=) parameter min. Once
     * this is found, the method can return immediately (without waiting to
     * analyse also the other files). The return value is a result that
     * contains: - path: the path to the text file that contains the line that
     * respects the condition; - number: the line number, starting from 1 (e.g.,
     * 1 if it is the first line, 3 if it is the third, etc.)
     *
     */
//    Task 2
    public static class assignment2 implements Runnable {

        private final Path dir;
        private final int min;

//    setting up a constructor
        private assignment2(Path dir, int min) {
            this.dir = dir;
            this.min = min;
        }

        @Override
        public void run() {
            try {
                find(dir, min);
            } catch (IOException ex) {
                Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Result m2(Path dir, int min) throws IOException, InterruptedException {
//    if the executor is shut down, creates a new one.
        if (executorServiceM2.isShutdown()) {
            executorServiceM2 = Executors.newWorkStealingPool(cores);
        }
//    calling the second dial on the directory to search through the files
        dial_2(dir, min);
        executorServiceM2.shutdown();
        executorServiceM2.awaitTermination(20, TimeUnit.SECONDS);

        while (ResultList2.isEmpty()) {
            executorServiceM2.shutdown();
        }
        Result result = (Result) ResultList2.get(0);
        return result;

    }

//    going through all files and sub-directories
//    if the file ends with a ".dat", the second constructor is called
    private static void dial_2(Path dir, int min) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (p.toString().endsWith(".dat")) {
                    Runnable run = new assignment2(p, min);
                    executorServiceM2.execute(run);

                } else if (Files.isDirectory(p)) {
                    dial_2(p, min);
                }
            }
        }
    }

//    searching through all the lines in the given file.
//    if the number on each line, added together is at least as big as the "min"
//    parameter, the method returns the directory and line
    private static void find(Path dir, int min) throws FileNotFoundException {
        String str = dir.toString();

        Scanner scan = new Scanner(new File(str));

        count = 0;
        while (scan.hasNextLine()) {
            count++;
            String next = scan.next();
            
//            Splitting strings by ","
            List<String> strArr = Arrays.asList(next.split(","));
            List<Integer> intArr = new ArrayList<>();
            
//            converting the Stringarray to an int Array
            for (String string : strArr) {
                intArr.add(Integer.valueOf(string));
            }
            int total = intArr.get(0);
            for (int i = 0; i < intArr.size() - 1; i++) {
                total += intArr.get(i);
            }

            if (total >= min) {
                return;
            } else {
//            Synchronizing the results gathered
                synchronized (ResultList2) {
                    ResultList2.add(new Result() {
                        @Override
                        public Path path() {
                            return dir;
                        }

                        @Override
                        public int number() {
                            return count;
                        }
                    });
                }
            }

            scan.nextLine();
        }
    }

    /**
     * Computes overall statistics about the occurrences of numbers in a
     * directory.
     *
     * This method recursively searches the directory for all numbers in all
     * lines of .txt and .dat files and returns a {@link Stats} object
     * containing the statistics of interest. See the documentation of
     * {@link Stats}.
     */
    public static class assignment3 implements Runnable {

        private final Path dir;

//    setting up a constructor
        private assignment3(Path dir) {
            this.dir = dir;
        }

        @Override
        public void run() {
            try {
                DictMaker(dictionary, dir);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Stats m3(Path dir) throws IOException, InterruptedException, FileNotFoundException {
//    if the executor is shut down, creates a new one.
        if (executorServiceM3.isShutdown()) {
            executorServiceM3 = Executors.newWorkStealingPool(cores);
        }
//    calling the third dial on the directory to search through the files
        dial_3(dir);
        executorServiceM3.shutdown();
        executorServiceM3.awaitTermination(1, TimeUnit.MINUTES);

        while (dictionary.isEmpty()) {
            executorServiceM3.shutdown();
        }

        Stats stat;
        stat = new Stats() {
//    implementing the functions from the interface
            @Override
//    returns the number of instances for a number in all the files
            public int occurrences(int number) {
                int occ = 0;
                for (Integer name : dictionary.keySet()) {
                    if (name == number) {
                        occ = dictionary.get(name);
                    }
                }
                return occ;
            }

            @Override
//    returns the most frequent number in all the files
            public int mostFrequent() {
                int freq = Integer.MIN_VALUE;
                int most = 0;

                for (Integer key : dictionary.keySet()) {
                    if (dictionary.get(key) > freq) {
                        freq = dictionary.get(key);
                        most = key;
                    }
                }
                return most;
            }

            @Override
//    returns the least frequent number in all the files
            public int leastFrequent() {
                int freq = Integer.MAX_VALUE;
                int least = 0;

                for (Integer key : dictionary.keySet()) {
                    if (dictionary.get(key) < freq) {
                        freq = dictionary.get(key);
                        least = key;
                    }
                }
                return least;
            }

            @Override
//    returns a list af all files, ordered by the total amount of the values added together
            public List<Path> byTotals() {
                try {
                    dial_4(dir);
                } catch (IOException ex) {
                    Logger.getLogger(Exam.class.getName()).log(Level.SEVERE, null, ex);
                }

                getRes();
                return ResultList;
            }
        };

        return stat;

    }
    
//    visits all subdirectories recursively
//    If a file ends with either ".txt" or ".dat", the constructor is called
    private static void dial_3(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (p.toString().endsWith(".dat")) {
                    Runnable run = new assignment3(p);
                    executorServiceM3.execute(run);

                } else if (p.toString().endsWith(".txt")) {
                    Runnable run = new assignment3(p);
                    executorServiceM3.execute(run);

                } else if (Files.isDirectory(p)) {
                    dial_3(p);
                }
            }
        }
    }

//    visits all subdirectories recursively
//    If a file ends with either ".txt" or ".dat", the function getTotal() is called
    private static void dial_4(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                if (p.toString().endsWith(".dat")) {
                    getTotal(p);

                } else if (p.toString().endsWith(".txt")) {
                    getTotal(p);

                } else if (Files.isDirectory(p)) {
                    dial_4(p);
                }
            }
        }
    }

//    The function updating the concurrentHashMap, containing the frequency of each number in all the files
    private static void DictMaker(ConcurrentHashMap<Integer, Integer> dict, Path dir) throws FileNotFoundException {
        String str = dir.toString();
        Scanner scan = new Scanner(new File(str));
//    Iterating through each line of the file
        while (scan.hasNextLine()) {
            String next = scan.next();
            
//    Splitting the line by ","
            List<String> strArr = Arrays.asList(next.split(","));
            List<Integer> intArr = new ArrayList<>();

//    converting the String array to int array
            for (String string : strArr) {
                intArr.add(Integer.valueOf(string));
            }
            synchronized (dict) {
//    adding another instance of each number once, synchronized to ensure thread safe
                intArr.forEach((i) -> {
                    Integer bob = dict.get(i);

                    if (!dict.containsKey(i)) {
                        dict.put(i, 1);
                    } else {
                        dict.put(i, bob + 1);
                    }
                });
            }
            scan.nextLine();
        }
    }

//    Getter for the dictionary
    public static void getDict() {
        dictionary.keySet().forEach((name) -> {
            String key = name.toString();
            String value = dictionary.get(name).toString();

            System.out.println(key + " : " + value + " time(s)");
        });

    }

//    function to get the total value of the file
    private static void getTotal(Path dir) throws FileNotFoundException {
        String str = dir.toString();
        total = 0;
        Scanner scan = new Scanner(new File(str));

//    Iterating through each line
        while (scan.hasNextLine()) {
            String next = scan.next();
            
//    Splitting by ","
            List<String> strArr = Arrays.asList(next.split(","));
            List<Integer> intArr = new ArrayList<>();

//    Converting String array to int array
            for (String string : strArr) {
                intArr.add(Integer.valueOf(string));
            }

//            computing the total value of the file
            total += intArr.get(0);
            for (int i = 0; i < intArr.size() - 1; i++) {
                total += intArr.get(i);
            }
            scan.nextLine();
        }
//    synchronizing the result and adding to a list
        synchronized (ResultList3) {
            final int num = total;
            Result result = new Result() {
                @Override
                public Path path() {
                    return dir;
                }

                @Override
                public int number() {
                    return num;
                }
            };
            ResultList3.add(result);
        }
    }

    private static List getRes() {

        for (int i = 0; i < ResultList3.size() - 1; i++) {
            Result result = (Result) ResultList3.get(i);
            dictionary2.putIfAbsent(result.path(), result.number());
        }

        List<Path> AList = new ArrayList<>(dictionary2.keySet());
        Collections.sort(AList, (Path path1, Path path2) -> dictionary2.get(path1) - dictionary2.get(path2));

        for (Path name : AList) {
            ResultList.add(name);
        }
        return ResultList;

    }

}
