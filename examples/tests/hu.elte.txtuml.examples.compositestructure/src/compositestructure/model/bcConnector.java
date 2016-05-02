package compositestructure.model;

import compositestructure.model.associations.AB;
import compositestructure.model.associations.AC;
import hu.elte.txtuml.api.model.Connector;

public class bcConnector extends Connector{
	public class connEnd1 extends One<AB.b, B.PB> {}
	public class connEnd2 extends One<AC.c, C.PC> {}
}
