package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Generates Table-View about possible seeders.
 */
public class DataTable extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene;
		TableView dataTable;
		TableColumn ip, port, id, name, size, date, type;
		Group group;
		VBox vbox;
		
		/**
		 * Components & Mechanics
		 */
		group = new Group();
		vbox = new VBox();
		scene = new Scene(group);
		
		dataTable = new TableView();
		dataTable.setEditable(true);
		
		ip = new TableColumn("IP");
		port = new TableColumn("PORT");
		id = new TableColumn("ID");
		name = new TableColumn("TITLE");
		size = new TableColumn("SIZE");
		date = new TableColumn("DATE");
		type = new TableColumn("TYPE");
		
		/**
		 * Layout
		 */
		stage.setTitle("Available Leecher");
		stage.setWidth(300);
		stage.setHeight(500);
		
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().add(dataTable);
		
		dataTable.getColumns().addAll(ip, port, id, name, size, date, type);
		
		/**
		 * Allocating View
		 */
		stage.setScene(scene);
		stage.show();		
	}
}
