package controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import javafx.stage.Stage;
import main.ConnectionDB;
import main.MainApp;
import models.Maladie;

public class ControllerParametreCompte {

    public ControllerParametreCompte() {
    	
    }
    
//PERMET D'ALLER CHERHCER UNE IMAGE DANS SON ORDINATEUR.
    public ArrayList <String> uploadAvatar() {
    	return Tools.recupImage();
    }
    

    public void mettreAJourInformations(String nomField, String prenomField, String emailField, LocalDate dateNaissancePicker, String pseudoField, String sexeGroup, boolean maladie1, boolean maladie2, boolean maladie3, ArrayList<String> listeUrl) {
        
        if (MainApp.getUtilisateurActif() != null) {
        	
        	ArrayList<Maladie> listeMaladie = new ArrayList<>();
        	
        	if (nomField!=null && nomField!="") {
        		MainApp.getUtilisateurActif().setNom_utilisateur(nomField);
        	}
        	
        	if (prenomField!=null && prenomField!="" ) {
        		MainApp.getUtilisateurActif().setPrenom_utilisateur(prenomField);
        	}
        	
        	if (emailField!=null && emailField!="" && Tools.isMailValid(emailField)) {
        		MainApp.getUtilisateurActif().setMail_utilisateur(emailField);
        	}
        	
        	if (dateNaissancePicker!=null) {
        		MainApp.getUtilisateurActif().setDate_naissance_utilisateur(dateNaissancePicker.toString());
        	}
        	
        	if (pseudoField!=null && pseudoField!="" && Tools.isUsernameValid(pseudoField)) {
        		MainApp.getUtilisateurActif().setPseudo_utilisateur(pseudoField);	
        	}
        	
        	if (sexeGroup!=null && sexeGroup!="") {
        		MainApp.getUtilisateurActif().setSexe_utilisateur(sexeGroup.charAt(0));
        	}
        	
        	if (maladie1==true) {
        		listeMaladie.add(MainApp.getMapAllMaladies().get(1));
        	}
        	if (maladie2==true) {
        		listeMaladie.add(MainApp.getMapAllMaladies().get(2));
        	}
        	if (maladie3==true) {
        		listeMaladie.add(MainApp.getMapAllMaladies().get(3));
        	}
        	
        	if (listeMaladie!=null) {
        		MainApp.getUtilisateurActif().setListe_maladie_utilisateur(listeMaladie);
        	}
        	
        	if (listeUrl!= null && !listeUrl.isEmpty()) {
        		MainApp.getUtilisateurActif().setAvatar_utilisateur(Tools.sauvegarderImage(listeUrl));
        	}
        	
            mettreAJourUtilisateurDansBDD();
        }
    }


    public void mettreAJourUtilisateurDansBDD() {
    	
	        String updateQuery = "UPDATE utilisateur SET nom_utilisateur= ?, prenom_utilisateur = ?, mail_utilisateur = ?,"
	        		+ "pseudo_utilisateur = ?, avatar_utilisateur = ?, date_naissance_utilisateur = ?, sexe_utilisateur = ? WHERE id_utilisateur = ?";
	        
        try {
        	ConnectionDB db = new ConnectionDB();
	        	
	        db.initPreparedStatement(updateQuery);
	        db.getPrepareStatement().setString(1, MainApp.getUtilisateurActif().getNom_utilisateur());
	        db.getPrepareStatement().setString(2, MainApp.getUtilisateurActif().getPrenom_utilisateur());
	        db.getPrepareStatement().setString(3, MainApp.getUtilisateurActif().getMail_utilisateur());
	        db.getPrepareStatement().setString(4, MainApp.getUtilisateurActif().getPseudo_utilisateur());
	        db.getPrepareStatement().setString(5, MainApp.getUtilisateurActif().getAvatar_utilisateur());
	        db.getPrepareStatement().setString(6, MainApp.getUtilisateurActif().getDate_naissance_utilisateur());
	        db.getPrepareStatement().setString(7, ""+MainApp.getUtilisateurActif().getSexe_utilisateur()); //attention à ça
	        db.getPrepareStatement().setInt(8, MainApp.getUtilisateurActif().getId_utilisateur());
	        
	        System.out.println(db.getPrepareStatement());
	        db.getPrepareStatement().executeUpdate();
	        db.closeConnection();
	        }
	        catch (SQLException e) {
	        	
	        }
	        
	        String deleteQuery = "DELETE FROM declarer WHERE id_utilisateur = ? ";
	        try {
	        	ConnectionDB db = new ConnectionDB();
	        	db.initPreparedStatement(deleteQuery);
	        	
	        	db.getPrepareStatement().setInt(1, MainApp.getUtilisateurActif().getId_utilisateur());
	        	
	        	db.getPrepareStatement().executeUpdate();
		        db.closeConnection();
	        }
	        
	        catch (SQLException e) {
	        	
	        }
	        
	        String insertQuery = "INSERT INTO declarer (id_utilisateur, id_maladie) VALUES (?, ?)";
	        try {
	        	ConnectionDB db = new ConnectionDB(); //ca fait quoi si la liste est vide ? 
	        	
	        	
	        	
	        	for (int i=0; i<MainApp.getUtilisateurActif().getListe_maladie_utilisateur().size();i++) {
	        		db.initPreparedStatement(insertQuery);
	        		db.getPrepareStatement().setInt(1, MainApp.getUtilisateurActif().getId_utilisateur());
	     	        db.getPrepareStatement().setInt(2, MainApp.getUtilisateurActif().getListe_maladie_utilisateur().get(i).getIdMaladie());
	     	        System.out.println(db.getPrepareStatement());
	     	        db.getPrepareStatement().executeUpdate();
	     	        
	        	}
		        db.closeConnection();
	        }
	        
	        catch (SQLException e) {
	        	
	        }
	        
	        System.out.println("Utilisateur mis à jour en bdd");
    }

    
	public void supprimerUtilisateur(Stage stage) {
		 	ConnectionDB db = new ConnectionDB();
	        String query = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
	        
	        db.initPreparedStatement(query);
	        try {
	            db.getPrepareStatement().setInt(1, MainApp.getUtilisateurActif().getId_utilisateur());
	            
	            int rowsUpdated = db.getPrepareStatement().executeUpdate();
	            
	            if (rowsUpdated > 0) {
	                System.out.println("Suppression réussie de l'utilisateur.");
	                new ControllerToolBar().seDeconnecter();
	                stage.close();
	            } else {
	                System.out.println("Suppression échouée de l'utilisateur.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
	        }
	}
	
	public String modifierMotDePasse(String modifMDP, String confirmModifMDP) {
		if (modifMDP.equals(confirmModifMDP) && Tools.isPasswordValid(modifMDP)) {
			String mdpHashed = BCrypt.hashpw(modifMDP, BCrypt.gensalt(12));
			
			try {
				String query = "UPDATE utilisateur SET mdp_utilisateur=? WHERE id_utilisateur = ?";
				ConnectionDB db = new ConnectionDB();
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setString(1, mdpHashed);
		        db.getPrepareStatement().setInt(2, MainApp.getUtilisateurActif().getId_utilisateur());

		        int rowsUpdated = db.getPrepareStatement().executeUpdate();
		        if (rowsUpdated > 0) {
					db.closeConnection();
					return "La modification a été enregistrée.";
		        }else {
					db.closeConnection();
					return "Erreur : Lors de l'enregistrement de la modification.";
		        }
			}catch(Exception e) {
				System.out.println("Changement mot de passe : "+e);
				return "Erreur : La modification n'a pas été enregistrée.";
				
			}
			
		}
		
		return "Les mots de passe ne sont pas identiques ou ne correspondent pas au format attendu.";
	}
	
}


