package views;

import java.util.ArrayList;
import java.util.Map;

import controllers.ControllerGenererMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import main.MainApp;
import models.Maladie;
	
public class VueGenererMenu {

// CONSTRUCTEUR
	public VueGenererMenu() {
    	afficherGenererMenu();
    }
	
	public void afficherGenererMenu() {
		
        /*VBox permettant au user de choisir le nombre de repas :  
		 - ComboBox nbReapsCBox pour savoir sur combien de jour plannifier le menu 
		 - ComboBox NbPersCBox pour choisir le nombre de personne (si besoin pour la liste de course générer à partir du menu)
		 - 2 RadiButton pour choisir si il faut prévoir seulement 1 repas par jour ou 2 
		 */
        VBox nbRepas = new VBox(5);
        Label titre = new Label("Générateur de menu");
        titre.setAlignment(Pos.CENTER);
        titre.getStyleClass().add("labelTitre");
        Label labelnbRepas = new Label("Nombres de jours à planifier : ");
        labelnbRepas.getStyleClass().add("labelText");
        ComboBox<Integer> nbRepasCBox  = new ComboBox<>();
        nbRepasCBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7);
        nbRepasCBox.setValue(1);
        
        Label labelRepas = new Label("Nombre de repas à planifier par jour : ");
        labelRepas.getStyleClass().add("labelText");
        
        ToggleGroup choixNbRepasGroup = new ToggleGroup();
        RadioButton repas1RB = new RadioButton("Un repas");
        RadioButton repas2RB = new RadioButton("Deux repas");
        repas1RB.setToggleGroup(choixNbRepasGroup);
        repas1RB.setSelected(true);
        repas2RB.setToggleGroup(choixNbRepasGroup);
        
        nbRepas.getChildren().addAll(titre,labelnbRepas,nbRepasCBox,labelRepas,repas1RB,repas2RB);
        
        /* Vbox pour choisir le nombre de plats à plannif 
         * CheckBox pour chosis entrée,plat,dessert
           et la restriction alimentaire à appliquer */
        VBox categoriePlat = new VBox(5);
        
        Label labelcategoriePlat = new Label("Plats :");
        labelcategoriePlat.getStyleClass().add("labelText");
        
        CheckBox entreeCB = new CheckBox("Entrée");
        CheckBox platCB = new CheckBox("Plat");
        CheckBox dessertCB = new CheckBox("Dessert");
        
        categoriePlat.getChildren().addAll(labelcategoriePlat,entreeCB, platCB,dessertCB);
        
        
        /*  */
        VBox restrictionMaladie = new VBox();
        
        Label labelRestriction = new Label("Choisir la restriction à appliquer pour tout le menu : ");
        labelRestriction.getStyleClass().add("labelText");
        
        restrictionMaladie.getChildren().add(labelRestriction);
       
       
        Map<Integer, Maladie> allMaladie = MainApp.getMapAllMaladies();
        ArrayList<CheckBox> listMaladieCB = new ArrayList<CheckBox>();
        
        for (Maladie maladie : allMaladie.values()) {
            CheckBox checkBox = new CheckBox(maladie.getNom_Maladie()); 
            restrictionMaladie.getChildren().add(checkBox);
            listMaladieCB.add(checkBox);
        }
        
        VBox validation = new VBox(5);
        Button buttonValider = new Button("Valider");
        
     // Observation de tous les événements au moment de cliquer sur le bouton valider
        buttonValider.setOnAction(e-> {
        	int nbJours = nbRepasCBox.getValue();
        	int nbRepasJours;
        	boolean platEntree;
        	boolean platPlat;
        	boolean platDessert;
        	ArrayList<String> restrictionMaladieList = new ArrayList<>();
        	
        	if (repas1RB.isSelected()) {
        		nbRepasJours = 1;
        	} else {
        		nbRepasJours = 2;
        	}
        	        	
        	if (!entreeCB.isSelected() && !platCB.isSelected() && !dessertCB.isSelected()) {
        		Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection requise");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner au moins un plat.");
                alert.showAndWait();
                //alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                //alert.getDialogPane().getStyleClass().add("custom-alert");
                return;
        	}
        	        
        	if (entreeCB.isSelected()) {
           		platEntree = true;
        	} else {
           		platEntree = false;
        	}
           	
        	if (platCB.isSelected()) {
        		platPlat = true;
        	} else {
        		platPlat = false;
        	}
           	
        	if (dessertCB.isSelected()) {
           		platDessert = true;
        	} else {
           		 platDessert = false;
        	}
           	
    		
        	for (CheckBox cb : listMaladieCB) {
        			if (cb.isSelected()) {
        				restrictionMaladieList.add(cb.getText());
        			}
        		}
        		
        	// récupération des objets Maladie en fonction des checkbox selectionné par l'utilisateur pour pouvoir utiliser la méthode généreerMenu  
        	ArrayList<Maladie> restrictionMaladieObjects = new ArrayList<>();
        	
        	for (String nomMaladie : restrictionMaladieList ) {
        		for (Maladie maladie : allMaladie.values()) {
        			if (maladie.getNom_Maladie().equals(nomMaladie)) {
        				restrictionMaladieObjects.add(maladie);
        				System.out.println("la maladie récuppéré : "+ maladie);
        			}
        		}
        	}
        	
        	new ControllerGenererMenu().genererMenu(nbJours,nbRepasJours,platEntree,platPlat,platDessert,restrictionMaladieObjects);
        	 	
        });
        
        validation.getChildren().addAll(buttonValider);
        
		nbRepas.setAlignment(Pos.CENTER);
		categoriePlat.setAlignment(Pos.CENTER);
		restrictionMaladie.setAlignment(Pos.CENTER);
		validation.setAlignment(Pos.CENTER);
		
        VBox root = new VBox(10); // Conteneur principal avec espacement
        root.getChildren().addAll(MainApp.getMaToolBar().getTop(), nbRepas, categoriePlat, restrictionMaladie, validation); 
        root.getStyleClass().add("background");
        root.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(root, 500, 500); // Associe root à la scène
		
        try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
        
        MainApp.getMainStage().setScene(scene);
        MainApp.getMainStage().setTitle("Générer un menu");
        MainApp.getMainStage().show();        
	}
}
	
