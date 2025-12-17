import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    static boolean end = true;
    public static void main(String[] args) {
        Frame frame = new Frame("DevWeb TP1 Chat");
	        frame.setSize(600, 420);
	        frame.setLayout(new BorderLayout());
	        CardLayout cards = new CardLayout();
	        Panel container = new Panel(cards);


	        Page2 page2 = new Page2(cards, container);
	        Page1 page1 = new Page1(cards, container, page2);

	        
	        container.add(page1, "page1");
	        container.add(page2, "page2");
        
        do{
        try (Socket socket = new Socket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            socket.connect(new InetSocketAddress(serverAddress, 7777), 5000);
            System.out.println("Connecte au serveur " + serverAddress + " sur le port 7777");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            

            page1.setMessageListener(new Page1.MessageListener() {
	            @Override
	            public void onMessageSent(String pseudo) {
                    out.println(pseudo);
	            }
	        });

	        page2.setMessageListener(new Page2.MessageListener() {
	            @Override
	            public void onMessageSent(String message) {
                    out.println(message);
	            	String formatted = "Moi: " + message;
	                System.out.println("Message envoyé : " + message);

	                page2.displayMessage(formatted);

                    if ("!exit".equals(message)) {
	                    System.out.println("Fermeture de la connexion.");
	                    try {
	                        socket.close();
	                    } catch (IOException ex) {
	                        System.out.println("Erreur lors de la fermeture du socket : " + ex.getMessage());
	                    }
	                }
	            }
	        });

	        cards.show(container, "page1");

	        frame.add(container, BorderLayout.CENTER);


	        frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
                    try {
                        if (!socket.isClosed()) socket.close();
                    } catch (IOException ex) {
                        System.out.println("Erreur fermeture socket: " + ex.getMessage());
                    }
                    end = false;
	                frame.dispose();
	                System.exit(0);
	            }
	        });

	        frame.setVisible(true);
            

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


            // Maintenir l'application ouverte tant que le socket n'est pas fermé
            while (!socket.isClosed()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Adresse du serveur inconnue : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de communication avec le serveur : " + e.getMessage());
        }
        } while(end);
    }
}
