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
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (loginUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Close login window
                createMainGUI(); // Open main event tracker GUI
            } else {
                statusLabel.setText("Login failed. Check credentials.");
            }
        });

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
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
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Increased rows to accommodate the new button
    
        JLabel label = new JLabel("Welcome to ENTR", SwingConstants.CENTER);
        JButton connectButton = new JButton("Connect to Database");
        JButton createEventButton = new JButton("Create Event"); // New button for event creation
        JLabel statusLabel = new JLabel("Status: Not Connected", SwingConstants.CENTER);
    
        connectButton.addActionListener(e -> {
            if (connectToDatabase()) {
                statusLabel.setText("Status: Connected to MySQL");
            } else {
                statusLabel.setText("Status: Connection Failed");
            }
        });
    
        createEventButton.addActionListener(e -> createEventGUI()); // Calls the method to open the event creation window
    
        panel.add(label);
        panel.add(connectButton);
        panel.add(createEventButton); // Adding the new button to the panel
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
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

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

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }

    private static void createEventGUI() {
        JFrame frame = new JFrame("Create Event");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        JTextField eventNameField = new JTextField();
        JTextField eventDateField = new JTextField();
        JTextField eventLocationField = new JTextField();
        JTextField eventStartTimeField = new JTextField();
        JTextField eventEndTimeField = new JTextField();
        JTextArea eventDescriptionArea = new JTextArea();
        JButton saveButton = new JButton("Save Event");

        saveButton.addActionListener(e -> {
            String name = eventNameField.getText().trim();
            String date = eventDateField.getText().trim();
            String location = eventLocationField.getText().trim();
            String startTime = eventStartTimeField.getText().trim();
            String endTime = eventEndTimeField.getText().trim();
            String description = eventDescriptionArea.getText().trim();

            if (name.isEmpty() || date.isEmpty() || location.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (saveEvent(name, date, location, startTime, endTime, description)) {
                JOptionPane.showMessageDialog(frame, "Event saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to save event.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Event Name:"));
        panel.add(eventNameField);
        panel.add(new JLabel("Date:"));
        panel.add(eventDateField);
        panel.add(new JLabel("Location:"));
        panel.add(eventLocationField);
        panel.add(new JLabel("Time Start:"));
        panel.add(eventStartTimeField);
        panel.add(new JLabel("Time End:"));
        panel.add(eventEndTimeField);
        panel.add(new JLabel("Description:"));
        panel.add(eventDescriptionArea);
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean saveEvent(String name, String date, String location, String startTime, String endTime, String description) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO events (name, date, location, start_time, end_time, description) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.setString(3, location);
            stmt.setString(4, startTime);
            stmt.setString(5, endTime);
            stmt.setString(6, description);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Event save failed: " + e.getMessage());
            return false;
        }
    }
}
