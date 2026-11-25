package carworkshop2;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerDashboard extends JFrame {

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

    private static final Font TITLE_FONT =
            new Font("Serif", Font.PLAIN, 32);

    private static final Font CARD_FONT =
            new Font("Serif", Font.PLAIN, 21);

    private static final Font BODY_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    public CustomerDashboard(int userId, String name) {

        this.userId = userId;
        this.name = name;

        setTitle("Car Workshop | Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 650));

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---------------- SIDEBAR ----------------

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setBackground(OLIVE);
        sidebar.setPreferredSize(new Dimension(255, 0));

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(
                new EmptyBorder(35, 20, 25, 20));

        JLabel brand = new JLabel("Auto Atelier");

        brand.setFont(
                new Font("Serif", Font.BOLD, 27));

        brand.setForeground(IVORY);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "CAR WORKSHOP MANAGEMENT");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 9));

        subtitle.setForeground(LIGHT_SAGE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(55));

        JLabel menuHeading = new JLabel("MY WORKSPACE");

        menuHeading.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        menuHeading.setForeground(LIGHT_SAGE);
        menuHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuHeading);
        sidebar.add(Box.createVerticalStrut(18));

        addSidebarButton(sidebar, "Dashboard", true, null);

        addSidebarButton(sidebar, "My Records", false,
                () -> new CustomerRecords(userId));

        addSidebarButton(sidebar, "My Appointments", false,
                () -> new AppointmentManagement(userId));

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
        accountPanel.add(Box.createVerticalStrut(6));
        accountPanel.add(accountName);

        sidebar.add(accountPanel);
        sidebar.add(Box.createVerticalStrut(20));

        JButton logoutButton = new JButton("↪   Logout");

        styleSidebarButton(logoutButton, false);

        logoutButton.setBackground(DARK_OLIVE);
        logoutButton.setForeground(IVORY);

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

        button.setHorizontalAlignment(
                SwingConstants.LEFT);

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

        JPanel main = new JPanel(
                new BorderLayout(20, 25));

        main.setBackground(IVORY);

        main.setBorder(
                new EmptyBorder(40, 45, 30, 45));

        main.add(createHeader(), BorderLayout.NORTH);

        main.add(createDashboardCards(), BorderLayout.CENTER);

        main.add(createFooter(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(
                new BorderLayout());

        header.setOpaque(false);

        JPanel headingPanel = new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel("WELCOME BACK");

        greeting.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        greeting.setForeground(OLIVE);

        JLabel title = new JLabel(name);

        title.setFont(TITLE_FONT);
        title.setForeground(BROWN);

        JLabel description = new JLabel(
                "Your vehicle care and service information, all in one place.");

        description.setFont(BODY_FONT);
        description.setForeground(MUTED);

        headingPanel.add(greeting);
        headingPanel.add(Box.createVerticalStrut(8));
        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(8));
        headingPanel.add(description);

        JLabel role = new JLabel("CUSTOMER PORTAL");

        role.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        role.setForeground(OLIVE);

        header.add(headingPanel, BorderLayout.WEST);
        header.add(role, BorderLayout.EAST);

        return header;
    }

    private JPanel createDashboardCards() {

        JPanel container = new JPanel(
                new BorderLayout(0, 22));

        container.setOpaque(false);

        JPanel sectionHeading = new JPanel(
                new BorderLayout());

        sectionHeading.setOpaque(false);

        JLabel sectionTitle = new JLabel("Your Workshop Space");

        sectionTitle.setFont(
                new Font("Serif", Font.PLAIN, 24));

        sectionTitle.setForeground(BROWN);

        JLabel sectionDescription = new JLabel(
                "Access your records and manage your appointments.");

        sectionDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        sectionDescription.setForeground(MUTED);

        JPanel sectionText = new JPanel();

        sectionText.setOpaque(false);

        sectionText.setLayout(
                new BoxLayout(sectionText, BoxLayout.Y_AXIS));

        sectionText.add(sectionTitle);
        sectionText.add(Box.createVerticalStrut(6));
        sectionText.add(sectionDescription);

        sectionHeading.add(sectionText, BorderLayout.WEST);

        JPanel cards = new JPanel(
                new GridLayout(1, 2, 25, 25));

        cards.setOpaque(false);

        cards.add(createCard(
                "01",
                "My Records",
                "View your profile, registered vehicles, service jobs and billing records.",
                "View Records",
                () -> new CustomerRecords(userId)));

        cards.add(createCard(
                "02",
                "My Appointments",
                "Request a new service appointment and track your appointment status.",
                "Manage Appointments",
                () -> new AppointmentManagement(userId)));

        container.add(sectionHeading, BorderLayout.NORTH);
        container.add(cards, BorderLayout.CENTER);

        return container;
    }


    private JPanel createCard(
            String number,
            String title,
            String description,
            String buttonText,
            Runnable action) {

        JPanel card = new JPanel(
                new BorderLayout(10, 20));

        card.setBackground(CREAM);

        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(30, 28, 25, 28)));

        JPanel information = new JPanel();

        information.setOpaque(false);

        information.setLayout(
                new BoxLayout(information, BoxLayout.Y_AXIS));

        JLabel numberLabel = new JLabel(number);

        numberLabel.setFont(
                new Font("Serif", Font.ITALIC, 21));

        numberLabel.setForeground(OLIVE);

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(CARD_FONT);
        titleLabel.setForeground(BROWN);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:260px; line-height:1.5;'>"
                        + description
                        + "</div></html>");

        descriptionLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        descriptionLabel.setForeground(MUTED);

        information.add(numberLabel);
        information.add(Box.createVerticalStrut(25));
        information.add(titleLabel);
        information.add(Box.createVerticalStrut(12));
        information.add(descriptionLabel);

        JButton actionButton = new JButton(buttonText + "   →");

        actionButton.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        actionButton.setForeground(OLIVE);
        actionButton.setBackground(LIGHT_SAGE);

        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setOpaque(true);

        actionButton.setBorder(
                new EmptyBorder(12, 18, 12, 18));

        actionButton.setHorizontalAlignment(
                SwingConstants.LEFT);

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

        card.add(information, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(
                new BorderLayout());

        footer.setOpaque(false);

        JLabel left = new JLabel(
                "AUTO ATELIER  |  CUSTOMER WORKSPACE");

        left.setFont(
                new Font("SansSerif", Font.PLAIN, 10));

        left.setForeground(MUTED);

        JLabel right = new JLabel("CUSTOMER PANEL");

        right.setFont(
                new Font("SansSerif", Font.BOLD, 9));

        right.setForeground(OLIVE);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }
}

