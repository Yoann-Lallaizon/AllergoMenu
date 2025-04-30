package models;

public class Ingredient {
	
	private int id_Ingredient;
	private String nom_Ingredient;
	private String categorie_Ingredient;
	private String sousCategorie_Ingredient;
	
	// constructeur
	
	public Ingredient(int id_Ingredient, String nom_Ingredient, String categorie_Ingredient, String sousCategorie_Ingredient) {
		this.id_Ingredient = id_Ingredient;
		this.nom_Ingredient = nom_Ingredient;
		this.categorie_Ingredient = categorie_Ingredient;
		this.sousCategorie_Ingredient = sousCategorie_Ingredient;
	}

	
	// Getters et Setters
	
    public int getId_Ingredient() {
		return id_Ingredient;
	}


	public void setId_Ingredient(int id_Ingredient) {
		this.id_Ingredient = id_Ingredient;
	}


	public String getNomIngredient() {
        return nom_Ingredient;
    }

    public void setNomIngredient(String nomIngredient) {
        this.nom_Ingredient = nomIngredient;
    }

    public String getCategorieIngredient() {
        return categorie_Ingredient;
    }
    public void setCategorieIngredient(String categorieIngredient) {
        this.categorie_Ingredient = categorieIngredient;
    }

    public String getSousCategorieIngredient() {
        return sousCategorie_Ingredient;
    }

    public void setSousCategorieIngredient(String sousCategorieIngredient) {
        this.sousCategorie_Ingredient = sousCategorieIngredient;
    }
    
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Ingredient [id_Ingredient=").append(id_Ingredient).append(", nom_Ingredient=")
				.append(nom_Ingredient).append(", categorie_Ingredient=").append(categorie_Ingredient)
				.append(", sousCategorie_Ingredient=").append(sousCategorie_Ingredient).append("]");
		return builder.toString();
	}
}
