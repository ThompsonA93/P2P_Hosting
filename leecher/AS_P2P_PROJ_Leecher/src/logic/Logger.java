package logic;

import javafx.scene.control.TextArea;

/** Logger used for the Leecherclient, used within Global Context
 */
public class Logger {
	TextArea logArea;
	
	public Logger() {}
	
	public void write(String text) {
		logArea.appendText(text+"\n");		
	}
	
	public void help() {
		logArea.setText("| Welcome to the Leecher-Client |\n"
				+ " +-+-+-+-+-+-+-+-+-+-+-+-+-+\n Connect to Root Server :: "
				+ "To connect to a root server, state it's IP and Port in thei respective fields to the upper left."
				+ " Leave the Field for resources empty and press 'Connect'\n"
				+ " +-+-+-+-+-+-+-+-+-+-+-+-+-+\n Connect to Seeder :: "
				+ "To secure the connection to seeder, you require the verified root-servers information."
				+ " You specify the data you wish to download via the seeders IP and Port, aswell as the data's ID."
				+ "The transmitted data will be written in the directory of the leecher-executable.\n"
				+ " +-+-+-+-+-+-+-+-+-+-+-+-+-+\n"
				+ "## Assuming Log-Functionality ##\n");
	}

	public void setlogArea(TextArea logArea) {
		this.logArea = logArea;
	}
}
