package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import leecher.Leecher;
import logic.Connector;
import logic.SeederData;

/**
 * Generic User Interface Setup
 * TODO: Possible redundancy with TableColumns.
 * TODO: Outsource somewhere
 */
public class View extends Application{
	
	private SplitPane container;
	private AnchorPane topPane, botPane;
	private TableView serverTable;
	private TableColumn ipColumn, portColumn, resourceIDColumn,
			resourceNameColumn, typeColumn, sizeColumn;
	private TextField ipField, portField, resourceField;
	private TextArea logArea;
	private Button connectButton, helpButton, exitButton;
	
	private ObservableList<SeederData> data = FXCollections.observableArrayList();

	
	@Override
	public void start(Stage stage) throws Exception{
		constructComponents();
		linkComponents();
		configureMechanics();
		
		Scene scene = new Scene(container, 600, 400);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Setup of Configurations for each Node within the Scene.
	 */
	private void configureMechanics() {
		// FIXME: Probably correct?
		ipColumn.setCellValueFactory(new PropertyValueFactory<SeederData, String>("IP"));
		portColumn.setCellValueFactory(new PropertyValueFactory<SeederData, Integer>("Port"));
		resourceIDColumn.setCellValueFactory(new PropertyValueFactory<SeederData, Integer>("ID"));
		resourceNameColumn.setCellValueFactory(new PropertyValueFactory<SeederData, String>("Name"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<SeederData, Integer>("Size"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<SeederData, String>("Type"));
		
		Leecher.logger.setlogArea(logArea);
		
		connectButton.setOnAction(d -> {
			if(resourceField.getText().isEmpty()) {
				String[] seeders = new Connector().getResourcesFromServer(
						ipField.getText(), 
						Integer.parseInt(portField.getText())
				);
				
				for(String s : seeders) {
					data.add(new SeederData(s));
					// TODO? Update on List, not sure if complete here.
				}		
				
			}else {
				// FIXME Support different file types.
				new Connector().getResourceFromSeeder(
						ipField.getText(), 
						Integer.parseInt(portField.getText()), 
						Integer.parseInt(resourceField.getText())
				);	
				
				
				
				
			}
		});
		
		helpButton.setOnAction(d -> {
			Leecher.logger.help();
		});
		
		exitButton.setOnAction(d -> {
			System.exit(0);		
		}); 
	}

	/**
	 * Setup imminent layout of the scene.
	 */
	private void linkComponents() {	
		
		serverTable.setItems(data);

/*		serverTable.getItems().addAll(
				ipColumn,
				portColumn,
				resourceIDColumn,
				resourceNameColumn,
				typeColumn,
				sizeColumn
				);
*/				
		topPane.getChildren().addAll(
				ipField,
				portField,
				resourceField,
				connectButton,
				helpButton,
				exitButton,
				logArea
				);
			
		botPane.getChildren().addAll(
				serverTable
				);
       
		container.getItems().addAll(
				topPane,
				botPane
				);
		
	}

	/**
	 * Creates Components, each with it's attributes.
	 */
	private void constructComponents() {
		ipField = new TextField();
		ipField.setLayoutX(14.0);
		ipField.setLayoutY(14.0);
		ipField.setPrefHeight(25.0);
		ipField.setPrefWidth(220.0);
		ipField.setPromptText("Enter Server-IP");
		
		portField = new TextField();
		portField.setLayoutX(14.0);
		portField.setLayoutY(47.0);
		portField.setPrefHeight(25.0);
		portField.setPrefWidth(220.0);
		portField.setPromptText("Enter Server-Port");
		
		resourceField = new TextField();
		resourceField.setLayoutX(14.0);
		resourceField.setLayoutY(82.0);
		resourceField.setPrefHeight(25.0);
		resourceField.setPrefWidth(220.0);
		resourceField.setPromptText("Enter Resource");
		
		logArea = new TextArea();
		logArea.setLayoutX(283.0);
		logArea.setLayoutY(0.0);
		logArea.setPrefHeight(183.0);
		logArea.setPrefWidth(316.0);
		logArea.setText("## Assuming Log-Functionality ##");
		logArea.setWrapText(true);
		
		
		connectButton = new Button();
		connectButton.setLayoutX(14.0);
		connectButton.setLayoutY(123.0);
		connectButton.setText("Connect");
	
		helpButton = new Button();
		helpButton.setLayoutX(113.0);
		helpButton.setLayoutY(123.0);
		helpButton.setText("Help");

		
		exitButton = new Button();
		exitButton.setLayoutX(191.0);
		exitButton.setLayoutY(123.0);
		exitButton.setText("Exit");

				
		serverTable = new TableView();
		serverTable.setPrefHeight(214.0);
		serverTable.setPrefWidth(600.0);
		
		ipColumn =  new TableColumn();
		ipColumn.setPrefWidth(100.0);
		ipColumn.setText("Server-IP");
		
		portColumn = new TableColumn();
		portColumn.setPrefWidth(60.0);
		portColumn.setText("Port");
		
		resourceIDColumn = new TableColumn();
		resourceIDColumn.setPrefWidth(55.0);
		resourceIDColumn.setText("ID");
		
		resourceNameColumn = new TableColumn();
		resourceNameColumn.setPrefWidth(220.0);
		resourceNameColumn.setText("Resource");
		
		typeColumn = new TableColumn();
		typeColumn.setPrefWidth(100.0);
		typeColumn.setText("Type");
		
		sizeColumn = new TableColumn();
		sizeColumn.setPrefWidth(100.0);
		sizeColumn.setText("Size");
		
		topPane = new AnchorPane();
		topPane.setPrefHeight(100.0);
		topPane.setPrefWidth(160.0);
		
		botPane = new AnchorPane();
		botPane.setPrefHeight(100.0);
		botPane.setPrefWidth(160.0);
		
		container = new SplitPane();
		container.setDividerPositions(0.45226130653266333);
		container.setOrientation(Orientation.VERTICAL);
		container.setPrefHeight(400.0);
		container.setPrefWidth(600.0);
	}
}
