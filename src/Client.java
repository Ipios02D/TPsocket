import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            socket.connect(new InetSocketAddress(serverAddress, 7777), 5000);
            System.out.println("Connecte au serveur " + serverAddress + " sur le port 7777");

             Frame frame = new Frame("DevWeb TP1 Chat");
	        frame.setSize(600, 420);
	        frame.setLayout(new BorderLayout());
	        CardLayout cards = new CardLayout();
	        Panel container = new Panel(cards);


	        Page2 page2 = new Page2(cards, container);
	        Page1 page1 = new Page1(cards, container, page2);

	        
	        container.add(page1, "page1");
	        container.add(page2, "page2");

	        page2.setMessageListener(new Page2.MessageListener() {
	            @Override
	            public void onMessageSent(String message) {
	            	String formatted = "Moi: " + message;
	                System.out.println("Message envoyé : " + message);

	                page2.displayMessage(formatted);
	            }
	        });

	        cards.show(container, "page1");

	        frame.add(container, BorderLayout.CENTER);


	        frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                frame.dispose();
	                System.exit(0);
	            }
	        });

	        frame.setVisible(true);
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            
            // Envoyer le pseudo
            
            String pseudo = Page1.pseudoField.getText();
            out.println(pseudo);

            // Thread pour lire les messages du serveur
            new Thread() {
                @Override
                public void run() {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            page2.displayMessage(serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Erreur de lecture du serveur : " + e.getMessage());
                    }
                }
            }.start();

            while (true) {
                // Envoyer un message
                String message = Page2.inputField.getText();

                // Gestion de la deconnexion
                if (message.equals("!exit")) {
                    System.out.println("Fermeture de la connexion.");
                    break;
                }
                out.println(message);
            }
            
            out.close();
            in.close();
            socket.close();
            scanner.close();

        } catch (UnknownHostException e) {
            System.out.println("Adresse du serveur inconnue : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de communication avec le serveur : " + e.getMessage());
        }
    }
}
