package animations;

import java.awt.Point;

import javax.swing.JLabel;

import main.Window;

public class Animation {
	public int[] framesToTake;
	public Point[] destinations;
	public Point[] pointsOnTrip;
	public int currentPoint = 0;
	public JLabel sprite;
	public int xIncrement;
	public int yIncrement;
	public Point startingLocation;
	public int currentDestination = 0;
	public Keyframe[] keyframes;
	private int currentKeyframe = 0;
	public String easingStyle;
	
	
	public Animation(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
		this.framesToTake = framesToTake;
		this.destinations = destinations;
		this.startingLocation = sprite.getLocation();
		this.sprite = sprite;
		this.easingStyle = easingStyle;

		
		/*for (int i = 0; i < destinations.length; i++) {
			if (destinations[i] != null) {
				destinations[i] = new Point(Window.scaleInt(destinations[i].x), Window.scaleInt(destinations[i].y));
				destinations[i] = Window.scalePoint(destinations[i]);
			}
		}*/
		
		xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / framesToTake[currentDestination];
		yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / framesToTake[currentDestination];
	}
	
	
	public Animation(JLabel characterSprite, Point[] destinations) {
		
	}
	
	
	public double chooseEasingFunction(String style, double x) {
		if (style == "easeOutQuart") {
			return (1 - Math.pow(1 - x, 4));
		} else if (style == "linear") {
			return x;
		} else if (style == "easeInOutSine"){ 
			return -(Math.cos(Math.PI * x) - 1) / 2;
		}
		
		return 0.0;
	}
	
	
	public void moveSprite() {
		if (destinations[currentDestination] == null) {
			return;
		}
		double x = (double)currentPoint/(double)framesToTake[currentDestination];
		
		double amount = this.chooseEasingFunction(this.easingStyle, x);
		
		int xPosition = (int)(this.startingLocation.x + ((destinations[currentDestination].x - this.startingLocation.x) * amount));
		int yPosition = (int)(this.startingLocation.y + ((destinations[currentDestination].y - this.startingLocation.y) * amount));
		
		Point newSport = new Point(xPosition, yPosition);
		
		this.sprite.setLocation(newSport);
	}
	
	public boolean playFrame() {
		moveSprite();
		Point spriteLocation = this.sprite.getLocation();
		
		if (this.keyframes != null) {

			if (this.keyframes[currentKeyframe] != null) {
				this.keyframes[currentKeyframe].playKeyframe();
			}
		}

		//this.sprite.setLocation(new Point(this.startingLocation.x + xIncrement * (currentPoint + 1), this.startingLocation.y + yIncrement * (currentPoint + 1)));
		currentKeyframe += 1;
		currentPoint += 1;
		
		if (currentPoint == framesToTake[currentDestination]) {
			if (currentDestination == destinations.length - 1) {
				if (destinations[currentDestination] != null) {
					sprite.setLocation(destinations[currentDestination]);
				}
				
				return true;
			} else {
				if (destinations[currentDestination] != null) {
					sprite.setLocation(destinations[currentDestination]);
				}
				
				currentPoint = 0;
				currentDestination += 1;
				
				if (destinations[currentDestination] != null) {
					
					this.startingLocation = this.sprite.getLocation();

					xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / framesToTake[currentDestination];
					yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / framesToTake[currentDestination];
				}
				
				return false;
			}
		} else {
			return false;
		}
	}
	
	// Moves characters position on the screen
	public static class MovementAnimations extends Animation {

		public MovementAnimations(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
			super(sprite, destinations, framesToTake, easingStyle);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean playFrame() {
			return false;
		}
	}
	
	// Squishes character to add movement
	public static class FadeAnimation extends Animation {

		public FadeAnimation(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
			super(sprite, destinations, framesToTake, easingStyle);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	public static class GraphicAnimation extends Animation {

		public GraphicAnimation(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
			super(sprite, destinations, framesToTake, easingStyle);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	public static class ResizeAnimation extends Animation {

		public ResizeAnimation(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
			super(sprite, destinations, framesToTake, easingStyle);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	public static class CombinedAnimation extends Animation {

		public CombinedAnimation(JLabel sprite, Point[] destinations, int[] framesToTake, String easingStyle) {
			super(sprite, destinations, framesToTake, easingStyle);
			// TODO Auto-generated constructor stub
		}
	}
}
