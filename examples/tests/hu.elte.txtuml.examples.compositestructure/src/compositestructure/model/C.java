package compositestructure.model;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;

public class C extends ModelClass{
	
	public class PC extends Port<MyEmptyInterface,MyEmptyInterface>{}
}
