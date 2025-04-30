package models;

import java.time.*;
import java.util.ArrayList;

public class Utilisateur {
	
	/* attributs */
	private int id_utilisateur;
	private String nom_utilisateur;
	private String prenom_utilisateur;
	private String mail_utilisateur;
	private String pseudo_utilisateur;
	private String avatar_utilisateur;
	private String date_inscription_utilisateur;
	private OffsetDateTime date_derniere_connexion_utilisateur; /*ZonedDateTime such as 2007-12-03T10:15:30+01:00 Europe/Paris. */
	private char sexe_utilisateur;
	private String date_naissance_utilisateur;
	private String nom_role_utilisateur;
	private String nom_famille_utilisateur;
	private ArrayList<Maladie> liste_maladie_utilisateur;
	private ArrayList<Recette> liste_favori_utilisateur;
	private ArrayList<Recette> listeMesRecettes;
	
	
	/* Constructeurs */
	
	public Utilisateur(int id_utilisateur,String nom_utilisateur,String prenom_utilisateur, String mail_utilisateur,
			String pseudo_utilisateur,String avatar_utilisateur, String date_inscription_utilisateur, OffsetDateTime date_derniere_connexion_utilisateur, 
			char sexe_utilisateur, String date_naissance_utilisateur,ArrayList<Maladie> liste_maladie_utilisateur,
			String nom_role_utilisateur, String nom_famille_utilisateur, ArrayList<Recette> liste_favori_utilisateur, 
			ArrayList<Recette> liste_mesRecettes_utilisateur){
		
	    this.id_utilisateur = id_utilisateur; 
		this.nom_utilisateur = nom_utilisateur;
		this.prenom_utilisateur = prenom_utilisateur;
		this.mail_utilisateur = mail_utilisateur;
		this.pseudo_utilisateur = pseudo_utilisateur;
		this.avatar_utilisateur = avatar_utilisateur;
		this.date_inscription_utilisateur = date_inscription_utilisateur;
		this.date_derniere_connexion_utilisateur = date_derniere_connexion_utilisateur;
		this.sexe_utilisateur = sexe_utilisateur;
		this.nom_role_utilisateur = nom_role_utilisateur;
		this.nom_famille_utilisateur = nom_famille_utilisateur;
		this.date_naissance_utilisateur=date_naissance_utilisateur;
		this.liste_maladie_utilisateur = new ArrayList<>();
		this.liste_favori_utilisateur=liste_favori_utilisateur;
	    this.listeMesRecettes = liste_mesRecettes_utilisateur;
	}
		
	/* getter setter */	
	 
	public ArrayList<Recette> getListeMesRecettes() {
		return listeMesRecettes;
	}

	public void setListeMesRecettes(ArrayList<Recette> listeMesRecettes) {
		this.listeMesRecettes = listeMesRecettes;
	}

	/* Getters publics */
    public int getId_utilisateur() {
        return id_utilisateur;
    }
    
    public String getNom_utilisateur() {
        return nom_utilisateur;
    }
    
    public String getPrenom_utilisateur() {
        return prenom_utilisateur;
    }
    
    public String getAvatar_utilisateur() {
    	return avatar_utilisateur;
    }
    
    public String getMail_utilisateur() {
        return mail_utilisateur;
    }
    
    public String getPseudo_utilisateur() {
        return pseudo_utilisateur;
    }
    
    public String getRole_utilisateur() {
        return nom_role_utilisateur;
    }
    
    public char getSexe_utilisateur() {
    	return sexe_utilisateur;
    }
    
    public ArrayList<Recette> getListe_favori_utilisateur(){
    	return liste_favori_utilisateur;
    }
    
    public ArrayList<Maladie> getListe_maladie_utilisateur() {
		return liste_maladie_utilisateur;
	}
    
    public String getDate_naissance_utilisateur() {
        return date_naissance_utilisateur;
    }
    
    //SETTERS
    
    public void setNom_utilisateur(String nom_utilisateur) {
    	this.nom_utilisateur=nom_utilisateur;
    }
    
    public void setPrenom_utilisateur(String prenom_utilisateur) {
    	this.prenom_utilisateur=prenom_utilisateur;
    }
    
    public void setMail_utilisateur(String mail_utilisateur) {
    	this.mail_utilisateur=mail_utilisateur;
    }
    
    public void setDate_naissance_utilisateur(String date_naissance_utilisateur) {
    	this.date_naissance_utilisateur=date_naissance_utilisateur;
    }
    
    public void setRole_utilisateur(String role_utilisateur) {
        this.nom_role_utilisateur = role_utilisateur;
    }
    
    public void setListe_favori_utilisateur(ArrayList<Recette> liste) {
    	this.liste_favori_utilisateur=liste;
    }
    
    public void setAvatar_utilisateur(String nomPhoto) {
    	this.avatar_utilisateur=nomPhoto;
    }
    
    public void setSexe_utilisateur(char sexe) {
    	this.sexe_utilisateur= sexe;
    }
    
    public void setListeMaladies(ArrayList<Maladie> listeMaladies) {
        this.liste_maladie_utilisateur = listeMaladies;
    }
    
    public void setPseudo_utilisateur(String pseudo_utilisateur) {
		this.pseudo_utilisateur = pseudo_utilisateur;
	}
    
    public void setListe_maladie_utilisateur(ArrayList<Maladie> liste_maladie_utilisateur) {
		this.liste_maladie_utilisateur = liste_maladie_utilisateur;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur [id_utilisateur=").append(id_utilisateur).append(", nom_utilisateur=")
				.append(nom_utilisateur).append(", prenom_utilisateur=").append(prenom_utilisateur)
				.append(", mail_utilisateur=").append(mail_utilisateur).append(", pseudo_utilisateur=").append(pseudo_utilisateur)
				.append(", avatar_utilisateur=").append(avatar_utilisateur).append(", date_inscription_utilisateur=")
				.append(date_inscription_utilisateur).append(", date_derniere_connexion_utilisateur=")
				.append(date_derniere_connexion_utilisateur).append(", sexe_utilisateur=").append(sexe_utilisateur)
				.append(", date_naissance_utilisateur=").append(date_naissance_utilisateur)
				.append(", nom_role_utilisateur=").append(nom_role_utilisateur).append(", nom_famille_utilisateur=")
				.append(nom_famille_utilisateur).append(", liste_maladie_utilisateur=")
				.append(liste_maladie_utilisateur).append("]");
		return builder.toString();
	}
}
