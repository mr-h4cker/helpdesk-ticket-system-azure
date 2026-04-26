import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;

public class ApiServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/health", new HealthHandler());
        server.createContext("/api/tickets", new TicketsHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("API running on port 8080...");
    }

    static class HealthHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            send(exchange, "Help Desk API is running!");
        }
    }

    static class TicketsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder json = new StringBuilder();

            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT TOP 10 * FROM tickets")) {

                ResultSetMetaData meta = rs.getMetaData();
                int columns = meta.getColumnCount();

                json.append("[");
                boolean firstRow = true;

                while (rs.next()) {
                    if (!firstRow) {
                        json.append(",");
                    }
                    firstRow = false;

                    json.append("{");

                    for (int i = 1; i <= columns; i++) {
                        if (i > 1) {
                            json.append(",");
                        }

                        String column = meta.getColumnName(i);
                        String value = rs.getString(i);

                        json.append("\"")
                            .append(escapeJson(column))
                            .append("\":");

                        json.append("\"")
                            .append(value == null ? "" : escapeJson(value))
                            .append("\"");
                    }

                    json.append("}");
                }

                json.append("]");

            } catch (Exception e) {
                json = new StringBuilder();
                json.append("{\"error\":\"")
                    .append(escapeJson(e.getMessage()))
                    .append("\"}");
            }

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            send(exchange, json.toString());
        }
    }

    static String escapeJson(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    static void send(HttpExchange exchange, String response) throws IOException {
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
