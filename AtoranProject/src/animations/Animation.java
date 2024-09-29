package animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import combat.CombatInterface;
import main.Window;
import utilities.AnimationPlayerModule;
import utilities.AnimationsPreloader;

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
	
	
	protected double chooseEasingFunction(String style, double x) {
		if (style == "easeOutQuart") {
			return (1 - Math.pow(1 - x, 4));
		} else if (style == "linear") {
			return x;
		} else if (style == "easeInOutSine"){ 
			return -(Math.cos(Math.PI * x) - 1) / 2;
		} else if (style == "easeInQuad") {
			return x * x;
		}

		return x;
	}
	
	
	public boolean playFrame() {
		return false;
	}
	
	protected void playKeyframe() {
		if (this.keyframes != null) {

			if (this.keyframes[this.currentKeyframe] != null) {
				this.keyframes[this.currentKeyframe].playKeyframe();
			}
		}
		
		this.currentKeyframe += 1;
	}
	
	
	// CHILDREN
	
	
	// Moves characters position on the screen
	public static class MovementAnimation extends Animation {
		public int xIncrement;
		public int yIncrement;
		public JLabel sprite;
		public Point destination;
		public Point startingLocation;

		public MovementAnimation(JLabel sprite, int frameCount, String easingStyle, Point destination, Point startingLocation) {
			super(frameCount, easingStyle);
			
			this.sprite = sprite;
			this.destination = destination;
			
			
			if (startingLocation == null) {
				this.startingLocation = sprite.getLocation();
			} else {
				this.startingLocation = startingLocation;
			}
			
			
			this.xIncrement = (this.destination.x - sprite.getLocation().x) / this.frameCount;
			this.yIncrement = (this.destination.y - sprite.getLocation().y) / this.frameCount;
		}
		
		public void moveSprite() {
			double x = (double)this.currentKeyframe/(double)this.frameCount;
			
			double amount = this.chooseEasingFunction(this.easingStyle, x);
			
			int xPosition = (int)(this.startingLocation.x + ((destination.x - this.startingLocation.x) * amount));
			int yPosition = (int)(this.startingLocation.y + ((destination.y - this.startingLocation.y) * amount));
			
			Point newSport = new Point(xPosition, yPosition);
			
			this.sprite.setLocation(newSport);
		}
		
		@Override
		public boolean playFrame() {
			moveSprite();
			playKeyframe();
			
			if (this.currentKeyframe == this.frameCount) {
				sprite.setLocation(this.destination);
				
				return true;
			} else {
				xIncrement = (this.destination.x - sprite.getLocation().x) / frameCount;
				yIncrement = (this.destination.y - sprite.getLocation().y) / frameCount;
				
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
			
			if (this.currentKeyframe == 0) {
				this.sprite.setForeground(new Color(sprite.getForeground().getRed(),
						sprite.getForeground().getGreen(),
						sprite.getForeground().getBlue(),
						this.startingValue));
			}
			
			// Fade
			this.sprite.setBackground(new Color(sprite.getBackground().getRed(),
					sprite.getBackground().getGreen(),
					sprite.getBackground().getBlue(),
					(int)((double)this.startingValue - ((this.startingValue - this.endingValue) * increment))));

			this.sprite.setForeground(new Color(sprite.getForeground().getRed(),
					sprite.getForeground().getGreen(),
					sprite.getForeground().getBlue(),
					(int)((double)this.startingValue - ((this.startingValue - this.endingValue) * increment))));
			
			playKeyframe();
			
			sprite.repaint();
			sprite.revalidate();
			
			// End
			if (this.currentKeyframe == this.frameCount) {
				// Fail safe in case increments do not meet the final value
				this.sprite.setBackground(new Color(sprite.getBackground().getRed(),
						sprite.getBackground().getGreen(),
						sprite.getBackground().getBlue(),
						this.endingValue));
				this.sprite.setForeground(new Color(sprite.getForeground().getRed(),
						sprite.getForeground().getGreen(),
						sprite.getForeground().getBlue(),
						this.endingValue));
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	public static class GraphicAnimation extends Animation {
		private ImageIcon[] images;
		private int skip; // frames that should be skipped;
		private int frameRate;
		private JLabel animationSprite;
		private boolean looped;
		private int loopCount = -1;
		private int loopingIndex = 0;

		public GraphicAnimation(JLabel sprite, int frameCount, int uniqueIndex, int skip, int frameRate) {
			super(frameCount, null);
			// TODO Auto-generated constructor stub
			
			this.animationSprite = sprite;
			this.images = AnimationsPreloader.getIconArray(uniqueIndex);
			
			if (frameRate == 0) {
				this.frameRate = 1;
			} else {
				this.frameRate = frameRate;
			}
		}
		
		
		public void setLooped(boolean looped) {
			if (looped == true) {
				this.looped = true;
			} else {
				this.looped = false;
			}
		}
		
		public void setLooped(int loopCount) {
			this.looped = true;
			this.loopCount = loopCount;
		}
		
		public void setLoopStartIndex(int index) {
			this.loopingIndex = index;
		}
		
		@Override
		public boolean playFrame() {
			//if (this.currentKeyframe % this.frameRate == 0) {
			JLabel animationLabel = this.animationSprite;
			
			if (this.frameRate > 1) {
				if (this.currentKeyframe % this.frameRate != 0 && this.currentKeyframe != 0) {
					playKeyframe();
					return false;
				}
			}
			
			if (this.currentKeyframe == this.frameCount) {
				if (this.looped == true) {
					this.currentKeyframe = this.loopingIndex;
					return false;
				} else {
					return true;
				}
			}

			//Image image;
			/*if (this.flipImage == true) {
				image = AnimationPlayerModule.createMirror(this.images[this.currentKeyframe / this.frameRate]);
			} else {
				image = this.images[this.currentKeyframe / this.frameRate];
			}
			
			image = Window.scaleImage(animationLabel.getWidth(), animationLabel.getHeight(), image);*/
			
			animationLabel.setIcon(this.images[this.currentKeyframe / this.frameRate]);
			//}
			
			playKeyframe();
			
			if (this.currentKeyframe == this.frameCount) {
				if (this.looped == true) {
					this.currentKeyframe = this.loopingIndex;
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}
	
	
	public static class ResizeAnimation extends Animation {
		private Dimension targetSize;
		private Dimension originalSize;
		private String anchor; // topLeft, bottomLeft, topRight, bottomRight, middle : how the image will scale
		private JLabel sprite;
		private int scaleFont;
		private int fontSize;
		
		public ResizeAnimation(JLabel sprite, int frameCount, String easingStyle, Dimension targetSize, String anchor, int scaleFont) {
			super(frameCount, easingStyle);
			// TODO Auto-generated constructor stub
			
			this.targetSize = targetSize;
			this.anchor = anchor;
			this.sprite = sprite;
			this.scaleFont = scaleFont;
			this.fontSize = sprite.getFont().getSize();
			
			this.originalSize = sprite.getSize();
		}
		
		@Override
		public boolean playFrame() {
			double increment = ((double)this.currentKeyframe)/((double)this.frameCount);
			increment = chooseEasingFunction(this.easingStyle, increment);
			

			Dimension currentSize = this.sprite.getSize();
			Dimension increase = new Dimension(this.targetSize.width - this.originalSize.width,
					this.targetSize.height - this.originalSize.height);
			
			Dimension totalIncrement = new Dimension(this.originalSize.width +
					(int)(increment * (double)increase.width),
					this.originalSize.height +
					(int)(increment * (double)increase.height));
			
			
			// Resize
			this.sprite.setSize(totalIncrement);
			
			
			if (this.scaleFont != 0) {
				Font font = this.sprite.getFont();

				Font newFont = font.deriveFont((float) (this.fontSize + ((double)this.scaleFont * increment)));
				
				this.sprite.setFont(newFont);
			}
			
			
			// Positioning to account for resize
			if (this.anchor == "middle") {
				Point newLocation = new Point(this.sprite.getLocation().x - (int)(((this.sprite.getWidth() - currentSize.getWidth())/2)),
						this.sprite.getLocation().y - (int)((this.sprite.getHeight() - currentSize.getHeight())/2));
				
				Point test = new Point(this.sprite.getLocation().x + (int)((increment * increase.width)/2),
				this.sprite.getLocation().y + (int)((increment * increase.width)/2));
				
				
				
				Point newLocation2 = new Point(this.sprite.getLocation().x + (int)(increment * (double)increase.width),
						this.sprite.getLocation().y + (int)(increment * (double)increase.height));
				
				this.sprite.setLocation(newLocation);
			}
			// Else do nothing (anchor == topLeft)
			
			playKeyframe();
			
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
		private ArrayList<Integer> startingFrames = new ArrayList<Integer>();

		public CombinedAnimation(int frameCount, ArrayList<Animation> animations, int[] startingFrames) {
			super(frameCount, null);
			
			for (int i = 0; i < startingFrames.length; i++) {
				this.startingFrames.add(startingFrames[i]);
			}
			this.animations = animations;
		}
		
		@Override
		public boolean playFrame() {
			ArrayList<Integer> removals = new ArrayList<Integer>();
			
			for (int i = 0; i < this.animations.size(); i++) {
				if (this.startingFrames.get(i) <= this.currentKeyframe) {
					boolean complete = animations.get(i).playFrame();
					
					if (complete == true) {
						this.animations.set(i, null);
						removals.add(i);
					}
				}
			}
			
			for (int i = 0; i < removals.size(); i++) {
				this.animations.remove(removals.get(i) - i);
				this.startingFrames.remove(removals.get(i) - i);
			}
			
			playKeyframe();
			
			if (this.currentKeyframe == this.frameCount) {
				return true;
			} else {
				return false;
			}
		}
	}
}
