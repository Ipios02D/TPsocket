package com.example;

import javafx.application.Application;
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

/**
 * Une application de chat JavaFX simple avec deux vues (Login et Chat).
 * Note : C'est une simulation locale de l'interface graphique, sans serveur backend.
 */
public class App extends Application {

    private Stage primaryStage; // La fenêtre principale
    private String pseudoActuel = "";

    // Composants de l'interface de chat qu'on doit pouvoir manipuler
    private TextArea zoneAffichageMessages;
    private TextField champSaisieMessage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("JavaFX Chat - WSL Demo");

        // Au démarrage, on affiche la vue de connexion
        afficherVueConnexion();

        stage.show();
    }

    /**
     * VUE 1 : L'écran de connexion pour demander le pseudo.
     */
    private void afficherVueConnexion() {
        // 1. Les contrôles
        Label titreLabel = new Label("Bienvenue sur le Chat");
        titreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Entrez votre pseudo...");
        pseudoField.setMaxWidth(200);

        Button btnConnexion = new Button("Se connecter");
        btnConnexion.setDefaultButton(true); // Permet d'appuyer sur Entrée pour valider

        Label erreurLabel = new Label();
        erreurLabel.setStyle("-fx-text-fill: red;");

        // 2. L'action du bouton
        btnConnexion.setOnAction(e -> {
            String inputPseudo = pseudoField.getText().trim();
            if (inputPseudo.isEmpty()) {
                erreurLabel.setText("Le pseudo ne peut pas être vide !");
            } else {
                this.pseudoActuel = inputPseudo;
                // Changement de scène : on passe à la vue du chat
                afficherVueChat();
            }
        });

        // 3. La mise en page (Layout)
        VBox layoutConnexion = new VBox(15); // 15px d'espace vertical entre les éléments
        layoutConnexion.setAlignment(Pos.CENTER);
        layoutConnexion.setPadding(new Insets(20));
        layoutConnexion.getChildren().addAll(titreLabel, pseudoField, btnConnexion, erreurLabel);

        // 4. Création de la scène
        Scene sceneConnexion = new Scene(layoutConnexion, 300, 250);
        primaryStage.setScene(sceneConnexion);
    }

    /**
     * VUE 2 : L'écran principal du chat.
     */
    private void afficherVueChat() {
        // --- HAUT : Barre de déconnexion ---
        Label infoUserLabel = new Label("Connecté en tant que : " + this.pseudoActuel);
        infoUserLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Button btnDeconnexion = new Button("Déconnexion");
        btnDeconnexion.setStyle("-fx-background-color: #ffcccc;"); // Un peu de style rouge clair
        btnDeconnexion.setOnAction(e -> {
            // Retour à la vue de connexion
            afficherVueConnexion();
        });

        BorderPane headerPane = new BorderPane();
        headerPane.setLeft(infoUserLabel);
        headerPane.setRight(btnDeconnexion);
        headerPane.setPadding(new Insets(10));
        headerPane.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");


        // --- CENTRE : Zone d'affichage des messages ---
        zoneAffichageMessages = new TextArea();
        zoneAffichageMessages.setEditable(false); // Important : l'utilisateur ne doit pas modifier l'historique
        zoneAffichageMessages.setWrapText(true);
        zoneAffichageMessages.appendText("[Système] Bienvenue " + this.pseudoActuel + " !\n");


        // --- BAS : Zone de saisie et bouton envoyer ---
        champSaisieMessage = new TextField();
        champSaisieMessage.setPromptText("Votre message...");
        HBox.setHgrow(champSaisieMessage, javafx.scene.layout.Priority.ALWAYS); // Le champ prend toute la largeur dispo

        Button btnEnvoyer = new Button("Envoyer");
        btnEnvoyer.setDefaultButton(true); // Permet d'envoyer avec la touche Entrée

        // Action d'envoi (sur clic du bouton OU appui sur Entrée dans le champ)
        btnEnvoyer.setOnAction(e -> envoyerMessage());

        HBox bottomBox = new HBox(10); // 10px d'espace horizontal
        bottomBox.setPadding(new Insets(10));
        bottomBox.getChildren().addAll(champSaisieMessage, btnEnvoyer);


        // --- Assemblage final de la vue Chat ---
        BorderPane rootLayout = new BorderPane();
        rootLayout.setTop(headerPane);
        rootLayout.setCenter(zoneAffichageMessages);
        rootLayout.setBottom(bottomBox);

        Scene sceneChat = new Scene(rootLayout, 500, 400);
        primaryStage.setScene(sceneChat);
        // Astuce : recentrer la fenêtre si sa taille change lors du changement de scène
        primaryStage.centerOnScreen();
    }

    /**
     * Logique d'envoi du message.
     */
    private void envoyerMessage() {
        String message = champSaisieMessage.getText().trim();

        if (!message.isEmpty()) {
            // --- SIMULATION DU RESEAU ---
            // Dans une vraie application, ici, on enverrait 'message' au serveur via une Socket.
            // Le serveur le renverrait ensuite à tout le monde.

            // Pour notre démo locale, on formate le message et on l'affiche directement :
            String messageFormate = "[" + this.pseudoActuel + "] : " + message + "\n";
            
            // On ajoute le texte à la fin de la zone d'affichage
            zoneAffichageMessages.appendText(messageFormate);

            // On vide le champ de saisie pour le prochain message
            champSaisieMessage.clear();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}