import javax.swing.*;
import java.awt.*;

public class ForgotPassword extends JFrame {
    private JTextField recoveryAnswerField;
    private JPasswordField newPinField;
    private JPasswordField confirmNewPinField;
    private JButton resetPasswordButton;

    public ForgotPassword() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ATM Simulator - Forgot Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, 10px spacing
        getContentPane().add(mainPanel);

        // Create components
        JLabel recoveryQuestion = new JLabel("Security Question: What's your favorite color?");
        JLabel recoveryLabel = new JLabel("Recovery Answer:");
        recoveryAnswerField = new JTextField(20);
        JLabel newPinLabel = new JLabel("New PIN:");
        newPinField = new JPasswordField(20);
        JLabel confirmNewPinLabel = new JLabel("Confirm New PIN:");
        confirmNewPinField = new JPasswordField(20);
        resetPasswordButton = new JButton("Reset Password");

        // Add components to the main panel
        mainPanel.add(recoveryQuestion);
        addRow(mainPanel, recoveryLabel, recoveryAnswerField);
        addRow(mainPanel, newPinLabel, newPinField);
        addRow(mainPanel, confirmNewPinLabel, confirmNewPinField);
        mainPanel.add(resetPasswordButton);

        resetPasswordButton.addActionListener(e -> resetPassword());

        setVisible(true);
    }

    private void addRow(JPanel panel, JLabel label, JComponent component) {
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns, 10px horizontal spacing
        rowPanel.add(label);
        rowPanel.add(component);
        panel.add(rowPanel);
    }

    private void resetPassword() {
        String recoveryAnswer = recoveryAnswerField.getText();
        String newPin = new String(newPinField.getPassword());
        String confirmNewPin = new String(confirmNewPinField.getPassword());

        // Perform your password reset logic here
        if (isValidInput(recoveryAnswer, newPin, confirmNewPin)) {
            // Update the password
            JOptionPane.showMessageDialog(this, "Password reset successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your information.");
        }
    }

    private boolean isValidInput(String recoveryAnswer, String newPin, String confirmNewPin) {
        // Add your validation logic here
        // For demonstration purposes, always return true here
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForgotPassword());
    }
}
