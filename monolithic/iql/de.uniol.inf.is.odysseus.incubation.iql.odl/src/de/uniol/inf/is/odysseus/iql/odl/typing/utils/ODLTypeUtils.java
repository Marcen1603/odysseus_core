package de.uniol.inf.is.odysseus.iql.odl.typing.utils;


import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;




import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.AbstractIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;

public class ODLTypeUtils extends AbstractIQLTypeUtils implements IODLTypeUtils {
	
		
	@Override
	public boolean isUserDefinedType(JvmType type, boolean array) {
		JvmType innerType = getInnerType(type, array);
		if (innerType instanceof JvmGenericType) {
			JvmGenericType genericType = (JvmGenericType) innerType;
			if (genericType.getPackageName() == null) {
				return innerType instanceof IQLClass | innerType instanceof IQLInterface | innerType instanceof ODLOperator; 
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
