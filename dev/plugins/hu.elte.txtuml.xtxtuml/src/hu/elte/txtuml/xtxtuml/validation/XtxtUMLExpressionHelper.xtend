package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.XLinkExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.XLogExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.XStartExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.util.XExpressionHelper

class XtxtUMLExpressionHelper extends XExpressionHelper {

	public override hasSideEffects(XExpression expr) {
		switch (expr) {
			RAlfDeleteObjectExpression,
			RAlfSendSignalExpression,
			XStartExpression,
			XLogExpression,
			XLinkExpression:
				true
			RAlfSignalAccessExpression,
			TUClassPropertyAccessExpression:
				false
			default:
				super.hasSideEffects(expr)
		}
	}

}
