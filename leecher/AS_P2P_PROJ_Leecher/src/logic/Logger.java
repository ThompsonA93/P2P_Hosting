package logic;

import javafx.scene.control.TextArea;

/**
 * Logger used for the Leecherclient
 * -- Used within Global Context
 */
public class Logger {
	TextArea logArea;
	
	public Logger() {}
	
	public void write(String text) {
		logArea.appendText(text);		
	}
	
	public void help() {
		logArea.setText("-- Welcome to the Leecher-Client --\n"
				+ "1.) Connect to Root Server\n"
				+ "To do so, enter the IP and the Port of a root server that you know of."
				+ " Leave the Field for resources empty.\n"
				+ "2.) Connect to Seeder\n"
				+ "Assuming you have fetched information about possible seeders from the server"
				+ "of your choice, enter the seeders information and the data's ID that u wish to fetch.\n\n"
				+ "## Assuming Log-Functionality ##");
	}

	public void setlogArea(TextArea logArea) {
		this.logArea = logArea;
	}
}
