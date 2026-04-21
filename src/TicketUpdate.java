import java.sql.Timestamp;

public class TicketUpdate 
{
    private int updateId;
    private int ticketId;
    private int updatedBy;
    private String updateNote;
    private Timestamp updatedAt;

    public TicketUpdate(int updateId, int ticketId, int updatedBy, String updateNote, Timestamp updatedAt) 
    {
        this.updateId = updateId;
        this.ticketId = ticketId;
        this.updatedBy = updatedBy;
        this.updateNote = updateNote;
        this.updatedAt = updatedAt;
    }

    public int getUpdateId()
    {
        return updateId;
    }

    public int getTicketId()
    {
        return ticketId;
    }

    public int getUpdatedBy()
    {
        return updatedBy;
    }

    public String getUpdateNote() 
    {
        return updateNote;
    }

    public Timestamp getUpdatedAt() 
    {
        return updatedAt;
    }
}