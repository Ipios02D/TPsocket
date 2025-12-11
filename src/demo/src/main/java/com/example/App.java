package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class App extends Application {

    private Stage primaryStage;
    private String pseudoActuel = "";

    // --- PARTIE RÉSEAU ---
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connecte = false;

    // --- PARTIE GRAPHIQUE ---
    private TextArea zoneAffichageMessages;
    private TextField champSaisieMessage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("JavaFX Chat - Client Connecté");
        afficherVueConnexion();
        stage.show();
    }

    /**
     * VUE 1 : Connexion au serveur
     */
    private void afficherVueConnexion() {
        Label titreLabel = new Label("Connexion au Serveur");
        titreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Votre pseudo...");
        pseudoField.setMaxWidth(200);

        // On peut aussi demander l'IP si besoin, ici on force localhost pour simplifier
        Label serverInfo = new Label("Serveur : localhost:7777");

        Button btnConnexion = new Button("Se connecter");
        btnConnexion.setDefaultButton(true);

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        btnConnexion.setOnAction(e -> {
            String inputPseudo = pseudoField.getText().trim();
            if (inputPseudo.isEmpty()) {
                erreurLabel.setText("Pseudo obligatoire !");
            } else {
                // TENTATIVE DE CONNEXION REELLE
                try {
                    connecterAuServeur("localhost", 7777, inputPseudo);
                    this.pseudoActuel = inputPseudo;
                    afficherVueChat();
                } catch (IOException ex) {
                    erreurLabel.setText("Erreur : Serveur introuvable ou hors ligne.");
                    ex.printStackTrace();
                }
            }
        });

        VBox layout = new VBox(15, titreLabel, serverInfo, pseudoField, btnConnexion, erreurLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(layout, 300, 250));
    }

    /**
     * LOGIQUE RÉSEAU : Initialisation de la connexion
     */
    private void connecterAuServeur(String host, int port, String pseudo) throws IOException {
        // 1. Création du Socket (comme dans votre Client.java)
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // 2. Envoi du pseudo (Protocole du serveur)
        out.println(pseudo);
        connecte = true;

        // 3. Lancement du Thread d'écoute (pour recevoir les messages sans bloquer l'interface)
        Thread threadEcoute = new Thread(() -> {
            try {
                String messageServeur;
                while (connecte && (messageServeur = in.readLine()) != null) {
                    // Important : On capture la variable pour l'utiliser dans le thread UI
                    String finalMessage = messageServeur;
                    
                    // Mise à jour de l'interface graphique via Platform.runLater
                    Platform.runLater(() -> {
                        zoneAffichageMessages.appendText(finalMessage + "\n");
                    });
                }
            } catch (IOException e) {
                if (connecte) {
                    Platform.runLater(() -> zoneAffichageMessages.appendText("[Erreur] Connexion perdue.\n"));
                }
            }
        });
        threadEcoute.setDaemon(true); // Le thread s'arrêtera si l'application ferme
        threadEcoute.start();
    }

    /**
     * VUE 2 : Interface de Chat
     */
    private void afficherVueChat() {
        Label infoUserLabel = new Label("Connecté : " + this.pseudoActuel);
        infoUserLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Button btnDeconnexion = new Button("Quitter");
        btnDeconnexion.setStyle("-fx-background-color: #ffcccc;");
        btnDeconnexion.setOnAction(e -> deconnecter());

        BorderPane headerPane = new BorderPane();
        headerPane.setLeft(infoUserLabel);
        headerPane.setRight(btnDeconnexion);
        headerPane.setPadding(new Insets(10));
        headerPane.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");

        zoneAffichageMessages = new TextArea();
        zoneAffichageMessages.setEditable(false);
        zoneAffichageMessages.setWrapText(true);

        champSaisieMessage = new TextField();
        champSaisieMessage.setPromptText("Message...");
        HBox.setHgrow(champSaisieMessage, javafx.scene.layout.Priority.ALWAYS);

        Button btnEnvoyer = new Button("Envoyer");
        btnEnvoyer.setDefaultButton(true);
        btnEnvoyer.setOnAction(e -> envoyerMessage());

        HBox bottomBox = new HBox(10, champSaisieMessage, btnEnvoyer);
        bottomBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(headerPane);
        root.setCenter(zoneAffichageMessages);
        root.setBottom(bottomBox);

        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.centerOnScreen();
    }

    private void envoyerMessage() {
        String message = champSaisieMessage.getText().trim();
        if (!message.isEmpty() && out != null) {
            // 1. Envoyer au serveur
            out.println(message);
            
            // 2. Afficher localement (car votre serveur ne renvoie pas le message à l'expéditeur)
            zoneAffichageMessages.appendText("[Moi] : " + message + "\n");
            
            champSaisieMessage.clear();
        }
    }

    private void deconnecter() {
        connecte = false;
        try {
            if (out != null) out.println("!exit"); // Dire au serveur qu'on part
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        afficherVueConnexion();
    }

    @Override
    public void stop() throws Exception {
        // Appelé quand on ferme la fenêtre (croix rouge)
        deconnecter();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}