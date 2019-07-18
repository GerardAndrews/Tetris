/*
 Copyright (c) Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package sf;

import java.util.HashMap;

/**
 * This is a helper class for playing sounds.<br>
 * Among its features are it primes the Java sound API, caches sounds, and
 * can play sounds in the background with or without a loop.
 * @author Ron.Coleman
 *
 */
public class SoundFactory {
	protected static HashMap<String, Sound> sounds = new HashMap<>();
	protected static Boolean primed = false;
	
	/**
	 * Constructor
	 */
	private SoundFactory() {
		
	}
	
	/**
	 * Makes a sound and caches it by the path.
	 * @param path File path
	 * @return Sound
	 */
	public static Sound getInstance(String path) {
		return new Sound(path);
	}
	
	/**
	 * Plays a sound n times in a loop in the background.
	 * @param sound Sound to play
	 * @param n Loop times
	 */
	public static void play(Sound sound, int n) {
		new Thread(new Runnable() {
			public void run() {
				for(int loop=0; loop < n; loop++) {
					sound.play();
				}
			}
		}).start();
	}
	
	/**
	 * Plays a sound one time in the background.
	 * @param sound Sound
	 */
	public static void play(Sound sound) {
		play(sound, 1);
	}
	
	/**
	 * Plays a sound n times in a loop in the background. 
	 * @param path Sound file path
	 * @param n Loop times
	 */
	public static void play(String path, int n) {
		Sound sound = getInstance(path);
		
		play(sound, n);
	}
	
	/**
	 * Plays a sound one time in the background.
	 * @param path Sound file path
	 */
	public static void play(String path) {
		play(path, 1);
	}
}
