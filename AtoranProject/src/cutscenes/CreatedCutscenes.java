package cutscenes;

import java.util.ArrayList;
import java.util.List;

public class CreatedCutscenes {
	
	public static Cutscene introCutscene() {
		List<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		Dialogue dialogueOne = new Dialogue("...");
		dialogues.add(dialogueOne);
		Dialogue dialogueTwo = new Dialogue("I need to get revenge, my hunger will not be satisfied until i kill Samoht— the wizard who murdered my mother and my pack");
		dialogues.add(dialogueTwo);
		Dialogue dialogueThree = new Dialogue("I wear her pelt in honor of the life she and the pack gave me after I was abandoned in the woods as a youngling. They raised me, and I became one of them.");
		dialogues.add(dialogueThree);
		Dialogue dialogueFour = new Dialogue("My senses—smell, direction, hearing—have been sharpened to perfection. Samoht took everything from me. Now, I will take everything from him.");
		dialogues.add(dialogueFour);
		
		Cutscene cutscene = new Cutscene(dialogues);
		return cutscene;
	}
	
	
	public static Cutscene levelOneIntro() {
		List<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		Dialogue dialogueOne = new Dialogue("Slimes are attacking the village! Please, you have to help us!");
		dialogues.add(dialogueOne);
		Dialogue dialogueTwo = new Dialogue("Why should I care? What could possibly make me fight for you?");
		dialogues.add(dialogueTwo);
		Dialogue dialogueThree = new Dialogue("I know where Samoht is. The evil wizard you’re hunting... I have the information you need.");
		dialogues.add(dialogueThree);
		Dialogue dialogueFour = new Dialogue("...");
		dialogues.add(dialogueFour);
		Dialogue dialogueFive = new Dialogue("Samoht? You speak his name as if it’s nothing. Fine, I’ll help you, but not for your sake. I’ll crush the slimes, and then I’ll crush him.");
		dialogues.add(dialogueFive);
		Dialogue dialogueSix = new Dialogue("He’s taken everything from me—my family, my world. My pack was my only family, and he slaughtered them without mercy. I wear their memory with me always.");
		dialogues.add(dialogueSix);
		Dialogue dialogueSeven = new Dialogue("My senses are sharp, honed by their teachings. And once I finish with these slimes, I will hunt down Samoht and make him suffer for what he's done.");
		dialogues.add(dialogueSeven);
		
		Cutscene cutscene = new Cutscene(dialogues);
		return cutscene;
	}
	
	public static Cutscene levelOneOutro() {
		List<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		Dialogue dialogueOne = new Dialogue("You saved us! We’re forever grateful. The slimes would have destroyed everything without your help.");
		dialogues.add(dialogueOne);
		Dialogue dialogueTwo = new Dialogue("I didn’t do it for you. My path is set. But your village is safe... for now.");
		dialogues.add(dialogueTwo);
		Dialogue dialogueThree = new Dialogue("Still, we owe you more than words. Please, take this as a token of our thanks,\r\n"
				+ "*\"This is a rare potion, brewed from the enchanted springs deep within the forest. It will restore your strength when you need it most.\r\n"
				+ "");
		dialogues.add(dialogueThree);
		Dialogue dialogueFour = new Dialogue("A potion? Fine. Perhaps it’ll come in handy");
		dialogues.add(dialogueFour);
		Dialogue dialogueFive = new Dialogue("May it guide you in your journey, wherever it may lead.");
		dialogues.add(dialogueFive);
		Dialogue dialogueSix = new Dialogue("The only place I’m going is to Samoht’s end.");
		dialogues.add(dialogueSix);

		
		Cutscene cutscene = new Cutscene(dialogues);
		return cutscene;
	}
	
	public static Cutscene bearCutscene() {
		List<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		Dialogue dialogueOne = new Dialogue("Well, well, if it isn’t the last wolf. I’ve heard about your pack’s fate, Good riddance, I say. I’m glad the wolf pack is gone. Now, the woods belong to us!");
		dialogues.add(dialogueOne);
		Dialogue dialogueTwo = new Dialogue("You speak of them as if they were nothing. The wolves and I protected these woods while your kind lurked in the shadows, scavenging our scraps.");
		dialogues.add(dialogueTwo);
		Dialogue dialogueThree = new Dialogue("Protected? Is that what you call it? You wolves thought you ruled these lands. But now, with your pack gone, the forest is ours, and there’s nothing left of your kind but you. Just another stray, lost in a world that no longer wants you.");
		dialogues.add(dialogueThree);
		Dialogue dialogueFour = new Dialogue("You should watch your words carefully. I may be the last, but I carry the strength of my pack with me. If you think the woods are yours, then challenge me and see how long you keep them.");
		dialogues.add(dialogueFour);
		Dialogue dialogueFive = new Dialogue("I’ve no need to dirty my claws with a lone wolf. The forest will consume you soon enough. Your time is over, Atoran. The woods no longer have a place for your kind.");
		dialogues.add(dialogueFive);
		Dialogue dialogueSix = new Dialogue("My time is far from over. If you think these woods belong to you, then prove it—right here, right now.");
		dialogues.add(dialogueSix);
		Dialogue dialogueSeven = new Dialogue("You want a fight? Fine. I’ll tear you apart, just like your pack!");
		dialogues.add(dialogueSeven);

		
		Cutscene cutscene = new Cutscene(dialogues);
		return cutscene;
	}
	
	public static Cutscene dragonCutscene() {
		List<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		Dialogue dialogueOne = new Dialogue("Who dares disturb my slumber?, I smell blood and vengeance on you, wolf. Speak, or be reduced to ash!");
		dialogues.add(dialogueOne);
		Dialogue dialogueTwo = new Dialogue("I didn’t come to talk, But if you want a fight, I’ll give you one.");
		dialogues.add(dialogueTwo);
		Dialogue dialogueThree = new Dialogue("Bold words for a mortal!");
		dialogues.add(dialogueThree);
		Dialogue dialogueFour = new Dialogue("I’ve fought worse.");
		dialogues.add(dialogueFour);
		Dialogue dialogueFive = new Dialogue("\"I’ve no need to dirty my claws with a lone wolf. The forest will consume you soon enough. Your time is over, Atoran. The woods no longer have a place for your kind.");
		dialogues.add(dialogueFive);
		Dialogue dialogueSix = new Dialogue("My time is far from over. If you think these woods belong to you, then prove it—right here, right now.");
		dialogues.add(dialogueSix);
		Dialogue dialogueSeven = new Dialogue("You want a fight? Fine. I’ll tear you apart, just like your pack!");
		dialogues.add(dialogueSeven);

		
		Cutscene cutscene = new Cutscene(dialogues);
		return cutscene;
	}
}
