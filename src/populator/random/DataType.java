package populator.random;

/**
 * The {@code DataSet} specifies what type of data is contained within a {@code DataSet}. This is
 * used within the {@code PersonGenerator}. If the {@code DataSet} is intended for separate use and
 * will not be used directly within the {@code PersonGenerator}, please use {@code {@link
 * DataType#OTHER}}.
 * 
 * @see DataSet
 * 
 * @author Richard Innocent
 *
 */
public enum DataType {
	/**
	 * Used to replace the male forenames in the {@code PersonGenerator}.
	 */
	MALE_FORENAMES,
	/**
	 * Used to replace the female forenames in the {@code PersonGenerator}.
	 */
	FEMALE_FORENAMES,
	/**
	 * Used to replace the surnames in the {@code PersonGenerator}.
	 */
	SURNAMES,
	/**
	 * Used to replace the male professions in the {@code PersonGenerator}.
	 */
	MALE_PROFESSIONS,
	/**
	 * Used to replace the female professions in the {@code PersonGenerator}.
	 */
	FEMALE_PROFESSIONS,
	/**
	 * Used to replace the email domains in the {@code PersonGenerator}.
	 */
	EMAIL_DOMAINS,
	/**
	 * Used any data that is not intended for use within the {@code PersonGenerator}.
	 */
	OTHER;
}
