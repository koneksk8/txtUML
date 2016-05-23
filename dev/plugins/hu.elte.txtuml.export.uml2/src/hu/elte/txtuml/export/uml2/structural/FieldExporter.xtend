package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.Exporter
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.NamedElement
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.TypedElement

abstract class TypedNamedExporter<T extends TypedElement & NamedElement> extends Exporter<IVariableBinding, IVariableBinding, T> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	override exportContents(IVariableBinding binding) {
		result.name = binding.name
		result.type = fetchType(binding.type)
	}
}

class FieldExporter extends TypedNamedExporter<Property> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IVariableBinding source) {
		if(source.isField) factory.createProperty
	}
}
