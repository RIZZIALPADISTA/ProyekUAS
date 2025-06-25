package DataDAO;

import Data.DataKRS;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KRS {
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
    
    public List<DataKRS> readKrs() throws SQLException {
        List<DataKRS> daftarKrs = new ArrayList<>();
        String sql = "SELECT k.ID_KRS, m.NIM, m.NAMA, mk.KODE_MK, mk.NAMA_MK, k.SEMESTER, k.TAHUN_AJARAN " +
                     "FROM krs k " +
                     "JOIN mahasiswa m ON k.NIM_MHS = m.NIM " +
                     "JOIN matakuliah mk ON k.KODE_MK = mk.KODE_MK " +
                     "ORDER BY k.TAHUN_AJARAN DESC, m.NAMA, mk.NAMA_MK";
        koneksi = getConnection();
        Statement st = koneksi.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            DataKRS krs = new DataKRS();
            krs.setIdKrs(rs.getInt("ID_KRS"));
            krs.setNimMhs(rs.getString("NIM"));
            krs.setNamaMhs(rs.getString("NAMA"));
            krs.setKodeMk(rs.getString("KODE_MK"));
            krs.setNamaMk(rs.getString("NAMA_MK"));
            krs.setSemester(rs.getInt("SEMESTER"));
            krs.setTahunAjaran(rs.getString("TAHUN_AJARAN"));
            daftarKrs.add(krs);
        }
        return daftarKrs;
    }

    public boolean insertKrs(DataKRS krs) throws SQLException {
        String sql = "INSERT INTO krs (NIM_MHS, KODE_MK, SEMESTER, TAHUN_AJARAN) VALUES (?, ?, ?, ?)";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, krs.getNimMhs());
        ps.setString(2, krs.getKodeMk());
        ps.setInt(3, krs.getSemester());
        ps.setString(4, krs.getTahunAjaran());
        return ps.executeUpdate() > 0;
    }

    public boolean deleteKrs(int idKrs) throws SQLException {
        String sql = "DELETE FROM krs WHERE ID_KRS = ?";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setInt(1, idKrs);
        return ps.executeUpdate() > 0;
    }
}
