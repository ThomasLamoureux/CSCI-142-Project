package cutscenes;

import java.util.function.Consumer;

public class Dialogue {
	private String text;
    private Consumer<Cutscene> event;

    public Dialogue(String text) {
        this(text, null);
    }

    public Dialogue(String text, Consumer<Cutscene> event) {
        this.text = text;
        this.event = event;
    }

    public String getText() {
        return text;
    }

    public void triggerEvent(Cutscene cutscene) {
        if (event != null) {
            event.accept(cutscene);
        }
    }

}

