import javax.swing.*;
import java.awt.*;
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
                JOptionPane.showMessageDialog(frame, "Login failed. Check credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        });

        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (loginUser(username, password)) {
                currentUser = username; // Store logged-in user
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                createMainGUI();
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

        frame.add(panel);
        frame.setVisible(true);
    }

    private static String currentUser = null;

    private static void createMainGUI() {
        JFrame frame = new JFrame("ENTR - Event Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Increased rows to accommodate the new button
    
        JLabel label = new JLabel("Welcome to ENTR", SwingConstants.CENTER);
        JButton createEventButton = new JButton("Create Event"); // New button for event creation
        JButton viewMyEventsButton = new JButton("View My Events");
        JButton accountSettingsButton = new JButton("Account Settings");
        JButton logoutButton = new JButton("Logout");
    
    
        createEventButton.addActionListener(e -> createEventGUI()); // Calls the method to open the event creation window

        viewMyEventsButton.addActionListener(e -> viewMyEventsGUI());

        accountSettingsButton.addActionListener(e -> openAccountSettingsGUI());
        logoutButton.addActionListener(e -> logout()); 
    
        panel.add(label);
        panel.add(createEventButton); // Adding the new button to the panel
        panel.add(viewMyEventsButton);
        panel.add(accountSettingsButton);
        panel.add(logoutButton);
    
        frame.add(panel);
        frame.setVisible(true);
    } 
    
    private static void openAccountSettingsGUI() {
        JFrame frame = new JFrame("Account Settings");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
    
        JLabel userLabel = new JLabel("New Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("New Password:");
        JPasswordField passField = new JPasswordField();
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete Account");
    
        updateButton.addActionListener(e -> {
            String newUsername = userField.getText().trim();
            String newPassword = new String(passField.getPassword()).trim();
    
            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (updateUser(newUsername, newPassword)) {
                JOptionPane.showMessageDialog(frame, "Account updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete your account?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteUser()) {
                    JOptionPane.showMessageDialog(frame, "Account deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    System.exit(0); // Exit application
                } else {
                    JOptionPane.showMessageDialog(frame, "Deletion failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(updateButton);
        panel.add(deleteButton);
    
        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean updateUser(String newUsername, String newPassword) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE users SET username = ?, password = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, currentUser); // Use the currently logged-in user
            int rowsUpdated = stmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                currentUser = newUsername; // Update session username
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
        return false;
    }
    
    private static boolean deleteUser() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentUser);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Deletion failed: " + e.getMessage());
        }
        return false;
    }

    private static void logout() {
        currentUser = null; // Clear session
        JOptionPane.showMessageDialog(null, "Logged out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(entr::createLoginGUI); // Return to login
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
    
            if (rs.next()) {
                currentUser = username;  // Store logged-in user
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        return false;
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
        JButton backButton = new JButton("Back");

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

        backButton.addActionListener(e -> {
            frame.dispose(); // Close the Create Event window and return to the main dashboard
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
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean saveEvent(String name, String date, String location, String startTime, String endTime, String description) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Fetch the current user's ID
            String userQuery = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, currentUser);
            ResultSet rs = userStmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("id"); // Get user ID
                
                // Insert event with user_id
                String query = "INSERT INTO events (user_id, event_name, event_date, location, time_start, time_end, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.setString(2, name);
                stmt.setString(3, date);
                stmt.setString(4, location);
                stmt.setString(5, startTime);
                stmt.setString(6, endTime);
                stmt.setString(7, description);
                stmt.executeUpdate();
                return true;
            } else {
                System.out.println("User not found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Event save failed: " + e.getMessage());
            return false;
        }
    }
        
    private static void viewMyEventsGUI() {
        JFrame frame = new JFrame("My Events");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(eventList);
        
        JButton updateButton = new JButton("Update Event");
        JButton backButton = new JButton("Back");
    
        // Fetch user events
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT id, event_name, event_date FROM events WHERE user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                listModel.addElement(rs.getInt("id") + ": " + rs.getString("event_name") + " - " + rs.getString("event_date"));
            }
    
            if (listModel.isEmpty()) {
                listModel.addElement("No event created.");
                updateButton.setEnabled(false); // Disable update if no events
            }
        } catch (SQLException e) {
            System.out.println("Error fetching events: " + e.getMessage());
        }
    
        updateButton.addActionListener(e -> {
            String selectedEvent = eventList.getSelectedValue();
            if (selectedEvent != null && !selectedEvent.equals("No event created.")) {
                int eventId = Integer.parseInt(selectedEvent.split(":")[0]);
                updateEventGUI(eventId);
                frame.dispose();
            }
        });
    
        backButton.addActionListener(e -> frame.dispose());
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
    
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private static void updateEventGUI(int eventId) {
        JFrame frame = new JFrame("Update Event");
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
        JButton updateButton = new JButton("Update Event");
        JButton backButton = new JButton("Back");
    
        // Load existing event details
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT event_name, event_date, location, time_start, time_end, description FROM events WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                eventNameField.setText(rs.getString("event_name"));
                eventDateField.setText(rs.getString("event_date"));
                eventLocationField.setText(rs.getString("location"));
                eventStartTimeField.setText(rs.getString("time_start"));
                eventEndTimeField.setText(rs.getString("time_end"));
                eventDescriptionArea.setText(rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading event: " + e.getMessage());
        }
    
        updateButton.addActionListener(e -> {
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
    
            if (updateEvent(eventId, name, date, location, startTime, endTime, description)) {
                JOptionPane.showMessageDialog(frame, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update event.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        backButton.addActionListener(e -> frame.dispose());
    
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
        panel.add(updateButton);
        panel.add(backButton);
    
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private static boolean updateEvent(int eventId, String name, String date, String location, String startTime, String endTime, String description) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE events SET event_name = ?, event_date = ?, location = ?, time_start = ?, time_end = ?, description = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.setString(3, location);
            stmt.setString(4, startTime);
            stmt.setString(5, endTime);
            stmt.setString(6, description);
            stmt.setInt(7, eventId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Event update failed: " + e.getMessage());
            return false;
        }
    }
}