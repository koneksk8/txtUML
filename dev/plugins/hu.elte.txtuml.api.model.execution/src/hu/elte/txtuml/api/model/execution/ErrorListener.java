package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Listener for runtime errors of the model execution.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface ErrorListener {

	void lowerBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd);

	void upperBoundOfMultiplicityOffended(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd);

	void linkingDeletedObject(ModelClass obj);

	void unlinkingDeletedObject(ModelClass obj);

	void startingDeletedObject(ModelClass obj);

	void objectCannotBeDeleted(ModelClass obj);

	void guardsOfTransitionsAreOverlapping(Transition transition1, Transition transition, Vertex vertex);

	void moreThanOneElseTransitionsFromChoice(Vertex choice);

	void noTransitionFromChoice(Vertex choice);

	void elseGuardFromNonChoiceVertex(Transition transition);

	void multipleContainerForAnObject(ModelClass obj, Class<? extends AssociationEnd<?, ?>> assocEnd);

}
