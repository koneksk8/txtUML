package compositestructure;

import compositestructure.model.A;
import compositestructure.model.B;
import compositestructure.model.C;
import compositestructure.model.D;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.TopMost;

public class CompositeStructureClassDiagram extends ClassDiagram{
	@TopMost(A.class)
	@North(from=B.class, val=A.class)
	@East(from=B.class, val=C.class)
	@East(from=C.class, val=D.class)
	class L extends Layout{}
}
