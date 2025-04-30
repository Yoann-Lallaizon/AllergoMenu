package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.MainApp;
import models.Maladie;
import models.Recette;
import models.RecetteIngredient;
import models.Utilisateur;
import views.VueAccueil;
import views.VueConnexion;
import views.VueCreerRecette;
import views.VueGererModerateur;
import views.VueListeRecette;
import views.VueModererCommentaire;
import views.VueModererRecette;
import views.VueParametreCompte;

public class ControllerToolBar {

	
	
    private static Utilisateur utilisateurActif;
    private static Map<Integer, Recette> filteredRecettes;
    private static ArrayList<Maladie> etatMaladiesSelectionne;
    private static ArrayList <Recette> pickThree;

    public ControllerToolBar() {
        this.utilisateurActif = MainApp.getUtilisateurActif();
        // Par défaut, les filtres sont ceux de l'utilisateur actif
        this.etatMaladiesSelectionne = new ArrayList<>(utilisateurActif.getListe_maladie_utilisateur());
        this.filteredRecettes = MainApp.getRecettesFiltre();
       // this.pickThree=new ArrayList();
    }

    //ACCESSEURS
    public static Map<Integer, Recette> getFilteredRecettes() {
        return filteredRecettes;
    }
    
    public static ArrayList<Recette> getPickThree(){
  		return pickThree;
  	}
    
    //AUTRES METHODES
    public void afficherModererCommentaire(){
    	new VueModererCommentaire();
    }
    
    public void afficherModererRecette(){
    	new VueModererRecette();
    }
    
    public void afficherGererModerateur() {
    	new VueGererModerateur();
    }
    
    public void afficherAccueil() {
    	tirerTroisRecettes(tirerIdFiltre());
    	new VueAccueil();
    }
    
    public void afficherCreerRecette() {
    	new VueCreerRecette();
    }
    
    public void afficherFavoris(){
    	new VueListeRecette(MainApp.getUtilisateurActif().getListe_favori_utilisateur(),0);
    }
    
    public void afficherMesRecettes(){
    	new VueListeRecette(MainApp.getUtilisateurActif().getListeMesRecettes(),1);
    }
    
    public void afficherParametreCompte() {
    	new VueParametreCompte();
    }
    
    public void seDeconnecter() {
    	MainApp.setMapAllMaladies(null);
    	MainApp.setMapRecettes(null);
        MainApp.setRecettesFiltre(null);
        MainApp.setMapIngredients(null);
        MainApp.setCommentairesNonModeres(null);
        MainApp.setRecetteNonModeres(null);
        MainApp.setAllUtilisateurs(null);
        MainApp.setUtilisateurNonAdmin(null);
        MainApp.setMaToolBar(null);
        System.gc();
        VueConnexion vueConnexion = new VueConnexion(); //Création de la vue connexion
        vueConnexion.afficherConnexion();

    }
    
    public static void applyFiltrage(ArrayList<Maladie> selectedMaladies) {
        etatMaladiesSelectionne = selectedMaladies;
        System.out.println("SELECTED MALADIES : "+selectedMaladies);
        if(selectedMaladies.isEmpty()) {
        	filteredRecettes=MainApp.getMapRecettes();
        }else {
	        Map<Integer, Recette> filtered = new HashMap<>();
	        for (Map.Entry<Integer, Recette> entry : MainApp.getMapRecettes().entrySet()) {
	            Recette rec = entry.getValue();
	            if (Tools.recetteCompatibleAvecMaladies(rec, selectedMaladies)) {
	                filtered.put(entry.getKey(), rec);
	            }
	        }
	        filteredRecettes = filtered;
        }
        
        System.out.println("Filtres appliqués, nombre de recettes filtrées : " + filteredRecettes.size());
    }
    
    public static void resetFiltrage() {
        etatMaladiesSelectionne = utilisateurActif.getListe_maladie_utilisateur();
        filteredRecettes = MainApp.getRecettesFiltre();
        // Relance la recherche dans la vue d'accueil
        System.out.println("Filtres réinitialisés, les recettes filtrés pour l'utilisateur sont affichées.");
    }

    public static ArrayList<Recette> rechercherRecettes(String query) {
        ArrayList<Recette> results = new ArrayList<>();
        // Si la requête est vide, renvoyer toutes les recettes filtrées
        if (query == null || query.trim().isEmpty()) {
            results.addAll(filteredRecettes.values());
            return results;
        }
        
        //Pour chacune des recettes on regarde si la recherche est dans le nom d'une recette ou sinon s'il est dans le nom d'un ingrédient de la recette
        String lowerQuery = query.toLowerCase();
        for (Recette rec : filteredRecettes.values()) {
            if (rec.getNom_recette().toLowerCase().contains(lowerQuery)) {
                results.add(rec);
            }else {
	            for(RecetteIngredient ri : rec.getListeIngredientsComplets()) {
	            	if(ri.getIngredient().getNomIngredient().toLowerCase().contains(lowerQuery)) {
	            		results.add(rec);
	            	}
	            }
            }
        }
        return results;
    }

    
  //Récupérer une liste de tous les id de la map recette. A passer dans le controleur
  	public ArrayList<Integer> tirerIdFiltre() { 
  		ArrayList<Integer> idFiltre = new ArrayList<>();
  		for (Map.Entry<Integer, Recette> entry : MainApp.getMapRecettes().entrySet()) {
              Recette recette = entry.getValue();
              idFiltre.add(recette.getId_recette());
  		}
  		return idFiltre;
  	}
  	
  //crée une ArrayListe de trois recettes aléatoires à partier la Map recettesFiltre et de la liste des id. A passer dans le controleur
  	public void tirerTroisRecettes(ArrayList<Integer> liste) {
  		if (this.pickThree==null) {
  			this.pickThree=new ArrayList<>();
  		}
  		if (liste.size()>=3) {
	  		for (int i=1;i<=3;i++) {
	  			int taille=liste.size();
	  			System.out.println(liste);
	  			
	  			int random = (int) (taille * Math.random());
	  			System.out.println("random = "+random);
	  			int id=liste.get(random);
	  			System.out.println("index = "+id);
	  			ControllerToolBar.pickThree.add(MainApp.getMapRecettes().get(id));
	  			liste.remove(random);
	  			System.out.println("-----------------------------------------------");
	  		}
  		}else if(liste.size()>0 && liste.size()<3) {
  			for (int i=0;i<3;i++) {
	  			
  				ControllerToolBar.pickThree.add(MainApp.getMapRecettes().get(liste.get(i%liste.size())));
	  			System.out.println("-------------------------------------------------");
	  			System.out.println("Moins de 3 recettes correspondantes trouvées");
	  		}
  		}else if(liste.size()==0){
  			//Prendre les 3 premiers éléments de la Map après avoir mit les objets dans une liste
  			ArrayList<Recette> recettesList = new ArrayList<>(MainApp.getMapRecettes().values());
  			if(recettesList.size() >= 3) {
  			    ControllerToolBar.pickThree.add(recettesList.get(0));
  			    ControllerToolBar.pickThree.add(recettesList.get(1));
  			    ControllerToolBar.pickThree.add(recettesList.get(2));
  			}
  			System.out.println("-------------------------------------------------");
  			System.out.println("Aucune recette correspondante trouvée");
  		}
  		System.out.println("Voici la liste pickThree : ");
  		System.out.println(pickThree);
  	}
  	
}



