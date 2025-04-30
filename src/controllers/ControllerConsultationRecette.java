package controllers;

import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import main.ConnectionDB;
import main.MainApp;
import models.Commentaire;
import models.Recette;
import views.VueConsultationRecette;
import views.VueModificationRecette;

public class ControllerConsultationRecette {
	private Recette recette;

	public ControllerConsultationRecette(Recette recette) {
		this.recette = recette;
		
	}
	
	public void afficherVueModificationRecette() {
		//Controle avant d'ouvrir la page
		if (MainApp.getUtilisateurActif().getId_utilisateur() == recette.getId_utilisateur_createur_recette()) {
			new VueModificationRecette(recette);
		}
	}
	
    public void afficherSignalerRecette(Recette recette) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir signaler cette recette ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            signalerRecette();
        }
    }
	
	public void signalerCommentaire(Commentaire commentaire) {
		
		String query = "UPDATE commentaire SET statut_moderation_commentaire = false "
				+ "WHERE id_commentaire = ?;";
		
		try {
		 	// CONNECTION A LA BDD
			ConnectionDB db = new ConnectionDB();
			
			// INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
	        db.initPreparedStatement(query);        
	        
	        
	        db.getPrepareStatement().setInt(1, commentaire.getIdCommentaire());
	        
		        int rowsAffected = db.getPrepareStatement().executeUpdate();
		        if (rowsAffected > 0) {	
		        	System.out.println("Commentaire signalé avec succès !");
		        	
		        	// MODIFICATION EN LOCAL DES VALEURS DE L'OBJET COMMENTAIRE
		        	System.out.println("modification des valeurs en local");
		        	commentaire.setStatutModeration(false);
		        	MainApp.getCommentairesNonModeres().add(commentaire);
		        	
		        } else {
	        	System.out.println("Aucune modification réalisée en BDD !");
	        }
		    // FERMETURE DE LA CONNEXION A LA BDD
	        db.closeConnection();
		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour de la recette : " + e.getMessage());
		}
		new ControllerAccueil(recette).afficherVueConsultationRecette();
	}
		
	public void supprimerCommentaire(int id_commentaire) {
		
		String query = "DELETE FROM commentaire "
				+ "WHERE id_commentaire = ?;";
		try {
			 	ConnectionDB db = new ConnectionDB();
		        db.initPreparedStatement(query);
		        
		        db.getPrepareStatement().setInt(1, id_commentaire);

		        int rowsDelated = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsDelated > 0) {
		        	System.out.println("Commentaire supprimé avec succès !");
		        			        	
		        } else {
		        	System.out.println("Aucune modification réalisée en BDD !");		        }
		        
		        db.closeConnection();
		        
		    } catch (SQLException e) {
		    	System.err.println("Erreur lors de la suppression du commentaire " + e.getMessage());
		    }
			new ControllerAccueil(recette).afficherVueConsultationRecette();
	}
    
    public void signalerRecette() {
        String req = "UPDATE recette SET statut_moderation_recette = 0 WHERE id_recette = ?";

        try {
            ConnectionDB db = new ConnectionDB();
            db.initPreparedStatement(req);
            db.getPrepareStatement().setInt(1, this.recette.getId_recette());
            int rowsUpdated = db.getPrepareStatement().executeUpdate();

            if (rowsUpdated > 0) {
                // Affichage des tailles avant les opérations
                System.out.println("Taille de MapRecettes avant signalement : " + MainApp.getMapRecettes().size());
                System.out.println("Taille de RecettesFiltre avant signalement : " + MainApp.getRecettesFiltre().size());
                System.out.println("Taille de RecetteNonModeres avant ajout : " + MainApp.getRecetteNonModeres().size());

                // Suppression de la recette de MapRecettes
                MainApp.getMapRecettes().remove(this.recette.getId_recette());
                MainApp.getRecettesFiltre().remove(this.recette.getId_recette());

                // Ajout de la recette à recetteNonModeres
                MainApp.getRecetteNonModeres().add(this.recette);

                // Affichage des tailles après les opérations
                System.out.println("Taille de MapRecettes après suppression : " + MainApp.getMapRecettes().size());
                System.out.println("Taille de RecettesFiltre après suppression : " + MainApp.getRecettesFiltre().size());
                System.out.println("Taille de RecetteNonModeres après ajout : " + MainApp.getRecetteNonModeres().size());

                // Vérifier si la recette a bien été supprimée de MapRecettes et RecettesFiltre
                if (!MainApp.getMapRecettes().containsKey(recette.getId_recette())) {
                    System.out.println("Recette supprimée de MapRecettes.");
                } else {
                    System.out.println("Erreur : Recette non supprimée de MapRecettes.");
                }

                if (!MainApp.getRecettesFiltre().containsKey(this.recette.getId_recette())) {
                    System.out.println("Recette supprimée de RecettesFiltre.");
                } else {
                    System.out.println("Erreur : Recette non supprimée de RecettesFiltre.");
                }

                // Vérifier si la recette a bien été ajoutée à RecetteNonModeres
                if (MainApp.getRecetteNonModeres().contains(recette)) {
                    System.out.println("Recette ajoutée à RecetteNonModeres.");
                } else {
                    System.out.println("Erreur : Recette non ajoutée à RecetteNonModeres.");
                }

                JOptionPane.showMessageDialog(null, "Recette signalée avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur : La recette n'a pas pu être signalée.");
            }

            db.closeConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du statut de la recette : " + e.getMessage());
        }
    }


    public String envoyerCommentaire(String commentaire) {
        // Controle du message avant l'envoi en BDD
        commentaire = commentaire.replaceAll("--", " ");
        if (commentaire.length() > 500) {
            commentaire = commentaire.substring(0, 500);
        }
        
        String req = "INSERT INTO `commentaire` (`id_commentaire`, `text_commentaire`, `statut_moderation_commentaire`, `id_recette`, `id_utilisateur`, `date_publi_commentaire`) "
                + "VALUES (NULL, ?, '1', ?, ?, CURDATE());";
        String message = "";
        
        try {
            ConnectionDB db = new ConnectionDB();
            db.initPreparedStatement(req);
            db.getPrepareStatement().setString(1, commentaire);
            db.getPrepareStatement().setInt(2, this.recette.getId_recette());
            db.getPrepareStatement().setInt(3, MainApp.getUtilisateurActif().getId_utilisateur());
            
            int rowsUpdated = db.getPrepareStatement().executeUpdate();
            
            if (rowsUpdated > 0) {
                message = "Commentaire envoyé avec succès !";
            } else {
                message = "Aucun commentaire n'a été ajouté.";
            }
            
            db.closeConnection();
        } catch (SQLException e) {
            message = "Erreur lors de la mise à jour du statut du commentaire : " + e.getMessage();
            System.out.println(message);
        }
        return message;
    }
    
    public void supprimerRecette(int id_recette) {
		
		retraitFavori();
		try {
			 	ConnectionDB db = new ConnectionDB();
			 	
			 	String queryRI = "DELETE FROM recette_ingredient WHERE id_recette = ?";
				db.initPreparedStatement(queryRI);
				db.getPrepareStatement().setInt(1, id_recette);
				db.getPrepareStatement().executeUpdate();
				
				String query = "DELETE FROM recette WHERE id_recette = ?;";
		        db.initPreparedStatement(query);
		        db.getPrepareStatement().setInt(1, id_recette);
		        int rowsDelated = db.getPrepareStatement().executeUpdate();
		        
		        if (rowsDelated > 0) {
		        	//Suppression de la liste de mes recettes
		        	for (int i = MainApp.getUtilisateurActif().getListeMesRecettes().size() - 1; i >= 0; i--) {
		        	    Recette r = MainApp.getUtilisateurActif().getListeMesRecettes().get(i);
		        	    if (r.getId_recette() == id_recette) {
		        	    	MainApp.getUtilisateurActif().getListeMesRecettes().remove(i);
		        	    }
		        	}
		        	
		        	Iterator<Recette> parcourMapRecettes = MainApp.getMapRecettes().values().iterator();
		        	while (parcourMapRecettes.hasNext()) {
		        	    Recette r = parcourMapRecettes.next();
		        	    if (r != null && r.getId_recette() == id_recette) {
		        	    	parcourMapRecettes.remove();
		        	    }
		        	}
		        	
		        	//Suppression de map des recettes filtrées
		        	Iterator<Recette> parcourMapRecettesFiltre = MainApp.getRecettesFiltre().values().iterator();
		        	while (parcourMapRecettesFiltre.hasNext()) {
		        	    Recette r = parcourMapRecettesFiltre.next();
		        	    if (r != null && r.getId_recette() == id_recette) {
		        	    	parcourMapRecettesFiltre.remove();
		        	    }
		        	}
		        	
		        	//Suppression de la liste des recettes favorites
		        	for (int i = MainApp.getUtilisateurActif().getListe_favori_utilisateur().size() - 1; i >= 0; i--) {
		        	    Recette r = MainApp.getUtilisateurActif().getListe_favori_utilisateur().get(i);
		        	    if (r.getId_recette() == id_recette) {
		        	    	MainApp.getUtilisateurActif().getListe_favori_utilisateur().remove(i);
		        	    }
		        	}
		        	
		        	System.out.println("Recette supprimée avec succès !");
		        	
		        	
		        			        	
		        } else {
		        	System.out.println("Aucune modification réalisée en BDD !");		        
		        
		        }
		        
		        db.closeConnection();
		        
		    } catch (SQLException e) {
		    	System.err.println("Erreur lors de la suppression de la recette " + e.getMessage());
		    }

		new ControllerToolBar().afficherMesRecettes();	
    }

	public void ajoutFavori() {

		ConnectionDB db = new ConnectionDB();
		String queryRI = "INSERT INTO favori SET id_utilisateur = ?, id_recette = ?;";

		try {
			
			// INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
	        db.initPreparedStatement(queryRI);        
	        db.getPrepareStatement().setFloat(1, MainApp.getUtilisateurActif().getId_utilisateur());
	        db.getPrepareStatement().setFloat(2, this.recette.getId_recette());
	        int lignesAffecte = db.getPrepareStatement().executeUpdate();
	        
	        if (lignesAffecte > 0) {
	    		MainApp.getUtilisateurActif().getListe_favori_utilisateur().add(this.recette);

	        }else {
	        	System.out.println("Erreur lors de la mise à jour de la base de donnée : favori");
	        }

		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour des favoris : " + e.getMessage());
			System.out.println("Aucune modification réalisée, échec de l'enregistrement des favoris !");
		}
        db.closeConnection();
        new VueConsultationRecette(this.recette,Tools.chargerCommentaire(this.recette));
	}

	public void retraitFavori() {
		ConnectionDB db = new ConnectionDB();
		String queryRI = "DELETE FROM favori WHERE id_utilisateur = ? AND id_recette = ?;";
		try {
			
			// INITIALISE UN PREPAREDSTATEMENT AVEC UNE REQUETE SQL
	        db.initPreparedStatement(queryRI);        
	        db.getPrepareStatement().setFloat(1, MainApp.getUtilisateurActif().getId_utilisateur());
	        db.getPrepareStatement().setFloat(2, this.recette.getId_recette());
	        int lignesAffecte = db.getPrepareStatement().executeUpdate();
	        
	        if (lignesAffecte > 0) {
	        	MainApp.getUtilisateurActif().getListe_favori_utilisateur().remove(this.recette);
	        	System.out.println("Recette retirée des favoris.");

	        }else {
	        	System.out.println("Erreur lors de la mise à jour de la base de donnée : favori");
	        }

	        
		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour des favoris : " + e.getMessage());
			System.out.println("Aucune modification réalisée, échec de l'enregistrement des favoris !");
		}
        db.closeConnection();
        new VueConsultationRecette(this.recette,Tools.chargerCommentaire(this.recette));

	}    
}

