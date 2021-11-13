package main;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jooq.impl.DSL;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main extends Application{

   private Parent root;
   public static String userName = "pmiw21g05";
   public static String password = "IL6CgkzEMcNY99TD";
   public static String url = "jdbc:mariadb://dbstudents01.imi.uni-luebeck.de:3306/pmiw21g05_v01";


    /**
     * this connection is used for the connection to the database
     */
   public static Connection connection;

    /**
     * try to connect to database
     */
    static {
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * the dsl context to interfere between the javacode and the database
     */
    public static DSLContext dslContext = DSL.using(connection, SQLDialect.MARIADB);

   
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