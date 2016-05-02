package compositestructure.model.associations;


import hu.elte.txtuml.api.model.Composition;
import compositestructure.model.A;
import compositestructure.model.B;

public class AB extends Composition{
	public class a extends HiddenContainer<A> {}
	public class b extends One<B> {}
}
