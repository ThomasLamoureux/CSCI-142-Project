package engine;

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
	
	
	public static void main(String[] args) {
		toggleFps(true);
	}
	
	
	public static void toggleFps(boolean toggle) {
		if (toggle == true) {
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
	
	/*
	private static void playAnimationFrame() {
		System.out.println("frame");
	}*/
	
	
	
	private static void runFps() {
		while (running == true) {
			try {
				TimeUnit.MILLISECONDS.sleep(frameMilliseconds);
				//TimeUnit.MILLISECONDS.wait(frameMilliseconds);
				
				//playAnimationFrame();
				AnimationPlayerModule.playAnimations();
				refreshWindow();
				Date dt = new Date();
				
				//System.out.println(dt.getTime());
				
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
}
