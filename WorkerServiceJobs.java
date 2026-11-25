package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class WorkerServiceJobs extends JFrame
{
    private int workerId;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusBox;

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

    public WorkerServiceJobs(int workerId)
    {
        this.workerId = workerId;

        setTitle("Car Workshop - My Service Jobs");
        setSize(1100, 650);
        setMinimumSize(new Dimension(900, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(IVORY);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(
                headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(IVORY);
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 20, 35));

        JLabel titleLabel = new JLabel("My Assigned Service Jobs");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(OLIVE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "View your assigned jobs and update their progress");
        subtitleLabel.setFont(NORMAL_FONT);
        subtitleLabel.setForeground(BROWN);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {
                "Job ID", "Vehicle Number", "Service",
                "Status", "Created Date", "Completed Date"
        };

        tableModel = new DefaultTableModel(columns, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        jobTable.setFont(NORMAL_FONT);
        jobTable.setForeground(BROWN);
        jobTable.setBackground(WHITE);
        jobTable.setSelectionBackground(SAGE);
        jobTable.setSelectionForeground(DARK_OLIVE);
        jobTable.setRowHeight(40);
        jobTable.setShowGrid(false);
        jobTable.setIntercellSpacing(new Dimension(0, 0));
        jobTable.setFillsViewportHeight(true);
        jobTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = jobTable.getTableHeader();
        tableHeader.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        tableHeader.setBackground(OLIVE);
        tableHeader.setForeground(WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 45));
        tableHeader.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(SAGE, 1));
        scrollPane.getViewport().setBackground(WHITE);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(IVORY);
        tableContainer.setBorder(
                BorderFactory.createEmptyBorder(5, 35, 20, 35));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        statusBox = new JComboBox<>(
                new String[]{"Pending", "In Progress", "Completed"});

        statusBox.setFont(NORMAL_FONT);
        statusBox.setForeground(BROWN);
        statusBox.setBackground(WHITE);
        statusBox.setPreferredSize(new Dimension(155, 38));

        JLabel statusLabel = new JLabel("New Status:");
        statusLabel.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(BROWN);

        JButton btnUpdate = createButton(
                "Update Job Status", OLIVE, WHITE);

        JButton btnRefresh = createButton(
                "Refresh", SAGE, DARK_OLIVE);

        JButton btnClose = createButton(
                "Close", DARK_OLIVE, WHITE);

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 14, 15));
        bottomPanel.setBackground(CREAM);
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 15));

        bottomPanel.add(statusLabel);
        bottomPanel.add(statusBox);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);

        add(bottomPanel, BorderLayout.SOUTH);

        btnUpdate.addActionListener(e -> updateJobStatus());
        btnRefresh.addActionListener(e -> loadJobs());
        btnClose.addActionListener(e -> dispose());

        loadJobs();

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
        button.setPreferredSize(new Dimension(165, 42));

        return button;
    }

    private void loadJobs()
    {
        tableModel.setRowCount(0);

        String sql = "SELECT j.job_id, v.vehicle_number, s.service_name, " +
                "j.status, j.created_date, j.completed_date " +
                "FROM jobs j " +
                "JOIN vehicles v ON j.vehicle_id = v.vehicle_id " +
                "JOIN services s ON j.service_id = s.service_id " +
                "WHERE j.worker_id = ? ORDER BY j.job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, workerId);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Object[] row = {
                            rs.getInt("job_id"),
                            rs.getString("vehicle_number"),
                            rs.getString("service_name"),
                            rs.getString("status"),
                            rs.getDate("created_date"),
                            rs.getDate("completed_date")
                    };

                    tableModel.addRow(row);
                }
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error loading service jobs: " + e.getMessage());
        }
    }

    private void updateJobStatus()
    {
        int row = jobTable.getSelectedRow();

        if (row == -1)
        {
            JOptionPane.showMessageDialog(this,
                    "Select a service job first.");
            return;
        }

        int jobId = (int) tableModel.getValueAt(row, 0);
        String newStatus = statusBox.getSelectedItem().toString();

        String sql = "UPDATE jobs SET status = ?, " +
                "completed_date = CASE WHEN ? = 'Completed' " +
                "THEN CURDATE() ELSE NULL END " +
                "WHERE job_id = ? AND worker_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, newStatus);
            ps.setString(2, newStatus);
            ps.setInt(3, jobId);
            ps.setInt(4, workerId);

            int updated = ps.executeUpdate();

            if (updated > 0)
            {
                JOptionPane.showMessageDialog(this,
                        "Job status updated successfully.");

                loadJobs();
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                        "Job was not found or is not assigned to you.");

                loadJobs();
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error updating job: " + e.getMessage());
        }
    }
}

