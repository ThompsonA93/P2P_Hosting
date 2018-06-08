package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import logic.Client;

/**
 * Generic User Interface
 */
public class View extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button exitButton, downloadButton;
		TextField ip, port, ressource;
		GridPane grid; 
		Scene scene;
		
		/**
		 * Components & mechanics
		 */
		ip = new TextField();
		ip.setPromptText("Enter Server-IP");
		ip.setPrefColumnCount(10);
		
		port = new TextField();
		port.setPromptText("Enter Server-Port");

		ressource = new TextField();
		ressource.setPromptText("Enter Data to fetch");
		
		exitButton = new Button("Exit");
		exitButton.setOnAction(e -> System.exit(0));
		
		downloadButton = new Button("Connect");
		downloadButton.setOnAction(d -> {
			new Client().initiate(ip.getText(), Integer.parseInt(port.getText()), ressource.getText());		
		});	
		
			
		/**
		 * Layout
		 * FIXME :: Pair those buttons
		 */
		GridPane.setConstraints(ip, 0, 0);
		GridPane.setConstraints(port, 0, 1);
		GridPane.setConstraints(ressource, 0, 2);
		GridPane.setConstraints(downloadButton, 0, 3);
		GridPane.setConstraints(exitButton, 1, 3);

		grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);
		grid.setHgap(5);
				
		grid.getChildren().addAll(ip, port, ressource, downloadButton, exitButton);
	
		/**
		 * View allocation
		 */
		scene = new Scene(grid, 500, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}	
}
