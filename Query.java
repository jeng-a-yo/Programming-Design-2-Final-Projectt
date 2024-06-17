package Programming_Design_2_Final_Project;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;


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

    
    public String getAttendanceByIDAndDate(String userID, String date) {

        String dictionary = "data";
        String filename = date + ".ser";
        String filePath = dictionary + File.separator + filename;

        
        if (!Files.exists(Paths.get(filePath))) {
            return "Invalid date";
        }

        AttendanceTable table = new AttendanceTable();
        table.deserializeAttendanceTable(dictionary, filename);

        int status = table.getAttendance(userID);

        return attendanceConversion(status) + table.getSignInTime(userID);
    }



    public String getAttendanceByID(String userID) {

        String dictioinary = "data";
        
        AttendanceTable table = new AttendanceTable();
        
        File folder = new File(dictioinary); 
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            
            if (files != null) {

                int expectedAttendance = files.length;
                Double attendanceSum = 0.0;
                int lateCount = 0;
                StringBuilder output = new StringBuilder();
                

                for (File file : files) {

                    String filename = file.getName();
                    table.deserializeAttendanceTable(dictioinary, filename);
                    
                    if (file.isFile() && filename.matches("\\d{4}-\\d{2}-\\d{2}.ser")) {

                        int idxOfdot = filename.indexOf(".");
                        String date = filename.substring(0, idxOfdot - 1); 
                        int status = table.getAttendance(userID);

                        if (status != 0 ){
                            attendanceSum += 1;

                            if (status == 2) {
                                lateCount += 1;
                            }
                        }

                        output.append(date + ":" + attendanceConversion(status) + "\n");
                        // 2024-06-08 : --absent--
                    }
                }
                
                Double attendancePercentage = attendanceSum / expectedAttendance;
                String attendancePercentageString = "Attendance percentage : " + Double.toString(attendancePercentage)  + "(Late : " + lateCount + ")";
                return output.toString() + "\n" + attendancePercentageString;

            } else {
                return ("The folder is empty or an error occurred.");
            }
        } else {
            return ("The provided path is not a directory.");
        }

    }


    public String getAtendanceByDate (String date) {

        String dictioinary = "data";
        String filename = date + ".ser";

        AttendanceTable table = new AttendanceTable();
        table.deserializeAttendanceTable(dictioinary, filename);

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


    public String attendanceConversion(int status){

        switch(status) {
            case 0:
                return "Absent";
            case 1:
                return "Present";
            case 2:
                return "Late";
            default:
                return "Unknown";
        } 
    }


}


