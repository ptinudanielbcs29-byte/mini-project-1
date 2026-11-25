package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ServiceManagement extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 13);

    public ServiceManagement() {
        setTitle("Car Workshop | Service Management");
        setSize(1100, 650);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(IVORY);

        add(createHeader(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(IVORY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(CREAM);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAGE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        searchField = new JTextField(17);
        styleTextField(searchField);

        JButton searchButton = createButton("Search", OLIVE, IVORY);
        JButton showAllButton = createButton("Show All", SAGE, BROWN);
        JButton addButton = createButton("Add Service", OLIVE, IVORY);
        JButton editButton = createButton("Edit Service", SAGE, BROWN);
        JButton deactivateButton = createButton("Deactivate", CREAM, BROWN);

        JLabel searchLabel = new JLabel("Service Name:");
        searchLabel.setFont(BOLD_FONT);
        searchLabel.setForeground(BROWN);

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showAllButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deactivateButton);

        model = new DefaultTableModel(
                new String[]{
                        "ID", "Service Name", "Description", "Price", "Active"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(SAGE));
        scrollPane.getViewport().setBackground(WHITE);

        JLabel sectionTitle = new JLabel("Available Workshop Services");
        sectionTitle.setFont(new Font("Serif", Font.BOLD, 22));
        sectionTitle.setForeground(OLIVE);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setBackground(IVORY);
        tablePanel.add(sectionTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e ->
                loadServices(searchField.getText()));

        searchField.addActionListener(e ->
                loadServices(searchField.getText()));

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadServices("");
        });

        addButton.addActionListener(e -> addService());
        editButton.addActionListener(e -> editService());
        deactivateButton.addActionListener(e -> deactivateService());

        loadServices("");
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OLIVE);
        header.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("SERVICE MANAGEMENT");
        title.setFont(new Font("Serif", Font.BOLD, 27));
        title.setForeground(IVORY);

        JLabel subtitle = new JLabel(
                "Manage service details, pricing and availability"
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

    private void styleTable(JTable table) {
        table.setFont(NORMAL_FONT);
        table.setForeground(BROWN);
        table.setBackground(WHITE);
        table.setRowHeight(36);
        table.setSelectionBackground(SAGE);
        table.setSelectionForeground(BROWN);
        table.setGridColor(CREAM);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(BOLD_FONT);
        header.setBackground(OLIVE);
        header.setForeground(IVORY);
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);
    }

    private void styleTextField(JTextField field) {
        field.setFont(NORMAL_FONT);
        field.setForeground(BROWN);
        field.setBackground(WHITE);
        field.setCaretColor(OLIVE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAGE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BOLD_FONT);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        background.equals(OLIVE) ? OLIVE : SAGE
                ),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)
        ));
        return button;
    }

    void loadServices(String search) {
        model.setRowCount(0);

        String sql = "SELECT service_id, service_name, description, price, is_active " +
                "FROM services WHERE service_name LIKE ? ORDER BY service_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + search + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("service_id"),
                            rs.getString("service_name"),
                            rs.getString("description"),
                            rs.getBigDecimal("price"),
                            rs.getBoolean("is_active") ? "Yes" : "No"
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void addService() {
        JTextField name = new JTextField();
        JTextField description = new JTextField();
        JTextField price = new JTextField();

        styleTextField(name);
        styleTextField(description);
        styleTextField(price);

        JPanel panel = createInputPanel(
                new String[]{"Service Name:", "Description:", "Price:"},
                new JTextField[]{name, description, price}
        );

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add Service",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty() ||
                price.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Service name and price are required.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double servicePrice = Double.parseDouble(price.getText().trim());

            if (servicePrice < 0) {
                JOptionPane.showMessageDialog(this,
                        "Price cannot be negative.",
                        "Invalid Price",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO services " +
                    "(service_name, description, price) VALUES (?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name.getText().trim());
                ps.setString(2, description.getText().trim());
                ps.setDouble(3, servicePrice);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Service added.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadServices(searchField.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid price.",
                    "Invalid Price",
                    JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void editService() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a service first.",
                    "No Service Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) model.getValueAt(modelRow, 0);

        JTextField name = new JTextField(model.getValueAt(modelRow, 1).toString());
        JTextField description = new JTextField(
                model.getValueAt(modelRow, 2) == null ? "" :
                        model.getValueAt(modelRow, 2).toString()
        );
        JTextField price = new JTextField(model.getValueAt(modelRow, 3).toString());

        styleTextField(name);
        styleTextField(description);
        styleTextField(price);

        JPanel panel = createInputPanel(
                new String[]{"Service Name:", "Description:", "Price:"},
                new JTextField[]{name, description, price}
        );

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Service",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Service name is required.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double servicePrice = Double.parseDouble(price.getText().trim());

            if (servicePrice < 0) {
                JOptionPane.showMessageDialog(this,
                        "Price cannot be negative.",
                        "Invalid Price",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "UPDATE services SET service_name = ?, " +
                    "description = ?, price = ? WHERE service_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name.getText().trim());
                ps.setString(2, description.getText().trim());
                ps.setDouble(3, servicePrice);
                ps.setInt(4, id);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Service updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadServices(searchField.getText());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid price.",
                    "Invalid Price",
                    JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void deactivateService() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a service first.",
                    "No Service Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) model.getValueAt(modelRow, 0);

        if ("No".equals(model.getValueAt(modelRow, 4).toString())) {
            JOptionPane.showMessageDialog(this,
                    "Service is already inactive.",
                    "Already Inactive",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Deactivate this service?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE services SET is_active = FALSE WHERE service_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Service deactivated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadServices(searchField.getText());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createInputPanel(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 12, 14));
        panel.setBackground(IVORY);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(BOLD_FONT);
            label.setForeground(BROWN);

            panel.add(label);
            panel.add(fields[i]);
        }

        return panel;
    }
}