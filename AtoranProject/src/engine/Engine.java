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
	
	
	/*private static void checkWindowSize() {
		System.out.println(Window.getWindow().getBounds());
		Rectangle windowSize = Window.getWindow().getBounds();
		
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		if (Window.currentWindowSize != windowSize.width) {
			Window.resizeWindow(windowSize.width);
		}
	}*/
	
	/*
	private static void playAnimationFrame() {
		System.out.println("frame");
	}*/
	
	/*class FPSCounter extends Thread{
	    private long lastTime;
	    private double fps; //could be int or long for integer values

	    public void run(){
	        while (true){//lazy me, add a condition for an finishable thread
	            lastTime = System.nanoTime();
	            try{
	                Thread.sleep(1000); // longer than one frame
	            } catch (InterruptedException e){

	            }
	            fps = 1000000000.0 / (System.nanoTime() - lastTime); //one second(nano) divided by amount of time it takes for one frame to finish
	            lastTime = System.nanoTime();
	        }
	    }
	    public double fps(){
	        return fps;
	    } 
	}*/
	
	private static void runFps() {
		while (running == true) {
			//checkWindowSize();
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
