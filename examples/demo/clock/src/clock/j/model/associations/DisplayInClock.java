package clock.j.model.associations;

import clock.j.model.classes.Clock;
import clock.j.model.classes.Display;
import hu.elte.txtuml.api.model.Composition;

public class DisplayInClock extends Composition {
	public class clock extends HiddenContainer<Clock> {}
	public class face extends One<Display> {}
}
