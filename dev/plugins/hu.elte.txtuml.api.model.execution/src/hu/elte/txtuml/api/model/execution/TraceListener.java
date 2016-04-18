package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Listener for valid events of the model execution.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface TraceListener {

	void executionStarted();

	void processingSignal(ModelClass object, Signal signal);

	void usingTransition(ModelClass object, Transition transition);

	void enteringVertex(ModelClass object, Vertex vertex);

	void leavingVertex(ModelClass object, Vertex vertex);

	void executionTerminated();

}
