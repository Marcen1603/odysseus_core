package de.uniol.inf.is.odysseus.iql.odl.ui.typing;

import org.eclipse.xtext.common.types.JvmGenericType;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.ODLTypeUtils;

public class ODLUiTypeUtils extends ODLTypeUtils {

	@Override
	protected String getLongNameOfUserDefinedType(JvmGenericType type) {
		String packageName = BasicIQLUiTypeUtils.getPackage(type);
		if (packageName != null && packageName.length() > 0) {
			return packageName+IQLQualifiedNameConverter.DELIMITER+type.getSimpleName();
		} else {
			return type.getIdentifier();
		}
	}
}
