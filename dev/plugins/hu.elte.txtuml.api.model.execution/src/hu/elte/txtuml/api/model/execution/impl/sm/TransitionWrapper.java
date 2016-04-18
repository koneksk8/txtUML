package hu.elte.txtuml.api.model.execution.impl.sm;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.Trigger.AnyPort;
import hu.elte.txtuml.api.model.runtime.ElseException;
import hu.elte.txtuml.api.model.runtime.Wrapper;

public interface TransitionWrapper extends Wrapper<Transition> {

	VertexWrapper getTarget();

	/**
	 * Checks whether the given signal is <b>not</b> triggering the wrapped
	 * transition.
	 * 
	 * @param signal
	 *            the signal to check
	 * @param port
	 *            the port through which the signal arrived (might be
	 *            {@code null} in case the signal did not arrive through a port)
	 * @return true if the signal does <b>not</b> trigger the specified
	 *         transition, false otherwise
	 */
	boolean notApplicableTrigger(Signal signal, Port<?, ?> port);

	boolean checkGuard() throws ElseException;

	void performEffect();

	// create methods

	abstract class Static {

		private Static() {

		}

		private static abstract class Base implements TransitionWrapper {

			@Override
			public Class<?> getTypeOfWrapped() {
				return getWrapped().getClass();
			}

			@Override
			public boolean checkGuard() throws ElseException {
				return getWrapped().guard();
			}

			@Override
			public void performEffect() {
				getWrapped().effect();
			}
		}

		static TransitionWrapper create(Transition wrapped, VertexWrapper target) {
			Trigger trigger = wrapped.getClass().getAnnotation(Trigger.class);
			if (trigger == null) {
				return createUnlabeled(wrapped, target);
			} else {
				return createLabeled(wrapped, target, trigger.value(), trigger.port());
			}
		}

		static TransitionWrapper createUnlabeled(final Transition wrapped, final VertexWrapper target) {
			return new Base() {

				@Override
				public Transition getWrapped() {
					return wrapped;
				}

				@Override
				public VertexWrapper getTarget() {
					return target;
				}

				@Override
				public boolean notApplicableTrigger(Signal signal, Port<?, ?> port) {
					return signal != null;
				}

			};
		}

		static TransitionWrapper createLabeled(final Transition wrapped, final VertexWrapper target,
				final Class<? extends Signal> trigger, final Class<? extends Port<?, ?>> portOfTrigger) {
			return new Base() {

				@Override
				public Transition getWrapped() {
					return wrapped;
				}

				@Override
				public VertexWrapper getTarget() {
					return target;
				}

				@Override
				public boolean notApplicableTrigger(Signal signal, Port<?, ?> portInstance) {
					if (trigger.isInstance(signal)
							&& (portOfTrigger == AnyPort.class || portOfTrigger.isInstance(portInstance))) {
						return false;
					}
					return true;
				}

			};
		}
	}

}
