package animations;

import java.awt.Point;

import javax.swing.JLabel;

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
	
	
	
	public Animation(JLabel sprite, Point[] destinations, int[] framesToTake) {
		this.framesToTake = framesToTake;
		this.destinations = destinations;
		this.startingLocation = sprite.getLocation();
		this.sprite = sprite;
		
		
		System.out.println(this.startingLocation.y);
		
		xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / framesToTake[currentDestination];
		yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / framesToTake[currentDestination];
		
		System.out.println(yIncrement);
	}
	
	public Animation(JLabel characterSprite, Point[] destinations) {
		
	}
	
	public double easingFunction(double x) {
		return (1 - Math.pow(1 - x, 4));
	}
	
	public void moveSprite() {
		double x = (double)currentPoint/(double)framesToTake[currentDestination];
		
		double amount = this.easingFunction(x);
		
		int xPosition = (int)(this.startingLocation.x + ((destinations[currentDestination].x - this.startingLocation.x) * amount));
		int yPosition = (int)(this.startingLocation.y + ((destinations[currentDestination].y - this.startingLocation.y) * amount));
		
		Point newSport = new Point(xPosition, yPosition);
		
		System.out.println(xPosition + " x");
		
		this.sprite.setLocation(newSport);
	}
	
	public boolean playFrame() {
		moveSprite();
		Point spriteLocation = this.sprite.getLocation();

		this.sprite.setLocation(new Point(this.startingLocation.x + xIncrement * (currentPoint + 1), this.startingLocation.y + yIncrement * (currentPoint + 1)));
		
		currentPoint += 1;
		
		if (currentPoint == framesToTake[currentDestination]) {
			if (currentDestination == destinations.length - 1) {
				sprite.setLocation(destinations[currentDestination]);
				return true;
			} else {
				sprite.setLocation(destinations[currentDestination]);
				
				currentPoint = 0;
				currentDestination += 1;
				this.startingLocation = this.sprite.getLocation();
				
				xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / framesToTake[currentDestination];
				yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / framesToTake[currentDestination];
				System.out.println(xIncrement);
				System.out.println("frames");
				
				return false;
			} 
		} else {
			return false;
		}
	}
	
	// Moves characters position on the screen
	public static class MovementAnimations extends Animation {

		public MovementAnimations(JLabel sprite, Point[] destinations, int[] framesToTake) {
			super(sprite, destinations, framesToTake);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean playFrame() {
			return false;
		}
	}
	
	// Squishes character to add movement
	public static class IdleAnimation extends Animation {

		public IdleAnimation(JLabel sprite, Point[] destinations, int[] framesToTake) {
			super(sprite, destinations, framesToTake);
			// TODO Auto-generated constructor stub
		}
		
	}
}
