package views;

import controllers.ControllerConnexion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.MainApp;

public class VueConnexion {

	public VueConnexion() {
		afficherConnexion();
	} 
	
	public void afficherConnexion() {
//IMAGES		
		ImageView logoView = new ImageView();
		Image logo;
		try {
			logo = new Image(getClass().getResourceAsStream("/images/logoAllergo.png"));
			logoView = new ImageView(logo);
			logoView.setPreserveRatio(true);
			logoView.setFitWidth(150);
        }catch(Exception e) {
        	System.out.println(e.getMessage());
        }
		  
        ImageView image2V = new ImageView();
        Image image2;
        try {
        	image2 = new Image(getClass().getResourceAsStream("/images/fusee.gif"));
            image2V.setImage(image2);
            image2V.setPreserveRatio(true);
            image2V.setFitWidth(200);
            image2V.setVisible(false);
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image : " + e.getMessage());
        }
        
        // CHAMPS DU FORMULAIRE
        Label userLabel = new Label("Nom d'utilisateur :");
        userLabel.getStyleClass().add("labelText");
        TextField userField = new TextField();
        Label titleLabel = new Label("Connexion");
        titleLabel.getStyleClass().add("labelTitre");
        Label passLabel = new Label("Mot de passe :");
        passLabel.getStyleClass().add("labelText");
        PasswordField passField = new PasswordField();
        Hyperlink lienInscription = new Hyperlink("Pas encore de compte ? Inscrivez-vous !");

        // BOUTON DE CONNEXION ET GRIDPANE
        Button loginBouton = new Button("Connexion");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.setAlignment(Pos.CENTER);
        VBox root = new VBox(10, logoView, titleLabel, grid, loginBouton, lienInscription, image2V);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("background");
        // DEFINITION DU BOUTON PAR DEFAUT (ENTREE)
        loginBouton.setDefaultButton(true);
        
        // ACTION : CONNEXION
        loginBouton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            
            if (username.equalsIgnoreCase("fusée") && password.equalsIgnoreCase("fusée")) {
            	image2V.setVisible(true);
            }
            
            if (new ControllerConnexion(username, password).handleLogin()) {	
                // CONNEXION REUSSIE
            } else {
                // CONNEXION ECHOUEE : REINITIALISER LE CHAMP MOT DE PASSE ET RETOURNER AU CHAMP USERNAME
                passField.setText("");
                userField.requestFocus();
            }
        });
        
        // ACTION : REDIRECTION VERS L'INSCRIPTION
        lienInscription.setOnAction(e -> {
        	new VueInscription();
        });
        
        // CREATION DE LA SCENE ET APPLICATION DU CSS
        Scene scene = new Scene(root);
        try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }

        // CONFIGURATION ET AFFICHAGE DU STAGE
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().setMaximized(true);
        MainApp.getMainStage().setTitle("Page de Connexion");
        MainApp.getMainStage().show();
      
      
    }


}
