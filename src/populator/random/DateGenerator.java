package populator.random;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;

/**
 * 
 * @author Richard
 *
 */
public final class DateGenerator {
	
	private final LocalDate startDate, endDate;
	private final int daysBetween;
	private final static Random random = new Random();
	
	/**
	 * Creates a new DateGenerator instance, that will provide random dates between the specified
	 * start date and end date. This class will not work with dates over 2,147,483,647 days apart
	 * (approximately 5,883,516 years).
	 * @param startDate
	 * Randomly generated dates will never be produced before this date.
	 * @param endDate
	 * Randomly generated dates will never be produced after this after.
	 * @throws IllegalArgumentException
	 * This exception will be thrown if the start date specified is on or before the end date.
	 */
	public DateGenerator(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
		// Do not allow the start date to be later than the end date
		if (startDate.isAfter(endDate) || startDate.isEqual(endDate))
			throw new IllegalArgumentException("Start date is on or after the end date.");
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.daysBetween = getDaysBetweenDates();
	}
	
	/**
	 * Gets the specified start date.
	 * @return Specified start date.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}
	
	/**
	 * Gets the specified end date.
	 * @return Specified end date.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Gets a random number of days between the start date and end date.
	 * @return Integer of days between the start date and the end date.
	 */
	public int getRandomNumberOfDays() {
		// Addition of 1 allows the full range to be covered as the nextInt method is does not
		// include the last value
		return random.nextInt(daysBetween + 1);
	}
	
	/**
	 * Gets a random date between the start date and end date.
	 * @return A random date between the start date and end date. 
	 */
	public LocalDate getRandomDate() {
		return startDate.plus(Period.ofDays(getRandomNumberOfDays()));
	}
	
	/**
	 * Calculates the length of time (in days) between the specified start date and end date.
	 * @return The number of days between the start date and end date.
	 */
	public int getDaysBetweenDates() {
		return (int) ChronoUnit.DAYS.between(startDate, endDate);
	}

}
