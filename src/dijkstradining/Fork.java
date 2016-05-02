package dijkstradining;

/**
 * Fork - This class represents a fork used in the "Dining Philosophers" problem.
 * Its #use method is Thread Safe - that is, check and set is treated as a mutually exclusive
 * operation
 * @author Dr. David Lamb, based on Dijkstra's "Dining Philosophers" problem
 */
public class Fork {
	static int globalForkID = 0;
	
	/**
	 * Constructs a new fork, with a unique ID.
	 * This constructor is NOT thread safe
	 */
	public Fork() {
		forkID = globalForkID;
		globalForkID++;
	}
	
	private int forkID = 0;
	
	/**
	 * The philosopher presently holding the fork
	 */
	private Philosopher beingUsedBy;
	
	/**
	 * Returns a value to indicate if the fork is free
	 * @return true if free, false if not
	 */
	public boolean isFree()
	{
		return beingUsedBy == null;
	}
	
	/**
	 * Marks the fork as "in use" by the specified Philosopher
	 * @param philosopher the specified philosopher
	 * @throws ForkInUseException if the Fork was already being used
	 */
	public void use(Philosopher philosopher) throws ForkInUseException
	{
		synchronized (this) {
			if (isFree())
				beingUsedBy = philosopher;
			else 
				throw new ForkInUseException(this, beingUsedBy, philosopher);
		}
	}
	
	/**
	 * Gets the numeric ID of the current fork
	 * @return the ID
	 */
	public int getForkID() {
		return forkID;
	}

	/**
	 * Gets the philosopher presently using the current fork
	 * @return the philosopher using the fork, or null if no-one is using it
	 */
	public Philosopher getBeingUsedBy() {
		return beingUsedBy;
	}

	/**
	 * Marks the operation of "putting the fork down".
	 * This method must be called with a parameter specifying the current philosopher
	 * using the fork
	 * @param philosopher the philosopher who was using the fork
	 */
	public void finish(Philosopher philosopher)
	{
		if (beingUsedBy == philosopher)
			beingUsedBy = null;
	}
	
	/**
	 * Thrown when a Fork is already in use
	 * 
	 */
	public class ForkInUseException extends Exception
	{
		public Philosopher user, stealer;
		public Fork fork;
		
		public ForkInUseException(Fork fork, Philosopher user, Philosopher stealer)
		{
			this.fork = fork;
			this.user = user;
			this.stealer = stealer;
		}
	}
}

