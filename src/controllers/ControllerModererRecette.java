package controllers;

import java.sql.SQLException;
import java.util.Iterator;
import main.ConnectionDB;
import main.MainApp;
import models.Recette;

public class ControllerModererRecette {

	public String accepterRecette(int idRecette, Recette recette) {
		String query = "UPDATE recette SET statut_moderation_recette=1 WHERE id_recette = ?";
        recette.setStatut_moderation_recette(true);
        String message ="";
		try {
			 	ConnectionDB db = new ConnectionDB();
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, idRecette);
		        int rowsUpdated = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsUpdated > 0) {
		        	message="Recette acceptée avec succès !";
		        	recette.setStatut_moderation_recette(true);
		            MainApp.getMapRecettes().put(idRecette, recette);
		            
		        	for (int i =  MainApp.getRecetteNonModeres().size() - 1; i >= 0; i--) {
		        	    Recette r =  MainApp.getRecetteNonModeres().get(i);
		        	    if (r.getId_recette() == idRecette) {
		        	    	 MainApp.getRecetteNonModeres().remove(i);
		        	    }
		        	}
		        } else {
		        	message="Aucune recette mise à jour pour l'id : " + idRecette;
		        }
		        
		        db.closeConnection();
		    } catch (SQLException e) {
		    	message="Erreur lors de la mise à jour du statut de la recette : " + e.getMessage();
		    }
		return message;

	}
	
	public String supprimerRecette(int idRecette) {
		
		String message ="";
		try {
			 	ConnectionDB db = new ConnectionDB();
			 	
			 	String queryRI = "DELETE FROM recette_ingredient WHERE id_recette = ?";
				db.initPreparedStatement(queryRI);
				db.getPrepareStatement().setInt(1, idRecette);
				db.getPrepareStatement().executeUpdate();
				
				String query = "DELETE FROM recette WHERE id_recette = ?";
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, idRecette);
		        int rowsAffected = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsAffected > 0) {
		        	message="Recette supprimée avec succès !";
		        	//Suppression de la liste de mes recettes
		        	for (int i = MainApp.getUtilisateurActif().getListeMesRecettes().size() - 1; i >= 0; i--) {
		        	    Recette r = MainApp.getUtilisateurActif().getListeMesRecettes().get(i);
		        	    if (r.getId_recette() == idRecette) {
		        	    	MainApp.getUtilisateurActif().getListeMesRecettes().remove(i);
		        	    }
		        	}
		        	
		        	Iterator<Recette> parcourMapRecettes = MainApp.getMapRecettes().values().iterator();
		        	while (parcourMapRecettes.hasNext()) {
		        	    Recette r = parcourMapRecettes.next();
		        	    if (r != null && r.getId_recette() == idRecette) {
		        	    	parcourMapRecettes.remove();
		        	    }
		        	}
		        	
		        	//Suppression de map des recettes filtrées
		        	Iterator<Recette> parcourMapRecettesFiltre = MainApp.getRecettesFiltre().values().iterator();
		        	while (parcourMapRecettesFiltre.hasNext()) {
		        	    Recette r = parcourMapRecettesFiltre.next();
		        	    if (r != null && r.getId_recette() == idRecette) {
		        	    	parcourMapRecettesFiltre.remove();
		        	    }
		        	}
		        	
		        	//Suppression de la liste des recettes favorites
		        	for (int i = MainApp.getUtilisateurActif().getListe_favori_utilisateur().size() - 1; i >= 0; i--) {
		        	    Recette r = MainApp.getUtilisateurActif().getListe_favori_utilisateur().get(i);
		        	    if (r.getId_recette() == idRecette) {
		        	    	MainApp.getUtilisateurActif().getListe_favori_utilisateur().remove(i);
		        	    }
		        	}
		        	
		        } else {
		        	message="Aucune recette supprimée pour l'id : " + idRecette;
		        }
		        
		        db.closeConnection();
		    } catch (SQLException e) {
		        message="Erreur lors de la suppression de la recette : " + e.getMessage();
		    }
		return message;

	}

}
