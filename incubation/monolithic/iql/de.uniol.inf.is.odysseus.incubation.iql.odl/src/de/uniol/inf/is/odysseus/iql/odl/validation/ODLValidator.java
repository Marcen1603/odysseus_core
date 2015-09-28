package de.uniol.inf.is.odysseus.iql.odl.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public class ODLValidator extends AbstractODLValidator{

	public static String DUPLICATE_OPERATORS = "duplicateOperators";

	@Check(CheckType.FAST)
	void checkDuplicateOperators(IQLModel model) {
		Set<String> names = new HashSet<>();
		for (ODLOperator op : EcoreUtil2.getAllContentsOfType(model, ODLOperator.class)) {
			if (names.contains(op.getSimpleName())) {
				error("Duplicate operator "+op.getSimpleName(), op, TypesPackage.eINSTANCE.getJvmMember_SimpleName() ,DUPLICATE_OPERATORS);
				
			} else {
				names.add(op.getSimpleName());
			}
		}
	}
}
