package views;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.MainApp;

public abstract class VueModerer<T> {
    protected Stage stage;
    protected Label titleLabel;
    protected Label notificationLabel;
    protected TableView<T> table;
    
    public VueModerer(String title) {
        this.stage = MainApp.getMainStage();
        this.titleLabel = new Label(title);
        titleLabel.getStyleClass().add("labelTitre");
        this.notificationLabel = new Label();
        notificationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    // Méthode pour configurer les colonnes spécifiques à chaque vue
    protected abstract void configureColumns();
    
    protected void afficherNotification(String message) {
        notificationLabel.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> notificationLabel.setText(""));
        pause.play();
    }
    
	public void afficherVue() {
        // Configure les colonnes et charge les données
        configureColumns();
        
        VBox root = new VBox(10, titleLabel, notificationLabel, table);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		VBox combinaison = new VBox(MainApp.getMaToolBar().getTop(), root); // Les deux VBox seront empilées verticalement
		combinaison.getStyleClass().add("background");
        Scene scene = new Scene(combinaison);
		try {
        	scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());	 
        } catch (NullPointerException e) {
            System.err.println("Le fichier styles.css est introuvable : " + e.getMessage());
        }
		combinaison.setMaxSize(1920, 1080);
        combinaison.setMinSize(1920, 1080);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle(titleLabel.getText());
        stage.show();
    }
}
