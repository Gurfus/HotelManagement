package ferranechaves.ioc.HolidayManagement;

public class Reserva {
     public String id;
     public String horaIni;
     public String horaFi;
     public String idUser;
     public String dia ;
     public String durada, idPista;

   public Reserva(){


    }

    public Reserva(String id ,String idPista, String idUser, String durada, String horaIni,String horaFi, String dia){
        this.id = id;
        this.durada = durada;
        this.horaFi = horaFi;
        this.horaIni = horaIni;
        this.idPista = idPista;
        this.idUser = idUser;
        this.dia = dia ;

    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFi() {
        return horaFi;
    }

    public void setHoraFi(String horaFi) {
        this.horaFi = horaFi;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDurada() {
        return durada;
    }

    public void setDurada(String durada) {
        this.durada = durada;
    }

    public String getIdPista() {
        return idPista;
    }

    public void setIdPista(String idPista) {
        this.idPista = idPista;
    }
}
