package models;

import java.util.ArrayList;
import java.util.List;

public class Recette {
	
	//Déclaration des attributs
	private int id_recette;
	private String nom_recette;
	private boolean statut_moderation_recette;
	private int temps_preparation;
	private int temps_cuisson;
	private int temps_total_recette;
	private String difficulte_recette;
	private int nb_personne_recette;
	private String prix_recette;
	private String etape_recette;
	private String nom_categorie_recette;
	private String photo_recette;
	private int id_utilisateur_createur_recette;
	private ArrayList <RecetteIngredient> listeIngredientsComplets;
	
	public Recette(int id_recette, String nom_recette, boolean statut_moderation_recette, int temps_preparation,
				int temps_cuisson, String difficulte_recette, int nb_personne_recette,
				String prix_recette, String etape_recette, String nom_categorie_recette,
				String photo_recette, int id_utilisateur_createur_recette, List<RecetteIngredient> listeIngredientsComplets) {
	
			this.id_recette = id_recette;
			this.nom_recette = nom_recette;
			this.statut_moderation_recette = statut_moderation_recette;
			this.temps_preparation = temps_preparation;
			this.temps_cuisson = temps_cuisson;
			this.temps_total_recette = temps_preparation+temps_cuisson;
			this.difficulte_recette = difficulte_recette;
			this.nb_personne_recette = nb_personne_recette;
			this.prix_recette = prix_recette;
			this.etape_recette = etape_recette;
			this.nom_categorie_recette = nom_categorie_recette;
			this.photo_recette = photo_recette;
			this.id_utilisateur_createur_recette = id_utilisateur_createur_recette;
			this.listeIngredientsComplets = new ArrayList<>();

		}
	
	public List<RecetteIngredient> getListeIngredientsComplets() {
	    return listeIngredientsComplets;
	}


	//Getters and Setters
	
	public String getEtape_recette() {
		return etape_recette;
	}

	public void setEtape_recette(String etape_recette) {
		this.etape_recette = etape_recette;
	}

	public Boolean getStatut_moderation_recette() {
		return statut_moderation_recette;
	}

	public void setStatut_moderation_recette(Boolean statut_moderation_recette) {
		this.statut_moderation_recette = statut_moderation_recette;
	}

	public int getTemps_cuisson() {
		return temps_cuisson;
	}

	public void setTemps_cuisson(int temps_cuisson) {
		this.temps_cuisson = temps_cuisson;
	}

	public String getDifficulte_recette() {
		return difficulte_recette;
	}

	public void setDifficulte_recette(String difficulte_recette) {
		this.difficulte_recette = difficulte_recette;
	}

	public int getNb_personne_recette() {
		return nb_personne_recette;
	}

	public void setNb_personne_recette(int nb_personne_recette) {
		this.nb_personne_recette = nb_personne_recette;
	}

	public String getNom_recette() {
		return nom_recette;
	}

	public void setNom_recette(String nom_recette) {
		this.nom_recette = nom_recette;
	}

	public String getPhoto_recette() {
		return photo_recette;
	}

	public void setPhoto_recette(String photo_recette) {
		this.photo_recette = photo_recette;
	}

	public int getTemps_total_recette() {
		return temps_total_recette;
	}

	public String getPrix_recette() {
		return prix_recette;
	}
	
//Méthodes
	
	//Calculer le temps total d'une recette
	public int calculerTempsTotalRecette () {
		this.temps_total_recette=this.temps_preparation+this.temps_cuisson;
		return temps_total_recette;
	}
	
	//signaler recette
	public void signalerRecette() {
		this.statut_moderation_recette=false;
	}

	public int getId_utilisateur_createur_recette() {
		return id_utilisateur_createur_recette;
	}

	public void setId_utilisateur_createur_recette(int id_utilisateur_createur_recette) {
		this.id_utilisateur_createur_recette = id_utilisateur_createur_recette;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Recette [id_recette=").append(id_recette).append(", nom_recette=").append(nom_recette)
				.append(", statut_moderation_recette=").append(statut_moderation_recette).append(", temps_preparation=")
				.append(temps_preparation).append(", temps_cuisson=").append(temps_cuisson)
				.append(", temps_total_recette=").append(temps_total_recette).append(", difficulte_recette=")
				.append(difficulte_recette).append(", nb_personne_recette=").append(nb_personne_recette)
				.append(", prix_recette=").append(prix_recette).append(", etape_recette=").append(etape_recette)
				.append(", nom_categorie_recette=").append(nom_categorie_recette).append(", photo_recette=")
				.append(photo_recette).append(", id_utilisateur_createur_recette=")
				.append(id_utilisateur_createur_recette).append(", listeIngredientsComplets=")
				.append(listeIngredientsComplets).append("]");
		return builder.toString();
	}

	public int getId_recette() {
		return id_recette;
	}

	public void setId_recette(int id_recette) {
		this.id_recette = id_recette;
	}

	public int getTemps_preparation() {
		return temps_preparation;
	}

	public void setTemps_preparation(int temps_preparation) {
		this.temps_preparation = temps_preparation;
	}

	public String getNom_categorie_recette() {
		return nom_categorie_recette;
	}

	public void setNom_categorie_recette(String nom_categorie_recette) {
		this.nom_categorie_recette = nom_categorie_recette;
	}

	public void setStatut_moderation_recette(boolean statut_moderation_recette) {
		this.statut_moderation_recette = statut_moderation_recette;
	}

	public void setTemps_total_recette(int temps_total_recette) {
		this.temps_total_recette = temps_total_recette;
	}

	public void setPrix_recette(String prix_recette) {
		this.prix_recette = prix_recette;
	}

	public void setListeIngredientsComplets(ArrayList<RecetteIngredient> listeIngredientsComplets) {
		this.listeIngredientsComplets = listeIngredientsComplets;
	}
}