package LoginSimulate.src.Minesweeper.Code;

import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Leaderboard extends JFrame {
    private static final String FILE_PATH = "LoginSimulate/src/Minesweeper/Code/score.txt"; // File lưu bảng điểm
    private static Leaderboard currentInstance; // Static reference to the current instance

    public Leaderboard(ArrayList<ScoreEntry> scores) {
        if (currentInstance != null) { // Check if an instance already exists
            currentInstance.dispose(); // Close the existing instance
        }
        currentInstance = this; // Set the current instance

        setTitle("Leaderboard");
        setSize(new Dimension(400, 300));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Rank", "Username", "Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ngăn không cho chỉnh sửa ô
            }
        };
        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // Center align

        // Add scores and set renderers
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            String formattedTime = entry.formatTime(); // Use the formatTime method

            switch (i) {
                case 0:
                    model.addRow(new Object[]{"GOLD", entry.getUsername(), formattedTime});
                    break;
                
                case 1:
                    model.addRow(new Object[]{"SILVER", entry.getUsername(), formattedTime});
                    break;
        
                case 2:
                    model.addRow(new Object[]{"BRONZE", entry.getUsername(), formattedTime});
                    break;
        
                default:
                    model.addRow(new Object[]{i + 1, entry.getUsername(), formattedTime});
                    break;
            }
        }

        // Custom renderer that combines rank color and center alignment
        DefaultTableCellRenderer combinedRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
                // Center align the text
                cell.setHorizontalAlignment(JLabel.CENTER);
        
                // Get the value in the "Rank" column of the current row
                Object rankValue = table.getValueAt(row, 0); // Get the value of the first column (Rank)
        
                // Set background colors based on the value in the "Rank" column
                if (rankValue.equals("GOLD")) {
                    cell.setBackground(java.awt.Color.decode("#FFD700")); // Gold background for the entire row
                } else if (rankValue.equals("SILVER")) {
                    cell.setBackground(java.awt.Color.decode("#C0C0C0")); // Silver background for the entire row
                } else if (rankValue.equals("BRONZE")) {
                    cell.setBackground(java.awt.Color.decode("#CD7F32")); // Bronze background for the entire row
                } else {
                    cell.setBackground(java.awt.Color.WHITE); // Default white background for other rows
                }
        
                // Ensure selected row is still highlighted
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                }
        
                return cell;
            }
        };
        
        // Apply the combined renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(combinedRenderer);
        }       

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        // Save scores after displaying
        saveScoresToFile(scores);
    }

    // Method to save scores to the file
    public void saveScoresToFile(ArrayList<ScoreEntry> scores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (ScoreEntry entry : scores) {
                writer.write(entry.getUsername() + "," + entry.getTime()); // Save username and time in milliseconds
                writer.newLine(); // New line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read scores from the file
    public static ArrayList<ScoreEntry> readScoresFromFile() {
        ArrayList<ScoreEntry> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                long time = Long.parseLong(parts[1]); // Read the time as long

                scores.add(new ScoreEntry(username, time)); // Create ScoreEntry with long time
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }
    
    // Method to remove the " (newest)" suffix from usernames
    public static void removeNewestSuffixFromUsernames() {
        ArrayList<ScoreEntry> scores = readScoresFromFile();

        for (ScoreEntry entry : scores) {
            String username = entry.getUsername();
            if (username.endsWith(" (newest)")) {
                entry.setUsername(username.substring(0, username.length() - " (newest)".length()));
            }
        }

        // Save the updated scores back to the file
        currentInstance.saveScoresToFile(scores);
    }
}
