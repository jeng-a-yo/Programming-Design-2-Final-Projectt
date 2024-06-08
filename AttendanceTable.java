package Programming_Design_2_Final_Project;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class AttendanceTable implements Serializable {
    private Map<String, Integer> attendanceMap = new HashMap<>();
    private Map<String, String> signInTimeMap = new HashMap<>();

    public void addUser(String userID, int attendance) {
        attendanceMap.put(userID, attendance);
        if (attendance == 0){
            signInTimeMap.put(userID, null);
        }else{
            String signInTime = getCurrentTime();
            signInTimeMap.put(userID, signInTime);
        }
        
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public Set<String> getUserIDSet() {
        return attendanceMap.keySet();
    }

    public Collection<Integer> getAttendanceList() {
        return attendanceMap.values();
    }

    public Integer getAttendance(String userID) {
        return attendanceMap.get(userID);
    }
    
    public String getSignInTime(String userID) {
        return signInTimeMap.get(userID);
    }

    public void serializeAttendanceTable(String directory) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        String filename = date + ".ser";
        
        // Ensure the directory exists
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, filename)))) {
            oos.writeObject(this);
            System.out.println("AttendanceTable has been serialized to " + filename + " in directory " + directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AttendanceTable deserializeAttendanceTable(String directory, String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(directory, filename)))) {
            return (AttendanceTable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
