
package carworkshop2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class AppointmentManagement extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    int customerId;
    boolean customerMode;

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


    public AppointmentManagement() {
        customerId = 0;
        customerMode = false;
        buildPage();
    }


    public AppointmentManagement(int customerId) {
        this.customerId = customerId;
        customerMode = true;
        buildPage();
    }


    void buildPage() {

        setTitle("Car Workshop | Appointment Management");
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

        loadAppointments("");

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

        JLabel title = new JLabel("Appointment Management");
        title.setFont(TITLE_FONT);
        title.setForeground(BROWN);

        JLabel description = new JLabel(
                customerMode
                        ? "Request and track your service appointments."
                        : "View, search and manage customer appointments.");

        description.setFont(BODY_FONT);
        description.setForeground(MUTED);

        titlePanel.add(smallTitle);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(description);

        JLabel role = new JLabel(
                customerMode ? "CUSTOMER PORTAL" : "ADMINISTRATION");

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

        searchField = new JTextField(20);
        styleTextField(searchField);

        JButton searchButton = createButton("Search", true);
        JButton showAllButton = createButton("Show All", false);
        JButton addButton = createButton(
                "+  Request Appointment", true);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));

        actionPanel.setOpaque(false);

        actionPanel.add(addButton);

        JButton statusButton = null;

        if (!customerMode) {
            statusButton = createButton("Update Status", false);
            actionPanel.add(statusButton);
        }

        toolbar.add(searchPanel, BorderLayout.WEST);
        toolbar.add(actionPanel, BorderLayout.EAST);

        searchButton.addActionListener(
                e -> loadAppointments(searchField.getText().trim()));

        searchField.addActionListener(
                e -> loadAppointments(searchField.getText().trim()));

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadAppointments("");
        });

        addButton.addActionListener(e -> addAppointment());

        if (!customerMode) {
            statusButton.addActionListener(e -> updateStatus());
        }

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
                        "Appointment ID",
                        "Customer ID",
                        "Customer Name",
                        "Vehicle Number",
                        "Service",
                        "Appointment Date",
                        "Status"
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

    void loadAppointments(String search) {

        model.setRowCount(0);

        String sql =
                "SELECT a.appointment_id, a.customer_id, u.name, " +
                        "v.vehicle_number, s.service_name, a.appointment_date, a.status " +
                        "FROM appointments a " +
                        "JOIN users u ON a.customer_id = u.user_id " +
                        "JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                        "JOIN services s ON a.service_id = s.service_id " +
                        "WHERE (u.name LIKE ? OR v.vehicle_number LIKE ?) ";

        if (customerMode) {
            sql += "AND a.customer_id = ? ";
        }

        sql += "ORDER BY a.appointment_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String value = "%" + search + "%";

            ps.setString(1, value);
            ps.setString(2, value);

            if (customerMode) {
                ps.setInt(3, customerId);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    model.addRow(new Object[]{
                            rs.getInt("appointment_id"),
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("vehicle_number"),
                            rs.getString("service_name"),
                            rs.getDate("appointment_date"),
                            rs.getString("status")
                    });
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load appointments:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void addAppointment() {

        JTextField customerField = new JTextField();
        JTextField vehicleField = new JTextField();
        JTextField serviceField = new JTextField();
        JTextField dateField =
                new JTextField(LocalDate.now().toString());

        styleTextField(customerField);
        styleTextField(vehicleField);
        styleTextField(serviceField);
        styleTextField(dateField);

        customerField.setEditable(true);
        vehicleField.setEditable(true);
        serviceField.setEditable(true);
        dateField.setEditable(true);

        customerField.setEnabled(true);
        vehicleField.setEnabled(true);
        serviceField.setEnabled(true);
        dateField.setEnabled(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(IVORY);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        if (customerMode) {

            addAppointmentField(
                    form, gbc, row++,
                    "Your Customer ID",
                    new JLabel(String.valueOf(customerId)));

        } else {

            addAppointmentField(
                    form, gbc, row++,
                    "Customer ID",
                    customerField);
        }

        addAppointmentField(
                form, gbc, row++,
                "Vehicle ID",
                vehicleField);

        addAppointmentField(
                form, gbc, row++,
                "Service ID",
                serviceField);

        addAppointmentField(
                form, gbc, row++,
                "Appointment Date",
                dateField);

        JLabel dateHint = new JLabel("Format: YYYY-MM-DD");
        dateHint.setFont(
                new Font("SansSerif", Font.ITALIC, 10));
        dateHint.setForeground(MUTED);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 10, 5, 10);

        form.add(dateHint, gbc);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Request Appointment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        int cId = customerId;
        int vId;
        int sId;
        java.sql.Date appointmentDate;

        try {

            if (!customerMode) {
                cId = Integer.parseInt(
                        customerField.getText().trim());
            }

            vId = Integer.parseInt(
                    vehicleField.getText().trim());

            sId = Integer.parseInt(
                    serviceField.getText().trim());

            appointmentDate = java.sql.Date.valueOf(
                    dateField.getText().trim());

            if (appointmentDate.toLocalDate().isBefore(LocalDate.now())) {

                JOptionPane.showMessageDialog(
                        this,
                        "Appointment date cannot be in the past.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid IDs and a date in YYYY-MM-DD format.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        String checkSql =
                "SELECT v.vehicle_id FROM vehicles v " +
                        "JOIN users u ON v.customer_id = u.user_id " +
                        "WHERE v.vehicle_id = ? AND v.customer_id = ? " +
                        "AND u.role = 'Customer' AND u.is_active = TRUE";

        String serviceSql =
                "SELECT service_id FROM services " +
                        "WHERE service_id = ? AND is_active = TRUE";

        String insertSql =
                "INSERT INTO appointments " +
                        "(customer_id, vehicle_id, service_id, appointment_date, status) " +
                        "VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection con = DBConnection.getConnection()) {

            try (PreparedStatement ps =
                         con.prepareStatement(checkSql)) {

                ps.setInt(1, vId);
                ps.setInt(2, cId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "The vehicle does not belong to that active customer.",
                                "Vehicle Verification",
                                JOptionPane.WARNING_MESSAGE);

                        return;
                    }
                }
            }

            try (PreparedStatement ps =
                         con.prepareStatement(serviceSql)) {

                ps.setInt(1, sId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Active service not found. Check the Service ID.",
                                "Service Verification",
                                JOptionPane.WARNING_MESSAGE);

                        return;
                    }
                }
            }

            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setInt(1, cId);
                ps.setInt(2, vId);
                ps.setInt(3, sId);
                ps.setDate(4, appointmentDate);

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Appointment requested successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadAppointments(searchField.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to create appointment:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }



    private void addAppointmentField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            Component component) {

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        label.setForeground(BROWN);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(10, 10, 10, 15);

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(component, gbc);
    }

    void updateStatus() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select an appointment first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        String[] statuses = {
                "Pending",
                "Approved",
                "Rejected",
                "Completed"
        };

        JComboBox<String> statusBox =
                new JComboBox<>(statuses);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(IVORY);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel(
                "Choose the new appointment status:");

        label.setFont(BODY_FONT);
        label.setForeground(BROWN);

        statusBox.setFont(BODY_FONT);
        statusBox.setBackground(CREAM);
        statusBox.setForeground(BROWN);

        panel.add(label, BorderLayout.NORTH);
        panel.add(statusBox, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Appointment Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String status =
                (String) statusBox.getSelectedItem();

        String sql =
                "UPDATE appointments SET status = ? " +
                        "WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Appointment status updated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadAppointments(searchField.getText().trim());

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update appointment:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
                new EmptyBorder(11, 16, 11, 16));

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
                "AUTO ATELIER  |  APPOINTMENT MANAGEMENT");

        left.setFont(
                new Font("SansSerif", Font.PLAIN, 10));

        left.setForeground(MUTED);

        JLabel right = new JLabel(
                customerMode
                        ? "CUSTOMER PORTAL"
                        : "WORKSHOP ADMINISTRATION");

        right.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        right.setForeground(OLIVE);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }
}
