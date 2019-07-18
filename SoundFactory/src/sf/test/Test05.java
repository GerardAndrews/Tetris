package sf.test;

import sf.SoundFactory;

/**
 * Tests an MP3 format which fails.
 * @author Ron.Coleman
 *
 */
public class Test05 {
	public final static String DIR = "src/res/";
	
	public final static String FILE = "gun-trigger-click-01.mp3";
	
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
