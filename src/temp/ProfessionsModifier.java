package temp;

import java.io.*;

public class ProfessionsModifier {

	public static void main(String [] args) {
		
		String fullDoc = "";
		
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader("src/populator/random/data/professions-TEMP.txt"));
//			String line;
//			boolean firstLine = true;
//			try {
//				while ((line = reader.readLine()) != null) {
//					String [] lineComponents = line.split("\\s+");
//					
//					if (!firstLine) {
//						fullDoc += "\r\n";
//					} else {
//						firstLine = false;
//					}
//					
//					fullDoc += lineComponents[0].charAt(0) + lineComponents[0].substring(1).toLowerCase() + "%" + lineComponents[2];
//				}
//			} finally {
//				reader.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/populator/random/data/professions-TEMP.txt"));
			String line;
			boolean firstLine = true;
			try {
				while ((line = reader.readLine()) != null) {
					String [] lineComponents = line.split(" # Employed: ");
					String profession = lineComponents[0];
					
					if (profession.endsWith("s")) {
						profession = profession.substring(0, profession.length()-1);
					}
					
					String count = lineComponents[1].replace(",", "");
					
					if (!firstLine) {
						fullDoc += "\r\n";
					} else {
						firstLine = false;
					}
					
					fullDoc += profession + "%" + count;
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/populator/random/data/professions-TEMP-mod.txt"));
			
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
