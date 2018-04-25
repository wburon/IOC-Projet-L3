package fr.uco.ima.tsp.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class <code>Time</code> is an abstract implementation useful for time
 * management.
 * 
 * @author froger
 * 
 */
public abstract class Time {

	/** The number of milliseconds in a day. */
	public static final int NB_MS_IN_DAY = 86400000;
	/** The number of nanoseconds in a day. */
	public static final long NB_NS_IN_DAY = 86400000000000l;
	/** The number of milliseconds in an hour. */
	public static final int NB_MS_IN_HOUR = 3600000;
	/** The number of nanoseconds in an hour. */
	public static final long NB_NS_IN_HOUR = 3600000000000l;
	/** The number of milliseconds in a minute. */
	public static final int NB_MS_IN_MIN = 60000;
	/** The number of nanoseconds in a minute. */
	public static final long NB_NS_IN_MIN = 60000000000l;
	/** The number of milliseconds in a second. */
	public static final int NB_MS_IN_S = 1000;
	/** The number of nanoseconds in a second (10e9). */
	public static final long NB_NS_IN_S = 1000000000l;
	/** The number of nanoseconds in a millisecond (10e6). */
	public static final long NB_NS_IN_MS = 1000000l;

	/** The Constant TIME_FORMAT. */
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	/** The Constant JVM_START_DATE. */
	private static final String JVM_START_DATE = getDateString();

	/**
	 * Returns the date time at the start of the jVM in form of a string
	 * <code>yymmdd_hh-mm</code>.
	 * 
	 * @return the date time at the start of the jVM in form of a string
	 *         <code>yymmdd_hh-mm</code>
	 */
	public static String getVMStartDateString() {
		return JVM_START_DATE;
	}

	/**
	 * Returns the current date time in form of a string
	 * <code>yymmdd_hh-mm</code>.
	 * 
	 * @return the current date time in form of a string
	 *         <code>yymmdd_hh-mm</code>
	 */
	public static String getDateString() {
		return String.format("%1$ty%1$tm%1$td_%1$tH-%1$tM", new Date(System.currentTimeMillis()));
	}

	/**
	 * Converts a duration in s in a human friendly string.
	 * 
	 * @param time
	 *            the duration to be converted
	 * @param maxDigits
	 *            the number of digits to show
	 * @param showMS
	 *            <code>true</code> if ms should be displayed,
	 *            <code>false</code> otherwise
	 * @param showAll
	 *            <code>false</code> to show only non-null values
	 * @return a string of format <code>"d h m s ms"</code>, or
	 *         <code>"na"</code> if <code>time</code> is negative
	 */
	public static String secondsToString(double time, int maxDigits, boolean showMS, boolean showAll) {
		return millisecondsToString((long) (time * 1000), maxDigits, showMS, showAll);
	}

	/**
	 * Converts a duration in ms in a human friendly string.
	 * 
	 * @param time
	 *            the duration to be converted
	 * @param maxDigits
	 *            the number of digits to show
	 * @param showMS
	 *            <code>true</code> if ms should be displayed,
	 *            <code>false</code> otherwise
	 * @param showAll
	 *            <code>false</code> to show only non-null values
	 * @return a string of format <code>"d h m s ms"</code>, or
	 *         <code>"na"</code> if <code>time</code> is negative
	 */
	public static String millisecondsToString(long time, int maxDigits, boolean showMS, boolean showAll) {
		if (time < 0) {
			return "na";
		}
		int[] duration = Time.decomposeMillis(time);
		String[] labels = { "d", "h", "m", "s", "ms" };
		boolean force = showAll;
		StringBuilder sb = new StringBuilder(20);
		int digits = 0;
		for (int i = 0; i < duration.length; i++) {
			if (force || (i < duration.length - 1 || showMS || digits == 0) && digits < maxDigits && duration[i] > 0) {
				if (i != 0) {
					if (i == 4 && duration[i] < 10)
						sb.append("  ");
					else if (duration[i] < 10 || (i == 4 && duration[i] < 100 && duration[i] >= 10))
						sb.append(" ");
				}
				sb.append(duration[i]);
				sb.append(labels[i]);
				digits++;
			}
		}
		if (sb.length() == 0)
			return "0ms";
		else
			return sb.toString();
	}

	/**
	 * Convert a duration in ns in an array containing the corresponding number
	 * of days, hours, minutes, seconds, milliseconds, and nanoseconds.
	 * 
	 * @param time
	 *            the duration to be converted
	 * @return <code>[days,hours,minutes,seconds,milliseconds]</code>
	 */
	public static int[] decomposeNanos(long time) {
		int[] duration = new int[6];
		if (time < 0) {
			return duration;
		}
		// Days
		duration[0] = (int) (time / NB_NS_IN_DAY);
		time -= duration[0] * NB_NS_IN_DAY;
		// Hours
		duration[1] = (int) (time / NB_NS_IN_HOUR);
		time -= duration[1] * NB_NS_IN_HOUR;
		// Minutes
		duration[2] = (int) (time / NB_NS_IN_MIN);
		time -= duration[2] * NB_NS_IN_MIN;
		// Seconds
		duration[3] = (int) (time / NB_NS_IN_S);
		time -= duration[3] * NB_NS_IN_S;
		// Milliseconds
		duration[4] = (int) (time / NB_NS_IN_MS);
		time -= duration[4] * NB_NS_IN_S;
		// Nanoseconds
		duration[5] = (int) time;
		return duration;
	}

	/**
	 * Convert a duration in milliseconds in an array containing the
	 * corresponding number of days, hours, minutes, seconds and milliseconds.
	 * 
	 * @param time
	 *            the duration to be converted
	 * @return <code>[days,hours,minutes,seconds,milliseconds]</code>
	 */
	public static int[] decomposeMillis(long time) {
		int[] duration = new int[5];
		if (time < 0) {
			return duration;
		}
		// Days
		duration[0] = (int) time / NB_MS_IN_DAY;
		time -= duration[0] * NB_MS_IN_DAY;
		// Hours
		duration[1] = (int) time / NB_MS_IN_HOUR;
		time -= duration[1] * NB_MS_IN_HOUR;
		// Minutes
		duration[2] = (int) time / NB_MS_IN_MIN;
		time -= duration[2] * NB_MS_IN_MIN;
		// Seconds
		duration[3] = (int) time / 1000;
		time -= duration[3] * 1000;
		// Milliseconds
		duration[4] = (int) time;
		return duration;
	}

}
