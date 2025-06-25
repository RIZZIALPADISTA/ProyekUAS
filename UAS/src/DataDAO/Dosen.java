package DataDAO;

import Data.DataDosen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dosen {
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

    public List<DataDosen> readDosen() throws SQLException {
        List<DataDosen> daftarDosen = new ArrayList<>();
        String sql = "SELECT NIDN, NAMA_DOSEN, EMAIL FROM dosen ORDER BY NAMA_DOSEN";
        koneksi = getConnection();
        Statement st = koneksi.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            DataDosen dosen = new DataDosen();
            dosen.setNidn(rs.getString("NIDN"));
            dosen.setNamaDosen(rs.getString("NAMA_DOSEN"));
            dosen.setEmail(rs.getString("EMAIL"));
            daftarDosen.add(dosen);
        }
        return daftarDosen;
    }

    public boolean insertDosen(DataDosen dosen) throws SQLException {
        String sql = "INSERT INTO dosen (NIDN, NAMA_DOSEN, EMAIL) VALUES (?, ?, ?)";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, dosen.getNidn());
        ps.setString(2, dosen.getNamaDosen());
        ps.setString(3, dosen.getEmail());
        return ps.executeUpdate() > 0;
    }

    public boolean updateDosen(DataDosen dosen) throws SQLException {
        String sql = "UPDATE dosen SET NAMA_DOSEN = ?, EMAIL = ? WHERE NIDN = ?";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, dosen.getNamaDosen());
        ps.setString(2, dosen.getEmail());
        ps.setString(3, dosen.getNidn());
        return ps.executeUpdate() > 0;
    }

    public boolean deleteDosen(String nidn) throws SQLException {
        String sql = "DELETE FROM dosen WHERE NIDN = ?";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, nidn);
        return ps.executeUpdate() > 0;
    }

    public String getNidn() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getNamaDosen() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
