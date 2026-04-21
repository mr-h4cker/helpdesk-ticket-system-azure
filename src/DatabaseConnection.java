import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection 
{

    private static final String URL = "jdbc:sqlserver://deepak-helpdesk-sql.database.windows.net:1433;database=helpdeskdb;user=sqladminuser@deepak-helpdesk-sql;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException 
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
