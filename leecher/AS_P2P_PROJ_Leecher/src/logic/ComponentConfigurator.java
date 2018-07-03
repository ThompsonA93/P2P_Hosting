
package logic;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import leecher.Leecher;

/** Contains the split Logic from View
 */
public class ComponentConfigurator {

	/** Sets the Loggers Area 
	 * @param logArea: Area for the logger to write in.
	 */
	public void configureLoggerArea(TextArea logArea) {
		Leecher.logger.setlogArea(logArea);		
	}

	/**
	 * Sets the Events for the connectbutton.
	 * @param connectButton: Event-Activator
	 * @param ipField: Field to hold ServersIP
	 * @param portField: Field to hold ServersPort
	 * @param resourceField: Field to hold resourceID
	 * @param data: Output Location
	 */
	public void configureConnectButton(Button connectButton, TextField ipField, TextField portField, TextField resourceField, ObservableList<SeederData> data) {
	connectButton.setOnAction(d -> {
		if(ipField.getText().isEmpty()) {
			Leecher.logger.write("# No IP has been specified!");
			// TODO Cancel event
		}
		if(ipField.getText().isEmpty()) {
			Leecher.logger.write("# No Port has been specified!");
			// TODO Cancel event
		}
		
		if(resourceField.getText().isEmpty()) {
			Connector rootServerCon = new Connector();			
			String[] seeders = rootServerCon.getResourcesFromServer(ipField.getText(), Integer.parseInt(portField.getText()));
				
			try {				
				for(String s : seeders) {
					data.add(new SeederData(s));
				}	
			}catch(ArrayIndexOutOfBoundsException e) {
				Leecher.logger.write("# The Connection did not fetch any Data.\n\tMake sure that you specified a root server.");
			}
					
		} else {
			Connector seederCon = new Connector();			
				try {
					int id = Integer.parseInt(resourceField.getText());
					SeederData resourceToFetch  = null;

					for(SeederData sd : data) {
						if(sd.getID() == id) {
							resourceToFetch = sd;
							break;
						}
					}
					
					if(resourceToFetch.equals(null)) {
						// TODO Cancel event
					}
					seederCon.getResourceFromSeeder(resourceToFetch);
				}catch(ArrayIndexOutOfBoundsException ae) {
					Leecher.logger.write("# The resources data has not been pre-fetched to the table.\n\tConnect to a root server first.");
				}					
			}				
		});
	}

	/**
	 * Configures button to call upon 'help'
	 * @param helpButton: Event-Activator
	 */
	public void configureHelpButton(Button helpButton) {
		helpButton.setOnAction(d -> {
			Leecher.logger.help();
		});		
	}

	/**
	 * Configures button to terminate leecher
	 * @param exitButton: Event-Activator
	 */
	public void configureExitButton(Button exitButton) {
		exitButton.setOnAction(d -> {
			System.exit(0);		
		}); 		
	}
}
