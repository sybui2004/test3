package com.example.library.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentEditor extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel studentDisplayPanel;
    private JPanel studentInsertPanel;
    private JPanel studentUpdatePanel;
    private JPanel studentDeletePanel;
    private JPanel studentResetPanel;
    private JPanel contentPanel;

    private JPanel insertMaSV;
    private JPanel insertTen;
    private JPanel insertLop;
    private JPanel insertGPA;

    private JTextField insertFieldMaSV;
    private JTextField insertFieldTen;
    private JTextField insertFieldLop;
    private JTextField insertFieldGPA;

    private JTable tableDisplay;


    private JButton displayButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton resetButton;

    private Connection connection;
    public StudentEditor() {
        String url = "jdbc:mysql://localhost:3306/quanlisinhviendb"; // Thay đổi tên DB nếu cần
        String username = "root"; // Thay đổi tên đăng nhập
        String password = "buithaisy123"; // Thay đổi mật khẩu

        Connection connection = null;

        try {
            // Đăng ký driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Thiết lập kết nối
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối đến cơ sở dữ liệu thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Driver không tìm thấy: " + e.getMessage());
        } finally {
            // Đóng kết nối nếu nó được thiết lập
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                }
            }
        }
//        try {
//            // Thiết lập kết nối đến CSDL
//            String url = "jdbc:mysql://localhost:3306/quanlisinhviendb"; // Thay đổi theo CSDL của bạn
//            String user = "root"; // Thay đổi theo thông tin của bạn
//            String password = "buithaisy123"; // Thay đổi theo thông tin của bạn
//            connection = DriverManager.getConnection(url, user, password);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        // SetUpUI cho Display
        //DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Mã SV", "Tên", "Lớp", "GPA"}, );
        //tableDisplay.setModel(tableModel);
        // SetUpUI cho Insert

        // SetUpUI cho Update

        // SetUpUI cho Delete

        // SetUpUI cho Reset
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudents();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStudent();
            }
        });


        setContentPane(contentPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student Editor");
        setResizable(false);
        setSize(400,400);
        //getContentPane().setBackground((new Color(123, 50, 100)));

        setLocationRelativeTo(null);
    }
    private void setupDatabaseConnection() {

    }

    private void displayStudents() {
        try {
            String query = "SELECT * FROM students"; // Thay đổi tên bảng theo CSDL của bạn
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Cập nhật bảng
            DefaultTableModel model = new DefaultTableModel(new String[]{"Mã SV", "Tên", "Lớp", "GPA"}, 0);
            while (resultSet.next()) {
                String maSV = resultSet.getString("ma_sv");
                String ten = resultSet.getString("ten");
                String lop = resultSet.getString("lop");
                String gpa = resultSet.getString("gpa");
                model.addRow(new Object[]{maSV, ten, lop, gpa});
            }
            tableDisplay.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        String maSV = insertFieldMaSV.getText();
        String ten = insertFieldTen.getText();
        String lop = insertFieldLop.getText();
        String gpa = insertFieldGPA.getText();

        if (maSV.isEmpty() || ten.isEmpty() || lop.isEmpty() || gpa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        try {
            String query = "INSERT INTO students (ma_sv, ten, lop, gpa) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, maSV);
            preparedStatement.setString(2, ten);
            preparedStatement.setString(3, lop);
            preparedStatement.setString(4, gpa);
            preparedStatement.executeUpdate();

            displayStudents(); // Cập nhật bảng
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Mã SV đã tồn tại hoặc có lỗi khác!");
        }
    }

    private void updateStudent() {
        int selectedRow = tableDisplay.getSelectedRow(); // Lấy hàng đang được chọn
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để cập nhật!");
            return;
        }

        // Lấy thông tin từ bảng
        String maSV = tableDisplay.getValueAt(selectedRow, 0).toString();
        String ten = insertFieldTen.getText();
        String lop = insertFieldLop.getText();
        String gpa = insertFieldGPA.getText();

        if (ten.isEmpty() || lop.isEmpty() || gpa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        try {
            // Câu lệnh UPDATE
            String query = "UPDATE students SET ten = ?, lop = ?, gpa = ? WHERE ma_sv = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ten);
            preparedStatement.setString(2, lop);
            preparedStatement.setString(3, gpa);
            preparedStatement.setString(4, maSV);
            preparedStatement.executeUpdate();

            displayStudents(); // Cập nhật bảng
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật thông tin!");
        }
    }

    private void deleteStudent() {
        int selectedRow = tableDisplay.getSelectedRow(); // Lấy hàng đang được chọn
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xoá!");
            return;
        }

        // Lấy mã sinh viên từ bảng
        String maSV = tableDisplay.getValueAt(selectedRow, 0).toString();

        // Xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xoá sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Câu lệnh DELETE
                String query = "DELETE FROM students WHERE ma_sv = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, maSV);
                preparedStatement.executeUpdate();

                displayStudents(); // Cập nhật bảng
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xoá sinh viên!");
            }
        }
    }

    private void resetStudent() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa toàn bộ dữ liệu sinh viên?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Câu lệnh DELETE để xóa toàn bộ dữ liệu
                String query = "DELETE FROM students";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();

                // Cập nhật bảng sau khi xóa
                displayStudents();
                JOptionPane.showMessageDialog(this, "Đã xóa toàn bộ dữ liệu sinh viên!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa dữ liệu!");
            }
        }
    }

}

