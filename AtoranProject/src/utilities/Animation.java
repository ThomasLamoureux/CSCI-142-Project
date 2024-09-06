package utilities;

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
	
	public boolean playFrame() {
		Point spriteLocation = this.sprite.getLocation();

		this.sprite.setLocation(new Point(this.startingLocation.x + xIncrement * (currentPoint + 1), this.startingLocation.y + yIncrement * (currentPoint + 1)));
		
		currentPoint += 1;
		
		if (currentPoint == framesToTake[currentDestination]) {
			if (currentDestination == destinations.length - 1) {
				return true;
			} else {
				currentPoint = 0;
				currentDestination += 1;
				this.startingLocation = this.sprite.getLocation();
				
				xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / framesToTake[currentDestination];
				yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / framesToTake[currentDestination];
				
				return false;
			} 
		} else {
			return false;
		}
	}
}
