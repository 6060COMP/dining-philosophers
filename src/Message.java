
public class Message {

	private int delay;
	private String text;
	
	public Message(int delay, String text) {
		this.delay = delay;
		this.text = text;
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public String getText() {
		return this.text;
	}
}
