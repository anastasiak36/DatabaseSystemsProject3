// connection_url is supplied at runtime because course teams may use different Oracle environments.
// Examples: WPI CS Oracle csorcl.cs.wpi.edu:1521/orcl.cs.wpi.edu
//           Oracle XE     localhost:1521/XEPDB1 (or your configured XE service)
//           Oracle FreeSQL db.freesql.com:1521/<service_name> (retrieve by clicking on the Connection button)
// CS3431 A26 Project Phase 3 - Java starter
// Team members:

import java.sql.*;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class p3 {
    static void reportEquipment(Connection connection, Scanner input) throws SQLException {
        System.out.println("Enter Equipment ID:");
        int equipID = input.nextInt();

        String str = "SELECT e.equipment_name, e.category_name, e.hourly_rate, e.equipment_status, r.room_name, r.floor_number FROM equipment e JOIN room r ON e.room_id = r.room_id WHERE e.equipment_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1, equipID);

        ResultSet rset = pstmt.executeQuery();
        String equipment_name ="";
        String category_name = "";
        float hourly_rate = 0;
        String equipment_status = "";
        String room_name = "";
        int floor_number = 0;


        while (rset.next()) {
            equipment_name = rset.getString("equipment_name");
            category_name = rset.getString("category_name");
            hourly_rate = rset.getFloat("hourly_rate");
            equipment_status = rset.getString("equipment_status");
            room_name = rset.getString("room_name");
            floor_number = rset.getInt("floor_number");
        }

        if (equipment_name.isEmpty()) {
            System.out.println("ERROR: Equipment not found.");
            return;
        }

        System.out.println("Equipment Information");
        System.out.println("Equipment ID: " + equipID);
        System.out.println("Equipment Name: " + equipment_name);
        System.out.println("Category: " + category_name);
        System.out.printf("Hourly Rate: $%.2f\n", hourly_rate);
        System.out.println("Status: " +  equipment_status);
        System.out.printf("Room: %s (Floor %d)\n", room_name, floor_number);

        if (rset != null) {
            rset.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
    }

    static void reportMember(Connection connection, Scanner input) throws SQLException {
        System.out.println("Enter Member Email:");
        String memberEmail = input.nextLine();

        String str = "SELECT p.person_id, p.first_name, p.last_name, p.phone, m.member_level, m.date_joined FROM person p JOIN member m ON p.person_id = m.person_id WHERE p.email = ?";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1, memberEmail);

        ResultSet rset = pstmt.executeQuery();
        int member_id = 0;
        String first_name = "";
        String last_name = "";
        String phone = "";
        String member_level = "";
        String date_joined = "";

        while (rset.next()) {
            member_id = rset.getInt("person_id");
            first_name = rset.getString("first_name");
            last_name = rset.getString("last_name");
            phone = rset.getString("phone");
            member_level = rset.getString("member_level");
            date_joined = rset.getDate("date_joined").toLocalDate().toString();
        }

        if (member_level.isEmpty()) {
            System.out.println("ERROR: Member not found.");
            return;
        }
        System.out.println("Member Information");
        System.out.println("Member ID: " + member_id);
        System.out.println("Full Name: " + first_name + " " + last_name);
        System.out.println("Email: " + memberEmail);
        System.out.println("Phone: " + phone);
        System.out.println("Member Level: " +  member_level);
        System.out.println("Date Joined:  " + date_joined);

        if (rset != null) {
            rset.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }

    }

    static void reportReservation(Connection connection, Scanner input) throws SQLException {
        System.out.println("Enter Reservation ID:");
        int reservationID = input.nextInt();

        String str = "SELECT p.first_name, p.last_name, e.equipment_name, c.category_name, r.start_time, r.end_time, r.reservation_status, p_s.first_name as staff_first, p_s.last_name as staff_last " +
                        "FROM reservation r JOIN member m ON r.member_id = m.person_id JOIN person p ON m.person_id = p.person_id JOIN equipment e ON r.equipment_id = e.equipment_id JOIN equipment_category c ON e.category_name = c.category_name " +
                        "JOIN certification cert ON m.person_id = cert.member_id AND c.category_name = cert.category_name JOIN staff s ON cert.certified_by = s.person_id JOIN person p_s ON s.person_id = p_s.person_id " +
                        "WHERE r.reservation_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1, reservationID);

        ResultSet rset = pstmt.executeQuery();
        String first_name = "";
        String last_name = "";
        String equipment_name = "";
        String category_name = "";
        String start_time = "";
        String end_time = "";
        String reservation_status = "";
        String staff_first = "";
        String staff_last = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (rset.next()) {
            first_name = rset.getString("first_name");
            last_name = rset.getString("last_name");
            equipment_name = rset.getString("equipment_name");
            category_name = rset.getString("category_name");
            start_time = rset.getTimestamp("start_time").toLocalDateTime().format(formatter);
            end_time = rset.getTimestamp("end_time").toLocalDateTime().format(formatter);
            reservation_status = rset.getString("reservation_status");
            staff_first = rset.getString("staff_first");
            staff_last = rset.getString("staff_last");
        }

        if (start_time.isEmpty()) {
            System.out.println("ERROR: Reservation not found.");
            return;
        }

        System.out.println("Reservation Information");
        System.out.println("Reservation ID: " + reservationID);
        System.out.println("Member: " + first_name + " " + last_name);
        System.out.println("Equipment: " + equipment_name);
        System.out.println("Category: " + category_name);
        System.out.println("Start: " + start_time);
        System.out.println("End: " + end_time);
        System.out.println("Status: " + reservation_status);
        System.out.println("Certified By: " + staff_first +  " " + staff_last);

        if (rset != null) {
            rset.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }

    }

    static void updateMemberPhone(Connection connection, Scanner input) throws SQLException {
        System.out.println("Enter Member Email:");
        String email = input.nextLine();
        System.out.println("Enter Updated Phone Number:");
        String phone = input.nextLine();

        String str = "UPDATE person SET phone = ? WHERE email = ? AND person_id IN (SELECT person_id FROM member)";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1, phone);
        pstmt.setString(2, email);

        int rows = pstmt.executeUpdate();
        if (rows < 1 ) {
            System.out.println("ERROR: Member not found.");
        }
        else{
            connection.commit();
            System.out.println("SUCCESS: Member phone number updated.");
        }

        if (pstmt != null) {
            pstmt.close();
        }

    }

    public static void main(String[] args) {
        // make sure everything was provided
        if (args.length < 3) {
            System.out.println("ERROR: Connection URL, username, and password are required.");
            System.out.println("USAGE: java p3 <connection_url> <username> <password> [option]");
            return;
        }

        String connectionUrl = args[0];
        String username = args[1];
        String password = args[2];

        // no option provided so print menu
        if (args.length < 4) {
            System.out.println("1 - Report Equipment Information");
            System.out.println("2 - Report Member Information");
            System.out.println("3 - Report Reservation Information");
            System.out.println("4 - Update Member Phone Number");
            return;
        }

        int option;

        // get option and convert to an integer
        try {
            option = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid option. Enter 1, 2, 3, or 4.");
            return;
        }

        // option must be 1-4
        if (option < 1 || option > 4) {
            System.out.println("ERROR: Invalid option. Enter 1, 2, 3, or 4.");
            return;
        }

        Connection connection = null;

        try {
            // register the Oracle driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e){
            System.out.println("ERROR: Unable to connect to database.");
            return;
        }

        // connect to the database
        try {

           //add jdbc prefix
            String jdbcUrl = "jdbc:oracle:thin:@" + connectionUrl;

            connection = DriverManager.getConnection(
                    jdbcUrl,
                    username,
                    password
            );

            connection.setAutoCommit(false);

        } catch (SQLException e) {
            System.out.println("ERROR: Unable to connect to database.");
            return;
        }

        // do the operation
        try {
            Scanner input = new Scanner(System.in);

            switch (option) {
                case 1:
                    reportEquipment(connection, input);
                    break;

                case 2:
                    reportMember(connection, input);
                    break;

                case 3:
                    reportReservation(connection, input);
                    break;

                case 4:
                    updateMemberPhone(connection, input);
                    break;
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("ERROR: Database operation failed.");
        }
    }
}
