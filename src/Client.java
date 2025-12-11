import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            socket.connect(new InetSocketAddress(serverAddress, 7777), 5000);
            System.out.println("Connecte au serveur " + serverAddress + " sur le port 7777");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Envoyer le pseudo
            System.out.println("Entrez votre pseudo:");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String pseudo = userInput.readLine();
            out.println(pseudo);

            new Thread() {
                @Override
                public void run() {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Erreur de lecture du serveur : " + e.getMessage());
                    }
                }
            }.start();

            while (true) {
                // Envoyer un message
                System.out.println("Entrez votre message:");
                String message = userInput.readLine();
                if (message.equals("!exit")) {
                    System.out.println("Fermeture de la connexion.");
                    break;
                }
                out.println(message);
            }
            out.close();
            in.close();
            socket.close();

        } catch (UnknownHostException e) {
            System.out.println("Adresse du serveur inconnue : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de communication avec le serveur : " + e.getMessage());
        }
    }
}
