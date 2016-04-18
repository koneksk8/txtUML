package pingpong.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import pingpong.x.model.Ball;
import pingpong.x.model.Game;

public class Tester {

	static void init() {
		Game game = Action.create(Game.class);
		Action.start(game);
		Action.send(new Ball(4), game);
	}

	public static void main(String[] args) {
		ModelExecutor.Static.create().setTraceLogging(true).run(Tester::init);
	}

}
