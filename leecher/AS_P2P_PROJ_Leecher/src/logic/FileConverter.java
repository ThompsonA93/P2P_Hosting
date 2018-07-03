package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

import leecher.Leecher;

/**
 * Class to handle File transformations.
 */
public class FileConverter {
	final String wrkdir = "/LeecherDownloads";
	
	/** Decodes downloaded data. Per default B64-Encoded.
	 * @param payload
	 * @return byte[] - the payload
	 */
	public byte[] decodeB64(byte[] payload) {
		Leecher.logger.write("### Decoding Base64-Payload");
		byte[] plaintext = Base64.getMimeDecoder().decode(payload);
		return plaintext;
	}
	
	/** Converts byte[] to files. Supports .pdf, .mp3, .jpg, .png, .doc, otherwise is .txt
	 * 
	 * TODO: Check if the datafiles are ACTUALLY converted correctly. Revision has shown that 
	 * not every filetype may be written as given below.
	 * 
	 * @param data: Content as byte[]
	 * @param name: Resources name
	 * @param type: suffix of file
	 */
	public void convertToFile(byte[] data, String name, String type) {
		Leecher.logger.write("### Converting Byte to File ");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String fileName = name.concat("_"+ts.getTime());
		
		switch(type) {
		case "pdf": 
			Leecher.logger.write("# Converting to '.pdf'");
			fileName += ".pdf"; 
			break;
		case "mp3":
			Leecher.logger.write("# Converting to '.mp3'");
			fileName += ".mp3"; 
			break;
		case "jpg":
			Leecher.logger.write("# Converting to '.jpg'");
			fileName += ".jpg"; 
			break;
		case "png":
			Leecher.logger.write("# Converting to '.png'");
			fileName += ".png"; 
			break;
		case "doc":
			Leecher.logger.write("# Converting to '.doc'");
			fileName += ".doc"; 
			break;
		default:
			Leecher.logger.write("# Could not determine Type.\n\tConverting as .txt");
			fileName += ".txt";
		}
		
		File file = new File(wrkdir, fileName);
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			Leecher.logger.write("# Successfully written to file: " + wrkdir + "/" + fileName);
		} catch (FileNotFoundException e) {
			Leecher.logger.write("# Creating the file has failed.");
			e.printStackTrace();
		} catch (IOException e) {
			Leecher.logger.write("# Stream to File experienced an fault.");
			e.printStackTrace();
		}
	}
}
