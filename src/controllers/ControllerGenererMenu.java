package controllers;

import java.util.ArrayList;
import java.util.Map;

import main.MainApp;
import models.Maladie;
import models.Recette;
import views.VueAfficherMenu;

	public class ControllerGenererMenu {

		public Map<Integer, Recette> allRecettes = MainApp.getMapRecettes();
				
				
		// CONSTRUCTEUR
		public ControllerGenererMenu() {}
		
		
		// METHODE PERMETTANT DE PIOCHER ALEATOIREMENT UNE RECETTE D4UNE LISTE DE RECETTE EN FONCTION DE SA CATEGORIE (ENTREE, PLAT, DESSERT) 
		public Recette getRandomRecetteByCategory(ArrayList<Recette> recettes, String nomCategorie) {

			ArrayList<Recette> recetteCategorie = new ArrayList<Recette>();
			
			for (Recette r : recettes) {
		        if (r.getNom_categorie_recette().equals(nomCategorie)) {
		        	recetteCategorie.add(r);
		        }
		    }
			
			if (recetteCategorie.isEmpty()) {
		        System.out.println(" !!!!!!!!!!!!!!!!!! getRandomRecetteByCategory = Aucune recette trouvée pour la catégorie : " + nomCategorie);
		        return null;
		    } 
			
			int taille = recetteCategorie.size();
			int random = (int) (taille * Math.random());
			Recette selectionRecette = recetteCategorie.get(random);
			recettes.remove(selectionRecette);
					
			return selectionRecette;
			}
		
		// METHODE PERMETTANT DE GENERER LE CONTENU D'UN MENU EN FONCTION DES CHOIX UTILISATEUR DANS LA VueGenererMenu ET INSTANCIE LA VueAfficherMenu
		public void genererMenu(int nbJours,int nbRepasJours,boolean platEntree,boolean platPlat,boolean platDessert,ArrayList<Maladie> restrictionMaladieList) {

			ArrayList<Recette> rList = new ArrayList<Recette>(allRecettes.values());
			
			ArrayList<Recette> recetteFiltreMenu = new ArrayList<Recette>();
			for (Recette r : rList) {
				if (Tools.recetteCompatibleAvecMaladies(r,restrictionMaladieList)) {
					recetteFiltreMenu.add(r);
				}
			}
			// BACK UP DE recetteFiltreMenu POUR POUVOIR RECHARGER LA LISTE DE RECETTE DISPO POUR LES MENUS LORSQUE QU'ELLE EST PRESQUE VIDE
			ArrayList<Recette> recetteFiltreMenu2 = new ArrayList<Recette>(recetteFiltreMenu);
			
			System.out.println("*********** Taille de allRecettes : " + allRecettes.size());
			System.out.println("*********** Taille de recetteFiltreMenu : " + recetteFiltreMenu.size());
			System.out.println("*********** Taille de recetteFiltreMenu2 : " + recetteFiltreMenu2.size());
		
		
			// CALCUL DU NOMBRE DE MENUS A PREVOIR
			int nbMenu = nbJours * nbRepasJours;
			System.out.println("passe dans nbMenu " + nbJours + " * "+nbRepasJours+ " = " + nbMenu);	
		
			// LISTE DE LISTE CONTENANT LES MENUS CONTENANT LES DIFFERENTES RECETTES (ENTREE ET/OU PLAT ET/OU DESSERT) 
			// LISTE TRANSMISSE A LA VueAfficherMenu 
			ArrayList<ArrayList<Recette>> totalMenu = new ArrayList<ArrayList<Recette>>();
		
			// BOUCLE POUR PERMETTANT DE REMPLIR totalMenu.   
			for (int i =1; i < nbMenu+1;i++) {
				
				System.out.println("menu" +i+": ");
				
				
				// CONDITION POUR SAVOIR SI IL RESTE SUFFISAMENT DE RECETTES DANS recetteFiltreMenu POUR TIRER ALEATOIREMENT UNE RECETTE ET RECHARGE LA LISTE AVEC LA BACK UP SI NECESSAIRE
				boolean flag = false;
				boolean flag1 = false;
				boolean flag2 = false;
				
				for (Recette re : recetteFiltreMenu) {
					
					if (platEntree) {
						if (re.getNom_categorie_recette() == "Entrée") {
							flag = true; }
					}
					if (platPlat) {
						if (re.getNom_categorie_recette() == "Plat") {
							flag1 = true; }
					}
					if (platDessert) {
						if (re.getNom_categorie_recette() == "Dessert") {
							flag2 = true; }
					}
					
					}
				
				if((flag || flag1 || flag2)==false) {
					System.out.println("++++++++ Taille de recetteFiltreMenu2 : " + recetteFiltreMenu2.size());
					for (Recette r : recetteFiltreMenu2){
						recetteFiltreMenu.add(r);
					}
					System.out.println(" ++++++++ Liste recetteFiltreMenu rechargé : " + recetteFiltreMenu.size());
				}else System.out.println("------------------liste recetteFiltreMenu en début de boucle : "+recetteFiltreMenu.size());
				
				System.out.println(" *********** taille totale menu : "+ totalMenu.size() + " Boucle : "+ i);
				
				
				ArrayList<Recette> compositionMenu = new ArrayList<Recette>();
				
				// A CHAQUE TOUR DE BOUCLE VERIFIE LES CATEGORIES DE PLATS CHOISI POUR LE MENU, TIRE ALEATOIREMENT UNE RECETTE AVEC getRandomRecetteByCategory ET REMPLI LA LISTE compositionMenu
				if (platEntree) {
					Recette recetteEntree = getRandomRecetteByCategory(recetteFiltreMenu,"Entrée");
					if(recetteEntree == null) {
						System.out.println("Entree non ajouté à la liste compositionMenu");
					}else {compositionMenu.add(recetteEntree);
						System.out.println( "recette entree menu "+ i + " " + recetteEntree.getNom_recette());}
				} 
			
				if (platPlat) {
					Recette recettePlat = getRandomRecetteByCategory(recetteFiltreMenu,"Plat");
					if(recettePlat == null) {
						System.out.println("Plat non ajouté à la liste compositionMenu");
					} else{compositionMenu.add(recettePlat);
					
					System.out.println( "recette plat menu "+ i + " " + recettePlat.getNom_recette());
					}
				}
            
				if (platDessert) {
					Recette recetteDessert = getRandomRecetteByCategory(recetteFiltreMenu,"Dessert");
					if (recetteDessert == null) {
						System.out.println("Dessert non ajouté à la liste compositionMenu");	
					}else {
						compositionMenu.add(recetteDessert);
						System.out.println( "recette dessert menu "+ i + " " + recetteDessert.getNom_recette());
					}
				}
				//REMPLI LA LISTE DE LISTE totalMenu AVEC LES MENUS GENERE
				totalMenu.add(new ArrayList<>(compositionMenu));
            
			}
		
		new VueAfficherMenu(totalMenu, nbJours, nbMenu, nbRepasJours,restrictionMaladieList);
	}
}