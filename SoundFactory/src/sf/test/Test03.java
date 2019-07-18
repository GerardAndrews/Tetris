package sf.test;

import sf.SoundFactory;

public class Test03 {
	public final static String DIR = "src/res/";
	
	public final static String FILE = "shoot.wav";
	
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
