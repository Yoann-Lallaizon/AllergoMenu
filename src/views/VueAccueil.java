package views;

import java.util.ArrayList;

import controllers.ControllerAccueil;
import controllers.ControllerToolBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.MainApp;
import models.Maladie;
import models.Recette;

public class VueAccueil {
	
	//ATTRIBUTS
	private TextField searchBar;
    private javafx.scene.control.ListView<Recette> resultsList;
	
	//CONSTRUCTEUR
	public VueAccueil() {
		this.afficher();
	}
	
	//AUTRES METHODES
	//Affiche la page d'accueil
	public void afficher() {
		//Bouton pour accéder à la page de générateur de menu. On pourrait mettre une image aussi.
		Label propalmenu=new Label ("Besoin de repas pour toute la semaine ? Laissez nous faire !");
		propalmenu.getStyleClass().add("labelTitre");
		propalmenu.setAlignment(Pos.CENTER);

		Button goMenu=new Button ("Générateur de menu");

		// Créez le titre
		Label titre1 = new Label("AllergoMenu");
		titre1.getStyleClass().add("labelTitre");
		titre1.setAlignment(Pos.CENTER);
		
		Label lblSousTitre =new Label("Bonjour "+MainApp.getUtilisateurActif().getPrenom_utilisateur()+ " !");
		lblSousTitre.getStyleClass().add("labelSousTitre");
		lblSousTitre.setAlignment(Pos.CENTER);
		
//SECTION DE RECHERCHE
		VBox searchComponent = barreDeRecherche(); 
		
		//SECTION FILTRAGE
		VBox filtreSection = new VBox(5);
		filtreSection.setPadding(new Insets(10));
		filtreSection.setAlignment(Pos.CENTER);
		Label filtreTitre = new Label("Filtrer par maladies :");
		filtreSection.getChildren().add(filtreTitre);
		VBox checkBoxContainer = new VBox(5);
		checkBoxContainer.setAlignment(Pos.CENTER);
		
		//INSERE UNE CHECKBOX POUR CHAQUE MALADIE
		for (Object value : MainApp.getMapAllMaladies().values()) {
		    models.Maladie maladie = (models.Maladie) value;
		    CheckBox cb = new CheckBox(maladie.getNom_Maladie());
		    for (models.Maladie m : MainApp.getUtilisateurActif().getListe_maladie_utilisateur()) {
		        if (m.getNom_Maladie().equalsIgnoreCase(maladie.getNom_Maladie())) {
		            cb.setSelected(true);
		            break;
		        }
		    }
		    checkBoxContainer.getChildren().add(cb);
		}
		filtreSection.getChildren().add(checkBoxContainer);
		
		//BOUTONS ACTIONS FILTRAGE
		Button btnAppliquerFiltres = new Button("Appliquer les filtres");
		btnAppliquerFiltres.setOnAction(e -> {
		    ArrayList<Maladie> selectedMaladies = new ArrayList<>();
		    checkBoxContainer.getChildren().stream()
		        .filter(node -> node instanceof CheckBox)
		        .forEach(node -> {
		            CheckBox cb = (CheckBox) node;
		            if (cb.isSelected()) {
		                for (models.Maladie m : MainApp.getMapAllMaladies().values()) {
		                    if (m.getNom_Maladie().equalsIgnoreCase(cb.getText())) {
		                        selectedMaladies.add(m);
		                        break;
		                    }
		                }
		            }
		        });
		    ControllerToolBar.applyFiltrage(selectedMaladies);
		    refreshRecherche();
		});

		Button btnResetFiltrage = new Button("Réinitialiser les filtres");
		btnResetFiltrage.setOnAction(e -> {
			// Cocher les CheckBox correspondant aux maladies (ici Recette) de l'utilisateur
		    ArrayList<Maladie> listeRecettes = MainApp.getUtilisateurActif().getListe_maladie_utilisateur();
		    for (Maladie maladie : listeRecettes) {
		        for (Node node : checkBoxContainer.getChildren()) {
		            if (node instanceof CheckBox) {
		                CheckBox checkBox = (CheckBox) node;
		                // On suppose que Recette a une méthode getNom() qui donne le nom de la recette
		                if (checkBox.getText().equals(maladie.getNom_Maladie())) {
		                    checkBox.setSelected(true);
		                }
		            }
		        }
		    }
		    ControllerToolBar.resetFiltrage();
		    refreshRecherche();
		});
		filtreSection.getChildren().addAll(btnAppliquerFiltres, btnResetFiltrage);
		
		Button btnFiltrer = new Button("Filtrer");
		VBox vBoxBtnFiltrer = new VBox(btnFiltrer);
		vBoxBtnFiltrer.setAlignment(Pos.CENTER);
		
		HBox recherche = new HBox(20, searchComponent, vBoxBtnFiltrer, filtreSection);
		recherche.setMaxWidth(Double.MAX_VALUE);
		recherche.setAlignment(Pos.CENTER);
		
		filtreSection.setVisible(false);

//AFFICHAGE DES MINIATURES
		ArrayList <VBox> miniatures = new ArrayList<>();
		
		// Ajout de 3 VBox à la liste miniatures
		for (int i = 0; i < 3; i++) {
		    miniatures.add(new VBox());
		}
		ArrayList<Button> voirs = new ArrayList<Button>();
		
		for (int i=0;i<3;i++){
		    VBox boucleVBox = miniatures.get(i);
		    boucleVBox.getStyleClass().add("card-recette");
		    miniatures.get(i).setAlignment(Pos.CENTER);
		    miniatures.get(i).setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		    miniatures.get(i).setAlignment(Pos.CENTER);
			//miniatures.get(i).setMinSize(250, 350);
			//miniatures.get(i).setMaxSize(250, 350);
		    Label titre = new Label(ControllerToolBar.getPickThree().get(i).getNom_recette());
			miniatures.get(i).getChildren().add(titre);
			
			
			try {
				Image image = new Image("/images/"+ControllerToolBar.getPickThree().get(i).getPhoto_recette());
		        ImageView imageView = new ImageView(image);
		        imageView.setFitWidth(200);
		        imageView.setPreserveRatio(true);
				miniatures.get(i).getChildren().add(imageView);

			}
			catch(Exception e) {
			}
			
			// AJOUT D'UN BOUTON DANS LA VBOX
		    Button btnVoirPlus = new Button("Voir plus");
		    boucleVBox.getChildren().add(btnVoirPlus);
		    voirs.add(btnVoirPlus);
			boucleVBox.setAlignment(Pos.TOP_CENTER);
		}
		
		
		voirs.get(0).setOnAction (x -> {
			new ControllerAccueil(ControllerToolBar.getPickThree().get(0)).afficherVueConsultationRecette(); //va chercher la recette correspondate dans la liste de trois recettes et ouvre la vue recette.
		});
		
		voirs.get(1).setOnAction (x -> {
			new ControllerAccueil(ControllerToolBar.getPickThree().get(1)).afficherVueConsultationRecette();
		});
		
		voirs.get(2).setOnAction (x -> {
			new ControllerAccueil(ControllerToolBar.getPickThree().get(2)).afficherVueConsultationRecette();
		});
	        
	        
//GESTION DES EVENEMENTS	
		goMenu.setOnAction (e -> {
			new ControllerAccueil(null).afficherVueGenererMenu();
		});

		btnFiltrer.setOnAction(e -> {
			filtreSection.setVisible(!filtreSection.isVisible());
		});
				
		HBox boxRecettes=new HBox(miniatures.get(0),miniatures.get(1), miniatures.get(2));
		boxRecettes.setAlignment(Pos.CENTER);
		Region espace = new Region();
		espace.setPrefHeight(50);
		VBox combinaison = new VBox(40, titre1, lblSousTitre, recherche, propalmenu, goMenu, boxRecettes,espace); // Les deux VBox seront empilées verticalement
		combinaison.setAlignment(Pos.CENTER);
		combinaison.getStyleClass().add("background");
		ScrollPane scrollpage = new ScrollPane();
		scrollpage.setContent(combinaison);	
		scrollpage.setFitToWidth(true); 
		scrollpage.setFitToHeight(true);
		scrollpage.setPannable(true);
		
		
		VBox page = new VBox(MainApp.getMaToolBar().getTop(), scrollpage);
		
//INSTANCIATION DE LA SCENE
		Scene scene=new Scene (page);
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }

//CREATION DE LA FENETRE DE L'APPLICATION
		MainApp.getMainStage().setScene(scene);
		MainApp.getMainStage().setTitle("Page d'accueil");
		MainApp.getMainStage().setMaximized(true);
		MainApp.getMainStage().setResizable(true);
		MainApp.getMainStage().centerOnScreen();
		MainApp.getMainStage().show();	 		 
		refreshRecherche();
	}

//RETOURNE LA BARRE DE RECHERCHE
	private VBox barreDeRecherche() {
		// Champ de recherche
	    searchBar = new TextField();
	    searchBar.setPromptText("Rechercher une recette ou un ingrédient...");
	    searchBar.setMaxWidth(Double.MAX_VALUE);
	    searchBar.setPrefWidth(400);

	    // ListView pour afficher les résultats
	    resultsList = new javafx.scene.control.ListView<>();
	    resultsList.setFixedCellSize(38);
	    resultsList.setMaxWidth(Double.MAX_VALUE);
	    resultsList.setMaxHeight(150);
	    resultsList.setMinHeight(150);
	    resultsList.setCellFactory(lv -> {
	        javafx.scene.control.ListCell<Recette> cell = new javafx.scene.control.ListCell<>() {
	        	//CRÉE UNE NOUVELLE CELLULE POUR CHAQUE ELEMENT
	            @Override
	            protected void updateItem(Recette rec, boolean empty) {
	                super.updateItem(rec, empty);
	                if (empty || rec == null) {
	                    setText(null);
	                } else {
	                    setText(rec.getNom_recette());
	                }
	            }
	        };
	        //REND CHAQUE CELLULE CLIQUABLE
	        cell.setOnMouseClicked(event -> {
	            if (!cell.isEmpty() && event.getClickCount() == 1) {
	                Recette rec = cell.getItem();
	                new ControllerAccueil(rec).afficherVueConsultationRecette();
	            }
	        });
	        return cell;
	    });

	    // ÉCOUTEUR D'ÉVÉNEMENTS SUR LA BARRE DE RECHERCHE DÉCLENCHE LA MÉTHODE DE RECHERCHE
	    searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
	    	rechercher(newValue);
	    });
	    
	    //COMBINAISON DE LA BARRE DE RECHERCHE ET DE LA RESULTLIST DANS UNE VBOX
	    VBox searchComponent = new VBox(5, searchBar, resultsList);
	    searchComponent.setPadding(new Insets(10));
	    searchComponent.setMaxWidth(Double.MAX_VALUE);
	    return searchComponent;
	}
	
//METHODE DE RECHERCHE
	private void rechercher(String query) {
	// CRÉATION D'UNE TÂCHE ASYNCHRONE POUR LA RECHERCHE
		//CRÉATION DE LA TASK POUR LE TRAITEMENT EN ARRIÈRE PLAN QUI SURCHARGE CALL()
	    javafx.concurrent.Task<ArrayList<Recette>> searchTask = new javafx.concurrent.Task<>() {
	        @Override
	        protected ArrayList<Recette> call() throws Exception {
	            // APPEL À LA MÉTHODE DU CONTRÔLEUR QUI EFFECTUE LE FILTRAGE
	            return ControllerToolBar.rechercherRecettes(query);
	        }
	    };
	    //SI LA TÂCHE EST EXÉCUTÉE AVEC SUCCÈS ON AJOUTE LE RÉSULTAT AU RESULLIST
	    searchTask.setOnSucceeded(e -> {
	        ArrayList<Recette> found = searchTask.getValue();
	        System.out.println("Nombre de résultats trouvés : " + found.size());
	        resultsList.getItems().setAll(found);
	        resultsList.requestLayout();
	    });
	    //EFFECTUE LA RECHERCHE SUR UN AUTRE THREAD
	    new Thread(searchTask).start();
	}
	
//METHODE POUR RAFRAICHIR LA RECHERCHE
	private void refreshRecherche() {
		if (searchBar != null) {
			String currentSearch = searchBar.getText();
			rechercher(currentSearch);
		}
	}
}