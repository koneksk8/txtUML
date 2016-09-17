package hp;

import hp.model.Box;
import hp.model.MagicBox;
import hp.model.Sound;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;

public class Main {

	static void init() {
		MagicBox m = Action.create(MagicBox.class);
		
		Box box = new BoxImpl();
		Sound sound = new SoundProducer();
		
		m.init(box, sound);

		Action.start(m);

		KeyHandlerImpl handler = new KeyHandlerImpl();
		handler.init(m);
		
		
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(true).addTerminationBlocker(new Object()).run(Main::init);
	}
	
}
