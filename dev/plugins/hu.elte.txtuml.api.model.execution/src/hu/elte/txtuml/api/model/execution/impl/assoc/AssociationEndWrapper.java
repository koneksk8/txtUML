package hu.elte.txtuml.api.model.execution.impl.assoc;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.runtime.Wrapper;
import hu.elte.txtuml.utils.InstanceCreator;

public interface AssociationEndWrapper<T extends ModelClass, C extends Collection<T>>
		extends Wrapper<AssociationEnd<T, C>> {

	C getCollection();

	void add(T object) throws MultiplicityException;

	void remove(T object);

	boolean checkLowerBound();

	boolean isEmpty();

	// create methods

	abstract class Static {

		private Static() {
		}

		@SuppressWarnings("unchecked")
		public static <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> create(
				Class<? extends AssociationEnd<T, ?>> otherEnd) {
			return (AssociationEndWrapper<T, C>) create(InstanceCreator.create(otherEnd, (Object) null));
		}

		public static <T extends ModelClass, C extends Collection<T>> AssociationEndWrapper<T, C> create(
				final AssociationEnd<T, C> wrapped) {
			return new AssociationEndWrapper<T, C>() {

				private C collection = wrapped.createEmptyCollection();

				@Override
				public AssociationEnd<T, C> getWrapped() {
					return wrapped;
				}

				@Override
				public C getCollection() {
					return collection;
				}

				@Override
				@SuppressWarnings("unchecked")
				public void remove(T object) {
					collection = (C) collection.remove(object);
				}

				@Override
				@SuppressWarnings("unchecked")
				public void add(T object) throws MultiplicityException {
					if (!wrapped.checkUpperBound(collection.count() + 1)) {
						throw new MultiplicityException();
					}
					collection = (C) collection.add(object);
				}

				@Override
				public boolean checkLowerBound() {
					return getWrapped().checkLowerBound(getCollection().count());
				}
				
				@Override
				public boolean isEmpty() {
					return getCollection().isEmpty();
				}

				@Override
				public String toString() {
					return "association_end<" + collection.toString() + ">";
				}

				@Override
				public Class<?> getTypeOfWrapped() {
					return getWrapped().getClass();
				}

			};
		}

	}

}
