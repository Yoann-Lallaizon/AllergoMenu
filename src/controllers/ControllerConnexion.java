package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import main.ConnectionDB;
import main.MainApp;
import models.Commentaire;
import models.Ingredient;
import models.Maladie;
import models.Recette;
import models.RecetteIngredient;
import models.Utilisateur;
import views.VueToolBar;

public class ControllerConnexion {
	
	private final String username;
    private final String password;

    // Constructeur
    public ControllerConnexion(String username, String password) {
        this.username = username;
        this.password = password;
    }
	
	public boolean handleLogin() {
		// Vérifier que les champs ne sont pas vides
        if (this.username == null || this.username.isEmpty()) {
        	Tools.showAlert("Le nom d'utilisateur n'est pas renseigné");
            return false;
        }
        
        if (this.password == null || this.password.isEmpty()) {
        	Tools.showAlert("Le mot de passe n'est pas renseigné !");
            return false;
        }

        // Vérifier des formats avec regex
        if (!Tools.isUsernameValid(this.username)) {
        	Tools.showAlert("Le nom d'utilisateur est incorrect."); //Le nom d'utilisateur doit contenir entre 4 et 20 caractères alphanumériques !
            return false;
        }

        if (!Tools.isPasswordValid(this.password)) {
        	Tools.showAlert("Le mot de passe est incorrect."); //Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre !
            return false;
        }

        try {            
        	String reqConn = "SELECT u.id_utilisateur, u.nom_utilisateur,u.mdp_utilisateur, u.prenom_utilisateur, " +
                    "u.mail_utilisateur, u.mdp_utilisateur, u.pseudo_utilisateur, u.avatar_utilisateur, " +
                    "u.date_inscription_utilisateur, u.date_derniere_connexion, u.date_naissance_utilisateur, " +
                    "u.sexe_utilisateur, r.nom_role AS nom_role_utilisateur, f.nom_famille AS nom_famille_utilisateur " +
                    "FROM utilisateur u " +
                    "JOIN role r ON u.id_role = r.id_role " +
                    "LEFT JOIN famille f ON u.id_famille = f.id_famille " +
                    "WHERE u.pseudo_utilisateur = ?;";
        	
        
            ConnectionDB db = new ConnectionDB();
            db.initPreparedStatement(reqConn);
            db.getPrepareStatement().setString(1, this.username);
            ResultSet rsUser = db.getPrepareStatement().executeQuery();
            

            // Vérification du résultat
            if (rsUser.next()) {
            	 
                String hashEnregistre = rsUser.getString("mdp_utilisateur");
                //System.out.println("Mdp stocké en BDD : " + hashEnregistre);

                if (BCrypt.checkpw(this.password, hashEnregistre)) {
	                
                	// Récupération des informations depuis le ResultSet
	                int idUtilisateur = rsUser.getInt("id_utilisateur");
	                String nomUtilisateur = rsUser.getString("nom_utilisateur");
	                String prenomUtilisateur = rsUser.getString("prenom_utilisateur");
	                String mailUtilisateur = rsUser.getString("mail_utilisateur");
	                String pseudoUtilisateur = rsUser.getString("pseudo_utilisateur");
	                String avatarUtilisateur = rsUser.getString("avatar_utilisateur");
	
	                // On récupère la date d'inscription en tant que String (vous pouvez adapter la conversion selon vos besoins)
	                String dateInscriptionUtilisateur = rsUser.getString("date_inscription_utilisateur");
	
	                // Pour la date de dernière connexion, nous récupérons un OffsetDateTime.
	                OffsetDateTime dateDerniereConnexionUtilisateur = rsUser.getObject("date_derniere_connexion", OffsetDateTime.class);
	
	                String dateNaissanceUtilisateur = rsUser.getString("date_naissance_utilisateur");
	
	                // Récupération du sexe et conversion en char (prendre le premier caractère)
	                String sexeUtilisateurStr = rsUser.getString("sexe_utilisateur");
	                char sexeUtilisateur = (sexeUtilisateurStr != null && !sexeUtilisateurStr.isEmpty()) ? sexeUtilisateurStr.charAt(0) : ' ';
	
	                String nomRoleUtilisateur = rsUser.getString("nom_role_utilisateur");
	                String nomFamilleUtilisateur = rsUser.getString("nom_famille_utilisateur");
	                         

//UTILISATEUR-ACTIF
	                MainApp.setUtilisateurActif(new Utilisateur(
		                    idUtilisateur,
		                    nomUtilisateur,
		                    prenomUtilisateur,
		                    mailUtilisateur,
		                    pseudoUtilisateur,
		                    avatarUtilisateur,
		                    dateInscriptionUtilisateur,
		                    dateDerniereConnexionUtilisateur,
		                    sexeUtilisateur,
		                    dateNaissanceUtilisateur,
		                    new ArrayList<>(),
		                    nomRoleUtilisateur,
		                    nomFamilleUtilisateur,
		                    new ArrayList<>(),
		                    new ArrayList<>()
		                ));
	                	            	
//RECUPERATION DE TOUTES LES RECETTES // TOUT LES INGREDIENTS // ASSOCIATION DES DEUX	            	
	            	ArrayList<Recette> recettes = getRecettesComplete();
	                
	                for (Recette r : recettes) {
	                    MainApp.getMapRecettes().put(r.getId_recette(), r);
	                }
	                
//RECUPERATION DE TOUTES LES MALADIES
	                MainApp.setMapAllMaladies(getMapAllMaladies());
	                
//CHARGEMENT DE LA LISTE DE MALADIES PROPRE A L'UTILISATEUR	                
	                ArrayList<Maladie> listeMaladiesUtilisateur = construireListeMaladiesUtilisateur(idUtilisateur);

	                MainApp.getUtilisateurActif().setListeMaladies(listeMaladiesUtilisateur);
	                
//ADAPTATION DE LA LISTE DES RECETTES	                
	                MainApp.setRecettesFiltre(filtrerRecettesParMaladies(listeMaladiesUtilisateur));
	                getMapUtilisateur();


//CHARGEMENT DE LA LISTE DES FAVORIS CREES PAR L'utilisateurActif
	                String reqFav = "SELECT favori.id_recette "+
	            			"FROM favori "+
	            			"WHERE favori.id_utilisateur="+idUtilisateur;
	                db.initPreparedStatement(reqFav);
	                ResultSet rsFav = db.getPrepareStatement().executeQuery();
	                
	                // Insertion des id_recette dans une liste
	                ArrayList <Recette> listeFavoris = new ArrayList<>();
	                while (rsFav.next()) {
	                    int idRecette = rsFav.getInt("id_recette");
	                    Recette recette = MainApp.getMapRecettes().get(idRecette);
	                    if (recette != null) {
	                    	listeFavoris.add(recette);
	                    }
	                }
	                
	                MainApp.getUtilisateurActif().setListe_favori_utilisateur(listeFavoris);
	                
//CHARGEMENT DE LA LISTE DES RECETTES CREES PAR L'utilisateurActif
	                ArrayList <Recette> mesRecettes = new ArrayList<>();

	                for (Map.Entry<Integer, Recette> entry : MainApp.getMapRecettes().entrySet()) {
	                    	Recette recette = entry.getValue();
	                    if (recette.getId_utilisateur_createur_recette() == MainApp.getUtilisateurActif().getId_utilisateur()) {
	                    	mesRecettes.add(entry.getValue());
	                    }
	                }
	                MainApp.getUtilisateurActif().setListeMesRecettes(mesRecettes); 
	                
	                
	                //Log de debug pour afficher les maladies
	                /*
	                for (Map.Entry<Integer, Maladie> entry : mapAllMaladies.entrySet()) {
                        Maladie maladie = entry.getValue();
                        System.out.println("ID : " + maladie.getIdMaladie()
                                           + " - Nom : " + maladie.getNom_Maladie());
                        System.out.println("  Caractéristique : " + maladie.getCaracteristique_Maladie());
                        System.out.println("-------------------------------------");
                    }
	                */
	                //Log de debug pour afficher les recettes filtrés 
	                /*
	                if (recettesFiltre.isEmpty()) {
	                    System.out.println("Aucune recette filtrée trouvée.");
	                } else {
	                    System.out.println("Liste des recettes filtrées :");
	                    for (Map.Entry<Integer, Recette> entry : recettesFiltre.entrySet()) {
	                        Recette recette = entry.getValue();
	                        System.out.println("ID : " + recette.getId_recette() 
	                                           + " - Nom : " + recette.getNom_recette());
	                        // Tu peux afficher d'autres informations si nécessaire, par exemple :
	                        System.out.println("  Temps préparation : " + recette.getTemps_preparation());
	                        System.out.println("  Difficulté : " + recette.getDifficulte_recette());
	                        System.out.println("-------------------------------------");
	                    }
	                }

	               	*/
	                // Log de debug pour afficher les recettes et leurs ingrédients.
	                	/*
		                for (Recette recette : recettes) {
		                	System.out.println("Recette : " + recette);
		                    
		                    System.out.println("Ingrédients : ");
		                    
		                    for (RecetteIngredient recetteIngredient : recette.getListeIngredientsComplets()) {
		                        System.out.println("- " + recetteIngredient.getIngredient().getNomIngredient() 
		                                           + " (" + recetteIngredient.getQuantite() 
		                                           + " " + recetteIngredient.getUnite() + ")");
		                    }
		                    System.out.println("----------------------------------");
		                }  
		                */       
	                
	                //Lire la mapRecette
	                /*for (Map.Entry<Integer, Recette> entry : mapRecettes.entrySet()) {
	                
	                    Integer idRecette = entry.getKey();
	                    Recette recette = entry.getValue();

	                    System.out.println("Recette ID : " + idRecette);
	                    System.out.println(recette);
	                    System.out.println("-------------------------------");
	                }
	                
	                 */
	                
// Traiter en fonction du rôle de l'utilisateur
	                if (MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("administrateur")) {
//RECUPERATION DES DONNEES NECESSAIRE A L'ADMINISTRATEUR                	
	                    MainApp.setUtilisateurNonAdmin(getUtilisateurNonAdmin());
	                    MainApp.setCommentairesNonModeres(getCommentairesNonModeres());
	                    MainApp.setRecetteNonModeres(getRecettesNonModerees());
	                    System.out.println("L'utilisateur est un administrateur, lancement du traitement admin.");

	                    // Affiche la liste de tout les utilisateurs sauf l'utilisateurActif
	                    /*
	                    for (Utilisateur u : allUtilisateurs) {
	                        System.out.println(u);
	                    }
	                    */
	
	                    //new VueGererModerateur(utilisateurNonAdmin);
	                    MainApp.setMaToolBar(new VueToolBar());
	                    VueToolBar.getAccueil().fire();
	                } else if (MainApp.getUtilisateurActif().getRole_utilisateur().equalsIgnoreCase("moderateur")) {
//RECUPERATION DES DONNEES NECESSAIRE AU MODERATEUR  
	                	MainApp.setCommentairesNonModeres(getCommentairesNonModeres());
	                    MainApp.setRecetteNonModeres(getRecettesNonModerees());
	                    System.out.println("L'utilisateur est un modérateur, lancement du traitement modérateur.");
	                    
	                    /*
	                     * Afficher les commentaire non modérés
	                    for (Commentaire commentaire : commentairesNonModeres) {
	                        System.out.println(commentaire);
	                    }
	                    */
	                    
	                    //Affichage des Recettes Non modérées
	                    /*
	                    if (recetteNonModeres.isEmpty()) {
	                        System.out.println("Aucune recette non modérée trouvée.");
	                    } else {
	                        System.out.println("Liste des recettes non modérées :");
	                        for (Recette recette : recetteNonModeres) {
	                             System.out.println("ID : " + recette.getId_recette() + ", Nom : " + recette.getNom_recette());
	                        }
	                    }*/
	                    
	                    //new VueModererCommentaire(commentairesNonModeres);
	                    MainApp.setMaToolBar(new VueToolBar());
	                    VueToolBar.getAccueil().fire();	
	                } else {
//INITIALISATION DE LA VUE ACCUEIL POUR UN UTILISATEUR NORMAL
	                    System.out.println("L'utilisateur est un utilisateur classique, lancement de la vue d'accueil.");
	                    MainApp.setMaToolBar(new VueToolBar());
	                    VueToolBar.getAccueil().fire();
	 
	                }
	             
	                db.closeConnection();
	                return true;
                }else {
                    Tools.showAlert("Nom d'utilisateur ou mot de passe incorrect !");
                    db.closeConnection();
                    return false;
                }
                
            } else {
            	Tools.showAlert("Nom d'utilisateur ou mot de passe incorrect !");
                db.closeConnection();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la requête : " + e.getMessage());
            return false;
        }
	}
	
	// 0. Méthode globale qui assemble tout
	public ArrayList<Recette> getRecettesComplete() {
	    MainApp.setMapIngredients(getAllIngredients());
	    MainApp.setMapRecettes(getAllRecettes());
	    populateRecetteIngredients();
	    return new ArrayList<>(MainApp.getMapRecettes().values());
	}
	
	// 1. Récupération de tous les ingrédients
	public Map<Integer, Ingredient> getAllIngredients() {
	    Map<Integer, Ingredient> mapIngredients = new HashMap<>();
	    String query = "SELECT i.id_ingredient, i.nom_ingredient, sc.nom_sous_cat, c.nom_cat_ingredient " +
	                   "FROM ingredient i " +
	                   "LEFT JOIN sous_cat_ingredient sc ON i.id_sous_cat = sc.id_sous_cat " +
	                   "LEFT JOIN cat_ingredient c ON sc.id_cat_ingredient = c.id_cat_ingredient;";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(query);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id_ingredient");
	            String nom = rs.getString("nom_ingredient");
	            String nomSousCat = rs.getString("nom_sous_cat");
	            String nomCat = rs.getString("nom_cat_ingredient");
	            Ingredient ingredient = new Ingredient(id, nom, nomCat, nomSousCat);
	            mapIngredients.put(id, ingredient);
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des ingrédients : " + e.getMessage());
	    }
	    return mapIngredients;
	}

	// 2. Récupération de toutes les recettes
	public Map<Integer, Recette> getAllRecettes() {
	    Map<Integer, Recette> mapRecettes = new HashMap<>();
	    String query = "SELECT r.id_recette, r.nom_recette, r.statut_moderation_recette, r.temps_preparation, " +
	               "r.temps_cuisson, r.temps_total_recette, r.difficulte_recette, r.nb_personne_recette, " +
	               "r.prix_recette, r.etape_recette, cr.nom_cat_recette AS nom_categorie, r.photo_recette, " +
	               "r.id_utilisateur " +
	               "FROM recette r " +
	               "LEFT JOIN cat_recette cr ON r.id_cat_recette = cr.id_cat_recette " + 
	               "WHERE r.statut_moderation_recette = '1';";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(query);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id_recette");
	            Recette recette = new Recette(
	                id,
	                rs.getString("nom_recette"),
	                rs.getBoolean("statut_moderation_recette"),
	                rs.getInt("temps_preparation"),
	                rs.getInt("temps_cuisson"),
	                rs.getString("difficulte_recette"),
	                rs.getInt("nb_personne_recette"),
	                rs.getString("prix_recette"),
	                rs.getString("etape_recette"),
	                rs.getString("nom_categorie"),
	                rs.getString("photo_recette"),
	                rs.getInt("id_utilisateur"),
	                new ArrayList<>()
	            );
	            mapRecettes.put(id, recette);
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des recettes : " + e.getMessage());
	    }
	    return mapRecettes;
	}

	// 3. Récupération des associations Recette-Ingrédient (RecetteIngredient)
	public void populateRecetteIngredients() {
	    String query = "SELECT id_recette, id_ingredient, quantite_ingredient, unite FROM recette_ingredient;";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(query);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            int recetteId = rs.getInt("id_recette");
	            int ingredientId = rs.getInt("id_ingredient");
	            float quantite = rs.getFloat("quantite_ingredient");
	            String unite = rs.getString("unite");
	            if (unite == null) {
	                unite = "";
	            }
	            // Récupérer les objets recette et ingredient depuis les maps
	            Recette recette = MainApp.getMapRecettes().get(recetteId);
	            Ingredient ingredient = MainApp.getMapIngredients().get(ingredientId);
	            if (recette != null && ingredient != null) {
	            	RecetteIngredient ri = new RecetteIngredient(recette, ingredient, quantite, unite);
	                // Ajouter l'association à la liste dédiée aux associations dans la recette
	                recette.getListeIngredientsComplets().add(ri);

	            }
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des associations Recette-Ingrédient : " + e.getMessage());
	    }
	}
	
	// 4 . Récupère toutes les maladies et les associent aux ingérdients
	public Map<Integer, Maladie> getMapAllMaladies() {
	    Map<Integer, Maladie> mapMaladies = new HashMap<>();
	    String queryMaladie = "SELECT id_maladie, nom_maladie, caracteristique_maladie FROM maladie;";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(queryMaladie);
	        ResultSet rsMaladie = db.getPrepareStatement().executeQuery();
	        while (rsMaladie.next()) {
	            int idMaladie = rsMaladie.getInt("id_maladie");
	            String nomMaladie = rsMaladie.getString("nom_maladie");
	            String caracteristique = rsMaladie.getString("caracteristique_maladie");
	            // Utiliser un constructeur qui accepte l'id
	            Maladie maladie = new Maladie(idMaladie, nomMaladie, caracteristique);
	            mapMaladies.put(idMaladie, maladie);
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des maladies : " + e.getMessage());
	    }
	    
	    // Remplir les ingrédients pour l'ensemble des maladies récupérées en une seule requête
	    if (!mapMaladies.isEmpty()) {
	        StringBuilder idList = new StringBuilder();
	        int count = 0;
	        for (Integer id : mapMaladies.keySet()) {
	            idList.append(id);
	            if (++count < mapMaladies.size()) {
	                idList.append(",");
	            }
	        }
	        
	        String queryMI = "SELECT id_maladie, id_ingredient FROM maladie_ingredient " +
	                         "WHERE id_maladie IN (" + idList.toString() + ")";
	        try {
	            ConnectionDB db = new ConnectionDB();
	            db.initPreparedStatement(queryMI);
	            ResultSet rsMI = db.getPrepareStatement().executeQuery();
	            while (rsMI.next()) {
	                int idMaladie = rsMI.getInt("id_maladie");
	                int idIngredient = rsMI.getInt("id_ingredient");
	                Ingredient ingredient = MainApp.getMapIngredients().get(idIngredient);
	                if (ingredient != null && mapMaladies.containsKey(idMaladie)) {
	                    mapMaladies.get(idMaladie).getListe_Ingredient().add(ingredient);
	                }
	            }
	            db.closeConnection();
	        } catch (SQLException e) {
	            System.err.println("Erreur lors de la récupération des associations maladie-ingrédient : " + e.getMessage());
	        }
	    }
	    
	    return mapMaladies;
	}


	
	//Méthode qui filtre les recettes en fonction des maladies Utilisateur Actif
	public Map<Integer, Recette> filtrerRecettesParMaladies(ArrayList<Maladie> maladiesUtilisateur) {
	    Map<Integer, Recette> recettesFiltre = new HashMap<>();
	    
	    for (Map.Entry<Integer, Recette> entry : MainApp.getMapRecettes().entrySet()) {
	        Recette recette = entry.getValue();
	        // Utilisation de la méthode recetteCompatibleAvecMaladies
	        if (Tools.recetteCompatibleAvecMaladies(recette, maladiesUtilisateur)) {
	            recettesFiltre.put(entry.getKey(), recette);
	        }
	    }
	    
	    return recettesFiltre;
	}
	
	//Méthode pour récupérer les id de maladies de l'utilisateur Actif 
	public ArrayList<Integer> getIdMaladiesUtilisateur(int idUtilisateur) {
		ArrayList<Integer> idMaladies = new ArrayList<>();
	    String query = "SELECT id_maladie FROM declarer WHERE id_utilisateur = ?";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(query);
	        db.getPrepareStatement().setInt(1, idUtilisateur);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            idMaladies.add(rs.getInt("id_maladie"));
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des IDs de maladies de l'utilisateur : " + e.getMessage());
	    }
	    return idMaladies;
	}
	
	//Méthode pour avoir la liste des maladies de l'utilisateurActif
	public ArrayList<Maladie> construireListeMaladiesUtilisateur(int idUtilisateur) {
		ArrayList<Maladie> maladiesUtilisateur = new ArrayList<>();
		ArrayList<Integer> idMaladiesUtilisateur = getIdMaladiesUtilisateur(idUtilisateur);
	    for (Integer id : idMaladiesUtilisateur) {
	        if (MainApp.getMapAllMaladies().containsKey(id)) {
	            maladiesUtilisateur.add(MainApp.getMapAllMaladies().get(id));
	        }
	    }
	    return maladiesUtilisateur;
	}
	
	
	//Méthode créant la map associant l'id utilisateur a son objet utilisateur
	public void getMapUtilisateur() {
	    // Récupération de tous les utilisateurs pour créer la map id -> Utilisateur
	    MainApp.setAllUtilisateurs(getAllUtilisateurs());
	    Map<Integer, Utilisateur> mapUtilisateurs = new HashMap<>();
	    for (Utilisateur u : MainApp.getAllUtilisateurs().values()) {
	        mapUtilisateurs.put(u.getId_utilisateur(), u);
	    }
	}
	
	//Méthode pour construire la liste des commentaires non modérés, destiné aux modérateurs
	public ArrayList<Commentaire> getCommentairesNonModeres() {
		ArrayList<Commentaire> commentaires = new ArrayList<>();
	    
	    Map<Integer, Utilisateur> mapUtilisateurs = new HashMap<>();
	    for (Utilisateur u : MainApp.getAllUtilisateurs().values()) {
	        mapUtilisateurs.put(u.getId_utilisateur(), u);
	    }
	    
	    try {
	        ConnectionDB db = new ConnectionDB();
	        String reqCommentairesNonModeres = "SELECT id_commentaire, text_commentaire, statut_moderation_commentaire, " +
	                                           "id_recette, id_utilisateur, date_publi_commentaire " +
	                                           "FROM commentaire " +
	                                           "WHERE statut_moderation_commentaire = 0;";
	        db.initPreparedStatement(reqCommentairesNonModeres);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        
	        while (rs.next()) {
	            int idCommentaire = rs.getInt("id_commentaire");
	            String textCommentaire = rs.getString("text_commentaire");
	            boolean statutModeration = false;
	            int idRecette = rs.getInt("id_recette");
	            int idUtilisateur = rs.getInt("id_utilisateur");
	            String datePubli = rs.getString("date_publi_commentaire");
	            
	            // Association de la recette et de l'utilisateur correspondants
	            Recette recette = MainApp.getMapRecettes().get(idRecette);
	            Utilisateur utilisateur = mapUtilisateurs.get(idUtilisateur);
	            
	            Commentaire commentaire = new Commentaire(
	                idCommentaire,
	                textCommentaire,
	                statutModeration,
	                recette,
	                utilisateur,
	                datePubli
	            );
	            commentaires.add(commentaire);
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des commentaires non modérés : " + e.getMessage());
	    }
	    
	    return commentaires;
	}
	
	//Méthode pour construire la liste des recettes non modérées, destiné aux modérateurs
	public ArrayList<Recette> getRecettesNonModerees() {
		ArrayList<Recette> mapRecettesNonModerees = new ArrayList<>();
		 String query = "SELECT r.id_recette, r.nom_recette, r.statut_moderation_recette, r.temps_preparation, " +
	               "r.temps_cuisson, r.temps_total_recette, r.difficulte_recette, r.nb_personne_recette, " +
	               "r.prix_recette, r.etape_recette, cr.nom_cat_recette AS nom_categorie, r.photo_recette, " +
	               "r.id_utilisateur " +
	               "FROM recette r " +
	               "LEFT JOIN cat_recette cr ON r.id_cat_recette = cr.id_cat_recette " + 
	               "WHERE r.statut_moderation_recette = '0';";
	    try {
	        ConnectionDB db = new ConnectionDB();
	        db.initPreparedStatement(query);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id_recette");
	            Recette recette = new Recette(
	                id,
	                rs.getString("nom_recette"),
	                rs.getBoolean("statut_moderation_recette"),
	                rs.getInt("temps_preparation"),
	                rs.getInt("temps_cuisson"),
	                rs.getString("difficulte_recette"),
	                rs.getInt("nb_personne_recette"),
	                rs.getString("prix_recette"),
	                rs.getString("etape_recette"),
	                rs.getString("nom_categorie"),
	                rs.getString("photo_recette"),
	                rs.getInt("id_utilisateur"),
	                new ArrayList<>()
	            );
	            mapRecettesNonModerees.add(recette);
	        }
	        db.closeConnection();
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération des recettes : " + e.getMessage());
	    }
	   
	    return mapRecettesNonModerees;
	}

	
	
	//Récupère tout les utilisateurs 
	public Map<Integer, Utilisateur> getAllUtilisateurs() {
		
		Map<Integer, Utilisateur> allUtilisateurs = new HashMap<>();
	    try {
	        ConnectionDB db = new ConnectionDB();
	        String reqAllUsers = "SELECT u.id_utilisateur, u.nom_utilisateur, u.prenom_utilisateur, " +
	                             "u.mail_utilisateur, u.pseudo_utilisateur, u.avatar_utilisateur, " +
	                             "u.date_inscription_utilisateur, u.date_derniere_connexion, u.date_naissance_utilisateur, " +
	                             "u.sexe_utilisateur, r.nom_role AS nom_role_utilisateur, f.nom_famille AS nom_famille_utilisateur " +
	                             "FROM utilisateur u " +
	                             "JOIN role r ON u.id_role = r.id_role " +
	                             "LEFT JOIN famille f ON u.id_famille = f.id_famille;";
	        db.initPreparedStatement(reqAllUsers);
	        ResultSet rs = db.getPrepareStatement().executeQuery();
	        while (rs.next()) {
	            int idUtilisateur = rs.getInt("id_utilisateur");
	            String nomUtilisateur = rs.getString("nom_utilisateur");
	            String prenomUtilisateur = rs.getString("prenom_utilisateur");
	            String mailUtilisateur = rs.getString("mail_utilisateur");
	            String pseudoUtilisateur = rs.getString("pseudo_utilisateur");
	            String avatarUtilisateur = rs.getString("avatar_utilisateur");
	            String dateInscriptionUtilisateur = rs.getString("date_inscription_utilisateur");
	            OffsetDateTime dateDerniereConnexionUtilisateur = rs.getObject("date_derniere_connexion", OffsetDateTime.class);
	            String dateNaissanceUtilisateur = rs.getString("date_naissance_utilisateur");

	            String sexeUtilisateurStr = rs.getString("sexe_utilisateur");
	            char sexeUtilisateur = (sexeUtilisateurStr != null && !sexeUtilisateurStr.isEmpty()) ? sexeUtilisateurStr.charAt(0) : ' ';

	            String nomRoleUtilisateur = rs.getString("nom_role_utilisateur");
	            String nomFamilleUtilisateur = rs.getString("nom_famille_utilisateur");

	            ArrayList<Maladie> listeMaladieUtilisateur = new ArrayList<>();

	            Utilisateur utilisateur = new Utilisateur(
	                idUtilisateur,
	                nomUtilisateur,
	                prenomUtilisateur,
	                mailUtilisateur,
	                pseudoUtilisateur,
	                avatarUtilisateur,
	                dateInscriptionUtilisateur,
	                dateDerniereConnexionUtilisateur,
	                sexeUtilisateur,
	                dateNaissanceUtilisateur,
	                listeMaladieUtilisateur,
	                nomRoleUtilisateur,
	                nomFamilleUtilisateur,
	                new ArrayList<Recette>(),
	                new ArrayList<Recette>()
	            );
	            allUtilisateurs.put(idUtilisateur, utilisateur);
	        }
	        db.closeConnection();
	        
	     // Récupére les associations utilisateur-maladie depuis la table "déclarer"
	        try {
	            db = new ConnectionDB();
	            String reqDeclarer = "SELECT id_utilisateur, id_maladie FROM declarer;";
	            db.initPreparedStatement(reqDeclarer);
	            ResultSet rsDecl = db.getPrepareStatement().executeQuery();

	            // Construire une map temporaire : id_utilisateur -> List<Maladie>
	            Map<Integer, ArrayList<Maladie>> userMaladies = new HashMap<>();
	            while (rsDecl.next()) {
	                int idUtilisateur = rsDecl.getInt("id_utilisateur");
	                int idMaladie = rsDecl.getInt("id_maladie");
	                // Récupérer l'objet Maladie à partir de la map statique
	                Maladie maladie = MainApp.getMapAllMaladies().get(idMaladie);
	                if (maladie != null) {
	                    userMaladies.computeIfAbsent(idUtilisateur, k -> new ArrayList<>()).add(maladie);
	                }
	            }
	            db.closeConnection();
	            // Mise à jour des listes de maladies de chaque utilisateurs avec leur Maladies
	            for (Utilisateur u : allUtilisateurs.values()) {
	            	ArrayList<Maladie> malList = userMaladies.get(u.getId_utilisateur());
	                if (malList != null) {
	                    u.setListe_maladie_utilisateur(malList);
	                }
	            }
	        } catch (SQLException ex) {
	            System.err.println("Erreur lors de la récupération des maladies pour les utilisateurs : " + ex.getMessage());
	        }
	        MainApp.setAllUtilisateurs(allUtilisateurs); 
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération de tous les utilisateurs : " + e.getMessage());
	    }
	    return allUtilisateurs;
	}
	
	//Méthode pour construire la liste des utilisateurs n'étant pas admin, destiné a l'administrateur
	public ArrayList<Utilisateur> getUtilisateurNonAdmin() {
		ArrayList<Utilisateur> nonAdminUsers = new ArrayList<>();
	    for (Utilisateur user : MainApp.getAllUtilisateurs().values()) {
	        if (!user.getRole_utilisateur().equalsIgnoreCase("Administrateur")) {
	            nonAdminUsers.add(user);
	        }
	    }
	    return nonAdminUsers;
	}

	//Méthode pour construire la map qui associe un id utilisateur a son pseudo, utilisé pour la modération entre autre
	public Map<Integer, String> getMapIdPseudoUtilisateur() {
	    Map<Integer, String> map = new HashMap<>();
	    for (Utilisateur u : MainApp.getAllUtilisateurs().values()) { //Get All Users
	        map.put(u.getId_utilisateur(), u.getPseudo_utilisateur());
	    }
	    return map;
	}
}