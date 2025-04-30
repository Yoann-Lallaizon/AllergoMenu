package views;

import controllers.ControllerInscription;
import controllers.Tools;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.MainApp;

public class VueInscription {
    public VueInscription() {
        afficherInscription();
    }

    public void afficherInscription() {
    	// CHAMPS DU FORMULAIRE
        Label titleLabel = new Label("Création d'un compte");
        titleLabel.getStyleClass().add("labelTitre");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Pseudo");
        TextField emailField = new TextField();
        emailField.setPromptText("Adresse e-mail");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmer le mot de passe");
        Button registerButton = new Button("Créer le compte");

        // ACTION SUR LE BOUTON : CREER LE COMPTE
        registerButton.setOnAction(e -> {
        	Tools.showAlert(new ControllerInscription().handleInscription(
                usernameField.getText(),
                emailField.getText(),
                passwordField.getText(),
                confirmPasswordField.getText()
            ));
        });
        
        // DEFINITION DU BOUTON PAR DEFAUT (ENTREE)
        registerButton.setDefaultButton(true);

        VBox layout = new VBox(10, titleLabel, usernameField, emailField, passwordField, confirmPasswordField, registerButton);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("background");

        // CREATION DE LA SCENE ET CHARGEMENT DU CSS
        Scene scene = new Scene(layout, 700, 500);
        try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
        
        // CONFIGURATION ET AFFICHAGE DE LA MAINSTAGE
        MainApp.getMainStage().setTitle("Inscription");
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();
    }
}
