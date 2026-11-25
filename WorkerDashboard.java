
package carworkshop2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WorkerDashboard extends JFrame {

    private int userId;
    private String name;

    private final Color IVORY = new Color(250, 248, 241);
    private final Color CREAM = new Color(242, 238, 226);
    private final Color OLIVE = new Color(83, 96, 69);
    private final Color DARK_OLIVE = new Color(65, 76, 54);
    private final Color SAGE = new Color(177, 190, 164);
    private final Color LIGHT_SAGE = new Color(224, 230, 215);
    private final Color BROWN = new Color(91, 75, 58);
    private final Color MUTED = new Color(137, 132, 115);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER = new Color(225, 221, 207);

    private final Font TITLE_FONT =
            new Font("Serif", Font.PLAIN, 30);

    private final Font HEADING_FONT =
            new Font("Serif", Font.PLAIN, 23);

    private final Font BODY_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    private final Font SMALL_FONT =
            new Font("SansSerif", Font.PLAIN, 11);


    public WorkerDashboard(int userId, String name) {

        this.userId = userId;
        this.name = name;

        setTitle("Car Workshop | Worker Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);

        buildDashboard();

        setVisible(true);
    }




    private void buildDashboard() {

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(IVORY);

        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createHomePanel(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }




    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout());

        sidebar.setPreferredSize(new Dimension(225, 0));
        sidebar.setBackground(DARK_OLIVE);

        sidebar.setBorder(
                new EmptyBorder(28, 18, 20, 18));

        JPanel topPanel = new JPanel();

        topPanel.setOpaque(false);

        topPanel.setLayout(
                new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("Auto Atelier");

        brand.setFont(
                new Font("Serif", Font.BOLD, 25));

        brand.setForeground(IVORY);

        JLabel subtitle = new JLabel("CAR WORKSHOP MANAGEMENT");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 9));

        subtitle.setForeground(SAGE);

        topPanel.add(brand);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitle);

        topPanel.add(Box.createVerticalStrut(45));

        JLabel menuHeading = new JLabel("MY WORKSPACE");

        menuHeading.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        menuHeading.setForeground(SAGE);

        topPanel.add(menuHeading);
        topPanel.add(Box.createVerticalStrut(15));

        JButton dashboardButton =
                createSidebarButton("Dashboard", true);

        JButton performanceButton =
                createSidebarButton("My Performance", false);

        JButton jobsButton =
                createSidebarButton("Service Jobs", false);

        topPanel.add(dashboardButton);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(performanceButton);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(jobsButton);

        dashboardButton.addActionListener(e -> showHome());

        performanceButton.addActionListener(
                e -> openPerformance());

        jobsButton.addActionListener(
                e -> openServiceJobs());

        JPanel bottomPanel = new JPanel();

        bottomPanel.setOpaque(false);

        bottomPanel.setLayout(
                new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JLabel signedIn = new JLabel("SIGNED IN AS");

        signedIn.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        signedIn.setForeground(SAGE);

        JLabel workerName = new JLabel(name);

        workerName.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        workerName.setForeground(IVORY);

        JButton logoutButton =
                createSidebarButton("↪   Logout", false);

        logoutButton.addActionListener(e -> logout());

        bottomPanel.add(signedIn);
        bottomPanel.add(Box.createVerticalStrut(7));
        bottomPanel.add(workerName);
        bottomPanel.add(Box.createVerticalStrut(18));
        bottomPanel.add(logoutButton);

        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }




    private JButton createSidebarButton(
            String text,
            boolean selected) {

        JButton button = new JButton(text);

        button.setFont(BODY_FONT);

        button.setHorizontalAlignment(
                SwingConstants.LEFT);

        button.setForeground(
                selected ? DARK_OLIVE : IVORY);

        button.setBackground(
                selected ? IVORY : DARK_OLIVE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setBorder(
                new EmptyBorder(11, 12, 11, 10));

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42));

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        if (!selected) {

            button.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(OLIVE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(DARK_OLIVE);
                }
            });
        }

        return button;
    }



    private JPanel createHomePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(IVORY);

        panel.setBorder(
                new EmptyBorder(38, 35, 25, 35));

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS));


        JLabel welcomeLabel = new JLabel("WELCOME BACK");

        welcomeLabel.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        welcomeLabel.setForeground(OLIVE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);

        nameLabel.setFont(TITLE_FONT);
        nameLabel.setForeground(BROWN);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "Your workshop activities and performance, all in one place.");

        description.setFont(BODY_FONT);
        description.setForeground(MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(welcomeLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(nameLabel);
        content.add(Box.createVerticalStrut(7));
        content.add(description);

        content.add(Box.createVerticalStrut(35));



        JLabel sectionTitle = new JLabel("Your Workshop Space");

        sectionTitle.setFont(HEADING_FONT);
        sectionTitle.setForeground(BROWN);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionDescription = new JLabel(
                "Manage your assigned service jobs and track your performance.");

        sectionDescription.setFont(SMALL_FONT);
        sectionDescription.setForeground(MUTED);
        sectionDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(6));
        content.add(sectionDescription);

        content.add(Box.createVerticalStrut(25));


        JPanel cardsPanel = new JPanel(
                new GridLayout(1, 2, 20, 0));

        cardsPanel.setOpaque(false);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel performanceCard = createFeatureCard(
                "01",
                "My Performance",
                "View your total assigned jobs, completed jobs and service-wise completion percentage.",
                "View Performance  →",
                this::openPerformance
        );

        JPanel jobsCard = createFeatureCard(
                "02",
                "Service Jobs",
                "View your assigned service jobs, customer details and update job status.",
                "Manage Service Jobs  →",
                this::openServiceJobs
        );

        cardsPanel.add(performanceCard);
        cardsPanel.add(jobsCard);

        cardsPanel.setPreferredSize(
                new Dimension(900, 360));

        cardsPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 450));

        content.add(cardsPanel);

        panel.add(content, BorderLayout.CENTER);



        JPanel footer = new JPanel(new BorderLayout());

        footer.setOpaque(false);

        JLabel footerLeft = new JLabel(
                "AUTO ATELIER  |  WORKER WORKSPACE");

        footerLeft.setFont(
                new Font("SansSerif", Font.PLAIN, 9));

        footerLeft.setForeground(MUTED);

        JLabel footerRight = new JLabel("WORKER PANEL");

        footerRight.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        footerRight.setForeground(OLIVE);

        footer.add(footerLeft, BorderLayout.WEST);
        footer.add(footerRight, BorderLayout.EAST);

        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }



    private JPanel createFeatureCard(
            String number,
            String heading,
            String description,
            String actionText,
            Runnable action) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(CREAM);

        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(22, 22, 20, 22)));

        JPanel cardContent = new JPanel();

        cardContent.setOpaque(false);

        cardContent.setLayout(
                new BoxLayout(cardContent, BoxLayout.Y_AXIS));

        JLabel numberLabel = new JLabel(number);

        numberLabel.setFont(
                new Font("Serif", Font.ITALIC, 17));

        numberLabel.setForeground(OLIVE);

        JLabel titleLabel = new JLabel(heading);

        titleLabel.setFont(
                new Font("Serif", Font.PLAIN, 21));

        titleLabel.setForeground(BROWN);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:250px;'>"
                        + description
                        + "</div></html>");

        descriptionLabel.setFont(BODY_FONT);
        descriptionLabel.setForeground(BROWN);

        cardContent.add(numberLabel);
        cardContent.add(Box.createVerticalStrut(27));
        cardContent.add(titleLabel);
        cardContent.add(Box.createVerticalStrut(10));
        cardContent.add(descriptionLabel);

        card.add(cardContent, BorderLayout.CENTER);

        JButton actionButton = new JButton(actionText);

        actionButton.setFont(
                new Font("SansSerif", Font.PLAIN, 11));

        actionButton.setForeground(OLIVE);
        actionButton.setBackground(LIGHT_SAGE);

        actionButton.setHorizontalAlignment(
                SwingConstants.LEFT);

        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setOpaque(true);

        actionButton.setBorder(
                new EmptyBorder(10, 13, 10, 10));

        actionButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        actionButton.addActionListener(e -> action.run());

        actionButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                actionButton.setBackground(SAGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                actionButton.setBackground(LIGHT_SAGE);
            }
        });

        card.add(actionButton, BorderLayout.SOUTH);

        return card;
    }



    private void showHome() {

        getContentPane().revalidate();
        getContentPane().repaint();
    }



    private void openServiceJobs() {


        new WorkerServiceJobs(userId);
    }



    private void openPerformance() {

        JFrame frame = new JFrame("My Performance");

        frame.setSize(1050, 680);
        frame.setMinimumSize(new Dimension(850, 550));
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setLayout(new BorderLayout());

        JPanel main = new JPanel(new BorderLayout(0, 20));

        main.setBackground(IVORY);

        main.setBorder(
                new EmptyBorder(25, 30, 25, 30));


        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel small = new JLabel("WORKER ANALYTICS");

        small.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        small.setForeground(OLIVE);
        small.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("My Performance");

        title.setFont(TITLE_FONT);
        title.setForeground(BROWN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Track your assigned work and service-wise completion.");

        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(small);
        header.add(Box.createVerticalStrut(7));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        main.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 18));

        center.setOpaque(false);

        JPanel summary = new JPanel(new BorderLayout());

        summary.setBackground(CREAM);

        summary.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(25, 25, 22, 25)));

        JLabel loading = new JLabel("Loading performance...");

        loading.setForeground(BROWN);
        loading.setFont(BODY_FONT);

        summary.add(loading, BorderLayout.CENTER);

        center.add(summary, BorderLayout.NORTH);

        String[] columns = {
                "Service",
                "Assigned Jobs",
                "Completed Jobs",
                "Pending / In Progress",
                "Completion %"
        };

        DefaultTableModel tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {
                        return false;
                    }
                };

        JTable performanceTable = new JTable(tableModel);

        performanceTable.setFont(BODY_FONT);
        performanceTable.setForeground(BROWN);
        performanceTable.setBackground(WHITE);
        performanceTable.setRowHeight(40);
        performanceTable.setGridColor(BORDER);
        performanceTable.setShowVerticalLines(false);
        performanceTable.setSelectionBackground(LIGHT_SAGE);
        performanceTable.setSelectionForeground(BROWN);

        JTableHeader tableHeader =
                performanceTable.getTableHeader();

        tableHeader.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        tableHeader.setBackground(OLIVE);
        tableHeader.setForeground(WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 42));

        JScrollPane scrollPane =
                new JScrollPane(performanceTable);

        scrollPane.setBorder(
                new LineBorder(BORDER, 1));

        scrollPane.getViewport().setBackground(WHITE);

        JPanel tablePanel = new JPanel(
                new BorderLayout(0, 10));

        tablePanel.setOpaque(false);

        JLabel serviceHeading = new JLabel(
                "Service-wise Performance");

        serviceHeading.setFont(HEADING_FONT);
        serviceHeading.setForeground(BROWN);

        tablePanel.add(serviceHeading, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        center.add(tablePanel, BorderLayout.CENTER);

        main.add(center, BorderLayout.CENTER);


        JPanel footer = new JPanel(new BorderLayout());

        footer.setOpaque(false);

        JLabel footerText = new JLabel(
                "AUTO ATELIER  |  WORKER PERFORMANCE");

        footerText.setFont(
                new Font("SansSerif", Font.PLAIN, 9));

        footerText.setForeground(MUTED);

        JButton closeButton = new JButton("Close");

        closeButton.setFont(BODY_FONT);
        closeButton.setForeground(OLIVE);
        closeButton.setBackground(LIGHT_SAGE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);

        closeButton.addActionListener(e -> frame.dispose());

        footer.add(footerText, BorderLayout.WEST);
        footer.add(closeButton, BorderLayout.EAST);

        main.add(footer, BorderLayout.SOUTH);

        frame.add(main);

        frame.setVisible(true);

        loadPerformance(summary, loading, tableModel);
    }


    private void loadPerformance(
            JPanel summary,
            JLabel loading,
            DefaultTableModel tableModel) {

        String overallSql =
                "SELECT COUNT(*) AS total_jobs, " +
                        "SUM(CASE WHEN LOWER(status) = 'completed' " +
                        "THEN 1 ELSE 0 END) AS completed_jobs " +
                        "FROM jobs WHERE worker_id = ?";

        String serviceSql =
                "SELECT s.service_name, " +
                        "COUNT(j.job_id) AS assigned_jobs, " +
                        "SUM(CASE WHEN LOWER(j.status) = 'completed' " +
                        "THEN 1 ELSE 0 END) AS completed_jobs " +
                        "FROM jobs j " +
                        "JOIN services s ON j.service_id = s.service_id " +
                        "WHERE j.worker_id = ? " +
                        "GROUP BY s.service_id, s.service_name " +
                        "ORDER BY s.service_name";

        try (Connection con = DBConnection.getConnection()) {

            int totalJobs = 0;
            int completedJobs = 0;


            try (PreparedStatement ps =
                         con.prepareStatement(overallSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        totalJobs = rs.getInt("total_jobs");

                        completedJobs =
                                rs.getInt("completed_jobs");
                    }
                }
            }

            int pendingJobs = totalJobs - completedJobs;

            double overallPercentage = totalJobs == 0
                    ? 0
                    : (completedJobs * 100.0 / totalJobs);


            summary.removeAll();

            JPanel statistics = new JPanel(
                    new GridLayout(2, 2, 30, 25));

            statistics.setOpaque(false);

            statistics.add(createStatItem(
                    "Total Assigned Jobs",
                    String.valueOf(totalJobs),
                    BROWN));

            statistics.add(createStatItem(
                    "Completed Jobs",
                    String.valueOf(completedJobs),
                    OLIVE));

            statistics.add(createStatItem(
                    "Pending / In Progress",
                    String.valueOf(pendingJobs),
                    BROWN));

            statistics.add(createStatItem(
                    "Overall Completion",
                    String.format("%.1f%%", overallPercentage),
                    OLIVE));

            JPanel progressPanel = new JPanel(
                    new BorderLayout(0, 10));

            progressPanel.setOpaque(false);

            progressPanel.setBorder(
                    new EmptyBorder(20, 0, 0, 0));

            JLabel progressTitle = new JLabel(
                    "Overall Job Completion");

            progressTitle.setFont(
                    new Font("Serif", Font.BOLD, 19));

            progressTitle.setForeground(BROWN);

            JProgressBar progressBar =
                    new JProgressBar(0, 100);

            progressBar.setValue(
                    (int) Math.round(overallPercentage));

            progressBar.setStringPainted(true);

            progressBar.setString(
                    String.format("%.1f%%", overallPercentage));

            progressBar.setForeground(OLIVE);
            progressBar.setBackground(LIGHT_SAGE);

            progressBar.setPreferredSize(
                    new Dimension(100, 19));

            JLabel explanation = new JLabel(
                    completedJobs + " completed out of "
                            + totalJobs + " assigned jobs.");

            explanation.setFont(
                    new Font("Serif", Font.ITALIC, 15));

            explanation.setForeground(BROWN);

            progressPanel.add(progressTitle, BorderLayout.NORTH);
            progressPanel.add(progressBar, BorderLayout.CENTER);
            progressPanel.add(explanation, BorderLayout.SOUTH);

            JPanel summaryContent = new JPanel(
                    new BorderLayout(0, 20));

            summaryContent.setOpaque(false);

            summaryContent.add(
                    statistics, BorderLayout.NORTH);

            summaryContent.add(
                    progressPanel, BorderLayout.CENTER);

            summary.add(summaryContent, BorderLayout.CENTER);


            tableModel.setRowCount(0);

            try (PreparedStatement ps =
                         con.prepareStatement(serviceSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        String serviceName =
                                rs.getString("service_name");

                        int assigned =
                                rs.getInt("assigned_jobs");

                        int completed =
                                rs.getInt("completed_jobs");

                        int pending = assigned - completed;

                        double percentage = assigned == 0
                                ? 0
                                : (completed * 100.0 / assigned);

                        tableModel.addRow(new Object[]{
                                serviceName,
                                assigned,
                                completed,
                                pending,
                                String.format("%.1f%%", percentage)
                        });
                    }
                }
            }

            if (tableModel.getRowCount() == 0) {

                tableModel.addRow(new Object[]{
                        "No assigned services",
                        0,
                        0,
                        0,
                        "0.0%"
                });
            }

            loading.setVisible(false);

            summary.revalidate();
            summary.repaint();

            tableModel.fireTableDataChanged();

        } catch (SQLException e) {

            loading.setText(
                    "Unable to load performance: " + e.getMessage());

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load worker performance:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }



    private JPanel createStatItem(
            String label,
            String value,
            Color valueColor) {

        JPanel panel = new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(label);

        title.setFont(
                new Font("Serif", Font.ITALIC, 17));

        title.setForeground(MUTED);

        JLabel number = new JLabel(value);

        number.setFont(
                new Font("Serif", Font.BOLD, 31));

        number.setForeground(valueColor);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(number);

        return panel;
    }



    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            dispose();

            new Login();
        }
    }
}
