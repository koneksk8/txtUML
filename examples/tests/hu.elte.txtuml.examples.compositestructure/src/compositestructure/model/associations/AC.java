package compositestructure.model.associations;


import hu.elte.txtuml.api.model.Composition;
import compositestructure.model.A;
import compositestructure.model.C;

public class AC extends Composition{
	public class a extends HiddenContainer<A> {}
	public class c extends One<C> {}
}
