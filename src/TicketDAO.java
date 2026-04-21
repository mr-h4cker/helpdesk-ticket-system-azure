import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketDAO 
{

    public void createTicket(String title, String description, String status, String priority,
                             int createdBy, Integer assignedTo, int categoryId) 
    {
        String sql = "INSERT INTO tickets (title, description, status, priority, created_by, assigned_to, category_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, status);
            statement.setString(4, priority);
            statement.setInt(5, createdBy);

            if (assignedTo == null) 
            {
                statement.setNull(6, java.sql.Types.INTEGER);
            } 
            else 
            {
                statement.setInt(6, assignedTo.intValue());
            }

            statement.setInt(7, categoryId);

            statement.executeUpdate();
            System.out.println("Ticket created successfully.");

        } 
        catch (Exception e)
        {
            System.out.println("Failed to create ticket.");
            e.printStackTrace();
        }
    }

    public void viewAllTickets() 
    {
        String sql =
            "SELECT " +
            "ticket.ticket_id, " +
            "ticket.title, " +
            "ticket.status, " +
            "ticket.priority, " +
            "creator_user.full_name AS created_by, " +
            "assigned_technician.full_name AS assigned_to, " +
            "ticket_category.category_name, " +
            "ticket.created_at " +
            "FROM tickets AS ticket " +
            "JOIN users AS creator_user ON ticket.created_by = creator_user.user_id " +
            "LEFT JOIN users AS assigned_technician ON ticket.assigned_to = assigned_technician.user_id " +
            "JOIN categories AS ticket_category ON ticket.category_id = ticket_category.category_id " +
            "ORDER BY ticket.ticket_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {

            System.out.println("\n--- All Tickets ---");
            while (resultSet.next()) 
            {
                String assignedTo = resultSet.getString("assigned_to");
                if (assignedTo == null) 
                {
                    assignedTo = "Unassigned";
                }

                System.out.println(
                    resultSet.getInt("ticket_id") + " | " +
                    resultSet.getString("title") + " | " +
                    resultSet.getString("status") + " | " +
                    resultSet.getString("priority") + " | Created by: " +
                    resultSet.getString("created_by") + " | Assigned to: " +
                    assignedTo + " | Category: " +
                    resultSet.getString("category_name") + " | " +
                    resultSet.getTimestamp("created_at")
                );
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to fetch tickets.");
            e.printStackTrace();
        }
    }

    public void viewOpenTickets() 
    {
        String sql =
            "SELECT " +
            "ticket.ticket_id, " +
            "ticket.title, " +
            "ticket.priority, " +
            "creator_user.full_name AS created_by, " +
            "assigned_technician.full_name AS assigned_to, " +
            "ticket_category.category_name, " +
            "ticket.created_at " +
            "FROM tickets AS ticket " +
            "JOIN users AS creator_user ON ticket.created_by = creator_user.user_id " +
            "LEFT JOIN users AS assigned_technician ON ticket.assigned_to = assigned_technician.user_id " +
            "JOIN categories AS ticket_category ON ticket.category_id = ticket_category.category_id " +
            "WHERE ticket.status = 'open' " +
            "ORDER BY ticket.created_at";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {

            System.out.println("\n--- Open Tickets ---");
            while (resultSet.next()) 
            {
                String assignedTo = resultSet.getString("assigned_to");
                if (assignedTo == null) 
                {
                    assignedTo = "Unassigned";
                }

                System.out.println(
                    resultSet.getInt("ticket_id") + " | " +
                    resultSet.getString("title") + " | Priority: " +
                    resultSet.getString("priority") + " | Created by: " +
                    resultSet.getString("created_by") + " | Assigned to: " +
                    assignedTo + " | Category: " +
                    resultSet.getString("category_name") + " | " +
                    resultSet.getTimestamp("created_at")
                );
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to fetch open tickets.");
            e.printStackTrace();
        }
    }

    public void assignTicket(int ticketId, int technicianId) 
    {
        String sql = "UPDATE tickets SET assigned_to = ? WHERE ticket_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setInt(1, technicianId);
            statement.setInt(2, ticketId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) 
            {
                System.out.println("Ticket assigned successfully.");
            } 
            else 
            {
                System.out.println("No ticket found with that ID.");
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to assign ticket.");
            e.printStackTrace();
        }
    }

    public void updateTicketStatus(int ticketId, String newStatus) 
    {
        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setString(1, newStatus);
            statement.setInt(2, ticketId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) 
            {
                System.out.println("Ticket status updated successfully.");
            } 
            else 
            {
                System.out.println("No ticket found with that ID.");
            }

        } 
        catch (Exception e)
        {
            System.out.println("Failed to update ticket status.");
            e.printStackTrace();
        }
    }

    public boolean ticketExists(int ticketId) 
    {
        String sql = "SELECT 1 FROM tickets WHERE ticket_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
             {

            statement.setInt(1, ticketId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public void viewTicketDetails(int ticketId) 
    {
        String ticketSql =
            "SELECT " +
            "ticket.ticket_id, " +
            "ticket.title, " +
            "ticket.description, " +
            "ticket.status, " +
            "ticket.priority, " +
            "creator_user.full_name AS created_by, " +
            "assigned_technician.full_name AS assigned_to, " +
            "ticket_category.category_name, " +
            "ticket.created_at " +
            "FROM tickets AS ticket " +
            "JOIN users AS creator_user ON ticket.created_by = creator_user.user_id " +
            "LEFT JOIN users AS assigned_technician ON ticket.assigned_to = assigned_technician.user_id " +
            "JOIN categories AS ticket_category ON ticket.category_id = ticket_category.category_id " +
            "WHERE ticket.ticket_id = ?";

        String updateSql =
            "SELECT " +
            "ticket_update.update_id, " +
            "updating_user.full_name AS updated_by, " +
            "ticket_update.update_note, " +
            "ticket_update.updated_at " +
            "FROM ticket_updates AS ticket_update " +
            "JOIN users AS updating_user ON ticket_update.updated_by = updating_user.user_id " +
            "WHERE ticket_update.ticket_id = ? " +
            "ORDER BY ticket_update.updated_at";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ticketStatement = connection.prepareStatement(ticketSql);
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) 
        {

            ticketStatement.setInt(1, ticketId);
            ResultSet ticketResult = ticketStatement.executeQuery();

            if (ticketResult.next()) 
            {
                String assignedTo = ticketResult.getString("assigned_to");
                if (assignedTo == null) 
                {
                    assignedTo = "Unassigned";
                }

                System.out.println("\n--- Ticket Details ---");
                System.out.println("Ticket ID: " + ticketResult.getInt("ticket_id"));
                System.out.println("Title: " + ticketResult.getString("title"));
                System.out.println("Description: " + ticketResult.getString("description"));
                System.out.println("Status: " + ticketResult.getString("status"));
                System.out.println("Priority: " + ticketResult.getString("priority"));
                System.out.println("Created By: " + ticketResult.getString("created_by"));
                System.out.println("Assigned To: " + assignedTo);
                System.out.println("Category: " + ticketResult.getString("category_name"));
                System.out.println("Created At: " + ticketResult.getTimestamp("created_at"));

                updateStatement.setInt(1, ticketId);
                ResultSet updateResult = updateStatement.executeQuery();

                System.out.println("\n--- Ticket Updates ---");
                boolean hasUpdates = false;
                while (updateResult.next()) 
                {
                    hasUpdates = true;
                    System.out.println(
                        updateResult.getInt("update_id") + " | " +
                        updateResult.getString("updated_by") + " | " +
                        updateResult.getString("update_note") + " | " +
                        updateResult.getTimestamp("updated_at")
                    );
                }

                if (!hasUpdates) 
                {
                    System.out.println("No updates found for this ticket.");
                }

            } 
            else 
            {
                System.out.println("No ticket found with that ID.");
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Failed to fetch ticket details.");
            e.printStackTrace();
        }
    }
}