package populator.random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses a data file and creates a {@code DataSet<String>}.<br>
 * <br>
 * <strong> Create an unweighted {@code DataSet}</strong><br>
 * To create an unweighted {@code DataSet}, the data must be in the form:<br>
 * {@code [data1]}<br>
 * {@code [data2]}<br>
 * {@code ...}<br>
 * Where each item of data is on a separate line. The square brackets are a placeholder only and
 * should not form a part of your data file (unless they form a part of the data).<br>
 * <br>
 * <strong>Create a weighted {@code DataSet}</strong><br>
 * To create a weighted {@code DataSet}, the data file must be in the form:<br>
 * {@code [data1][delimiter][frequency1]}<br>
 * {@code [data2][delimiter][frequency2]}<br>
 * {@code ...}<br>
 * Where each item of data is on a separate line. The square brackets are a placeholder only and
 * should not form a part of your data file (unless they form a part of the data).
 * 
 * @see DataSet
 * 
 * @author Richard Innocent
 *
 */
public class DataSetParser {
	
	/**
	 * Creates a DataSet by giving a filepath from which the data will be read. Data is added a
	 * line at a time, hence all of the different sets of data within the data file must be on
	 * a new line. The {@code DataType} will be initialised to {@code DataType.OTHER}.
	 * @param filepath The filepath of the data.
	 * @param dataType The type of data. If this being used as a standalone DataSet, DataType.OTHER
	 * can be used. Otherwise, use the appropriate DataSet.
	 * @throws IOException Thrown if there is a problem reading at the specified filepath.
	 * @return A new {@code DataSet} with the data specified from the data file.
	 */
	public static DataSet<String> parseData(String filepath) throws IOException {
		return parseData(filepath, DataType.OTHER);
	}
	
	/**
	 * Creates a {@code DataSet} by giving a filepath from which the data will be read. Data is
	 * added a line at a time, hence all of the different sets of data within the data file must be
	 * on a new line.
	 * @param filepath The filepath of the data.
	 * @param dataType The type of Data. If this being used as a standalone {@code DataSet}, {@code
	 * DataType.OTHER} can be used. Otherwise, use the appropriate {@code DataSet}.
	 * @throws IOException Thrown if there is a problem reading at the specified filepath.
	 * @return A new {@code DataSet} with the data specified from the data file.
	 */
	public static DataSet<String> parseData(String filepath, DataType dataType)
		throws IOException {
		ArrayList<String> data = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		try {
			// Take the first non-white space element
			String line;
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				// Ignores comments
				if (line.startsWith("//")) {
					continue;
				}
				
				data.add(line);
			}
		} finally {
			reader.close();
		}
		
		return new DataSet<String>(data, dataType);
		
	}
	
	/**
	 * Creates a weighted {@code DataSet} by giving a filepath from which the data will be read.
	 * The {@code DataType} will be defined as {@code DataType.OTHER}. The delimiter allows data to
	 * be entered in the text file as follows:<br>
	 * 	{@code [data][delimiter][weighting]}<br>
	 * where the weighting can be parsed to a double and is non-negative. Note that the square
	 * brackets should not be a part of the file (unless part of the data section or set as the
	 * delimiter).<br>
	 * The weighting is the probability that the particular data is returned when using the {@code
	 * getWeightedValue()} method is called.
	 * Data is added a line at a time, hence all of the different sets of data within the data file
	 * must be on a new line.
	 * @param filepath The filepath of the data.
	 * @param dataType The type of Data. If this being used as a standalone DataSet, DataType.OTHER
	 * can be used. Otherwise, use the appropriate DataSet.
	 * @param delimiter The separator between the data and the frequency.
	 * @throws IOException
	 * Thrown if there is a problem reading the specified file.
	 * @throws IllegalArgumentException
	 * Thrown if the weighting for a line is negative.
	 * @throws RuntimeException
	 * Thrown if there is no delimiter on one of the uncommented lines.
	 * @return The {@code DataSet} object with the data and weightings specified from the file.
	 */
	public static DataSet<String> parseData(String filepath, String delimiter)
			throws IOException, IllegalArgumentException, RuntimeException {
		return parseData(filepath, DataType.OTHER, delimiter);
	}
	
	/**
	 * Creates a weighted {@code DataSet} by giving a filepath from which the data will be read.
	 * The delimiter allows data to be entered in the text file as follows:<br>
	 * 	{@code [data][delimiter][weighting]}<br>
	 * where the weighting can be parsed to a double and is non-negative.
	 * The weighting is the probability that the particular data is returned when using the
	 * {@code getWeightedValue()} method is called.
	 * Data is added a line at a time, hence all of the different sets of data within the data file
	 * must be on a new line.
	 * @param filepath The filepath of the data.
	 * @param dataType The type of data. If this being used as a standalone {@code DataSet}, {@code
	 * DataType.OTHER} can be used. Otherwise, use the appropriate {@code DataSet}.
	 * @param delimiter The separator between the data and the frequency.
	 * @throws IOException
	 * Thrown if there is a problem reading the specified file.
	 * @throws IllegalArgumentException
	 * Thrown if the weighting for a line is negative, or if the specified delimiter is an empty
	 * {@code String}.
	 * @throws RuntimeException
	 * Thrown if there is no delimiter on one of the uncommented lines.
	 * @return The {@code DataSet} object with the data and weightings specified from the file.
	 */
	public static DataSet<String> parseData(String filepath, DataType dataType, String delimiter)
			throws IOException, IllegalArgumentException, RuntimeException {
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<Double> weightings = new ArrayList<Double>();
		
		if (delimiter.isEmpty())
			throw new IllegalArgumentException("Delimiter is empty.");

		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		try {
			// Take the first non-white space element
			String line;
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				
				lineNumber++;
				line = line.trim();
				
				// Ignore comments
				if (line.startsWith("//") || line.isEmpty()) {
					continue;
				}

				// If no delimiter is found, user must have made a mistake
				if (!line.contains(delimiter))
					throw new RuntimeException("No delimiter (" + delimiter + ") found at line " +
							line);
				
				String [] lineComponents = line.split(delimiter);
				
				try {
					Double weighting = Double.valueOf(lineComponents[1]);
					data.add(lineComponents[0]);
					
					if (weighting < 0) {
						throw new IllegalArgumentException("Weighting is negative at line "
								+ lineNumber);
					}
					
					weightings.add(weighting);
					
				} catch (NumberFormatException e) {
					System.out.println("Line " + lineNumber +
							"'s weighting is not formatted correctly.");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Line " + lineNumber +
							" does not contain enough components.");
				}
			}
		} finally {
			reader.close();
		}
		
		return new DataSet<String>(data, weightings, dataType);
		
	}
}
