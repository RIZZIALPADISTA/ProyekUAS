package DataDAO;

import Data.DataMahasiswa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    
    String url = "jdbc:mysql://localhost:3306/kampus";
    String user = "root";
    String password = "";
    Connection koneksi;
    
    public Connection getConnection() throws SQLException {
        if(koneksi == null || koneksi.isClosed()){
            koneksi = DriverManager.getConnection(url, user, password);
        }
        return koneksi;
    }
    
    public List<DataMahasiswa> readMahasiswa() throws SQLException {
        List<DataMahasiswa> daftarMahasiswa = new ArrayList<>();
        
        String sql = "SELECT NIM, NAMA, ALAMAT, JENIS_KELAMIN FROM mahasiswa";
        
        koneksi = getConnection();
        Statement st = koneksi.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while(rs.next()){
            DataMahasiswa mhs = new DataMahasiswa();
            mhs.setNim(rs.getString("NIM"));
            mhs.setNama(rs.getString("NAMA"));
            mhs.setAlamat(rs.getString("ALAMAT"));
            mhs.setJenisKelamin(rs.getString("JENIS_KELAMIN"));
            daftarMahasiswa.add(mhs);
        }
        return daftarMahasiswa;
    }
    
    public boolean insertMahasiswa(DataMahasiswa mhs) throws SQLException {
        String sql = "INSERT mahasiswa (NIM, NAMA, ALAMAT, JENIS_KELAMIN) VALUES (?, ?, ?, ?)";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        
        ps.setString(1, mhs.getNim());
        ps.setString(2, mhs.getNama());
        ps.setString(3, mhs.getAlamat());
        ps.setString(4, mhs.getJenisKelamin());
        
        int result = ps.executeUpdate();
        return result > 0;
    }
    
    public boolean updateMahasiswa(DataMahasiswa mhs) throws SQLException {
        String sql = "UPDATE mahasiswa SET NAMA = ?, ALAMAT = ?, JENIS_KELAMIN = ? WHERE NIM = ?";
        koneksi = getConnection();
        PreparedStatement ps = koneksi.prepareStatement(sql);
        
        ps.setString(1, mhs.getNama());
        ps.setString(2, mhs.getAlamat());
        ps.setString(3, mhs.getJenisKelamin());
        ps.setString(4, mhs.getNim());
        
        int result = ps.executeUpdate();
        return result > 0;
    }
    public boolean deleteMahasiswa(String nim) throws SQLException {
        String sql = "DELETE FROM mahasiswa WHERE NIM = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nim);
            return ps.executeUpdate() > 0;
        }
    }

    public String getNim() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}


