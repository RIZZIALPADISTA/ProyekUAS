package Layout;

import Data.DataDosen;
import Data.DataMataKuliah;
import DataDAO.Dosen;
import DataDAO.MataKuliah;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LayoutMataKuliah extends JPanel {
    JTextField txtKodeMk = new JTextField(10);
    JTextField txtNamaMk = new JTextField(20);
    JTextField txtSks = new JTextField(3);
    JComboBox<DataDosen> comboDosen; 
    JButton btnSimpan = new JButton("Simpan");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Hapus");
    JButton btnReset = new JButton("Reset");
    JTable tabelMk;
    DefaultTableModel modelTabel;
    MataKuliah mataKuliahDAO;
    Dosen dosenDAO;

    public LayoutMataKuliah() {
        mataKuliahDAO = new MataKuliah();
        dosenDAO = new Dosen();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Kode MK", "Nama Mata Kuliah", "SKS", "Dosen Pengampu"};
        modelTabel = new DefaultTableModel(columns, 0);
        tabelMk = new JTable(modelTabel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(),
                new JScrollPane(tabelMk));
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        registerListeners();
        loadDosenToComboBox();
        loadData();
        resetForm();
    }

    private JPanel buildFormPanel() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Data Mata Kuliah"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        comboDosen = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Kode MK:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; panelForm.add(txtKodeMk, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nama MK:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtNamaMk, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("SKS:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtSks, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Dosen Pengampu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelForm.add(comboDosen, gbc);
        
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
        tabelMk.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelMk.getSelectedRow() != -1) {
                fillFormFromTable();
            }
        });
    }

    public void loadDosenToComboBox() {
        try {
            List<DataDosen> daftarDosen = dosenDAO.readDosen();
            comboDosen.removeAllItems();
            for (DataDosen dsn : daftarDosen) {
                comboDosen.addItem(dsn); 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data dosen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        try {
            List<DataMataKuliah> daftarMk = mataKuliahDAO.readMataKuliah();
            modelTabel.setRowCount(0);
            for (DataMataKuliah mk : daftarMk) {
                modelTabel.addRow(new Object[]{mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getNamaDosen()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data mata kuliah: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        DataDosen dosenTerpilih = (DataDosen) comboDosen.getSelectedItem();
        
        if (txtKodeMk.getText().isEmpty() || txtNamaMk.getText().isEmpty() || dosenTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi dan dipilih.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DataMataKuliah mk = new DataMataKuliah();
            mk.setKodeMk(txtKodeMk.getText());
            mk.setNamaMk(txtNamaMk.getText());
            mk.setSks(Integer.parseInt(txtSks.getText()));
            mk.setNidnDosen(dosenTerpilih.getNidn()); 

            if (mataKuliahDAO.insertMataKuliah(mk)) {
                JOptionPane.showMessageDialog(this, "Data mata kuliah berhasil disimpan!");
                loadData(); 
                resetForm();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "SKS harus berupa angka.", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateData() {
        DataDosen dosenTerpilih = (DataDosen) comboDosen.getSelectedItem();
        if (dosenTerpilih == null) {
             JOptionPane.showMessageDialog(this, "Pilih Dosen Pengampu.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            DataMataKuliah mk = new DataMataKuliah();
            mk.setKodeMk(txtKodeMk.getText());
            mk.setNamaMk(txtNamaMk.getText());
            mk.setSks(Integer.parseInt(txtSks.getText()));
            mk.setNidnDosen(dosenTerpilih.getNidn());
            
            if(mataKuliahDAO.updateMataKuliah(mk)) {
                JOptionPane.showMessageDialog(this, "Data mata kuliah berhasil diupdate!");
                loadData(); 
                resetForm();
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Error saat update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void deleteData() {
        String kodeMk = txtKodeMk.getText();
        if (kodeMk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus mata kuliah ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            try {
                if(mataKuliahDAO.deleteMataKuliah(kodeMk)) {
                     JOptionPane.showMessageDialog(this, "Data mata kuliah berhasil dihapus.");
                     loadData(); 
                     resetForm();
                }
            } catch (SQLException e) {
                 JOptionPane.showMessageDialog(this, "Gagal menghapus. Kemungkinan mata kuliah ini terdaftar di KRS.\n" + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillFormFromTable() {
        int row = tabelMk.getSelectedRow();
        String kodeMk = modelTabel.getValueAt(row, 0).toString();
        String namaMk = modelTabel.getValueAt(row, 1).toString();
        String sks = modelTabel.getValueAt(row, 2).toString();
        String namaDosen = modelTabel.getValueAt(row, 3).toString();

        txtKodeMk.setText(kodeMk);
        txtNamaMk.setText(namaMk);
        txtSks.setText(sks);

        for (int i = 0; i < comboDosen.getItemCount(); i++) {
            DataDosen dosen = comboDosen.getItemAt(i);
            if (dosen.getNamaDosen().equals(namaDosen)) {
                comboDosen.setSelectedIndex(i);
                break;
            }
        }
        
        btnSimpan.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
        txtKodeMk.setEditable(false);
    }
    
    private void resetForm() {
        txtKodeMk.setText("");
        txtNamaMk.setText("");
        txtSks.setText("");
        comboDosen.setSelectedIndex(-1);
        tabelMk.clearSelection();
        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        txtKodeMk.setEditable(true);
        txtKodeMk.requestFocus();
    }
}