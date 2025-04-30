package models;

public class RecetteIngredient {
    private Recette recette; // L'objet Recette
    private Ingredient ingredient; // L'objet Ingredient
    private float quantite; // Quantité d'ingrédient pour cette recette
    private String unite; // Unité associée (grammes, litres, etc.)

    // Constructeur
    public RecetteIngredient(Recette recette, Ingredient ingredient, float quantite, String unite) {
        this.recette = recette;
        this.ingredient = ingredient;
        this.quantite = quantite;
        this.unite = unite;
    }
    
    // Getters et setters
    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "Ingrédient : " + ingredient.getNomIngredient() + ", Quantité : " + quantite + " " + unite;
    }
}
