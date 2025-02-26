import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class entr {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/entr_db";
    private static final String USER = "root";
    private static final String PASSWORD = "codexnii";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(entr::createLoginGUI);
    }

    private static void createLoginGUI() {
        JFrame frame = new JFrame("ENTR - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (loginUser(username, password)) {
                statusLabel.setText("Login successful!");
                frame.dispose(); // Close login window
                createMainGUI(); // Open main event tracker GUI
            } else {
                statusLabel.setText("Login failed. Check credentials.");
            }
        });

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (registerUser(username, password)) {
                statusLabel.setText("Registration successful!");
            } else {
                statusLabel.setText("User already exists.");
            }
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void createMainGUI() {
        JFrame frame = new JFrame("ENTR - Event Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,750);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Welcome to ENTR", SwingConstants.CENTER);
        JButton connectButton = new JButton("Connect to Database");
        JLabel statusLabel = new JLabel("Status: Not Connected", SwingConstants.CENTER);

        connectButton.addActionListener(e -> {
            if (connectToDatabase()) {
                statusLabel.setText("Status: Connected to MySQL");
            } else {
                statusLabel.setText("Status: Connection Failed");
            }
        });

        panel.add(label);
        panel.add(connectButton);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean connectToDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MySQL successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean registerUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Check if user already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

            // Insert new user
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean loginUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If there is a match, login is successful
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }
}
