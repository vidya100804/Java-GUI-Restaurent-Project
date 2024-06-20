import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BooksAndAuthorsGUI extends JFrame {
    private JComboBox<String> comboAuthors;
    private JComboBox<String> comboGenres;
    private JTextArea displayArea;
    private Connection conn;

    public BooksAndAuthorsGUI() {
        super("Books and Authors");

        // Initialize GUI components
        comboAuthors = new JComboBox<>();
        comboGenres = new JComboBox<>();
        displayArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        JButton btnByAuthor = new JButton("By Author");
        JButton btnByGenre = new JButton("By Genre");

        // Layout setup
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("By Author:"));
        topPanel.add(comboAuthors);
        topPanel.add(btnByAuthor);
        topPanel.add(new JLabel("By Genre:"));
        topPanel.add(comboGenres);
        topPanel.add(btnByGenre);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set up database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Button actions
        btnByAuthor.addActionListener(e -> displayBooksByAuthor());
        btnByGenre.addActionListener(e -> displayBooksByGenre());

        // Frame settings
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void displayBooksByAuthor() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, title, genre, cost FROM books WHERE author_id = " + comboAuthors.getSelectedIndex());

            displayResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayBooksByGenre() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT title, id, author_id, cost FROM books WHERE genre = '" + comboGenres.getSelectedItem() + "'");

            displayResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayResultSet(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append("ID: ").append(rs.getInt("id")).append("\n");
            sb.append("Title: ").append(rs.getString("title")).append("\n");
            sb.append("Genre: ").append(rs.getString("genre")).append("\n");
            sb.append("Cost: ").append(rs.getDouble("cost")).append("\n\n");
        }
        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BooksAndAuthorsGUI::new);
    }
}
