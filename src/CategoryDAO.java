import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoryDAO 
{

    public void addCategory(String categoryName) 
    {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setString(1, categoryName);
            statement.executeUpdate();

            System.out.println("Category added successfully.");

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to add category.");
            e.printStackTrace();
        }
    }

    public void getAllCategories() 
    {
        String sql = "SELECT category_id, category_name FROM categories ORDER BY category_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {

            System.out.println("\n--- All Categories ---");
            while (resultSet.next()) 
            {
                System.out.println(
                    resultSet.getInt("category_id") + " | " +
                    resultSet.getString("category_name")
                );
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to fetch categories.");
            e.printStackTrace();
        }
    }

    public boolean categoryExists(int categoryId) 
    {
        String sql = "SELECT 1 FROM categories WHERE category_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {

            statement.setInt(1, categoryId);
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