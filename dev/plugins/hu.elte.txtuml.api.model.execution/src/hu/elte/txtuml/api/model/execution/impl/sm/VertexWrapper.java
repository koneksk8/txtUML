package hu.elte.txtuml.api.model.execution.impl.sm;

import hu.elte.txtuml.api.model.StateMachine.Choice;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.runtime.Wrapper;

public interface VertexWrapper extends Wrapper<Vertex> {

	VertexWrapper getContainer();

	TransitionWrapper[] getOutgoings();

	boolean isChoice();

	boolean isComposite();

	void performEntry();

	void performExit();

	VertexWrapper getInitialOfSubSM();

	void setSubSM(VertexWrapper initial);

	// create methods

	abstract class Static {

		private Static() {
		}

		private static abstract class Base implements VertexWrapper {
			@Override
			public boolean isChoice() {
				return getWrapped() instanceof Choice;
			}

			@Override
			public Class<?> getTypeOfWrapped() {
				return getWrapped().getClass();
			}

			@Override
			public void performEntry() {
				getWrapped().entry();
			}

			@Override
			public void performExit() {
				getWrapped().exit();
			}

		}

		public static VertexWrapper create(final Vertex wrapped, final VertexWrapper container,
				final TransitionWrapper[] outgoings) {
			return new Base() {

				@Override
				public Vertex getWrapped() {
					return wrapped;
				}

				@Override
				public VertexWrapper getContainer() {
					return container;
				}

				@Override
				public TransitionWrapper[] getOutgoings() {
					return outgoings;
				}

				@Override
				public boolean isComposite() {
					return false;
				}

				@Override
				public VertexWrapper getInitialOfSubSM() {
					throw new UnsupportedOperationException();
				}

				@Override
				public void setSubSM(VertexWrapper initial) {
					throw new UnsupportedOperationException();
				}

			};
		}

		public static VertexWrapper createComposite(final Vertex wrapped, final VertexWrapper container,
				final TransitionWrapper[] outgoings) {
			return new Base() {
				private VertexWrapper initialOfSubSM = null;

				@Override
				public Vertex getWrapped() {
					return wrapped;
				}

				@Override
				public VertexWrapper getContainer() {
					return container;
				}

				@Override
				public TransitionWrapper[] getOutgoings() {
					return outgoings;
				}

				@Override
				public boolean isComposite() {
					return true;
				}

				@Override
				public boolean isChoice() {
					return false;
				}

				@Override
				public VertexWrapper getInitialOfSubSM() {
					return initialOfSubSM;
				}

				@Override
				public void setSubSM(VertexWrapper initial) {
					if (initialOfSubSM == null) {
						initialOfSubSM = initial;
					}
				}

			};
		}
	}
}
