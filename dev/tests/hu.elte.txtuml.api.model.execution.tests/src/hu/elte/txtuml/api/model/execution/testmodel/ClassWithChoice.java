package hu.elte.txtuml.api.model.execution.testmodel;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;

public class ClassWithChoice extends ModelClass {

	public class Init extends Initial {
	}

	public class S1 extends State {
	}

	public class C extends Choice {
	}

	@From(Init.class)
	@To(S1.class)
	public class Initialize extends Transition {
	}

	@From(S1.class)
	@To(C.class)
	@Trigger(Sig0.class)
	public class S1_C extends Transition {
	}

	@From(C.class)
	@To(S1.class)
	@Trigger(Sig0.class)
	public class T1 extends Transition {

		@Override
		public boolean guard() {
			Sig0 s = getTrigger(Sig0.class);
			return s.value == 0;
		}

	}

	@From(C.class)
	@To(S1.class)
	@Trigger(Sig0.class)
	public class T2 extends Transition {

		@Override
		public boolean guard() {
			Sig0 s = getTrigger(Sig0.class);
			return s.value == 1;
		}

	}

	@From(C.class)
	@To(S1.class)
	@Trigger(Sig0.class)
	public class T3 extends Transition {

		@Override
		public boolean guard() {
			return Else();
		}

	}
}
