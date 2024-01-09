import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Random;
import sql.SqlConnection;

public class SignUpForm extends JFrame {

    private final SqlConnection sqlConnection = new SqlConnection();
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField recoveryQuestionField;
    private JTextField recoveryAnswerField;
    private JTextField accountNumberField;
    private JTextField amountField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JButton signUpButton;

    public SignUpForm() {
        initializeUI();
        createDatabaseAndTable();
    }

    private void initializeUI() {
        setTitle("ATM Simulator - Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(13, 1, 10, 10));
        getContentPane().add(mainPanel);

        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.add(firstNameField);
        namePanel.add(lastNameField);
        namePanel.setBackground(new Color(135, 206, 235));
        addRow(mainPanel, "Name:", namePanel);
        namePanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        usernameField = new JTextField(20);
        addRow(mainPanel, "Username:", usernameField);

        passwordField = new JPasswordField(20);
        addRow(mainPanel, "Pin:", passwordField);

        recoveryQuestionField = new JTextField(30);
        addRow(mainPanel, "Recovery Question:", recoveryQuestionField);

        recoveryAnswerField = new JTextField(30);
        addRow(mainPanel, "Recovery Answer:", recoveryAnswerField);

        accountNumberField = new JTextField(20);
        addRow(mainPanel, "Account Number:", accountNumberField);

        amountField = new JTextField(20);
        addRow(mainPanel, "Amount:", amountField);

        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        ButtonGroup genderButtonGroup = new ButtonGroup();
        genderButtonGroup.add(maleRadioButton);
        genderButtonGroup.add(femaleRadioButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(new JLabel("Gender:"));
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        addRow(mainPanel, "", genderPanel);

        signUpButton = new JButton("Sign Up");
        mainPanel.add(signUpButton);
        signUpButton.setBackground(new Color(135, 206, 235));
        signUpButton.addActionListener(e -> signUp());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
        mainPanel.setBackground(new Color(135, 206, 235));
        setVisible(true);

    }

    private void addRow(JPanel panel, String labelText, JComponent component) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(new Color(135, 206, 235));

        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
//        label.setFont(new Font("Arial", Font.BOLD, 16));
        rowPanel.add(label, BorderLayout.WEST);
        rowPanel.add(component, BorderLayout.CENTER);

        panel.add(rowPanel);
    }


    private void createDatabaseAndTable() {
        try {
            Connection conn = sqlConnection.getConnection();

            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS Atmsimulator";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createDatabaseQuery);
            }

            String useDatabaseQuery = "USE Atmsimulator";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(useDatabaseQuery);
            }

            String createTableQuery = "CREATE TABLE IF NOT EXISTS signup (user_id INT PRIMARY KEY AUTO_INCREMENT,card_number VARCHAR(10) UNIQUE,firstname VARCHAR(50) NOT NULL,lastname VARCHAR(50) NOT NULL,username VARCHAR(50) NOT NULL UNIQUE,pin CHAR(4) NOT NULL CHECK (LENGTH(pin) = 4),recoveryquestion VARCHAR(100) NOT NULL,recoveryanswer VARCHAR(100) NOT NULL,accountnumber BIGINT(11) UNIQUE NOT NULL,amount DECIMAL(10, 2) DEFAULT 0.00,gender ENUM('Male', 'Female'));";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableQuery);
            }

            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }

    private void signUp() {
        try {
            Connection conn = sqlConnection.getConnection();

            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String pin = new String(passwordField.getPassword());
            String recoveryQuestion = recoveryQuestionField.getText();
            String recoveryAnswer = recoveryAnswerField.getText();
            String accountNumber = accountNumberField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String conAmount = amountField.getText();
            String gender = maleRadioButton.isSelected() ? "Male" : "Female";

            String cardNumber = generateCardNumber();

            String query = "INSERT INTO signup (firstname, lastname, username, pin, recoveryquestion, recoveryanswer, accountnumber, card_number, amount, gender)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, pin);
                preparedStatement.setString(5, recoveryQuestion);
                preparedStatement.setString(6, recoveryAnswer);
                preparedStatement.setString(7, accountNumber);
                preparedStatement.setString(8, cardNumber);
                preparedStatement.setDouble(9, amount);
                preparedStatement.setString(10, gender);

                preparedStatement.executeUpdate();
//                if( !(firstName.equals("") && lastName.equals("") && username.equals("") && pin.equals("")
//                && recoveryQuestion.equals("")   && accountNumber.equals("")   && conAmount.equals("")
//                        && gender.isEmpty()
//                ) ){
//                    System.out.println(" sign up successful");
//                }else {
//                    System.out.println("Not Successful");
//                }

                JOptionPane.showMessageDialog(this, "Sign up successful!\nCard Number: " + cardNumber);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                conn.close();
            }

            dispose();
            SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
        } catch (Exception e) {
            System.err.println("There was an error: " + e.getMessage());
            JOptionPane.showMessageDialog( null,"Field cant be empty");

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpForm::new);
    }
}
