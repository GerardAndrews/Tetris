package sf.test;

import sf.SoundFactory;

/**
 * Plays a test sound.
 * @author Ron.Coleman
 *
 */
public class Test04 {
	public final static String DIR = "src/res/";
	
	public final static String FILE = "invaderkilled.wav";
	
	public static void main(String[] args) {	
		SoundFactory.play(DIR+FILE);
		
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SoundFactory.play(DIR+FILE);
	}
}
