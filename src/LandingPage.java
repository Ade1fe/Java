import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPage extends JFrame {

    public LandingPage() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Landing Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);

        // Set background color for the JFrame
        getContentPane().setBackground(new Color(135, 206, 235)); // Light yellow color

        // Use a layout manager, like BorderLayout, to manage component placement
        setLayout(new BorderLayout());

        JLabel label = createLabel("Welcome to the Landing Page");
        add(label, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBackground(new Color(135, 206, 235));
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JLabel createLabel(String text) {
        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setFont(new Font("Arial", Font.BOLD, 18));
        return jLabel;
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        loginButton.setBorderPainted(false);
        signUpButton.setBorderPainted(false);


        loginButton.setFocusable(false);
        signUpButton.setFocusable(false);

        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        signUpButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open login form or perform login-related actions
//                JOptionPane.showMessageDialog(null, "Opening Login Form");
                dispose();
                new LoginForm();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the SignUpForm when "Sign Up" is clicked
                new SignUpForm();
                dispose(); // Close the LandingPage frame
            }
        });

        return buttonPanel;
    }

    public static void main(String[] args) {
        new LandingPage();
    }
}
