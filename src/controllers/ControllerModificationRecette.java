package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import main.ConnectionDB;
import main.MainApp;
import models.Ingredient;
import models.Recette;
import models.RecetteIngredient;
import views.VueConsultationRecette;

public class ControllerModificationRecette {
	
	private Recette recette;
	
	public ControllerModificationRecette() {
		
	}
	
	public void afficherVueConsultationRecette(Recette recette) {
		this.recette = recette;
    	new VueConsultationRecette(this.recette, Tools.chargerCommentaire(this.recette));
	}
	
	//PERMET D'ALLER CHERHCER UNE IMAGE DANS SON ORDINATEUR.
    public ArrayList <String> uploadImage() {
    	return Tools.recupImage();
    }
    
	public void miseAJourRecette(Recette recetteM, String nomRecetteM, String difficulteRecetteM,
			int valeurNbPersonneRecetteM, String prixRecetteM,
			int valeurTpsPreparationM, int valeurTpsCuissonM, String etpM,		
			int idRecette, ArrayList<String[]> ingredientData, ArrayList<String> photoRecette) {

    	if (etpM.isEmpty()) {
    		Tools.showAlert("Votre recette n'a pas d'étapes !");
    		return;
    	}
    	
		String nomPhoto="";
		if(photoRecette == null) {
    		nomPhoto="";
    	}else if (!photoRecette.isEmpty()) {
			nomPhoto=Tools.sauvegarderImage(photoRecette);
    	} 
		
		String query = "UPDATE recette SET nom_recette = ?, difficulte_recette = ?, nb_personne_recette = ?, prix_recette = ?, "
				+ "temps_preparation = ?, temps_cuisson = ?, etape_recette = ?, photo_recette = ?"
				+ "WHERE id_recette = ?;";

		try {
		 	// CONNECTION A LA BDD
			ConnectionDB db = new ConnectionDB();
			
			// INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
	        db.initPreparedStatement(query);        
	        
	        db.getPrepareStatement().setString(1, nomRecetteM);
	        db.getPrepareStatement().setString(2, difficulteRecetteM);
	        db.getPrepareStatement().setInt(3, valeurNbPersonneRecetteM);
	        db.getPrepareStatement().setString(4, prixRecetteM);
	        db.getPrepareStatement().setInt(5, valeurTpsPreparationM);
	        db.getPrepareStatement().setInt(6, valeurTpsCuissonM);
	        db.getPrepareStatement().setString(7, etpM);
	        db.getPrepareStatement().setString(8, nomPhoto);
	        db.getPrepareStatement().setInt(9, idRecette);
	        
		        int rowsAffected = db.getPrepareStatement().executeUpdate();
		        if (rowsAffected > 0) {	
		        	System.out.println("Recette modifiée avec succès !");
		        	
		        	// MODIFICATION EN LOCAL DES VALEURS DE L'OBJET RECETTE
		        	recetteM.setNom_recette(nomRecetteM);
		        	recetteM.setDifficulte_recette(difficulteRecetteM);
		        	recetteM.setNb_personne_recette(valeurNbPersonneRecetteM);
		        	recetteM.setPrix_recette(prixRecetteM);
		        	recetteM.setTemps_preparation(valeurTpsPreparationM);
		        	recetteM.setTemps_cuisson(valeurTpsCuissonM);
		        	recetteM.setEtape_recette(etpM);
		        	recetteM.setPhoto_recette(nomPhoto);
		        	
		        	 // Suppression des anciennes liaisons dans la table recette_ingredient
		            String deleteQuery = "DELETE FROM recette_ingredient WHERE id_recette = ?;";
		            db.initPreparedStatement(deleteQuery);
		            db.getPrepareStatement().setInt(1, idRecette);
		            int suppr = db.getPrepareStatement().executeUpdate();
		            System.out.println("Anciennes liaisons supprimées : " + suppr);
		            
		            
		            // Vide la liste locale d'ingrédients associée à la recette
		            recetteM.getListeIngredientsComplets().clear();
		            
		            System.out.println(recetteM.getListeIngredientsComplets());
		            
		            // Pour chaque ligne de ingredientData, créer un nouvel objet RecetteIngredient
		            for (String[] data : ingredientData) {
		                String nomIngredient = data[0];
		                float quantite = Float.valueOf(data[1]);
		                String unite = data[2];
		                
		                // Recherche de l'objet Ingredient dans la map
		                Ingredient ingredient = null;
		                for (Ingredient ing : MainApp.getMapIngredients().values()) {
		                    if (ing.getNomIngredient().equals(nomIngredient)) {
		                        ingredient = ing;
		                        break;
		                    }
		                }
		                
		                if (ingredient == null) {
		                    System.err.println("L'ingrédient " + nomIngredient + " n'a pas été trouvé.");
		                    continue;
		                }

		                // Création du RecetteIngredient en local
		                RecetteIngredient ri = new RecetteIngredient(recetteM, ingredient, quantite, unite);
		                recetteM.getListeIngredientsComplets().add(ri);
		                
		                // Insertion dans la table de liaison
		                String queryRI = "INSERT INTO recette_ingredient SET quantite_ingredient = ?, unite = ?, id_recette = ?, id_ingredient = ?;";
		                db.initPreparedStatement(queryRI);
		                db.getPrepareStatement().setFloat(1, ri.getQuantite());
		                db.getPrepareStatement().setString(2, ri.getUnite());
		                db.getPrepareStatement().setInt(3, recetteM.getId_recette());
		                db.getPrepareStatement().setInt(4, ri.getIngredient().getId_Ingredient());
		                
		                int lignesAffecte = db.getPrepareStatement().executeUpdate();
		                if (lignesAffecte > 0) {
		                    System.out.println("Insertion dans recette_ingredient réussie pour : " + nomIngredient);
		                } else {
		                    System.out.println("Erreur lors de l'insertion dans recette_ingredient pour : " + nomIngredient);
		                }
		            }
		            new ControllerToolBar().afficherMesRecettes();
	        	
		        } else {
	        	System.out.println("Aucune modification réalisée en BDD !");
	        }
		    // FERMETURE DE LA CONNEXION A LA BDD
	        db.closeConnection();
		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour de la recette : " + e.getMessage());
		}
	}
}
