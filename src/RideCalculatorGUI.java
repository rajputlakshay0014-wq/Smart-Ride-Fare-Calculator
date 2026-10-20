
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RideCalculatorGUI extends JFrame {

    private final BookingManager manager = new BookingManager();

    private final JTextField passengerField = new JTextField();
    private final JTextField distanceField = new JTextField();

    private final JComboBox<String> rideBox = new JComboBox<>(
            new String[]{
                    "Bike - Rs. 8/km",
                    "Mini - Rs. 12/km",
                    "Sedan - Rs. 18/km",
                    "Premium - Rs. 25/km"
            }
    );

    private final JLabel fareLabel = new JLabel("Rs. 0.00");
    private final JTextArea receiptArea = new JTextArea(8, 30);
    private final JLabel countLabel = new JLabel("Total bookings: 0");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{
                            "Booking ID", "Passenger", "Ride",
                            "Distance", "Fare", "Status"
                    }, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable bookingTable = new JTable(tableModel);

    public RideCalculatorGUI() {
        setTitle("Smart Ride Fare Calculator");
        setSize(1000, 720);
        setMinimumSize(new Dimension(800, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        JLabel heading = new JLabel("SMART RIDE FARE CALCULATOR");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setForeground(new Color(30, 55, 90));
        root.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        form.add(new JLabel("Passenger Name"));
        form.add(passengerField);

        form.add(new JLabel("Choose Ride"));
        form.add(rideBox);

        form.add(new JLabel("Distance (kilometers)"));
        form.add(distanceField);

        JButton createButton = createButton("Create Booking");
        createButton.addActionListener(e -> createBooking());
        form.add(createButton);

        fareLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        fareLabel.setForeground(new Color(20, 135, 85));
        form.add(fareLabel);

        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receiptArea.setBackground(Color.WHITE);
        receiptArea.setForeground(new Color(35, 45, 60));
        receiptArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel receiptPanel = new JPanel(new BorderLayout(5, 5));
        receiptPanel.setBackground(root.getBackground());
        receiptPanel.add(new JLabel("Booking Receipt"), BorderLayout.NORTH);
        receiptPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(root.getBackground());
        leftPanel.add(form, BorderLayout.NORTH);
        leftPanel.add(receiptPanel, BorderLayout.CENTER);

        bookingTable.setRowHeight(25);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setAutoCreateRowSorter(true);

        JPanel historyPanel = new JPanel(new BorderLayout(8, 8));
        historyPanel.setBackground(root.getBackground());

        JLabel historyHeading = new JLabel("Booking History");
        historyHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        historyPanel.add(historyHeading, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        actionPanel.setBackground(root.getBackground());

        JButton searchButton = createButton("Search Booking ID");
        searchButton.addActionListener(e -> searchBooking());
        actionPanel.add(searchButton);

        JButton cancelButton = createButton("Cancel Selected Booking");
        cancelButton.addActionListener(e -> cancelSelectedBooking());
        actionPanel.add(cancelButton);

        JButton refreshButton = createButton("Refresh History");
        refreshButton.addActionListener(e -> refreshBookings());
        actionPanel.add(refreshButton);

        actionPanel.add(countLabel);
        historyPanel.add(actionPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, leftPanel, historyPanel
        );
        splitPane.setResizeWeight(0.42);
        splitPane.setBorder(null);

        root.add(splitPane, BorderLayout.CENTER);

        refreshBookings();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setBackground(new Color(20, 65, 130));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private RideVehicle getSelectedRide() {
        switch (rideBox.getSelectedIndex()) {
            case 0:
                return new BikeRide();
            case 1:
                return new MiniRide();
            case 2:
                return new SedanRide();
            default:
                return new PremiumRide();
        }
    }

    private void createBooking() {
        String name = passengerField.getText().trim();

        if (name.isEmpty()) {
            showWarning("Please enter passenger name.");
            passengerField.requestFocus();
            return;
        }

        double distance;

        try {
            distance = Double.parseDouble(
                    distanceField.getText().trim()
            );
        } catch (NumberFormatException ex) {
            showWarning("Please enter a valid distance.");
            distanceField.requestFocus();
            return;
        }

        if (!Double.isFinite(distance) || distance <= 0) {
            showWarning("Distance must be greater than zero.");
            distanceField.requestFocus();
            return;
        }

        RideVehicle ride = getSelectedRide();

        Booking booking = new Booking(name, ride, distance);

        if (!manager.addBooking(booking)) {
            showWarning("Booking limit reached.");
            return;
        }

        fareLabel.setText(String.format("Rs. %.2f", booking.getFare()));
        displayReceipt(booking);
        refreshBookings();

        JOptionPane.showMessageDialog(
                this,
                "Booking created successfully!\nBooking ID: "
                        + booking.getBookingId(),
                "Booking Confirmed",
                JOptionPane.INFORMATION_MESSAGE
        );

        passengerField.setText("");
        distanceField.setText("");
    }

    private void displayReceipt(Booking booking) {
        receiptArea.setText(
                "========== BOOKING RECEIPT ==========\n"
                + "Booking ID : " + booking.getBookingId() + "\n"
                + "Passenger  : " + booking.getPassengerName() + "\n"
                + "Ride Type  : " + booking.getRide().getVehicleName() + "\n"
                + "Distance   : " + booking.getDistanceKm() + " km\n"
                + String.format(
                        "Rate       : Rs. %.2f/km%n",
                        booking.getRide().getRatePerKm()
                )
                + String.format(
                        "Total Fare : Rs. %.2f%n",
                        booking.getFare()
                )
                + "Status     : "
                + (booking.isCancelled() ? "Cancelled" : "Confirmed")
                + "\n====================================="
        );
    }

    private void refreshBookings() {
        tableModel.setRowCount(0);

        for (Booking booking : manager.getAllBookings()) {
            tableModel.addRow(new Object[]{
                    booking.getBookingId(),
                    booking.getPassengerName(),
                    booking.getRide().getVehicleName(),
                    booking.getDistanceKm() + " km",
                    String.format("Rs. %.2f", booking.getFare()),
                    booking.isCancelled() ? "Cancelled" : "Confirmed"
            });
        }

        countLabel.setText("Total bookings: " + manager.getBookingCount());
    }

    private void searchBooking() {
        String input = JOptionPane.showInputDialog(
                this, "Enter Booking ID:"
        );

        if (input == null) {
            return;
        }

        try {
            int id = Integer.parseInt(input.trim());
            Booking booking = manager.findBooking(id);

            if (booking == null) {
                JOptionPane.showMessageDialog(this, "Booking not found.");
            } else {
                displayReceipt(booking);
            }
        } catch (NumberFormatException ex) {
            showWarning("Booking ID must be a whole number.");
        }
    }

    private void cancelSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();

        if (selectedRow == -1) {
            showWarning("Please select a booking from the history table.");
            return;
        }

        int modelRow = bookingTable.convertRowIndexToModel(selectedRow);
        int bookingId = (int) tableModel.getValueAt(modelRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Cancel booking ID " + bookingId + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        if (manager.cancelBooking(bookingId)) {
            Booking booking = manager.findBooking(bookingId);
            displayReceipt(booking);
            refreshBookings();

            JOptionPane.showMessageDialog(
                    this, "Booking cancelled successfully."
            );
        } else {
            showWarning("Booking not found or already cancelled.");
        }
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Notice",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RideCalculatorGUI().setVisible(true);
        });
    }
}
