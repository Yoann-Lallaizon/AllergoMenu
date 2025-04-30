package views;

import java.util.ArrayList;

import controllers.Tools;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.MainApp;
import models.Recette;

public class VueListeRecette {
	private ArrayList<Recette> listeRecettes;
	private int choix;
	
	public VueListeRecette(ArrayList<Recette> listeRecettes, int y) {
		this.listeRecettes = listeRecettes;
		this.choix=y;
		System.out.println(this.listeRecettes);
		afficher();
	}
	
	private void afficher() {
		if(listeRecettes.isEmpty()) {
			String labelContent = new String();
		    Button btnCreerRecette = new Button("Créer une recette");
			if (choix==1) {
				labelContent="Créez une recette dès maintenant en cliquant sur créer une recette !";
			}
			
			if (choix==0) {
				labelContent="Vous n'avez pas encore de favoris !";
				btnCreerRecette.setVisible(false);
			}
			
				Label titreNoRecette = new Label(labelContent);
				VBox root = new VBox(10, MainApp.getMaToolBar().getTop(), titreNoRecette, btnCreerRecette);
			    root.setAlignment(Pos.TOP_CENTER);
			    root.getStyleClass().add("background");
			    
			    ScrollPane scrollPage = new ScrollPane(root);
			    scrollPage.getStyleClass().add("background");
			    scrollPage.setFitToWidth(true);
			    scrollPage.setFitToHeight(true);
			    scrollPage.setPannable(true);
			    // Création et affichage de la scène
			    Scene scene = new Scene(scrollPage, 1920, 1080);
			    try {
		        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	
		        	System.out.println("Style OK ");
		        } catch (NullPointerException e) {
		            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
		        }
			    MainApp.getMainStage().setScene(scene);
			    MainApp.getMainStage().setTitle("Mes Recettes");
			    MainApp.getMainStage().setMaximized(true);
			    MainApp.getMainStage().setResizable(true);
			    MainApp.getMainStage().show();	
			    
			    btnCreerRecette.setOnAction(event -> {
			      	new VueCreerRecette();
			    });
		}else {
				GridPane grid = new GridPane();
				grid.setHgap(3);
				grid.setVgap(11);
				

				for (int i = 0; i < listeRecettes.size(); i++) {
			        Recette recette = listeRecettes.get(i);
			        			        
			        // Création d'un GridPane pour la miniature de la recette
			        GridPane miniature = new GridPane();
			        miniature.getStyleClass().add("card-recette");
			        miniature.setHgap(5);
			        miniature.setVgap(5);
			        miniature.setAlignment(Pos.CENTER);
			        miniature.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			        miniature.setAlignment(Pos.CENTER);
			        // Création d'un label avec le nom de la recette
			        Label titreRecette = new Label(recette.getNom_recette());
			        miniature.add(titreRecette, 0, 0);
			        
			        if(!recette.getStatut_moderation_recette()) {
			        	Tooltip tooltip = new Tooltip("Recette en attente de modération, non visible.");
			            titreRecette.setTooltip(tooltip);
			            titreRecette.setTextFill(Color.RED);
			        }
			        // Création de l'image
			        try {
			            Image image = new Image("/images/" + recette.getPhoto_recette());
			            ImageView imageView = new ImageView(image);
			            imageView.setFitWidth(200);
			            imageView.setPreserveRatio(true);
			            miniature.add(imageView, 0, 1);
			        } catch(Exception e) {
			            System.out.println("Erreur lors du chargement de l'image pour la recette : " + recette.getNom_recette());
			        }
			        
			        // Création du bouton "Voir plus"
			        Button btnVoirPlus = new Button("Voir plus");
			        btnVoirPlus.setOnAction(event -> {
			        	new VueConsultationRecette(recette, Tools.chargerCommentaire(recette));
			        });
			        VBox boutonContainer = new VBox(btnVoirPlus);
			        boutonContainer.setAlignment(Pos.CENTER);
			        miniature.add(boutonContainer, 0, 2);
			        
			        // Ajout de la miniature dans la grille
			        grid.add(miniature, i % 3, i / 3);
			    }
				
				grid.setAlignment(Pos.CENTER);			    
			    ScrollPane scrollPage = new ScrollPane(grid);
			    grid.getStyleClass().add("background");
			    
			    scrollPage.setFitToWidth(true);
			    scrollPage.setPannable(true);
			    scrollPage.getStyleClass().add("background");
			    VBox combinaison = new VBox(MainApp.getMaToolBar().getTop(), scrollPage);
			    combinaison.getStyleClass().add("background");
			    // Création et affichage de la scène
			    Scene scene = new Scene(combinaison);
				try {
		        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	
		        	System.out.println("Style OK ");
		        } catch (NullPointerException e) {
		            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
		        }
				MainApp.getMainStage().setScene(scene);
			    MainApp.getMainStage().setTitle("Mes Recettes");
			    MainApp.getMainStage().setMaximized(true);
			    MainApp.getMainStage().setResizable(true);
			    MainApp.getMainStage().show();	 
		}
		
	}

}
