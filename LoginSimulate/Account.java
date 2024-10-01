package LoginSimulate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Account {
    String stoEmail = LoginApp.stoEmail;

    public void showGUI() {
        JFrame frame = new JFrame("Mục lục tài khoản");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 300);
        frame.setLayout(new BorderLayout());

        // Modify table model to show "Tài khoản", "Mật khẩu", and "Email"
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Tài khoản", "Mật khẩu", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        loadAccounts("LoginSimulate/src/index.txt", tableModel);

        table.setSelectionBackground(Color.YELLOW);
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(80, 30));
        btnClose.addActionListener(e -> frame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadAccounts(String filename, DefaultTableModel tableModel) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String username = parts[0]; // First part is the username
                    String password = parts[1]; // Second part is the password
                    String email = parts[2]; // Third part is the email

                    // Mask the email according to your requirements
                    String maskedEmail = maskEmail(email);

                    // Check if the current email matches the stored email in stoEmail
                    String displayPassword;
                    if (email.equals(stoEmail)) { // Compare with stoEmail
                        displayPassword = password; // Display plain password
                    } else {
                        displayPassword = maskPassword(password); // Mask the password with '*'
                    }

                    tableModel.addRow(new Object[]{username, displayPassword, maskedEmail}); // Add username, password, and masked email
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Không thể mở file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to mask the email
    private static String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length < 2) return email; // Return original if format is incorrect

        String localPart = parts[0]; // Part before the '@'
        String domain = parts[1]; // Part after the '@'

        // Mask the local part
        String maskedLocalPart;
        if (localPart.length() > 4) {
            maskedLocalPart = localPart.substring(0, 2) + "****" + localPart.substring(localPart.length() - 2);
        } else {
            maskedLocalPart = localPart; // No masking if local part is too short
        }

        // Return the masked email
        return maskedLocalPart + "@" + domain;
    }

    // Method to mask the password
    private static String maskPassword(String password) {
        return password.replaceAll(".", "*"); // Replace all characters with '*'
    }
}
