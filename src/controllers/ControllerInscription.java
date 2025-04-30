package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import main.ConnectionDB;
import views.VueCompleterProfil;

public class ControllerInscription {

    public ControllerInscription() {
    }
    // GESTION DE L'INSCRIPTION
    public String handleInscription(String username, String email, String password, String confirmPassword) {
        if (!username.isEmpty() && !email.isEmpty() && !password.isBlank() && !confirmPassword.isBlank() && username!=null && email!=null && password!=null && confirmPassword!=null && Tools.isUsernameValid(username) && Tools.isPasswordValid(password) && Tools.isMailValid(email)) {
        	if (password.equals(confirmPassword)) {
        		// HACHAGE DU MDP AVANT INSERTION EN BDD
                String hashMotDePasse = BCrypt.hashpw(password, BCrypt.gensalt(12));
                
            	String query = "SELECT nom_maladie FROM maladie";
            	ArrayList<String> nomMaladies = new ArrayList<>();
                try {
                	ConnectionDB db = new ConnectionDB();
                	Connection connection = db.getConnection();
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    
                    while (rs.next()) {
                        nomMaladies.add(rs.getString("nom_maladie"));
                    }
                    db.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                new VueCompleterProfil(username, email, hashMotDePasse, nomMaladies);
                return "";
            }else {
            	return "Les mots de passe ne correspondent pas !";
            }
        }else {
        	return "Certains champs ne sont pas renseignés correctement.";
        }
    }

}
