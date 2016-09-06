/*
* generated by Xtext
*/
package de.uniol.inf.is.odysseus.iql.odl.ui.outline;

import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;

import de.uniol.inf.is.odysseus.iql.basic.ui.outline.AbstractIQLOutlineTreeProvider;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter;


/**
 * Customization of the default outline structure.
 *
 * see http://www.eclipse.org/Xtext/documentation.html#outline
 */
public class ODLOutlineTreeProvider extends AbstractIQLOutlineTreeProvider {
	
		
	protected void _createChildren(IOutlineNode parentNode, ODLOperator operator) {
		for (JvmMember member : operator.getMembers()) {
			createOutlineNode(parentNode, member);
		}
	}
	
	@Override
	protected void createOutlineNode(IOutlineNode parentNode, JvmMember member) {
		if (member instanceof ODLParameter) {
			createOutlineNode(parentNode,  (ODLParameter)member);
		} else if (member instanceof ODLMethod) {
			createOutlineNode(parentNode,  (ODLMethod)member);
		} else if (member instanceof ODLOperator) {
			createOutlineNode(parentNode,  (ODLOperator)member);
		} else  {
			super.createOutlineNode(parentNode, member);
		}
	}
	protected void createOutlineNode(IOutlineNode parentNode, ODLOperator operator) {
		boolean isLeaf = operator.getMembers().size() == 0;
		createEObjectNode(parentNode, operator, _image(operator), operator.getSimpleName(), isLeaf);
	}
		
	protected void createOutlineNode(IOutlineNode parentNode, ODLParameter parameter) {
		createEObjectNode(parentNode, parameter, _image(parameter), parameter.getSimpleName(), true);
	}
	

	
	protected void createOutlineNode(IOutlineNode parentNode, ODLMethod method) {
		String name = method.getSimpleName();
		if (name == null) {
			name = "validate";
		}
		createEObjectNode(parentNode, method, _image(method), method.getSimpleName(), true);
	}
}
