import java.sql.*;
import java.util.Scanner;

public class RestaurantBillGenerator {

    // JDBC URL, username, and password of MySQL server
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/Restaurant";
    static final String JDBC_USER = "root"; // replace with your MySQL username
    static final String JDBC_PASSWORD = "tiger"; // replace with your MySQL password

    // Connection and statement objects
    static Connection connection = null;
    static Statement statement = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Establish the connection
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            statement = connection.createStatement();

            while (true) {  // Infinite loop to keep the process running for new and existing customers
                // Handle customer logic (new or existing)
                int customerId = handleCustomer(sc);

                // Show menu items to the customer
                displayMenu();

                // Take customer order
                System.out.print("Enter the number of items you want to order: ");
                int itemCount = sc.nextInt();
                double totalBill = 0;

                for (int i = 0; i < itemCount; i++) {
                    System.out.print("Enter the products ID of item " + (i + 1) + ": ");
                    int productId = sc.nextInt();

                    System.out.print("Enter the quantity of the item: ");
                    int quantity = sc.nextInt();

                    // Fetch product details from the menu table
                    String query = "SELECT product_name, price FROM menu WHERE product_id =  " + productId;
                    ResultSet rs = statement.executeQuery(query);

                    if (rs.next()) {
                        String productName = rs.getString("product_name");
                        double price = rs.getDouble("price");
                        double subtotal = price * quantity;
                        totalBill += subtotal;

                        // Insert new order into bill table for the existing customer
                        insertIntoBill(customerId, productId, productName, quantity, price, subtotal);
                    } else {
                        System.out.println("Invalid product ID.");
                    }
                }

                // Fetch the current bill for the customer, including old and new orders
                generateBill(customerId);

                // After displaying the bill, thank the customer and restart the process
                System.out.println("\nThank you for your order! Starting next customer process...\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        sc.close();
    }

    // Function to handle both new and returning customers
    public static int handleCustomer(Scanner sc) throws SQLException {
        System.out.print("Is this an existing customer? (yes/no): ");
        sc.next();  // Consume newline
        String response = sc.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.println("Enter the customer name: ");
            String customerName = sc.nextLine();

            // Check if customer exists in the database
            String checkQuery = "SELECT customer_id FROM customer WHERE customer_name = ?";
            PreparedStatement ps = connection.prepareStatement(checkQuery);
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Customer exists, return the existing customer_id
                int customerId = rs.getInt("customer_id");
                System.out.println("Welcome back, " + customerName + "! Your Customer ID is " + customerId);
                return customerId;
            } else {
                // Customer does not exist, handle as a new customer
                System.out.println("Customer not found, treating as new customer.");
                return createNewCustomer(sc, customerName);
            }
        } else {
            // If customer is new, create a new customer
            return createNewCustomer(sc, null);
        }
    }

    // Create a new customer and return the customer_id
    public static int createNewCustomer(Scanner sc, String existingCustomerName) throws SQLException {
        String customerName;

        if (existingCustomerName != null) {
            customerName = existingCustomerName;
        } else {
            System.out.print("Enter the customer name: ");
            customerName = sc.nextLine();
        }

        // Insert customer name into the customer table (customer_id will be auto-generated)
        String insertCustomerQuery = "INSERT INTO customer (customer_name) VALUES (?)";
        PreparedStatement ps = connection.prepareStatement(insertCustomerQuery, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, customerName);
        ps.executeUpdate();

        // Retrieve the generated customer_id
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int customerId = rs.getInt(1);  // Retrieve the generated customer_id
            System.out.println("Customer ID generated: " + customerId);
            return customerId;
        } else {
            throw new SQLException("Error generating customer ID.");
        }
    }

    // Display the menu
    public static void displayMenu() throws SQLException {
        String query = "SELECT * FROM menu";
        ResultSet rs = statement.executeQuery(query);

        System.out.println("---- Menu ----");
        while (rs.next()) {
            int id = rs.getInt("product_id");
            String name = rs.getString("product_name");
            double price = rs.getDouble("price");
            System.out.println(id + ". " + name + " - ₹" + price);
        }
    }

    // Insert order details into the bill table
    public static void insertIntoBill(int customerId, int productId, String productName, int quantity, double price, double subtotal) throws SQLException {
        String insertQuery = "INSERT INTO bill (customer_id, product_id, product_name, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);
        ps.setInt(1, customerId);
        ps.setInt(2, productId);
        ps.setString(3, productName);
        ps.setInt(4, quantity);
        ps.setDouble(5, price);
        ps.setDouble(6, subtotal);

        ps.executeUpdate();
    }

    // Generate the final bill and display it to the customer (includes both old and new items)
    public static void generateBill(int customerId) throws SQLException {
        // Fetch all the orders for the given customer
        String billQuery = "SELECT * FROM bill WHERE customer_id = " + customerId;
        ResultSet rs = statement.executeQuery(billQuery);

        double totalBill = 0;
        System.out.println("\n---- Bill for Customer ID: " + customerId + " ----");
        while (rs.next()) {
            String productName = rs.getString("product_name");
            int quantity = rs.getInt("quantity");
            double price = rs.getDouble("price");
            double subtotal = rs.getDouble("subtotal");
            totalBill += subtotal;
            System.out.println(productName + " x" + quantity + " - ₹" + price + " = ₹" + subtotal);
        }

        // Calculate and display GST
        double gst = totalBill * 0.18;  // 18% GST
        double finalTotal = totalBill + gst;
        System.out.println("\nSubtotal: ₹" + totalBill);
        System.out.println("GST (18%): ₹" + gst);
        System.out.println("Total Bill: ₹" + finalTotal);

        // Optionally, update the total in the bill table
        String updateBillQuery = "UPDATE bill SET total = ? WHERE customer_id = ?";
        PreparedStatement ps = connection.prepareStatement(updateBillQuery);
        ps.setDouble(1, finalTotal);
        ps.setInt(2, customerId);
        ps.executeUpdate();
    }
}
