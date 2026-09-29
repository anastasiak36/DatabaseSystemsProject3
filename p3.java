// connection_url is supplied at runtime because course teams may use different Oracle environments.
// Examples: WPI CS Oracle csorcl.cs.wpi.edu:1521/orcl.cs.wpi.edu
//           Oracle XE     localhost:1521/XEPDB1 (or your configured XE service)
//           Oracle FreeSQL db.freesql.com:1521/<service_name> (retrieve by clicking on the Connection button)
// CS3431 A26 Project Phase 3 - Java starter
// Team members:

import java.sql.*;
import java.util.Scanner;

public class p3 {
    static void reportEquipment(Connection connection, Scanner input) throws SQLException {
        System.out.println("Enter Equipment ID:");
        int equipID = input.nextInt();

        try {
            Statement stmt = connection.createStatement();
            //figure out how to put the acc equipID var into where
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

            if (equipment_name == "") {
                System.out.println("ERROR: Equipment not found.");
                return;
            }
            //Equipment Information
            //Equipment ID: [equipment_id]
            //Equipment Name: [equipment_name]
            //Category: [category_name]
            //Hourly Rate: $[rate with exactly two decimal places]
            //Status: [equipment_status]
            //Room: [room_name] (Floor [floor_number])

            System.out.println("Equipment Information");
            System.out.println("Equipment ID: " + equipID);
            System.out.println("Equipment Name: " + equipment_name);
            System.out.println("Category: " + category_name);
            System.out.printf("Hourly Rate: %.2f\n", hourly_rate);
            System.out.println("Status: " +  equipment_status);
            System.out.printf("Room: %s (Floor %d)", room_name, floor_number);


        } catch (SQLException e) {
            System.out.println("ERROR: SQL Error");

        }

    }

    static void reportMember(Connection connection, Scanner input) throws SQLException {
        // TODO
    }

    static void reportReservation(Connection connection, Scanner input) throws SQLException {
        // TODO
    }

    static void updateMemberPhone(Connection connection, Scanner input) throws SQLException {
        // TODO
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
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
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

        } catch (SQLException e) {
            System.out.println("ERROR: Unable to connect to database.");
            System.out.println("Oracle error: " + e.getMessage());
            e.printStackTrace();
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
