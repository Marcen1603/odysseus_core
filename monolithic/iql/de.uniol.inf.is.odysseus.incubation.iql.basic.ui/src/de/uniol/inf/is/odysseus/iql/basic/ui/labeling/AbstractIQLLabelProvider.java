package de.uniol.inf.is.odysseus.iql.basic.ui.labeling;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmVisibility;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration;


public abstract class AbstractIQLLabelProvider extends org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider {

	public AbstractIQLLabelProvider(org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	// Labels and icons can be computed like this:
	
//	String text(Greeting ele) {
//		return "A greeting to " + ele.getName();
//	}
//
	String image(JvmGenericType ele) {
		if (ele.isInterface()) {
			return "java_interface.gif";
		} else {
			return "java_file.gif";
		}
	}
	
	
	String image(IQLClass ele) {
		return "java_file.gif";
	}
	
	String image(IQLInterface ele) {
		return "java_interface.gif";
	}
	
	String image(IQLMethodDeclaration ele) {
		return "methpub_obj.gif";
	}
	
	String image(JvmField ele) {
		if (ele.getVisibility().getValue() == JvmVisibility.PUBLIC_VALUE) {
			return "field_public_obj.gif";
		} else if (ele.getVisibility().getValue() == JvmVisibility.PRIVATE_VALUE) {
			return "field_private_obj.gif";
		} else {
			return "field_protected_obj.gif";
		}
	}
	
	String image(JvmOperation ele) {
		if (ele.getVisibility().getValue() == JvmVisibility.PUBLIC_VALUE) {
			return "methpub_obj.gif";
		} else if (ele.getVisibility().getValue() == JvmVisibility.PRIVATE_VALUE) {
			return "methpri_obj.gif";
		} else {
			return "methpro_obj.gif";
		}
	}
	
	String image(IQLMethod ele) {
		return "methpub_obj.gif";
	}
	
	String image(IQLAttribute ele) {
		return "field_public_obj.gif";
	}
	
	String image(IQLJavaMember ele) {
		return "Java-icon.png";
	}
}
