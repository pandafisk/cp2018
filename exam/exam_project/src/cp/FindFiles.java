package cp;

import java.io.File;
import java.nio.file.Path;

public class FindFiles implements Result {

    public static int countD = 0;
    public static int countF = 0;

    public static void main(String[] args) {
        File[] mappe = new File("C:\\Users\\Troels\\Documents\\GitHub\\cp2018-f\\exam\\data_example").listFiles();
        System.out.println(mappe);
        findTxt(mappe);
        System.out.println("Directories: " + countD);
        System.out.println(".txt - Files: " + countF);
    }

    public static void findTxt(File[] files) {

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory- " + file.getAbsolutePath());
                findTxt(file.listFiles());
                countD++;
            } else if (file.getName().endsWith(".txt")) {
                System.out.println("File- " + file.getName());
                countF++;
                
                
                
            }
        }

    }

    @Override
    public Path path() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int number() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
