package views;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.MainApp;
import models.Maladie;
import models.Recette;
import models.RecetteIngredient;

public class VueAfficherMenu {
	
	// CONSTRUCTEUR
	public VueAfficherMenu(ArrayList<ArrayList<Recette>> totalMenu,int nbJours,int nbMenu,int nbMenuJours, ArrayList<Maladie> restrictionMaladieList) {
		
		//CREATION DE LA GRILLE PRINCIPALE
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(10);
		root.setAlignment(Pos.TOP_CENTER);
		
		BorderPane borderPane = new BorderPane();
		
		//CREATION DU HAUT DE LA FENETRE
		VBox top = new VBox(5);
		top.setAlignment(Pos.CENTER);
		Label labelTitre = new Label("Menu pour "+ nbJours +" jours");
		labelTitre.getStyleClass().add("labelTitre");
		HBox recap = new HBox(2);
		recap.setAlignment(Pos.CENTER);
		recap.setPadding(new Insets(5, 10, 5, 10));
		Label recapitulatifMenu = new Label(" Menu prévu avec " + nbMenuJours + " repas par jour " );
		recapitulatifMenu.getStyleClass().add("labelSousTitre");
		recap.getChildren().add(recapitulatifMenu);
		
		/*for (Maladie m :restrictionMaladieList) {
			Label nomMaladie = new Label(m.getNom_Maladie());
			recap.getChildren().addAll(new Label(" "), nomMaladie); 
		}*/
		top.getChildren().add(new VBox(MainApp.getMaToolBar().getTop()));
		top.getChildren().addAll(labelTitre, recap);
		
		//CREATION DU HAUT DE LA FENETRE
		HBox bottom = new HBox();
		bottom.setPadding(new Insets(10));
		bottom.setAlignment(Pos.TOP_LEFT);
		bottom.setPrefWidth(Double.MAX_VALUE);
		
		top.getStyleClass().add("background");
		root.getStyleClass().add("background");
		bottom.getStyleClass().add("background");
		borderPane.setTop(top);
		//borderPane.setCenter(root);
		borderPane.setBottom(bottom);
		
		BackgroundFill background_fill = new BackgroundFill(Color.web("#CCE5A8"), CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(background_fill);
		
		int colonne = 0;
		int ligne = 0;
		int nbligne = 2;
		
		// CREATION DE LA SCROLLPANE POUR AFFICHER LES MENUS
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setPannable(true); 
		scrollPane.setContent(root);
		scrollPane.getStyleClass().add("background");
		borderPane.setCenter(scrollPane);
		
		// CREATION DE LA SCROLLPANE POUR AFFICHER LES RECETTES
		ScrollPane sp = new ScrollPane();
		sp.setFitToWidth(true); 
        sp.setFitToHeight(true);
		sp.setPannable(true); 
		sp.setContent(bottom);
		sp.getStyleClass().add("background");
		borderPane.setBottom(sp);
		borderPane.getStyleClass().add("background");
		// BOUCLE FOR POUR AFFICHER LES MENUS
		for (int i = 0; i < nbMenu ; i++) {
			for (int j = 0; j < totalMenu.get(i).size() ; j++) {
				System.out.println("vueAff totalMenu get i : " + i +  " get j : " + j + totalMenu.get(i).get(j).getNom_recette());
			}
			
			VBox vbMenu = new VBox(5);
			vbMenu.setPadding(new Insets(10));
			vbMenu.setBackground(background);
			vbMenu.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
			vbMenu.setPrefWidth(300);
			vbMenu.setPrefHeight(175);
			HBox enTeteMenu = new HBox(20);
			
			Label numeroMenu = new Label("Menu "+ (i+1));
			numeroMenu.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
			enTeteMenu.getChildren().add(numeroMenu);
			vbMenu.getChildren().add(enTeteMenu);	
			
			// AFFICHAGE DEJEUNER/DINER SI 2 REPAS PAR J 
			if (nbMenuJours == 2) {
				if (i % 2 == 0) {
					Label dej = new Label("Déjeuner ");
					enTeteMenu.getChildren().add(dej);
				}else {
					Label diner = new Label("Dinner ");
					enTeteMenu.getChildren().add(diner);
				}
			}
			
			//BOUCLE FOR POUR AFFICHAGE LES RECETTES GENERE
			for (Recette r : totalMenu.get(i)) {
				if (r.getNom_categorie_recette().equals("Entrée")) {
					Label labelE = new Label("Entrée");
					labelE.setStyle("-fx-font-size: 17px;");
					vbMenu.getChildren().add(labelE);	
				}
				if (r.getNom_categorie_recette().equals("Plat")) {
					Label labelP = new Label("Plat");
					labelP.setStyle("-fx-font-size: 17px;");
					vbMenu.getChildren().add(labelP);	
				}
				if (r.getNom_categorie_recette().equals("Dessert")) {
					Label labelD = new Label("Dessert");
					labelD.setStyle("-fx-font-size: 17px;");
					vbMenu.getChildren().add(labelD);	
				}
				//HYPERLIEN POUR OUVRIR LA VBOX D'AFFICHAGE DES RECETTES ET GESTIONS DES EVENEMENTS 
				Hyperlink link = new Hyperlink();
				String nomRecette = new String(r.getNom_recette());
				link.setText(nomRecette);
				link.setStyle("-fx-font-weight: bold;");
				link.setOnAction( event -> {
				    System.out.println("This link is clicked");
				    VBox contenu = AfficherRecettebox(r); // Récupérer la recette
				    sp.setContent(contenu);
				    //bottom.getChildren().clear();
				    //bottom.getChildren().add(AfficherRecetteHbox(r));
				});
				
				Label diff = new Label("Difficulté : " + r.getDifficulte_recette());
				Label tpsTotal = new Label ("Temps total de prépatation : " + r.getTemps_total_recette() + " min");
				Label prix_recette = new Label("Prix : " + r.getPrix_recette());
				
				//VBox.getVgrow(nomRecette);
				vbMenu.getChildren().addAll(link,tpsTotal,diff,prix_recette);
			}

			// AJOUT DU MENU DANS LA GRILLE
			root.add(vbMenu, colonne, ligne);
			
			// GERE LA DISPOSITION DANS LA GRILLE
			ligne++;
			
			if (ligne >= nbligne) {
				ligne = 0;
				colonne++;
			}
			
			
		}	
		//CREATION DE LA SCENE 
	    Scene scene = new Scene(borderPane);
	    scene.setFill(Color.web("#ece5db"));
	    try {
	    	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
	    } catch (NullPointerException e) {
	        System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
	    }
	    MainApp.getMainStage().setScene(scene);
	    MainApp.getMainStage().setTitle("Affichage du menu");
	    MainApp.getMainStage().setMaximized(true);
	    MainApp.getMainStage().setResizable(true);
	    MainApp.getMainStage().centerOnScreen();
	    MainApp.getMainStage().show();	
		
	}
	
	// AFFICHAGE D'UNE RECETTE DANS LE BOTTOM DE LA GRILLE
	public VBox AfficherRecettebox(Recette r) {
		
		VBox afficherRecette = new VBox();
		afficherRecette.setPrefHeight(400);
		afficherRecette.setSpacing(10);
		afficherRecette.setPadding(new Insets(10));
		
		VBox titre = new VBox();
		Label nomCatRecette = new Label(r.getNom_categorie_recette());
		nomCatRecette.setWrapText(true);
		nomCatRecette.getStyleClass().add("labelSousTitre");
		
		Label nomRecette = new Label(r.getNom_recette());
		nomRecette.setWrapText(true);
		nomRecette.getStyleClass().add("labelTitre");
		titre.getChildren().addAll(nomRecette,nomCatRecette);
		
		Label persRecette = new Label("Recette pour "+r.getNb_personne_recette()+" personnes");
		persRecette.setWrapText(true);
		persRecette.getStyleClass().add("labelSousTitre");
		titre.getChildren().add(persRecette);
		
		try {
			Image image1 = new Image("/images/"+ r.getPhoto_recette());       
        	ImageView photoRecette = new ImageView(image1);
    		photoRecette.setFitWidth(250);
        	photoRecette.setFitHeight(200); 
        	titre.getChildren().add(photoRecette);
        	
		}catch(Exception e){e.printStackTrace();}
		
		VBox info = new VBox(5);
		Label tpsPrepa = new Label("Temps de préparation :  "+ r.getTemps_preparation() + " mn");
		tpsPrepa.getStyleClass().add("labelText");
		tpsPrepa.setWrapText(true);
		
		Label tpsCuisson = new Label("Temps de cuisson :  "+ r.getTemps_cuisson() + " mn");
		tpsCuisson.getStyleClass().add("labelText");
		tpsCuisson.setWrapText(true);
		
		Label diffRecette = new Label("difficulté : " + r.getDifficulte_recette());
		diffRecette.getStyleClass().add("labelText");
		diffRecette.setWrapText(true);
		
		Label prixRecette = new Label("Prix : " + r.getPrix_recette());
		prixRecette.getStyleClass().add("labelText");
		prixRecette.setWrapText(true);
		info.getChildren().addAll(tpsPrepa,tpsCuisson,diffRecette,prixRecette);
		
		
		
		VBox ingredient = new VBox();
		for ( RecetteIngredient ri : r.getListeIngredientsComplets() ) {
			ingredient.getChildren().add(new Label("- "+ ri +""));
		}
		
		TextArea etapeRecette = new TextArea(r.getEtape_recette());
		etapeRecette.setWrapText(true);
		etapeRecette.setEditable(false);
		etapeRecette.setPrefRowCount(7);
		etapeRecette.setMinSize(400, 200);  // Taille minimale
		etapeRecette.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		//etapeRecette.setPrefHeight(200);
		
		
		VBox.setVgrow(etapeRecette, Priority.ALWAYS);
		afficherRecette.getChildren().addAll(titre,info,ingredient,etapeRecette);
		afficherRecette.getStyleClass().add("background");
		return afficherRecette;
	}
}


