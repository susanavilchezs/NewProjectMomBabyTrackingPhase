import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FeedingGUI extends JFrame {
    private FeedingManager manager = new FeedingManager();
    private DefaultTableModel tableModel;
    private JTable dataTable;

    private JTextField txtName, txtType, txtAmount, txtTime, txtDuration, txtNotes;

    public FeedingGUI() {
        setTitle("Baby Feeding Tracker - Phase 3");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP: Load Button
        JPanel topPanel = new JPanel();
        JButton btnLoad = new JButton("Load Records From File");
        topPanel.add(btnLoad);
        add(topPanel, BorderLayout.NORTH);

       //CENTER
        String[] columnNames = {"ID", "Baby Name", "Feeding Type", "Amount (oz)", "Time", "Duration (min)", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        add(new JScrollPane(dataTable), BorderLayout.CENTER);


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
        JButton btnAdd = new JButton("Add Record");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnRemove = new JButton("Remove Selected");
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



        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (manager.loadFromFile(selectedFile.getAbsolutePath())) {
                    updateTableUI();
                    JOptionPane.showMessageDialog(this, "File Loaded!");
                }
            }
        });

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
                JOptionPane.showMessageDialog(this, "Error: Check your numbers.");
            }
        });

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
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid data for update.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to update.");
            }
        });

        btnRemove.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row != -1) {
                manager.removeRecord(row);
                updateTableUI();
            }
        });

        btnAvg.addActionListener(e -> {
            double avg = manager.calculateAverageAmount();
            JOptionPane.showMessageDialog(this, "Average: " + String.format("%.2f", avg) + " oz");
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