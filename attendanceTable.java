package Programming_Design_2_Final_Project;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

class AttendanceTable implements Serializable {
    private Map<String, Integer> attendanceMap = new HashMap<>();

    public void addUser(String userID, int attendance) {
        attendanceMap.put(userID, attendance);
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

    public static AttendanceTable deserializeAttendanceTable(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (AttendanceTable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
