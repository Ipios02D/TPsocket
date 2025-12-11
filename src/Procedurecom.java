import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Procedurecom extends Thread  {
    final Socket comm;
    public String pseudo;
    public String messageRecu;
    final BufferedReader in;
    final PrintWriter out;


    public Procedurecom (Socket s) throws IOException {
        this.comm = s;
        this.in = new BufferedReader(new InputStreamReader(comm.getInputStream()));
        this.out = new PrintWriter(comm.getOutputStream(), true);
    }

    public String envoyer(String message, String pseudo) throws IOException {
            out.println(pseudo + ": " + message);
            return "Message envoyé";
    }

    public String recevoirPseudo() throws IOException {
            pseudo = in.readLine();

            // Assert Pseudo deja utilisé
            for(int i=0; i<=Serveur.id; i++){
                if(Serveur.clients[i] != null && Serveur.clients[i].equals(pseudo)){
                    System.out.println("Pseudo deja utilisé, veuillez en choisir un autre.");
                    recevoirPseudo();
                    break;
                }
            }

            // Assert Trop d'utilisateurs
            if(Serveur.id +1 > 20){
                System.out.println("Nombre maximum de clients atteint.");
                comm.close();
            }

            // Enregistrer le pseudo
            Serveur.clients[Serveur.id] = pseudo;
            Serveur.id++;
            return pseudo;
    }

    public String recevoirMessage() throws IOException {
            String message = in.readLine();
            return message;
    }


    @Override
    public void run() {
        try (comm) {

            System.out.println("Communication avec le client " + comm.getInetAddress());
            this.pseudo = recevoirPseudo();
            System.out.println("Pseudo: " + pseudo);

            while (true) {

                messageRecu = recevoirMessage();

                if (messageRecu.equals("!exit") || messageRecu == null) break;

                System.out.println("Message de " + pseudo + ": " + messageRecu);

                Serveur.partager(Serveur.procedures, this);
            }

        } catch (IOException e) {
            System.out.println("Erreur de communication : " + e.getMessage());
        }
    }
}