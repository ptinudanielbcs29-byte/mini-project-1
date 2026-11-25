
package carworkshop2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {

    private int userId;
    private String name;

    private static final Color OLIVE = new Color(83, 96, 69);
    private static final Color DARK_OLIVE = new Color(65, 76, 54);
    private static final Color SAGE = new Color(177, 190, 164);
    private static final Color LIGHT_SAGE = new Color(224, 230, 215);
    private static final Color IVORY = new Color(250, 248, 241);
    private static final Color CREAM = new Color(242, 238, 226);
    private static final Color BROWN = new Color(91, 75, 58);
    private static final Color MUTED = new Color(137, 132, 115);
    private static final Color BORDER = new Color(225, 221, 207);
    private static final Color TERRACOTTA = new Color(174, 112, 79);

    private static final Font TITLE_FONT =
            new Font("Serif", Font.PLAIN, 32);

    private static final Font CARD_FONT =
            new Font("Serif", Font.PLAIN, 20);

    private static final Font BODY_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    public AdminDashboard(int userId, String name) {

        this.userId = userId;
        this.name = name;

        setTitle("Car Workshop | Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 700));

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(OLIVE);
        sidebar.setPreferredSize(new Dimension(260, 0));

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(
                new EmptyBorder(35, 20, 25, 20));

        JLabel brand = new JLabel("Auto Atelier");
        brand.setFont(new Font("Serif", Font.BOLD, 27));
        brand.setForeground(IVORY);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("CAR WORKSHOP MANAGEMENT");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 9));
        subtitle.setForeground(LIGHT_SAGE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(55));

        JLabel menuHeading = new JLabel("WORKSPACE");
        menuHeading.setFont(
                new Font("SansSerif", Font.BOLD, 10));
        menuHeading.setForeground(LIGHT_SAGE);
        menuHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuHeading);
        sidebar.add(Box.createVerticalStrut(16));

        addSidebarButton(sidebar, "Dashboard", true, null);

        addSidebarButton(sidebar, "Customer Management", false,
                () -> new CustomerManagement());

        addSidebarButton(sidebar, "Vehicle Management", false,
                () -> new VehicleManagement());

        addSidebarButton(sidebar, "Worker Management", false,
                () -> new WorkerManagement());

        addSidebarButton(sidebar, "Service Management", false,
                () -> new ServiceManagement());

        addSidebarButton(sidebar, "Appointment Management", false,
                () -> new AppointmentManagement());

        addSidebarButton(sidebar, "Job Assignment", false,
                () -> new JobAssignment());

        addSidebarButton(sidebar, "Billing", false,
                () -> new Billing().setVisible(true));

        sidebar.add(Box.createVerticalGlue());

        JPanel accountPanel = new JPanel();
        accountPanel.setOpaque(false);
        accountPanel.setLayout(
                new BoxLayout(accountPanel, BoxLayout.Y_AXIS));

        JLabel accountLabel = new JLabel("SIGNED IN AS");
        accountLabel.setFont(
                new Font("SansSerif", Font.BOLD, 9));
        accountLabel.setForeground(LIGHT_SAGE);

        JLabel accountName = new JLabel(name);
        accountName.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        accountName.setForeground(IVORY);

        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountName.setAlignmentX(Component.LEFT_ALIGNMENT);

        accountPanel.add(accountLabel);
        accountPanel.add(Box.createVerticalStrut(5));
        accountPanel.add(accountName);

        sidebar.add(accountPanel);
        sidebar.add(Box.createVerticalStrut(20));

        JButton logoutButton = new JButton("↪   Logout");

        styleSidebarButton(logoutButton, false);

        logoutButton.setForeground(IVORY);
        logoutButton.setBackground(DARK_OLIVE);

        logoutButton.addActionListener(e -> {
            new Login();
            dispose();
        });

        sidebar.add(logoutButton);

        return sidebar;
    }

    private void addSidebarButton(
            JPanel sidebar,
            String text,
            boolean selected,
            Runnable action) {

        JButton button = new JButton(text);

        styleSidebarButton(button, selected);

        button.addActionListener(e -> {
            if (action != null) {
                action.run();
            }
        });

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(7));
    }

    private void styleSidebarButton(
            JButton button,
            boolean selected) {

        button.setFont(BODY_FONT);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setForeground(
                selected ? DARK_OLIVE : IVORY);

        button.setBackground(
                selected ? LIGHT_SAGE : OLIVE);

        button.setBorder(
                new EmptyBorder(13, 14, 13, 10));

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45));

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    button.setBackground(DARK_OLIVE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    button.setBackground(OLIVE);
                }
            }
        });
    }


    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(20, 25));
        main.setBackground(IVORY);


        main.setBorder(
                new EmptyBorder(38, 42, 30, 8));

        main.add(createHeader(), BorderLayout.NORTH);

        main.add(createDashboardContent(), BorderLayout.CENTER);

        main.add(createFooter(), BorderLayout.SOUTH);

        return main;
    }


    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);

        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel("WELCOME BACK");
        greeting.setFont(
                new Font("SansSerif", Font.BOLD, 10));
        greeting.setForeground(MUTED);

        JLabel title = new JLabel(name);
        title.setFont(TITLE_FONT);
        title.setForeground(BROWN);

        JLabel description = new JLabel(
                "Manage your workshop operations from one place.");

        description.setFont(BODY_FONT);
        description.setForeground(MUTED);

        headingPanel.add(greeting);
        headingPanel.add(Box.createVerticalStrut(7));
        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(8));
        headingPanel.add(description);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(
                new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel role = new JLabel("ADMINISTRATOR");
        role.setFont(
                new Font("SansSerif", Font.BOLD, 11));
        role.setForeground(OLIVE);
        role.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel system = new JLabel("Workshop Workspace");
        system.setFont(
                new Font("SansSerif", Font.PLAIN, 11));
        system.setForeground(MUTED);
        system.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(role);
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(system);

        header.add(headingPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createDashboardContent() {

        JPanel container = new JPanel(new BorderLayout(0, 20));
        container.setOpaque(false);

        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setOpaque(false);

        JLabel sectionTitle = new JLabel("Workshop Overview");
        sectionTitle.setFont(
                new Font("Serif", Font.PLAIN, 23));
        sectionTitle.setForeground(BROWN);

        JLabel sectionDescription = new JLabel(
                "Choose a section to continue");

        sectionDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 11));
        sectionDescription.setForeground(MUTED);

        JPanel sectionText = new JPanel();
        sectionText.setOpaque(false);
        sectionText.setLayout(
                new BoxLayout(sectionText, BoxLayout.Y_AXIS));

        sectionText.add(sectionTitle);
        sectionText.add(Box.createVerticalStrut(5));
        sectionText.add(sectionDescription);

        sectionHeader.add(sectionText, BorderLayout.WEST);

        JPanel cards = new JPanel(
                new GridLayout(0, 3, 20, 20));

        cards.setOpaque(false);

        cards.add(createCard(
                "01",
                "Customer Management",
                "View and manage customer accounts.",
                "Explore",
                () -> new CustomerManagement()));

        cards.add(createCard(
                "02",
                "Vehicle Management",
                "Manage registered customer vehicles.",
                "Explore",
                () -> new VehicleManagement()));

        cards.add(createCard(
                "03",
                "Worker Management",
                "Manage workshop worker details.",
                "Explore",
                () -> new WorkerManagement()));

        cards.add(createCard(
                "04",
                "Service Management",
                "Manage workshop services and pricing.",
                "Explore",
                () -> new ServiceManagement()));

        cards.add(createCard(
                "05",
                "Appointments",
                "View and manage service appointments.",
                "Explore",
                () -> new AppointmentManagement()));

        cards.add(createCard(
                "06",
                "Job Assignment",
                "Assign and manage workshop jobs.",
                "Explore",
                () -> new JobAssignment()));

        cards.add(createCard(
                "07",
                "Billing",
                "Generate invoices and manage payments.",
                "Explore",
                () -> new Billing().setVisible(true)));

        JScrollPane scrollPane = new JScrollPane(cards);

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar bar = scrollPane.getVerticalScrollBar();

        bar.setPreferredSize(new Dimension(10, 0));
        bar.setUnitIncrement(18);

        bar.setBackground(IVORY);
        bar.setOpaque(true);

        bar.setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = SAGE;
                trackColor = IVORY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createEmptyButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createEmptyButton();
            }

            private JButton createEmptyButton() {

                JButton button = new JButton();

                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));

                return button;
            }
        });

        container.add(sectionHeader, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }


    private JPanel createCard(
            String number,
            String title,
            String description,
            String buttonText,
            Runnable action) {

        JPanel card = new JPanel(new BorderLayout(10, 18));

        card.setBackground(CREAM);

        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(23, 22, 20, 22)));

        JPanel information = new JPanel();
        information.setOpaque(false);

        information.setLayout(
                new BoxLayout(information, BoxLayout.Y_AXIS));

        JLabel numberLabel = new JLabel(number);

        numberLabel.setFont(
                new Font("Serif", Font.ITALIC, 19));

        numberLabel.setForeground(OLIVE);

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(CARD_FONT);
        titleLabel.setForeground(BROWN);

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:190px;'>"
                        + description
                        + "</div></html>");

        descriptionLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        descriptionLabel.setForeground(MUTED);

        information.add(numberLabel);
        information.add(Box.createVerticalStrut(19));
        information.add(titleLabel);
        information.add(Box.createVerticalStrut(10));
        information.add(descriptionLabel);

        JButton openButton = new JButton(buttonText + "   →");

        openButton.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        openButton.setForeground(OLIVE);
        openButton.setBackground(LIGHT_SAGE);

        openButton.setFocusPainted(false);
        openButton.setBorderPainted(false);
        openButton.setOpaque(true);

        openButton.setBorder(
                new EmptyBorder(10, 16, 10, 16));

        openButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        openButton.setHorizontalAlignment(
                SwingConstants.LEFT);

        openButton.addActionListener(e -> action.run());

        openButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                openButton.setBackground(SAGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                openButton.setBackground(LIGHT_SAGE);
            }
        });

        card.add(information, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());

        footer.setOpaque(false);

        JLabel footerText = new JLabel(
                "AUTO ATELIER  |  CAR WORKSHOP MANAGEMENT SYSTEM");

        footerText.setFont(
                new Font("SansSerif", Font.PLAIN, 10));

        footerText.setForeground(MUTED);

        JLabel version = new JLabel("ADMIN PANEL");

        version.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        version.setForeground(TERRACOTTA);

        footer.add(footerText, BorderLayout.WEST);
        footer.add(version, BorderLayout.EAST);

        return footer;
    }
}
