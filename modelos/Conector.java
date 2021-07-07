package modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {

    private static String HOST = "127.0.0.1";
    private static int PORT = 3306;
    private static String DB_NAME = "school2";
    private static String USERNAME = "root";
    private static String PASSWORD = "kjkszpj";

    private static Connection connection;

    public static Connection conectar() throws SQLException{

        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME), USERNAME, PASSWORD);


        return connection;
    }
    
}
