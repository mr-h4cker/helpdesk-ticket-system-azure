import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO 
{

    public void addUser(String fullName, String email, String role) 
    {
        String sql = "INSERT INTO users (full_name, email, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {

            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, role);
            statement.executeUpdate();

            System.out.println("User added successfully.");

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to add user.");
            e.printStackTrace();
        }
    }

    public void getAllUsers() 
    {
        String sql = "SELECT user_id, full_name, email, role FROM users ORDER BY user_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {

            System.out.println("\n--- All Users ---");
            while (resultSet.next()) 
            {
                System.out.println(
                    resultSet.getInt("user_id") + " | " +
                    resultSet.getString("full_name") + " | " +
                    resultSet.getString("email") + " | " +
                    resultSet.getString("role")
                );
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to fetch users.");
            e.printStackTrace();
        }
    }

    public boolean userExists(int userId) 
    {
        String sql = "SELECT 1 FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }
}