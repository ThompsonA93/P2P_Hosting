
package logic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import leecher.Leecher;

/** Refactored from View for less bile on the laptop - Configures core-mechanics in View
 * TODO At some later time. Throws really weird bugs once realized in .View.java
 */
public class ComponentConfigurator {
	public void configureForceFetchCheckBox(CheckBox cb) {
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					Leecher.logger.write("# Force Fetch has been activated. \n\t We hope you know what you are doing.");	
				}else {
					Leecher.logger.write("# Force Fetch has been deactivated. \n\t Assuming default execution.");
				}
                				
			}
        });		
	}

	public void configureLoggerArea(TextArea logArea) {
		Leecher.logger.setlogArea(logArea);		
	}

	public void configureConnectButton(Button connectButton, TextField ipField, TextField portField, TextField resourceField, ObservableList<SeederData> data) {
	connectButton.setOnAction(d -> {
		
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
					SeederData resourceToFetch = data.get(data.indexOf(id)); // FIXME?
					seederCon.getResourceFromSeeder(resourceToFetch);
				}catch(ArrayIndexOutOfBoundsException ae) {
					Leecher.logger.write("# The resources data has not been pre-fetched to the table.\n\tConnect to a root server first.");
				}					
			}				
		});	// End of connectbutton
	}

	public void configureHelpButton(Button helpButton) {
		helpButton.setOnAction(d -> {
			Leecher.logger.help();
		});		
	}

	public void configureExitButton(Button exitButton) {
		exitButton.setOnAction(d -> {
			System.exit(0);		
		}); 		
	}
}
