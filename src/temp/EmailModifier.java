package temp;

import java.io.*;

public class EmailModifier {

	public static void main(String [] args) {
		
		String fullDoc = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/populator/random/data/email_domains-TEMP.txt"));
			String line;
			boolean firstLine = true;
			try {
				while ((line = reader.readLine()) != null) {
					String [] lineComponents = line.split("\\s+");
					
					if (!firstLine) {
						fullDoc += "\r\n";
					} else {
						firstLine = false;
					}
					
					fullDoc += lineComponents[0] + "%" + lineComponents[1].replace(",", "");
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/populator/random/data/email_domains-TEMP-mod.txt"));
			
			try {
				writer.write(fullDoc);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
}
