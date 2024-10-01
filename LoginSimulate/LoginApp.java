package LoginSimulate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LoginApp {
    static boolean captchaVerified = false;
    static String currentCaptchaText = "";
    static String generatedOTP = "";
    static String guestEmail = "";
    static boolean guestClicked = false;
    static String stoEmail = "";
    public static String stoUsername = "";

    public static String generateCaptchaText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            captcha.append(chars.charAt(index));
        }
        return captcha.toString();
    }

    public static String generateOTP(int length) {
        String digits = "0123456789";
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(digits.length());
            otp.append(digits.charAt(index));
        }
        return otp.toString();
    }

    public static BufferedImage generateCaptchaImage(String captchaText) {
        int width = 300;
        int height = 100;
        BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) captchaImage.getGraphics();

        g.setColor(new Color(180, 220, 240));
        g.fillRect(0, 0, width, height);

        g.setFont(new Font("Serif", Font.BOLD, 40));
        Random rand = new Random();
        int x = 20;

        for (char c : captchaText.toCharArray()) {
            AffineTransform originalTransform = g.getTransform();
            double rotation = (rand.nextDouble() - 0.5) * 0.4;
            double scale = 1 + (rand.nextDouble() - 0.5) * 0.3;
            g.rotate(rotation, x, 50);
            g.scale(scale, scale);
            g.setColor(new Color(10, 10, rand.nextInt(150) + 100));

            g.drawString(String.valueOf(c), x, 50 + rand.nextInt(20) - 10);

            g.setTransform(originalTransform);
            x += 40;
        }

        g.setColor(Color.GRAY);
        for (int i = 0; i < 8; i++) {
            int x1 = rand.nextInt(width);
            int y1 = rand.nextInt(height);
            int x2 = rand.nextInt(width);
            int y2 = rand.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();
        return captchaImage;
    }

    static HashMap<String, String> accountsMap = new HashMap<>();
    public static void main(String[] args) {
        loadAccounts("index.txt");
        JFrame frame = new JFrame("Đăng nhập vào Minesweeper");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1)); // Thay đổi số hàng để có thêm dòng nhỏ
        frame.setResizable(false);
    
        JPanel panelLogin = new JPanel(new GridLayout(2, 2));
        JLabel labelUsername = new JLabel("    Tên đăng nhập:");
        JTextField textUsername = new JTextField();
        JLabel labelPassword = new JLabel("    Mật khẩu:");
        JPasswordField textPassword = new JPasswordField();
    
        JButton btnTogglePassword = new JButton("🙈");
        btnTogglePassword.addActionListener(new ActionListener() {
            private boolean passwordVisible = false;
    
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordVisible) {
                    textPassword.setEchoChar('*');
                    btnTogglePassword.setText("🙈");
                } else {
                    textPassword.setEchoChar((char) 0);
                    btnTogglePassword.setText("👁️");
                }
                passwordVisible = !passwordVisible;
            }
        });
    
        panelLogin.add(labelUsername);
        panelLogin.add(textUsername);
        panelLogin.add(labelPassword);
    
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(textPassword, BorderLayout.CENTER);
        passwordPanel.add(btnTogglePassword, BorderLayout.EAST);
        panelLogin.add(passwordPanel);
    
        JCheckBox checkHuman = new JCheckBox("I am human");
        JPanel panelCheckbox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCheckbox.add(checkHuman);
        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnExit = new JButton("Thoát");
    
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButtons.add(btnLogin);
        panelButtons.add(btnExit);
    
        JLabel guestLoginLabel = new JLabel("Đăng nhập bằng tài khoản khách", JLabel.CENTER);
        guestLoginLabel.setForeground(Color.BLUE);
        guestLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        JLabel viewAccountLabel = new JLabel("Quên tài khoản?", JLabel.CENTER);
        viewAccountLabel.setForeground(Color.BLUE);
        viewAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        // Thêm dòng này sau khi bạn đã khai báo các thành phần của JFrame (trước khi thêm frame.setVisible(true);)
        JPanel guestAndViewPanel = new JPanel(new GridLayout(2, 1)); // Tạo JPanel mới với 2 hàng
        guestAndViewPanel.add(guestLoginLabel); // Thêm dòng đăng nhập khách vào panel
        guestAndViewPanel.add(viewAccountLabel); // Thêm dòng xem tài khoản vào panel
    
        // Thay thế phần thêm guestLoginLabel và viewAccountLabel vào frame bằng việc thêm panel mới vào frame
        frame.add(panelLogin);
        frame.add(panelCheckbox);
        frame.add(panelButtons);
        frame.add(guestAndViewPanel); // Thêm panel chứa 2 dòng vào frame
    
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    
        viewAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Kiểm tra CAPTCHA trước khi tiếp tục
                if (!captchaVerified) {
                    JOptionPane.showMessageDialog(frame, "Bạn phải xác minh CAPTCHA trước khi xem tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                boolean validEmail = false;
                String enteredEmail = ""; // Khai báo biến để lưu email nhập vào
                
                while (!validEmail) {
                    enteredEmail = JOptionPane.showInputDialog(frame, "Nhập email của bạn để nhận mã OTP:", "Nhập email", JOptionPane.QUESTION_MESSAGE);
                    
                    if (enteredEmail == null) {
                        return; // Nếu người dùng nhấn Hủy
                    }
                    
                    if (enteredEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Email không được để trống. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
        
                    // Tìm kiếm email trong accountsMap
                    boolean emailFound = false;
                    for (String key : accountsMap.keySet()) {
                        String[] parts = accountsMap.get(key).split(" ");
                        if (parts.length >= 2 && parts[1].equals(enteredEmail)) {
                            emailFound = true;
                            break;
                        }
                    }

                    if (emailFound) { // Kiểm tra xem email có trong accountsMap không
                        validEmail = true; // Email hợp lệ
                        // Gọi FirstClass để thiết lập giá trị
                        stoEmail = enteredEmail;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Email chưa được đăng kí. Vui lòng liên hệ Admin để được giúp đỡ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
        
                generatedOTP = generateOTP(6);
                JOptionPane.showMessageDialog(frame, "Gửi mã OTP thành công! Hãy kiểm tra email " + enteredEmail + ".", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
                showFakeGmailWindow(frame, enteredEmail, generatedOTP);
                showOTPInputDialog(frame, generatedOTP);
            }
        });
        
    
        guestLoginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!captchaVerified) {
                    JOptionPane.showMessageDialog(frame, "Bạn phải xác minh CAPTCHA trước khi đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
                boolean validEmail = false;
                while (!validEmail) {
                    guestEmail = JOptionPane.showInputDialog(frame, "Nhập email của bạn để nhận mã OTP:", "Nhập email", JOptionPane.QUESTION_MESSAGE);
                    
                    if (guestEmail == null) {
                        return;
                    }
                    
                    if (guestEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Email không được để trống. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
            
                    if (guestEmail.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
                        validEmail = true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Email không hợp lệ. Vui lòng nhập đúng định dạng (vd: user@example.com).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            
                generatedOTP = generateOTP(6);
                JOptionPane.showMessageDialog(frame, "Gửi mã OTP thành công! Hãy kiểm tra email " + guestEmail + ".", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
                guestClicked = true;
                showFakeGmailWindow(frame, guestEmail, generatedOTP);
                showOTPInputDialog(frame, generatedOTP);
            }
        });
 
        checkHuman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkHuman.isSelected()) {
                    captchaVerified = false;
                    return;
                }

                currentCaptchaText = generateCaptchaText(6);
                BufferedImage captchaImage = generateCaptchaImage(currentCaptchaText);

                JPanel captchaPanel = new JPanel(new BorderLayout());
                JLabel captchaLabel = new JLabel(new ImageIcon(captchaImage));
                JButton btnRefreshCaptcha = new JButton("🔁");

                btnRefreshCaptcha.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentCaptchaText = generateCaptchaText(6);
                        BufferedImage newCaptchaImage = generateCaptchaImage(currentCaptchaText);
                        captchaLabel.setIcon(new ImageIcon(newCaptchaImage));
                    }
                });

                captchaPanel.add(captchaLabel, BorderLayout.CENTER);
                captchaPanel.add(btnRefreshCaptcha, BorderLayout.EAST);

                String enteredCaptcha = JOptionPane.showInputDialog(frame, captchaPanel, "Xác nhận CAPTCHA", JOptionPane.QUESTION_MESSAGE);

                if (enteredCaptcha != null && enteredCaptcha.equalsIgnoreCase(currentCaptchaText)) {
                    captchaVerified = true;
                    checkHuman.setSelected(true);
                    JOptionPane.showMessageDialog(frame, "CAPTCHA đúng!", "Xác nhận thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    captchaVerified = false;
                    checkHuman.setSelected(false);
                    JOptionPane.showMessageDialog(frame, "Mã CAPTCHA không đúng. Vui lòng thử lại.", "Lỗi CAPTCHA", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textUsername.getText();
                String password = new String(textPassword.getPassword());
        
                if (!captchaVerified) {
                    JOptionPane.showMessageDialog(frame, "Bạn phải xác minh CAPTCHA trước khi đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Check if username and password are valid
                if (accountsMap.containsKey(username) && accountsMap.get(username).split(" ")[0].equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Xin chào, " + username + "! Đăng nhập thành công.", "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
                    textUsername.setText("");
                    textPassword.setText("");
        
                    stoUsername = username;
                    LoginSelection.Minesweeper();
                } else {
                    JOptionPane.showMessageDialog(frame, "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
                    checkHuman.setSelected(false);
                    captchaVerified = false;
                }
            }
        });
        
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private static void showOTPInputDialog(JFrame parentFrame, String generatedOTP) {
        JTextField otpInput = new JTextField(6);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Nhập mã OTP:"), BorderLayout.NORTH);
        panel.add(otpInput, BorderLayout.CENTER);
    
        boolean otpValid = false;
        while (!otpValid) {
            int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Xác nhận OTP", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String enteredOTP = otpInput.getText();
                if (enteredOTP.equals(generatedOTP)) {
                        if (guestClicked == true) {
                            JOptionPane.showMessageDialog(parentFrame, "Đăng nhập thành công với tư cách Guest.", "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
                            stoUsername = "Guest";
                            LoginSelection.Minesweeper();
                            guestClicked = false;
                        } else {
                            Account account = new Account();
                            account.showGUI();
                        }
                    otpValid = true;
                } 
                 else {
                    JOptionPane.showMessageDialog(parentFrame, "Mã OTP không đúng. Vui lòng thử lại.", "Lỗi OTP", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }    

    private static void showFakeGmailWindow(JFrame parentFrame, String email, String otp) {
        JFrame gmailFrame = new JFrame("Gmail - Hộp thư đến");
        gmailFrame.setSize(400, 300);
        gmailFrame.setResizable(false);
        gmailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gmailFrame.setLayout(new BorderLayout());
    
        JPanel headerPanel = new JPanel(new GridLayout(4, 1));
        JLabel fromLabel = new JLabel("    Từ: no-reply@gmail.eggplant.com");
        JLabel toLabel = new JLabel("    Đến: " + email);
        JLabel dateLabel = new JLabel("    Ngày: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        JLabel subjectLabel = new JLabel("    Chủ đề: Mã OTP của bạn");
    
        headerPanel.add(fromLabel);
        headerPanel.add(toLabel);
        headerPanel.add(dateLabel);
        headerPanel.add(subjectLabel);
    
        JLabel otpLabel = new JLabel("Mã OTP của bạn: " + otp, JLabel.CENTER);
        otpLabel.setFont(new Font("Serif", Font.BOLD, 20));
    
        JButton btnOK = new JButton("OK");
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.add(btnOK);
    
        gmailFrame.add(headerPanel, BorderLayout.NORTH);
        gmailFrame.add(otpLabel, BorderLayout.CENTER);
        gmailFrame.add(footerPanel, BorderLayout.SOUTH);
    
        gmailFrame.setLocationRelativeTo(null);
        gmailFrame.setLocation(100, 100);
        
        gmailFrame.setVisible(true);
    
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gmailFrame.dispose();
            }
        });
    }

    private static void loadAccounts(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader("LoginSimulate/src/" + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) { // Cần 3 phần để lấy tên, mật khẩu và email
                    String username = parts[0];
                    String password = parts[1];
                    String email = parts[2];
                    accountsMap.put(username, password + " " + email); // Lưu mật khẩu và email
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}