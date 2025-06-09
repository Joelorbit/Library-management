import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

// Abstract Book class
abstract class Book {
    private String bookID;
    private String title;
    private String author;
    private String publisher;
    private String yearOfPublication;
    private String isbn;
    private int numberOfCopies;

    public Book(String bookID, String title, String author, String publisher, 
               String yearOfPublication, String isbn, int numberOfCopies) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.yearOfPublication = yearOfPublication;
        this.isbn = isbn;
        this.numberOfCopies = numberOfCopies;
    }

    // Getters and Setters
    public String getBookID() { return bookID; }
    public void setBookID(String bookID) { this.bookID = bookID; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getYearOfPublication() { return yearOfPublication; }
    public void setYearOfPublication(String yearOfPublication) { this.yearOfPublication = yearOfPublication; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getNumberOfCopies() { return numberOfCopies; }
    public void setNumberOfCopies(int numberOfCopies) { this.numberOfCopies = numberOfCopies; }

    public abstract String getCategory();
}

// FictionBook subclass
class FictionBook extends Book {
    public FictionBook(String bookID, String title, String author, String publisher, 
                      String yearOfPublication, String isbn, int numberOfCopies) {
        super(bookID, title, author, publisher, yearOfPublication, isbn, numberOfCopies);
    }

    @Override
    public String getCategory() {
        return "Fiction";
    }
}

// NonFictionBook subclass
class NonFictionBook extends Book {
    public NonFictionBook(String bookID, String title, String author, String publisher, 
                         String yearOfPublication, String isbn, int numberOfCopies) {
        super(bookID, title, author, publisher, yearOfPublication, isbn, numberOfCopies);
    }

    @Override
    public String getCategory() {
        return "Non-Fiction";
    }
}

// Main LibraryManagement class with GUI
public class LibraryManagement extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JButton addButton, viewButton, editButton, deleteButton, clearButton, saveButton, searchButton, exitButton;
    private JComboBox<String> categoryComboBox;
    private JPanel panel;
    private ArrayList<Book> books = new ArrayList<>();

    public LibraryManagement() {
        setTitle("Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load books from file
        loadBooksFromFile();

        // Initialize components
        label1 = new JLabel("Book ID");
        label2 = new JLabel("Book Title");
        label3 = new JLabel("Author");
        label4 = new JLabel("Publisher");
        label5 = new JLabel("Year of Publication");
        label6 = new JLabel("ISBN");
        label7 = new JLabel("Number of Copies");

        textField1 = new JTextField(10);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);
        textField5 = new JTextField(10);
        textField6 = new JTextField(20);
        textField7 = new JTextField(10);

        String[] categories = {"Fiction", "Non-Fiction"};
        categoryComboBox = new JComboBox<>(categories);

        addButton = new JButton("Add");
        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        saveButton = new JButton("Save");
        searchButton = new JButton("Search");
        exitButton = new JButton("Exit");

        // Add action listeners
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        saveButton.addActionListener(this);
        searchButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Set up layout
        panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(8, 2));
        formPanel.add(label1); formPanel.add(textField1);
        formPanel.add(label2); formPanel.add(textField2);
        formPanel.add(label3); formPanel.add(textField3);
        formPanel.add(label4); formPanel.add(textField4);
        formPanel.add(label5); formPanel.add(textField5);
        formPanel.add(label6); formPanel.add(textField6);
        formPanel.add(label7); formPanel.add(textField7);
        formPanel.add(new JLabel("Category")); formPanel.add(categoryComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(exitButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    // Load books from books.txt
    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1); // -1 to include empty fields
                if (fields.length == 8) { // Ensure correct number of fields
                    String bookID = fields[0];
                    String title = fields[1];
                    String author = fields[2];
                    String publisher = fields[3];
                    String yearOfPublication = fields[4];
                    String isbn = fields[5];
                    int numberOfCopies;
                    try {
                        numberOfCopies = Integer.parseInt(fields[6]);
                    } catch (NumberFormatException e) {
                        continue; // Skip invalid entries
                    }
                    String category = fields[7];
                    Book book = category.equals("Fiction") ?
                        new FictionBook(bookID, title, author, publisher, yearOfPublication, isbn, numberOfCopies) :
                        new NonFictionBook(bookID, title, author, publisher, yearOfPublication, isbn, numberOfCopies);
                    books.add(book);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, which is fine for first run
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading books from file");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                String bookID = textField1.getText();
                for (Book b : books) {
                    if (b.getBookID().equals(bookID)) {
                        JOptionPane.showMessageDialog(this, "Book ID already exists");
                        return;
                    }
                }
                int copies = Integer.parseInt(textField7.getText());
                if (copies < 0) {
                    JOptionPane.showMessageDialog(this, "Number of copies cannot be negative");
                    return;
                }
                String category = (String) categoryComboBox.getSelectedItem();
                Book book = category.equals("Fiction") ?
                    new FictionBook(bookID, textField2.getText(), textField3.getText(),
                        textField4.getText(), textField5.getText(), textField6.getText(), copies) :
                    new NonFictionBook(bookID, textField2.getText(), textField3.getText(),
                        textField4.getText(), textField5.getText(), textField6.getText(), copies);
                books.add(book);
                JOptionPane.showMessageDialog(this, "Book added successfully");
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number of copies");
            }
        }
        else if (e.getSource() == viewButton) {
            displayBooks(books, "View Books");
        }
        else if (e.getSource() == editButton) {
            String bookID = JOptionPane.showInputDialog(this, "Enter book ID to edit:");
            for (Book book : books) {
                if (book.getBookID().equals(bookID)) {
                    // Populate fields for editing
                    textField1.setText(book.getBookID());
                    textField2.setText(book.getTitle());
                    textField3.setText(book.getAuthor());
                    textField4.setText(book.getPublisher());
                    textField5.setText(book.getYearOfPublication());
                    textField6.setText(book.getIsbn());
                    textField7.setText(String.valueOf(book.getNumberOfCopies()));
                    categoryComboBox.setSelectedItem(book.getCategory());
                    JOptionPane.showMessageDialog(this, "Edit the fields and click 'Save' to update the book.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Book not found");
        }
        else if (e.getSource() == deleteButton) {
            String bookID = JOptionPane.showInputDialog(this, "Enter book ID to delete:");
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookID().equals(bookID)) {
                    books.remove(i);
                    JOptionPane.showMessageDialog(this, "Book deleted successfully");
                    clearFields();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Book not found");
        }
        else if (e.getSource() == clearButton) {
            clearFields();
        }
        else if (e.getSource() == saveButton) {
            String bookID = textField1.getText().trim();
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                if (book.getBookID().equals(bookID)) { // Remove trim() here
                    try {
                        int copies = Integer.parseInt(textField7.getText().trim());
                        if (copies < 0) {
                            JOptionPane.showMessageDialog(this, "Number of copies cannot be negative");
                            return;
                        }
                        String category = (String) categoryComboBox.getSelectedItem();
                        Book updatedBook = category.equals("Fiction") ?
                            new FictionBook(bookID, textField2.getText(), textField3.getText(),
                                textField4.getText(), textField5.getText(), textField6.getText(), copies) :
                            new NonFictionBook(bookID, textField2.getText(), textField3.getText(),
                                textField4.getText(), textField5.getText(), textField6.getText(), copies);
                        books.set(i, updatedBook);
                        saveBooksToFile(); // Save to file after editing
                        JOptionPane.showMessageDialog(this, "Book edited and saved successfully");
                        clearFields();
                        found = true;
                        break;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number of copies");
                        return;
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Book not found");
            }
        }
        else if (e.getSource() == searchButton) {
            String searchTerm = JOptionPane.showInputDialog(this, "Enter title or author to search:");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                ArrayList<Book> matchingBooks = new ArrayList<>();
                String lowerSearchTerm = searchTerm.trim().toLowerCase();
                for (Book book : books) {
                    if (book.getTitle().toLowerCase().contains(lowerSearchTerm) ||
                        book.getAuthor().toLowerCase().contains(lowerSearchTerm)) {
                        matchingBooks.add(book);
                    }
                }
                if (matchingBooks.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No books found matching the search term.");
                } else {
                    displayBooks(matchingBooks, "Search Results");
                }
            }
        }
        else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void displayBooks(ArrayList<Book> booksToDisplay, String frameTitle) {
        String[] columns = {"Book ID", "Title", "Author", "Publisher", "Year", "ISBN", "Copies", "Category"};
        Object[][] data = new Object[booksToDisplay.size()][8];
        for (int i = 0; i < booksToDisplay.size(); i++) {
            Book book = booksToDisplay.get(i);
            data[i][0] = book.getBookID();
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = book.getPublisher();
            data[i][4] = book.getYearOfPublication();
            data[i][5] = book.getIsbn();
            data[i][6] = book.getNumberOfCopies();
            data[i][7] = book.getCategory();
        }
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame(frameTitle);
        frame.add(scrollPane);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    public static void main(String[] args) {
        new LibraryManagement();
    }

    // Add this method to your class (anywhere outside actionPerformed)
    private void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("books.txt"))) {
            for (Book book : books) {
                writer.println(
                    book.getBookID() + "," +
                    book.getTitle() + "," +
                    book.getAuthor() + "," +
                    book.getPublisher() + "," +
                    book.getYearOfPublication() + "," +
                    book.getIsbn() + "," +
                    book.getNumberOfCopies() + "," +
                    book.getCategory()
                );
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving books to file");
        }
    }
}