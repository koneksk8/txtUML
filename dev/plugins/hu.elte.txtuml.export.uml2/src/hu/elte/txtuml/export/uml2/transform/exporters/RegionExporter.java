package hu.elte.txtuml.export.uml2.transform.exporters;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.uml2.TxtUMLToUML2.ExportMode;
import hu.elte.txtuml.export.uml2.transform.visitors.TransitionVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.VertexVisitor;

public class RegionExporter {

	private final ModelExporter modelExporter;
	private ExportMode exportMode;

	public RegionExporter(ModelExporter modelExporter, ExportMode exportMode) {
		this.modelExporter = modelExporter;
		this.exportMode = exportMode;
	}

	public void exportRegion(TypeDeclaration ownerDeclaration, StateMachine stateMachine, Region region) {
		exportVertices(ownerDeclaration, stateMachine, region);
		exportTransitions(ownerDeclaration, stateMachine, region);
	}

	private void exportVertices(TypeDeclaration ownerDeclaration, StateMachine stateMachine, Region region) {
		VertexVisitor visitor = new VertexVisitor(new VertexExporter(modelExporter, stateMachine, region, exportMode),
				ownerDeclaration);
		ownerDeclaration.accept(visitor);
		
	}

	private void exportTransitions(TypeDeclaration ownerDeclaration, StateMachine stateMachine, Region region) {
		TransitionVisitor visitor = new TransitionVisitor(new TransitionExporter(modelExporter, stateMachine, region, exportMode),
				ownerDeclaration);
		ownerDeclaration.accept(visitor);
	}

}
