package views;

import controllers.ControllerModererCommentaire;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.MainApp;
import models.Commentaire; // Assurez-vous d'avoir créé la classe Commentaire

public class VueModererCommentaire extends VueModerer<Commentaire> {
    private ControllerModererCommentaire controller;
    private String message="";

    public VueModererCommentaire() {
        super("Modération des Commentaires");
        table.getItems().addAll(MainApp.getCommentairesNonModeres());
        table.setFixedCellSize(80);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(MainApp.getCommentairesNonModeres().size()).add(50));
        controller = new ControllerModererCommentaire(); 
        afficherVue();
    }
    

    @Override
    protected void configureColumns() {
        TableColumn<Commentaire, String> colID = new TableColumn<>("ID");
        colID.setMinWidth(50);
        colID.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getIdCommentaire())));
        
        TableColumn<Commentaire, String> colTexte = new TableColumn<>("Commentaire");
        colTexte.setMinWidth(300);
        colTexte.setMinWidth(300);
        colTexte.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTextCommentaire()));

        colTexte.setCellFactory(tc -> new TableCell<Commentaire, String>() {
            private final Text text = new Text();
            private final ScrollPane scrollPane = new ScrollPane(text);
            {
                // Le ScrollPane occupe toute la largeur de la cellule
                scrollPane.setFitToWidth(true);
                // On désactive la barre de défilement horizontale, car le texte sera en wrapping
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                // Définir la hauteur de la vue du ScrollPane pour correspondre à la hauteur fixe de la cellule
                scrollPane.setPrefViewportHeight(80);
                // Le binding pour permettre au texte de revenir à la ligne
                text.wrappingWidthProperty().bind(scrollPane.widthProperty().subtract(10));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(scrollPane);
                }
            }
        });
        TableColumn<Commentaire, String> colUser = new TableColumn<>("Utilisateur");
        colUser.setMinWidth(100);
        colUser.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUtilisateur() != null 
                                                ? data.getValue().getUtilisateur().getPseudo_utilisateur() : "Inconnu"));
        
        TableColumn<Commentaire, String> colRecette = new TableColumn<>("Recette");
        colRecette.setMinWidth(150);
        colRecette.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRecette() != null 
                                                ? data.getValue().getRecette().getNom_recette() : "Inconnue"));
        
        TableColumn<Commentaire, Void> colActions = new TableColumn<>("Actions");
        colActions.setMinWidth(150);
        colActions.setCellFactory(param -> new TableCell<Commentaire, Void>() {
            private final Button btnAccepter = new Button("Accepter");
            private final Button btnRejeter = new Button("Rejeter");
            private final VBox vbox = new VBox(3, btnAccepter, btnRejeter);
            {
                vbox.setAlignment(Pos.CENTER);
                btnAccepter.setOnAction(event -> {
                    Commentaire commentaire = getTableView().getItems().get(getIndex());
                    message = controller.accepterCommentaire(commentaire);
                    
                    //Met à jour le tableau
                    getTableView().getItems().remove(commentaire);
                    
                    
                    //Affiche la notification
                    afficherNotification(message);
                    System.out.println(message);
                });
                btnRejeter.setOnAction(event -> {
                    Commentaire commentaire = getTableView().getItems().get(getIndex());
                    message = controller.rejeterCommentaire(commentaire);
                    
                    //Met à jour le tableau
                    getTableView().getItems().remove(commentaire);
                    
                    
                    //Affiche la notification
                    afficherNotification(message);
                    System.out.println(message);
                });
            }
            
            //Permet de mettre à jour l'affichage des cellules, nottament quand une est vide on affiche rien dutout
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : vbox);
            }
        });
        
        table.getColumns().addAll(colID, colTexte, colUser, colRecette, colActions);
        
        // Calcul de la largeur totale fixe occupée par les colonnes autres que Description
        double fixedWidth = colID.getMinWidth() + colUser.getMinWidth() +
        		colRecette.getMinWidth() + colActions.getMinWidth();
        // Optionnel : un padding pour tenir compte des espaces ou bordures
        double padding = 20;

        // Lier la largeur de la colonne Description pour qu'elle prenne le reste
        colTexte.prefWidthProperty().bind(table.widthProperty().subtract(fixedWidth + padding));
    }
    
}

