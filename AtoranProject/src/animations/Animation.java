package animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;

import main.Window;
import utilities.AnimationPlayerModule;

public class Animation {
	public int frameCount;
	public Keyframe[] keyframes;
	protected int currentKeyframe = 0;
	public String easingStyle;
	
	
	public Animation(int frameCount, String easingStyle) {
		this.frameCount = frameCount;
		this.easingStyle = easingStyle;
		
		this.keyframes = new Keyframe[frameCount];
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
	
	
	public boolean playFrame() {
		return false;
	}
	
	
	// CHILDREN
	
	
	// Moves characters position on the screen
	public static class MovementAnimation extends Animation {
		public int xIncrement;
		public int yIncrement;
		public JLabel sprite;
		public int currentPoint = 0;
		public int currentDestination = 0;
		public Point[] destinations;
		public Point startingLocation;

		public MovementAnimation(JLabel sprite, Point[] destinations, int frameCount, String easingStyle) {
			super(frameCount, easingStyle);
			
			this.sprite = sprite;
			this.destinations = destinations;
			
			
			this.xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / frameCount[currentDestination];
			this.yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / frameCount[currentDestination];
		}
		
		public void moveSprite() {
			if (destinations[currentDestination] == null) {
				return;
			}
			double x = (double)currentPoint/(double)frameCount;
			
			double amount = this.chooseEasingFunction(this.easingStyle, x);
			
			int xPosition = (int)(this.startingLocation.x + ((destinations[currentDestination].x - this.startingLocation.x) * amount));
			int yPosition = (int)(this.startingLocation.y + ((destinations[currentDestination].y - this.startingLocation.y) * amount));
			
			Point newSport = new Point(xPosition, yPosition);
			
			this.sprite.setLocation(newSport);
		}
		
		@Override
		public boolean playFrame() {
			moveSprite();
			
			if (this.keyframes != null) {

				if (this.keyframes[this.currentKeyframe] != null) {
					this.keyframes[this.currentKeyframe].playKeyframe();
				}
			}

			//this.sprite.setLocation(new Point(this.startingLocation.x + xIncrement * (currentPoint + 1), this.startingLocation.y + yIncrement * (currentPoint + 1)));
			currentKeyframe += 1;
			currentPoint += 1;
			
			if (currentPoint == frameCount) {
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

						xIncrement = (this.destinations[currentDestination].x - sprite.getLocation().x) / frameCount[currentDestination];
						yIncrement = (this.destinations[currentDestination].y - sprite.getLocation().y) / frameCount[currentDestination];
					}
					
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	// Squishes character to add movement
	public static class FadeAnimation extends Animation {
		private int endingValue;
		private int startingValue;
		private int increment;
		private JLabel sprite;

		public FadeAnimation(JLabel sprite, int frameCount, String easingStyle, int startingValue, int endingValue) {
			super(frameCount, easingStyle);
			// TODO Auto-generated constructor stub
			
			this.endingValue = endingValue;
			this.startingValue = startingValue;
			this.sprite = sprite;
		}
		
		@Override
		public boolean playFrame() {
			double increment = (double)this.currentKeyframe/(double)this.frameCount;
			increment = chooseEasingFunction(this.easingStyle, increment);
			
			// Begin
			if (this.currentKeyframe == 0) {
				this.sprite.setBackground(new Color(sprite.getBackground().getRed(),
						sprite.getBackground().getGreen(),
						sprite.getBackground().getBlue(),
						this.startingValue));
			}
			
			// Fade
			this.sprite.setBackground(new Color(sprite.getBackground().getRed(),
					sprite.getBackground().getGreen(),
					sprite.getBackground().getBlue(),
					(int)((double)this.endingValue * increment)));
			
			// End
			if (this.currentKeyframe == this.frameCount) {
				// Fail safe in case increments do not meet the final value
				this.sprite.setBackground(new Color(sprite.getBackground().getRed(),
						sprite.getBackground().getGreen(),
						sprite.getBackground().getBlue(),
						this.endingValue));
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	public static class GraphicAnimation extends Animation {
		private Image[] images;

		public GraphicAnimation(JLabel sprites, int frameCount, String easingStyle, String folderPath, int skip, int frameRate) {
			super(frameCount, easingStyle);
			// TODO Auto-generated constructor stub
			
			this.images = AnimationPlayerModule.createIconsFromFolder(folderPath);
		}
		
	}
	
	
	public static class ResizeAnimation extends Animation {
		private Dimension targetSize;
		private Dimension originalSize;
		private String anchor; // topLeft, bottomLeft, topRight, bottomRight, middle : how the image will scale
		private JLabel sprite;

		public ResizeAnimation(JLabel sprite, int frameCount, String easingStyle, Dimension targetSize, String anchor) {
			super(frameCount, easingStyle);
			// TODO Auto-generated constructor stub
			
			this.targetSize = targetSize;
			this.anchor = anchor;
			this.sprite = sprite;
			
			this.originalSize = sprite.getSize();
		}
		
		@Override
		public boolean playFrame() {
			double increment = (double)this.currentKeyframe/(double)this.frameCount;
			increment = chooseEasingFunction(this.easingStyle, increment);
			
			Dimension increase = new Dimension(this.targetSize.width - this.originalSize.width,
					this.targetSize.height - this.originalSize.height);
			
			Dimension totalIncrement = new Dimension(this.originalSize.width +
					(int)(increment * (double)increase.width),
					this.originalSize.height +
					(int)(increment * (double)increase.height));
			
			// Resize
			this.sprite.setSize(totalIncrement);
			
			// Positioning to account for resize
			if (this.anchor == "middle") {
				Point newLocation = new Point(this.sprite.getLocation().x + (int)(increment * (double)increase.width),
						this.sprite.getLocation().y + (int)(increment * (double)increase.height));
				this.sprite.setLocation(newLocation);
			}
			// Else do nothing (anchor == topLeft)
			
			// End
			if (this.currentKeyframe == this.frameCount) {
				// Fail safe in case increments do not meet the final value
				this.sprite.setSize(this.targetSize);
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	
	public static class CombinedAnimation extends Animation {
		private ArrayList<Animation> animations = new ArrayList<Animation>();
		private int[] startingFrames;

		public CombinedAnimation(int frameCount, String easingStyle, ArrayList<Animation> animations, int[] startingFrames) {
			super(frameCount, easingStyle);
			// TODO Auto-generated constructor stub
			
			this.animations = animations;
			this.startingFrames = startingFrames;
		}
		
		@Override
		public boolean playFrame() {
			for (int i = 0; i < this.animations.size(); i++) {
				if (this.startingFrames[i] >= this.currentKeyframe) {
					boolean complete = animations.get(i).playFrame();
					
					if (complete == true) {
						this.animations.remove(i);
						i -= 1;
					}
				}
			}
			
			
			if (this.currentKeyframe == this.frameCount) {
				return true;
			} else {
				return false;
			}
		}
	}
}
