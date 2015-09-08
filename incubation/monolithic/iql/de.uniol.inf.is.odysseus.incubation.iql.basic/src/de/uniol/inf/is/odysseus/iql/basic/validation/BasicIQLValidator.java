package de.uniol.inf.is.odysseus.iql.basic.validation;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.validation.Check;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod;
import de.uniol.inf.is.odysseus.iql.basic.linking.IIQLMethodFinder;


public class BasicIQLValidator extends AbstractBasicIQLValidator {
	

	public static String DUPLICATE_ATTRIBUTES = "duplicateAttributes";
	public static String DUPLICATE_METHOD= "duplicateMethods";

	@Inject
	private IIQLMethodFinder methodFinder;
	
	@Check
	void checkDuplicateAttributes(JvmGenericType type) {
		Set<String> names = new HashSet<>();
		for (IQLAttribute attr : EcoreUtil2.getAllContentsOfType(type, IQLAttribute.class)) {
			if (names.contains(attr.getSimpleName())) {
				error("Duplicate attribute "+attr.getSimpleName(), attr, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_ATTRIBUTES);
			} else {
				names.add(attr.getSimpleName());
			}
		}
	}
	
	@Check
	void checkDuplicateMethod(JvmGenericType type) {
		Set<String> names = new HashSet<>();
		for (IQLMethod meth : EcoreUtil2.getAllContentsOfType(type, IQLMethod.class)) {			 
			String name = methodFinder.createExecutableID(meth);
			if (names.contains(name)) {
				if (meth.getSimpleName() != null) {
					error("Duplicate method "+meth.getSimpleName(), meth, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_METHOD);
				} else {
					error("Duplicate method", meth, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_METHOD);
				}
			} else {
				names.add(name);
			}
		}
	}
}
