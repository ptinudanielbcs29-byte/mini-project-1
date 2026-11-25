package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class VehicleManagement extends JFrame {

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

    public VehicleManagement() {
        setTitle("Car Workshop | Vehicle Management");
        setSize(1150, 650);
        setMinimumSize(new Dimension(900, 520));
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

        searchField = new JTextField(18);
        styleTextField(searchField);

        JButton searchButton = createButton("Search", OLIVE, IVORY);
        JButton showAllButton = createButton("Show All", SAGE, BROWN);
        JButton addButton = createButton("Add Vehicle", OLIVE, IVORY);
        JButton editButton = createButton("Edit Vehicle", SAGE, BROWN);

        JLabel searchLabel = new JLabel("Vehicle Number / Owner:");
        searchLabel.setFont(BOLD_FONT);
        searchLabel.setForeground(BROWN);

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showAllButton);
        topPanel.add(addButton);
        topPanel.add(editButton);

        model = new DefaultTableModel(
                new String[]{
                        "Vehicle ID", "Customer ID", "Owner",
                        "Vehicle Number", "Brand", "Model", "Year"
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

        JLabel sectionTitle = new JLabel("Registered Vehicles");
        sectionTitle.setFont(new Font("Serif", Font.BOLD, 22));
        sectionTitle.setForeground(OLIVE);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setBackground(IVORY);
        tablePanel.add(sectionTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> loadVehicles(searchField.getText()));

        searchField.addActionListener(e -> loadVehicles(searchField.getText()));

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadVehicles("");
        });

        addButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());

        loadVehicles("");
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OLIVE);
        header.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("VEHICLE MANAGEMENT");
        title.setFont(new Font("Serif", Font.BOLD, 27));
        title.setForeground(IVORY);

        JLabel subtitle = new JLabel(
                "Register, search and update customer vehicles"
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
                BorderFactory.createEmptyBorder(9, 13, 9, 13)
        ));
        return button;
    }

    private JPanel createInputPanel(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 12, 14));
        panel.setBackground(IVORY);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(BOLD_FONT);
            label.setForeground(BROWN);

            styleTextField(fields[i]);

            panel.add(label);
            panel.add(fields[i]);
        }

        return panel;
    }

    void loadVehicles(String search) {
        model.setRowCount(0);

        String sql = "SELECT v.vehicle_id, v.customer_id, u.name, " +
                "v.vehicle_number, v.brand, v.model, v.manufacturing_year " +
                "FROM vehicles v JOIN users u ON v.customer_id = u.user_id " +
                "WHERE u.role = 'Customer' AND " +
                "(v.vehicle_number LIKE ? OR u.name LIKE ?) " +
                "ORDER BY v.vehicle_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + search + "%";
            ps.setString(1, value);
            ps.setString(2, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("vehicle_id"),
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("vehicle_number"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getObject("manufacturing_year")
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

    void addVehicle() {
        JTextField customerId = new JTextField();
        JTextField vehicleNumber = new JTextField();
        JTextField brand = new JTextField();
        JTextField vehicleModel = new JTextField();
        JTextField year = new JTextField();

        String[] labels = {
                "Customer ID:",
                "Vehicle Number:",
                "Brand:",
                "Model:",
                "Manufacturing Year:"
        };

        JTextField[] fields = {
                customerId, vehicleNumber, brand, vehicleModel, year
        };

        JPanel panel = createInputPanel(labels, fields);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Add Vehicle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (customerId.getText().trim().isEmpty() ||
                vehicleNumber.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Customer ID and vehicle number are required.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        Integer manufacturingYear = null;

        try {
            id = Integer.parseInt(customerId.getText().trim());

            if (!year.getText().trim().isEmpty()) {
                manufacturingYear = Integer.parseInt(year.getText().trim());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Customer ID and manufacturing year must be numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkSql = "SELECT user_id FROM users " +
                "WHERE user_id = ? AND role = 'Customer' AND is_active = TRUE";

        String insertSql = "INSERT INTO vehicles " +
                "(customer_id, vehicle_number, brand, model, manufacturing_year) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, id);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this,
                            "No active customer exists with that ID.",
                            "Customer Not Found",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, id);
                ps.setString(2, vehicleNumber.getText().trim());
                ps.setString(3, brand.getText().trim());
                ps.setString(4, vehicleModel.getText().trim());

                if (manufacturingYear == null) {
                    ps.setNull(5, Types.INTEGER);
                } else {
                    ps.setInt(5, manufacturingYear);
                }

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Vehicle added.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadVehicles(searchField.getText());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not add vehicle. Check the vehicle number and customer ID.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void editVehicle() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a vehicle first.",
                    "No Vehicle Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int vehicleId = (int) model.getValueAt(modelRow, 0);

        JTextField customerId =
                new JTextField(model.getValueAt(modelRow, 1).toString());

        JTextField vehicleNumber =
                new JTextField(model.getValueAt(modelRow, 3).toString());

        JTextField brand =
                new JTextField(model.getValueAt(modelRow, 4) == null ? "" :
                        model.getValueAt(modelRow, 4).toString());

        JTextField vehicleModel =
                new JTextField(model.getValueAt(modelRow, 5) == null ? "" :
                        model.getValueAt(modelRow, 5).toString());

        JTextField year =
                new JTextField(model.getValueAt(modelRow, 6) == null ? "" :
                        model.getValueAt(modelRow, 6).toString());

        String[] labels = {
                "Customer ID:",
                "Vehicle Number:",
                "Brand:",
                "Model:",
                "Manufacturing Year:"
        };

        JTextField[] fields = {
                customerId, vehicleNumber, brand, vehicleModel, year
        };

        JPanel panel = createInputPanel(labels, fields);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Vehicle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (customerId.getText().trim().isEmpty() ||
                vehicleNumber.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Customer ID and vehicle number are required.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        Integer manufacturingYear = null;

        try {
            id = Integer.parseInt(customerId.getText().trim());

            if (!year.getText().trim().isEmpty()) {
                manufacturingYear = Integer.parseInt(year.getText().trim());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Customer ID and manufacturing year must be numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkSql = "SELECT user_id FROM users " +
                "WHERE user_id = ? AND role = 'Customer' AND is_active = TRUE";

        String updateSql = "UPDATE vehicles SET customer_id = ?, " +
                "vehicle_number = ?, brand = ?, model = ?, manufacturing_year = ? " +
                "WHERE vehicle_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, id);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this,
                            "No active customer exists with that ID.",
                            "Customer Not Found",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, id);
                ps.setString(2, vehicleNumber.getText().trim());
                ps.setString(3, brand.getText().trim());
                ps.setString(4, vehicleModel.getText().trim());

                if (manufacturingYear == null) {
                    ps.setNull(5, Types.INTEGER);
                } else {
                    ps.setInt(5, manufacturingYear);
                }

                ps.setInt(6, vehicleId);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Vehicle updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadVehicles(searchField.getText());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not update vehicle. Check the vehicle number and customer ID.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
