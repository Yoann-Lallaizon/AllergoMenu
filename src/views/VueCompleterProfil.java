package views;

import java.io.File;
import java.util.ArrayList;

import controllers.ControllerCompleterProfil;
import controllers.ControllerParametreCompte;
import controllers.Tools;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.MainApp;

public class VueCompleterProfil extends StackPane {

    private ArrayList<String> listeUrl, nomMaladies;
    private String pseudo, email, password;

    public VueCompleterProfil(String pseudo, String email, String password, ArrayList<String> nomMaladies) {
    	this.pseudo = pseudo;
    	this.password = password;
    	this.email = email;
    	this.nomMaladies = nomMaladies;
    	afficherCompleterProfil() ;
    }

    public void afficherCompleterProfil() {
        TabPane tabPane = new TabPane();

    // ONGLET "MES INFORMATIONS"
        Tab tabInformations = new Tab("Mes informations");
        tabInformations.setClosable(false);

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(20));

        Label avatarLabel = new Label("Avatar (nécessaire pour créer un compte) :");
        avatarLabel.getStyleClass().add("labelText");
        ImageView avatarView = new ImageView();
        avatarView.setFitWidth(100);
        avatarView.setFitHeight(100);
        try {
            Image avatar = new Image("/images/avatarParDefaut.jpg");
            avatarView.setImage(avatar);
            avatarView.setFitWidth(100);
            avatarView.setFitHeight(100);
        } catch(Exception e) {
            System.out.println(e);
        }
        
        Button selectAvatarButton = new Button("Choisir une image");
        selectAvatarButton.setOnAction(event -> {
        	// REMPLISSAGE DE LA LISTE DES URL A L'AIDE DES INFOS DONNEES PAR LE CONTROLEUR
        	this.listeUrl = new ControllerParametreCompte().uploadAvatar();
        	try {
        		String chemin = listeUrl.get(0);
        		File fichierImage = new File(chemin);

        		if (fichierImage.exists()) {
        		    avatarView.setImage(new Image(fichierImage.toURI().toString()));
        		} 
            } catch(Exception e) {
                System.out.println(e);
            }
        });

        Label nameLabel = new Label("Nom:");
        nameLabel.getStyleClass().add("labelText");
        TextField nameField = new TextField();
        Label firstNameLabel = new Label("Prénom:");
        firstNameLabel.getStyleClass().add("labelText");
        TextField firstNameField = new TextField();
        Label birthLabel = new Label("Date de naissance:");
        birthLabel.getStyleClass().add("labelSousTitre");
        DatePicker birthDatePicker = new DatePicker();

        Label sexLabel = new Label("Sexe:");
        sexLabel.getStyleClass().add("labelText");
        ToggleGroup sexGroup = new ToggleGroup();
        RadioButton hommeRadio = new RadioButton("Homme");
        hommeRadio.setToggleGroup(sexGroup);
        hommeRadio.setUserData("H"); // ASSOCIE "H" A CE BOUTON

        RadioButton femmeRadio = new RadioButton("Femme");
        femmeRadio.setToggleGroup(sexGroup);
        femmeRadio.setUserData("F"); // ASSOCIE "F" A CE BOUTON

        RadioButton nbRadio = new RadioButton("NB");
        nbRadio.setToggleGroup(sexGroup);
        nbRadio.setUserData("N"); // ASSOCIE "N" A CE BOUTON

        infoBox.getChildren().addAll(avatarLabel, avatarView, selectAvatarButton, 
                nameLabel, nameField, firstNameLabel, firstNameField, 
                birthLabel, birthDatePicker, sexLabel, hommeRadio, femmeRadio, nbRadio);
        tabInformations.setContent(infoBox);

     // ONGLET "INTOLÉRANCES"
        Tab tabIntolerances = new Tab("Intolérances:");
        tabIntolerances.setClosable(false);

        VBox intolerancesBox = new VBox(10);
        intolerancesBox.setPadding(new Insets(20));

        Label intolerancesLabel = new Label("Sélectionner mes intolérances:");
        intolerancesLabel.getStyleClass().add("labelSousTitre");
        VBox maladiesContainer = new VBox(10);
        ArrayList<CheckBox> checkBoxMaladies = new ArrayList<>();
        for (String nom : nomMaladies) {
            CheckBox checkBox = new CheckBox(nom);
            checkBoxMaladies.add(checkBox); // AJOUT A LA LISTE
            maladiesContainer.getChildren().add(checkBox);
        }
        intolerancesBox.getChildren().addAll(intolerancesLabel, maladiesContainer);

        Button validerButton = new Button("Valider");
        validerButton.setOnAction(event -> {
            // RECUPERE LE SEXE
        	Toggle selectedToggle = sexGroup.getSelectedToggle();
        	String sexeSelect="";
            if (selectedToggle != null) {
                sexeSelect =  (String)selectedToggle.getUserData(); // RETOURNE "H", "F", OU "N"
            }else {
            	return;
            }
            
            // RECUPERE LES MALADIES SÉLECTIONNÉES
            ArrayList<String> maladieSelect = new ArrayList<>();
            for (CheckBox cb : checkBoxMaladies) {
                if (cb.isSelected()) {
                	maladieSelect.add(cb.getText());
                }
            }
            System.out.println(sexeSelect);
            Tools.showAlert(new ControllerCompleterProfil().enregistrerProfil(this.pseudo, this.email, this.password, nameField.getText(), firstNameField.getText(),birthDatePicker.getValue().toString(), sexeSelect, maladieSelect, listeUrl));
        });
        intolerancesBox.getChildren().add(validerButton);

        tabIntolerances.setContent(intolerancesBox);
       
        // AJOUT DES ONGLETS DANS LE TABPANE
        tabPane.getTabs().addAll(tabInformations, tabIntolerances);

        // VBOX PRINCIPALE : CONTIENT LE TABPANE
        VBox mainBox = new VBox(10);
        mainBox.setPadding(new Insets(20));
        mainBox.getChildren().addAll(tabPane);
        mainBox.getStyleClass().add("background");

        ScrollPane scrollpane = new ScrollPane(mainBox);
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);
        scrollpane.getStyleClass().add("background");
        this.getChildren().add(scrollpane);
        
        // CREATION DE LA SCENE ET CHARGEMENT DU CSS
        Scene scene = new Scene(mainBox);
        try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
        // CONFIGURATION ET AFFICHAGE DE LA MAINSTAGE
        MainApp.getMainStage().setTitle("Compléter profil");
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().show();

    }
    
}