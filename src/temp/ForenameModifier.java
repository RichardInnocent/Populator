package temp;

import java.io.*;

public class ForenameModifier {

	public static void main(String [] args) {
		
		String fullDoc = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/person/data/female_forenames.txt"));
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
					
					fullDoc += lineComponents[0].charAt(0) + lineComponents[0].toLowerCase().substring(1) + "%" + lineComponents[1];
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/person/data/female_forenames.txt"));
			
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
