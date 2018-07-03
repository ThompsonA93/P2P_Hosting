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
	
	/** Decodes downloaded data. Per default B64-Encoded.
	 * @param ciphertext
	 * @return plaintext
	 */
	public byte[] decodeB64(byte[] payload) {
		Leecher.logger.write("### Decoding Base64-Payload");
		byte[] plaintext = Base64.getMimeDecoder().decode(payload);
		return plaintext;
	}
	
	/** Converts byte[] to files. Supports .pdf, .mp3, .jpg, .png, .doc, otherwise is .txt
	 * TODO: Convert correctly - .png and .pdf are misswritten.
	 * @param data: Content as byte[]
	 * @param name: Resources name
	 * @param type: suffix of file
	 */
	public void convertToFile(byte[] data, String name, String type) {
		Leecher.logger.write("### Converting Byte to File ");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String fileName = name.concat("_"+ts.getTime());
		
		switch(type) {
		case ".pdf": 
			Leecher.logger.write("# Converting to '.pdf'");
			fileName += ".pdf"; 
			break;
		case ".mp3":
			Leecher.logger.write("# Converting to '.mp3'");
			fileName += ".mp3"; 
			break;
		case ".jpg":
			Leecher.logger.write("# Converting to '.jpg'");
			fileName += ".jpg"; 
			break;
		case ".png":
			Leecher.logger.write("# Converting to '.png'");
			fileName += ".png"; 
			break;
		case ".doc":
			Leecher.logger.write("# Converting to '.doc'");
			fileName += ".doc"; 
			break;
		default:
			Leecher.logger.write("# Could not determine Type.\n\tConverting as .txt");
			fileName += ".txt";
		}
				
		File file = new File(fileName);
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			Leecher.logger.write("# Successfully written to file: " + file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			Leecher.logger.write("# Creating the file has failed.");
			e.printStackTrace();
		} catch (IOException e) {
			Leecher.logger.write("# Writing to File has failed.");
			e.printStackTrace();
		}
	}
}
