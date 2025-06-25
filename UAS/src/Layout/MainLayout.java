package Layout;

import javax.swing.*;

public class MainLayout extends JFrame {
    JTabbedPane tabbedPane;
    LayoutMahasiswa layoutMahasiswa;
    LayoutDosen layoutDosen;
    LayoutMataKuliah layoutMataKuliah;
    LayoutKRS layoutKrs;

    public MainLayout() {
        setTitle("Sistem Informasi Akademik (SIAKAD)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        
        layoutMahasiswa = new LayoutMahasiswa();
        layoutDosen = new LayoutDosen();
        layoutMataKuliah = new LayoutMataKuliah();
        layoutKrs = new LayoutKRS();

        tabbedPane.addTab("Mahasiswa", layoutMahasiswa);
        tabbedPane.addTab("Dosen", layoutDosen);
        tabbedPane.addTab("Mata Kuliah", layoutMataKuliah);
        tabbedPane.addTab("Input KRS", layoutKrs);
        
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == layoutMataKuliah) {
                layoutMataKuliah.loadDosenToComboBox();
            }
            if (tabbedPane.getSelectedComponent() == layoutKrs) {
                layoutKrs.loadInitialData();
            }
        });
        add(tabbedPane);
    }
}
