

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Graphical User Interface for the Baby Feeding Tracker.
 * Provides a window for users to interact with feeding data and the database.
 */

public class FeedingGUI extends JFrame {
    private FeedingManager manager = new FeedingManager();
    private DefaultTableModel tableModel;
    private JTable dataTable;

    private JTextField txtName, txtType, txtAmount, txtTime, txtDuration, txtNotes;

    public FeedingGUI() {
        // GUI Layout (Test 1)
        setTitle("Santi's Feeding Tracker - Phase 4 (Database)");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP: Database Connection (Test 2)
        JPanel topPanel = new JPanel();
        JButton btnConnect = new JButton("Connect to Database (.db)");
        topPanel.add(btnConnect);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: Table Display (Test 3)
        String[] columnNames = {"ID", "Baby Name", "Feeding Type", "Amount (oz)", "Time", "Duration (min)", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        // BOTTOM: Inputs and Action Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));

        JPanel inputFieldsPanel = new JPanel();
        txtName = new JTextField(7);
        txtType = new JTextField(7);
        txtAmount = new JTextField(5);
        txtTime = new JTextField(7);
        txtDuration = new JTextField(5);
        txtNotes = new JTextField(12);

        inputFieldsPanel.add(new JLabel("Name:")); inputFieldsPanel.add(txtName);
        inputFieldsPanel.add(new JLabel("Type:")); inputFieldsPanel.add(txtType);
        inputFieldsPanel.add(new JLabel("Oz:")); inputFieldsPanel.add(txtAmount);
        inputFieldsPanel.add(new JLabel("Time:")); inputFieldsPanel.add(txtTime);
        inputFieldsPanel.add(new JLabel("Min:")); inputFieldsPanel.add(txtDuration);
        inputFieldsPanel.add(new JLabel("Notes:")); inputFieldsPanel.add(txtNotes);

        JPanel buttonsPanel = new JPanel();
        JButton btnAdd = new JButton("Add to DB");
        JButton btnUpdate = new JButton("Update DB");
        JButton btnRemove = new JButton("Remove from DB");
        JButton btnAvg = new JButton("Calculate Average");
        JButton btnExit = new JButton("Exit");

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnRemove);
        buttonsPanel.add(btnAvg);
        buttonsPanel.add(btnExit);

        bottomPanel.add(inputFieldsPanel);
        bottomPanel.add(buttonsPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- BUTTON ACTIONS ---

        // Test 2: Connecting to Database
        btnConnect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (manager.connect(selectedFile.getAbsolutePath())) {
                    updateTableUI();
                    JOptionPane.showMessageDialog(this, "Database Connected Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error connecting to database.");
                }
            }
        });

        // Test 4: Create Data
        btnAdd.addActionListener(e -> {
            try {
                FeedingRecord newRecord = new FeedingRecord(
                        txtName.getText(), txtType.getText(),
                        Double.parseDouble(txtAmount.getText()),
                        txtTime.getText(),
                        Integer.parseInt(txtDuration.getText()),
                        txtNotes.getText()
                );
                manager.addRecord(newRecord);
                updateTableUI();
                clearInputFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input Error: Check your numeric values.");
            }
        });

        // Test 6: Update Data
        btnUpdate.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row != -1) {
                try {
                    FeedingRecord updated = new FeedingRecord(
                            txtName.getText(), txtType.getText(),
                            Double.parseDouble(txtAmount.getText()),
                            txtTime.getText(),
                            Integer.parseInt(txtDuration.getText()),
                            txtNotes.getText()
                    );
                    manager.updateRecord(row, updated);
                    updateTableUI();
                    JOptionPane.showMessageDialog(this, "Record Updated in Database!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating record.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a record to update.");
            }
        });

        // Test 5: Remove Data
        btnRemove.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row != -1) {
                manager.removeRecord(row);
                updateTableUI();
                JOptionPane.showMessageDialog(this, "Record Removed from Database!");
            }
        });

        // Test 7: Custom Feature
        btnAvg.addActionListener(e -> {
            double avg = manager.calculateAverageAmount();
            JOptionPane.showMessageDialog(this, "Average Amount (SQL Calculation): " + String.format("%.2f", avg) + " oz");
        });

        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void updateTableUI() {
        tableModel.setRowCount(0);
        List<FeedingRecord> allRecords = manager.getAllRecords();
        for (int i = 0; i < allRecords.size(); i++) {
            FeedingRecord r = allRecords.get(i);
            Object[] rowData = {i, r.getBabyName(), r.getFeedingType(), r.getAmountOz(), r.getFeedingTime(), r.getDurationMinutes(), r.getNotes()};
            tableModel.addRow(rowData);
        }
    }

    private void clearInputFields() {
        txtName.setText(""); txtType.setText(""); txtAmount.setText("");
        txtTime.setText(""); txtDuration.setText(""); txtNotes.setText("");
    }

    public static void main(String[] args) {
        new FeedingGUI();
    }
}