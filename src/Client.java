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
            out.println("Bonjour serveur, je suis un client.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            System.out.println("Reponse du serveur : " + response);
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
