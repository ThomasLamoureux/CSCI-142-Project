package engine;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import animations.Animation;
import combat.CombatEntity;
import main.Window;
import utilities.AnimationPlayerModule;

public class Engine {
	private static boolean running = false; 
	private static int frameMilliseconds = 10; // Framerate
	
	// Turns the FPS system on or off
	public static void toggleFps(boolean toggle) {
		if (toggle == true) { // Enables FPS system
			if (running == true) {
				return;
			}
			running = true;
			
			// Starts the FPS thread
			Runnable fpsMethod = () -> {
				runFps();
			};
			Thread fpsThread = new Thread(fpsMethod);
			fpsThread.start();
		} else {
			running = false; // Disables FPS system
		}
	}
	
	// Refreshes the window
	private static void refreshWindow() {
		Window.getWindow().refresh();
	}
	
	// FPS method
	private static void runFps() {
		while (running == true) {
			try {
				TimeUnit.MILLISECONDS.sleep(frameMilliseconds); // Framerate

				AnimationPlayerModule.playAnimations(); // Plays animations
				refreshWindow(); // Refreshes window
				
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
}
