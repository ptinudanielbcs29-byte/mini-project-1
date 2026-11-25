package carworkshop2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Billing extends JFrame {

    private static final Color OLIVE = new Color(83, 96, 69);
    private static final Color DARK_OLIVE = new Color(65, 76, 54);
    private static final Color IVORY = new Color(250, 248, 241);
    private static final Color BROWN = new Color(91, 75, 58);
    private static final Color BORDER = new Color(225, 221, 207);

    private static final BigDecimal GST_HALF =
            new BigDecimal("9.00");


    private JTable table;
    private DefaultTableModel model;


    private static class JobRow {

        int jobId;
        int vehicleId;
        int billId;

        String customerName;
        String vehicleNumber;
        String serviceName;
        String workerName;
        String billingStatus;

        BigDecimal price;
        java.sql.Date completedDate;

        JobRow(
                int jobId,
                int vehicleId,
                int billId,
                String customerName,
                String vehicleNumber,
                String serviceName,
                String workerName,
                BigDecimal price,
                java.sql.Date completedDate,
                String billingStatus) {

            this.jobId = jobId;
            this.vehicleId = vehicleId;
            this.billId = billId;

            this.customerName = customerName;
            this.vehicleNumber = vehicleNumber;
            this.serviceName = serviceName;
            this.workerName = workerName;

            this.price = price;
            this.completedDate = completedDate;
            this.billingStatus = billingStatus;
        }
    }

    private static class InvoiceLine {

        int jobId;
        String serviceName;
        String workerName;

        BigDecimal serviceAmount;
        BigDecimal cgst;
        BigDecimal sgst;
        BigDecimal total;

        InvoiceLine(
                int jobId,
                String serviceName,
                String workerName,
                BigDecimal serviceAmount,
                BigDecimal total) {

            this.jobId = jobId;
            this.serviceName = serviceName;
            this.workerName = workerName;
            this.serviceAmount = serviceAmount;
            this.total = total;

            this.cgst = calculateTax(serviceAmount, GST_HALF);
            this.sgst = calculateTax(serviceAmount, GST_HALF);
        }
    }


    public Billing() {

        setTitle("Car Workshop | Billing");

        setSize(1150, 650);
        setMinimumSize(new Dimension(950, 550));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(IVORY);

        setLayout(new BorderLayout(15, 15));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadCompletedJobs();
    }



    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(IVORY);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        22, 25, 5, 25));

        JLabel title = new JLabel("Billing Management");

        title.setFont(
                new Font("Serif", Font.BOLD, 30));

        title.setForeground(DARK_OLIVE);

        JLabel subtitle = new JLabel(
                "Select completed services and generate consolidated invoices");

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        subtitle.setForeground(BROWN);

        JPanel labels = new JPanel(
                new GridLayout(2, 1, 0, 5));

        labels.setOpaque(false);

        labels.add(title);
        labels.add(subtitle);

        panel.add(labels, BorderLayout.WEST);

        return panel;
    }


    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(IVORY);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 25, 5, 25));


        String[] columns = {

                "Select",
                "Job ID",
                "Customer",
                "Vehicle Number",
                "Service",
                "Price",
                "Worker",
                "Completed Date",
                "Billing Status",
                "Bill ID",
                "Vehicle ID"
        };


        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row, int column) {

                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int column) {

                if (column == 0) {
                    return Boolean.class;
                }

                return Object.class;
            }
        };

        table = new JTable(model);

        table.setRowHeight(32);

        table.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        table.setForeground(BROWN);

        table.setBackground(Color.WHITE);

        table.setSelectionBackground(
                new Color(224, 230, 215));

        table.setSelectionForeground(Color.BLACK);

        table.setAutoCreateRowSorter(true);


        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        table.getColumnModel()
                .getColumn(0)
                .setMinWidth(55);

        table.getColumnModel()
                .getColumn(0)
                .setMaxWidth(65);

        table.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        table.getTableHeader().setBackground(OLIVE);

        table.getTableHeader().setForeground(Color.WHITE);

        table.getTableHeader().setReorderingAllowed(false);


        table.removeColumn(
                table.getColumnModel().getColumn(10));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.getViewport().setBackground(Color.WHITE);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 15));

        panel.setBackground(IVORY);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 20, 10, 25));

        JButton generateButton =
                createButton("Generate Consolidated Bill");

        JButton paidButton =
                createButton("Mark as Paid");

        JButton pdfButton =
                createButton("View PDF");

        JButton refreshButton =
                createButton("Refresh");

        JButton closeButton =
                createButton("Close");

        generateButton.addActionListener(
                e -> generateBill());

        paidButton.addActionListener(
                e -> markAsPaid());

        pdfButton.addActionListener(
                e -> viewPDF());

        refreshButton.addActionListener(
                e -> loadCompletedJobs());

        closeButton.addActionListener(
                e -> dispose());

        panel.add(generateButton);
        panel.add(paidButton);
        panel.add(pdfButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }


    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        button.setForeground(Color.WHITE);

        button.setBackground(OLIVE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16));

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        return button;
    }


    private void loadCompletedJobs() {

        model.setRowCount(0);

        String sql =
                "SELECT j.job_id, j.vehicle_id, " +
                        "u.name AS customer_name, " +
                        "v.vehicle_number, " +
                        "s.service_name, s.price, " +
                        "COALESCE(w.name, 'Unassigned') AS worker_name, " +
                        "j.completed_date, " +
                        "CASE WHEN b.bill_id IS NULL " +
                        "THEN 'Not Generated' " +
                        "ELSE b.payment_status END AS billing_status, " +
                        "b.bill_id " +

                        "FROM jobs j " +

                        "JOIN vehicles v " +
                        "ON j.vehicle_id = v.vehicle_id " +

                        "JOIN users u " +
                        "ON v.customer_id = u.user_id " +

                        "JOIN services s " +
                        "ON j.service_id = s.service_id " +

                        "LEFT JOIN users w " +
                        "ON j.worker_id = w.user_id " +

                        "LEFT JOIN bills b " +
                        "ON j.job_id = b.job_id " +

                        "WHERE LOWER(TRIM(j.status)) = 'completed' " +

                        "ORDER BY j.completed_date DESC, j.job_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[]{

                        false,

                        rs.getInt("job_id"),

                        rs.getString("customer_name"),

                        rs.getString("vehicle_number"),

                        rs.getString("service_name"),

                        rs.getBigDecimal("price"),

                        rs.getString("worker_name"),

                        rs.getDate("completed_date"),

                        rs.getString("billing_status"),

                        rs.getObject("bill_id"),

                        rs.getInt("vehicle_id")
                });
            }

        } catch (SQLException ex) {

            showError(
                    "Could not load completed jobs.\n" +
                            ex.getMessage());
        }
    }


    private List<JobRow> getSelectedJobs() {

        List<JobRow> selectedJobs = new ArrayList<>();

        int vehicleId = -1;


        for (int row = 0; row < model.getRowCount(); row++) {

            if (!Boolean.TRUE.equals(
                    model.getValueAt(row, 0))) {

                continue;
            }

            int currentVehicleId =
                    Integer.parseInt(
                            model.getValueAt(row, 10).toString());


            if (vehicleId == -1) {

                vehicleId = currentVehicleId;

            } else if (vehicleId != currentVehicleId) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select jobs belonging to the same vehicle.\n" +
                                "Different vehicles require separate invoices.",
                        "Vehicle Selection",
                        JOptionPane.WARNING_MESSAGE);

                return null;
            }

            Object billValue =
                    model.getValueAt(row, 9);

            int billId = billValue == null
                    ? 0
                    : Integer.parseInt(billValue.toString());

            BigDecimal price =
                    (BigDecimal) model.getValueAt(row, 5);

            java.sql.Date completedDate =
                    (java.sql.Date) model.getValueAt(row, 7);

            JobRow job = new JobRow(

                    Integer.parseInt(
                            model.getValueAt(row, 1).toString()),

                    currentVehicleId,

                    billId,

                    String.valueOf(
                            model.getValueAt(row, 2)),

                    String.valueOf(
                            model.getValueAt(row, 3)),

                    String.valueOf(
                            model.getValueAt(row, 4)),

                    String.valueOf(
                            model.getValueAt(row, 6)),

                    price,

                    completedDate,

                    String.valueOf(
                            model.getValueAt(row, 8))
            );

            selectedJobs.add(job);
        }

        if (selectedJobs.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please tick at least one job checkbox.");

            return null;
        }

        return selectedJobs;
    }


    private void generateBill() {

        List<JobRow> jobs = getSelectedJobs();

        if (jobs == null) return;


        for (JobRow job : jobs) {

            if (job.billId != 0) {

                showError(
                        "Job ID " + job.jobId +
                                " already has a bill.\n" +
                                "Select only unbilled jobs.");

                return;
            }
        }

        int confirmation = JOptionPane.showConfirmDialog(

                this,

                "Generate one consolidated invoice for " +
                        jobs.size() + " completed service(s)?" +

                        "\nVehicle Number: " +
                        jobs.get(0).vehicleNumber +

                        "\nCustomer: " +
                        jobs.get(0).customerName,

                "Confirm Consolidated Billing",

                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) return;

        Connection con = null;

        List<Integer> billIds =
                new ArrayList<>();

        try {

            con = DBConnection.getConnection();

            con.setAutoCommit(false);

            BigDecimal serviceAmount =
                    BigDecimal.ZERO;

            BigDecimal totalCGST =
                    BigDecimal.ZERO;

            BigDecimal totalSGST =
                    BigDecimal.ZERO;


            String checkSql =
                    "SELECT j.status, j.vehicle_id, s.price " +
                            "FROM jobs j " +
                            "JOIN services s ON j.service_id = s.service_id " +
                            "WHERE j.job_id = ? FOR UPDATE";

            String insertSql =
                    "INSERT INTO bills " +
                            "(job_id, amount, payment_status, payment_date) " +
                            "VALUES (?, ?, 'Unpaid', NULL)";

            for (JobRow job : jobs) {

                BigDecimal price;

                try (PreparedStatement check =
                             con.prepareStatement(checkSql)) {

                    check.setInt(1, job.jobId);

                    try (ResultSet rs =
                                 check.executeQuery()) {

                        if (!rs.next()) {

                            throw new SQLException(
                                    "Job ID " + job.jobId +
                                            " not found.");
                        }

                        if (!"completed".equalsIgnoreCase(
                                rs.getString("status"))) {

                            throw new SQLException(
                                    "Job ID " + job.jobId +
                                            " is not completed.");
                        }

                        if (rs.getInt("vehicle_id") !=
                                job.vehicleId) {

                            throw new SQLException(
                                    "Vehicle details changed. Refresh the table.");
                        }

                        price = rs.getBigDecimal("price");
                    }
                }


                String duplicateSql =
                        "SELECT bill_id FROM bills WHERE job_id = ?";

                try (PreparedStatement duplicate =
                             con.prepareStatement(duplicateSql)) {

                    duplicate.setInt(1, job.jobId);

                    try (ResultSet rs =
                                 duplicate.executeQuery()) {

                        if (rs.next()) {

                            throw new SQLException(
                                    "A bill already exists for job ID " +
                                            job.jobId);
                        }
                    }
                }


                BigDecimal cgst =
                        calculateTax(price, GST_HALF);

                BigDecimal sgst =
                        calculateTax(price, GST_HALF);

                BigDecimal finalAmount =
                        price.add(cgst).add(sgst)
                                .setScale(2, RoundingMode.HALF_UP);

                serviceAmount =
                        serviceAmount.add(price);

                totalCGST =
                        totalCGST.add(cgst);

                totalSGST =
                        totalSGST.add(sgst);


                try (PreparedStatement ps =
                             con.prepareStatement(
                                     insertSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, job.jobId);

                    ps.setBigDecimal(2, finalAmount);

                    ps.executeUpdate();

                    try (ResultSet keys =
                                 ps.getGeneratedKeys()) {

                        if (!keys.next()) {

                            throw new SQLException(
                                    "Could not retrieve bill ID.");
                        }

                        billIds.add(keys.getInt(1));
                    }
                }
            }

            BigDecimal grandTotal =
                    serviceAmount.add(totalCGST)
                            .add(totalSGST)
                            .setScale(2, RoundingMode.HALF_UP);


            con.commit();

            try {

                File pdf = createInvoicePDF(billIds);

                JOptionPane.showMessageDialog(

                        this,

                        "CONSOLIDATED BILL GENERATED SUCCESSFULLY!" +

                                "\n\nVehicle Number: " +
                                jobs.get(0).vehicleNumber +

                                "\nTotal Services: " +
                                jobs.size() +

                                "\nService Amount: Rs. " +
                                money(serviceAmount) +

                                "\nCGST: Rs. " +
                                money(totalCGST) +

                                "\nSGST: Rs. " +
                                money(totalSGST) +

                                "\nGrand Total: Rs. " +
                                money(grandTotal) +

                                "\n\nBill IDs: " +
                                billIds.stream()
                                        .map(String::valueOf)
                                        .collect(Collectors.joining(", ")) +

                                "\n\nPDF: " +
                                pdf.getAbsolutePath(),

                        "Billing Success",

                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception pdfError) {

                JOptionPane.showMessageDialog(

                        this,

                        "Bill records saved successfully." +
                                "\nHowever, PDF generation failed.\n" +
                                pdfError.getMessage(),

                        "PDF Generation Warning",

                        JOptionPane.WARNING_MESSAGE
                );
            }

            loadCompletedJobs();

        } catch (Exception ex) {

            if (con != null) {

                try {
                    con.rollback();
                } catch (SQLException ignored) {
                }
            }

            showError(
                    "Bill generation failed.\n" +
                            ex.getMessage());

        } finally {

            if (con != null) {

                try {

                    con.setAutoCommit(true);

                    con.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }


    private static BigDecimal calculateTax(
            BigDecimal amount,
            BigDecimal percentage) {

        return amount.multiply(percentage)
                .divide(
                        new BigDecimal("100"),
                        2,
                        RoundingMode.HALF_UP);
    }


    private void markAsPaid() {

        List<JobRow> jobs = getSelectedJobs();

        if (jobs == null) return;

        for (JobRow job : jobs) {

            if (job.billId == 0) {

                showError(
                        "Job ID " + job.jobId +
                                " has no bill.\nGenerate its bill first.");

                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(

                this,

                "Mark all " + jobs.size() +
                        " selected bills as PAID?",

                "Confirm Payment",

                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql =
                "UPDATE bills SET payment_status = 'Paid', " +
                        "payment_date = CURDATE() " +
                        "WHERE bill_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            for (JobRow job : jobs) {

                ps.setInt(1, job.billId);

                ps.addBatch();
            }

            ps.executeBatch();

            JOptionPane.showMessageDialog(
                    this,
                    "All selected bills marked as paid.");

            loadCompletedJobs();

        } catch (SQLException ex) {

            showError(
                    "Could not update payments.\n" +
                            ex.getMessage());
        }
    }

    private void viewPDF() {

        List<JobRow> jobs = getSelectedJobs();

        if (jobs == null) return;

        List<Integer> billIds =
                new ArrayList<>();

        for (JobRow job : jobs) {

            if (job.billId == 0) {

                showError(
                        "Job ID " + job.jobId +
                                " has not been billed yet.");

                return;
            }

            billIds.add(job.billId);
        }

        try {

            File file = createInvoicePDF(billIds);

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop().open(file);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "PDF saved at:\n" +
                                file.getAbsolutePath());
            }

        } catch (Exception ex) {

            showError(
                    "Could not open PDF.\n" +
                            ex.getMessage());
        }
    }


    private File createInvoicePDF(
            List<Integer> billIds)
            throws SQLException, IOException {

        if (billIds == null || billIds.isEmpty()) {

            throw new SQLException(
                    "No bills selected.");
        }

        String placeholders =
                String.join(
                        ",",
                        Collections.nCopies(
                                billIds.size(), "?"));

        String sql =
                "SELECT b.bill_id, b.job_id, b.amount, " +
                        "b.payment_status, b.payment_date, " +

                        "u.name AS customer_name, u.phone, u.email, " +

                        "v.vehicle_id, v.vehicle_number, v.brand, v.model, " +

                        "s.service_name, s.price, " +

                        "COALESCE(w.name, 'Unassigned') AS worker_name " +

                        "FROM bills b " +

                        "JOIN jobs j ON b.job_id = j.job_id " +

                        "JOIN vehicles v ON j.vehicle_id = v.vehicle_id " +

                        "JOIN users u ON v.customer_id = u.user_id " +

                        "JOIN services s ON j.service_id = s.service_id " +

                        "LEFT JOIN users w ON j.worker_id = w.user_id " +

                        "WHERE b.bill_id IN (" + placeholders + ") " +

                        "ORDER BY j.job_id";

        List<InvoiceLine> lines =
                new ArrayList<>();

        String customerName = "";
        String phone = "";
        String email = "";

        String vehicleNumber = "";
        String brand = "";
        String vehicleModel = "";

        int vehicleId = -1;

        boolean allPaid = true;

        BigDecimal subtotal =
                BigDecimal.ZERO;

        BigDecimal totalCGST =
                BigDecimal.ZERO;

        BigDecimal totalSGST =
                BigDecimal.ZERO;

        BigDecimal grandTotal =
                BigDecimal.ZERO;

        java.sql.Date paymentDate = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            for (int i = 0; i < billIds.size(); i++) {

                ps.setInt(i + 1, billIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int currentVehicle =
                            rs.getInt("vehicle_id");

                    if (vehicleId == -1) {

                        vehicleId = currentVehicle;

                        customerName =
                                rs.getString("customer_name");

                        phone = rs.getString("phone");

                        email = rs.getString("email");

                        vehicleNumber =
                                rs.getString("vehicle_number");

                        brand = rs.getString("brand");

                        vehicleModel =
                                rs.getString("model");

                    } else if (vehicleId != currentVehicle) {

                        throw new SQLException(
                                "Selected bills belong to different vehicles.");
                    }

                    BigDecimal price =
                            rs.getBigDecimal("price");

                    BigDecimal billAmount =
                            rs.getBigDecimal("amount");

                    InvoiceLine line = new InvoiceLine(

                            rs.getInt("job_id"),

                            rs.getString("service_name"),

                            rs.getString("worker_name"),

                            price,

                            billAmount
                    );

                    lines.add(line);

                    subtotal = subtotal.add(price);

                    totalCGST =
                            totalCGST.add(line.cgst);

                    totalSGST =
                            totalSGST.add(line.sgst);

                    grandTotal =
                            grandTotal.add(billAmount);

                    if (!"Paid".equalsIgnoreCase(
                            rs.getString("payment_status"))) {

                        allPaid = false;
                    }

                    if (rs.getDate("payment_date") != null) {

                        paymentDate =
                                rs.getDate("payment_date");
                    }
                }
            }
        }

        if (lines.size() != billIds.size()) {

            throw new SQLException(
                    "Some selected bill records could not be found.");
        }

        File folder = new File(
                System.getProperty("user.dir"),
                "bills");

        if (!folder.exists() && !folder.mkdirs()) {

            throw new IOException(
                    "Could not create bills folder.");
        }

        String safeVehicleNumber =
                vehicleNumber.replaceAll(
                        "[^a-zA-Z0-9_-]", "_");

        String billIdText =
                billIds.stream()
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining("-"));

        File file = new File(
                folder,
                "Consolidated_" +
                        safeVehicleNumber +
                        "_Bills_" +
                        billIdText +
                        ".pdf");


        try (PDDocument document = new PDDocument()) {

            PDPage page =
                    new PDPage(PDRectangle.A4);

            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(
                                 document, page)) {

                float y = 790;


                writeText(
                        content,
                        "CAR WORKSHOP",
                        50, y, 24, true);

                y -= 28;

                writeText(
                        content,
                        "CONSOLIDATED SERVICE INVOICE",
                        50, y, 14, true);

                y -= 40;


                writeText(
                        content,
                        "Invoice Date: " +
                                LocalDate.now(),
                        50, y, 10, false);

                writeText(
                        content,
                        "Bill IDs: " + billIdText,
                        320, y, 10, false);

                y -= 30;

                writeText(
                        content,
                        "CUSTOMER DETAILS",
                        50, y, 13, true);

                y -= 20;

                writeText(
                        content,
                        "Name: " + safe(customerName),
                        50, y, 10, false);

                y -= 16;

                writeText(
                        content,
                        "Phone: " + valueOrDash(phone),
                        50, y, 10, false);

                y -= 16;

                writeText(
                        content,
                        "Email: " + valueOrDash(email),
                        50, y, 10, false);

                y -= 28;


                writeText(
                        content,
                        "VEHICLE DETAILS",
                        50, y, 13, true);

                y -= 20;

                writeText(
                        content,
                        "Vehicle Number: " +
                                safe(vehicleNumber),
                        50, y, 10, false);

                y -= 16;

                writeText(
                        content,
                        "Make / Model: " +
                                valueOrDash(brand) +
                                " / " +
                                valueOrDash(vehicleModel),
                        50, y, 10, false);

                y -= 30;


                writeText(
                        content,
                        "SERVICE BREAKDOWN",
                        50, y, 13, true);

                y -= 25;

                writeText(
                        content,
                        "Job ID",
                        50, y, 9, true);

                writeText(
                        content,
                        "Service",
                        105, y, 9, true);

                writeText(
                        content,
                        "Worker",
                        300, y, 9, true);

                writeText(
                        content,
                        "Amount",
                        465, y, 9, true);

                y -= 12;

                drawLine(content, 50, y, 545, y);

                y -= 20;

                for (InvoiceLine line : lines) {

                    if (y < 130) {

                        throw new IOException(
                                "Too many services for one-page invoice. " +
                                        "Please reduce the selected jobs.");
                    }

                    writeText(
                            content,
                            String.valueOf(line.jobId),
                            50, y, 9, false);

                    writeText(
                            content,
                            safe(line.serviceName),
                            105, y, 9, false);

                    writeText(
                            content,
                            safe(line.workerName),
                            300, y, 9, false);

                    writeText(
                            content,
                            "Rs. " + money(line.serviceAmount),
                            445, y, 9, false);

                    y -= 22;
                }

                y -= 5;

                drawLine(content, 50, y, 545, y);

                y -= 25;


                writeText(
                        content,
                        "Service Subtotal",
                        330, y, 11, false);

                writeText(
                        content,
                        "Rs. " + money(subtotal),
                        455, y, 11, false);

                y -= 22;

                writeText(
                        content,
                        "CGST (9%)",
                        330, y, 11, false);

                writeText(
                        content,
                        "Rs. " + money(totalCGST),
                        455, y, 11, false);

                y -= 22;

                writeText(
                        content,
                        "SGST (9%)",
                        330, y, 11, false);

                writeText(
                        content,
                        "Rs. " + money(totalSGST),
                        455, y, 11, false);

                y -= 25;

                drawLine(content, 330, y, 545, y);

                y -= 28;

                writeText(
                        content,
                        "GRAND TOTAL",
                        330, y, 15, true);

                writeText(
                        content,
                        "Rs. " + money(grandTotal),
                        455, y, 15, true);

                y -= 35;

                writeText(
                        content,
                        "Payment Status: " +
                                (allPaid ? "PAID" : "UNPAID"),
                        50, y, 11, true);

                y -= 20;

                writeText(
                        content,
                        "Payment Date: " +
                                valueOrDash(paymentDate),
                        50, y, 10, false);

                y -= 35;

                writeText(
                        content,
                        "Thank you for choosing our workshop.",
                        50, y, 11, true);

                y -= 18;

                writeText(
                        content,
                        "Service | Care | Trust",
                        50, y, 9, false);
            }

            document.save(file);
        }

        return file;
    }

    private void drawLine(
            PDPageContentStream content,
            float x1,
            float y1,
            float x2,
            float y2) throws IOException {

        content.moveTo(x1, y1);

        content.lineTo(x2, y2);

        content.stroke();
    }

    private void writeText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            int fontSize,
            boolean bold) throws IOException {

        content.beginText();

        content.setFont(
                bold
                        ? PDType1Font.HELVETICA_BOLD
                        : PDType1Font.HELVETICA,
                fontSize);

        content.newLineAtOffset(x, y);

        content.showText(cleanForPDF(text));

        content.endText();
    }


    private String cleanForPDF(String text) {

        return text == null
                ? "-"
                : text.replaceAll("[^\\x20-\\x7E]", "?");
    }


    private String safe(String value) {

        return value == null ||
                value.trim().isEmpty()
                ? "-"
                : value;
    }

    private String valueOrDash(Object value) {

        return value == null
                ? "-"
                : value.toString();
    }


    private String money(BigDecimal value) {

        return value == null
                ? "0.00"
                : value.setScale(
                        2,
                        RoundingMode.HALF_UP)
                .toPlainString();
    }


    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Billing Error",
                JOptionPane.ERROR_MESSAGE);
    }
}