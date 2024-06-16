import java.security.SecureRandom;
import java.io.*;
import java.util.*;

public class App{
    public static void main(String[] args) {

        ArrayList<String> userEmailList = new ArrayList<String>();

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
            ReceiveEmail re = new ReceiveEmail(generateTokens);

            re.firstCall();
            try {
                Thread.sleep(180 * 1000);
                re.secondCall();
                re.addZeros();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String directory = "data";
            Path path = Paths.get(System.getProperty("user.dir"), "Programming_Design_2_Final_Project", directory);

            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            re.serializeAttendanceTable(directory);
            
            



        }else if (mode.equals("1")) {
    
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
                
        }else {
            System.out.println("Invalid Mode");
        }
    }
}


class TokenGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static ArrayList<String> generateTokens(int n) {
        ArrayList tokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String token = generateRandomAlphanumericToken(6);
            tokens.add(token)
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
