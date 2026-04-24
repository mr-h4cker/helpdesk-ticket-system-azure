import java.util.Scanner;
import java.sql.Connection;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        UserDAO userDAO = new UserDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        TicketDAO ticketDAO = new TicketDAO();
        TicketUpdateDAO ticketUpdateDAO = new TicketUpdateDAO();

        try (Connection connection = DatabaseConnection.getConnection())
        {
            System.out.println("Connected to Azure SQL successfully.");
        } 
        catch (Exception e) 
        {
            System.out.println("Database connection failed.");
            e.printStackTrace();
            return;
        }

        boolean running = true;

        while (running) 
        {
            System.out.println("\n===== IT HELP DESK TICKET MANAGEMENT SYSTEM =====");
            System.out.println("1. View all users");
            System.out.println("2. Add user");
            System.out.println("3. View all categories");
            System.out.println("4. Add category");
            System.out.println("5. Create ticket");
            System.out.println("6. View all tickets");
            System.out.println("7. View open tickets");
            System.out.println("8. Assign ticket");
            System.out.println("9. Update ticket status");
            System.out.println("10. Add ticket update");
            System.out.println("11. View ticket details");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) 
            {
                case "1":
                    userDAO.getAllUsers();
                    break;

                case "2":
                    System.out.print("Enter full name: ");
                    String fullName = scanner.nextLine().trim();

                    System.out.print("Enter email: ");
                    String email = scanner.nextLine().trim();

                    System.out.print("Enter role (employee / technician / admin): ");
                    String role = scanner.nextLine().trim().toLowerCase();

                    if (!role.equals("employee") && !role.equals("technician") && !role.equals("admin")) 
                    {
                        System.out.println("Invalid role.");
                        break;
                    }

                    userDAO.addUser(fullName, email, role);
                    break;

                case "3":
                    categoryDAO.getAllCategories();
                    break;

                case "4":
                    System.out.print("Enter category name: ");
                    String categoryName = scanner.nextLine().trim();
                    categoryDAO.addCategory(categoryName);
                    break;

                case "5":
                    try 
                    {
                        System.out.print("Enter ticket title: ");
                        String title = scanner.nextLine().trim();

                        System.out.print("Enter ticket description: ");
                        String description = scanner.nextLine().trim();

                        System.out.print("Enter status (open / in_progress / closed): ");
                        String status = scanner.nextLine().trim().toLowerCase();

                        if (!status.equals("open") && !status.equals("in_progress") && !status.equals("closed")) 
                        {
                            System.out.println("Invalid status.");
                            break;
                        }

                        System.out.print("Enter priority (low / medium / high): ");
                        String priority = scanner.nextLine().trim().toLowerCase();

                        if (!priority.equals("low") && !priority.equals("medium") && !priority.equals("high"))
                        {
                            System.out.println("Invalid priority.");
                            break;
                        }

                        System.out.print("Enter creator user ID: ");
                        int createdBy = Integer.parseInt(scanner.nextLine());

                        if (!userDAO.userExists(createdBy)) 
                        {
                            System.out.println("Creator user ID does not exist.");
                            break;
                        }

                        System.out.print("Enter assigned technician ID (or press Enter for none): ");
                        String assignedInput = scanner.nextLine().trim();
                        Integer assignedTo = null;

                        if (!assignedInput.isEmpty()) 
                        {
                            assignedTo = Integer.valueOf(assignedInput);
                            if (!userDAO.userExists(assignedTo)) 
                            {
                                System.out.println("Assigned user ID does not exist.");
                                break;
                            }
                        }

                        System.out.print("Enter category ID: ");
                        int categoryId = Integer.parseInt(scanner.nextLine());

                        if (!categoryDAO.categoryExists(categoryId)) 
                        {
                            System.out.println("Category ID does not exist.");
                            break;
                        }

                        ticketDAO.createTicket(title, description, status, priority, createdBy, assignedTo, categoryId);

                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Invalid input.");
                    }
                    break;

                case "6":
                    ticketDAO.viewAllTickets();
                    break;

                case "7":
                    ticketDAO.viewOpenTickets();
                    break;

                case "8":
                    try 
                    {
                        System.out.print("Enter ticket ID: ");
                        int ticketId = Integer.parseInt(scanner.nextLine());

                        if (!ticketDAO.ticketExists(ticketId)) 
                        {
                            System.out.println("Ticket ID does not exist.");
                            break;
                        }

                        System.out.print("Enter technician user ID: ");
                        int technicianId = Integer.parseInt(scanner.nextLine());

                        if (!userDAO.userExists(technicianId)) 
                        {
                            System.out.println("User ID does not exist.");
                            break;
                        }

                        ticketDAO.assignTicket(ticketId, technicianId);

                    }
                    catch (Exception e) 
                    {
                        System.out.println("Invalid input.");
                    }
                    break;

                case "9":
                    try 
                    {
                        System.out.print("Enter ticket ID: ");
                        int ticketId = Integer.parseInt(scanner.nextLine());

                        if (!ticketDAO.ticketExists(ticketId)) 
                        {
                            System.out.println("Ticket ID does not exist.");
                            break;
                        }

                        System.out.print("Enter new status (open / in_progress / closed): ");
                        String newStatus = scanner.nextLine().trim().toLowerCase();

                        if (!newStatus.equals("open") && !newStatus.equals("in_progress") && !newStatus.equals("closed"))
                        {
                            System.out.println("Invalid status.");
                            break;
                        }

                        ticketDAO.updateTicketStatus(ticketId, newStatus);

                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Invalid input.");
                    }
                    break;

                case "10":
                    try 
                    {
                        System.out.print("Enter ticket ID: ");
                        int ticketId = Integer.parseInt(scanner.nextLine());

                        if (!ticketDAO.ticketExists(ticketId)) 
                        {
                            System.out.println("Ticket ID does not exist.");
                            break;
                        }

                        System.out.print("Enter updater user ID: ");
                        int updatedBy = Integer.parseInt(scanner.nextLine());

                        if (!userDAO.userExists(updatedBy)) 
                        {
                            System.out.println("Updater user ID does not exist.");
                            break;
                        }

                        System.out.print("Enter update note: ");
                        String updateNote = scanner.nextLine().trim();

                        ticketUpdateDAO.addTicketUpdate(ticketId, updatedBy, updateNote);

                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Invalid input.");
                    }
                    break;

                case "11":
                    try 
                    {
                        System.out.print("Enter ticket ID: ");
                        int ticketId = Integer.parseInt(scanner.nextLine());
                        ticketDAO.viewTicketDetails(ticketId);

                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Invalid input.");
                    }
                    break;

                case "0":
                    System.out.println("Exiting system.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}