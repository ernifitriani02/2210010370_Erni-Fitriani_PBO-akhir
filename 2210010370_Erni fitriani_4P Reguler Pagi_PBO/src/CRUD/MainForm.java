package CRUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainForm extends JFrame {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tes="; // Ubah dengan nama database yang benar
    private static final String USER = "root"; // Ganti dengan username MySQL Anda
    private static final String PASS = ""; // Ganti dengan password MySQL Anda

    private Connection conn = null;
    private Statement stmt = null;

    private JPanel mainPanel;
    private JButton tambahButton;
    private JButton tampilkanButton;
    private JButton updateButton;
    private JButton hapusButton;

    // Komponen-komponen input
    private JTextField jenisField;
    private JTextField namaKendaraanField;
    private JTextField merkKendaraanField;
    private JTextField idField;

    public MainForm() {
        initializeUI();
        connectToDatabase();
        setupActions();
    }

    private void initializeUI() {
        setTitle("Aplikasi CRUD");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2));

        // Inisialisasi komponen input
        idField = new JTextField();
        jenisField = new JTextField();
        namaKendaraanField = new JTextField();
        merkKendaraanField = new JTextField();

        tambahButton = new JButton("Tambah Data");
        tampilkanButton = new JButton("Tampilkan Data");
        updateButton = new JButton("Update Data");
        hapusButton = new JButton("Hapus Data");

        // Tambahkan label dan field input ke panel
        mainPanel.add(new JLabel("ID"));
        mainPanel.add(idField);
        mainPanel.add(new JLabel("Jenis"));
        mainPanel.add(jenisField);
        mainPanel.add(new JLabel("Nama Kendaraan"));
        mainPanel.add(namaKendaraanField);
        mainPanel.add(new JLabel("Merk Kendaraan"));
        mainPanel.add(merkKendaraanField);
        mainPanel.add(tambahButton);
        mainPanel.add(tampilkanButton);
        mainPanel.add(updateButton);
        mainPanel.add(hapusButton);

        add(mainPanel);
        setVisible(true); // Set visible di sini agar frame sudah diinisialisasi sepenuhnya sebelum terlihat
    }

    private void connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghubungkan ke database.");
        }
    }

    private void setupActions() {
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahData();
            }
        });

        tampilkanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanData();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });
    }

   private void tambahData() {
    try {
        // Ambil nilai dari field input
        String jenis = jenisField.getText();
        String namaKendaraan = namaKendaraanField.getText();
        String merkKendaraan = merkKendaraanField.getText();

        // Siapkan pernyataan SQL untuk memasukkan data ke tabel data_kendaraan
        String sql = "INSERT INTO data_kendaraan (jenis, nama_kendaraan, merk_kendaraan) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, jenis);
        pstmt.setString(2, namaKendaraan);
        pstmt.setString(3, merkKendaraan);

        // Jalankan pernyataan SQL untuk memasukkan data
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
    } catch (SQLException se) {
        se.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menambah data.");
    }
}


    private void tampilkanData() {
        try {
            String sql = "SELECT * FROM data_kendaraan";
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder data = new StringBuilder();
            while (rs.next()) {
                int id = rs.getInt("id_kendaraan");
                String jenis = rs.getString("jenis");
                String namaKendaraan = rs.getString("nama_kendaraan");
                String merkKendaraan = rs.getString("merk_kendaraan");

                data.append("ID: ").append(id)
                    .append(", Jenis: ").append(jenis)
                    .append(", Nama Kendaraan: ").append(namaKendaraan)
                    .append(", Merk Kendaraan: ").append(merkKendaraan)
                    .append("\n");
            }
            JOptionPane.showMessageDialog(this, data.toString());
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menampilkan data.");
        }
    }

    private void updateData() {
        try {
            // Ambil nilai dari field input
            int id = Integer.parseInt(idField.getText());
            String jenis = jenisField.getText();
            String namaKendaraan = namaKendaraanField.getText();
            String merkKendaraan = merkKendaraanField.getText();

            // Siapkan pernyataan SQL untuk mengupdate data di tabel data_kendaraan
            String sql = "UPDATE data_kendaraan SET jenis=?, nama_kendaraan=?, merk_kendaraan=? WHERE id_kendaraan=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jenis);
            pstmt.setString(2, namaKendaraan);
            pstmt.setString(3, merkKendaraan);
            pstmt.setInt(4, id);

            // Jalankan pernyataan SQL untuk mengupdate data
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            } else {
                JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException | NumberFormatException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate data.");
        }
    }

    private void hapusData() {
        try {
            // Ambil nilai dari field input
            int id = Integer.parseInt(idField.getText());

            // Siapkan pernyataan SQL untuk menghapus data di tabel data_kendaraan
            String sql = "DELETE FROM data_kendaraan WHERE id_kendaraan=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            // Jalankan pernyataan SQL untuk menghapus data
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(this, "Data dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException | NumberFormatException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainForm();
            }
        });
    }
}
