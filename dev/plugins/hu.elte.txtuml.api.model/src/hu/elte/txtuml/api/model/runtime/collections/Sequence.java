package hu.elte.txtuml.api.model.runtime.collections;

import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import hu.elte.txtuml.api.model.Collection;

/**
 * An immutable ordered collection which may contain any number of elements
 * (including zero).
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of the contained elements
 */
public class Sequence<T> extends AbstractCollection<T, ArrayList<T>> implements Collection<T> {

	static <T> Builder<T, Sequence<T>> builder() {
		return Builder.create(new Supplier<ArrayList<T>>() {
			@Override
			public ArrayList<T> get() {
				return new ArrayList<>();
			}
		}, new Function<ArrayList<T>, Sequence<T>>() {
			@Override
			public Sequence<T> apply(ArrayList<T> input) {
				return new Sequence<T>(input);
			}
		});
	}

	/**
	 * Creates an empty {@code Sequence}.
	 */
	public static <T> Sequence<T> empty() {
		return new Sequence<>();
	}

	/**
	 * Creates a {@code Sequence} which will contain the elements of the given
	 * collection.
	 */
	public static <T> Sequence<T> of(Collection<T> elements) {
		return Sequence.<T> builder().addAll(elements).build();
	}

	private Sequence() {
		super(new ArrayList<T>());
	}

	private Sequence(ArrayList<T> backend) {
		super(backend);
	}

	@Override
	public boolean isEmpty() {
		return count() == 0;
	}

	@Override
	protected Builder<T, Sequence<T>> createBuilder() {
		return builder();
	}

}
