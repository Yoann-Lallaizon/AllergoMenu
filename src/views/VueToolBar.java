package views;

import controllers.ControllerToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import main.MainApp;

public class VueToolBar extends ToolBar {
	
	private static ToolBar top;
	private static Button accueil;
	private static Button favoris;
	private static Button gestionModerateurs;
	private static Button mesRecettes;
	private static Button modererCom;
	private static Button modererRec;
	private static Button monCompte;
	private static Button deconnexion;
	private static Button creerRecette;
	
	public VueToolBar() {
		VueToolBar.top = new ToolBar();
		VueToolBar.accueil=new Button("Accueil");
		VueToolBar.favoris=new Button("Mes favoris");
		VueToolBar.gestionModerateurs=new Button("Gestion des modérateurs");
		VueToolBar.mesRecettes=new Button("Mes recettes");
		VueToolBar.modererCom=new Button("Modération des commentaires");
		VueToolBar.modererRec=new Button("Modération des recettes");
		VueToolBar.creerRecette=new Button("Créer Recette");
		VueToolBar.monCompte=new Button("Mon compte");
		VueToolBar.deconnexion=new Button("Déconnexion");
		afficher();
	}
	
//ACCESSEURS
	public static Button getAccueil() {
		return accueil;
	}
	
	public ToolBar getTop() {
		return top;
	}
	
	public void afficher() {
		
//MODIFICATION DE L'AFFICHAGE DE LA TOOLBAR EN FONCTION DU ROLE DE L'UTILISATEUR
  		if (MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("administrateur")) {
			top.getItems().addAll(accueil,mesRecettes, favoris, monCompte, creerRecette, modererCom, modererRec, gestionModerateurs, deconnexion);
		}
		
		else if (MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("moderateur")) {
			top.getItems().addAll(accueil, mesRecettes,  favoris, monCompte, creerRecette, modererCom, modererRec, deconnexion);
		}
		
		else {
			top.getItems().addAll(accueil, mesRecettes, favoris, monCompte, creerRecette, deconnexion);
		}
		
  		
//GESTION DES EVENEMENTS
		accueil.setOnAction(e ->{
			new ControllerToolBar().afficherAccueil();

		});
		
		favoris.setOnAction(e ->{
			//new ControllerToolBar().afficherConsultationFavori();
			new ControllerToolBar().afficherFavoris();
		});
		
		creerRecette.setOnAction(e ->{
			new ControllerToolBar().afficherCreerRecette();
		});
		
		mesRecettes.setOnAction(e ->{
			new ControllerToolBar().afficherMesRecettes();
		});
		
		modererCom.setOnAction(e ->{
			new ControllerToolBar().afficherModererCommentaire();
		});
		
		modererRec.setOnAction(e ->{
			new ControllerToolBar().afficherModererRecette();
		});
		
		gestionModerateurs.setOnAction(e ->{
			new ControllerToolBar().afficherGererModerateur();
		});
		
		monCompte.setOnAction(e ->{
			new ControllerToolBar().afficherParametreCompte();
		});
		
		deconnexion.setOnAction(e ->{
			new ControllerToolBar().seDeconnecter();
		});
		

	}
		
}
