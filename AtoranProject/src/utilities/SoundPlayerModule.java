package utilities;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class SoundPlayerModule {
	
	public static class GameSound {
		private Clip clip;
		
		// Sound
		public GameSound(String path) {
			createSound(path);
		}
		
		// Plays the sound looped
		public void playLooped() {
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		
		// Plays the sound looped for a set amount of time
		public void playLooped(int loopCount) {
			this.clip.loop(loopCount);
		}
		
		// Stops the sound from playing
		public void stop() {
			if (this.clip.isOpen() == true) {
				this.clip.close();
			}
		}
		
		// Stops the loop without stopping the sound
		public void endLoop() {
			if (this.clip.isRunning() == true) {
				clip.stop();
			}
		}
		
		// Plays the sound
		public void play() {
			playSound();
		}
		
		// Creates the sound clip
		private void createSound(final String url) {
			try {
				Clip clip  = AudioSystem.getClip();
				File file = new File(url);
				
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
				
				clip.open(inputStream);
				
				this.clip = clip;
			} catch (Exception err) {
				
			}
		}
		
		// Plays the sound
		private synchronized void playSound() {
			GameSound sound = this;
			new Thread(new Runnable() {
				public void run() {
					try {
						sound.clip.start();
					} catch (Exception err) {
						System.out.println(err);
					}
				}
			}).start();
		}
	}
}