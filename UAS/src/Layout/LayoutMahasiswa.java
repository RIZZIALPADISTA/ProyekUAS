package Layout;

import Data.DataMahasiswa;
import DataDAO.Mahasiswa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LayoutMahasiswa extends JPanel {
    JTextField txtNim = new JTextField(11);
    JTextField txtNama = new JTextField(20);
    JTextArea txtAlamat = new JTextArea(3, 20);
    JRadioButton rbLaki = new JRadioButton("Laki-laki (L)");
    JRadioButton rbPerempuan = new JRadioButton("Perempuan (P)");
    ButtonGroup groupJk = new ButtonGroup();

    JButton btnSimpan = new JButton("Simpan");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Hapus");
    JButton btnReset = new JButton("Reset");

    JTable tabelDataMahasiswa;
    DefaultTableModel modelTabel;

    Mahasiswa mhs;

    public LayoutMahasiswa() {
        mhs = new Mahasiswa();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"NIM", "Nama", "Alamat", "Jenis Kelamin"};
        modelTabel = new DefaultTableModel(columnNames, 0);
        tabelDataMahasiswa = new JTable(modelTabel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(),
                new JScrollPane(tabelDataMahasiswa));
        splitPane.setDividerLocation(400); // Lebar awal panel form
        add(splitPane, BorderLayout.CENTER);

        registerListeners();
        loadData();
        resetForm();
    }
    
    private JPanel buildFormPanel() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Data Mahasiswa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("NIM:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; panelForm.add(txtNim, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; panelForm.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; panelForm.add(txtNama, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; panelForm.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; panelForm.add(new JScrollPane(txtAlamat), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Jenis Kelamin:"), gbc);
        JPanel panelJk = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        groupJk.add(rbLaki); groupJk.add(rbPerempuan);
        panelJk.add(rbLaki); panelJk.add(rbPerempuan);
        gbc.gridx = 1; gbc.gridy = 3; panelForm.add(panelJk, gbc);
        
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.add(btnSimpan); panelTombol.add(btnUpdate);
        panelTombol.add(btnDelete); panelTombol.add(btnReset);
        gbc.gridx = 1; gbc.gridy = 4; panelForm.add(panelTombol, gbc);

        return panelForm;
    }

    private void registerListeners() {
        btnSimpan.addActionListener(e -> saveData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnReset.addActionListener(e -> resetForm());
        
        tabelDataMahasiswa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelDataMahasiswa.getSelectedRow() != -1) {
                fillFormFromTable();
            }
        });
    }

    private void loadData() {
        try {
            List<DataMahasiswa> daftarMahasiswa = mhs.readMahasiswa();
            modelTabel.setRowCount(0);
            for (DataMahasiswa mahasiswa : daftarMahasiswa) {
                modelTabel.addRow(new Object[]{
                    mahasiswa.getNim(),
                    mahasiswa.getNama(),
                    mahasiswa.getAlamat(),
                    mahasiswa.getJenisKelamin()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveData() {
        String nim = txtNim.getText();
        String nama = txtNama.getText();
        String alamat = txtAlamat.getText();
        String jenisKelamin = rbLaki.isSelected() ? "L" : (rbPerempuan.isSelected() ? "P" : null);
        
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin == null) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan Jenis Kelamin tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataMahasiswa mahasiswa = new DataMahasiswa();
        mahasiswa.setNim(nim);
        mahasiswa.setNama(nama);
        mahasiswa.setAlamat(alamat);
        mahasiswa.setJenisKelamin(jenisKelamin);

        try {
            if (mhs.insertMahasiswa(mahasiswa)) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                loadData();
                resetForm();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        DataMahasiswa mahasiswa = new DataMahasiswa();
        mahasiswa.setNim(txtNim.getText());
        mahasiswa.setNama(txtNama.getText());
        mahasiswa.setAlamat(txtAlamat.getText());
        mahasiswa.setJenisKelamin(rbLaki.isSelected() ? "L" : "P");

        try {
            if (mhs.updateMahasiswa(mahasiswa)) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                loadData();
                resetForm();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saat update: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteData() {
        String nim = txtNim.getText();
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data dengan NIM " + nim + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if(mhs.deleteMahasiswa(nim)) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                    loadData();
                    resetForm();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error saat menghapus: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillFormFromTable() {
        int row = tabelDataMahasiswa.getSelectedRow();
        txtNim.setText(modelTabel.getValueAt(row, 0).toString());
        txtNama.setText(modelTabel.getValueAt(row, 1).toString());
        txtAlamat.setText(modelTabel.getValueAt(row, 2).toString());
        
        if (modelTabel.getValueAt(row, 3).toString().equals("L")) {
            rbLaki.setSelected(true);
        } else {
            rbPerempuan.setSelected(true);
        }
        
        btnSimpan.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
        txtNim.setEditable(false);
    }
    
    private void resetForm() {
        txtNim.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        groupJk.clearSelection();
        tabelDataMahasiswa.clearSelection();
        
        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        txtNim.setEditable(true);
        txtNim.requestFocus();
    }
}