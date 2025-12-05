import java.net.Socket;

public class Procedurecom extends Thread  {
    private Socket comm;
    public Procedurecom (Socket s) {
        this.comm = s;
    }
    public void run() {
        try {
            System.out.println("Communication avec le client " + comm.getInetAddress());
            // Ici, on pourrait ajouter du code pour communiquer avec le client via le socket 'comm'
            comm.close();
        } catch (Exception e) {
            System.out.println("Erreur de communication : " + e.getMessage());
        }
    }
}