package views;

import controllers.ControllerGererModerateur;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.MainApp;
import models.Utilisateur;


public class VueGererModerateur {
	

	public VueGererModerateur() {
        construirePage();
    }
	
    private Label notificationLabel;
    
    private void construirePage() {
        Label titleLabel = new Label("Gérer modérateurs");
        titleLabel.getStyleClass().add("labelTitre");
        notificationLabel = new Label();
        

        MainApp.getMainStage().setResizable(false);

        // Création du tableau des utilisateurs
        TableView<Utilisateur> table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Colonne "Nom"
        TableColumn<Utilisateur, String> colNom = new TableColumn<>("Nom");
        colNom.setMinWidth(100);
        colNom.setMaxWidth(100);
        colNom.setPrefWidth(100);
        colNom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNom_utilisateur()));

        // Colonne "Prénom"
        TableColumn<Utilisateur, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.prefWidthProperty().bind(table.widthProperty().subtract(300).divide(3));
        colPrenom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPrenom_utilisateur()));

        // Colonne "Pseudo"
        TableColumn<Utilisateur, String> colPseudo = new TableColumn<>("Pseudo");
        colPseudo.prefWidthProperty().bind(table.widthProperty().subtract(300).divide(3));
        colPseudo.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPseudo_utilisateur()));

        // Colonne "Email"
        TableColumn<Utilisateur, String> colEmail = new TableColumn<>("Email");
        colEmail.prefWidthProperty().bind(table.widthProperty().subtract(300).divide(3));
        colEmail.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getMail_utilisateur()));

        table.getColumns().addAll(colNom, colPrenom, colPseudo, colEmail);

        // Colonne des actions (boutons)
        TableColumn<Utilisateur, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setMinWidth(200);
        actionColumn.setMaxWidth(200);
        actionColumn.setPrefWidth(200);
        actionColumn.setResizable(false);

        actionColumn.setCellFactory(tc -> new TableCell<Utilisateur, Void>() {
            private final Button toggleRoleButton = new Button();
            private final Button deleteButton = new Button("Supprimer");
            private final HBox container = new HBox(5, toggleRoleButton, deleteButton);

            {
                // Action sur le bouton de basculement de rôle
            	toggleRoleButton.setOnAction(event -> {
            	    Utilisateur user = getTableView().getItems().get(getIndex());
            	    
            	    // Créer une alerte de confirmation pour le changement de rôle
            	    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            	    confirmationAlert.setTitle("Confirmation de modification de rôle");
            	    confirmationAlert.setHeaderText("Modifier le rôle de " + user.getPseudo_utilisateur());
            	    confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier le rôle de cet utilisateur ?");
            	    
            	    // Afficher la pop-up et attendre la réponse
            	    confirmationAlert.showAndWait().ifPresent(response -> {
            	        if (response == ButtonType.OK) {
            	            String message = new ControllerGererModerateur().toggleRoleUtilisateur(user);
            	            updateButtonText(user);
            	            afficherNotification(message);
            	            getTableView().refresh();
            	        } else {
            	            afficherNotification("Modification du rôle annulée.");
            	        }
            	    });
            	});

                
                // Action sur le bouton supprimer
                deleteButton.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    
                    // Créer une alerte de confirmation
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation de suppression");
                    confirmationAlert.setHeaderText("Supprimer l'utilisateur " + user.getPseudo_utilisateur());
                    confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur et les recettes qu'il a créées ?");
                    
                    // Afficher la pop-up et attendre la réponse
                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                        	// Appel de la méthode de suppression dans le contrôleur
                            String message = new ControllerGererModerateur().supprimerUtilisateur(user);
                            afficherNotification(message);
                            // Retirer l'utilisateur du tableau si la suppression a réussi
                            getTableView().getItems().remove(user);
                        } else {
                            afficherNotification("Suppression annulée.");
                        }
                    });
                    
                });
            }

            private void updateButtonText(Utilisateur user) {
                if (user.getRole_utilisateur().equalsIgnoreCase("Utilisateur")) {
                    toggleRoleButton.setText("Promouvoir");
                } else if (user.getRole_utilisateur().equalsIgnoreCase("Moderateur")) {
                    toggleRoleButton.setText("Dégrader");
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    updateButtonText(user);
                    setGraphic(container);
                }
            }
        });
        
        table.getColumns().add(actionColumn);
        
        // Remplir le tableau avec la liste des modérateurs depuis le controller
        table.getItems().addAll(MainApp.getUtilisateurNonAdmin());
        
        // Fixer la hauteur pour afficher toutes les lignes
        table.setFixedCellSize(30);
        table.setPrefHeight(700);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(700);

        VBox root = new VBox(10, titleLabel, notificationLabel,scrollPane);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
		
        VBox combinaison = new VBox(MainApp.getMaToolBar().getTop(), root);
        combinaison.setMaxSize(1920, 1080);
        combinaison.setMinSize(1920, 1080);
        combinaison.getStyleClass().add("background");
        Scene scene = new Scene(combinaison);
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
		MainApp.getMainStage().setScene(scene);
		MainApp.getMainStage().setMaximized(true);
		MainApp.getMainStage().setResizable(true);
		MainApp.getMainStage().setTitle("Gestion des modérateurs");
		MainApp.getMainStage().show();
    }
    
    /**
     * Affiche le message de notification en vert pendant 5 secondes.
     */
    private void afficherNotification(String message) {
        notificationLabel.setText(message);
        // Crée une pause de 5 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> notificationLabel.setText(""));
        pause.play();
    }
}

