// package Programming_Design_2_Final_Project;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

import myPackage.*;


class Query {

    public int parserMode(String queryData) {
       
        String idPattern = "^[A-Za-z][A-Za-z0-9]{8}$";
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";

        if (Pattern.matches(idPattern, queryData) && Pattern.matches(datePattern, queryData)) {
            return 0;
        }else if (Pattern.matches(idPattern, queryData)) {
            return 1;
        }else if (Pattern.matches(datePattern, queryData)) {
            return 2;
        }else {
            return -1;
        }

        
    }

    //AttendanceTable table = new AttendanceTable();
    public String getAttendanceByIDAndDate (String userID, String signInTime) {
        
        String dictioinary = "";
        String filename = "";
        String path = dictioinary + filename + ".ser";

        AttendanceTable table = new AttendanceTable();
        table.deserializeAttendanceTable(path);

        int attendance = table.getAttendance(userID);
        
        return attendanceConversion(attendance) + table.getSignInTime(userID);
        
    }


    public String getAttendanceByID(String userID) {

        String dictioinary = "";
        
        AttendanceTable table = new AttendanceTable();
        
        File folder = new File(dictioinary); // 替換為資料夾路徑
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            
            if (files != null) {

                int expectedAttendance = files.length;
                int attendanceSum = 0;
                StringBuilder dateRow = new StringBuilder();
                StringBuilder attendanceRow = new StringBuilder();

                for (File file : files) {

                    String filename = file.getName();
                    table.deserializeAttendanceTable(filename);
                    
                    if (file.isFile() && filename.matches("\\d{4}-\\d{2}-\\d{2}.ser")) {
                        int idxOfdot = filename.indexOf(".");
                        String date = filename.substring(0, idxOfdot - 1); 
                        int attendance = table.getAttendance(userID);

                        attendanceSum += attendance;

                        dateRow.append(date + " ");
                        attendanceRow.append("--" + attendanceConversion(table.getAttendance(userID)) + "-- ");
                        // 2024-06-08
                        // --absent--    
                    }

                }
                
                String attendancePercentage = "Attendance percentage : " + Integer.toString(attendanceSum / expectedAttendance);
                return dateRow.toString() + "\n" + attendanceRow.toString() + "\n" + attendancePercentage;

            } else {
                return ("The folder is empty or an error occurred.");
            }
        } else {
            return ("The provided path is not a directory.");
        }

    }


    public String getAtendanceByDate (String date) {

        String dictioinary = "";
        String filename = "";
        String path = dictioinary + filename + ".ser";

        AttendanceTable table = new AttendanceTable();
        table.deserializeAttendanceTable(path);

        Set<String> UserIDSet = table.getUserIDSet();
        List<String> UserIDs = new ArrayList<String>(UserIDSet);

        Collection<Integer> attendanceList = table.getAttendanceList();
        List<Integer> attendances = new ArrayList<Integer>(attendanceList);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < UserIDSet.size(); i++) {
            String userID = UserIDs.get(i);
            sb.append(userID + " : " + attendanceConversion(attendances.get(i)) + " at " + table.getSignInTime(userID) + "\n");
        }
        
        return sb.toString();
    }


    public String attendanceConversion(int attendance){

        return attendance == 0 ? "Absent" : "Present";
    }


}
