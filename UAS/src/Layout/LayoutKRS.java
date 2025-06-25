package Layout;

import Data.DataKRS;
import Data.DataMahasiswa;
import Data.DataMataKuliah;
import DataDAO.KRS;
import DataDAO.Mahasiswa;
import DataDAO.MataKuliah;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LayoutKRS extends JPanel {
    JComboBox<DataMahasiswa> comboMahasiswa = new JComboBox<>();
    JComboBox<DataMataKuliah> comboMataKuliah = new JComboBox<>();
    JTextField txtSemester = new JTextField(5);
    JTextField txtTahunAjaran = new JTextField(10);
    JButton btnSimpanKrs = new JButton("Simpan KRS");
    JButton btnHapusKrs = new JButton("Hapus KRS Terpilih");
    JTable tabelKrs;
    DefaultTableModel modelTabel;
    KRS krsDAO;
    Mahasiswa mahasiswaDAO;
    MataKuliah mataKuliahDAO;

    public LayoutKRS() {
        krsDAO = new KRS();
        mahasiswaDAO = new Mahasiswa();
        mataKuliahDAO = new MataKuliah();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(buildFormPanel(), BorderLayout.NORTH);
        
        String[] columns = {"ID", "NIM", "Nama Mahasiswa", "Kode MK", "Nama Mata Kuliah", "Semester", "Tahun Ajaran"};
        modelTabel = new DefaultTableModel(columns, 0);
        tabelKrs = new JTable(modelTabel);
        add(new JScrollPane(tabelKrs), BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(btnHapusKrs);
        add(southPanel, BorderLayout.SOUTH);

        registerListeners();
        loadInitialData();
    }
    
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Form Pengisian KRS"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Pilih Mahasiswa:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(comboMahasiswa, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Pilih Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(comboMataKuliah, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; panel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panel.add(txtSemester, gbc);
        gbc.gridx = 2; gbc.gridy = 1; panel.add(new JLabel("Tahun Ajaran:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panel.add(txtTahunAjaran, gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panel.add(btnSimpanKrs, gbc);
        return panel;
    }
    
    private void registerListeners() {
        btnSimpanKrs.addActionListener(e -> saveData());
        btnHapusKrs.addActionListener(e -> deleteData());
    }

    public void loadInitialData() {
        try {
            List<DataMahasiswa> mahasiswaList = mahasiswaDAO.readMahasiswa();
            comboMahasiswa.removeAllItems();
            for (DataMahasiswa mhs : mahasiswaList) { 
                comboMahasiswa.addItem(mhs); 
            }
            List<DataMataKuliah> mkList = mataKuliahDAO.readMataKuliah();
            comboMataKuliah.removeAllItems();
            for (DataMataKuliah mk : mkList) { 
                comboMataKuliah.addItem(mk); 
            }
        } catch (SQLException e) {
            showError("Gagal memuat data awal untuk form: " + e.getMessage());
        }
        loadKrsTable();
    }

    private void loadKrsTable() {
        try {
            List<DataKRS> krsList = krsDAO.readKrs();
            modelTabel.setRowCount(0);
            for (DataKRS krsItem : krsList) {
                modelTabel.addRow(new Object[]{
                    krsItem.getIdKrs(), krsItem.getNimMhs(), krsItem.getNamaMhs(),
                    krsItem.getKodeMk(), krsItem.getNamaMk(), krsItem.getSemester(), krsItem.getTahunAjaran()
                });
            }
        } catch (SQLException e) {
            showError("Gagal memuat data KRS: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            DataMahasiswa mhsTerpilih = (DataMahasiswa) comboMahasiswa.getSelectedItem();
            DataMataKuliah mkTerpilih = (DataMataKuliah) comboMataKuliah.getSelectedItem();
            String tahunAjaran = txtTahunAjaran.getText();

            if (mhsTerpilih == null || mkTerpilih == null || txtSemester.getText().isEmpty() || tahunAjaran.isEmpty()) {
                showWarning("Semua field harus diisi."); 
                return;
            }
            int semester = Integer.parseInt(txtSemester.getText());
            
            DataKRS krsData = new DataKRS(); 
            krsData.setNimMhs(mhsTerpilih.getNim()); // Sekarang method ini ada
            krsData.setKodeMk(mkTerpilih.getKodeMk()); // Sekarang method ini ada
            krsData.setSemester(semester);
            krsData.setTahunAjaran(tahunAjaran);
            
            if (krsDAO.insertKrs(krsData)) { 
                showMessage("KRS berhasil disimpan."); 
                loadKrsTable();
            }
        } catch (NumberFormatException e) {
            showWarning("Semester harus berupa angka.");
        } catch (SQLException e) {
            showError("Gagal menyimpan KRS: " + e.getMessage());
        }
    }
    
    private void deleteData() {
        int selectedRow = tabelKrs.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih data KRS yang akan dihapus dari tabel."); 
            return;
        }
        
        int idKrs = (int) modelTabel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data KRS ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if(krsDAO.deleteKrs(idKrs)) {
                    showMessage("Data KRS berhasil dihapus."); 
                    loadKrsTable();
                }
            } catch (SQLException ex) {
                showError("Gagal menghapus KRS: " + ex.getMessage());
            }
        }
    }

    private void showMessage(String message) { JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    private void showError(String message) { JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE); }
    private void showWarning(String message) { JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE); }
}