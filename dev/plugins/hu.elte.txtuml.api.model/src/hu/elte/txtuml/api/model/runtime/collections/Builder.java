package hu.elte.txtuml.api.model.runtime.collections;

import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import hu.elte.txtuml.api.model.Collection;

/**
 * A mutable builder for an immutable collection.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of items contained in the collection
 * @param <C>
 *            the type of the immutable collection
 */
abstract class Builder<T, C extends Collection<T>> {

	abstract Builder<T, C> add(T element);

	Builder<T, C> addAll(Iterable<? extends T> elements) {
		for (T e : elements) {
			add(e);
		}
		return this;
	}

	Builder<T, C> addAll(Iterator<? extends T> it) {
		while (it.hasNext()) {
			this.add(it.next());
		}
		return this;
	}

	/**
	 * May only be called <b>once</b> to create the prepared immutable
	 * collection.
	 */
	abstract C build();

	// create method

	static <T, C extends Collection<T>, B extends java.util.Collection<T>> Builder<T, C> create(
			final Supplier<B> backendCollectionCreator, final Function<B, C> immutableCollectionCreator) {
		return new Builder<T, C>() {
			private B backendCollection = backendCollectionCreator.get();

			@Override
			public Builder<T, C> add(T element) {
				backendCollection.add(element);
				return this;
			}

			@Override
			public C build() {
				C ret = immutableCollectionCreator.apply(backendCollection);
				backendCollection = null;
				return ret;
			}
		};
	}

}
