package hu.elte.txtuml.api.model.assocends;

/**
 * Association ends are bounded: they have a lower bound and an upper bound.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Bounds {

	/**
	 * Returns the lower bound of this multiplicity.
	 * 
	 * @return a non-negative integer that is the lower bound of this
	 *         multiplicity
	 */
	int lowerBound();

	/**
	 * Returns the upper bound of this multiplicity, -1 means no upper bound.
	 * 
	 * @return a non-negative integer that is the upper bound of this
	 *         multiplicity or -1 if there is no upper bound
	 */
	int upperBound();

	/**
	 * Checks whether the given actual size conforms to the lower bound.
	 */
	boolean checkLowerBound(int actualSize);

	/**
	 * Checks whether the given actual size conforms to the upper bound.
	 */
	boolean checkUpperBound(int actualSize);

}
