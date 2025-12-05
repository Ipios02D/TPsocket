import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur{
    static  Procedurecom[] procedures = new Procedurecom[20];
    private static int nbclient = 0;
    public static boolean messageAttente;
    public static Procedurecom dernierEmetteur;

    public void partager(Procedurecom[] procedures, Procedurecom expediteur) {
        for (Procedurecom procedure : procedures) {
            try {
                procedure.envoyer(expediteur.messageRecu, expediteur.pseudo);
            } catch (IOException e) {
                System.out.println("Erreur lors du partage du message : " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        try(ServerSocket conn = new ServerSocket(7777)) {
            Serveur serveur = new Serveur();
            System.out.println("Serveur en ecoute sur le port 7777");
            while (true) {
                Socket comm = conn.accept();
                System.out.println("Client connecte : " + comm.getInetAddress());
                Procedurecom procedure = new Procedurecom(comm);
                procedures[nbclient] = procedure;
                procedure.start();
                nbclient++;
                if (messageAttente) {
                    serveur.partager(procedures, dernierEmetteur);
                    messageAttente = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
