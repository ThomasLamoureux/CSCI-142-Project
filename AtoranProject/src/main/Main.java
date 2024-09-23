package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import levels.GameMap;

public class Main {
	public static void main(String[] args) {
		playSound("yrdy");
		
		GameMap yes = new GameMap();
	}
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        File file = new File("Resources/Sounds/testingsound.wav");
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		    	System.out.println(e);
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}
}
