package hu.elte.txtuml.api.model.execution.unittests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_A;
import hu.elte.txtuml.api.model.execution.testmodel.assoc.A_B_2;
import hu.elte.txtuml.api.model.execution.util.MutableBoolean;

public class AssociationEndTests extends UnitTestsBase {

	@Test
	public void testAssociationEnds() {
		MutableBoolean exceptionThrown = new MutableBoolean(false);

		executor.run(() -> {
			try {
				init();
			} catch (AssertionError e) {
				e.printStackTrace();
				exceptionThrown.value = true;
			}
		});

		Assert.assertFalse(exceptionThrown.value);
	}

	private static void init() {
		A a1 = new A(), a2 = new A(), a3 = new A();
		B b1 = new B();

		assertCollection(new A[] {}, b1.assoc(A_B_2.a.class));

		Action.link(A_B_2.a.class, a1, A_B_2.b.class, b1);
		assertCollection(new A[] { a1 }, b1.assoc(A_B_2.a.class));

		Action.link(A_B_2.a.class, a2, A_B_2.b.class, b1);
		assertCollection(new A[] { a1, a2 }, b1.assoc(A_B_2.a.class));

		Action.link(A_B_2.a.class, a3, A_B_2.b.class, b1);
		assertCollection(new A[] { a1, a2, a3 }, b1.assoc(A_B_2.a.class));

		Action.link(A_B_2.a.class, a1, A_B_2.b.class, b1);
		assertCollection(new A[] { a1, a2, a3, a1 }, b1.assoc(A_B_2.a.class));

		Action.unlink(A_B_2.a.class, a1, A_B_2.b.class, b1);
		assertCollection(new A[] { a2, a3, a1 }, b1.assoc(A_B_2.a.class));

		Action.unlink(A_B_2.a.class, a3, A_B_2.b.class, b1);
		assertCollection(new A[] { a2, a1 }, b1.assoc(A_B_2.a.class));

		Action.unlink(A_B_2.a.class, a2, A_B_2.b.class, b1);
		assertCollection(new A[] { a1 }, b1.assoc(A_B_2.a.class));

		Action.link(A_A.a1.class, a1, A_A.a2.class, a2);
		assertCollection(new A[] {}, a1.assoc(A_A.a1.class));
		assertCollection(new A[] { a2 }, a1.assoc(A_A.a2.class));
		assertCollection(new A[] { a1 }, a2.assoc(A_A.a1.class));
		assertCollection(new A[] {}, a2.assoc(A_A.a2.class));

		Action.link(A_A.a1.class, a2, A_A.a2.class, a1);
		assertCollection(new A[] { a2 }, a1.assoc(A_A.a1.class));
		assertCollection(new A[] { a2 }, a1.assoc(A_A.a2.class));
		assertCollection(new A[] { a1 }, a2.assoc(A_A.a1.class));
		assertCollection(new A[] { a1 }, a2.assoc(A_A.a2.class));

		Action.link(A_A.a1.class, a1, A_A.a2.class, a1);
		assertCollection(new A[] { a2, a1 }, a1.assoc(A_A.a1.class));
		assertCollection(new A[] { a2, a1 }, a1.assoc(A_A.a2.class));
		assertCollection(new A[] { a1 }, a2.assoc(A_A.a1.class));
		assertCollection(new A[] { a1 }, a2.assoc(A_A.a2.class));
	}

	private static <T extends ModelClass> void assertCollection(T[] expecteds, Collection<T> collection) {
		List<T> actuals = new ArrayList<>();
		collection.forEach(actuals::add);

		Assert.assertArrayEquals(expecteds, actuals.toArray());
	}

}
