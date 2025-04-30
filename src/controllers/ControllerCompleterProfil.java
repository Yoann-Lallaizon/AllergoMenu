package controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.ConnectionDB;
import views.VueConnexion;


public class ControllerCompleterProfil {
    
    public ControllerCompleterProfil() {
        
    }
    
    public String enregistrerProfil(String pseudo, String email, String password, String nom, String prenom, String dateNaissance, String sexeSelect, 
    								ArrayList<String> maladieSelect, ArrayList<String> listeUrl) {
    	
        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance == null) {
            return "Veuillez remplir tous les champs.";
        }
        
        if (sexeSelect == null || sexeSelect.isEmpty() || !sexeSelect.equals("H") && !sexeSelect.equals("F") && !sexeSelect.equals("N")) {
            return "Veuillez sélectionner un sexe.";
        }
        
        try {
        	// CONNECTION A LA BDD
        	ConnectionDB db = new ConnectionDB();
            
        	//Photo
        	String avatarChemin = Tools.sauvegarderImage(listeUrl);
            // Insérer l'utilisateur
            String userQuery = "INSERT INTO utilisateur (nom_utilisateur, prenom_utilisateur, mail_utilisateur, mdp_utilisateur, pseudo_utilisateur, avatar_utilisateur, date_naissance_utilisateur, sexe_utilisateur, id_role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			db.initPreparedStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
			db.getPrepareStatement().setString(1, nom);
			db.getPrepareStatement().setString(2, prenom);
			db.getPrepareStatement().setString(3, email);
			db.getPrepareStatement().setString(4, password);
			db.getPrepareStatement().setString(5, pseudo);
			db.getPrepareStatement().setString(6, avatarChemin);
			db.getPrepareStatement().setDate(7, Date.valueOf(dateNaissance));
			db.getPrepareStatement().setString(8, sexeSelect); // VALEUR CORRECTE "H", "F" OU "N"
			db.getPrepareStatement().setInt(9, 2);

			int idUser=0;
	        int ligneModif = db.getPrepareStatement().executeUpdate();
	        if (ligneModif > 0) {	
	            ResultSet generatedKeys = db.getPrepareStatement().getGeneratedKeys();
	            
	            if (generatedKeys.next()) {
	            	idUser = generatedKeys.getInt(1);
	                System.out.println("ID de l'utilisateur créé : " + idUser);
	            } else {
	                System.err.println("La création de l'utilisateur a réussi, mais aucun ID n'a été récupéré.");
	            }
	        }

            // INSERER LES MALADIES SELECTIONNEES
	        int nbInsertion = 0;
            String declarerQuery = "INSERT INTO declarer (id_utilisateur, id_maladie) VALUES (?, (SELECT id_maladie FROM maladie WHERE nom_maladie = ?))";
	        for (String nomMaladieSelect : maladieSelect ) {
	        	db.initPreparedStatement(declarerQuery);        
		        db.getPrepareStatement().setInt(1, idUser);
		        db.getPrepareStatement().setString(2, nomMaladieSelect);
		        nbInsertion += db.getPrepareStatement().executeUpdate();
	        }
	        if (nbInsertion == maladieSelect.size()-1) {	
	            return "Profil enregistré avec succès !";
	        }
	        
	        db.closeConnection();  
	        
	        new VueConnexion();
          
        } catch (SQLException e) {
            e.printStackTrace();            
            return"Une erreur est survenue lors de l'enregistrement.";
        }
        return "";
    }
}
