package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class WorkerManagement extends JFrame
{
    private JTable workerTable;
    private DefaultTableModel tableModel;

    private final Color OLIVE = new Color(83, 96, 69);
    private final Color DARK_OLIVE = new Color(65, 77, 54);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font TITLE_FONT =
            new Font("Serif", Font.BOLD, 30);

    private final Font NORMAL_FONT =
            new Font("SansSerif", Font.PLAIN, 14);

    private final Font BUTTON_FONT =
            new Font("SansSerif", Font.BOLD, 13);

    public WorkerManagement()
    {
        setTitle("Car Workshop - Worker Management");
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(IVORY);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(IVORY);
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 20, 35));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(IVORY);

        JLabel titleLabel = new JLabel("Worker Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(OLIVE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Manage workshop staff and worker accounts");
        subtitleLabel.setFont(NORMAL_FONT);
        subtitleLabel.setForeground(BROWN);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);


        String[] columns = {
                "ID", "Username", "Name", "Phone", "Email", "Active"
        };

        tableModel = new DefaultTableModel(columns, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        workerTable = new JTable(tableModel);
        workerTable.setFont(NORMAL_FONT);
        workerTable.setForeground(BROWN);
        workerTable.setBackground(WHITE);
        workerTable.setSelectionBackground(SAGE);
        workerTable.setSelectionForeground(DARK_OLIVE);
        workerTable.setRowHeight(38);
        workerTable.setShowGrid(false);
        workerTable.setIntercellSpacing(new Dimension(0, 0));
        workerTable.setFillsViewportHeight(true);
        workerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = workerTable.getTableHeader();
        tableHeader.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        tableHeader.setBackground(OLIVE);
        tableHeader.setForeground(WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 45));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(workerTable);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(SAGE, 1));
        scrollPane.getViewport().setBackground(WHITE);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(IVORY);
        tableContainer.setBorder(
                BorderFactory.createEmptyBorder(5, 35, 20, 35));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        JButton btnAdd = createButton("Add Worker", OLIVE, WHITE);
        JButton btnEdit = createButton("Edit Worker", SAGE, DARK_OLIVE);
        JButton btnDeactivate = createButton(
                "Deactivate Worker", CREAM, BROWN);
        JButton btnRefresh = createButton("Refresh", CREAM, BROWN);
        JButton btnBack = createButton("Close", DARK_OLIVE, WHITE);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 15));
        buttonPanel.setBackground(CREAM);
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 15));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDeactivate);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);


        btnAdd.addActionListener(e -> addWorker());
        btnEdit.addActionListener(e -> editWorker());
        btnDeactivate.addActionListener(e -> deactivateWorker());
        btnRefresh.addActionListener(e -> loadWorkers());
        btnBack.addActionListener(e -> dispose());

        loadWorkers();

        setVisible(true);
    }

    private JButton createButton(
            String text, Color background, Color foreground)
    {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(155, 42));

        return button;
    }

    private void styleFormPanel(JPanel panel)
    {
        panel.setBackground(IVORY);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (Component component : panel.getComponents())
        {
            if (component instanceof JLabel)
            {
                component.setFont(NORMAL_FONT);
                component.setForeground(BROWN);
            }
            else if (component instanceof JTextField)
            {
                component.setFont(NORMAL_FONT);
                component.setForeground(BROWN);
                component.setBackground(WHITE);
            }
        }
    }

    private void loadWorkers()
    {
        tableModel.setRowCount(0);

        String sql = "SELECT user_id, username, name, phone, email, is_active " +
                "FROM users WHERE role = 'Worker' ORDER BY user_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                Object[] row = {
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getBoolean("is_active") ? "Yes" : "No"
                };

                tableModel.addRow(row);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error loading workers: " + e.getMessage());
        }
    }

    private void addWorker()
    {
        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);
        JTextField nameField = new JTextField(18);
        JTextField phoneField = new JTextField(18);
        JTextField emailField = new JTextField(18);

        JPanel panel = new JPanel(new GridLayout(5, 2, 12, 14));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        styleFormPanel(panel);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add New Worker",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION)
            return;

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Username, password and name are required.");
            return;
        }

        String sql = "INSERT INTO users " +
                "(username, password, role, name, phone, email, is_active) " +
                "VALUES (?, ?, 'Worker', ?, ?, ?, TRUE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, phone);
            ps.setString(5, email);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Worker account created successfully.");

            loadWorkers();
        }
        catch (SQLIntegrityConstraintViolationException e)
        {
            JOptionPane.showMessageDialog(this,
                    "This username already exists. Use another username.");
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error creating worker: " + e.getMessage());
        }
    }

    private void editWorker()
    {
        int row = workerTable.getSelectedRow();

        if (row == -1)
        {
            JOptionPane.showMessageDialog(this,
                    "Select a worker to edit.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String username = tableModel.getValueAt(row, 1).toString();
        String name = tableModel.getValueAt(row, 2).toString();
        String phone = tableModel.getValueAt(row, 3) == null
                ? "" : tableModel.getValueAt(row, 3).toString();
        String email = tableModel.getValueAt(row, 4) == null
                ? "" : tableModel.getValueAt(row, 4).toString();

        JTextField usernameField = new JTextField(username, 18);
        JTextField nameField = new JTextField(name, 18);
        JTextField phoneField = new JTextField(phone, 18);
        JTextField emailField = new JTextField(email, 18);

        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 14));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        styleFormPanel(panel);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Worker Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION)
            return;

        String newUsername = usernameField.getText().trim();
        String newName = nameField.getText().trim();

        if (newUsername.isEmpty() || newName.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Username and name are required.");
            return;
        }

        String sql = "UPDATE users SET username = ?, name = ?, " +
                "phone = ?, email = ? " +
                "WHERE user_id = ? AND role = 'Worker'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, newUsername);
            ps.setString(2, newName);
            ps.setString(3, phoneField.getText().trim());
            ps.setString(4, emailField.getText().trim());
            ps.setInt(5, userId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Worker details updated successfully.");

            loadWorkers();
        }
        catch (SQLIntegrityConstraintViolationException e)
        {
            JOptionPane.showMessageDialog(this,
                    "This username already exists. Use another username.");
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error updating worker: " + e.getMessage());
        }
    }

    private void deactivateWorker()
    {
        int row = workerTable.getSelectedRow();

        if (row == -1)
        {
            JOptionPane.showMessageDialog(this,
                    "Select a worker to deactivate.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String active = tableModel.getValueAt(row, 5).toString();

        if (active.equals("No"))
        {
            JOptionPane.showMessageDialog(this,
                    "This worker is already inactive.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to deactivate this worker?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION)
            return;

        String sql = "UPDATE users SET is_active = FALSE " +
                "WHERE user_id = ? AND role = 'Worker'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Worker deactivated successfully.");

            loadWorkers();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating worker: " + e.getMessage());
        }
    }
}

