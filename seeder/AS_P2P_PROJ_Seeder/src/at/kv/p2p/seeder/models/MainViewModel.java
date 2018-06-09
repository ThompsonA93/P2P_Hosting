package at.kv.p2p.seeder.models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.kv.p2p.seeder.logic.P2PFile;
import at.kv.p2p.seeder.logic.P2PSharingManager;
import at.kv.p2p.seeder.views.MainView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewModel extends ViewModel{
	
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	public MainViewModel(Stage primaryStage) {
		super(primaryStage);
	}
		
	@Override
	public void show(){
		
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/kv/p2p/seeder/views/MainView.fxml"));		
			
			//set controller and its constructor
			loader.setController(new MainView(this));
		
			Parent root = loader.load();
			
			Scene scene = new Scene(root);				
		
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public P2PFile addFile(){
		
		FileChooser fileChooser = new FileChooser();        
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
                    	
        	P2PFile f = new P2PFile(getFileName(file),file.getAbsolutePath(),(int)file.length(),fileDate(file),getFileType(file));
        	
            if(P2PSharingManager.addFile(f)){
            	return f;
            }
            
        }
		
		return null;
	}
	
	public List<P2PFile> addDirectory(){
		
		DirectoryChooser dirChooser = new DirectoryChooser();        
        File file = dirChooser.showDialog(primaryStage);
        if (file != null) {
            
        	List<P2PFile> files = new ArrayList<>();
        	
        	for(File f : file.listFiles()){
        		if(f.isFile()){
        			
        			P2PFile p2pFile = new P2PFile(getFileName(f),f.getAbsolutePath(),(int)f.length(),fileDate(f),getFileType(f));
        			
        			files.add(p2pFile);
        			
        			P2PSharingManager.addFile(p2pFile);
        			
        		}
        	}
        	
        	return files;
            
        }
		
		return null;
		
	}
	
	public boolean removeFile(int id){
		if(P2PSharingManager.removeFile(id)){
			return true;
		}
		return false;
	}

	private String fileDate(File f){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);		
		return sdf.format(f.lastModified());
	}	
	
	private String getFileName(File f){
		return f.getName().substring(0, f.getName().lastIndexOf("."));
	}
	
	private String getFileType(File f){
		return f.getName().substring(f.getName().lastIndexOf("."), f.getName().length());
	}

}
