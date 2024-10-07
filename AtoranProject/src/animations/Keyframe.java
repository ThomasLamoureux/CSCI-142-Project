package animations;

// This is really just a Runnable. Used for animations
public class Keyframe {
	private Runnable method;
	
	public Keyframe(Runnable method) {
		this.method = method;
	}

	
	public void playKeyframe() {
		method.run();
	}
}
