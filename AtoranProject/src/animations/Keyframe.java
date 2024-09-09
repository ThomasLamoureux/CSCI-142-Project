package animations;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Keyframe {
	private Runnable method;
	
	public Keyframe(Runnable method) {
		this.method = method;
	}

	
	public void playKeyframe() {
		method.run();
	}
}
