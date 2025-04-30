package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.ConnectionDB;
import main.MainApp;
import models.Commentaire;
import models.Maladie;
import models.Recette;



abstract public class Tools {
	
	
	
	//AUTRES METHODES
		
		//RECUPERATION DU NOM D'UNE IMAGE DANS LES FICHIERS LOCAUX
		protected static ArrayList<String> recupImage() {
			
			FileChooser fileChooser = new FileChooser();
	        File file = fileChooser.showOpenDialog(new Stage());
	        
	        // Ajout d'un filtre pour les fichiers JPG et PNG
	        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
	            "Fichiers image (JPG, PNG)", "*.jpg", "*.png"
	        );
	        
	        fileChooser.getExtensionFilters().add(imageFilter);
	       

	        String urlACopier = (Paths.get(file.getAbsolutePath())).toString();
	        String nomImage=(Paths.get(file.getAbsolutePath()).getFileName()).toString();
	        String urlAColler = (Paths.get(("./src/images/"+ Paths.get(file.getAbsolutePath()).getFileName().toString()))).toString();
	        
	        
	        //Création de la liste à transmettre (0:urlACopier ; 1:urlAColler ; 2:nomImage)
	        ArrayList<String> listeUrl = new ArrayList<>();
	        listeUrl.add(urlACopier);
	        listeUrl.add(urlAColler);
	        listeUrl.add(nomImage);
	        
	        return listeUrl;
		}
		
		//COPIE D'UNE IMAGE A PARTIR D'UNE LISTE DE DEUX ADRESSES ET UN NOM
		protected static String sauvegarderImage(ArrayList<String> listeUrl) {
			try {
				
	    		Path copier=Paths.get(listeUrl.get(0));
	    		Path coller=Paths.get(listeUrl.get(1));
				Files.copy(copier, coller); //il faudra virer l'option pour remplacer la copie existante car ça peut vite foutre le bordel.
				
				if(!copier.toString().isEmpty() || copier.toString() != null) {
					System.out.println("Image enregistrée");
				}
				return listeUrl.get(2);
				
			} catch (IOException e1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("Le nom de l'image existe déjà ! Veuillez changer le nom de votre image avant de l'uploader, ou sélectionnez une autre image !");
				alert.showAndWait();
				return null;
			}
			
		}
		
		//REMPLISSAGE D'UNE LISTE DE COMMENTAIRE
		public static ArrayList<Commentaire>  chargerCommentaire(Recette recette) {
			ArrayList<Commentaire> commentairesRecette = new ArrayList<>();
	    	
	    	String query = "SELECT c.id_commentaire, c.text_commentaire, "
	    			+ "c.id_recette, c.id_utilisateur, c.date_publi_commentaire "
	        		+ "FROM commentaire c "
	        		+ "WHERE c.id_recette = ? AND c.statut_moderation_commentaire =1 ;";
	    	
	    	try {
	    		// CONNECTION A LA BDD
		        ConnectionDB db = new ConnectionDB();
		        
		        // INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, recette.getId_recette());
		        
		        ResultSet rs = db.getPrepareStatement().executeQuery();
		        
		        while (rs.next()) {
		        	Commentaire commentaire = new Commentaire(
		        			rs.getInt("id_commentaire"),
		        			rs.getString("text_commentaire"),
		        			true,
		        			MainApp.getMapRecettes().get(rs.getInt("id_recette")),
		        			MainApp.getAllUtilisateurs().get(rs.getInt("id_utilisateur")),
		        			rs.getString("date_publi_commentaire")
		        		);
		        	commentairesRecette.add(commentaire); // RAJOUTE LES ELEMENTS DANS LA LISTE COMMENTAIRES_RECETTE
		        }
		        db.closeConnection();
		    } catch (SQLException e) {
		        System.err.println("Erreur lors de la récupération des commentaires de la recette : " + e.getMessage());
		    }
	    	
	    	return commentairesRecette;
		}
	
		//METHODE PRIVEE POUR VERIFIER LA COMPATIBILITE D'UNE RECETTE AVEC LES MALADIES SELECTIONNEES
		protected static boolean recetteCompatibleAvecMaladies(Recette rec, ArrayList<Maladie> selectedMaladies) {
	        // Si aucun filtre n'est appliqué, la recette est compatible
	        if (selectedMaladies == null || selectedMaladies.isEmpty()) {
	            return true;
	        }
	        for (Maladie maladie : selectedMaladies) {
	            ArrayList<?> ingredientsMaladie = maladie.getListe_Ingredient(); // Supposé List<Ingredient>
	            if (ingredientsMaladie == null || ingredientsMaladie.isEmpty()) {
	                continue;
	            }
	            for (Object o : rec.getListeIngredientsComplets()) {
	                models.RecetteIngredient ri = (models.RecetteIngredient) o;
	                models.Ingredient ingRecette = ri.getIngredient();
	                for (Object obj : ingredientsMaladie) {
	                    models.Ingredient ingMaladie = (models.Ingredient) obj;
	                    if (ingRecette.getNomIngredient().trim().equalsIgnoreCase(ingMaladie.getNomIngredient().trim())) {
	                        return false;
	                    }
	                }
	            }
	        }
	        return true;
	    }
	
	//Méthode pour le contrôle de saisie du mot de passe
	protected static boolean isPasswordValid(String password) {
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$"; // 8+ caractères 1 majuscule 1 minuscule 1 chiffre
        return Pattern.matches(passwordRegex, password);
	}

	//Méthode pour le contrôle de saisie du username
	protected static boolean isUsernameValid(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{4,20}$"; // Alphanumérique 4-20 caractères
        return Pattern.matches(usernameRegex, username);
	}
	
	//Méthode pour le contrôle de saisie du mail
	protected static boolean isMailValid(String mail) {
		String mailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Partie locale puis '@' puis domaine et extension de plus de 2 lettres.
        return Pattern.matches(mailRegex, mail);
	}
	
	// Méthode pour afficher une alerte
    public static void showAlert(String message) {
    	if(message.isEmpty() || message == null) {
    		return;
    	}
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setContentText(message);
        alert.show();
    }
}
