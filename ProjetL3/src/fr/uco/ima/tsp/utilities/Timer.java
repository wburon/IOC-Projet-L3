package fr.uco.ima.tsp.utilities;

/**
 * The class <code>Timer</code> provides a timer with nanosecond accuracy, pause
 * and resume functionalities, and various methods to convert a time to a
 * string.
 * 
 * @author froger
 * 
 */
public class Timer {

	/** Time when the timer has been started. */
	private long mStartTime;
	/** Time when the timer has been stopped. */
	private long mEndTime;
	/** A timout associated to the timer. */
	private long mTimeOut;
	/** Time when the timer has been paused. */
	private long mPauseTime;
	/** An accumulated time used when pausing/resuming (in nanoseconds). */
	private long mAccumulated;

	/**
	 * Creates a new <code>Timer</code> with the given timeout value.
	 * 
	 * @param timeout
	 *            the timeout for this timer (in ms).
	 * @see Timer#hasTimedOut().
	 */
	public Timer(long timeOut) {
		this.mTimeOut = timeOut;
	}

	public Timer() {
		mStartTime = -1;
		mEndTime = -1;
		mPauseTime = -1;
		mAccumulated = 0;
		mTimeOut = Long.MAX_VALUE;
	}

	/**
	 * Starts the timer.
	 */
	public void start() {
		if (mEndTime > 0)
			throw new IllegalStateException("Timer is stopped - restart first");
		if (mStartTime > 0)
			if (mPauseTime > 0)
				throw new IllegalStateException("Timer is already started and paused, use resume instead");
			else
				throw new IllegalStateException("Timer is already started");

		if (mPauseTime >= 0) {
			mAccumulated += mPauseTime - mStartTime;
		}
		mStartTime = System.nanoTime();
		mEndTime = -1;
		mPauseTime = -1;
	}

	/**
	 * Pauses the timer.
	 */
	public void pause() {
		if (mEndTime > 0)
			throw new IllegalStateException("Timer is stopped");
		else if (mStartTime < 0)
			throw new IllegalStateException("Timer is not started");
		else if (mPauseTime > 0)
			throw new IllegalStateException("Stopwatch is already paused");

		mPauseTime = System.nanoTime();
		mAccumulated += mPauseTime - mStartTime;
	}

	/**
	 * Resume a paused timer.
	 */
	public void resume() {
		if (mStartTime < 0)
			throw new IllegalStateException("Timer is not started");
		else if (mPauseTime < 0)
			throw new IllegalStateException("Timer is not paused");
		else if (mEndTime > 0)
			throw new IllegalStateException("Timer is stopped");
		else {
			mStartTime = System.nanoTime();
			mEndTime = -1;
			mPauseTime = -1;
		}
	}

	/**
	 * Stops the timer.
	 */
	public void stop() {
		if (mEndTime > 0)
			throw new IllegalStateException("Timer is already stopped");
		else if (mStartTime < 0)
			throw new IllegalStateException("Timer is not started");
		if (mPauseTime > 0) {
			// Virtually resume and stop the time at the last pause time
			mEndTime = mPauseTime;
			mStartTime = mPauseTime;
			mPauseTime = -1;
		} else {
			mEndTime = System.nanoTime();
		}
	}

	/**
	 * Restarts the timer, erasing possible accumulated time.
	 */
	public void restart() {
		reset();
		start();
	}

	/**
	 * Started state.
	 * 
	 * @return {@code true} if the timer is started, {@code false} otherwise.
	 */
	public boolean isStarted() {
		return mStartTime >= 0;
	}

	/**
	 * Stopped state.
	 * 
	 * @return {@code true} if the timer is stopped, {@code false} otherwise.
	 */
	public boolean isStopped() {
		return mEndTime >= 0;
	}

	/**
	 * Reset this timer.
	 */
	public void reset() {
		mStartTime = -1;
		mEndTime = -1;
		mPauseTime = -1;
		mAccumulated = 0;
		mTimeOut = Long.MAX_VALUE;
	}

	/**
	 * Set the timeout for this timer.
	 * 
	 * @param timeout
	 *            the new timout (in s).
	 * @see #isTimedout().
	 */
	public void setTimeOut(long timeOut) {
		this.mTimeOut = timeOut * Time.NB_MS_IN_S;
	}

	/**
	 * Timeout of this timer. Compare the current running time to the timeout
	 * value.
	 * 
	 * @return <code>true</code> if the timer has timed out, <code>false</code>
	 *         otherwise.
	 * @see #setTimout(long).
	 */
	public boolean hasTimedOut() {
		if (mStartTime < 0) {
			return false;
		}
		return readTimeMS() > mTimeOut;
	}

	/**
	 * Read the elapsed time on this timer (in nanoseconds):
	 * <ul>
	 * <li>If the timer is not started: returns {@code  0}</li>
	 * <li>If the timer is stopped: returns the total final elapsed time</li>
	 * <li>If the timer is paused: returns the total elapsed time at the time it
	 * was paused</li>
	 * <li>If the timer is running: returns the current elapsed time</li>.
	 * </ul>
	 * 
	 * @return the elapsed time on this timer (in nanoseconds)
	 */
	public long readTime() {
		if (mStartTime < 0)
			return 0;
		else if (mEndTime > 0)
			return mEndTime - mStartTime + mAccumulated;
		else if (mPauseTime > 0)
			return mAccumulated;
		else
			return System.nanoTime() - mStartTime + mAccumulated;
	}

	/**
	 * Read the elapsed time on this timer (in milliseconds).
	 * 
	 * @return the elapsed time on this timer (in milliseconds)
	 * @see #readTime()
	 */
	public double readTimeMS() {
		return readTime() / Time.NB_NS_IN_MS;
	}

	/**
	 * Read the elapsed time on this timer (in seconds).
	 * 
	 * @return the elapsed time on this timer (in seconds)
	 * @see #readTime()
	 */
	public double readTimeS() {
		return readTime() / Time.NB_NS_IN_S;
	}

	/**
	 * Get the timeout value for this timer.
	 * 
	 * @return the number of ms after which the {@link #hasTimedOut()} method
	 *         will return <code>true</code>.
	 */
	private long getTimeOut() {
		return mTimeOut;
	}

	/**
	 * Remaining time before the timeout, in seconds.
	 * 
	 * @return the time remaining before timeout, in seconds
	 */
	public long getRemainingTimeS() {
		if (mTimeOut == Long.MAX_VALUE) {
			return Long.MAX_VALUE;
		} else {
			return (long) (getTimeOut() / 1000 - readTimeS());
		}
	}

	@Override
	public String toString() {
		if (!isStarted()) {
			if (mTimeOut != Long.MAX_VALUE && mTimeOut != Integer.MAX_VALUE) {
				return String.format("not started, timeout:%sms", getTimeOut());
			} else {
				return "not started";
			}
		} else {
			if (mTimeOut != Long.MAX_VALUE && mTimeOut != Integer.MAX_VALUE) {
				return String.format("%.0f/%sms", readTimeMS(), getTimeOut());
			} else {
				return String.format("%.0fms", readTimeMS());
			}
		}
	}

	/**
	 * Gets the elapsed time and return an human friendly string.
	 * 
	 * @return the elapsed time in an human friendly string
	 */
	public String getTimeString() {
		return Time.millisecondsToString((long) readTimeMS(), 3, false, false);
	}

}
