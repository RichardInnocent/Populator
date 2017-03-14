package populator.random;

import java.io.IOException;
import java.util.*;

/**
 * This class provides a quick way for generating person data. It can randomly generate any of the
 * following data:
 * <list><li>Male/female forename</li>
 * <li>Surname</li>
 * <li>Professions (in the male or female form)</li>
 * <li>Email domain</li></list>
 * <br>
 * <strong>Standard {@code DataSet}s</strong><br>
 * Under most circumstances, it is expected that the standard data set will be used to generate
 * data. The standard {@code DataSet}s used are all weighted, and aim to provide a roughly
 * representational set of data, based surveys conducted on the population of the USA.<br>
 * <br>
 * To use the standard {@code DataSets}s, call {@code PersonGenerator.standardDataSet()}.<br>
 * <br>
 * <strong>Custom {@code DataSet}s</strong><br>
 * It is possible to use custom {@code DataSet}s from which to generate data for more specific
 * cases. To use custom {@code DataSet}s, the {@code DataSet} should first be created
 * independently, and its {@code DataType} should be set to reflect its purpose. The accepted
 * {@code DataType}s for the {@code DataSet}s are:
 * <list><li>MALE_FORENAMES</li>
	<li>FEMALE_FORENAMES</li>
	<li>SURNAMES</li>
	<li>MALE_PROFESSIONS</li>
	<li>FEMALE_PROFESSIONS</li>
	<li>EMAIL_DOMAINS</li></list>
	<br>
 * @see DataType
 * @see DataSet
 * @author Richard Innocent
 *
 */
public class PersonGenerator {
	
	private static Random random = new Random(); // Generates random genders
	private DataSet<String> maleForenames, femaleForenames, surnames, maleProfessions,
		femaleProfessions, emailDomains;

	private PersonGenerator() {}
	
	/**
	 * Will create a {@code PersonGenerator} object using the standard {@code DataSet}s. This is a
	 * large set of data which can provide versatile results, but can take up a large amount of
	 * RAM. Note, however, that data will not be read in unless it is required - e.g. forenames
	 * will not be stored until the {@code getRandomForename()} method is called;
	 * @return A new {@code PersonGenerator} object that will use the standard {@code DataSet}s.
	 */
	public static PersonGenerator standardDataSet() {
		return new PersonGenerator();
	}
	
	/**
	 * Will create a {@code PersonGenerator} object using custom {@code DataSet}s. Note that a
	 * {@code PersonGenerator} object created with custom {@code DataSet}s will use the standard
	 * {@code DataSet}s for any data that is not provided, if it is required. For example, calling
	 * this method and providing only a {@code DataSet} of {@code DataType.PROFESSION}, but then
	 * calling the {@code getRandomForename()} function later will cause the standard {@code
	 * DataSet} to be used for the forenames.
	 * Permitted DataTypes:<list>
	 *   <li>{@code DataType.MALE_FORENAMES}</li>
	 *   <li>{@code DataType.FEMALE_FORENAMES}</li>
	 *   <li>{@code DataType.SURNAMES}</li>
	 *   <li>{@code DataType.PROFESSIONS}</li></list><br>
	 * Any other {@code DataType}s will not be accepted. If two sets of the same {@code DataType}
	 * are given, the last given {@code DataSet} of that {@code DataType} will be used.
	 * @param data The {@code DataSet} that should be used in replace of one of the standard {@code
	 * DataSet}s.
	 * @return A new {@code PersonGenerator} object that will use the custom {@code DataSet}s where
	 * possible.
	 * @see DataType
	 * @see DataSet
	 */
	@SafeVarargs
	public static PersonGenerator customDataSet(DataSet<String>... data) {
		PersonGenerator personGenerator = new PersonGenerator();
		
		for (DataSet<String> dataSet : data) {
			switch (dataSet.getDataType()) {
			
			case MALE_FORENAMES:
				personGenerator.maleForenames = dataSet;
				break;
			case FEMALE_FORENAMES:
				personGenerator.femaleForenames = dataSet;
				break;
			case SURNAMES:
				personGenerator.surnames = dataSet;
				break;
			case MALE_PROFESSIONS:
				personGenerator.maleProfessions = dataSet;
				break;
			case FEMALE_PROFESSIONS:
				personGenerator.femaleProfessions = dataSet;
				break;
			case EMAIL_DOMAINS:
				personGenerator.emailDomains = dataSet;
				break;
			default:
				System.err.println(
						"The following DataSet cannot be used to create custom data for a Person:\n"
				+ dataSet);
			}
		}
		
		return personGenerator;
	}
	
	/**
	 * Gets a random forename from the {@code DataSet},  ignoring any weightings that may have been
	 * implemented. There is a 50% chance that the forename will be male.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return A random forename as a String.
	 */
	public String getRandomForename() {
		return (random.nextBoolean()) ? getRandomMaleForename() : getRandomFemaleForename();
	}
	
	/**
	 * Gets a random forename from the {@code DataSet}, ignoring any weightings that may have been
	 * implemented.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @param maleProbability
	 * The likelihood that the forename will be male. It must be a value between 0 and 1 - the
	 * closer to 1, the more likely that the forename will be male. If the value is out of range,
	 * the parameter will be ignored and the probability of returning a forename for either gender
	 * will be even.
	 * @return A forename.
	 */
	public String getRandomForename(double maleProbability) {
		if (maleProbability < 0 || maleProbability > 1) {
			// Probability is out of range - use default
			return getRandomForename();
		} else if (maleProbability == 0) {
			return getRandomFemaleForename();
		} else if (maleProbability == 1) {
			return getRandomMaleForename();
		} else {
			if (random.nextDouble() <= maleProbability) {
				return getRandomMaleForename();
			} else {
				return getRandomFemaleForename();
			}
		}
	}
	
	/**
	 * Gets a random forename from the {@code DataSet}, according to the weightings. If the
	 * weightings have not been configured, each forename will have an equal probability of being
	 * chosen. There is a 50% chance that the forename will be male.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return A forename.
	 */
	public String getWeightedForename() {
		return (random.nextBoolean()) ? getWeightedMaleForename() :
			getWeightedFemaleForename();
	}
	
	/**
	 * Gets a random forename from the {@code DataSet}, with probabilities of each name set
	 * according to the weightings given.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @param maleProbability
	 * The likelihood that the forename will be male. It must be a value between 0 and 1 - the
	 * closer to 1, the more likely that the forename will be male. If the value is out of range,
	 * the parameter will be ignored and the probability of returning a forename for either gender
	 * will be even.
	 * @return A forename.
	 */
	public String getWeightedForename(double maleProbability) {
		if (maleProbability < 0 || maleProbability > 1) {
			// Probability is out of range - use default
			return getWeightedForename();
		} else if (maleProbability == 0) {
			return getWeightedFemaleForename();
		} else if (maleProbability == 1) {
			return getWeightedMaleForename();
		} else {
			if (random.nextDouble() <= maleProbability) {
				return getWeightedMaleForename();
			} else {
				return getWeightedFemaleForename();
			}
		}
	}
	
	/**
	 * Gets a random male forename from the {@code DataSet}, ignoring any weightings that may have
	 * been implemented.
	 * @return A forename.
	 */
	public String getRandomMaleForename() {
		if (maleForenames == null)
			readInto(DataFiles.MALE_FORENAMES.getFilepath(), DataType.MALE_FORENAMES);
		return (String) maleForenames.getRandomValue();
	}
	
	/**
	 * Gets a random male forename from the {@code DataSet} according to the weightings. If the
	 * {@code DataSet} does not contain weightings, this will return a random forename, where each
	 * forename will have an equal probability of being chosen.<br>
	 * <br>
	 * Note that the standard {@code DataSet} is weighted.
	 * @return A forename.
	 */
	public String getWeightedMaleForename() {
		if (maleForenames == null)
			readInto(DataFiles.MALE_FORENAMES.getFilepath(), DataType.MALE_FORENAMES);
		return (String) maleForenames.getWeightedValue();
	}
	
	/**
	 * Gets a random female forename from the {@code DataSet}, ignoring any weightings that may
	 * have been implemented.
	 * @return A forename.
	 */
	public String getRandomFemaleForename() {
		if (femaleForenames == null)
			readInto(DataFiles.FEMALE_FORENAMES.getFilepath(), DataType.FEMALE_FORENAMES);
		return (String) femaleForenames.getRandomValue();
	}
	
	/**
	 * Gets a random female forename from the {@code DataSet} according to the weightings. If the
	 * {@code DataSet} does not contain weightings, this will return a random forename, where each
	 * forename will have an equal probability of being chosen.<br>
	 * <br>
	 * Note that the standard {@code DataSet} is weighted.
	 * @return A forename.
	 */
	public String getWeightedFemaleForename() {
		if (femaleForenames == null)
			readInto(DataFiles.FEMALE_FORENAMES.getFilepath(), DataType.FEMALE_FORENAMES);
		return (String) femaleForenames.getWeightedValue();
	}
	
	/**
	 * Returns a random surname from the {@code DataSet}, ignoring any weightings that may have
	 * been implemented.
	 * @return A surname.
	 */
	public String getRandomSurname() {
		if (surnames == null)
			readInto(DataFiles.SURNAMES.getFilepath(), DataType.SURNAMES);
		return (String) surnames.getRandomValue();
	}
	
	/**
	 * Gets a random surname from the {@code DataSet} according to the weightings. If the {@code
	 * DataSet} does not contain weightings, this will return a random surname, where each surname
	 * will have an equal probability of being chosen.<br>
	 * <br>
	 * Note that the standard {@code DataSet} is weighted.
	 * @return A surname.
	 */
	public String getWeightedSurname() {
		if (surnames == null)
			readInto(DataFiles.SURNAMES.getFilepath(), DataType.SURNAMES);
		return (String) surnames.getWeightedValue();
	}
	
	/**
	 * Returns a random profession from the {@code DataSet}, ignoring any weightings that may have
	 * been implemented. There is a 50% chance that the profession will be in the male form.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return A profession.
	 */
	public String getRandomProfession() {
		return (random.nextBoolean()) ? getRandomMaleProfession() : getRandomFemaleProfession();
	}
	
	/**
	 * Gets a random profession from the {@code DataSet} according to the weightings. If the {@code
	 * DataSet} does not contain weightings, this will return a random surname, where each surname
	 * will have an equal probability of being chosen. There is a 50% chance that the profession
	 * will be in the male form.<br>
	 * <br>
	 * Note that the standard {@code DataSet} is weighted.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return A profession.
	 */
	public String getWeightedProfession() {
		return (random.nextBoolean()) ? getWeightedMaleProfession() :
			getWeightedFemaleProfession();
	}
	
	/**
	 * Returns a random male profession from the {@code DataSet}, ignoring any weightings that may
	 * have been implemented.
	 * @return A profession.
	 */
	public String getRandomMaleProfession() {
		if (maleProfessions == null)
			readInto(DataFiles.MALE_PROFESSIONS.getFilepath(), DataType.MALE_PROFESSIONS);
		return (String) maleProfessions.getRandomValue();
	}
	
	/**
	 * Returns a random male profession from the {@code DataSet} according to the weightings. If
	 * the {@code DataSet} does not contain weightings, this will return a random profession, where
	 * each profession will have an equal probability of being chosen.
	 * @return A profession.
	 */
	public String getWeightedMaleProfession() {
		if (maleProfessions == null)
			readInto(DataFiles.MALE_PROFESSIONS.getFilepath(), DataType.MALE_PROFESSIONS);
		return (String) maleProfessions.getWeightedValue();
	}
	
	/**
	 * Returns a random female profession from the {@code DataSet}, ignoring any weightings that
	 * may have been implemented.
	 * @return A profession.
	 */
	public String getRandomFemaleProfession() {
		if (femaleProfessions == null)
			readInto(DataFiles.FEMALE_PROFESSIONS.getFilepath(), DataType.FEMALE_PROFESSIONS);
		return (String) femaleProfessions.getRandomValue();
	}
	
	/**
	 * Returns a random female profession from the {@code DataSet} according to the weightings. If
	 * the {@code DataSet} does not contain weightings, this will return a random profession, where
	 * each profession will have an equal probability of being chosen.
	 * @return A profession.
	 */
	public String getWeightedFemaleProfession() {
		if (femaleProfessions == null)
			readInto(DataFiles.FEMALE_PROFESSIONS.getFilepath(), DataType.FEMALE_PROFESSIONS);
		return (String) femaleProfessions.getWeightedValue();
	}

	/**
	 * Returns a random full name, consisting of a random forename and a random surname, separated
	 * by a space. There is a 50% chance that the name will be male. This method ignores any
	 * weightings that may have been implemented.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getRandomFullName() {
		return getRandomForename() + " " + getRandomSurname();
	}
	
	/**
	 * Returns a random full name, consisting of a random forename, chosen according to the
	 * weightings, and a random surname, chosen according to the weightings, separated by a space.
	 * There is a 50% chance that the name will be male.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getWeightedFullName() {
		return getWeightedForename() + " " + getWeightedSurname();
	}
	
	/**
	 * Returns a random full name, consisting of a random forename and surname, ignoring any
	 * weightings that may have been implemented.<br>
	 * <br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @param maleProbability Allows the likelihood of the gender to be set. It is a value between
	 * 0 and 1. The closer to 1, the more likely that this will return a male name. If the value is
	 * out of range, the probability of each gender will be even.
	 * @return A full name. Format: "forename surname".
	 */
	public String getRandomFullName(double maleProbability) {
		return getRandomForename(maleProbability) + " " + getRandomSurname();
	}
	
	/**
	 * Returns a random full name, consisting of a random forename, chosen according to the
	 * weightings, and a random surname, chosen according to the weightings, separated by a space.
	 * <br><br>
	 * <i>This call may lead to inconsistent data if gender affects any results later, and is only
	 * recommended for quick use.</i>
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getWeightedFullName(double maleProbability) {
		return getWeightedForename(maleProbability) + " " + getWeightedSurname();
	}
	
	/**
	 * Returns a random male full name, consisting of a random forename and a random surname, both
	 * of which are selected ignoring any weightings that may have been implemented.
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getRandomMaleFullName() {
		return getRandomMaleForename() + " " + getRandomSurname();
	}
	
	/**
	 * Returns a random male full name, consisting of a weighted forename and a weighted surname,
	 * both of which are selected according to any weightings that have been set up. Note that if
	 * the weightings have not been set up, this will return a random male name, where each surname
	 * and male forename has an equal probability of being selected.
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getWeightedMaleFullName() {
		return getWeightedMaleForename() + " " + getWeightedSurname();
	}
	
	/**
	 * Returns a random female full name, consisting of a random forename and a random surname,
	 * both of which are selected ignoring any weightings that may have been implemented.
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getRandomFemaleFullName() {
		return getRandomFemaleForename() + " " + getRandomSurname();
	}
	
	/**
	 * Returns a random female full name, consisting of a weighted forename and a weighted surname,
	 * both of which are selected according to any weightings that have been set up. Note that if
	 * the weightings have not been set up, this will return a random female name, where each
	 * surname and female forename has an equal probability of being selected.
	 * @return
	 * A full name. Format: "forename surname".
	 */
	public String getWeightedFemaleFullName() {
		return getWeightedFemaleForename() + " " + getWeightedSurname();
	}
	
	/**
	 * Returns a random email domain from the {@code DataSet}, ignoring any weightings that may
	 * have been implemented. Note that the email domain will not include the {@code '@'} symbol.
	 * @return An email domain.
	 */
	public String getRandomEmailDomain() {
		if (emailDomains == null)
			readInto(DataFiles.EMAIL_DOMAINS.getFilepath(), DataType.EMAIL_DOMAINS);
		return emailDomains.getRandomValue();
	}
	
	/**
	 * Returns a random email domain from the {@code DataSet} according to the weightings. If the
	 * {@code DataSet} does not contain weightings, this will return a random domain, where each
	 * domain will have an equal probability of being chosen. Note that the email domain will not
	 * include the {@code '@'} symbol.<br>
	 * <br>
	 * Note that the standard {@code DataSet} is weighted.
	 * @return An email domain as a String.
	 */
	public String getWeightedEmailDomain() {
		if (emailDomains == null)
			readInto(DataFiles.EMAIL_DOMAINS.getFilepath(), DataType.EMAIL_DOMAINS);
		return emailDomains.getWeightedValue();
	}
	
	/**
	 * Gets the {@code DataSet} of male forenames.
	 * @return The male forenames {@code DataSet}.
	 */
	public DataSet<String> getMaleForenames() {
		if (maleForenames == null)
			readInto(DataFiles.MALE_FORENAMES.getFilepath(), DataType.MALE_FORENAMES);
		return maleForenames;
	}
	
	/**
	 * Gets the {@code DataSet} of female forenames.
	 * @return The female forenames {@code DataSet}.
	 */
	public DataSet<String> getFemaleForenames() {
		if (femaleForenames == null)
			readInto(DataFiles.FEMALE_FORENAMES.getFilepath(), DataType.FEMALE_FORENAMES);
		return femaleForenames;
	}
	
	/**
	 * Gets the {@code DataSet} of surnames.
	 * @return The surnames {@code DataSet}.
	 */
	public DataSet<String> getSurnames() {
		if (surnames == null)
			readInto(DataFiles.SURNAMES.getFilepath(), DataType.SURNAMES);
		return surnames;
	}
	
	/**
	 * Gets the {@code DataSet} of male professions.
	 * @return The male professions {@code DataSet}.
	 */
	public DataSet<String> getMaleProfessions() {
		if (maleProfessions == null)
			readInto(DataFiles.MALE_PROFESSIONS.getFilepath(), DataType.MALE_PROFESSIONS);
		return maleProfessions;
	}
	
	/**
	 * Gets the {@code DataSet} of female professions.
	 * @return The female professions {@code DataSet}.
	 */
	public DataSet<String> getFemaleProfessions() {
		if (femaleProfessions == null)
			readInto(DataFiles.FEMALE_PROFESSIONS.getFilepath(), DataType.FEMALE_PROFESSIONS);
		return femaleProfessions;
	}
	
	/**
	 * Gets the {@code DataSet} of email domains.
	 * @return The email domains {@code DataSet}.
	 */
	public DataSet<String> getEmailDomains() {
		if (emailDomains == null)
			readInto(DataFiles.EMAIL_DOMAINS.getFilepath(), DataType.EMAIL_DOMAINS);
		return emailDomains;
	}
	
	/**
	 * This method is intended to be used for reading in the standard {@code DataSet}.
	 * @param filepath The filepath of the data file.
	 * @param target The {@code DataType} for the data.
	 */
	private void readInto(String filepath, DataType target) {
		try {
			switch (target) {
			case MALE_FORENAMES:
				maleForenames = DataSetParser.parseData(filepath, target, "%");
				break;
			case FEMALE_FORENAMES:
				femaleForenames = DataSetParser.parseData(filepath, target, "%");
				break;
			case SURNAMES:
				surnames = DataSetParser.parseData(filepath, target, "%");
				break;
			case MALE_PROFESSIONS:
				maleProfessions = DataSetParser.parseData(filepath, target, "%");
				break;
			case FEMALE_PROFESSIONS:
				femaleProfessions = DataSetParser.parseData(filepath, target, "%");
				break;
			case EMAIL_DOMAINS:
				emailDomains = DataSetParser.parseData(filepath, target, "%");
				break;
			default:
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Contains the locations of the standard data files.
	 * @author Richard
	 *
	 */
	private enum DataFiles {
		
		MALE_FORENAMES {
			public String getFilepath() {
				return getDirectory() + "male_forenames.txt";
			}
		}, FEMALE_FORENAMES {
			public String getFilepath() {
				return getDirectory() + "female_forenames.txt";
			}
		}, SURNAMES {
			public String getFilepath() {
				return getDirectory() + "surnames.txt";
			}
		}, MALE_PROFESSIONS {
			public String getFilepath() {
				return getDirectory() + "male_professions.txt";
			}
		}, FEMALE_PROFESSIONS {
			public String getFilepath() {
				return getDirectory() + "female_professions.txt";
			}
		}, EMAIL_DOMAINS {
			public String getFilepath() {
				return getDirectory() + "email_domains.txt";
			}
		};
		
		/**
		 * Returns the relative filepath for the given file. The filepaths are relative to the
		 * project base (all start with "{@code src/}"...)
		 * @return The filepath of the given file.
		 */
		public abstract String getFilepath();
		
		/**
		 * Returns the absolute filepath of the directory that contains all of the data files.
		 * Note that there is a trailing "{@code /}" at the end of the directory filepath for
		 * easier concatenation.
		 * @return The base directory of the standard {@code DataSet} files.
		 */
		public String getDirectory() {
			String directory = PersonGenerator.class.getProtectionDomain().getCodeSource().
					getLocation().getPath();
			
			// Getting rid of /bin
			String [] directoryComponents = directory.split("/");
			directory = "";
			for (int i = 0; i < directoryComponents.length-1; i++) {
				directory += "/" + directoryComponents[i];
			}
			directory += "/src/populator/random/data/";
			return directory;
		}
		
	}

	
}