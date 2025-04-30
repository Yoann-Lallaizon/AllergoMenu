package controllers;

import java.sql.SQLException;

import main.ConnectionDB;
import main.MainApp;
import models.Utilisateur;

public class ControllerGererModerateur {

    //CONSTRUCTEUR
    public ControllerGererModerateur() {
    }

    public String toggleRoleUtilisateur(Utilisateur user) {
    	String message="";
        if (user.getRole_utilisateur().equals("utilisateur")) {
        	String query = "UPDATE utilisateur SET id_role=3 WHERE id_Utilisateur = ?";
    		
    		int idUtilisateur = user.getId_utilisateur();
    		try {
    			 	ConnectionDB db = new ConnectionDB();
    		        db.initPreparedStatement(query);
    		        db.getPrepareStatement().setInt(1, idUtilisateur);
    		        int rowsUpdated = db.getPrepareStatement().executeUpdate();
    		        
    		        if (rowsUpdated > 0) {
    		        	message = "Le role de "+user.getPseudo_utilisateur()+" a été mis à jour.";
    		        	user.setRole_utilisateur("moderateur");
    		        } else {
    		        	message = "Le role de "+user.getPseudo_utilisateur()+" n'a pas été mis à jour. Role : "+user.getRole_utilisateur();
    		        }
    		        
    		        db.closeConnection();
    		    } catch (SQLException e) {
    		    	message="Erreur lors de la mise à jour du statut du commentaire : " + e.getMessage();
    		    }
            
        } else if (user.getRole_utilisateur().equals("moderateur")) {
        	String query = "UPDATE utilisateur SET id_role=2 WHERE id_Utilisateur = ?";
    		
    		int idUtilisateur = user.getId_utilisateur();
    		try {
    			 	ConnectionDB db = new ConnectionDB();
    		        db.initPreparedStatement(query);
    		        db.getPrepareStatement().setInt(1, idUtilisateur);
    		        int rowsUpdated = db.getPrepareStatement().executeUpdate();
    		        
    		        if (rowsUpdated > 0) {
    		        	message = "Le role de "+user.getPseudo_utilisateur()+" a été mis à jour.";
    		        	user.setRole_utilisateur("utilisateur");
    		        } else {
    		        	message = "Le role de "+user.getPseudo_utilisateur()+" n'a pas été mis à jour. Role : "+user.getRole_utilisateur();
    		        }
    		        
    		        db.closeConnection();
    		    } catch (SQLException e) {
    		    	message="Erreur lors de la mise à jour du statut du commentaire : " + e.getMessage();
    		    }
            
        } else {
        	message = "Rôle inconnu pour " + user.getNom_utilisateur();
        }
        return message;
    }

    public String supprimerUtilisateur(Utilisateur user) {
        String query = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        String message;
        try {
            ConnectionDB db = new ConnectionDB();
            db.initPreparedStatement(query);
            db.getPrepareStatement().setInt(1, user.getId_utilisateur());
            int rowsAffected = db.getPrepareStatement().executeUpdate();
            db.closeConnection();
            
            if (rowsAffected > 0) {
                message = "L'utilisateur " + user.getPseudo_utilisateur() + " a été supprimé avec succès.";
                //Suppression de la liste des Utilisateurs non admin
                for (int i = MainApp.getUtilisateurNonAdmin().size() - 1; i >= 0; i--) {
                    if (MainApp.getUtilisateurNonAdmin().get(i).getId_utilisateur() == user.getId_utilisateur()) {
                    	MainApp.getUtilisateurNonAdmin().remove(i);
                    }
                }
            } else {
                message = "Aucun utilisateur n'a été supprimé pour l'id " + user.getId_utilisateur();
            }
        } catch (SQLException e) {
            message = "Erreur lors de la suppression de l'utilisateur : " + e.getMessage();
        }
        return message;
    }

}
