import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur{

    public static String[] clients = new String[20];
    public static int id = 0;
    static  Procedurecom[] procedures = new Procedurecom[20];
    private static int nbclient = 0;

    // Partage le message recu par l'expediteur avec tous les autres clients connectés
    public static void partager(Procedurecom[] procedures, Procedurecom expediteur) {
        for (Procedurecom procedure : procedures) {
            if (procedure != null && procedure != expediteur) {
                procedure.out.println(expediteur.pseudo + ": " + expediteur.messageRecu);
            }
        }
    }
    public static void main(String[] args) {
        // Permet d'ajouter de nouveaux clients
        try(ServerSocket conn = new ServerSocket(7777)) {
            System.out.println("Serveur en ecoute sur le port 7777");
            while (true) {
                Socket comm = conn.accept();
                System.out.println("Client connecte : " + comm.getInetAddress());
                Procedurecom procedure = new Procedurecom(comm);
                procedures[nbclient] = procedure;
                procedure.start();
                nbclient++;
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
