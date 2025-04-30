package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import main.ConnectionDB;
import main.MainApp;
import models.Commentaire;

public class ControllerModererCommentaire {

	public String accepterCommentaire(Commentaire commentaire) {
		String query = "UPDATE commentaire SET statut_moderation_commentaire=1 WHERE id_commentaire = ?";
		commentaire.setStatutModeration(true);
		String message="";
		int idCommentaire = commentaire.getIdCommentaire();
		try {
			 	ConnectionDB db = new ConnectionDB();
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, idCommentaire);
		        int rowsUpdated = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsUpdated > 0) {
		        	message="Commentaire acceptée avec succès !";
		        	ArrayList<Commentaire> commentaireNonmoderes = MainApp.getCommentairesNonModeres();
		        	for (int i = commentaireNonmoderes.size() - 1; i >= 0; i--) {
		        	    Commentaire c = commentaireNonmoderes.get(i);
		        	    if (c.getIdCommentaire() == commentaire.getIdCommentaire()) {
		        	    	commentaireNonmoderes.remove(i);
		        	    }
		        	}
		        } else {
		        	message="Aucun commentaire mise à jour pour l'id : " + idCommentaire;
		        }
		        
		        db.closeConnection();
		    } catch (SQLException e) {
		    	message="Erreur lors de la mise à jour du statut du commentaire : " + e.getMessage();
		    }
		return message;
	}

	public String rejeterCommentaire(Commentaire commentaire) {
		String query = "DELETE FROM commentaire WHERE id_commentaire = ?";
		String message="";
		int idCommentaire = commentaire.getIdCommentaire();
		try {
			 	ConnectionDB db = new ConnectionDB();
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, idCommentaire);
		        int rowsAffected = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsAffected > 0) {
		        	message="Commentaire supprimée avec succès !";
		        	ArrayList<Commentaire> commentaireNonmoderes = MainApp.getCommentairesNonModeres();
		        	for (int i = commentaireNonmoderes.size() - 1; i >= 0; i--) {
		        	    Commentaire c = commentaireNonmoderes.get(i);
		        	    if (c.getIdCommentaire() == commentaire.getIdCommentaire()) {
		        	    	commentaireNonmoderes.remove(i);
		        	    }
		        	}
		        } else {
		        	message="Aucun commentaire supprimée pour l'id : " + idCommentaire;
		        }
	        
		        db.closeConnection();
		    } catch (SQLException e) {
		        message="Erreur lors de la suppression du commentaire : " + e.getMessage();
		    }
		return message;

	}

}
