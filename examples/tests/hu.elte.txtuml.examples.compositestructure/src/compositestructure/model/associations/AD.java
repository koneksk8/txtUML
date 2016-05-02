package compositestructure.model.associations;

import compositestructure.model.A;
import compositestructure.model.D;
import hu.elte.txtuml.api.model.Composition;

public class AD extends Composition{
	public class a extends HiddenContainer<A> {}
	public class d extends One<D> {}
}