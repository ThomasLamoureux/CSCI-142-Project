package animations;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Keyframe {
	
	public Keyframe(Animation parent, Runnable method) {
		if (method != null) {
			method.run();
		}
	}

	
	public void playKeyframe() {
		
	}
}
