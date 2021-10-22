package main;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

   private Parent root;
   
   public static void main(String[] args) {
		Application.launch(Main.class, args);	      
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