package views;


import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import controllers.ControllerParametreCompte;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.MainApp;

public class VueParametreCompte {
    
   

    private ImageView avatarView;
    private ArrayList<String> listeUrl;
    private Stage stage;
	// Constructeur
    public VueParametreCompte() {
    	this.avatarView = new ImageView();
    	this.listeUrl = new ArrayList<>();
    	this.stage = new Stage();
        start();
    }
    
    //ACCESSEURS
    
    //sert potentiellement à rien j'ose pas y toucher
    public void setAvatarView(Image image) {
    	this.avatarView.setImage(image);
    }

    public void start() {
    	
        // Section 1: Mes informations
        Label infoLabel = new Label("Mes informations");
        infoLabel.getStyleClass().add("labelText");
        
        //NOM
        TextField nomField = new TextField();
        nomField.setText(MainApp.getUtilisateurActif().getNom_utilisateur());
        
        //PRENOM
        TextField prenomField = new TextField();
        prenomField.setText(MainApp.getUtilisateurActif().getPrenom_utilisateur());
        
        //BOUTONS RADIO
        ToggleGroup sexeGroup = new ToggleGroup();
        RadioButton hommeRadio = new RadioButton("Masculin");
        RadioButton femmeRadio = new RadioButton("Féminin");
        RadioButton noSexRadio = new RadioButton("Non renseigné");
        hommeRadio.setToggleGroup(sexeGroup);
        femmeRadio.setToggleGroup(sexeGroup);
        noSexRadio.setToggleGroup(sexeGroup);
        
        if (MainApp.getUtilisateurActif().getSexe_utilisateur()=='M' ) {
        	hommeRadio.setSelected(true);
        }
        
        if (MainApp.getUtilisateurActif().getSexe_utilisateur()=='F') {
        	femmeRadio.setSelected(true);
        }
        
        if (MainApp.getUtilisateurActif().getSexe_utilisateur()=='N') {
        	noSexRadio.setSelected(true);
        }
        
        VBox boxSexGroup = new VBox(hommeRadio, femmeRadio, noSexRadio);
//EMAIL
        TextField emailField = new TextField();
        emailField.setText(MainApp.getUtilisateurActif().getMail_utilisateur());
        
//DATE DE NAISSANCE
        DatePicker dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setValue(LocalDate.parse(MainApp.getUtilisateurActif().getDate_naissance_utilisateur()));
        
//PSEUDO
        TextField pseudoField = new TextField();
        pseudoField.setText(MainApp.getUtilisateurActif().getPseudo_utilisateur());
        
//BOUTTON POUR UPLOAD L'AVATAR
        Button uploadAvatarButton = new Button("Choisir un avatar");
        
//AFFICHAGE DE L'AVATAR STOCKE DANS L'UTILISATEUR ACTIF
        try {
            Image avatar = new Image("/images/"+MainApp.getUtilisateurActif().getAvatar_utilisateur());
            avatarView.setImage(avatar);
            avatarView.setFitWidth(100);
            avatarView.setFitHeight(100);
            
            //Si aucun avatar sélectionné ou si url invalide, ça met une image par défaut.
        } catch(Exception e) {
            System.out.println(e);
            Image avatar = new Image("/images/avatarParDefaut.jpg");
            avatarView.setImage(avatar);
            avatarView.setFitWidth(100);
            avatarView.setFitHeight(100);
        }
        
        
        
        	// Section 2: Mes maladies
        Label maladieLabel = new Label("Mes maladies");
        maladieLabel.getStyleClass().add("labelText");
        
//LES CHECKBOX POUR LES TROIS MALADIES
        CheckBox cholesterolCheck = new CheckBox("Cholestérol");
        CheckBox hypertensionCheck = new CheckBox("Hypertension");
        CheckBox insuffisanceRenaleCheck = new CheckBox("Insuffisance rénale");


//COCHAGE DES CHECKBOX EN FONCTION DE LA LISTE DES MALADIES DE L'UTILISATEUR ACTIF
        
        //On vérifie d'abord si la liste est vide.
        if (!MainApp.getUtilisateurActif().getListe_maladie_utilisateur().isEmpty()) {
        	
        	//on parcours la liste et on coche les checkbox selon les correspondance
        	for (int i=0 ; i < MainApp.getUtilisateurActif().getListe_maladie_utilisateur().size() ; i++ ) {
            String maladieUtilisateur = MainApp.getUtilisateurActif().getListe_maladie_utilisateur().get(i).getNom_Maladie();
            
	            if (maladieUtilisateur.equalsIgnoreCase("Cholestérol")) {
	                cholesterolCheck.setSelected(true);
	            } 
	            
	            if (maladieUtilisateur.equalsIgnoreCase("Hypertension")) {
	                hypertensionCheck.setSelected(true);
	            }
	            if (maladieUtilisateur.equalsIgnoreCase("Insuffisance rénale")) {
	                insuffisanceRenaleCheck.setSelected(true);
	            }
        	}
        }
        
     // BOUTTON DE VALIDATION
        Button validationButton = new Button("Valider");
        validationButton.setAlignment(Pos.CENTER);

      //gestion du click sur le bouton d'upload
        uploadAvatarButton.setOnAction(e -> {
       
        	//remplissage de la liste des url à l'aide des informations données par le controlleur
        	this.listeUrl = new ControllerParametreCompte().uploadAvatar();
        	
        	try {
        		String chemin = listeUrl.get(0);
        		File fichierImage = new File(chemin);

        		if (fichierImage.exists()) {
        		    avatarView.setImage(new Image(fichierImage.toURI().toString()));
        		} 
            } catch(Exception r) {
                System.out.println(r);
            }
        });
        
        validationButton.setOnAction(e -> {
        	//Transmission des parametres à modifier au controller. Attention au Null, normalement ca sera la maladie séelctionnée, j'ai juste fait ça pour le test. 
        	RadioButton selectedSexeRadio = (RadioButton) sexeGroup.getSelectedToggle();
        	new ControllerParametreCompte().mettreAJourInformations(nomField.getText(), prenomField.getText(), emailField.getText(), dateNaissancePicker.getValue(), pseudoField.getText(), selectedSexeRadio.getText(), cholesterolCheck.isSelected(), hypertensionCheck.isSelected(), insuffisanceRenaleCheck.isSelected(),  listeUrl);
        	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("Paramètres enregistrés !");
			alert.showAndWait();
            System.out.println("Paramètres validés");
            System.out.println("nouvelle photo de l'utilisateur : "+MainApp.getUtilisateurActif().getAvatar_utilisateur());
            
        });
               
        VBox maladieBox = new VBox(10, maladieLabel, cholesterolCheck, hypertensionCheck, insuffisanceRenaleCheck);
        Button btnSup = new Button("Supprimer mon profil");

        btnSup.setOnAction(e -> {
        	afficherPopUpConfirmation("Êtes-vous sûr de vouloir supprimer votre compte et toutes vos recettes ?");
        });
        
        VBox infoBox = new VBox(5);
        infoBox.getChildren().addAll(infoLabel, nomField, prenomField, boxSexGroup, emailField, dateNaissancePicker, pseudoField, uploadAvatarButton, avatarView);
        infoBox.setAlignment(Pos.CENTER);
        Label infoModifMDP = new Label();
        PasswordField txtModifMDP = new PasswordField();
        txtModifMDP.setPromptText("Entrez votre nouveau mot de passe");
        PasswordField txtConfirmModifMDP = new PasswordField();
        txtConfirmModifMDP.setPromptText("Ressaisir le mot de passe");
        Button btnModifMDP = new Button("Modifier le mot de passe");
        btnModifMDP.setOnAction(e ->{
        	infoModifMDP.setText(new ControllerParametreCompte().modifierMotDePasse(txtModifMDP.getText(), txtConfirmModifMDP.getText()));
        });
        VBox boxModifMDP = new VBox(5, infoModifMDP, txtModifMDP, txtConfirmModifMDP, btnModifMDP);
        boxModifMDP.setAlignment(Pos.CENTER);
        
        VBox root = new VBox(20, infoBox, maladieBox, validationButton, btnSup, boxModifMDP);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        
        ScrollPane global = new ScrollPane(root);
        
        /*------------------- STYLE CSS---------------------------*/
        root.getStyleClass().add("background");
        global.getStyleClass().add("background");
        global.setFitToWidth(true);
        global.setFitToHeight(true);
        
        /*--------------------------------------------------------*/
        
        // CALCULS TAILLE ECRAN
        double largeurEcran = Screen.getPrimary().getBounds().getWidth();
        double hauteurEcran = Screen.getPrimary().getBounds().getHeight();
        
        // DEFINIR LES DIMENSIONS DU STAGE
        double largeurStage = 265;
        this.stage.setWidth(largeurStage);
        this.stage.setHeight(hauteurEcran - 50);
        
        // POSITIONNEMENT DU STAGE
        this.stage.setX(largeurEcran - largeurStage); // ALIGNE A DROITE
        this.stage.setY(0); // DANS LE COIN SUPERIEUR
        
        Scene scene = new Scene(global);
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
		
		
		this.stage.setScene(scene);
        this.stage.setTitle("Paramètres du compte");
        this.stage.show();
    }
    
    private void afficherPopUpConfirmation(String message) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("Confirmation");
		confirmation.setHeaderText(null);
		confirmation.setContentText(message);
		
		confirmation.showAndWait().ifPresent(response -> {
				if (response.getText().equals("OK")) {
		        	new ControllerParametreCompte().supprimerUtilisateur(this.stage);
				}
		});
	}

}
