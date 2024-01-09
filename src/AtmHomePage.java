import sql.SqlConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AtmHomePage extends JFrame {
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton payBillsButton;
    private JButton checkBalanceButton;
    private JButton accountInfoButton;
    JPanel namePanel;
    JLabel nameLabel;

    private final SqlConnection sqlConnection = new SqlConnection();
    private static String loggedInCardNumber; // Store the logged-in card number


    public AtmHomePage(String loggedInCardNumber) {
        this.loggedInCardNumber = loggedInCardNumber;
        String name = fetchUserNameFromDatabase(loggedInCardNumber);


        setTitle("ATM Homepage");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        namePanel = new JPanel(new GridLayout(0,1));
        // Set the background color to light purple
        nameLabel = new JLabel("       Welcome " + name);

        Color lightBlack = new Color(0, 0, 0);
        Color lightPurple = new Color(135, 206, 235);  // You can adjust these RGB values
        namePanel.setBackground(lightPurple);
        namePanel.setPreferredSize(new Dimension(600, 100));

        nameLabel = new JLabel("       Welcome " + name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Adjust the font family, style, and size as needed
        nameLabel.setForeground(lightBlack);


        namePanel.add(nameLabel);
        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JPanel FinalPanel = new JPanel(new GridLayout(2, 1, 40, 40));// 3 rows, 2 columns, 10px spacing
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        transferButton = new JButton("Transfer");
        payBillsButton = new JButton("Pay Bills");
        checkBalanceButton = new JButton("Check Balance");
        accountInfoButton = new JButton("Account Information");

        // Add buttons to the mainPanel
        depositButton.setBorderPainted(false);
        withdrawButton.setBorderPainted(false);
        transferButton.setBorderPainted(false);
        payBillsButton.setBorderPainted(false);
        checkBalanceButton.setBorderPainted(false);
        accountInfoButton.setBorderPainted(false);


        // Set the background color for the mainPanel
        Color mainPanelColor = new Color(230, 230, 250);  // Light lavender color
        mainPanel.setBackground(mainPanelColor);

        // Set button colors
        Color buttonColor = new Color(135, 206, 235);  // Sky blue color
        depositButton.setBackground(buttonColor);
        withdrawButton.setBackground(buttonColor);
        transferButton.setBackground(buttonColor);
        payBillsButton.setBackground(buttonColor);
        checkBalanceButton.setBackground(buttonColor);
        accountInfoButton.setBackground(buttonColor);

        // Set button text color
        Color buttonText = new Color(0, 102, 204);  // Dark blue color
        depositButton.setForeground(buttonText);
        withdrawButton.setForeground(buttonText);
        transferButton.setForeground(buttonText);
        payBillsButton.setForeground(buttonText);
        checkBalanceButton.setForeground(buttonText);
        accountInfoButton.setForeground(buttonText);

        // Set font color for nameLabel
        Color nameLabelColor = new Color(0, 0, 0);  // Dark violet color
        nameLabel.setForeground(nameLabelColor);

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "Enter deposit amount:");
                if (input != null && !input.isEmpty()) {
                    try {
                        double depositAmount = Double.parseDouble(input);
                        int result = performDeposit(loggedInCardNumber, depositAmount);
                        if (result == 1) {
                            JOptionPane.showMessageDialog(null, "Deposit successful. Thank you!");
                        } else if (result == -1) {
                            JOptionPane.showMessageDialog(null, "Card number not found.");
                        } else {
                            JOptionPane.showMessageDialog(null, "An error occurred.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                    }
                }
            }
        });


        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "Enter withdrawal amount:");
                if (input != null && !input.isEmpty()) {
                    try {
                        double withdrawalAmount = Double.parseDouble(input);
                        int result = performWithdraw(loggedInCardNumber, withdrawalAmount);
                        if (result == 1) {
                            JOptionPane.showMessageDialog(null, "Withdrawal successful. Please take your cash.");
                        } else if (result == -1) {
                            JOptionPane.showMessageDialog(null, "Insufficient balance.");
                        } else if (result == -2) {
                            JOptionPane.showMessageDialog(null, "Card number not found.");
                        } else {
                            JOptionPane.showMessageDialog(null, "An error occurred.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                    }
                }
            }
        });



        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String receiverCardNumber = JOptionPane.showInputDialog(null, "Enter receiver's Account Number:");
                if (receiverCardNumber != null && !receiverCardNumber.isEmpty()) {
                    try {
                        double transferAmount = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter transfer amount:"));
                        int result = performTransfer(loggedInCardNumber, receiverCardNumber, transferAmount);
                        if (result == 1) {
                            JOptionPane.showMessageDialog(null, "Transfer successful. Amount transferred: $" + transferAmount);
                        } else if (result == -1) {
                            JOptionPane.showMessageDialog(null, "Insufficient balance.");
                        } else if (result == -2) {
                            JOptionPane.showMessageDialog(null, "Card number not found.");
                        } else if (result == -3) {
                            JOptionPane.showMessageDialog(null, "Receiver's card number not found.");
                        } else {
                            JOptionPane.showMessageDialog(null, "An error occurred.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
                    }
                }
            }
        });

        payBillsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement your pay bills logic here
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double balance = checkBalance(loggedInCardNumber);

                if (balance >= 0) {
                    JOptionPane.showMessageDialog(null, "Your balance is: $" + balance);
                } else if (balance == -1) {
                    JOptionPane.showMessageDialog(null, "Card number not found.");
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred.");
                }
            }
        });




        accountInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResultSet resultSet = getAccountInfo(loggedInCardNumber);
                if (resultSet != null) {
                    try {
                        if (resultSet.next()) {
                            String fullName = resultSet.getString("firstname");
                            String lastName = resultSet.getString("lastname");
                            String username = resultSet.getString("username");
                            String pin = resultSet.getString("pin");
                            String recoveryQuestion = resultSet.getString("recoveryquestion");
                            String recoverAnswer = resultSet.getString("recoveryanswer");
                            String cardNumber = resultSet.getString("card_number");
                            double balance = resultSet.getDouble("amount");
                            String gender = resultSet.getString("gender");
                            // Retrieve other information as needed

                            String accountInfoMessage = "Full Name: " + fullName +" " + lastName + "\n" +

                                    "Username: " + username + "\n" +
                                    "Pin: " + pin + "\n" +
                                    "Recovery Question: " + recoveryQuestion + "\n" +
                                    "Recover Answer: " + recoverAnswer + "\n" +
                                    "Card Number: " + cardNumber + "\n" +
                                    "Gender: "+ gender + "\n" +
                                    "Net-Worth: $" + balance;

                            // Append other information to the message

                            JOptionPane.showMessageDialog(null, accountInfoMessage, "Account Information", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Card number not found.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while retrieving account information.");
                }
            }
        });

//        mainPanel.add(namePanel);
        // Create the mainPanel and set its background color
//        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        // Add buttons to the mainPanel
        mainPanel.add(depositButton);
        mainPanel.add(withdrawButton);
        mainPanel.add(transferButton);
        mainPanel.add(payBillsButton);
        mainPanel.add(checkBalanceButton);
        mainPanel.add(accountInfoButton);

        // Create the FinalPanel using BorderLayout
        JPanel finalPanel = new JPanel(new BorderLayout());
        // Add empty space around the namePanel
        namePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        finalPanel.add(namePanel, BorderLayout.NORTH); // Place namePanel at the top

        // Add empty space around the mainPanel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        finalPanel.add(mainPanel, BorderLayout.CENTER); // Place mainPanel in the center


        // Set the content pane of the JFrame to the finalPanel
        setContentPane(finalPanel);
    }



    private String fetchUserNameFromDatabase(String cardNumber) {
        String firstName = null;
        String secondName = null;

        try {
            Connection conn = sqlConnection.getConnection(); // Get the database connection

            // Prepare and execute SELECT query using PreparedStatement
            String query = "SELECT firstname,lastname FROM signup WHERE card_number = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        firstName = resultSet.getString("firstname");
                        secondName = resultSet.getString("lastname");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return firstName +" " + secondName;
    }




    private int performWithdraw(String cardNumber, double withdrawalAmount) {
        try {
            Connection conn = sqlConnection.getConnection(); // Get the database connection

            // Check if the card number exists and has sufficient balance
            String checkQuery = "SELECT amount FROM signup WHERE card_number = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setString(1, cardNumber);

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double currentBalance = resultSet.getDouble("amount");
                        if (currentBalance >= withdrawalAmount) {
                            // Update the balance after withdrawal
                            String updateQuery = "UPDATE signup SET amount = ? WHERE card_number = ?";
                            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                                double newBalance = currentBalance - withdrawalAmount;
                                updateStatement.setDouble(1, newBalance);
                                updateStatement.setString(2, cardNumber);
                                updateStatement.executeUpdate();
                                return 1; // Successful withdrawal
                            }
                        } else {
                            return -1; // Insufficient balance
                        }
                    } else {
                        return -2; // Card number not found
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -3; // An error occurred
        }
    }

    private int performDeposit(String cardNumber, double depositAmount) {
        try {
            Connection conn = sqlConnection.getConnection(); // Get the database connection

            // Check if the card number exists
            String checkQuery = "SELECT amount FROM signup WHERE card_number = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setString(1, cardNumber);

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double currentBalance = resultSet.getDouble("amount");

                        // Update the balance after deposit
                        String updateQuery = "UPDATE signup SET amount = ? WHERE card_number = ?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                            double newBalance = currentBalance + depositAmount;
                            updateStatement.setDouble(1, newBalance);
                            updateStatement.setString(2, cardNumber);
                            updateStatement.executeUpdate();
                            return 1; // Successful deposit
                        }
                    } else {
                        return -1; // Card number not found
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // An error occurred
        }
    }


//    private int performTransfer(String senderCardNumber, String receiverCardNumber, double transferAmount) {
//        try {
//            Connection conn = sqlConnection.getConnection(); // Get the database connection
//
//            // Check if sender's card number exists and has sufficient balance
//            String senderCheckQuery = "SELECT amount FROM signup WHERE card_number = ?";
//            try (PreparedStatement senderCheckStatement = conn.prepareStatement(senderCheckQuery)) {
//                senderCheckStatement.setString(1, senderCardNumber);
//
//                try (ResultSet senderResultSet = senderCheckStatement.executeQuery()) {
//                    if (senderResultSet.next()) {
//                        double senderBalance = senderResultSet.getDouble("amount");
//                        if (senderBalance >= transferAmount) {
//                            // Update sender's balance after transfer
//                            double newSenderBalance = senderBalance - transferAmount;
//                            String senderUpdateQuery = "UPDATE signup SET amount = ? WHERE card_number = ?";
//                            try (PreparedStatement senderUpdateStatement = conn.prepareStatement(senderUpdateQuery)) {
//                                senderUpdateStatement.setDouble(1, newSenderBalance);
//                                senderUpdateStatement.setString(2, senderCardNumber);
//                                senderUpdateStatement.executeUpdate();
//                            }
//
//                            // Update receiver's balance after transfer
//                            String receiverCheckQuery = "SELECT amount FROM signup WHERE accountnumber = ?";
//                            try (PreparedStatement receiverCheckStatement = conn.prepareStatement(receiverCheckQuery)) {
//                                receiverCheckStatement.setString(1, receiverCardNumber);
//
//                                try (ResultSet receiverResultSet = receiverCheckStatement.executeQuery()) {
//                                    if (receiverResultSet.next()) {
//                                        double receiverBalance = receiverResultSet.getDouble("amount");
//                                        double newReceiverBalance = receiverBalance + transferAmount;
//                                        String receiverUpdateQuery = "UPDATE signup SET amount = ? WHERE accountnumber = ?";
//                                        try (PreparedStatement receiverUpdateStatement = conn.prepareStatement(receiverUpdateQuery)) {
//                                            receiverUpdateStatement.setDouble(1, newReceiverBalance);
//                                            receiverUpdateStatement.setString(2, receiverCardNumber);
//                                            receiverUpdateStatement.executeUpdate();
//                                            return 1; // Successful transfer
//                                        }
//                                    } else {
//                                        return -3; // Receiver's card number not found
//                                    }
//                                }
//                            }
//                        } else {
//                            return -1; // Insufficient balance
//                        }
//                    } else {
//                        return -2; // Sender's card number not found
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -4; // An error occurred
//        }
//    }


    private int performTransfer(String senderCardNumber, String receiverAccountNumber, double transferAmount) {
        try {
            Connection conn = sqlConnection.getConnection(); // Get the database connection

            // Check if sender's card number exists and has sufficient balance
            String senderCheckQuery = "SELECT amount FROM signup WHERE card_number = ?";
            try (PreparedStatement senderCheckStatement = conn.prepareStatement(senderCheckQuery)) {
                senderCheckStatement.setString(1, senderCardNumber);


                String[] options = {"Local Bank", "Other Banks"};
                int transferType = JOptionPane.showOptionDialog(
                        null,
                        "Select transfer type:",
                        "Transfer Type",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );


                int result;
                if (transferType == 0) {
                    try (ResultSet senderResultSet = senderCheckStatement.executeQuery()) {
                        if (senderResultSet.next()) {
                            double senderBalance = senderResultSet.getDouble("amount");
                            if (senderBalance >= transferAmount) {
                                // Update sender's balance after transfer
                                double newSenderBalance = senderBalance - transferAmount;
                                String senderUpdateQuery = "UPDATE signup SET amount = ? WHERE card_number = ?";
                                try (PreparedStatement senderUpdateStatement = conn.prepareStatement(senderUpdateQuery)) {
                                    senderUpdateStatement.setDouble(1, newSenderBalance);
                                    senderUpdateStatement.setString(2, senderCardNumber);
                                    senderUpdateStatement.executeUpdate();
                                }

                                // Update receiver's balance after transfer
                                String receiverCheckQuery = "SELECT amount FROM signup WHERE accountnumber = ?";
                                try (PreparedStatement receiverCheckStatement = conn.prepareStatement(receiverCheckQuery)) {
                                    receiverCheckStatement.setString(1, receiverAccountNumber);

                                    try (ResultSet receiverResultSet = receiverCheckStatement.executeQuery()) {
                                        if (receiverResultSet.next()) {
                                            double receiverBalance = receiverResultSet.getDouble("amount");
                                            double newReceiverBalance = receiverBalance + transferAmount;
                                            String receiverUpdateQuery = "UPDATE signup SET amount = ? WHERE accountnumber = ?";
                                            try (PreparedStatement receiverUpdateStatement = conn.prepareStatement(receiverUpdateQuery)) {
                                                receiverUpdateStatement.setDouble(1, newReceiverBalance);
                                                receiverUpdateStatement.setString(2, receiverAccountNumber);
                                                receiverUpdateStatement.executeUpdate();

                                                // Insert transfer record
                                                String insertTransferQuery = "INSERT INTO transfers (sender_card_number, receiver_account_number, transfer_amount) VALUES (?, ?, ?)";
                                                try (PreparedStatement insertTransferStatement = conn.prepareStatement(insertTransferQuery)) {
                                                    insertTransferStatement.setString(1, senderCardNumber);
                                                    insertTransferStatement.setString(2, receiverAccountNumber);
                                                    insertTransferStatement.setDouble(3, transferAmount);
                                                    insertTransferStatement.executeUpdate();
                                                }

                                                return 1; // Successful transfer
                                            }
                                        } else {
                                            return -3; // Receiver's account number not found
                                        }
                                    }
                                }
                            } else {
                                return -1; // Insufficient balance
                            }
                        } else {
                            return -2; // Sender's card number not found
                        }
                    }
                }


                else if (transferType == 1) {
                    String bankName = JOptionPane.showInputDialog(null, "Enter the bank name:");
                    if (bankName != null && !bankName.isEmpty()) {
                        try (ResultSet senderResultSet = senderCheckStatement.executeQuery()) {
                            if (senderResultSet.next()) {
                                double senderBalance = senderResultSet.getDouble("amount");
                                if (senderBalance >= transferAmount) {
                                    // Update sender's balance after transfer
                                    double newSenderBalance = senderBalance - transferAmount;
                                    String senderUpdateQuery = "UPDATE signup SET amount = ? WHERE card_number = ?";
                                    try (PreparedStatement senderUpdateStatement = conn.prepareStatement(senderUpdateQuery)) {
                                        senderUpdateStatement.setDouble(1, newSenderBalance);
                                        senderUpdateStatement.setString(2, senderCardNumber);
                                        senderUpdateStatement.executeUpdate();
                                    }

                                    // Check if receiver's account exists in the Bank table
//                                    String receiverCheckQuery = "SELECT * FROM Bank WHERE account_number = ?";
                                    String receiverCheckQuery = "SELECT * FROM Bank WHERE account_number = ? AND bank_name = ?";
                                    try (PreparedStatement receiverCheckStatement = conn.prepareStatement(receiverCheckQuery)) {
                                        receiverCheckStatement.setString(1, receiverAccountNumber);
                                        receiverCheckStatement.setString(2, bankName);


                                        try (ResultSet receiverResultSet = receiverCheckStatement.executeQuery()) {
                                            if (receiverResultSet.next()) {
                                                // Receiver's account with the specific account number and bank name exists in the Bank table
                                                double receiverBalance = receiverResultSet.getDouble("amount");
                                                double newReceiverBalance = receiverBalance + transferAmount;

                                                String receiverUpdateQuery = "UPDATE Bank SET amount = ? WHERE account_number = ? AND bank_name = ?";
                                                try (PreparedStatement receiverUpdateStatement = conn.prepareStatement(receiverUpdateQuery)) {
                                                    receiverUpdateStatement.setDouble(1, newReceiverBalance);
                                                    receiverUpdateStatement.setString(2, receiverAccountNumber);
                                                    receiverUpdateStatement.setString(3, bankName);
                                                    receiverUpdateStatement.executeUpdate();
                                                }
                                            }
                                            else {
                                                // Receiver's account does not exist in the Bank table, insert new account
                                                String insertReceiverQuery = "INSERT INTO Bank (bank_name, account_number, amount) VALUES (?, ?, ?)";
                                                try (PreparedStatement insertReceiverStatement = conn.prepareStatement(insertReceiverQuery)) {
                                                    insertReceiverStatement.setString(1, bankName);
                                                    insertReceiverStatement.setString(2, receiverAccountNumber);
                                                    insertReceiverStatement.setDouble(3, transferAmount);
                                                    insertReceiverStatement.executeUpdate();
                                                }
                                            }

                                            // Insert transfer record
                                            String insertTransferQuery = "INSERT INTO transfers (sender_card_number, receiver_account_number, transfer_amount) VALUES (?, ?, ?)";
                                            try (PreparedStatement insertTransferStatement = conn.prepareStatement(insertTransferQuery)) {
                                                insertTransferStatement.setString(1, senderCardNumber);
                                                insertTransferStatement.setString(2, receiverAccountNumber);
                                                insertTransferStatement.setDouble(3, transferAmount);
                                                insertTransferStatement.executeUpdate();
                                            }

                                            return 1; // Successful transfer
                                        }
                                    }
                                } else {
                                    return -1; // Insufficient balance
                                }
                            } else {
                                return -2; // Sender's card number not found
                            }
                        }
                    } else {
                        return -5; // Bank name not provided
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -4; // An error occurred
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }





    private double checkBalance(String cardNumber) {
        try {
            Connection conn = sqlConnection.getConnection();

            // Prepare and execute SELECT query using PreparedStatement
            String query = "SELECT amount FROM signup WHERE card_number = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("amount"); // Return the amount if a row is returned
                    } else {
                        return -1; // Indicate that the card number was not found
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // Indicate an error occurred
        }
    }

    private ResultSet getAccountInfo(String cardNumber) {
        try {
            Connection conn = sqlConnection.getConnection(); // Get the database connection

            String query = "SELECT * FROM signup WHERE card_number = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);

            return preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // An error occurred
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Provide the loggedInCardNumber obtained from LoginForm
            new AtmHomePage(loggedInCardNumber).setVisible(true);
        });
    }



}
