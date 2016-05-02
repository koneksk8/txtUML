package compositestructure.model;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;

public class A extends ModelClass{
	
	public class P extends Port<MyEmptyInterface,MyEmptyInterface>{}
}
