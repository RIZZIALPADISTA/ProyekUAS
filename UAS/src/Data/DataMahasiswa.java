package Data;

public class DataMahasiswa {
    
    String nim;
    String nama;
    String alamat;
    String jenisKelamin;
    
    public DataMahasiswa(){
        
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    
    public String toString() {
        return this.nama + " (" + this.nim + ")";
    }
}
