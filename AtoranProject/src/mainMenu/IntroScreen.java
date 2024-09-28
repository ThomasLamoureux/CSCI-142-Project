package mainMenu;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import animations.Animation;
import animations.Animation.CombinedAnimation;
import animations.Animation.FadeAnimation;
import animations.Animation.GraphicAnimation;
import animations.Keyframe;
import engine.Engine;
import main.Window;
import utilities.AnimationPlayerModule;

public class IntroScreen {
	public static void main(String[] args) {
		/*Window window = Window.getWindow();
		window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
		
		JLayeredPane pane = new JLayeredPane();
		pane.setSize(window.getSize());
		pane.setLayout(null);
		pane.setLocation(new Point(0, 0));
		
		JLabel fadeLabel = new JLabel();
		fadeLabel.setSize(window.getSize());
		fadeLabel.setBackground(Color.black);
		fadeLabel.setLocation(new Point(0, 0));
		fadeLabel.setOpaque(true);
		
		JLabel animationLabel = new JLabel();
		animationLabel.setSize(window.getSize());
		animationLabel.setLocation(new Point(0, 0));
		animationLabel.setOpaque(true);
		
		pane.add(animationLabel, JLayeredPane.PALETTE_LAYER);
		pane.add(fadeLabel, JLayeredPane.DEFAULT_LAYER);
		
		GraphicAnimation logoAnimation = new GraphicAnimation(animationLabel, 85, "Resources/Animations/SigmaStudiosIntro", 0, 0, false);
		FadeAnimation fadingAnimation = new FadeAnimation(fadeLabel, 120, "easeOutQuart", 255, 0);
		
		Runnable removeLogoAnimation = () -> {
			animationLabel.setVisible(false);
			System.out.println("hi");
		};
		logoAnimation.keyframes[84] = new Keyframe(removeLogoAnimation);
		
		ArrayList<Animation> animationsList = new ArrayList<>();
		animationsList.add(logoAnimation);
		animationsList.add(fadingAnimation);
		
		CombinedAnimation finalAnimation = new CombinedAnimation(240, animationsList, new int[] {5, 120});
		
		window.setLayout(null);
		window.add(pane);
		
		window.setVisible(true);
		
		Engine.toggleFps(true);
		AnimationPlayerModule.addAnimation(finalAnimation);*/
	}
}
