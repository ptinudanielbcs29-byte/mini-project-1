package carworkshop2;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registration extends JFrame {

    private JTextField txtName;
    private JTextField txtUsername;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 13);
    private final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public Registration() {
        setTitle("Car Workshop | Customer Registration");
        setSize(900, 800);
        setMinimumSize(new Dimension(750, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(IVORY);

        add(createHeader(), BorderLayout.NORTH);
        add(createRegistrationPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OLIVE);
        header.setBorder(BorderFactory.createEmptyBorder(22, 35, 22, 35));

        JLabel title = new JLabel("CUSTOMER REGISTRATION");
        title.setFont(new Font("Serif", Font.BOLD, 27));
        title.setForeground(IVORY);

        JLabel subtitle = new JLabel(
                "Create your account to manage your vehicle services"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(230, 230, 216));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createRegistrationPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(IVORY);
        outer.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CREAM, 1),
                BorderFactory.createEmptyBorder(40, 35, 40, 35)
        ));
        card.setPreferredSize(new Dimension(500, 620));

        JLabel heading = new JLabel("Create Your Account");
        heading.setFont(new Font("Serif", Font.BOLD, 25));
        heading.setForeground(OLIVE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel("Enter your details below");
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(BROWN);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField();
        txtUsername = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        JButton btnRegister = createButton("REGISTER", OLIVE, IVORY);
        JButton btnBack = createButton("BACK TO LOGIN", OLIVE, IVORY);

        btnRegister.addActionListener(e -> registerCustomer());

        btnBack.addActionListener(e -> {
            new Login();
            dispose();
        });

        card.add(heading);
        card.add(Box.createVerticalStrut(20 ));
        card.add(description);
        card.add(Box.createVerticalStrut(12));

        addField(card, "Full Name", txtName);
        addField(card, "Username", txtUsername);
        addField(card, "Phone", txtPhone);
        addField(card, "Email", txtEmail);
        addField(card, "Password", txtPassword);

        card.add(Box.createVerticalStrut(10));
        card.add(btnRegister);
        card.add(Box.createVerticalStrut(10));
        card.add(btnBack);

        outer.add(card);
        return outer;
    }

    private void addField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setFont(INPUT_FONT);
        field.setForeground(BROWN);
        field.setBackground(IVORY);
        field.setCaretColor(OLIVE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAGE),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        field.setPreferredSize(new Dimension(400, 36));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(11, 15, 11, 15));
        button.setPreferredSize(new Dimension(400, 42));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private void registerCustomer() {
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Name, username, and password are required.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "INSERT INTO users " +
                "(username, password, role, name, phone, email) " +
                "VALUES (?, ?, 'Customer', ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, name);
            pst.setString(4, phone);
            pst.setString(5, email);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful. You can now log in.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new Login();
            dispose();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(
                        this,
                        "That username already exists.",
                        "Duplicate Username",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed: " + e.getMessage(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
