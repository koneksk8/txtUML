package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.ContainmentKind;
import hu.elte.txtuml.api.model.assocends.Multiplicity;
import hu.elte.txtuml.api.model.assocends.Navigability;

/**
 * A base class for associations in the model.
 * 
 * <p>
 * <b>Represents:</b> association
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * An association in the model is a subclass of <code>Association</code>, having
 * two inner classes which both extend {@link AssociationEnd}. These two inner
 * classes will represent the two ends of this association. Their navigability
 * and multiplicity depend on which predefined subclass of
 * <code>AssociationEnd</code> is extended ({@code AssociationEnd} itself may
 * not be extended).
 * <p>
 * The two model classes which the association connects are defined by the two
 * association ends' generic parameters.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * <li>must have two inner classes which are subclasses of
 * <code>AssociationEnd</code></li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> allowed at most two, both of which are non-static
 * and are subclasses of <code>AssociationEnd</code></li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class SampleAssociation extends Association {
 * 	class SampleEnd1 extends {@literal Many<SampleClass1>} {}
 * 	class SampleEnd2 extends {@literal HiddenOne<SampleClass2>} {}
 * }
 * </code>
 * </pre>
 * 
 * @see Composition
 * @see Association.Many
 * @see Association.One
 * @see Association.MaybeOne
 * @see Association.Some
 * @see Association.Multiple
 * @see Association.HiddenMany
 * @see Association.HiddenOne
 * @see Association.HiddenMaybeOne
 * @see Association.HiddenSome
 * @see Association.HiddenMultiple
 */
public abstract class Association {

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 0..*.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 0..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public class Many<T extends ModelClass> extends ManyEnd<T>
			implements Navigability.Navigable, Multiplicity.ZeroToUnlimited, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 0;
		}

		@Override
		public int upperBound() {
			return -1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 1..*.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 1..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class Some<T extends ModelClass> extends ManyEnd<T>
			implements Navigability.Navigable, Multiplicity.OneToUnlimited, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 1;
		}

		@Override
		public int upperBound() {
			return -1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 0..1.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 0..1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class MaybeOne<T extends ModelClass> extends MaybeEnd<T>
			implements Navigability.Navigable, Multiplicity.ZeroToOne, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 0;
		}

		@Override
		public int upperBound() {
			return 1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a multiplicity of 1.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a multiplicity of 1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class One<T extends ModelClass> extends MaybeEnd<T>
			implements Navigability.Navigable, Multiplicity.One, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 1;
		}

		@Override
		public int upperBound() {
			return 1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a navigable
	 * association end with a user-defined multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> navigable association end with a user-defined
	 * multiplicity
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * For general information about association ends, see the documentation of
	 * {@link AssociationEnd}.
	 * <p>
	 * A class that extends <code>Multiple</code> (or its non-navigable
	 * counterpart, {@link HiddenMultiple}), represents an association end that
	 * may have a custom, user-specified multiplicity by applying the
	 * {@link Min} and/or {@link Max} annotations on the class. <code>Min</code>
	 * sets the lower, <code>Max</code> the upper bound of the multiplicity. If
	 * one of the annotations is not present, that means that there is no lower
	 * and/or upper bound. Therefore, an omitted <code>Min</code> equals to an
	 * explicitly specified lower bound of zero, whereas a missing
	 * <code>Max</code> shows that any number of object might be present at the
	 * association end (if their count satisfies the lower bound).
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class Multiple<T extends ModelClass> extends MultipleEnd<T>
			implements Navigability.Navigable, Multiplicity.MinToMax, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			Min min = getClass().getAnnotation(Min.class);
			return min == null ? 0 : min.value();
		}

		@Override
		public int upperBound() {
			Max max = getClass().getAnnotation(Max.class);
			return max == null ? -1 : max.value();
		}
	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 0..*.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 0..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenMany<T extends ModelClass> extends ManyEnd<T>
			implements Navigability.NonNavigable, Multiplicity.ZeroToUnlimited, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 0;
		}

		@Override
		public int upperBound() {
			return -1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 1..*.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 1..*
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenSome<T extends ModelClass> extends ManyEnd<T>
			implements Navigability.NonNavigable, Multiplicity.OneToUnlimited, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 1;
		}

		@Override
		public int upperBound() {
			return -1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 0..1.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of
	 * 0..1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenMaybeOne<T extends ModelClass> extends MaybeEnd<T>
			implements Navigability.NonNavigable, Multiplicity.ZeroToOne, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 0;
		}

		@Override
		public int upperBound() {
			return 1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a multiplicity of 1.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a multiplicity of 1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenOne<T extends ModelClass> extends MaybeEnd<T>
			implements Navigability.NonNavigable, Multiplicity.One, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			return 1;
		}

		@Override
		public int upperBound() {
			return 1;
		}

	}

	/**
	 * An immutable collection which contains the elements of a non-navigable
	 * association end with a user-defined multiplicity.
	 * 
	 * <p>
	 * <b>Represents:</b> non-navigable association end with a user-defined
	 * multiplicity.
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link Multiple} and {@link AssociationEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of an association class (a subclass of
	 * {@link Association})</li>
	 * </ul>
	 * <p>
	 * <b>Subtype restrictions:</b>
	 * <ul>
	 * <li><i>Be abstract:</i> disallowed</li>
	 * <li><i>Generic parameters:</i> disallowed</li>
	 * <li><i>Constructors:</i> disallowed</li>
	 * <li><i>Initialization blocks:</i> disallowed</li>
	 * <li><i>Fields:</i> disallowed</li>
	 * <li><i>Methods:</i> disallowed</li>
	 * <li><i>Nested interfaces:</i> disallowed</li>
	 * <li><i>Nested classes:</i> disallowed</li>
	 * <li><i>Nested enums:</i> disallowed</li>
	 * </ul>
	 * </li>
	 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 */
	public abstract class HiddenMultiple<T extends ModelClass> extends MultipleEnd<T>
			implements Navigability.NonNavigable, Multiplicity.MinToMax, ContainmentKind.SimpleEnd {

		@Override
		public int lowerBound() {
			Min min = getClass().getAnnotation(Min.class);
			return min == null ? 0 : min.value();
		}

		@Override
		public int upperBound() {
			Max max = getClass().getAnnotation(Max.class);
			return max == null ? -1 : max.value();
		}

	}
}
