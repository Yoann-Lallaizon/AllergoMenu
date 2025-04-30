package views;

import java.util.ArrayList;

import controllers.ControllerConsultationRecette;
import controllers.Tools;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.MainApp;
import models.Commentaire;
import models.Recette;
import models.RecetteIngredient;

public class VueConsultationRecette {
	
	private Recette recette;
	
	private ArrayList<Commentaire> commentairesRecette;
	
	private String message;
	private Label notificationLabel;
	
	public VueConsultationRecette(Recette recette, ArrayList<Commentaire> commentairesRecette) {
		this.recette = recette;
		 if (commentairesRecette != null) {
		        this.commentairesRecette = commentairesRecette;
		    } else {
		        this.commentairesRecette = new ArrayList<>();
		    }
		afficherConsultationRecette();
	}

	
	public void afficherConsultationRecette() {
		
		Button btnModifier = new Button();
		Button btnSupprimer = new Button();
		
		if(MainApp.getUtilisateurActif().getId_utilisateur() == this.recette.getId_utilisateur_createur_recette()) {
			btnModifier.setVisible(true);
			btnSupprimer.setVisible(true);
			Image imgModifier;
			ImageView imgViewModifier = new ImageView();
			Image imgSupprimer;
			ImageView imgViewSupprimer = new ImageView();
			//Icone du bouton modifier
			try {
				imgModifier = new Image("/images/modifier.png");       
				imgViewModifier = new ImageView(imgModifier);
				imgViewModifier.setFitWidth(25);
				imgViewModifier.setFitHeight(25);
				btnModifier.setGraphic(imgViewModifier);		
			}catch(Exception e) {
				System.out.println("Bouton modifier : "+e);
				btnModifier.setText("Modifier la recette");
			}
			
			//Icone du bouton supprimer
			try {
				imgSupprimer = new Image("/images/supprimer.png");       
				imgViewSupprimer = new ImageView(imgSupprimer);
				imgViewSupprimer.setFitWidth(25);
				imgViewSupprimer.setFitHeight(25);
				btnSupprimer.setGraphic(imgViewSupprimer);		
			}catch(Exception e) {
				System.out.println("Bouton supprimer : "+e);
				btnSupprimer.setText("Supprimer la recette");
			}
			
		}else {
			btnModifier.setVisible(false);
			btnSupprimer.setVisible(false);
		}
		
//BOUTON MODIFIER
		btnModifier.setOnAction(e -> {
            new ControllerConsultationRecette(this.recette).afficherVueModificationRecette();
        });
		
//BOUTON SUPPRIMER
		btnSupprimer.setOnAction(e -> afficherPopUpConfirmation("Etes-vous sûr de vouloir supprimer cette recette ?"));
		
//BOUTON SIGNALER
		Image imgSignaler;
		ImageView imgViewSignaler = new ImageView();
		Button btnSignaler = new Button();

		try {
			imgSignaler = new Image("/images/signaler.png");       
			imgViewSignaler = new ImageView(imgSignaler);
			imgViewSignaler.setFitWidth(25);
			imgViewSignaler.setFitHeight(25);
			btnSignaler.setGraphic(imgViewSignaler);		
		}catch(Exception e) {
			System.out.println("Bouton signaler : "+e);
			btnSignaler.setText("Signaler la recette");	
			
		}
		// Création du bouton avec l'image
		btnSignaler.setOnAction(e -> afficherPopUpConfirmation("Etes-vous sûr de vouloir signaler cette recette ?"));
	
//BOUTON FAVORI		
		boolean enFavori = false;
		for (Recette r : MainApp.getUtilisateurActif().getListe_favori_utilisateur()) {
		    if (r.getId_recette() == recette.getId_recette()) {
		    	enFavori = true;
		        break;
		    }
		}
		Image imgFavori;
		ImageView imgViewFavori = new ImageView();
		Button btnFavori = new Button();
		if (!enFavori) {
			//Icone du bouton favori
			try {
				imgFavori = new Image("/images/non-fav.png");       
				imgViewFavori = new ImageView(imgFavori);
				imgViewFavori.setFitWidth(25);
				imgViewFavori.setFitHeight(25);
				btnFavori.setGraphic(imgViewFavori);		
			}catch(Exception e) {
				System.out.println("Bouton favoris : "+e);
				btnFavori.setText("Ajouter aux favoris");
			}
			
			btnFavori.setOnAction(e -> new ControllerConsultationRecette(this.recette).ajoutFavori());

		}else {
			//Icone du bouton favori
			try {
				imgFavori = new Image("/images/fav.png");       
				imgViewFavori = new ImageView(imgFavori);
				imgViewFavori.setFitWidth(25);
				imgViewFavori.setFitHeight(25);
				btnFavori.setGraphic(imgViewFavori);		
			}catch(Exception e) {
				System.out.println("Bouton favoris : "+e);
				btnFavori.setText("Retirer des favoris");
			}
			
			btnFavori.setOnAction(e -> new ControllerConsultationRecette(this.recette).retraitFavori());
		}
					
		HBox barreAction = new HBox(btnModifier, btnSupprimer, btnSignaler, btnFavori);
		barreAction.setAlignment(Pos.CENTER);
        
// IMAGE
		Image image1;
		ImageView photoRecette = new ImageView();
		try {
			 	image1 = new Image("/images/"+this.recette.getPhoto_recette());       
		        photoRecette = new ImageView(image1);
		    	photoRecette.setFitWidth(250); 
		        photoRecette.setFitHeight(200);
		}catch(Exception e) {
			System.out.println(e);
		}
       
    	
// NOM_RECETTE
        Label nomRecette = new Label(this.recette.getNom_recette());
        nomRecette.getStyleClass().add("labelTitre");

// DIFFICULTE_RECETTE
        Label champ1 = new Label("Difficulté : " + this.recette.getDifficulte_recette());
        champ1.getStyleClass().add("labelText");
        HBox difficulteRecette = new HBox(champ1);
        difficulteRecette.setAlignment(Pos.CENTER);
        
// NB_PERSONNE_RECETTE
        Label champ2 = new Label("Nombre de personnes : " + Integer.toString(this.recette.getNb_personne_recette()));	//CONVERTI LA VALEUR DU GETTER DE INT A STRING
        champ2.getStyleClass().add("labelText");
        HBox nbPersonneRecette = new HBox(champ2);
        nbPersonneRecette.setAlignment(Pos.CENTER);
        
// PRIX_RECETTE
        Label champ3 = new Label("Niveau de prix : " + (this.recette.getPrix_recette()));
        champ3.getStyleClass().add("labelText");
        HBox prixRecette = new HBox(champ3);
        prixRecette.setAlignment(Pos.CENTER);
        
// TEMPS_PREPARATION
        Label champ4 = new Label("Temps de préparation : " + Integer.toString(this.recette.getTemps_preparation()) + " min.");
        champ4.getStyleClass().add("labelText");
        HBox tempsPreparation = new HBox(champ4);
        tempsPreparation.setAlignment(Pos.CENTER);
        
// TEMPS_CUISSON
        Label champ5 = new Label("Temps de cuisson : " + Integer.toString(this.recette.getTemps_cuisson()) + " min.");	
        champ5.getStyleClass().add("labelText");
        HBox tempsCuisson = new HBox(champ5);
        tempsCuisson.setAlignment(Pos.CENTER);
        
// TEMPS_TOTAL_RECETTE
        Label champ6 = new Label("Temps total : " + Integer.toString(this.recette.getTemps_total_recette()) + " min.");
        champ6.getStyleClass().add("labelText");
        HBox tempsTotalRecette = new HBox(champ6);
        tempsTotalRecette.setAlignment(Pos.CENTER);
        
// LISTE_INGREDIENTS_COMPLETS
        VBox listeIngredientsComplets = new VBox();
        for (RecetteIngredient i : this.recette.getListeIngredientsComplets()) {	// BOUCLE QUI PARCOURT CHAQUE VALEUR RECETTE_INGREDIENT DE LISTE_INGREDIENTS_COMPLETS
        	Label ingredientComplet = new Label ("- "+i.getIngredient().getNomIngredient().toUpperCase()+" : "+i.getQuantite()+" "+ i.getUnite());
        	listeIngredientsComplets.getChildren().addAll(ingredientComplet);
            listeIngredientsComplets.setAlignment(Pos.CENTER);
        }

// ETAPE_RECETTE       
        TextArea champ7 = new TextArea("Etapes : " + this.recette.getEtape_recette());
        VBox etapeRecette = new VBox(champ7);
        etapeRecette.setAlignment(Pos.CENTER);
        champ7.setWrapText(true); // ACTIVE LE RETOUR A LA LIGNE AUTOMATIQUE
        champ7.setMaxWidth(500);
        champ7.setPrefHeight(150);
        champ7.setEditable(false);
        
// COMMENTAIRE_RECETTE
        VBox commentaireGlobal = new VBox();
        if (commentairesRecette.size()>0) {
	        for(Commentaire c : commentairesRecette) {
        		Label pseudoUtilisateur = new Label(c.getUtilisateur().getPseudo_utilisateur());
        		pseudoUtilisateur.getStyleClass().add("labelCom1");
        		Label datePubliCommentaire = new Label(c.getDatePubliCommentaire());
        		datePubliCommentaire.getStyleClass().add("labelCom2");
        		Image avatarComm;
        		ImageView avatarCommentaire = new ImageView();
        		try {
        				avatarComm = new Image("/images/"+c.getUtilisateur().getAvatar_utilisateur());       
        				avatarCommentaire = new ImageView(avatarComm);
        				avatarCommentaire.setFitWidth(40); 
        				avatarCommentaire.setFitHeight(40);
        		}catch(Exception e) {
        			System.out.println(e);
        		}
        		
        		VBox premiereColonneCom = new VBox(datePubliCommentaire, avatarCommentaire, pseudoUtilisateur);
        		premiereColonneCom.setAlignment(Pos.CENTER);
        		Label textCommentaire = new Label(c.getTextCommentaire());
        		textCommentaire.getStyleClass().add("labelCom3");
        		Image imgSignalerCom;
        		ImageView imgViewSignalerCom = new ImageView();
        		Button btnSignalerCom = new Button();

        		try {
        			imgSignalerCom = new Image("/images/signaler.png");       
        			imgViewSignalerCom = new ImageView(imgSignalerCom);
        			imgViewSignalerCom.setFitWidth(25);
        			imgViewSignalerCom.setFitHeight(25);
        			btnSignalerCom.setGraphic(imgViewSignalerCom);		
        		}catch(Exception e) {
        			System.out.println("Bouton signaler : "+e);
        			btnSignaler.setText("Signaler la recette");	
        			
        		}        		
        		
        		btnSignalerCom.setOnAction(e -> {
        		        new ControllerConsultationRecette(this.recette).signalerCommentaire(c);
        		});
        		
        		Button btnsupprimerCommentaire = new Button("Supprimer mon commentaire");
        		if(MainApp.getUtilisateurActif().getId_utilisateur() == c.getUtilisateur().getId_utilisateur()) {
        			btnsupprimerCommentaire.setVisible(true);
        			btnSignalerCom.setVisible(false);
        		}else {
        			btnsupprimerCommentaire.setVisible(false);
        		}
        		
        		btnsupprimerCommentaire.setOnAction(e -> {
        			new ControllerConsultationRecette(this.recette).supprimerCommentaire(c.getIdCommentaire());
        		 });
        		
        		VBox deuxiemeColonneCom = new VBox(textCommentaire);
        		deuxiemeColonneCom.setAlignment(Pos.CENTER);
     
        		VBox troisiemeColonneCom = new VBox(btnSignalerCom,btnsupprimerCommentaire);
        		troisiemeColonneCom.setAlignment(Pos.CENTER);
        		
        		HBox ligneCom = new HBox(premiereColonneCom,deuxiemeColonneCom,troisiemeColonneCom);
        		ligneCom.setMinHeight(100);
        		ligneCom.setAlignment(Pos.CENTER);
        		commentaireGlobal.getChildren().add(ligneCom);
        	}
        }
// AJOUT COMMENTAIRE        
        TextArea textNouveaucommentaire = new TextArea();
        textNouveaucommentaire.setPromptText("Entrez votre commentaire ici ...");
        textNouveaucommentaire.setPrefRowCount(3);
        textNouveaucommentaire.setWrapText(true);
        textNouveaucommentaire.setMaxWidth(400);
        
     // Filtrage et controle de saisie
        textNouveaucommentaire.textProperty().addListener((observable, oldValue, newValue) -> {
            // On définit ici le ou les caractères à interdire
            String filtered = newValue.replaceAll("[@#()=+~²></]", "");
            if (!filtered.equals(newValue)) {
                //  Si caractères interdits le texte est remplacé avec la version filtrée
            	textNouveaucommentaire.setText(filtered);
            }
        });
        
        
        Label counterLabel = new Label("0/500");
     // Ajout d'un listener pour limiter le texte à 500 caractères et mettre à jour le compteur
        textNouveaucommentaire.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 500) {
                // Si le texte dépasse 500 caractères, on le tronque
            	textNouveaucommentaire.setText(newValue.substring(0, 500));
            } else {
                // Mise à jour du compteur
                counterLabel.setText(newValue.length() + "/500");
            }
        });
        VBox count = new VBox(counterLabel);
        count.setAlignment(Pos.BOTTOM_LEFT);
        
        
        Button btnEnvoyer = new Button("Envoyer");
        btnEnvoyer.setAlignment(Pos.CENTER);
     // Définir l'action lors du clic sur le bouton
        btnEnvoyer.setOnAction(e -> {
            String commentaire = textNouveaucommentaire.getText();
            if (!commentaire.trim().isEmpty()) {
                message = new ControllerConsultationRecette(this.recette).envoyerCommentaire(commentaire);
                // On peut vider la zone de texte après l'envoi
                textNouveaucommentaire.clear();
                Tools.showAlert(message);
            } else {
                System.out.println("Veuillez entrer un commentaire.");
            }
        });
        notificationLabel = new Label("");
        HBox ajoutcommentaire = new HBox(5, textNouveaucommentaire,count, btnEnvoyer);
        ajoutcommentaire.setPadding(new Insets(10));
        ajoutcommentaire.setAlignment(Pos.CENTER);
        
// CONTENEUR GLOBAL VBOX CONTENANT CHAQUE ELEMENT DECRIT CI-DESSUS, ESPACE DE 10 PIXELS
        VBox global = new VBox(10,MainApp.getMaToolBar().getTop(),nomRecette,photoRecette,difficulteRecette,nbPersonneRecette,
        		tempsPreparation,tempsCuisson,tempsTotalRecette,listeIngredientsComplets,etapeRecette, barreAction, notificationLabel, ajoutcommentaire,commentaireGlobal);
        global.setAlignment(Pos.TOP_CENTER);
        global.getStyleClass().add("background");
// COMPOSANT PERMETTANT LE DEFILEMENT LORSQUE LE CONTENU DEPASSE LA ZONE VISIBLE
        ScrollPane scrollPane = new ScrollPane(global);// PEUT AUSSI S'ECRIRE : scrollPane.setContent(global);
        scrollPane.setFitToWidth(true); // AJUSTE LA LARGEUR DU CONTENU A LA LARGEUR DU SCROLLPANE
        scrollPane.setFitToHeight(true);       
// CREATION ET CONFIGURATION DE LA SCENE
        Scene scene = new Scene(scrollPane,800,800);
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
// CONFIGURATION DE LA SCENE PRINCIPALE
        MainApp.getMainStage().setTitle("Affichage recette");
        MainApp.getMainStage().setScene(scene);   
        MainApp.getMainStage().setMaximized(true);
        MainApp.getMainStage().show();
	}	
	
	private void afficherPopUpConfirmation(String message) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("Confirmation");
		confirmation.setHeaderText(null);
		confirmation.setContentText(message);
		
		confirmation.showAndWait().ifPresent(response -> {
			if(message.toString().equals("Etes-vous sûr de vouloir signaler cette recette ?")) {
				if (response.getText().equals("OK")) {
					new ControllerConsultationRecette(this.recette).signalerRecette();
				}
			}
			
			if(message.toString().equals("Etes-vous sûr de vouloir supprimer cette recette ?")) {
				if (response.getText().equals("OK")) {
					new ControllerConsultationRecette(this.recette).supprimerRecette(this.recette.getId_recette());
				}
			}
		});
	}
}
