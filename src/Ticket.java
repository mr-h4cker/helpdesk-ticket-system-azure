import java.sql.Timestamp;

public class Ticket 
{
    private int ticketId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private int createdBy;
    private Integer assignedTo;
    private int categoryId;
    private Timestamp createdAt;

    public Ticket(int ticketId, String title, String description, String status, String priority,
                  int createdBy, Integer assignedTo, int categoryId, Timestamp createdAt) 
    {
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public int getTicketId() 
    {
        return ticketId;
    }

    public String getTitle() 
    {
        return title;
    }

    public String getDescription() 
    {
        return description;
    }

    public String getStatus() 
    {
        return status;
    }

    public String getPriority() 
    {
        return priority;
    }

    public int getCreatedBy() 
    {
        return createdBy;
    }

    public Integer getAssignedTo() 
    {
        return assignedTo;
    }

    public int getCategoryId() 
    {
        return categoryId;
    }

    public Timestamp getCreatedAt() 
    {
        return createdAt;
    }
}