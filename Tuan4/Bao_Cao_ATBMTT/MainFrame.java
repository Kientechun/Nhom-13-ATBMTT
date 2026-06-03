import java.awt.*;
import java.io.*;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.*;
public class MainFrame extends JFrame {

        private JTextArea inputArea;
        private JTextArea outputArea;

        private JTextField keyField;
        private JTextField ivField;

        private JLabel statusLabel;

        private SecretKey secretKey;
        private byte[] iv;

public MainFrame() {

        setTitle("HỆ THỐNG MÃ HÓA AES");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

}

private void initUI() {

        Color bgColor = new Color(245, 247, 250);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // ================= HEADER =================

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(bgColor);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(
                "HỆ THỐNG MÃ HÓA AES",
                SwingConstants.CENTER);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                28));

        title.setForeground(
                new Color(41, 128, 185));

        JLabel subTitle = new JLabel(
                "AES / CBC / PKCS5Padding",
                SwingConstants.CENTER);

        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        subTitle.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                15));

        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(title);
        headerPanel.add(subTitle);
        headerPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ================= CENTER =================

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(bgColor);

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS));

        JPanel infoPanel = new JPanel(
                new GridLayout(2, 2, 10, 10));

        infoPanel.setBackground(bgColor);

        infoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(41,128,185)),
                        "Thông tin bảo mật"
                ));

        keyField = new JTextField();
        ivField = new JTextField();

        keyField.setEditable(true);
        ivField.setEditable(true);

        infoPanel.add(new JLabel("Khóa AES"));
        infoPanel.add(keyField);

        infoPanel.add(new JLabel("Vector IV"));
        infoPanel.add(ivField);

        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        inputArea = new JTextArea();
        inputArea.setFont(
                new Font("Consolas",
                        Font.PLAIN,
                        14));

        JScrollPane inputScroll =
                new JScrollPane(inputArea);

        inputScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Dữ liệu đầu vào"));

        inputScroll.setPreferredSize(
                new Dimension(800,200));

        centerPanel.add(inputScroll);
        centerPanel.add(Box.createVerticalStrut(10));

        outputArea = new JTextArea();
        outputArea.setFont(
                new Font("Consolas",
                        Font.PLAIN,
                        14));

        JScrollPane outputScroll =
                new JScrollPane(outputArea);

        outputScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Kết quả"));

        outputScroll.setPreferredSize(
                new Dimension(800,200));

        centerPanel.add(outputScroll);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= BUTTON =================

        JPanel buttonPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        10));

        JButton btnGenerate =
                createButton(
                        "Sinh khóa",
                        new Color(41,128,185));

        JButton btnOpen =
                createButton(
                        "Mở tệp TXT",
                        new Color(52,152,219));

        JButton btnEncrypt =
                createButton(
                        "Mã hóa",
                        new Color(39,174,96));

        JButton btnDecrypt =
                createButton(
                        "Giải mã",
                        new Color(230,126,34));

        JButton btnSave =
                createButton(
                        "Lưu tệp TXT",
                        new Color(155,89,182));

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnOpen);
        buttonPanel.add(btnEncrypt);
        buttonPanel.add(btnDecrypt);
        buttonPanel.add(btnSave);

        // ================= STATUS =================

        statusLabel = new JLabel(
                "Trạng thái: Sẵn sàng");

        statusLabel.setBorder(
                new EmptyBorder(
                        5,10,5,10));

        JPanel southPanel =
                new JPanel(
                        new BorderLayout());

        southPanel.add(
                buttonPanel,
                BorderLayout.CENTER);

        southPanel.add(
                statusLabel,
                BorderLayout.SOUTH);

        mainPanel.add(
                southPanel,
                BorderLayout.SOUTH);

        add(mainPanel);

        // ================= EVENTS =================

        btnGenerate.addActionListener(e -> generateKey());

        btnEncrypt.addActionListener(e -> encrypt());

        btnDecrypt.addActionListener(e -> decrypt());

        btnOpen.addActionListener(e -> openFile());

        btnSave.addActionListener(e -> saveFile());
}

private JButton createButton(
        String text,
        Color color) {

        JButton btn =
                new JButton(text);

        btn.setPreferredSize(
                new Dimension(130,40));

        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14));

        return btn;
}

private void generateKey() {

        try {

                String randomKey =
                        AESUtil.generateRandomKey();

                keyField.setText(randomKey);

                iv = AESUtil.generateIV();

                ivField.setText(
                        Base64.getEncoder()
                                .encodeToString(iv));

                statusLabel.setText(
                        "Trạng thái: Sinh khóa thành công");

                JOptionPane.showMessageDialog(
                        this,
                        "Đã sinh khóa AES thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Sinh khóa thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
        }
}

private void encrypt() {

        try {

                if(inputArea.getText().trim().isEmpty()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Vui lòng nhập văn bản cần mã hóa!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                }

                String keyText =
                keyField.getText().trim();

                if(keyText.length() != 16) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Khóa phải có đúng 16 ký tự!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                }

        SecretKey key =
                AESUtil.createKeyFromString(
                        keyText);

                byte[] ivData =
                        AESUtil.stringToIV(
                                ivField.getText().trim());

                String encrypted =
                        AESUtil.encrypt(
                                inputArea.getText(),
                                key,
                                ivData);

                outputArea.setText(encrypted);

                statusLabel.setText(
                        "Trạng thái: Mã hóa thành công");

                JOptionPane.showMessageDialog(
                        this,
                        "Mã hóa thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

        } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Mã hóa thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
        }
}

private void decrypt() {

        try {

                String cipherText =
                        inputArea.getText().trim();

                if(cipherText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Chuỗi mã hóa không hợp lệ: Thiếu dữ liệu hoặc không đủ ký tự!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
                }

                String keyText =
                        keyField.getText().trim();

                if(keyText.length() != 16) {

                JOptionPane.showMessageDialog(
                        this,
                        "Khóa phải có đúng 16 ký tự!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
                }

                SecretKey key =
                        AESUtil.createKeyFromString(
                                keyText);

                byte[] ivData =
                        AESUtil.stringToIV(
                                ivField.getText().trim());

                String decrypted =
                        AESUtil.decrypt(
                                cipherText,
                                key,
                                ivData);

                outputArea.setText(decrypted);

                statusLabel.setText(
                        "Trạng thái: Giải mã thành công");

                JOptionPane.showMessageDialog(
                        this,
                        "Giải mã thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

        } catch(IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể giải mã: Bản mã đã bị thay đổi hoặc khóa/IV không đúng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);

        } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Không thể giải mã: Bản mã đã bị thay đổi hoặc khóa/IV không đúng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
        }
}

private void openFile() {

        JFileChooser chooser =
                new JFileChooser();

        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {

                try {

                        BufferedReader br =
                                new BufferedReader(
                                        new FileReader(
                                                chooser.getSelectedFile()));

                        String keyLine = br.readLine();
                        String ivLine = br.readLine();

                        if(keyLine == null || ivLine == null
                                || !keyLine.startsWith("KEY:")
                                || !ivLine.startsWith("IV:")) {
                                JOptionPane.showMessageDialog(
                                        this,
                                        "File không đúng định dạng AES!",
                                        "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);

                                br.close();
                                return;
                        }

                        br.readLine(); // DATA:

                        StringBuilder cipherText =
                                new StringBuilder();

                        String line;

                        while((line = br.readLine()) != null) {

                                cipherText.append(line);
                        }

                        br.close();

                        String keyString =
                                keyLine.substring(4);

                        String ivString =
                                ivLine.substring(3);

                        keyField.setText(keyString);
                        ivField.setText(ivString);

                        iv =
                                AESUtil.stringToIV(
                                        ivString);

                        inputArea.setText(
                                cipherText.toString());

                        statusLabel.setText(
                                "Trạng thái: Đã mở file");

                        JOptionPane.showMessageDialog(
                                this,
                                "Đã tải khóa, IV và dữ liệu thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);

                } catch(Exception ex) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể mở file!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);

                        ex.printStackTrace();
                }
        }
}

private void saveFile() {

        if(keyField.getText().trim().isEmpty()
                || ivField.getText().trim().isEmpty()
                || outputArea.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Không có dữ liệu để lưu!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
        }

        JFileChooser chooser =
                new JFileChooser();

        if(chooser.showSaveDialog(this)
                == JFileChooser.APPROVE_OPTION) {

        try {

                BufferedWriter bw =
                        new BufferedWriter(
                                new FileWriter(
                                        chooser.getSelectedFile()));

                bw.write(
                        "KEY:" +
                        keyField.getText().trim());

                bw.newLine();

                bw.write(
                        "IV:" +
                        ivField.getText().trim());

                bw.newLine();

                bw.write("DATA:");

                bw.newLine();

                bw.write(
                        outputArea.getText());

                bw.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Lưu file thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

        } catch(Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Lưu file thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);

                ex.printStackTrace();
                }
        }
}
}