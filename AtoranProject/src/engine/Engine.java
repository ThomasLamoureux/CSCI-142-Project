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
	private static int frameMilliseconds = 10;
	
	
	public static void toggleFps(boolean toggle) {
		if (toggle == true) {
			if (running == true) {
				return;
			}
			running = true;
			
			Runnable fpsMethod = () -> {
				runFps();
			};
			Thread fpsThread = new Thread(fpsMethod);
			fpsThread.start();
		} else {
			running = false;
		}
	}
	
	
	private static void refreshWindow() {
		Window.getWindow().refresh();
	}
	
	private static void runFps() {
		while (running == true) {
			//checkWindowSize();
			try {
				TimeUnit.MILLISECONDS.sleep(frameMilliseconds);
				//TimeUnit.MILLISECONDS.wait(frameMilliseconds);
				
				//playAnimationFrame();
				AnimationPlayerModule.playAnimations();
				refreshWindow();
				
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
}
