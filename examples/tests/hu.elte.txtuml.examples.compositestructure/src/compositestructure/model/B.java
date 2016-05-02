package compositestructure.model;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;

public class B extends ModelClass{
	
	public class PB extends Port<MyEmptyInterface,MyEmptyInterface>{}
}
