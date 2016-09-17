package hp;

import hp.model.MagicBox;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Show;

public class HpClassDiagram extends ClassDiagram {

	@Show(MagicBox.class)
	class MyLayout extends Layout {}
}
