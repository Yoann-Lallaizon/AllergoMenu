package models;

import java.util.*;

public class Maladie {
	private int idMaladie;
	private String nom_Maladie;
	private String caracteristique_Maladie;
	private ArrayList<Ingredient> liste_Ingredient;
	
	// constructeurs
	
	public Maladie(int idMaladie, String nom_Maladie, String caracteristique_Maladie) {
	    this.idMaladie = idMaladie;
		this.nom_Maladie = nom_Maladie;
		this.caracteristique_Maladie = caracteristique_Maladie;
		this.liste_Ingredient = new ArrayList<>();
	}
	// getters et setters
	
	public String getNom_Maladie() {
		return nom_Maladie;
	}
	public void setNom_Maladie(String nom_Maladie) {
		this.nom_Maladie = nom_Maladie;
	}
	public String getCaracteristique_Maladie() {
        return caracteristique_Maladie;
    }
	public void setCaracteristique_Maladie(String caracteristique_Maladie) {
        this.caracteristique_Maladie = caracteristique_Maladie;
    }
	public ArrayList<Ingredient> getListe_Ingredient() {
        return liste_Ingredient;
    }

    public void setListe_Ingredient(ArrayList<Ingredient> liste_Ingredient) {
        this.liste_Ingredient = liste_Ingredient;
    }
    
    public int getIdMaladie() {
        return idMaladie;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Maladie [idMaladie=").append(idMaladie).append(", nom_Maladie=").append(nom_Maladie)
				.append(", caracteristique_Maladie=").append(caracteristique_Maladie).append(", liste_Ingredient=")
				.append(liste_Ingredient).append("]");
		return builder.toString();
	}
}
