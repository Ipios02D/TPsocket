import java.net.*;
import java.io.*;

public class Serveur{
    public static void main(String[] args) {
        try {
            ServerSocket conn = new ServerSocket(7777);
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
