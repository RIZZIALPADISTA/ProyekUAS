package DataDAO;

import Data.DataMataKuliah;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MataKuliah {
    String url = "jdbc:mysql://localhost:3306/kampus";
    String user = "root";
    String password = "";
    Connection koneksi;

    public Connection getConnection() throws SQLException {
        if (koneksi == null || koneksi.isClosed()) {
            koneksi = DriverManager.getConnection(url, user, password);
        }
        return koneksi;
    }

    public List<DataMataKuliah> readMataKuliah() throws SQLException {
        List<DataMataKuliah> daftarMk = new ArrayList<>();
        String sql = "SELECT mk.KODE_MK, mk.NAMA_MK, mk.SKS, mk.NIDN_DOSEN, d.NAMA_DOSEN " +
                     "FROM matakuliah mk " +
                     "JOIN dosen d ON mk.NIDN_DOSEN = d.NIDN " +
                     "ORDER BY mk.NAMA_MK";
        koneksi = getConnection();
        Statement st = koneksi.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()) {
            DataMataKuliah mk = new DataMataKuliah();
            mk.setKodeMk(rs.getString("KODE_MK"));
            mk.setNamaMk(rs.getString("NAMA_MK"));
            mk.setSks(rs.getInt("SKS"));
            mk.setNidnDosen(rs.getString("NIDN_DOSEN"));
            mk.setNamaDosen(rs.getString("NAMA_DOSEN")); 
            daftarMk.add(mk);
        }
        return daftarMk;
    }

    public boolean insertMataKuliah(DataMataKuliah mk) throws SQLException {
        String sql = "INSERT INTO matakuliah (KODE_MK, NAMA_MK, SKS, NIDN_DOSEN) VALUES (?, ?, ?, ?)";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, mk.getKodeMk());
        ps.setString(2, mk.getNamaMk());
        ps.setInt(3, mk.getSks());
        ps.setString(4, mk.getNidnDosen());
        return ps.executeUpdate() > 0;
    }
    
    public boolean updateMataKuliah(DataMataKuliah mk) throws SQLException {
        String sql = "UPDATE matakuliah SET NAMA_MK = ?, SKS = ?, NIDN_DOSEN = ? WHERE KODE_MK = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mk.getNamaMk());
            ps.setInt(2, mk.getSks());
            ps.setString(3, mk.getNidnDosen());
            ps.setString(4, mk.getKodeMk());
            return ps.executeUpdate() > 0;
        }
    }

   public boolean deleteMataKuliah(String kodeMk) throws SQLException {
        String sql = "DELETE FROM matakuliah WHERE KODE_MK = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeMk);
            return ps.executeUpdate() > 0;
        }
    }

    public String getKodeMk() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
