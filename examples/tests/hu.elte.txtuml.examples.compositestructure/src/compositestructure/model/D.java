package compositestructure.model;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;

public class D extends ModelClass{

	class PD extends Port<MyEmptyInterface,MyEmptyInterface>{}
}
