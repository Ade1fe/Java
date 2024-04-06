import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sql.*;
import sql.SqlConnection;

public class LoginForm extends JFrame {
    private final SqlConnection sqlConnection = new SqlConnection();
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private String loggedInCardNumber;

    public LoginForm() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ATM Simulator - Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        getContentPane().add(mainPanel);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10));

        cardNumberField = new JTextField(20);
        pinField = new JPasswordField(4);

        cardNumberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkCardNumberLength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkCardNumberLength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkCardNumberLength();
            }
        });

        ActionListener numericButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                String digit = button.getText();
                JTextField focusedField = pinField.isFocusOwner() ? pinField : cardNumberField;

                if (focusedField == cardNumberField && cardNumberField.getText().length() >= 10) {
                    pinField.requestFocusInWindow();
                    focusedField = pinField;
                }

                focusedField.setText(focusedField.getText() + digit);
            }
        };

        addNumericButtons(buttonPanel, numericButtonListener);

        addRow(inputPanel, "Card Number:", cardNumberField);
        addRow(inputPanel, "PIN:", pinField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(135, 206, 235));

        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setBackground(new Color(135, 206, 235));

        footerPanel.add(loginButton);
        footerPanel.add(forgotPasswordButton);

        footerPanel.setBackground(new Color(135, 206, 235));

        mainPanel.setBackground(new Color(135, 206, 235));
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        addActionListeners();

        setVisible(true);
    }

    private void addNumericButtons(JPanel buttonPanel, ActionListener numericButtonListener) {
        for (int i = 1; i <= 9; i++) {
            JButton numericButton = new JButton(String.valueOf(i));
            numericButton.addActionListener(numericButtonListener);
            buttonPanel.add(numericButton);
            buttonPanel.setBackground(new Color(135, 206, 235));
            numericButton.setBackground(new Color(135, 206, 235));
        }
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(numericButtonListener);
        zeroButton.setBackground(new Color(135, 206, 235));
        buttonPanel.add(zeroButton);

        JButton deleteButton = new JButton("Del");
        deleteButton.addActionListener(e -> deleteButtonClicked());
        deleteButton.setBackground(new Color(135, 206, 235));

        buttonPanel.add(deleteButton);
    }

    private void addActionListeners() {
        loginButton.addActionListener(e -> login());
        forgotPasswordButton.addActionListener(e -> forgotPassword());
    }

    private void addRow(JPanel panel, String labelText, JComponent component) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;

        panel.add(new JLabel(labelText), constraints);
        panel.setBackground(new Color(135, 206, 235));
        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, constraints);
    }

    private void deleteButtonClicked() {
        JTextField focusedField = pinField.isFocusOwner() ? pinField : cardNumberField;
        String currentText = focusedField.getText();
        if (!currentText.isEmpty()) {
            focusedField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void forgotPassword() {
        String cardNumber = cardNumberField.getText();

        // Perform your forgot password logic here
        // For demonstration purposes, show a dialog with a message
//        JOptionPane.showMessageDialog(this, "Please contact customer support for password reset.");
        dispose();
        new ForgotPassword();

    }

    private boolean isValidLogin(String cardNumber, String password) {
        try {
            Connection conn = sqlConnection.getConnection();
            String query = "SELECT * FROM signup WHERE card_number = ? AND pin = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next(); // If a row exists, the login is valid
            }
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void login() {
        String cardNumber = cardNumberField.getText();
        String password = new String(pinField.getPassword());

        if (isValidLogin(cardNumber, password)) {
            loggedInCardNumber = cardNumber; // Store the logged-in card number
            dispose();
            SwingUtilities.invokeLater(() -> new AtmHomePage(loggedInCardNumber).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid account number or PIN.");
        }
    }

    private void checkCardNumberLength() {
        if (cardNumberField.getText().length() >= 10) {
            pinField.requestFocusInWindow();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}