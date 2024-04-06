import javax.swing.*;
import java.awt.*;
import java.sql.*;
import sql.SqlConnection;


public class ForgotPassword extends JFrame {

    private final SqlConnection sqlConnection = new SqlConnection();
    private JTextField cardNumberField;
    private JTextField recoveryAnswerField;
    private JPasswordField newPinField;
    private JPasswordField confirmNewPinField;
    private JButton resetPasswordButton;
    private JButton loginButton;

    public ForgotPassword() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ATM Simulator - Forgot Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        getContentPane().add(mainPanel);

        JLabel recoveryQuestion = new JLabel("Please complete the fields.");
        recoveryQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField(20);
        JLabel recoveryLabel = new JLabel("Recovery Answer:");
        recoveryAnswerField = new JTextField(20);
        JLabel newPinLabel = new JLabel("New PIN:");
        newPinField = new JPasswordField(20);
        JLabel confirmNewPinLabel = new JLabel("Confirm New PIN:");
        confirmNewPinField = new JPasswordField(20);
        resetPasswordButton = new JButton("Reset Password");
        loginButton = new JButton("Log in ");

        mainPanel.add(recoveryQuestion);
        addRow(mainPanel, cardNumberLabel, cardNumberField);
        addRow(mainPanel, recoveryLabel, recoveryAnswerField);
        addRow(mainPanel, newPinLabel, newPinField);
        addRow(mainPanel, confirmNewPinLabel, confirmNewPinField);

        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel);

        resetPasswordButton.addActionListener(e -> resetPassword());
        loginButton.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });

        setVisible(true);
    }

    private void addRow(JPanel panel, JLabel label, JComponent component) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rowPanel.add(label);
        rowPanel.add(component);
        panel.add(rowPanel);
    }

    private String retrieveRecoveryAnswerFromDB(String cardNumber) {
        try {
            Connection conn = sqlConnection.getConnection();

            String query = "SELECT recoveryanswer FROM signup WHERE card_number = ?";
            System.out.println("Query executed: " + query); // Add this line for debugging

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("recoveryanswer");
                    }
                }
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    private void resetPassword() {
        String cardNumber = cardNumberField.getText();
        String recoveryAnswer = recoveryAnswerField.getText();
        String newPin = new String(newPinField.getPassword());
        String confirmNewPin = new String(confirmNewPinField.getPassword());

        String recoveryAnswerFromDB = retrieveRecoveryAnswerFromDB(cardNumber);

        if (recoveryAnswerFromDB != null && recoveryAnswerFromDB.equals(recoveryAnswer)) {
            if (isValidInput(newPin, confirmNewPin)) {
                try {
                    Connection conn = sqlConnection.getConnection();

                    // Update the PIN in the database
                    String updateQuery = "UPDATE signup SET pin = ? WHERE card_number = ?";
                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, newPin);
                        updateStatement.setString(2, cardNumber);
                        int rowsAffected = updateStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Password reset successful!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update PIN. Please try again.");
                        }
                    }
                    conn.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input for PIN. Please check your information.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid card number or recovery answer.");
        }
    }


    private boolean isValidInput(String newPin, String confirmNewPin) {
        if (newPin.isEmpty() || confirmNewPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all PIN fields.");
            return false;
        } else if (!newPin.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PIN should only contain numbers.");
            return false;
        } else if (newPin.length() != 4) {
            JOptionPane.showMessageDialog(this, "PIN should be 4 digits.");
            return false;
        } else if (!newPin.equals(confirmNewPin)) {
            JOptionPane.showMessageDialog(this, "New PIN and Confirm PIN do not match.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ForgotPassword::new);
    }
}
