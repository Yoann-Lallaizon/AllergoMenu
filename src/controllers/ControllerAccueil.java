package controllers;

import models.Recette;
import views.VueConsultationRecette;
import views.VueGenererMenu;

public class ControllerAccueil {
	
	//ATTRIBUTS
	private Recette recette;
	
	//CONSTRUCTEUR
    public ControllerAccueil(Recette recette) {
        this.recette = recette;
    }
    
    //AUTRES METHODES
    public void afficherVueConsultationRecette() {
    	new VueConsultationRecette(this.recette, Tools.chargerCommentaire(this.recette));
	}
    
	public void afficherVueGenererMenu() {
    	new VueGenererMenu();
    }
}
