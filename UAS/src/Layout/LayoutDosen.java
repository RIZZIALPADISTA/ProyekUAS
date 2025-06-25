package Layout;

import Data.DataDosen; 
import DataDAO.Dosen;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LayoutDosen extends JPanel {
    JTextField txtNidn = new JTextField(10);
    JTextField txtNama = new JTextField(20);
    JTextField txtEmail = new JTextField(20);
    JButton btnSimpan = new JButton("Simpan");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Hapus");
    JButton btnReset = new JButton("Reset");
    JTable tabelDosen;
    DefaultTableModel modelTabel;
    Dosen dsn;

    public LayoutDosen() {
        dsn = new Dosen();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"NIDN", "Nama Dosen", "Email"};
        modelTabel = new DefaultTableModel(columnNames, 0);
        tabelDosen = new JTable(modelTabel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(),
                new JScrollPane(tabelDosen));
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        registerListeners();
        loadData();
        resetForm();
    }
    
    private JPanel buildFormPanel() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Data Dosen"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("NIDN:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; panelForm.add(txtNidn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nama Dosen:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtNama, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtEmail, gbc);
        
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.add(btnSimpan); panelTombol.add(btnUpdate);
        panelTombol.add(btnDelete); panelTombol.add(btnReset);
        gbc.gridx = 1; gbc.gridy = 3; panelForm.add(panelTombol, gbc);

        return panelForm;
    }

    private void registerListeners() {
        btnSimpan.addActionListener(e -> saveData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnReset.addActionListener(e -> resetForm());
        
        tabelDosen.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelDosen.getSelectedRow() != -1) {
                fillFormFromTable();
            }
        });
    }

    private void loadData() {
        try {
            List<DataDosen> daftarDosen = dsn.readDosen();
            modelTabel.setRowCount(0);
            for (DataDosen dosen : daftarDosen) {
                modelTabel.addRow(new Object[]{dosen.getNidn(), dosen.getNamaDosen(), dosen.getEmail()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data dosen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveData() {
        if (txtNidn.getText().isEmpty() || txtNama.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIDN dan Nama tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DataDosen dosen = new DataDosen();
        dosen.setNidn(txtNidn.getText());
        dosen.setNamaDosen(txtNama.getText());
        dosen.setEmail(txtEmail.getText());

        try {
            if (dsn.insertDosen(dosen)) { 
                JOptionPane.showMessageDialog(this, "Data dosen berhasil disimpan!");
                loadData();
                resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateData() {
        DataDosen dosen = new DataDosen();
        dosen.setNidn(txtNidn.getText());
        dosen.setNamaDosen(txtNama.getText());
        dosen.setEmail(txtEmail.getText());

        try {
            if (dsn.updateDosen(dosen)) { 
                JOptionPane.showMessageDialog(this, "Data dosen berhasil diupdate!");
                loadData(); 
                resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteData() {
        String nidn = txtNidn.getText();
        if (nidn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih dosen yang akan dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus dosen ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if(dsn.deleteDosen(nidn)) {
                    JOptionPane.showMessageDialog(this, "Data dosen berhasil dihapus.");
                    loadData(); 
                    resetForm();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus. Kemungkinan dosen ini masih mengajar mata kuliah.\n" + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillFormFromTable() {
        int row = tabelDosen.getSelectedRow();
        txtNidn.setText(modelTabel.getValueAt(row, 0).toString());
        txtNama.setText(modelTabel.getValueAt(row, 1).toString());
        txtEmail.setText(modelTabel.getValueAt(row, 2) != null ? modelTabel.getValueAt(row, 2).toString() : "");
        
        btnSimpan.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
        txtNidn.setEditable(false);
    }
    
    private void resetForm() {
        txtNidn.setText("");
        txtNama.setText("");
        txtEmail.setText("");
        tabelDosen.clearSelection();
        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        txtNidn.setEditable(true);
        txtNidn.requestFocus();
    }
}