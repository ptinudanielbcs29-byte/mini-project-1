package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class JobAssignment extends JFrame {

    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<WorkerItem> workerBox;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color WHITE = Color.WHITE;

    private final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 14);

    public JobAssignment() {
        setTitle("Car Workshop | Job Assignment");
        setSize(1150, 650);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(IVORY);

        add(createHeader(), BorderLayout.NORTH);

        String[] columns = {
                "Job ID", "Vehicle Number", "Service",
                "Status", "Assigned Worker", "Created Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        styleTable(jobTable);
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(SAGE));
        scrollPane.getViewport().setBackground(WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 12));
        contentPanel.setBackground(IVORY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(22, 25, 15, 25));

        JLabel sectionTitle = new JLabel("Service Job Details");
        sectionTitle.setFont(new Font("Serif", Font.BOLD, 22));
        sectionTitle.setForeground(OLIVE);

        contentPanel.add(sectionTitle, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        workerBox = new JComboBox<>();
        workerBox.setFont(NORMAL_FONT);
        workerBox.setPreferredSize(new Dimension(210, 36));
        workerBox.setBackground(WHITE);
        workerBox.setForeground(BROWN);

        JButton btnCreateJob = createButton("Create Job", OLIVE, IVORY);
        JButton btnAssign = createButton("Assign / Reassign", SAGE, BROWN);
        JButton btnUnassign = createButton("Unassign Job", CREAM, BROWN);
        JButton btnRefresh = createButton("Refresh", CREAM, BROWN);
        JButton btnClose = createButton("Close", OLIVE, IVORY);

        JPanel assignmentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        assignmentPanel.setBackground(CREAM);
        assignmentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, SAGE),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel workerLabel = new JLabel("Select Worker:");
        workerLabel.setFont(BOLD_FONT);
        workerLabel.setForeground(BROWN);

        assignmentPanel.add(btnCreateJob);
        assignmentPanel.add(workerLabel);
        assignmentPanel.add(workerBox);
        assignmentPanel.add(btnAssign);
        assignmentPanel.add(btnUnassign);
        assignmentPanel.add(btnRefresh);
        assignmentPanel.add(btnClose);

        add(assignmentPanel, BorderLayout.SOUTH);

        btnCreateJob.addActionListener(e -> createJob());
        btnAssign.addActionListener(e -> assignJob());
        btnUnassign.addActionListener(e -> unassignJob());
        btnRefresh.addActionListener(e -> refreshData());
        btnClose.addActionListener(e -> dispose());

        refreshData();
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OLIVE);
        header.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("JOB ASSIGNMENT");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(IVORY);

        JLabel subtitle = new JLabel(
                "Create service jobs and manage worker assignments"
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

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BOLD_FONT);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        background.equals(OLIVE) ? OLIVE : SAGE
                ),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void refreshData() {
        loadWorkers();
        loadJobs();
    }

    private void loadWorkers() {
        workerBox.removeAllItems();

        String sql = "SELECT user_id, name, username FROM users " +
                "WHERE role = 'Worker' AND is_active = TRUE " +
                "ORDER BY name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                workerBox.addItem(new WorkerItem(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading workers: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobs() {
        tableModel.setRowCount(0);

        String sql = "SELECT j.job_id, v.vehicle_number, s.service_name, " +
                "j.status, u.name AS worker_name, j.created_date " +
                "FROM jobs j " +
                "JOIN vehicles v ON j.vehicle_id = v.vehicle_id " +
                "JOIN services s ON j.service_id = s.service_id " +
                "LEFT JOIN users u ON j.worker_id = u.user_id " +
                "ORDER BY j.job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String workerName = rs.getString("worker_name");

                Object[] row = {
                        rs.getInt("job_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("service_name"),
                        rs.getString("status"),
                        workerName == null ? "Unassigned" : workerName,
                        rs.getDate("created_date")
                };

                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading jobs: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createJob() {
        JComboBox<VehicleItem> vehicleBox = new JComboBox<>();
        JComboBox<ServiceItem> serviceBox = new JComboBox<>();

        vehicleBox.setFont(NORMAL_FONT);
        serviceBox.setFont(NORMAL_FONT);

        loadVehiclesForJob(vehicleBox);
        loadServicesForJob(serviceBox);

        if (vehicleBox.getItemCount() == 0 ||
                serviceBox.getItemCount() == 0) {

            JOptionPane.showMessageDialog(this,
                    "Add at least one vehicle and one active service first.",
                    "Cannot Create Job",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 12, 15));
        panel.setBackground(IVORY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel vehicleLabel = new JLabel("Select Vehicle:");
        JLabel serviceLabel = new JLabel("Select Service:");

        vehicleLabel.setFont(BOLD_FONT);
        serviceLabel.setFont(BOLD_FONT);
        vehicleLabel.setForeground(BROWN);
        serviceLabel.setForeground(BROWN);

        panel.add(vehicleLabel);
        panel.add(vehicleBox);
        panel.add(serviceLabel);
        panel.add(serviceBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create Service Job",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        VehicleItem vehicle = (VehicleItem) vehicleBox.getSelectedItem();
        ServiceItem service = (ServiceItem) serviceBox.getSelectedItem();

        if (vehicle == null || service == null) {
            return;
        }

        String sql = "INSERT INTO jobs " +
                "(vehicle_id, service_id, worker_id, status, created_date) " +
                "VALUES (?, ?, NULL, 'Pending', CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicle.id);
            ps.setInt(2, service.id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Service job created successfully. " +
                            "Select it and assign a worker.",
                    "Job Created",
                    JOptionPane.INFORMATION_MESSAGE);

            loadJobs();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating job: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadVehiclesForJob(JComboBox<VehicleItem> vehicleBox) {
        String sql = "SELECT v.vehicle_id, v.vehicle_number, u.name " +
                "FROM vehicles v " +
                "JOIN users u ON v.customer_id = u.user_id " +
                "WHERE u.role = 'Customer' AND u.is_active = TRUE " +
                "ORDER BY v.vehicle_number";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehicleBox.addItem(new VehicleItem(
                        rs.getInt("vehicle_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading vehicles: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadServicesForJob(JComboBox<ServiceItem> serviceBox) {
        String sql = "SELECT service_id, service_name FROM services " +
                "WHERE is_active = TRUE ORDER BY service_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                serviceBox.addItem(new ServiceItem(
                        rs.getInt("service_id"),
                        rs.getString("service_name")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading services: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignJob() {
        int row = jobTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a job from the table first.",
                    "No Job Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        WorkerItem worker = (WorkerItem) workerBox.getSelectedItem();

        if (worker == null) {
            JOptionPane.showMessageDialog(this,
                    "No active workers are available. Create a worker first.",
                    "No Workers Available",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = jobTable.convertRowIndexToModel(row);
        int jobId = (int) tableModel.getValueAt(modelRow, 0);

        String sql = "UPDATE jobs SET worker_id = ? WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, worker.id);
            ps.setInt(2, jobId);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Job " + jobId + " assigned to " + worker.name + ".",
                        "Assignment Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Job not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error assigning job: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void unassignJob() {
        int row = jobTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a job from the table first.",
                    "No Job Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = jobTable.convertRowIndexToModel(row);
        int jobId = (int) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove the worker assignment for job " + jobId + "?",
                "Confirm Unassignment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE jobs SET worker_id = NULL WHERE job_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Job unassigned successfully.",
                        "Unassignment Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Job not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error unassigning job: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class WorkerItem {
        private int id;
        private String name;
        private String username;

        public WorkerItem(int id, String name, String username) {
            this.id = id;
            this.name = name;
            this.username = username;
        }

        @Override
        public String toString() {
            return name + " (" + username + ")";
        }
    }

    private static class VehicleItem {
        private int id;
        private String number;
        private String customerName;

        public VehicleItem(int id, String number, String customerName) {
            this.id = id;
            this.number = number;
            this.customerName = customerName;
        }

        @Override
        public String toString() {
            return number + " - " + customerName;
        }
    }

    private static class ServiceItem {
        private int id;
        private String name;

        public ServiceItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
