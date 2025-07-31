package Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class ConsoleUtils {

    public static String readNonEmpty(BufferedReader console, String prompt) throws IOException {
        System.out.println(prompt);
        String line = console.readLine();
        while(validation(line)){
            System.out.println("invalid type,try again");
            line = console.readLine();
        }
        return line.trim();
    }


    public static void  writeLine(Object input) throws IOException {
        switch (input) {
            case String s -> System.out.println(input);
            case BufferedReader bufferedReader -> {
                String msg = bufferedReader.readLine();
                System.out.println(msg);
            }
            case Set<?> objects -> {
                for (Object o : objects) {
                    System.out.println(o);
                }
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + input.getClass());
        }
    }

    private static boolean validation(String line){
        return line == null||line.isEmpty() || line.replaceAll(" ", "").isEmpty();
    }
}
