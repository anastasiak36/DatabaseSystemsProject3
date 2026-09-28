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
        // TODO
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

        } catch (SQLException e) {
            System.out.println("ERROR: Database operation failed.");

        } finally {
            // close connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
