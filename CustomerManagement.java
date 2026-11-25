package carworkshop2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerManagement extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;


    private static final Color OLIVE = new Color(83, 96, 69);
    private static final Color DARK_OLIVE = new Color(65, 76, 54);
    private static final Color SAGE = new Color(177, 190, 164);
    private static final Color LIGHT_SAGE = new Color(224, 230, 215);
    private static final Color IVORY = new Color(250, 248, 241);
    private static final Color CREAM = new Color(242, 238, 226);
    private static final Color BROWN = new Color(91, 75, 58);
    private static final Color MUTED = new Color(137, 132, 115);
    private static final Color BORDER = new Color(225, 221, 207);

    private static final Font TITLE_FONT =
            new Font("Serif", Font.PLAIN, 30);

    private static final Font BODY_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    public CustomerManagement() {

        setTitle("Car Workshop | Customer Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(20, 22));
        mainPanel.setBackground(IVORY);
        mainPanel.setBorder(
                new EmptyBorder(30, 38, 25, 38));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 18));
        centerPanel.setOpaque(false);

        centerPanel.add(createToolbar(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        loadCustomers("");

        setVisible(true);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel smallTitle = new JLabel("WORKSHOP WORKSPACE");
        smallTitle.setFont(
                new Font("SansSerif", Font.BOLD, 10));
        smallTitle.setForeground(OLIVE);

        JLabel title = new JLabel("Customer Management");
        title.setFont(TITLE_FONT);
        title.setForeground(BROWN);

        JLabel description = new JLabel(
                "Manage customer profiles and account information.");

        description.setFont(BODY_FONT);
        description.setForeground(MUTED);

        titlePanel.add(smallTitle);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(description);

        JLabel role = new JLabel("CUSTOMER DIRECTORY");
        role.setFont(
                new Font("SansSerif", Font.BOLD, 10));
        role.setForeground(OLIVE);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(role, BorderLayout.EAST);

        return header;
    }


    private JPanel createToolbar() {

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(CREAM);

        toolbar.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(15, 18, 15, 18)));

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 9, 0));

        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        searchLabel.setForeground(BROWN);

        searchField = new JTextField(19);
        styleTextField(searchField);

        JButton searchButton = createButton("Search", true);
        JButton showAllButton = createButton("Show All", false);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));

        actionPanel.setOpaque(false);

        JButton addButton = createButton("+ Add Customer", true);
        JButton editButton = createButton("Edit Customer", false);
        JButton deactivateButton = createButton("Deactivate", false);

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deactivateButton);

        toolbar.add(searchPanel, BorderLayout.WEST);
        toolbar.add(actionPanel, BorderLayout.EAST);

        searchButton.addActionListener(
                e -> loadCustomers(searchField.getText().trim()));

        searchField.addActionListener(
                e -> loadCustomers(searchField.getText().trim()));

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadCustomers("");
        });

        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deactivateButton.addActionListener(e -> deactivateCustomer());

        return toolbar;
    }

    private JPanel createTablePanel() {

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(IVORY);

        tablePanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(1, 1, 1, 1)));

        model = new DefaultTableModel(
                new String[]{
                        "Customer ID",
                        "Username",
                        "Customer Name",
                        "Phone",
                        "Email",
                        "Active"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(43);
        table.setFont(BODY_FONT);
        table.setForeground(BROWN);
        table.setBackground(IVORY);

        table.setSelectionBackground(LIGHT_SAGE);
        table.setSelectionForeground(DARK_OLIVE);

        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();

        header.setBackground(OLIVE);
        header.setForeground(Color.WHITE);
        header.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 45));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(IVORY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    void loadCustomers(String search) {

        model.setRowCount(0);

        String sql =
                "SELECT user_id, username, name, phone, email, is_active " +
                        "FROM users WHERE role = 'Customer' AND " +
                        "(name LIKE ? OR username LIKE ? OR phone LIKE ?) " +
                        "ORDER BY user_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + search + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    model.addRow(new Object[]{
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getBoolean("is_active") ? "Yes" : "No"
                    });
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load customers:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    void addCustomer() {

        JTextField name = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();

        styleTextField(name);
        styleTextField(username);
        styleTextField(password);
        styleTextField(phone);
        styleTextField(email);

        JPanel form = createFormPanel();

        addFormRow(form, "Customer Name", name);
        addFormRow(form, "Username", username);
        addFormRow(form, "Password", password);
        addFormRow(form, "Phone", phone);
        addFormRow(form, "Email", email);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()
                || username.getText().trim().isEmpty()
                || password.getPassword().length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Name, username and password are required.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        String sql =
                "INSERT INTO users " +
                        "(username, password, role, name, phone, email) " +
                        "VALUES (?, ?, 'Customer', ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.getText().trim());
            ps.setString(2, new String(password.getPassword()));
            ps.setString(3, name.getText().trim());
            ps.setString(4, phone.getText().trim());
            ps.setString(5, email.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Customer added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCustomers(searchField.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add customer. Check whether the username already exists.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void editCustomer() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a customer first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        String oldName = model.getValueAt(row, 2).toString();

        Object phoneValue = model.getValueAt(row, 3);
        Object emailValue = model.getValueAt(row, 4);

        JTextField name = new JTextField(oldName);

        JTextField phone = new JTextField(
                phoneValue == null ? "" : phoneValue.toString());

        JTextField email = new JTextField(
                emailValue == null ? "" : emailValue.toString());

        styleTextField(name);
        styleTextField(phone);
        styleTextField(email);

        JPanel form = createFormPanel();

        addFormRow(form, "Customer Name", name);
        addFormRow(form, "Phone", phone);
        addFormRow(form, "Email", email);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Edit Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String sql =
                "UPDATE users SET name = ?, phone = ?, email = ? " +
                        "WHERE user_id = ? AND role = 'Customer'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.getText().trim());
            ps.setString(2, phone.getText().trim());
            ps.setString(3, email.getText().trim());
            ps.setInt(4, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Customer updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCustomers(searchField.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update customer:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void deactivateCustomer() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a customer first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        if ("No".equals(model.getValueAt(row, 5).toString())) {

            JOptionPane.showMessageDialog(
                    this,
                    "Customer is already inactive.",
                    "Customer Status",
                    JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to deactivate this customer?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE users SET is_active = FALSE " +
                        "WHERE user_id = ? AND role = 'Customer'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Customer deactivated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadCustomers(searchField.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to deactivate customer:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createFormPanel() {

        JPanel form = new JPanel(
                new GridLayout(0, 2, 13, 16));

        form.setBackground(IVORY);

        form.setBorder(
                new EmptyBorder(18, 18, 18, 18));

        return form;
    }

    private void addFormRow(
            JPanel panel,
            String labelText,
            Component component) {

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        label.setForeground(BROWN);

        panel.add(label);
        panel.add(component);
    }

    private void styleTextField(JTextField field) {

        field.setFont(BODY_FONT);
        field.setForeground(BROWN);
        field.setBackground(Color.WHITE);
        field.setCaretColor(OLIVE);

        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(9, 10, 9, 10)));
    }

    private JButton createButton(
            String text,
            boolean primary) {

        JButton button = new JButton(text);

        button.setFont(BODY_FONT);

        button.setForeground(
                primary ? Color.WHITE : OLIVE);

        button.setBackground(
                primary ? OLIVE : LIGHT_SAGE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setBorder(
                new EmptyBorder(11, 15, 11, 15));

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(
                        primary ? DARK_OLIVE : SAGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(
                        primary ? OLIVE : LIGHT_SAGE);
            }
        });

        return button;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel left = new JLabel(
                "AUTO ATELIER  |  CUSTOMER DIRECTORY");

        left.setFont(
                new Font("SansSerif", Font.PLAIN, 10));

        left.setForeground(MUTED);

        JLabel right = new JLabel("ADMINISTRATION");

        right.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        right.setForeground(OLIVE);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }
}
