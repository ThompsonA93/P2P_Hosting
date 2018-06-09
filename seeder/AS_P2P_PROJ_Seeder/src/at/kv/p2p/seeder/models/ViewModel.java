package at.kv.p2p.seeder.models;

import javafx.stage.Stage;

public abstract class ViewModel {
	
	protected Stage primaryStage;
	
	public ViewModel(Stage primaryStage){		
		this.primaryStage = primaryStage;		
	}
	
	public abstract void show();
	

}
