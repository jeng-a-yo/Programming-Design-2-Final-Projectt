import java.security.SecureRandom;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import Programming_Design_2_Final_Project.*;

public class App {
    public static void main(String[] args) {
        int firstDelay  = 90*1000;
        int secondDelay = 60*1000;

        ArrayList<String> userEmailList = new ArrayList<String>();
        userEmailList.add("c14121048@gs.ncku.edu.tw");
        userEmailList.add("c14126111@gs.ncku.edu.tw");
        userEmailList.add("e14106278@gs.ncku.edu.tw");
        userEmailList.add("f74126351@gs.ncku.edu.tw");

        HashMap<String, Integer> userHashMap = new HashMap<String, Integer>();
        for (String userEmail : userEmailList) {
            userHashMap.put(userEmail, 0);
        }

        System.out.println("Please Choose the Mode: (0: Record, 1: Query)");
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();

        if (mode.equals("0")) {

            System.out.println("Enter Record Mode");
            System.out.println("================================================");

            SendEmail sendEmail = new SendEmail();

            sendEmail.sendNotificationEmail(userEmailList);

            TokenGenerator tg = new TokenGenerator();
            ArrayList<String> generateTokens = tg.generateTokens(4);
            ReceiveEmail re = new ReceiveEmail(generateTokens, userEmailList);

            try {
                Thread.sleep(firstDelay); //first call
                re.firstCall();
                re.outputAttendanceTable();

                Thread.sleep(secondDelay); //second call
                re.secondCall();
                re.addZeros();
                re.outputAttendanceTable();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String directory = "data";
            Path path = Paths.get(System.getProperty("user.dir"), directory);

            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            re.serializeAttendanceTable(directory);

        } else if (mode.equals("1")) {

            Query query = new Query();

            System.out.println("Enter Query Mode");
            System.out.println("================================================");

            String queryData = scanner.nextLine();

            switch (query.parserMode(queryData)) {

                case 0:

                    String[] queryterms = queryData.split("\\s+");
                    String userID = queryterms[0];
                    String signInTime = queryterms[1];

                    System.out.println(query.getAttendanceByIDAndDate(userID, signInTime));
                    break;

                case 1:

                    System.out.println(query.getAttendanceByID(queryData));
                    break;

                case 2:

                    System.out.println(query.getAtendanceByDate(queryData));
                    break;

                case -1:

                    System.out.println("Unknown input, please enter a valid format");
                    break;

            }

        } else {
            System.out.println("Invalid Mode");
        }
    }
}

class TokenGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static ArrayList<String> generateTokens(int n) {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String token = generateRandomAlphanumericToken(6);
            System.out.println(token);
            tokens.add(token);
        }
        return tokens;
    }

    private static String generateRandomAlphanumericToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }

}
