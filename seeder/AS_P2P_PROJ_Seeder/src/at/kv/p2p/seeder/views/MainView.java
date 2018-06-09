package at.kv.p2p.seeder.views;

import java.util.List;

import at.kv.p2p.seeder.logic.Logger;
import at.kv.p2p.seeder.logic.LoggerListener;
import at.kv.p2p.seeder.logic.P2PFile;
import at.kv.p2p.seeder.models.MainViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainView {
	
	@FXML
	private TableView<P2PFile> tableFiles;
	
	@FXML
	private TableColumn tableColumnFile;
	
	@FXML
	private TableColumn tableColumnFullPath;
	
	@FXML
	private TableColumn tableColumnType;
	
	@FXML
	private TableColumn tableColumnSize;
	
	@FXML
	private TableColumn tableColumnDate;
	
	@FXML
	private TableColumn tableColumnID;
	
	@FXML
	private Button btnRemove;
	
	@FXML
	private TextArea txtAreaLog;
	
	@FXML
	private Button btnLogClear;
	
	
	private MainViewModel model;
	
	private ObservableList<P2PFile> data = FXCollections.observableArrayList();
	
	public MainView(MainViewModel model){
		this.model = model;
	}
	
	@FXML
	public void initialize(){		
		
		btnRemove.setDisable(true);
		tableFiles.setPlaceholder(new Label("No files shared!"));
		
		tableColumnFile.setCellValueFactory(new PropertyValueFactory<P2PFile,String>("name"));
		tableColumnFullPath.setCellValueFactory(new PropertyValueFactory<P2PFile,String>("path"));
		tableColumnType.setCellValueFactory(new PropertyValueFactory<P2PFile,String>("type"));
		tableColumnSize.setCellValueFactory(new PropertyValueFactory<P2PFile,Integer>("size"));
		tableColumnDate.setCellValueFactory(new PropertyValueFactory<P2PFile,String>("date"));
		tableColumnID.setCellValueFactory(new PropertyValueFactory<P2PFile,Integer>("id"));
		tableFiles.setItems(data);
		
		tableFiles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		        btnRemove.setDisable(false);
		    }else{
		    	btnRemove.setDisable(true);
		    }
		});
		
		Logger.addListener(new LoggerListener(){

			@Override
			public void newEntire(String entrie) {
				txtAreaLog.appendText(entrie+"\n");				
			}
			
		});

		
	}
	
	
			
	public void btnAddFileClicked(){
		Logger.write("[MainView] Btn add file clicked!");
		P2PFile f = model.addFile();
		
		if(f != null){
			data.add(f);
		}
		
	}
	
	public void btnAddDirectoryClicked(){
		Logger.write("[MainView] Btn add directory clicked!");
		List<P2PFile> files = model.addDirectory();
		
		if(files != null){
			for(P2PFile f : files){
				data.add(f);
			}
		}
		
	}
	
	public void btnRemoveClicked(){
		Logger.write("[MainView] Btn remove clicked!");
		
		P2PFile selectedFile = tableFiles.getSelectionModel().getSelectedItem();
		
		if(model.removeFile(selectedFile.getId())){
			int index = tableFiles.getSelectionModel().getSelectedIndex();
			data.remove(index);
		}
		
		tableFiles.getSelectionModel().clearSelection();
		
	}
	
	public void btnLogClearClicked(){
		Logger.write("[MainView] Btn log clear clicked!");
		txtAreaLog.clear();
	}

}
