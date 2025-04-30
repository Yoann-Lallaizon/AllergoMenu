package views;

import java.io.File;
import java.util.ArrayList;

import controllers.ControllerCreerRecette;
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


public class VueCreerRecette {

    private ArrayList<String> listeUrl;
    
	public VueCreerRecette() {
		afficherModificationRecette();
	}

	public void afficherModificationRecette() {
	    
   
// NOM_RECETTE
	    Label label1 = new Label("Nom recette : ");
	    label1.getStyleClass().add("labelSousTitre");
	    TextField nomRecetteText = new TextField();
	    nomRecetteText.setPromptText("Entrer le nom de votre recette");
	    nomRecetteText.setPrefHeight(40);	
	    nomRecetteText.setMinWidth(300);		
	    nomRecetteText.setStyle("-fx-alignment: center; -fx-text-alignment: center;-fx-text-fill: black;");
        
   	    // AJUSTEMENT TAILLE DU CHAMP EN FONCTION DU TEXTE SAISI PAR L'UTILISATEUR
	    nomRecetteText.textProperty().addListener((observable, oldValue, newValue) -> {
	    	// CALCUL DE LA LARGEUR NECESSAIRE EN FONCTION DE LA LONGUEUR DU TEXTE
            int textLength = newValue.length();
            double newWidth1 = textLength * 7 + 20;
            nomRecetteText.setPrefWidth(newWidth1); 
        });
	    	  
	    HBox nomRecette = new HBox(label1,nomRecetteText);
	    nomRecette.setAlignment(Pos.CENTER);

//IMAGE    
        ImageView photoRecette = new ImageView();
	    
// SELECTEUR DE FICHIERS
        Button boutonParcourir = new Button("Parcourir");
	    TextField nomFichier = new TextField();
	    nomFichier.setPromptText("Sélectionnez un fichier...");
	    nomFichier.setPrefWidth(100);
	    nomFichier.setDisable(true);
        // ACTION SUR LE BOUTON QUI PERMET 
        boutonParcourir.setOnAction(e -> {
        	this.listeUrl = new ControllerCreerRecette().uploadImage();
        	try {
        		String chemin = this.listeUrl.get(0);
        		File fichierImage = new File(chemin);

        		if (fichierImage.exists()) {
        			photoRecette.setImage(new Image(fichierImage.toURI().toString()));
        		} 
            } catch(Exception z) {
                System.out.println(z);
            }
        });
        
        HBox selecteurFichier = new HBox(10,nomFichier,boutonParcourir);
	    selecteurFichier.setAlignment(Pos.CENTER);
        
// CATEGORIE RECETTE
	    Label lblCatRecette = new Label("Type : ");
	    lblCatRecette.getStyleClass().add("labelText");
	    ComboBox<String> listeCatRecette = new ComboBox<>();
	    listeCatRecette.getItems().addAll("Entrée", "Plat", "Dessert");
	    listeCatRecette.getSelectionModel().select("Plat");
        HBox catRecette = new HBox(lblCatRecette,listeCatRecette);
        catRecette.setAlignment(Pos.CENTER); 
        
// DIFFICULTE_RECETTE
	    Label label2 = new Label("Difficulté : ");
	    label2.getStyleClass().add("labelText");
	    ComboBox<String> listeDifficulte = new ComboBox<>();
        listeDifficulte.getItems().addAll("Très facile","Facile","Moyen","Difficile","Très difficile");
        listeDifficulte.getSelectionModel().select("Facile");
        HBox difficulteRecette = new HBox(label2,listeDifficulte);
        difficulteRecette.setAlignment(Pos.CENTER);
                 
// NB_PERSONNE_RECETTE
        Label label3 = new Label("Nombre de personnes : ");
        label3.getStyleClass().add("labelText");
        Spinner<Integer> nbPersonneSelect = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs1 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        nbPersonneSelect.setValueFactory(plageDeValeurs1);
        nbPersonneSelect.setEditable(true);
        nbPersonneSelect.getEditor().setPrefWidth(70);

        TextFormatter<Integer> restrictionEntiers1 = new TextFormatter<>(plageDeValeurs1.getConverter(), plageDeValeurs1.getValue()); //FORMATE ET RESTREINT LES ENTREES DE TEXTE DANS LE CHAMP TEXTE
        nbPersonneSelect.getEditor().setTextFormatter(restrictionEntiers1); // APPLICATION DE CE FORMATAGE AUX ENTREES DANS LE CHAMP LISTE DEROULANTE
        
        nbPersonneSelect.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (newValue != null) {
    			if (newValue < 1 || newValue> 10) {
    				Tools.showAlert("Le nombre de personne doit être compris entre 1 et 10 !");
    				nbPersonneSelect.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le nombre de personne ne peut être nul !");
    			nbPersonneSelect.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox nbPersonneRecette = new HBox(label3,nbPersonneSelect);
        nbPersonneRecette.setAlignment(Pos.CENTER);

// PRIX_RECETTE
        Label label4 = new Label("Niveau de prix : ");
        label4.getStyleClass().add("labelText");
        ComboBox<String> listePrix = new ComboBox<>();
        listePrix.getItems().addAll("Economique","Bon marché","Abordable","Couteux","Très couteux");
        listePrix.getSelectionModel().select("Abordable");
        HBox prixRecette = new HBox(label4,listePrix);
        prixRecette.setAlignment(Pos.CENTER);

// TEMPS_PREPARATION
        Label label5 = new Label("Temps de préparation : ");
        label5.getStyleClass().add("labelText");
        Spinner<Integer> listeTmpPrepa = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs2 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1);
        listeTmpPrepa.setValueFactory(plageDeValeurs2);
        listeTmpPrepa.setEditable(true);
        listeTmpPrepa.getEditor().setPrefWidth(70);
        
        TextFormatter<Integer> restrictionEntiers2 = new TextFormatter<>(plageDeValeurs2.getConverter(), plageDeValeurs2.getValue());
        listeTmpPrepa.getEditor().setTextFormatter(restrictionEntiers2);
        
        listeTmpPrepa.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (newValue != null) {
    			if (newValue < 1 || newValue> 200) {
    				Tools.showAlert("Le temps de préparation doit être compris entre 1 et 200 !");
    				listeTmpPrepa.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le temps de préparation ne peut être nul !");
    			listeTmpPrepa.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox tempsPreparation = new HBox(label5,listeTmpPrepa);
        tempsPreparation.setAlignment(Pos.CENTER);
        
// TEMPS_CUISSON
        Label label6= new Label("Temps de cuisson : ");
        label6.getStyleClass().add("labelText");
        Spinner<Integer> listeTmpCuisson = new Spinner<>();
        SpinnerValueFactory<Integer> plageDeValeurs3 =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1);
        listeTmpCuisson.setValueFactory(plageDeValeurs3);
        listeTmpCuisson.setEditable(true);
        listeTmpCuisson.getEditor().setPrefWidth(70);
        
        TextFormatter<Integer> restrictionEntiers3 = new TextFormatter<>(plageDeValeurs3.getConverter(), plageDeValeurs3.getValue()); 
        listeTmpCuisson.getEditor().setTextFormatter(restrictionEntiers3); 
        listeTmpCuisson.getValueFactory().setValue(0);
        
        listeTmpCuisson.valueProperty().addListener((obs, oldValue, newValue) -> {
        	if (newValue != null) {
    			if (newValue < 0 || newValue> 200) {
    				Tools.showAlert("Le temps de cuisson doit être compris entre 0 et 200 !");
    				listeTmpCuisson.getValueFactory().setValue(oldValue);
    			} 
    		} else {
    			Tools.showAlert("Le temps de cuisson ne peut être nul !");
    			listeTmpCuisson.getValueFactory().setValue(oldValue);
    		}
    	});
        
        HBox tempsCuisson = new HBox(label6,listeTmpCuisson);
        tempsCuisson.setAlignment(Pos.CENTER);
 
// LISTE_INGREDIENTS_COMPLETS        
       VBox listeIngredients = new VBox(5);
       HBox premiereLigneIng = creerLigneIngredient(listeIngredients);
       listeIngredients.getChildren().add(premiereLigneIng);
       Button btnAjouter = new Button("Ajouter un ingrédient");
       btnAjouter.setOnAction(e -> {
           HBox ligneIngredient = creerLigneIngredient(listeIngredients);
           listeIngredients.getChildren().add(ligneIngredient);
       });
       
       
// ETAPE_RECETTE  
        Label label8 = new Label("Etapes : ");
        label8.getStyleClass().add("labelText");
        TextArea txtEtapeRecette = new TextArea("");
        txtEtapeRecette.setPromptText("Décrivez ici les étapes de votre recette ...");
        txtEtapeRecette.setWrapText(true);	// ACTIVE LE RETOUR A LA LIGNE AUTOMATIQUE
        txtEtapeRecette.setMaxWidth(500);
        txtEtapeRecette.setMinHeight(80);
                
        txtEtapeRecette.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
            	if (newValue.length() > 30000) {
            		Tools.showAlert("Le descriptif des étapes ne doit pas excéder 30000 caractères !");
    				txtEtapeRecette.setText(newValue.substring(0, 30000)); // LIMITATION ET CONSERVATION DU TEXTE SAISIE JUSQU'A 30000 CARACTERES
    			} 
            } else {
            	Tools.showAlert("La valeur etape_recette est NULL !");
    			txtEtapeRecette.setText(oldValue);
    		}
    	});
          
// BOUTON Créer Recette
        Button creerRecette = new Button("Créer la recette");
        creerRecette.setOnAction(e -> {

        	ArrayList<String[]> ingredientsData = new ArrayList<>();

    	    // Parcourir toutes les lignes d'ingrédients dans le VBox listeIngredients
    	    for (Node node : listeIngredients.getChildren()) {
    	        if (node instanceof HBox) {
    	            HBox ligne = (HBox) node;
    	            
    	            ComboBox<String> comboBox = (ComboBox<String>) ligne.getChildren().get(0);
    	            Spinner<Double> spinnerQuantite = (Spinner<Double>) ligne.getChildren().get(2);
    	            TextField tfUnite = (TextField) ligne.getChildren().get(3);
    	            
    	            String ingredientSelectionne = comboBox.getValue();
    	            Double quantite = spinnerQuantite.getValue();
    	            String unite = tfUnite.getText();
    	            
    	            if (ingredientSelectionne != null && !ingredientSelectionne.isEmpty() && !ingredientSelectionne.toString().equals("Sélectionnez un ingrédient")
    	                    && quantite != null && quantite > 0) {
    	                // Conversion de la quantité en String
    	                String quantiteStr = quantite.toString();
    	                // Stocker les 3 infos dans un tableau de String
    	                ingredientsData.add(new String[] { ingredientSelectionne, quantiteStr, unite });
    	            } else {
    	            	Tools.showAlert("Veuillez remplir correctement tous les champs de vos ingrédients.");
    	                return;
    	            }
    	        }
            }
            
    	    Tools.showAlert(
	    		new ControllerCreerRecette().creerRecette(nomRecetteText.getText(),listeDifficulte.getValue(),
	    				nbPersonneSelect.getValue(), listePrix.getValue(), listeTmpPrepa.getValue(), listeTmpCuisson.getValue(),
	    				txtEtapeRecette.getText(), listeUrl, listeCatRecette.getValue(), ingredientsData)
	    		);
        });

        HBox etapeRecette = new HBox(label8,txtEtapeRecette);        
        etapeRecette.setAlignment(Pos.CENTER);
        
// CONTENEUR GLOBAL VBOX CONTENANT CHAQUE ELEMENT DECRIT CI-DESSUS, ESPACE DE 10 PIXELS
	    VBox global = new VBox(20,nomRecette, photoRecette, nomFichier,selecteurFichier,catRecette,difficulteRecette,nbPersonneRecette,prixRecette,
	    		tempsPreparation,tempsCuisson,btnAjouter, listeIngredients, etapeRecette, creerRecette);
	    global.setAlignment(Pos.CENTER);
	    global.getStyleClass().add("background");
	    VBox combinaison = new VBox(MainApp.getMaToolBar().getTop(), global);
	    combinaison.getStyleClass().add("background");
// COMPOSANT PERMETTANT LE DEFILEMENT LORSQUE LE CONTENU DEPASSE LA ZONE VISIBLE
	    ScrollPane scrollPane = new ScrollPane(combinaison);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(true);
	    
	    
// CREATION ET CONFIGURATION DE LA SCENE
	    Scene scene = new Scene(scrollPane);     
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
// CONFIGURATION DE LA SCENE PRINCIPALE
	    MainApp.getMainStage().setTitle("Créer une recette");
	    MainApp.getMainStage().setScene(scene);
	    MainApp.getMainStage().setMaximized(true);
	    MainApp.getMainStage().show();
	 
	}
	
	private HBox creerLigneIngredient(VBox listeIngredients) {
	   	// ComboBox pour les ingrédients
	    ComboBox<String> comboBox = new ComboBox<>();
	    for (Ingredient ing : MainApp.getMapIngredients().values()) {
	        comboBox.getItems().add(ing.getNomIngredient());
	    }
	    comboBox.setPromptText("Sélectionnez un ingrédient");
	    Label label7 = new Label(" : ");
	    
	    // Spinner pour la quantité
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
	    
	    // TextField pour l'unité
	    TextField unite = new TextField();
	    unite.setPromptText("Unité (Ex: Grammes, centilitres...)");
	    unite.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (!newValue.matches("^[a-zA-Zà-ÿÀ-Ÿ]*(\\.[a-zA-Zà-ÿÀ-Ÿ]*){0,1}$")) {
	        	Tools.showAlert("Format unité saisi non adapté !");
	            unite.setText(oldValue);
	        }
	    });
	    
	    // Bouton pour supprimer la ligne
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