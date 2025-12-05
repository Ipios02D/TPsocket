import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur{

    public void partager(Procedurecom[] procedures, String message, String pseudoExpediteur) {
        for (Procedurecom procedure : procedures) {
            try {
                procedure.envoyer(message, pseudoExpediteur);
            } catch (IOException e) {
                System.out.println("Erreur lors du partage du message : " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        try(ServerSocket conn = new ServerSocket(7777)) {
            System.out.println("Serveur en ecoute sur le port 7777");
            while (true) {
                Socket comm = conn.accept();
                System.out.println("Client connecte : " + comm.getInetAddress());
                Procedurecom procedure = new Procedurecom(comm);
                procedure.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
