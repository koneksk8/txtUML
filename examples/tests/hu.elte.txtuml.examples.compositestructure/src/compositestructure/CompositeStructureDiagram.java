package compositestructure;

import compositestructure.model.A;
import compositestructure.model.associations.AB;
import compositestructure.model.associations.AC;
import compositestructure.model.associations.AD;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.Right;

public class CompositeStructureDiagram extends CompositeDiagram<A>{
	
	@Below(from = AB.b.class, val=AC.c.class)
	@Right(from = AB.b.class, val=AD.d.class)
	class L extends Layout{}
}
