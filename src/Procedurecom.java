import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Procedurecom extends Thread  {
    final Socket comm;
    public String pseudo;
    public String messageRecu;

    public Procedurecom (Socket s) {
        this.comm = s;
    }

    public String envoyer(String message, String pseudo) throws IOException {
            PrintWriter out = new PrintWriter(comm.getOutputStream(), true);
            out.println(pseudo + ": " + message);
            return "Message envoyé";
    }

    public String recevoirPseudo() throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(comm.getInputStream()));
            String message = in.readLine();
            return message;
    }

    public String recevoirMessage() throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(comm.getInputStream()));
            String message = in.readLine();
            Serveur.messageAttente = true;
            return message;
    }


    @Override
    public void run() {
        try (comm;) {
            System.out.println("Communication avec le client " + comm.getInetAddress());
            this.pseudo = recevoirPseudo();
            System.out.println("Pseudo: " + pseudo);
            while (true) {
                messageRecu = recevoirMessage();
                if (messageRecu.equals("!exit")) {
                    System.out.println("Client " + pseudo + " deconnecte.");
                    break;
                }
                System.out.println("Message de " + pseudo + ": " + messageRecu);
                envoyer("Message reçu: " + messageRecu, pseudo);
            }
        } catch (IOException e) {
            System.out.println("Erreur de communication : " + e.getMessage());
        }
    }
}