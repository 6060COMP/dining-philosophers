package dijkstradining;

import java.util.Vector;

/**
 * The dining table in the (broken) Dining Philosopher's problem.
 * 
 * @author Dr David Lamb, based on a problem by Dijkstra
 */
public class Table {

	// Set of forks
	Vector<Fork> forks = new Vector<Fork>();
	// Set of Philosophers
	Vector<Philosopher> philosophers = new Vector<Philosopher>();

	// Set of Threads used to background run this problem
	Vector<Thread> threads = new Vector<>();

	// Names used to create an (anachronistic!) dining table of philosophers.
	String[] names = { "Descartes", "Plato", "Kant", "Hegel", "Socrates",
			"Nietzsche", "Russell", "Aristotle" };

	/**
	 * Creates a new dining table. 
	 */
	public Table() {

		// set up as many forks as we'll need to put one in between each
		// philosopher
		for (int i = 0; i < names.length; i++)
			forks.add(new Fork());

		// now create all the philosophers, based on the names in the "names"
		// array
		int forkCount = 0;
		for (String name : names) {
			// This iteration's rightFork becomes the next iterations leftFork,
			// and so on
			Fork leftFork = forks.get(forkCount);
			Fork rightFork = forks.get((forkCount + 1) % names.length);
			//
			philosophers.add(new Philosopher(name, leftFork, rightFork));
			System.out.println(name + ", left fork:" + leftFork.getForkPriority()
					+ ", right fork:" + rightFork.getForkPriority());

			forkCount++;
		}
	}

	/**
	 * Starts the dining process This method creates a new thread for each
	 * philosopher, then starts each thread.
	 */
	public void startDining() {

		// Create a new thread for each philosopher
		for (Philosopher philosopher : philosophers) {
			Thread diningThread = new Thread(philosopher);
			threads.add(diningThread);
		}

		// begin the threads
		for (Thread thread : threads)
			thread.start();
	}

	/**
	 * This method stops the dining process by setting each philosopher's
	 * "atTable" flag to false. This will cause any un-deadlocked threads to
	 * finish. However, any deadlocked threads will still be hung, and will need
	 * forcibly stopping.
	 */
	public void stopDining() {
		//
		for (Philosopher philosopher : philosophers) {
			philosopher.atTable = false;
		}
	}

	/**
	 * Prints out the current state of the software; enumerating: -Each
	 * Philosopher's name -How many meals they've eaten
	 */
	public void printStats() {
		System.out.println("The Dining Philosophers:-");
		for (Philosopher philosopher : philosophers) {
			System.out.println(philosopher.name + " has eaten "
					+ philosopher.timesEaten + " times");
		}
	}

	/**
	 * This method checks to see how many threads are still alive, and returns
	 * the associated Philosophers. This method, when called after
	 * {@link #stopDining()}, will indicate which threads are stuck waiting for
	 * a lock.
	 * 
	 * @return a list of Philosophers
	 */
	public Vector<Philosopher> checkDeadlock() {
		int i = 0;
		Vector<Philosopher> stuck = new Vector<Philosopher>();
		for (Thread thread : threads) {
			if (thread.isAlive())
				stuck.add(philosophers.get(i));
			i++;
		}
		return stuck;
	}

	/**
	 * This method is run when the program first starts. It creates a new Table
	 * object, and then: -calls the #startDining method -calls the #printStats
	 * method every 2 seconds, a total of 10 times. * -calls the #stopDining
	 * method -waits for 2 seconds, and then checks to see if any Philosophers
	 * are still deadlocked
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Table table = new Table();
		table.startDining();
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// ignore
			}
			table.printStats();
		}
		table.stopDining();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// ignore
		}

		System.out.println("Which Philosophers are Deadlocked?");
		Vector<Philosopher> stuckPhil = table.checkDeadlock();
		for (Philosopher stuck : stuckPhil)
			System.out.print(stuck.name + ", ");

		System.out.println("Finished!");
		if (!stuckPhil.isEmpty())
			System.exit(1);
	}

}
