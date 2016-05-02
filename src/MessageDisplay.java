import java.util.concurrent.atomic.AtomicInteger;

public class MessageDisplay extends Thread {
	
	private static AtomicInteger count = new AtomicInteger();
	
	private int id;
	
	private boolean isStopped;
	
	private boolean isPaused;
	
	private Message message;
	
	public MessageDisplay(Message message) {
		this.id = count.incrementAndGet();
		this.message = message;
	}
	
	@Override
	public void run() {
		// Cache these values as they are immutable
		String text = String.format("[%d] %s", this.id, message.getText());
		int delay = message.getDelay();
		// Whilst uninterrupted
		while (!this.isStopped) {
			try {
				// Sleep for the delay
				Thread.sleep(delay);
				
				synchronized(this) {
					// If it is paused, wait
					if (this.isPaused && !this.isStopped) {
						this.wait();
					}
				}
				System.out.println(text);
			} catch (InterruptedException ex) {
				// This will be thrown if the thread is paused or killed
			}
		}
	}
	
	public void pause() {
		this.isPaused = true;
		this.interrupt();
	}
	
	public synchronized void restart() {
		this.isPaused = false;
		this.notifyAll();
	}
	
	public void kill() {
		this.isStopped = true;
		this.interrupt();
	}
}
