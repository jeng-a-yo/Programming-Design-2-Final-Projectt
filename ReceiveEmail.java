package Programming_Design_2_Final_Project;

import java.util.HashMap;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;


public class ReceiveEmail {
    // public static void main(String[] args) {

    // FetchEmail rc = new FetchEmail();
    // }
}

class FetchEmail {
    private HashMap<String, String> attendanceRecord = new HashMap<>();
    private String passwordStd;
    private String pop3Host = "pop.gmail.com";
    // private String storeType = "pop3";
    private String user = "albert920507@gmail.com";
    private String password = "rdbdkmtwafxakdpn";

    public FetchEmail() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3.host", pop3Host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        try {
            Store store = emailSession.getStore("pop3s");
            store.connect(pop3Host, user, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            String passwordProf = "niuewfnw"; // change accordingly
            System.out.println("Correct Password: " + passwordProf);
            System.out.println("============================");

            for (int i = 0; i < messages.length; i++) {
                passwordStd = new String();

                // first record (attend on time)
                verifyPassword(messages[i], passwordProf);

                // second record (late to class)
                // recordLate(messages[i], passwordProf);
            }

            System.out.println("============================");
            for (String s : attendanceRecord.keySet()) {
                System.out.println("StudentID: " + s + ",Status: " + attendanceRecord.get(s));
            }

            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyPassword(Part p, String passwordProf) throws Exception {
        String studentID = new String();

        if (p instanceof Message) {
            studentID = fetchInfo((Message) p);
        }
        if (!studentID.equals("NotValid")) {
            readContent(p, studentID);
            // System.out.println("passwordStd: " + passwordStd);
            System.out.print("studentID:" + studentID);
            System.out.print(",passwordStd: [" + passwordStd.trim() + "]\n");

            if (passwordStd.trim().equals(passwordProf)) {
                System.out.println("PASSWORD MATCH!!");
                attendanceRecord.put(studentID, "ATTENDED");
            } else {
                System.out.println("PASSWORD DIDN'T MATCH!!");
                attendanceRecord.put(studentID, "ABSENT");
            }
        }

    }

    public void recordLate(Part p, String passwordProf) throws Exception {
        String studentID = new String();

        if (p instanceof Message) {
            studentID = fetchInfo((Message) p);
        }

        if (!studentID.equals("NotValid")) {
            readContent(p, studentID);
            System.out.println("passwordStd: " + passwordStd);
            System.out.print("studentID:" + studentID);
            System.out.print(",passwordStd: [" + passwordStd.trim() + "]\n");

            if (passwordStd.trim().equals(passwordProf)) {
                System.out.println("PASSWORD MATCH BUT LATE!!!");
                if (attendanceRecord.keySet().contains(studentID)) {
                    attendanceRecord.remove(studentID);
                    attendanceRecord.put(studentID, "LATE");
                } else {
                    attendanceRecord.put(studentID, "LATE");
                }

            } else {
                System.out.println("PASSWORD DOESN'T MATCH!!");
                attendanceRecord.put(studentID, "ABSENT");
            }
        }

    }

    public void readContent(Part p, String studentID) throws Exception {

        if (p.isMimeType("text/plain")) {
            String content = (String) p.getContent();
            String target = studentID + ":";
            if (content.indexOf(target) != -1) {
                int start = content.indexOf(target);
                int end = content.indexOf("\n", start);
                String temp = content.substring(start, end);
                passwordStd = temp.substring(temp.indexOf(":") + 1);
            }
            return;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                readContent(mp.getBodyPart(i), studentID);
            }
        } else if (p.isMimeType("message/rfc822")) {
            readContent((Part) p.getContent(), studentID);
        }

    }

    public String fetchInfo(Message m) throws Exception {
        Address[] a;
        String studentID = new String();

        if (m.getSubject().equals("Re: 點名")) {
            // System.out.println("SUCCESS");
            a = m.getFrom();
            for (Address address : a) {
                if (address instanceof InternetAddress) {
                    String email = ((InternetAddress) address).getAddress();
                    int at = email.indexOf("@");
                    studentID = email.substring(0, at);
                }
            }
        } else {
            // System.out.println("FAILED");
            return "NotValid";
        }

        return studentID;
    }

}
