package hu.elte.txtuml.export.cpp;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

public class Shared {
	public static List<Property> getProperties(Class class_) {
		List<Property> properties = new LinkedList<Property>();
		for (Association assoc : class_.getAssociations()) {
			for (Property prop : assoc.getMemberEnds()) {
				if (!prop.getType().equals(class_)) {
					properties.add(prop);
				}
			}
		}
		properties.addAll(class_.getOwnedAttributes());
		return properties;
	}

	public static String getGuard(Constraint guard_) {
		String source = "";
		if (guard_.eClass().equals(UMLPackage.Literals.DURATION_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.TIME_CONSTRAINT)) {
			// TODO
		} else if (guard_.eClass().equals(UMLPackage.Literals.CONSTRAINT)) {

			// source=getGuardFromValueSpecification(guard_.getSpecification());
			source = GenerationTemplates.getDefaultReturnValue("Boolean");
		}
		return source;
	}

	// TODO we need a more complex ocl parse....
	public static String getGuardFromValueSpecification(ValueSpecification guard_) {
		String source = "";
		if (guard_ != null) {
			if (guard_.eClass().equals(UMLPackage.Literals.LITERAL_STRING)) {
				source = ((LiteralString) guard_).getValue();
				if (source.toLowerCase().equals("else")) {
					source = "";
				}
			} else if (guard_.eClass().equals(UMLPackage.Literals.OPAQUE_EXPRESSION)) {
				source = parseOCL(((OpaqueExpression) guard_).getBodies().get(0));
			} else {
				source = "UNKNOWN_GUARD_TYPE";
			}
		}
		return source;
	}

	@SuppressWarnings("unchecked")
	public static <ElementTypeT, EClassTypeT> void getTypedElements(Collection<ElementTypeT> dest_,
			Collection<Element> source_, EClassTypeT eClass_) {
		for (Element item : source_) {
			if (item.eClass().equals(eClass_)) {
				dest_.add((ElementTypeT) item);
			}
		}
	}

	// TODO need a better solution
	public static boolean isBasicType(String typeName_) {

		if (typeName_.equals("Integer") || typeName_.equals("Real") || typeName_.equals("Boolean")) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean isGeneratedClass(Class item) {
			return item.getName().startsWith("#");		
	}

	public static void writeOutSource(String path_, String fileName_, String source_)
			throws FileNotFoundException, UnsupportedEncodingException {
		try {
			File file = new File(path_);
			if (!file.exists()) {
				file.mkdirs();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(path_ + File.separator + fileName_, "UTF-8");
		writer.println(source_);
		writer.close();
	}

	// TODO just basic pars, need a much better
	public static String parseOCL(String ocl_) {
		String source = ocl_.replaceAll("\n", "");
		if (!source.equals("self")) {
			source = source.replaceAll("self.", "(*" + ActivityTemplates.Self + ").");
			source = source.replace("self", ActivityTemplates.Self);
			source = source.replaceAll("->", ActivityTemplates.AccessOperatorForSets);
		} else {
			source = ActivityTemplates.Self;
		}

		if (source.contains("first")) {
			source = source.replaceAll("first", ActivityTemplates.Operators.First);
		}
		if (source.contains("last")) {
			source = source.replaceAll("last", ActivityTemplates.Operators.Last);
		}
		if (source.contains("not")) {
			source = source.replaceAll("^[a-zA-Z]not", ActivityTemplates.Operators.Not);
		}
		if (source.contains("=")) {
			source = source.replaceAll("[^<>]=", ActivityTemplates.Operators.Equal);
		}
		if (source.contains("<>")) {
			source = source.replaceAll("<>", ActivityTemplates.Operators.NotEqual);
		}
		if (source.contains("excluding")) {
			source = source.replaceAll("excluding", ActivityTemplates.Operators.Remove);
		}
		return source;
	}

	public static String calculateSmElseGuard(Transition elseTransition_) {
		String source = "";
		Pseudostate choice = (Pseudostate) elseTransition_.getSource();
		for (Transition transition : choice.getOutgoings()) {
			if (!transition.equals(elseTransition_)) {
				if (!source.isEmpty()) {
					source += " " + ActivityTemplates.Operators.And + " ";
				}
				source += ActivityTemplates.Operators.Not + "(" + Shared.getGuard(transition.getGuard()) + ")";
			}
		}
		return source;
	}

}
