
package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class CustomerRecords extends JFrame {

    private final int customerId;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font TITLE_FONT =
            new Font("Serif", Font.BOLD, 27);

    private final Font NORMAL_FONT =
            new Font("SansSerif", Font.PLAIN, 14);

    private final Font TAB_FONT =
            new Font("SansSerif", Font.BOLD, 13);


    public CustomerRecords(int customerId) {

        this.customerId = customerId;

        setTitle("Car Workshop | My Records");
        setSize(1050, 650);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(IVORY);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(TAB_FONT);
        tabs.setBackground(CREAM);
        tabs.setForeground(BROWN);
        tabs.setBorder(
                BorderFactory.createEmptyBorder(8, 18, 12, 18));

        tabs.addTab("Profile", createProfilePanel());
        tabs.addTab("My Vehicles", createVehiclesPanel());
        tabs.addTab("My Appointments", createAppointmentsPanel());
        tabs.addTab("My Service Jobs", createJobsPanel());
        tabs.addTab("My Bills", createBillsPanel());

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }


    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OLIVE);
        header.setBorder(
                BorderFactory.createEmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("MY RECORDS");
        title.setFont(TITLE_FONT);
        title.setForeground(IVORY);

        JLabel subtitle = new JLabel(
                "Your profile, vehicles, appointments and service history");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        subtitle.setForeground(new Color(230, 230, 216));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(
                new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        return header;
    }


    private JPanel createProfilePanel() {

        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(IVORY);
        outer.setBorder(
                BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JPanel card = new JPanel(
                new GridLayout(0, 1, 10, 10));

        card.setBackground(WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAGE, 1),
                BorderFactory.createEmptyBorder(
                        25, 30, 25, 30)));

        JLabel heading = new JLabel("Customer Profile");
        heading.setFont(new Font("Serif", Font.BOLD, 22));
        heading.setForeground(OLIVE);

        card.add(heading);

        String sql =
                "SELECT username, name, phone, email " +
                        "FROM users WHERE user_id = ? AND role = 'Customer'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    addProfileDetail(
                            card, "Username", rs.getString("username"));

                    addProfileDetail(
                            card, "Full Name", rs.getString("name"));

                    addProfileDetail(
                            card, "Phone", rs.getString("phone"));

                    addProfileDetail(
                            card, "Email", rs.getString("email"));

                } else {

                    JLabel notFound =
                            new JLabel("Customer record not found.");

                    notFound.setFont(NORMAL_FONT);
                    notFound.setForeground(BROWN);

                    card.add(notFound);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading profile: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        outer.add(card, BorderLayout.NORTH);

        return outer;
    }


    private void addProfileDetail(
            JPanel panel, String label, String value) {

        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(WHITE);

        row.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, CREAM));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(
                new Font("SansSerif", Font.BOLD, 14));

        nameLabel.setForeground(OLIVE);

        JLabel valueLabel = new JLabel(
                value == null ? "-" : value);

        valueLabel.setFont(NORMAL_FONT);
        valueLabel.setForeground(BROWN);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);

        panel.add(row);
    }


    private JPanel createVehiclesPanel() {

        String sql =
                "SELECT vehicle_id, vehicle_number, brand, model, " +
                        "manufacturing_year FROM vehicles WHERE customer_id = ?";

        return createTablePanel(
                sql,
                new Object[]{customerId},
                new String[]{
                        "Vehicle ID", "Number", "Brand",
                        "Model", "Year"
                }
        );
    }


    private JPanel createAppointmentsPanel() {

        String sql =
                "SELECT a.appointment_id, v.vehicle_number, " +
                        "s.service_name, a.appointment_date, a.status " +
                        "FROM appointments a " +
                        "JOIN vehicles v ON a.vehicle_id = v.vehicle_id " +
                        "JOIN services s ON a.service_id = s.service_id " +
                        "WHERE a.customer_id = ? " +
                        "ORDER BY a.appointment_date DESC";

        return createTablePanel(
                sql,
                new Object[]{customerId},
                new String[]{
                        "Appointment ID", "Vehicle",
                        "Service", "Date", "Status"
                }
        );
    }


    private JPanel createJobsPanel() {

        String sql =
                "SELECT j.job_id, v.vehicle_number, " +
                        "s.service_name, j.status, j.created_date, " +
                        "j.completed_date " +
                        "FROM jobs j " +
                        "JOIN vehicles v ON j.vehicle_id = v.vehicle_id " +
                        "JOIN services s ON j.service_id = s.service_id " +
                        "WHERE v.customer_id = ? " +
                        "ORDER BY j.job_id DESC";

        return createTablePanel(
                sql,
                new Object[]{customerId},
                new String[]{
                        "Job ID", "Vehicle", "Service",
                        "Status", "Created Date", "Completed Date"
                }
        );
    }


    private JPanel createBillsPanel() {

        String sql =
                "SELECT b.bill_id, j.job_id, s.service_name, " +
                        "v.vehicle_number, " +
                        "s.price AS service_amount, " +
                        "(b.amount - s.price) AS gst, " +
                        "b.amount, b.payment_status, b.payment_date " +
                        "FROM bills b " +
                        "JOIN jobs j ON b.job_id = j.job_id " +
                        "JOIN vehicles v ON j.vehicle_id = v.vehicle_id " +
                        "JOIN services s ON j.service_id = s.service_id " +
                        "WHERE v.customer_id = ? " +
                        "ORDER BY b.bill_id DESC";

        return createTablePanel(
                sql,
                new Object[]{customerId},
                new String[]{
                        "Bill ID",
                        "Job ID",
                        "Service",
                        "Vehicle Number",
                        "Service Amount",
                        "GST",
                        "Final Amount",
                        "Payment Status",
                        "Payment Date"
                }
        );
    }


    private JPanel createTablePanel(
            String sql,
            Object[] parameters,
            String[] columns) {

        JPanel panel = new JPanel(new BorderLayout(0, 15));

        panel.setBackground(IVORY);

        panel.setBorder(
                BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel heading = new JLabel(getTableHeading(columns));

        heading.setFont(new Font("Serif", Font.BOLD, 21));
        heading.setForeground(OLIVE);

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {

                        return false;
                    }
                };

        JTable table = new JTable(model);

        table.setFont(NORMAL_FONT);
        table.setForeground(BROWN);
        table.setBackground(WHITE);
        table.setRowHeight(34);
        table.setSelectionBackground(SAGE);
        table.setSelectionForeground(BROWN);
        table.setGridColor(CREAM);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(TAB_FONT);
        tableHeader.setBackground(OLIVE);
        tableHeader.setForeground(IVORY);
        tableHeader.setPreferredSize(new Dimension(0, 40));
        tableHeader.setReorderingAllowed(false);

        if (columns.length > 6) {
            table.setAutoResizeMode(
                    JTable.AUTO_RESIZE_OFF);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(SAGE));

        scrollPane.getViewport().setBackground(WHITE);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Object[] row = new Object[columns.length];

                    for (int i = 0; i < columns.length; i++) {
                        row[i] = rs.getObject(i + 1);
                    }

                    model.addRow(row);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading records: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        if (model.getRowCount() == 0) {

            JLabel emptyLabel = new JLabel(
                    "No records available.",
                    SwingConstants.CENTER);

            emptyLabel.setFont(NORMAL_FONT);
            emptyLabel.setForeground(BROWN);

            emptyLabel.setBorder(
                    BorderFactory.createEmptyBorder(
                            12, 0, 12, 0));

            JPanel tableContainer = new JPanel(
                    new BorderLayout());

            tableContainer.setBackground(IVORY);

            tableContainer.add(
                    scrollPane, BorderLayout.CENTER);

            tableContainer.add(
                    emptyLabel, BorderLayout.SOUTH);

            panel.add(heading, BorderLayout.NORTH);
            panel.add(tableContainer, BorderLayout.CENTER);

        } else {

            panel.add(heading, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        return panel;
    }


    private String getTableHeading(String[] columns) {

        if (columns.length > 0) {

            switch (columns[0]) {

                case "Vehicle ID":
                    return "My Vehicles";

                case "Appointment ID":
                    return "My Appointments";

                case "Job ID":
                    return "My Service Jobs";

                case "Bill ID":
                    return "My Bills";

                default:
                    return "My Records";
            }
        }

        return "My Records";
    }
}
