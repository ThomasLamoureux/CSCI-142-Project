package utilities;

import java.util.ArrayList;

public class AnimationPlayerModule {
	private static ArrayList<Animation> animations = new ArrayList<Animation>();

	public static boolean playFrame(Animation animation) {
		if (animation.playFrame() == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void playAnimations() {
		for (int i = 0; i < animations.size(); i++) {
			if (i >= animations.size()) {
				break;
			}
			boolean complete = playFrame(animations.get(i));
			
			if (complete == true) {
				animations.remove(i);
				i -= 1;
			}
		}
	}
	
	public static void addAnimation(Animation animation) {
		animations.add(animation);
	}
}
