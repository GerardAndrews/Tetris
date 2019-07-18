package sf.test;

import sf.Sound;
import sf.SoundFactory;

/**
 * Plays a test sound.
 * @author Ron.Coleman
 *
 */
public class Test02 {
	public final static String DIR = "src/res/";
	
	public final static String FILE = "goodshot-2.wav";
	
	public static void main(String[] args) {	
    	Sound sound = SoundFactory.getInstance(DIR+FILE);
    	
		SoundFactory.play(sound);
	}
}
