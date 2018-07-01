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
		Leecher.logger.write("### Decoding Payload");
		byte[] plaintext = Base64.getMimeDecoder().decode(payload);
		return plaintext;
	}
	
	/** Converts byte[] to files.
	 * Note: Timestamps are used as filenames
	 * FIXME: There are different types of files you ploughin idiot
	 */
	public void convertToFile(byte[] data) {
		Leecher.logger.write("### Converting Byte to File ");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String fileName = ""+ts.getTime();
		File file = new File(wrkdir, fileName);
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			Leecher.logger.write("# Successfully written to file.");
		} catch (FileNotFoundException e) {
			Leecher.logger.write("# Specified File to write to was not found.");
			e.printStackTrace();
		} catch (IOException e) {
			Leecher.logger.write("# Stream to File experienced an fault.");
			e.printStackTrace();
		}

	}

}
