import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, 7777), 5000);
            System.out.println("Connecte au serveur " + serverAddress + " sur le port 7777");
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("Adresse du serveur inconnue : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de communication avec le serveur : " + e.getMessage());
        }
    }
}
