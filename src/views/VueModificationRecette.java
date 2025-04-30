package views;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import controllers.ControllerAccueil;
import controllers.ControllerModificationRecette;
import controllers.Tools;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.MainApp;
import models.Ingredient;
import models.Recette;
import models.RecetteIngredient;


public class VueModificationRecette {
	
	private Recette recette;
	private ArrayList<String> lienPhoto;
	public VueModificationRecette(Recette recette) {
		this.recette = recette;
		afficherModificationRecette();
	}

	public void afficherModificationRecette() {
	    
// RECUPERATION DES DONNEES UTILES POUR LE CONTROLLER
	    int idRecette = recette.getId_recette();
	    int valeurNbPersonneRecette = recette.getNb_personne_recette();
	    int valeurTpsPreparation = recette.getTemps_preparation();
	    int valeurTpsCuisson = recette.getTemps_cuisson();
	    
//BOUTON RETOUR
	    Button btnretour = new Button("Retour à la recette");
	    btnretour.setOnAction(e -> {
	    	new ControllerAccueil(this.recette).afficherVueConsultationRecette();
	    });
	    
// NOM_RECETTE
	    Label label1 = new Label("Nom recette : ");
	    label1.getStyleClass().add("labelSousTitre");
	    TextField zone1 = new TextField(recette.getNom_recette());
	    zone1.setPromptText("Entrer le nom de votre recette...");	// PROMPT QUI S'AFFICHE SI SUPPRESSION DU TEXTE
	    zone1.setPrefHeight(40);	// HAUTEUR PREFEREE
	    zone1.setMinWidth(300);		// LARGEUR PREFEREE
	    zone1.setStyle("-fx-alignment: center; -fx-text-alignment: center;-fx-text-fill: black;"); // EXEMPLE APPLICATION STYLE CSS
        
   	    // AJUSTEMENT TAILLE DU CHAMP EN FONCTION DU TEXTE SAISI PAR L'UTILISATEUR
	    zone1.textProperty().addListener((observable, oldValue, newValue) -> {
	    	// CALCUL DE LA LARGEUR NECESSAIRE EN FONCTION DE LA LONGUEUR DU TEXTE
            int textLength = newValue.length();
            double newWidth1 = textLength * 7 + 20;  // AJUSTER LE COEFFICIENT EN FONCTION DE LA POLICE
            zone1.setPrefWidth(newWidth1);  // APPLIQUER LA NOUVELLE LARGEUR
        });
	    	  
	    HBox nomRecette = new HBox(label1,zone1);
	    nomRecette.setAlignment(Pos.CENTER);

// IMAGE
	    Image image = new Image("/images/"+recette.getPhoto_recette());       
        ImageView photoRecette = new ImageView(image);
    	photoRecette.setFitWidth(250);
        photoRecette.setFitHeight(200);

// SELECTEUR DE FICHIERS
        Button boutonParcourir = new Button("Parcourir");
	    TextField zone2 = new TextField();
        zone2.setPromptText("Sélectionnez un fichier...");
        zone2.setPrefWidth(300);
        zone2.setDisable(true);

        // ACTION SUR LE BOUTON QUI PERMET 
        boutonParcourir.setOnAction(e -> {
        	this.lienPhoto = new ControllerModificationRecette().uploadImage();
        	try {
        		String chemin = this.lienPhoto.get(0);
        		File fichierImage = new File(chemin);

        		if (fichierImage.exists()) {
        			photoRecette.setImage(new Image(fichierImage.toURI().toString()));
        		} 
            } catch(Exception z) {
                System.out.println(z);
            }
        });
        
        HBox selecteurFichier = new HBox(10,zone2,boutonParcourir);
	    selecteurFichier.setAlignment(Pos.CENTER);
	    
// DIFFICULTE_RECETTE
	    Label label2 = new Label("Difficulté : ");
	    label2.getStyleClass().add("labelText");
	    ComboBox<String> listeDifficulte = new ComboBox<>();
        listeDifficulte.getItems().addAll("Très facile","Facile","Moyen","Difficile","Très difficile");
        listeDifficulte.getSelectionModel().select(recette.getDifficulte_recette());
        HBox difficulteRecette = new HBox(label2,listeDifficulte);
        difficulteRecette.setAlignment(Pos.CENTER); 
                 
// NB_PERSONNE_RECETTE
        Label label3 = new Label("Nombre de personnes : ");
        label3.getStyleClass().add("labelText");
        Spinner<Integer> listeDeroulante1 = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs1 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        listeDeroulante1.setValueFactory(plageDeValeurs1);
        listeDeroulante1.setEditable(true);
        listeDeroulante1.getEditor().setPrefWidth(70);

        TextFormatter<Integer> restrictionEntiers1 = new TextFormatter<>(plageDeValeurs1.getConverter(), plageDeValeurs1.getValue()); //FORMATE ET RESTREINT LES ENTREES DE TEXTE DANS LE CHAMP TEXTE
        listeDeroulante1.getEditor().setTextFormatter(restrictionEntiers1); // APPLICATION DE CE FORMATAGE AUX ENTREES DANS LE CHAMP LISTE DEROULANTE
        listeDeroulante1.getValueFactory().setValue(recette.getNb_personne_recette()); // VALEUR RECUPEREE A L'AFFICHAGE
        
        listeDeroulante1.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (newValue != null) {
    			if (newValue < 1 || newValue> 10) {
    				Tools.showAlert("Le nombre de personne doit être compris entre 1 et 10 !");
    				listeDeroulante1.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le nombre de personne ne peut être nul !");
				listeDeroulante1.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox nbPersonneRecette = new HBox(label3,listeDeroulante1);
        nbPersonneRecette.setAlignment(Pos.CENTER);

// PRIX_RECETTE
        Label label4 = new Label("Niveau de prix : ");
        label4.getStyleClass().add("labelText");
        ComboBox<String> listePrix = new ComboBox<>();
        listePrix.getItems().addAll("Economique","Bon marché","Abordable","Couteux","Très couteux");
        listePrix.getSelectionModel().select(recette.getPrix_recette());
        HBox prixRecette = new HBox(label4,listePrix);
        prixRecette.setAlignment(Pos.CENTER);

// TEMPS_PREPARATION
        Label label5 = new Label("Temps de préparation : ");
        label5.getStyleClass().add("labelText");
        Spinner<Integer> listeDeroulante2 = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs2 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1);
        listeDeroulante2.setValueFactory(plageDeValeurs2);
        listeDeroulante2.setEditable(true);
        listeDeroulante2.getEditor().setPrefWidth(70);
        
        TextFormatter<Integer> restrictionEntiers2 = new TextFormatter<>(plageDeValeurs2.getConverter(), plageDeValeurs2.getValue());
        listeDeroulante2.getEditor().setTextFormatter(restrictionEntiers2);
        listeDeroulante2.getValueFactory().setValue(recette.getTemps_preparation());
        
        listeDeroulante2.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (newValue != null) {
    			if (newValue < 1 || newValue> 200) {
    				Tools.showAlert("Le temps de préparation doit être compris entre 1 et 200 !");
    				listeDeroulante2.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le temps de préparation ne peut être nul !");
				listeDeroulante2.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox tempsPreparation = new HBox(label5,listeDeroulante2);
        tempsPreparation.setAlignment(Pos.CENTER);
        
// TEMPS_CUISSON
        Label label6= new Label("Temps de cuisson : ");
        label6.getStyleClass().add("labelText");
        Spinner<Integer> listeDeroulante3 = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs3 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1);
        listeDeroulante3.setValueFactory(plageDeValeurs3);
        listeDeroulante3.setEditable(true);
        listeDeroulante3.getEditor().setPrefWidth(70);
        
        TextFormatter<Integer> restrictionEntiers3 = new TextFormatter<>(plageDeValeurs3.getConverter(), plageDeValeurs3.getValue()); 
        listeDeroulante3.getEditor().setTextFormatter(restrictionEntiers3); 
        listeDeroulante3.getValueFactory().setValue(recette.getTemps_cuisson());
        
        listeDeroulante3.valueProperty().addListener((obs, oldValue, newValue) -> {
        	if (newValue != null) {
    			if (newValue < 1 || newValue> 200) {
    				Tools.showAlert("Le temps de cuisson doit être compris entre 1 et 200 !");
    				listeDeroulante3.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le temps de cuisson ne peut être nul !");
				listeDeroulante3.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox tempsCuisson = new HBox(label6,listeDeroulante3);
        tempsCuisson.setAlignment(Pos.CENTER);
 
// LISTE_INGREDIENTS_COMPLETS
        
        // RECUPERATION DE LA MAP_INGREDIENTS DEPUIS MAINAPP
       Map<Integer, Ingredient> mapIngredients = MainApp.getMapIngredients();
        
        VBox listeIngredients = new VBox(5);
        Button btnAjouter = new Button("Ajouter un ingrédient");
        btnAjouter.setOnAction(e -> {
            HBox ligneIngredient = creerLigneIngredient(listeIngredients);
            listeIngredients.getChildren().add(ligneIngredient);
        });
            for (RecetteIngredient i : recette.getListeIngredientsComplets()) {	// BOUCLE QUI PARCOURT CHAQUE VALEUR RECETTE_INGREDIENT DE LISTE_INGREDIENTS_COMPLETS 
        	
        	ComboBox<String> comboBox = new ComboBox<>();
        	       	   	
        	for (Ingredient j : mapIngredients.values()) {	// BOUCLE QUI PARCOURT CHAQUE VALEUR INGREDIENT DE MAP_INGREDIENTS
                comboBox.getItems().add(j.getNomIngredient());	// POUR CHAQUE INGREDIENT LE NOM DE L'INGREDIENT EST AJOUTE A LA LISTE DES ELEMENTS DU COMBOBOX
            }
        	
        	comboBox.getSelectionModel().select(i.getIngredient().getNomIngredient());	//L'ELEMENT I CORRESPONDANT AU NOM DE L'INGREDIENT ACTUEL DANS LA LISTE DES INGREDIENTS COMPLETS EST SELECTIONNE DANS LE COMBOBOX
        	   	
        	Label label7 = new Label(" : ");
        	Spinner<Double> quantiteDouble  = new Spinner<>();
            SpinnerValueFactory.DoubleSpinnerValueFactory plageDeValeurs4 =  new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1,1000,i.getQuantite(),0.1);
            quantiteDouble.setValueFactory(plageDeValeurs4);
            quantiteDouble.setEditable(true);
            quantiteDouble.getEditor().setPrefWidth(70);
            
            TextFormatter<Double> restrictionEntiers4 = new TextFormatter<>(plageDeValeurs4.getConverter(), plageDeValeurs4.getValue()); 
            quantiteDouble.getEditor().setTextFormatter(restrictionEntiers4); 
            quantiteDouble.getValueFactory().setValue((double) i.getQuantite());
            
            quantiteDouble .valueProperty().addListener((obs, oldValue, newValue) -> {
           		if (newValue != null) {
	    		 {
	    			if (newValue < 0.1 || newValue> 1000) {
	    				Tools.showAlert("La quantité choisie doit être comprise entre 0.1 et 1000 !");
	    				quantiteDouble .getValueFactory().setValue(oldValue);
	    			} 
	    		 }
	    		 } else {
	    			 Tools.showAlert("La quantité ne peut être nulle !");
	    			quantiteDouble .getValueFactory().setValue(oldValue);
	    		 } 
        	});
            
        	TextField unite = new TextField(i.getUnite());
        	//REGARDE LA NOUVELLE VALEUR ET SI ELLE NE RESPECTE PAS LE REGEX ON REMET LA VALEUR PRÉCÉDENTE
        	unite.textProperty().addListener((obs, oldValue, newValue) -> {
        	    if (!newValue.matches("^[a-zA-Zà-ÿÀ-Ÿ]*(\\.[a-zA-Zà-ÿÀ-Ÿ]*){0,1}$")) {
        	        Tools.showAlert("Format saisi non adapté !");
        	        unite.setText(oldValue);
        	    }
        	});
        	
        	HBox ligneIngredient = new HBox(comboBox,label7,quantiteDouble ,unite);
        	ligneIngredient.setAlignment(Pos.CENTER);
        	listeIngredients.getChildren().addAll(ligneIngredient);
            }

// ETAPE_RECETTE  
        Label label8 = new Label("Etapes : ");
        TextArea zone8 = new TextArea(recette.getEtape_recette());
        zone8.setWrapText(true);
        zone8.setMaxWidth(500);
        zone8.setMinHeight(80);
                
        zone8.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
            	if (newValue.length() > 30000) {
            		Tools.showAlert("Le descriptif des étapes ne doit pas excéder 30000 caractères !");
    				zone8.setText(newValue.substring(0, 30000)); // LIMITATION ET CONSERVATION DU TEXTE SAISIE JUSQU'A 30000 CARACTERES
    			} 
            } else {
            	Tools.showAlert("La valeur etape_recette n'est pas renseignée !");
    			zone8.setText(oldValue);
    		}
    	});
          
// BOUTON MODIFIER RECETTE
        Button modifierRecette = new Button("Mettre à jour votre recette");
        modifierRecette.setOnAction(e -> {
        	
            // CRÉATION DE LA LISTE DE DONNÉES D'INGRÉDIENTS
            ArrayList<String[]> ingredientsData = new ArrayList<>();
            
            // PARCOURIR TOUTES LES LIGNES D'INGRÉDIENTS DANS LE VBOX LISTEINGREDIENTS
            for (Node node : listeIngredients.getChildren()) {
                if (node instanceof HBox) {
                    HBox ligne = (HBox) node;
                    // RÉCUPÈRE DANS L'ORDRE DU CONTENEUR : 0 COMBOBOX, 1 LABEL, 2 SPINNER, 3 TEXTFIELD, 4 BOUTON DE SUPPRESSION
                    ComboBox<String> comboBox = (ComboBox<String>) ligne.getChildren().get(0);
                    Spinner<Double> spinnerQuantite = (Spinner<Double>) ligne.getChildren().get(2);
                    TextField tfUnite = (TextField) ligne.getChildren().get(3);

                    String ingredientSelectionne = comboBox.getValue();
                    Double quantite = spinnerQuantite.getValue();
                    String unite = tfUnite.getText();

                    if (ingredientSelectionne != null && !ingredientSelectionne.isEmpty() && !ingredientSelectionne.toString().equals("Sélectionnez un ingrédient")
                            && quantite != null && quantite > 0) {
                        // CONVERSION EN STRING, ET STOCKAGE DANS UN TABLEAU DE STRING 2D
                        ingredientsData.add(new String[] { ingredientSelectionne, quantite.toString(), unite });
                    } else {
                    	Tools.showAlert("Veuillez remplir correctement tous les champs de vos ingrédients.");
                        return;
                    }
                }
            }
            
        	// ORDRE IDENTIQUE ENTRE LES PARAMETRES DE L'OBJET CONTROLLEUR ET L'ORDRE DES PARAMETRES DE LA METHODE APPELEE
        	new ControllerModificationRecette().miseAJourRecette(
        			recette,
        			zone1.getText(),
        			listeDifficulte.getValue(),
        			valeurNbPersonneRecette,
        			listePrix.getValue(),
        			valeurTpsPreparation,
        			valeurTpsCuisson,
        			zone8.getText(),
        			idRecette,
        			ingredientsData,
        			this.lienPhoto);
        });

        HBox etapeRecette = new HBox(label8,zone8);
        etapeRecette.setAlignment(Pos.CENTER);
        
// CONTENEUR GLOBAL VBOX CONTENANT CHAQUE ELEMENT DECRIT CI-DESSUS, ESPACE DE 10 PIXELS
	    VBox global = new VBox(10, btnretour, nomRecette, photoRecette, selecteurFichier, difficulteRecette, nbPersonneRecette, prixRecette,
	    		tempsPreparation, tempsCuisson, btnAjouter, listeIngredients, etapeRecette, modifierRecette);
	    global.setAlignment(Pos.CENTER);
	    global.getStyleClass().add("background");

// COMPOSANT PERMETTANT LE DEFILEMENT LORSQUE LE CONTENU DEPASSE LA ZONE VISIBLE
	    ScrollPane scrollPane = new ScrollPane(global);
	    scrollPane.setFitToWidth(true);
	    scrollPane.getStyleClass().add("background");
	    
	    VBox combinaison = new VBox(MainApp.getMaToolBar().getTop() ,scrollPane);
	    
// CREATION ET CONFIGURATION DE LA SCENE
	    Scene scene = new Scene(combinaison);     
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
	    MainApp.getMainStage().setTitle("Affichage modification recette");
	    MainApp.getMainStage().setScene(scene);
	    MainApp.getMainStage().setMaximized(true);
	    MainApp.getMainStage().show();
	 
	}
	
	private HBox creerLigneIngredient(VBox listeIngredients) {
	   	// COMBOBOX POUR LES INGRÉDIENTS
	    ComboBox<String> comboBox = new ComboBox<>();
	    for (Ingredient ing : MainApp.getMapIngredients().values()) {
	        comboBox.getItems().add(ing.getNomIngredient());
	    }
	    comboBox.setPromptText("Sélectionnez un ingrédient");
	    Label label7 = new Label(" : ");
	    
	    // SPINNER POUR LA QUANTITÉ
	    Spinner<Double> quantite = new Spinner<>();
	    SpinnerValueFactory.DoubleSpinnerValueFactory plageDeValeurs = 
	            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 1000, 0.1, 0.1);
	    quantite.setValueFactory(plageDeValeurs);
	    quantite.setEditable(true);
	    quantite.getEditor().setPrefWidth(70);
	    
	    TextFormatter<Double> restrictionEntiers = new TextFormatter<>(plageDeValeurs.getConverter(), plageDeValeurs.getValue()); 
	    quantite.getEditor().setTextFormatter(restrictionEntiers); 
	    quantite.getValueFactory().setValue(0.1);
	    
	    quantite.valueProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null) {
	            if (newValue < 0.1 || newValue > 1000) {
	            	Tools.showAlert("La quantité choisie doit être comprise entre 0.1 et 1000 !");
	                quantite.getValueFactory().setValue(oldValue);
	            }
	        } else {
	        	Tools.showAlert("La quantité ne peut être nulle !");
	            quantite.getValueFactory().setValue(oldValue);
	        }
	    });
	    
	    // TEXTFIELD POUR L'UNITÉ
	    TextField unite = new TextField();
	    unite.setPromptText("Unité (Ex: Grammes, centilitres...)");
	    unite.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (!newValue.matches("^[a-zA-Zà-ÿÀ-Ÿ]*(\\.[a-zA-Zà-ÿÀ-Ÿ]*){0,1}$")) {
	        	Tools.showAlert("Format unité saisi non adapté !");
	            unite.setText(oldValue);
	        }
	    });
	    
	    // BOUTON POUR SUPPRIMER LA LIGNE
	    Button btnSupprimer = new Button("✖");
	    btnSupprimer.setOnAction(e -> {
	        listeIngredients.getChildren().remove(btnSupprimer.getParent());
	    });
	    
	    // Création de l'HBox et ajout de tous les éléments
	    HBox ligneIngredient = new HBox(10, comboBox, label7, quantite, unite, btnSupprimer);
	    ligneIngredient.setAlignment(Pos.CENTER);
	    return ligneIngredient;
	}
	
}