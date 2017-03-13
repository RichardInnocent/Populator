package populator.random;

import java.util.*;
import java.util.function.Predicate;
import java.io.*;

/**
 * The {@code DataSet} provides the ability to created a list of weighted or unweighted data, and
 * then provides a simple method of extracting random elements from this. This class is immutable
 * and thread-safe.<br>
 * <br>
 * <strong>Weighted Data</strong><br>
 * Data that represents frequency distributions among its components. For example, if a computer
 * algorithm was generating random letters, there's a 21/26 chance that the next letter would be
 * a consonant, and only a 5/26 chance of a vowel.<br>
 * <br>
 * For this type of data, the frequency for each piece of data should be specified when creating
 * the {@code DataSet}. This can either be specified by passing in two {@code List}s to the
 * constructor - one for the data, and one for the weightings, or by passing in the filepath to a
 * file with a specified delimiter that will separate the data from its corresponding frequency.
 * <br><br>
 * Data can then be pulled back out of the {@code DataSet} in similar proportions to those
 * specified in the given frequencies. For example, using the random letter example, calling
 * {@code getWeightedValue()} 26 times should roughly 21 returns of {@code "consonant"} and 5
 * {@code "vowel"}s.<br>
 * <br>
 * Even though the weightings have been specified, it is still possible to then pull the data back
 * out completely randomly if desired, using the {@code getRandomValue()} method.<br>
 * <br>
 * <strong>Unweighted Data</strong><br>
 * Data where each piece of data has the same probability of being selected, such as rolling a
 * dice. For this type of data, the weightings do not need to be specified, and both {@code
 * getRandomValue()} and {@code getWeightedValue()} will return a random piece of data without
 * any biasing.<br>
 * <br>
 * <strong>Setting the {@code DataType}</strong><br>
 * The {@code DataType} must be used when being used in conjunction with any specific generators in
 * this package. If the {@code DataSet} is being used as a stand-alone instance, the type should be
 * set to {@code {@link DataType#OTHER}}. Otherwise, please see the documentation for the
 * particular class to see which {@code DataType} should be used.<br>
 * <br>
 * <strong>Notes</strong>
 * <list><li>This class implements the {@link #Iterable} interface so a foreach loop can be used.</li>
 * <li>This class implements the {@link Serializable} interface.</li></list><br>
 * 
 * @see DataType
 * 
 * @author Richard Innocent
 *
 */
public final class DataSet<T> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = -2387264411104162405L;
	private final List<T> data;
	private List<Double> weightings;
	private final DataType dataType;
	private final static Random random = new Random();
	private double weightingSum = 0;
	private boolean isWeighted = false;
	
	/**
	 * Creates a {@code DataSet} by directly giving the data and the type. When initialised in this
	 * manner, {@code DataType} is initialised to {@code DataType.OTHER}, and hence it cannot be
	 * used with other generator classes in this package.
	 * @param data An {@code List} of data that should be stored in this {@code DataSet}. Note that
	 * the {@code List} is copied, so changing the referenced {@code List} will not affect the
	 * {@code DataSet}.
	 * @throws IllegalArgumentException
	 * Thrown if the data {@code List} is empty.
	 */
	public DataSet(List<T> data) throws IllegalArgumentException {
		if (data.isEmpty())
			throw new IllegalArgumentException("The data consists of no elements.");
		this.data = new ArrayList<T>(data);
		this.dataType = DataType.OTHER;
	}
	
	/**
	 * Creates a {@code DataSet} by directly giving the data and the type.
	 * @param data A {@code List} of data that should be stored in this {@code DataSet}. Note that
	 * the {@code List} is copied, so changing the referenced {@code List} will not affect the
	 * {@code DataSet}.
	 * @param dataType The type of data. If this being used as a stand-alone {@code DataSet}, {@code
	 * DataType.OTHER} can be used. Otherwise, use the appropriate {@code DataSet}.
	 * @throws IllegalArgumentException
	 * Thrown if the data {@code List} is empty.
	 */
	public DataSet(List<T> data, DataType dataType) throws IllegalArgumentException {
		if (data.isEmpty())
			throw new IllegalArgumentException("The data consists of no elements.");
		this.data = new ArrayList<T>(data);
		this.dataType = dataType;
	}
	
	/**
	 * Creates a weighted {@code DataSet}. Weighted means that there is a given probability that a
	 * particular data value is returned when {@code getWeightedValue()} is called. The {@code
	 * DataType} is automatically initialised to {@code DataType.OTHER}.
	 * @param data An {@code List} of data that should be stored in this {@code DataSet}. Note that
	 * the {@code List} is copied, so changing the referenced {@code List} will not affect the
	 * {@code DataSet}.
	 * @param weightings A {@code List} of {@code Double} values that represent the frequencies of
	 * each data piece (in the same order). This must not contain any negative values.
	 * @throws IllegalArgumentException
	 * Thrown if the weightings is the same size as the data {@code List}, or if the weighting
	 * {@code List} contains negative numbers.
	 */
	public DataSet(List<T> data, List<Double> weightings) throws IllegalArgumentException {
		this(data, weightings, DataType.OTHER);
	}
	
	/**
	 * Creates a weighted {@code DataSet}. Weighted means that there is a given probability that a
	 * particular data value is returned when {@code getWeightedValue()} is called.
	 * @param data An {@code List} of {@code String} data that should be stored in this {@code
	 * DataSet}. Note that the {@code List} is copied, so changing the referenced {@code List} will
	 * not affect the {@code DataSet}.
	 * @param weightings A {@code List} of {@code Double} values that represent the frequencies of
	 * each data piece (in the same order). This must not contain any negative values.
	 * @param dataType The type of data. If this being used as a stand-alone {@code DataSet},
	 * {@code DataType.OTHER} can be used. Otherwise, use the appropriate {@code DataSet}.
	 * @throws IllegalArgumentException
	 * Thrown if the weightings is the same size as the data {@code List}, or if the weighting
	 * {@code List} contains negative numbers.
	 */
	public DataSet(List<T> data, List<Double> weightings, DataType dataType)
			throws IllegalArgumentException {
		
		this(data, dataType);
		
		// Checking sizes
		if (data.size() != weightings.size())
			throw new IllegalArgumentException(
					"The data and weightings Lists are of different sizes.");
		
		// Negative numbers will corrupt calculation
		for (int i = 0; i < weightings.size(); i++) {
			if (weightings.get(i) < 0) {
				throw new IllegalArgumentException("Weighting List contains negative number at index "
						+ i);
			}
		}
		
		this.weightings = weightings;
		calculateSumOfWeightings();
		isWeighted = true;
	}
	
	/**
	 * This method returns a random piece of data from the {@code DataSet}, ignoring any weightings
	 * that may have been implemented.
	 * @return A piece of data.
	 */
	public T getRandomValue() {
		return data.get(random.nextInt(data.size()));
	}
	
	/** This method returns a random piece of data from the {@code DataSet}, according to any
	 * weightings that have been implemented. if the weightings have not been set, each data piece
	 * will have an equal probability of being returned.
	 * @return A piece of data.
	 */
	public T getWeightedValue() {
		if (weightings == null || weightings.isEmpty())
			return getRandomValue();
		
		double value = random.nextDouble() * weightingSum;
		double runningTotal = 0;
		
		for (int i = 0; i < data.size(); i++) {
			runningTotal += weightings.get(i);
			if (value <= runningTotal) {
				return data.get(i);
			}
		}
		
		// If there's a problem
		return null;
		
	}
	
	/**
	 * Returns a {@code List} containing the different pieces of data.
	 * @return The {@code List} of data.
	 */
	public List<T> getData() {
		return data;
	}
	
	/**
	 * Returns the type of data that this {@code DataSet} stores.
	 * @return The {@code DataType} for the {@code DataSet}.
	 * @see DataType
	 */
	public DataType getDataType() {
		return dataType;
	}
	
	/**
	 * Returns the weightings that have been set for this {@code DataSet}.
	 * @return The {@code List} of weightings.
	 */
	public List<Double> getWeightings() {
		return weightings;
	}
	
	/**
	 * Searches the {@code DataSet} for the given piece of data, and returns the corresponding
	 * weighting. If no equivalent data is found, or if the weightings have not been set, this
	 * method returns {@code -1}.
	 * @param item The piece of data that the weighting is required for.
	 * @return The weighting.
	 */
	public double getWeightingFor(String item) {
		if (weightings == null)
			return -1;
		
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).equals(item))
				return weightings.get(i);
		}
		return -1;
	}
	
	/**
	 * Returns the grand sum of weightings that have been specified for this {@code DataSet}. If
	 * the weightings have not been set, this method returns {@code 0}.
	 * @return The grand sum of weightings.
	 */
	public double getSumOfWeightings() {
		return weightingSum;
	}
	
	/**
	 * Returns the probability that the piece of data will be returned when using weighted calls.
	 * If the data cannot be found, this method returns -1. If the weightings have not been set,
	 * this method will return the probability that the item is chosen at random.
	 * @param data The data whose probability is requested.
	 * @return The probability that the data will be returned when using weighted calls.
	 */
	public double getProbabilityOf(String data) {
		if (weightingSum == 0) {
			return 1.0 / data.length();
		} else {
			return getWeightingFor(data) / getSumOfWeightings();
		}
	}
	
	/**
	 * Returns whether or not the given piece of data is contained within the {@code DataSet}.
	 * @param data The piece of data to be searched for.
	 * @return {@code true} if the {@code DataSet} contains an equivalent piece of data.
	 */
	public boolean contains(Object data) {
		for (Object instance : this.data) {
			if (instance.equals(data))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the number of different entries in the {@code DataSet}.
	 * @return The number of elements.
	 */
	public int size() {
		return data.size();
	}
	
	/**
	 * Returns whether or not the weightings have been set up for this {@code DataSet}.
	 * @return '{@code true}' if the weightings have been set up. 
	 */
	public boolean isWeighted() {
		return isWeighted();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			int numberOfElements = size();
			int currentIndex = 0;
			
			@Override
			public boolean hasNext() {
				return currentIndex < numberOfElements;
			}

			@Override
			public T next() {
				return data.get(currentIndex++); 
			}
			
		};
	}
	
	@Override
	public String toString() {
		return (isWeighted ? "Weighted" : "Unweighted") + " DataSet of type " + dataType + " with "
				+ size() + " elements";
	}
	
	@Override
	public int hashCode() {
		return new Boolean(isWeighted).hashCode() + 19 * dataType.hashCode() + 7 * size();
	}
	
	/**
	 * Returns a new {@code DataSet} of data that matches specific criteria.
	 * @param predicate The criteria for the data.
	 * @return A new {@code DataSet} of data matching specific criteria.
	 */
	public DataSet<T> getMatching(Predicate<T> predicate) {
		List<T> matchingData = new ArrayList<>();
		List<Double> matchingWeightings = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			if (predicate.test(data.get(i))) {
				matchingData.add(data.get(i));
				if (isWeighted) {
					matchingWeightings.add(weightings.get(i));
				}
			}
		}
		return isWeighted ? new DataSet<T>(matchingData) :
			new DataSet<T>(matchingData, matchingWeightings);
	}
	
	/**
	 * This method calculates the sum of the weightings and sets the {@code weightingSum} variable.
	 */
	private void calculateSumOfWeightings() {
		weightingSum = 0;
		for (double weighting : weightings) {
			weightingSum += weighting;
		}
	}

}
