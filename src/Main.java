import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Main {

	public static Vector<MessageDisplay> messageDisplays;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner reader = new Scanner(System.in);
		boolean finished = false;
		messageDisplays = new Vector<MessageDisplay>();
		
		while(!finished) {
			System.out.println("	1. Create new message, 2. Pause thread, 3. Restart thread, 4. Exit");
			int input = reader.nextInt();
			// Clean input from int
			if (reader.hasNextLine()) {
				reader.nextLine();
			}
			switch(input) {
			case 1:
				System.out.println("	Enter message:");
				String text = reader.nextLine();
				System.out.println("	Enter delay");
				int delay = reader.nextInt() * 1000;
				addMessageDisplay(delay, text);
				break;
			case 2:
				System.out.println("	Enter message number to pause:");
				int pauseIdx = reader.nextInt() - 1;
				messageDisplays.get(pauseIdx).pause();
				break;
			case 3:
				System.out.println("	Enter message number to restart:");
				int restartIdx = reader.nextInt() - 1;
				messageDisplays.get(restartIdx).restart();
				break;
			case 4:
				System.out.println("Exiting");
				for(MessageDisplay display : messageDisplays) {
					display.kill();
				}
				finished = true;
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
		}
		
		for(MessageDisplay display : messageDisplays) {
			display.join();
		}
	}
	
	private static void addMessageDisplay(int seconds, String text) {
		Message message = new Message(seconds, text);
		MessageDisplay messageDisplay = new MessageDisplay(message);
		messageDisplays.add(messageDisplay);
		messageDisplay.start();
	}
}
