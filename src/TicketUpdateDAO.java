import java.sql.Connection;
import java.sql.PreparedStatement;

public class TicketUpdateDAO 
{

    public void addTicketUpdate(int ticketId, int updatedBy, String updateNote)
    {
        String sql = "INSERT INTO ticket_updates (ticket_id, updated_by, update_note) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
             {

            statement.setInt(1, ticketId);
            statement.setInt(2, updatedBy);
            statement.setString(3, updateNote);

            statement.executeUpdate();
            System.out.println("Ticket update added successfully.");

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to add ticket update.");
            e.printStackTrace();
        }
    }
}