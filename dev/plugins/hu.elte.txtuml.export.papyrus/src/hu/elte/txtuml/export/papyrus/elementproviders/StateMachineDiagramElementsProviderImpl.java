package hu.elte.txtuml.export.papyrus.elementproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;

import hu.elte.txtuml.export.papyrus.layout.txtuml.TxtUMLElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsProviderImpl implements StateMachineDiagramElementsProvider {

	private DiagramExportationReport report;
	private Collection<Element> nodes;
	private Collection<Relationship> connections;

	public StateMachineDiagramElementsProviderImpl(DiagramExportationReport report, TxtUMLElementsMapper mapper) {
		this.report = report;
		cacheNodes(mapper);
		cacheConnections(mapper);
	}

	private void cacheConnections(TxtUMLElementsMapper mapper) {
		this.connections = mapper.getConnections(this.report);

	}

	private void cacheNodes(TxtUMLElementsMapper mapper) {
		this.nodes = mapper.getNodes(this.report);

	}

	@Override
	public Collection<State> getStatesForRegion(Region region) {
		List<State> result = new ArrayList<>();
		result = region.getOwnedElements().stream()
				.filter(e->(e instanceof State) && this.nodes.contains(e))
				.map(e -> (State)e).collect(Collectors.toList());
		return result;
	}

	@Override
	public Collection<Region> getMainRegions() {
		List<Region> result = new ArrayList<>();
		String stateMachineName = this.report.getReferencedElementName();
		nodes.stream().filter(e -> e.getModel().getName().equals(stateMachineName)).findFirst().ifPresent( sm -> {
			result.addAll(((StateMachine) sm).getRegions());
		});
		
		return result;
	}

	@Override
	public Collection<Region> getRegionsOfState(State state) {
		List<Region> result = new ArrayList<>();
		result = state.getOwnedElements().stream()
				.filter(e->(e instanceof Region)&& this.nodes.contains(e))
				.map(e -> (Region)e).collect(Collectors.toList());
		return result;
	}

}
