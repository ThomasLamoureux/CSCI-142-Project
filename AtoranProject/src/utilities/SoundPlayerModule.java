package utilities;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class SoundPlayerModule {
	
	public static class GameSound {
		private Clip clip;
		
		
		public GameSound(String path) {
			createSound(path);
		}
		
		
		public void playLooped() {
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		
		
		public void playLooped(int loopCount) {
			this.clip.loop(loopCount);
		}
		
		
		public void stop() {
			if (this.clip.isOpen() == true) {
				this.clip.close();
			}
		}
		
		
		public void pause() {
			
		}
		
		
		public void endLoop() {
			if (this.clip.isRunning() == true) {
				clip.stop();
			}
		}
		
		
		public void play() {
			playSound();
		}
		
		public void setVolume() {
			FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN); 
			gainControl.setValue(1.0f);
		}
		
		
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
		
		
		private synchronized void playSound() {
			//Clip sound = this.clip;
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