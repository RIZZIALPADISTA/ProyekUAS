package Data;

public class DataMataKuliah {
    String kodeMk;
    String namaMk;
    int sks;
    String nidnDosen; 
    String namaDosen;

    public DataMataKuliah() {
    }

    public String getKodeMk() {
        return kodeMk;
    }

    public void setKodeMk(String kodeMk) {
        this.kodeMk = kodeMk;
    }

    public String getNamaMk() {
        return namaMk;
    }

    public void setNamaMk(String namaMk) {
        this.namaMk = namaMk;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public String getNidnDosen() {
        return nidnDosen;
    }

    public void setNidnDosen(String nidnDosen) {
        this.nidnDosen = nidnDosen;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

    @Override
    public String toString() {
        return this.namaMk;
    }  
}
