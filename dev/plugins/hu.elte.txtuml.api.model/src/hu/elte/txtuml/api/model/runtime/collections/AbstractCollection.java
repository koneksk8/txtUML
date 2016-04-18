package hu.elte.txtuml.api.model.runtime.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.base.Predicate;

import hu.elte.txtuml.api.model.Collection;

/**
 * An abstract immutable collection with a back-end Java collection.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of items contained in the collection
 * @param <B>
 *            the back-end java collection
 */
public abstract class AbstractCollection<T, B extends java.util.Collection<T>> implements Collection<T> {

	private final B backend;

	protected AbstractCollection(B backend) {
		this.backend = backend;
	}

	@Override
	public Iterator<T> iterator() {
		final Iterator<T> it = backend.iterator();
		return new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public T next() {
				return it.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int count() {
		return backend.size();
	}

	@Override
	public boolean contains(Object element) {
		return backend.contains(element);
	}

	@Override
	public T selectAny() throws NoSuchElementException {
		Iterator<T> it = backend.iterator();
		if (it.hasNext()) {
			return it.next();
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public Collection<T> selectAll(final Predicate<T> cond) {
		Iterator<T> it = new Iterator<T>() {
			private final Iterator<T> it = backend.iterator();
			private T next = null;

			{
				step();
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public T next() {
				T ret = next;
				step();
				return ret;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void step() {
				next = null;
				while (it.hasNext()) {
					T t = it.next();
					if (cond.apply(t)) {
						next = t;
						return;
					}
				}
			}
		};

		return createBuilder().addAll(it).build();
	}

	@Override
	public Collection<T> add(T element) {
		return createBuilder().addAll(backend).add(element).build();
	}

	@Override
	public Collection<T> addAll(Collection<T> objects) {
		return createBuilder().addAll(backend).addAll(objects).build();
	}

	@Override
	public Collection<T> remove(Object element) {
		if (element == null) {
			return this;
		}

		Builder<T, ? extends Collection<T>> builder = createBuilder();
		Iterator<T> it = backend.iterator();
		while (it.hasNext()) {
			T e = it.next();
			if (element.equals(e)) {
				return builder.addAll(it).build();
			} else {
				builder.add(e);
			}
		}
		return this;
	}

	protected abstract Builder<T, ? extends AbstractCollection<T, B>> createBuilder();

}
