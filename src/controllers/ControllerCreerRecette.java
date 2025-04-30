package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.ConnectionDB;
import main.MainApp;
import models.Ingredient;
import models.Recette;
import models.RecetteIngredient;

public class ControllerCreerRecette {
	
	private String message =" ";
	
	public ControllerCreerRecette() {
		
	}
	
//PERMET D'ALLER CHERHCER UNE IMAGE DANS SON ORDINATEUR.
    public ArrayList <String> uploadImage() {
    	return Tools.recupImage();
    }
	
	public String creerRecette(String nomRecette, String difficulteRecette,
			int valeurNbPersonneRecette, String prixRecette,
			int valeurTpsPreparation, int valeurTpsCuisson, String etape, ArrayList<String> nomsFichierPhoto, String catRecette, ArrayList<String[]> ingredientsData) {
		if (etape.isEmpty()) {
			return"Votre recette n'a pas d'étapes !";
    	}
		
		if (nomRecette.isEmpty()) {
    		return"Votre recette n'a pas de nom !";
    	}
		
		
		int categorieRecette = 0;
		switch (catRecette) {
		case "Entrée" : categorieRecette =1;
			break;
			
		case "Plat" : categorieRecette = 2;
			break;
			
		case "Dessert" : categorieRecette = 3;
			break;
			
		default : categorieRecette = 2;
			break;
		}
		String nomPhoto="";
		if(nomsFichierPhoto == null) {
    		nomPhoto="";
    	}else if (!nomsFichierPhoto.isEmpty()) {
			nomPhoto=Tools.sauvegarderImage(nomsFichierPhoto);
    	} 
		
		String query = "INSERT INTO recette SET nom_recette = ?, difficulte_recette = ?, nb_personne_recette = ?, prix_recette = ?, "
				+ "temps_preparation = ?, temps_cuisson = ?, temps_total_recette = ?, etape_recette = ?, id_utilisateur = ?, photo_recette = ?, id_cat_recette = ?;";

		try {
		 	// CONNECTION A LA BDD
			ConnectionDB db = new ConnectionDB();
			
			db.initPreparedStatement(query, Statement.RETURN_GENERATED_KEYS);
	        db.getPrepareStatement().setString(1, nomRecette);
	        db.getPrepareStatement().setString(2, difficulteRecette);
	        db.getPrepareStatement().setInt(3, valeurNbPersonneRecette);
	        db.getPrepareStatement().setString(4, prixRecette);
	        db.getPrepareStatement().setInt(5, valeurTpsPreparation);
	        db.getPrepareStatement().setInt(6, valeurTpsCuisson);
	        db.getPrepareStatement().setInt(7, valeurTpsCuisson+valeurTpsPreparation);
	        db.getPrepareStatement().setString(8, etape);
	        db.getPrepareStatement().setInt(9, MainApp.getUtilisateurActif().getId_utilisateur());
	        db.getPrepareStatement().setString(10, nomPhoto);
	        db.getPrepareStatement().setInt(11, categorieRecette);

	    
	        	int idGenRecette = 0;
		        int rowsAffected = db.getPrepareStatement().executeUpdate();
		        if (rowsAffected > 0) {	
		        	System.out.println("Recette modifiée avec succès !");

		            ResultSet generatedKeys = db.getPrepareStatement().getGeneratedKeys();
		            
		            if (generatedKeys.next()) {
		                idGenRecette = generatedKeys.getInt(1);
		                System.out.println("ID de la recette créée : " + idGenRecette);
		            } else {
		                System.err.println("La création de la recette a réussi, mais aucun ID n'a été récupéré.");
		            }
		            
		        	// MODIFICATION EN LOCAL DES VALEURS DE L'OBJET RECETTE avec la liste d'ingrédients vide
		        	System.out.println("Création de la recette en local");
		        	Recette recette = new Recette(idGenRecette, nomRecette, false, valeurTpsPreparation,
		        			valeurTpsCuisson, difficulteRecette, valeurNbPersonneRecette,
		    				prixRecette, etape, catRecette,
		    				nomPhoto, MainApp.getUtilisateurActif().getId_utilisateur(), null
		        			);
		        			
		        	ArrayList<RecetteIngredient> listeRecetteIngredient = new ArrayList<>();
		        	
		        	// Pour chaque tableau de String, retrouver l'objet Ingredient et créer un RecetteIngredient
		            for (String[] data : ingredientsData) {
		                String nomIngredient = data[0];
		                float quantite = Float.valueOf(data[1]);
		                String unite = data[2];
		                
		                Ingredient ingredient = null;
		                
		                for(Ingredient ing : MainApp.getMapIngredients().values()) {
		                	if (ing.getNomIngredient().equals(nomIngredient)) {
		                	    ingredient = ing;
		                	    break;
		                	}
		                }
		                
		                if (ingredient == null) {
		                    System.err.println("L'ingrédient " + nomIngredient + " n'a pas été trouvé.");
		                    continue;
		                }
		                // Création du RecetteIngredient et ajout à la recette
		                RecetteIngredient ri = new RecetteIngredient(recette, ingredient, quantite, unite);
		                listeRecetteIngredient.add(ri);
		                //Ajout dans la table de liaison
		            	String queryRI = "INSERT INTO recette_ingredient SET quantite_ingredient = ?, unite = ?, id_recette = ?, id_ingredient = ?;";

		        		try {
		        			
		        			// INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
		        	        db.initPreparedStatement(queryRI);        
		        	        System.out.println("ID INGREDIENT : "+ri.getIngredient().getId_Ingredient());
		        	        db.getPrepareStatement().setFloat(1, ri.getQuantite());
		        	        db.getPrepareStatement().setString(2, ri.getUnite());
		        	        db.getPrepareStatement().setInt(3, ri.getRecette().getId_recette());
		        	        db.getPrepareStatement().setInt(4, ri.getIngredient().getId_Ingredient());
		        	        
		    		        int lignesAffecte = db.getPrepareStatement().executeUpdate();
		    		        
		    		        if (lignesAffecte > 0) {
		    		        	//Ajout de la liste à l'objet recette
				        		recette.setListeIngredientsComplets(listeRecetteIngredient);
				        		message = "Recette ajoutée !";
		    		        }else {
				        		System.out.println("Erreur lors de la mise à jour de la table de liaison remplie : recette_ingredient");
		    		        }

		        	        
		        		} catch (SQLException e) {
		        			System.err.println("Erreur lors de la mise à jour de recette_ingredients : " + e.getMessage());
		        			message ="Aucune modification réalisée, échec de l'enregistrement !";
		        		}
		        		
		        		
		        		//Ajout de la recette à la liste de recettes non modérés si l'utilisateur est modérateur ou admin
		        		if (MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("administrateur") || MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("moderateur")) {
			        		MainApp.getRecetteNonModeres().add(recette);
		        		}
		        		
		            }
		        	
		            //Ajout à la liste MesRecette
		            MainApp.getUtilisateurActif().getListeMesRecettes().add(recette);
	        		
	        		new ControllerToolBar().afficherMesRecettes();
		        } else {
		        message ="Aucune modification réalisée, échec de l'enregistrement !";
	        }
		    // FERMETURE DE LA CONNEXION A LA BDD
	        db.closeConnection();
		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour de la recette : " + e.getMessage());
		}
		return message;
	}

}
