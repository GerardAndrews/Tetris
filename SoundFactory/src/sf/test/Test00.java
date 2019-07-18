package sf.test;

import sf.Sound;
import sf.SoundFactory;

/**
 * Plays a sound in rapid-fire succession.
 * @author Ron.Coleman
 *
 */
public class Test00 {
	public final static String DIR = "src/res/";
	
	public final static String FILE = "shoot.wav";
	
	public static void main(String[] args) {	
    	Sound sound = SoundFactory.getInstance(DIR+FILE);
    	
		SoundFactory.play(sound);
//		SoundFactory.play(DIR+FILE);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SoundFactory.play(sound);
//		SoundFactory.play(DIR+FILE);
	}
}
