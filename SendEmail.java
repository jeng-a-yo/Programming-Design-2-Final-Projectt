package Programming_Design_2_Final_Project;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    private HashMap<String, String> attendanceRecord = new HashMap<>(); //TODO unused?
    private String sender = "albert920507@gmail.com";
    private String username = "albert920507";
    private String password = "rdbdkmtwafxakdpn";
    private String smtpHost = "smtp.gmail.com";
    private String smtpPort = "587";

    //sending the email 
    void compose(String recipient, String body){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        Session emailSession = Session.getInstance(properties,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            MimeMessage message = new MimeMessage(emailSession);
            message.setFrom(sender);
            message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(recipient));
            message.setSubject("點名");
            message.setText("Hello, world!\n");
            Transport.send(message, sender, password);
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
    void sendNotificationEmail(List<String> recipientList){ //TODO: probably change type?
        ExecutorService executor = Executors.newCachedThreadPool();
        String body = "Please reply to this email with the attendance code.\n" +
            "Please respond within 3 minutes. The correct code is case-sensitive.";
        for(String recipient : recipientList){
            executor.submit(()-> compose(recipient, body));
        }
        executor.shutdown();
        try{
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    void sendReplyEmail(String recipient, int status){
        String body = null;
        switch (status) {
            case -1:
                //wrong
                body = "The attendance code you provided is incorrect. Please try again with the correct code.";
                break;
            
            case 0:
                //two wrong, absent
                body = "The attendance code you provided is incorrect again. You are marked as absent.";
                break;
            
            case 1:
                //correct, on time
                body = "Your attendance code is correct. You are marked as Present.";
                break;
            
            case 2:
                //correct after one wrong, late
                body = "Your attendance code is correct. You are marked as Late.";
                break;
        }
        compose(recipient, body);
    }
    void sendTimeoutEmail(String recipient){
        //timeout, absent
        String body = "You did not reply to the attendance code on time. You are marked as absent.";
        compose(recipient, body);
    }
}
