package cutscenes;

import java.util.function.Consumer;

public class Dialogue {
	private String text;
    private Consumer<Cutscene> event;
    private String namecard;

    public Dialogue(String text, String namecard) {
        this(text, namecard, null);
    }

    public Dialogue(String text, String namecard, Consumer<Cutscene> event) {
        this.text = text;
        this.event = event;
        this.namecard = namecard;
    }

    public String getText() {
        return text;
    }
    
    public String getName() {
    	return namecard;
    }

    public void triggerEvent(Cutscene cutscene) {
        if (event != null) {
            event.accept(cutscene);
        }
    }

}

