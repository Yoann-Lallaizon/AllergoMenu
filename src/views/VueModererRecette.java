package views;

import controllers.ControllerModererRecette;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.MainApp;
import models.Recette;

public class VueModererRecette extends VueModerer<Recette> {
    private ControllerModererRecette controller;
    
    public VueModererRecette() {
        super("Modération des Recettes");
        // Remplit la table avec la liste de recettes
        table.getItems().addAll(MainApp.getRecetteNonModeres());
        // Optionnel : ajustement de la hauteur du tableau
        table.setFixedCellSize(80);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty()
                .multiply(MainApp.getRecetteNonModeres().size()).add(50));
        controller = new ControllerModererRecette(); 
        // Affichage de la vue
        afficherVue();
    }
    
    @Override
    protected void configureColumns() {
        // Colonne ID recette
        TableColumn<Recette, String> colID = new TableColumn<>("ID");
        colID.setMinWidth(50);
        colID.setCellValueFactory(data -> 
                new ReadOnlyStringWrapper(String.valueOf(data.getValue().getId_recette())));

        // Colonne Nom de la recette
        TableColumn<Recette, String> colNom = new TableColumn<>("Nom de la recette");
        colNom.setMinWidth(150);
        colNom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNom_recette()));

        // Colonne Description : etape_recette
        TableColumn<Recette, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getEtape_recette()));
        // On définit un cellFactory avec wrapping (pour le retour à la ligne)
        colDescription.setCellFactory(tc -> new TableCell<Recette, String>() {
            private final Text text = new Text();
            private final ScrollPane scrollPane = new ScrollPane(text);
            {
                // Le ScrollPane s'adapte à la largeur de la cellule
                scrollPane.setFitToWidth(true);
                // On désactive la barre de défilement horizontale car le texte sera wrapping
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                // Optionnel : définir une hauteur préférée pour la cellule (ou utiliser la hauteur fixe de la table)
                scrollPane.setPrefViewportHeight(80);
                // Le binding pour que le texte s'adapte à la largeur disponible du ScrollPane
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
        // Colonne Nom de l'utilisateur  
        TableColumn<Recette, String> colUser = new TableColumn<>("Nom de l'utilisateur");
        colUser.setMinWidth(150);
        colUser.setCellValueFactory(data -> {
            // Récupère l'id du créateur stocké dans la recette
            int idCreateur = data.getValue().getId_utilisateur_createur_recette();
           
            String nomUtilisateur = MainApp.getAllUtilisateurs().get(idCreateur).getPseudo_utilisateur();
            return new ReadOnlyStringWrapper(nomUtilisateur != null ? nomUtilisateur : "Inconnu ou créé par un administrateur");
        });
        
        // Colonne Photo (URL de la photo)
        TableColumn<Recette, String> colPhoto = new TableColumn<>("Photo");
        colPhoto.setMinWidth(150);
        colPhoto.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPhoto_recette()));

        // Colonne Action (similaire à votre version précédente)
        TableColumn<Recette, Void> colActions = new TableColumn<>("Actions");
        colActions.setMinWidth(150);
        colActions.setCellFactory(param -> new TableCell<Recette, Void>() {
            private final Button btnAccepter = new Button("Accepter");
            private final Button btnRejeter = new Button("Rejeter");
            private final VBox vbox = new VBox(3, btnAccepter, btnRejeter);
            {
                vbox.setAlignment(Pos.CENTER);
                btnAccepter.setOnAction(event -> {
                    Recette recette = getTableView().getItems().get(getIndex());
                    
                    //Execute le changement en base de donnée et modifie l'objet recette
                    String message = controller.accepterRecette(recette.getId_recette(), recette);
                    
                    //Met à jour le tableau
                    getTableView().getItems().remove(recette);
                    
                    
                    //Affiche la notification
                    afficherNotification(message);
                    System.out.println(message);
                });
                btnRejeter.setOnAction(event -> {
                    Recette recette = getTableView().getItems().get(getIndex());
                    String message = controller.supprimerRecette(recette.getId_recette());
                                        
                    //Met à jour le tableau
                    getTableView().getItems().remove(recette);
                    
                    //Affiche la notification
                    afficherNotification(message);
                    System.out.println(message);
                });
            }
            
            @Override
            //Permet de mettre à jour l'affichage des cellules, nottament quand une est vide on affiche rien dutout
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : vbox);
            }
        });
        
        table.getColumns().addAll(colID, colNom, colDescription, colUser, colPhoto, colActions);
        
        // Calcul de la largeur totale fixe occupée par les colonnes autres que Description
        double fixedWidth = colID.getMinWidth() + colNom.getMinWidth() + colUser.getMinWidth() +
                            colPhoto.getMinWidth() + colActions.getMinWidth();
        // Optionnel : un padding pour tenir compte des espaces ou bordures
        double padding = 20;

        // Lier la largeur de la colonne Description pour qu'elle prenne le reste
        colDescription.prefWidthProperty().bind(table.widthProperty().subtract(fixedWidth + padding));
    }

}
