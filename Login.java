package carworkshop2;


import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    private final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public Login() {
        setTitle("Car Workshop | Login");
        setSize(850, 550);
        setMinimumSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(IVORY);

        add(createLeftPanel(), BorderLayout.WEST);
        add(createLoginPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(310, 550));
        panel.setBackground(OLIVE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(65, 35, 40, 30));

        JLabel brand = new JLabel("CAR");
        brand.setFont(new Font("Serif", Font.BOLD, 45));
        brand.setForeground(IVORY);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandSecond = new JLabel("WORKSHOP");
        brandSecond.setFont(new Font("Serif", Font.BOLD, 29));
        brandSecond.setForeground(SAGE);
        brandSecond.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel line = new JPanel();
        line.setBackground(SAGE);
        line.setMaximumSize(new Dimension(75, 3));
        line.setPreferredSize(new Dimension(75, 3));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html><div style='width:210px; line-height:1.6;'>"
                        + "Your vehicle care, service management and workshop "
                        + "records in one place."
                        + "</div></html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(IVORY);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footer = new JLabel("SERVICE  •  CARE  •  TRUST");
        footer.setFont(new Font("SansSerif", Font.BOLD, 10));
        footer.setForeground(SAGE);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(brand);
        panel.add(brandSecond);
        panel.add(Box.createVerticalStrut(20));
        panel.add(line);
        panel.add(Box.createVerticalStrut(25));
        panel.add(description);
        panel.add(Box.createVerticalGlue());
        panel.add(footer);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(IVORY);
        outer.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CREAM, 1),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));
        card.setPreferredSize(new Dimension(390, 370));

        JLabel heading = new JLabel("Welcome Back");
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(OLIVE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to continue to your account");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(BROWN);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username");
        styleLabel(usernameLabel);

        txtUsername = new JTextField();
        styleInput(txtUsername);

        JLabel passwordLabel = new JLabel("Password");
        styleLabel(passwordLabel);

        txtPassword = new JPasswordField();
        styleInput(txtPassword);

        JButton btnLogin = createButton("LOGIN", OLIVE, IVORY);
        JButton btnRegister = createButton("REGISTER",OLIVE, IVORY );

        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(7));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(0));

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(10));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(10));

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(12));
        card.add(btnRegister);


        btnLogin.addActionListener(e -> loginUser());

        btnRegister.addActionListener(e -> {
            new Registration();
            dispose();
        });

        txtPassword.addActionListener(e -> loginUser());

        outer.add(card);
        return outer;
    }

    private void styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(BROWN);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleInput(JTextField field) {
        field.setFont(INPUT_FONT);
        field.setForeground(BROWN);
        field.setBackground(IVORY);
        field.setCaretColor(OLIVE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAGE),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(300, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(300, 45));
        return button;
    }

    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter username and password.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "SELECT user_id, name, role FROM users " +
                "WHERE username = ? AND password = ? AND is_active = TRUE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String role = rs.getString("role");

                    dispose();

                    if (role.equalsIgnoreCase("Admin")) {
                        new AdminDashboard(userId, name);
                    } else if (role.equalsIgnoreCase("Worker")) {
                        new WorkerDashboard(userId, name);
                    } else if (role.equalsIgnoreCase("Customer")) {
                        new CustomerDashboard(userId, name);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Unknown account role.",
                                "Login Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        new Login();
                    }

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid username or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}