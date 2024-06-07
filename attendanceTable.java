package Programming_Design_2_Final_Project;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


class AttendanceTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> userIDList = new ArrayList<String>();
    private List<Integer> attendanceList = new ArrayList<Integer>();

    public void addUser(String userID, int attendance) {
        userIDList.add(userID);
        attendanceList.add(attendance);
    }

    public List<String> getUserIDList() {
        return userIDList;
    }

    public List<Integer> getAttendanceList() {
        return attendanceList;
    }

    public void serializeAttendanceTable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String filename = date + ".ser";
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("AttendanceTable has been serialized to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
