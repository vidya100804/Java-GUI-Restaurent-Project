import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class GUI {
    Connection connection = null;

    public GUI() {
        try {
            // Establishment of connection to MySQL database \
            // Using JDBC to connect to the database 'foodmenu' \
            // on a local MySQL server at 'localhost:3306' \
            // with the username 'root' and password 'vidya@123'
            String url = "jdbc:mysql://localhost:3306/foodmenu";
            String username = "root";
            String password = "vidya@123";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace(); // If any SQL exception occurs, print it to the console
        }

        // Creating the main JFrame
        JFrame mainFrame = new JFrame(" ");
        JButton nonVegButton = new JButton("Non-Veg");
        nonVegButton.setBounds(50, 50, 100, 30); // Setting the position and size of the button
        nonVegButton.setFocusable(false); // Disabling focus for the button

        JButton vegButton = new JButton("Veg");
        vegButton.setBounds(160, 50, 100, 30); // Setting the position and size of the button
        vegButton.setFocusable(false); // Disabling focus for the button

        mainFrame.add(nonVegButton); // Adding the Non-Veg button to the main JFrame
        mainFrame.add(vegButton); // Adding the Veg button to the main JFrame

        nonVegButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCategoryFrame("Non-Veg");
            }
        });

        vegButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCategoryFrame("Veg");
            }
        });

        mainFrame.setSize(400, 150); // Setting the size of the main JFrame
        mainFrame.setLayout(null); // Setting layout to null for absolute positioning
        mainFrame.setVisible(true); // Setting the main JFrame visible
    }

    private void createCategoryFrame(String foodType) {
        JFrame categoryFrame = new JFrame(foodType + " Categories");
        JComboBox<String> categoryComboBox = new JComboBox<>();

        String[] vegCategories = {"VegStarters", "VegSoups", "VegMainCourse"};
        String[] nonVegCategories = {"NonVegStarters", "NonVegSoups", "NonvegMainCourse"};

        String[] selectedCategories = (foodType.equals("Veg")) ? vegCategories : nonVegCategories;

        for (String category : selectedCategories) {
            categoryComboBox.addItem(category); // Adding categories to the combo box
        }

        categoryComboBox.setBounds(50, 50, 150, 30); // Setting position and size for the combo box

        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                displayMenuOptions(selectedCategory);
            }
        });

        categoryFrame.add(categoryComboBox); // Adding the combo box to the category JFrame

        categoryFrame.setSize(400, 200); // Setting size of the category JFrame
        categoryFrame.setLayout(null); // Setting layout to null for absolute positioning
        categoryFrame.setVisible(true); // Setting the category JFrame visible
    }

    private void displayMenuOptions(String category) {
        JFrame menuFrame = new JFrame(" ");
        JTextArea menuTextArea = new JTextArea();  
        JScrollPane scrollPane = new JScrollPane(menuTextArea);

        scrollPane.setBounds(60, 60, 300, 250); // Setting position and size for the scroll pane

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT " + category + " FROM foodmenu"; // SQL query to fetch menu items
            ResultSet result = statement.executeQuery(query);

            StringBuilder menuItems = new StringBuilder();
            menuItems.append("MENU\n\n");
            while (result.next()) {
                String menuItem = result.getString(category); // Retrieve menu items
                menuItems.append(menuItem).append("\n");
            }

            menuTextArea.setText(menuItems.toString()); // Display menu items in JTextArea

        } catch (SQLException ex) {
            ex.printStackTrace(); // Print SQL exception to console if encountered
        }

        menuFrame.add(scrollPane); // Adding the scroll pane to the menu JFrame
        menuFrame.setSize(400, 350); // Setting size of the menu JFrame
        menuFrame.setLayout(null); // Setting layout to null for absolute positioning
        menuFrame.setVisible(true); // Setting the menu JFrame visible
    }

    public static void main(String[] args) {
        new GUI(); // Creating an instance of the GUI class to initiate the application
    }
}
