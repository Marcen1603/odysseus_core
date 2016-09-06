package de.uniol.inf.is.odysseus.iql.basic.ui.outline;

import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;

public class AbstractIQLOutlineTreeProvider extends org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider {
	
	protected void _createChildren(IOutlineNode parentNode, IQLModel model) {
		for (IQLModelElement element : model.getElements()) {
			createOutlineNode(parentNode, element.getInner());
		}
	}
	
	
	protected void _createChildren(IOutlineNode parentNode, IQLClass clazz) {
		for (JvmMember member : clazz.getMembers()) {
			createOutlineNode(parentNode, member);
		}
	}
	
	protected void _createChildren(IOutlineNode parentNode, IQLInterface interf) {
		for (JvmMember member : interf.getMembers()) {
			createOutlineNode(parentNode, member);
		}
	}
	
	protected void createOutlineNode(IOutlineNode parentNode, JvmMember member) {
		if (member instanceof IQLAttribute) {
			createOutlineNode(parentNode,  (IQLAttribute)member);
		} else if (member instanceof IQLMethod) {
			createOutlineNode(parentNode,  (IQLMethod)member);
		} else if (member instanceof IQLMethodDeclaration) {
			createOutlineNode(parentNode,  (IQLMethodDeclaration)member);
		} else if (member instanceof IQLJavaMember) {
			createOutlineNode(parentNode,  (IQLJavaMember)member);
		} else if (member instanceof IQLClass) {
			createOutlineNode(parentNode,  (IQLClass)member);
		} else if (member instanceof IQLInterface) {
			createOutlineNode(parentNode,  (IQLInterface)member);
		} 
	}
	
	protected void createOutlineNode(IOutlineNode parentNode, IQLClass clazz) {
		boolean isLeaf = clazz.getMembers().size() == 0;
		createEObjectNode(parentNode, clazz, _image(clazz), clazz.getSimpleName(), isLeaf);
	}
	
	protected void createOutlineNode(IOutlineNode parentNode, IQLInterface interf) {
		boolean isLeaf = interf.getMembers().size() == 0;
		createEObjectNode(parentNode, interf, _image(interf), interf.getSimpleName(), isLeaf);
	}
	
	protected void createOutlineNode(IOutlineNode parentNode, IQLAttribute attribute) {
		createEObjectNode(parentNode, attribute, _image(attribute), attribute.getSimpleName(), true);
	}

	
	protected void createOutlineNode(IOutlineNode parentNode, IQLMethod method) {
		createEObjectNode(parentNode, method, _image(method), method.getSimpleName(), true);
	}

	protected void createOutlineNode(IOutlineNode parentNode, IQLMethodDeclaration methodDeclaration) {
		createEObjectNode(parentNode, methodDeclaration, _image(methodDeclaration), methodDeclaration.getSimpleName(), true);
	}	

	protected void createOutlineNode(IOutlineNode parentNode, IQLJavaMember javaMember) {
		createEObjectNode(parentNode, javaMember, _image(javaMember), "java", true);
	}
	
}
