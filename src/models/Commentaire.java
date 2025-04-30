package models;

public class Commentaire {
    private int idCommentaire;
    private String textCommentaire;
    private boolean statutModeration;
    private Recette recette;
    private Utilisateur utilisateur;
    private String datePubliCommentaire;

    public Commentaire(int idCommentaire, String textCommentaire, boolean statutModeration,
                       Recette recette, Utilisateur utilisateur, String datePubliCommentaire) {
        this.idCommentaire = idCommentaire;
        this.textCommentaire = textCommentaire;
        this.statutModeration = statutModeration;
        this.recette = recette;
        this.utilisateur = utilisateur;
        this.datePubliCommentaire = datePubliCommentaire;
    }
    
    public void signalerCommentaire() {
    	this.statutModeration = false;
        System.out.println("Le commentaire a été signalé.");
    }
    
    public void afficherCommentaireAModerer() {
    	if (!statutModeration) {
            System.out.println("Commentaire à modérer : " + textCommentaire);
        } else {
            System.out.println("Aucun commentaire à modérer.");
        }
    }
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Commentaire [idCommentaire=").append(idCommentaire).append(", textCommentaire=")
				.append(textCommentaire).append(", statutModeration=").append(statutModeration).append(", recette=")
				.append(recette).append(", utilisateur=").append(utilisateur).append(", datePubliCommentaire=")
				.append(datePubliCommentaire).append("]");
		return builder.toString();
	}
	public int getIdCommentaire() {
		return idCommentaire;
	}
	public void setIdCommentaire(int idCommentaire) {
		this.idCommentaire = idCommentaire;
	}
	public String getTextCommentaire() {
		return textCommentaire;
	}
	public void setTextCommentaire(String textCommentaire) {
		this.textCommentaire = textCommentaire;
	}
	public boolean isStatutModeration() {
		return statutModeration;
	}
	public void setStatutModeration(boolean statutModeration) {
		this.statutModeration = statutModeration;
	}
	public Recette getRecette() {
		return recette;
	}
	public void setRecette(Recette recette) {
		this.recette = recette;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public String getDatePubliCommentaire() {
		return datePubliCommentaire;
	}
	public void setDatePubliCommentaire(String datePubliCommentaire) {
		this.datePubliCommentaire = datePubliCommentaire;
	}
    
   
}
