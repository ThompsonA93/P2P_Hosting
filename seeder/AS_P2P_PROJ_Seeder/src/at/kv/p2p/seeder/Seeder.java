package at.kv.p2p.seeder;
	
import at.kv.p2p.seeder.logic.P2PSharingManager;
import at.kv.p2p.seeder.logic.Server;
import at.kv.p2p.seeder.logic.ServerFactory;
import at.kv.p2p.seeder.models.MainViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class Seeder extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		
		
		MainViewModel m = new MainViewModel(primaryStage);
		m.show();
		
		Server server = ServerFactory.getServer(SeederProperties.OWN_SERVER_PORT);
		server.start();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  P2PSharingManager.removeAllFiles();
	        	  server.stop();
	              System.exit(0);
	          }
	      });      
		
	}	
	
	public static void main(String[] args) {
		
		System.out.println("Arguments:");
		System.out.println("<central server ip> <central server port> <own server address> <own server port>");
		
		if(args.length > 3){
			SeederProperties.CENTRAL_SERVER_ADDRESS = args[0];
			SeederProperties.CENTRAL_SERVER_PORT = Integer.parseInt(args[1]);
			SeederProperties.OWN_SERVER_ADDRESS = args[2];
			SeederProperties.OWN_SERVER_PORT = Integer.parseInt(args[3]);
		}else{
			System.out.println("No arguments!");
			System.out.println("Starting with default arguments!");
		}
		
		System.out.println("Central server address = ["+SeederProperties.CENTRAL_SERVER_ADDRESS +"]");
		System.out.println("Central server port = ["+SeederProperties.CENTRAL_SERVER_PORT +"]");
		System.out.println("Own server address = ["+SeederProperties.CENTRAL_SERVER_ADDRESS +"]");
		System.out.println("Own server port = ["+SeederProperties.OWN_SERVER_PORT +"]");
		
		launch(args);
	}
}
