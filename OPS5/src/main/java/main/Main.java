package main;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;


public class Main extends Application{

   private Parent root;
   
   public static void main(String[] args) {

		String userName = "pmiw21g05";
		String password = "IL6CgkzEMcNY99TD";
		String url = "jdbc:mariadb://dbstudents01.imi.uni-luebeck.de:3306/pmiw21g05_v01";
		try(Connection conn = DriverManager.getConnection(url, userName, password)){
		    System.out.println("Mit DB verbunden.");
			Application.launch(Main.class, args);
        }
		catch(Exception e){
		    e.printStackTrace();
        }

	   }
   
   @Override
   public void init() throws Exception {
	   FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainForm.fxml"));
       root = loader.load();
       MainController.setInstance(loader.getController());
   }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("eHealth Praktikum GUI");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}
	
	@Override
	public void stop(){
	}

}