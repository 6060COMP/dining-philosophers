package dijkstradining;

import java.util.Random;

import dijkstradining.Fork.ForkInUseException;

/**
 * Philosopher - This class represents a philosopher used in the
 * "Dining Philosophers" problem. Its #run method is never invoked directly, but
 * via Thread.start(), which executes #run in a background Thread.
 * 
 * @author Dr. David Lamb, based on Dijkstra's "Dining Philosophers" problem
 */
public class Philosopher extends Thread { // implements Runnable {
	public Fork leftFork, rightFork;
	public String name;
	public boolean atTable = true;
	public int timesEaten = 0;
	public int timesThinking = 0;
	public static Random rand = new Random();

	/**
	 * Constructs a new philosopher, with the following parameters
	 * 
	 * @param name
	 *            the name of the philosopher
	 * @param left
	 *            the fork to their left
	 * @param right
	 *            the fork to their right
	 */
	public Philosopher(String name, Fork left, Fork right) {
		this.name = name;
		this.leftFork = left;
		this.rightFork = right;
		setName(name);
	}
	
	/**
	 * Encapsulates the main behaviour of the philosopher. Note that this method
	 * is never called directly; calling #start() instead runs this code in a
	 * background thread. The behaviour is as follows:
	 * <ol>
	 * <li>Loop until the #atTable flag becomes false
	 * <li>call the #think method
	 * <li>Gain exclusive access to the left fork
	 * <li>Gain exclusive access to the right fork
	 * <li>call the #eat method
	 * <li>Release exclusive access on the right fork, then the left fork.
	 * </ol>
	 */
	public void run() {

		while (atTable) {
			think(); // dum de dum de dum

			// now ensure I've got my left fork
			synchronized (leftFork) {
				// then my right fork
				synchronized (rightFork) {
					// yum yum yum!
					eat();
				}
			}
		}
	}

	/**
	 * Thinks (i.e. sleeps) for a random amount of time, as governed by
	 * #getRandomThinkTime()
	 */
	public void think() {
		timesThinking++;
		try {
			Thread.sleep(getRandomThinkTime());
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * <ol>
	 * <li>Picks up the left and right fork, using {@link Fork#use(Philosopher)}
	 * - this act will throw an exception if the philosopher is not the
	 * exclusive owner of the fork!
	 * <li>Eats (i.e. sleeps) for a random amount of time, as governed by
	 * {@link #getRandomEatTime()}.
	 * <li>Puts down the right and left fork, using
	 * {@link Fork#finish(Philosopher)}
	 * </ol>
	 */
	public void eat() {
		try {
			leftFork.use(this);
			rightFork.use(this);

			timesEaten++;

			try {
				Thread.sleep(getRandomEatTime());
			} catch (InterruptedException ie) {
				System.err.println(ie.getMessage()); // ignore
			}

			rightFork.finish(this);
			leftFork.finish(this);

		} catch (ForkInUseException e) {
			// this is to ensure you don't try to cheat
			// you must have the fork taken (with a lock)
			// before you try to eat
			Fork fork = e.fork;
			System.err.println("Fork " + fork.getForkID()
					+ " already in use by " + fork.getBeingUsedBy().name);
		}
	}

	/**
	 * Generates a random duration value to think for
	 * 
	 * @return a time value (in ms)
	 */
	public int getRandomThinkTime() {
		return rand.nextInt(25);
	}

	/**
	 * Generates a random duration value to eat for
	 * 
	 * @return a time value (in ms)
	 */
	public int getRandomEatTime() {
		return rand.nextInt(200);
	}

}
