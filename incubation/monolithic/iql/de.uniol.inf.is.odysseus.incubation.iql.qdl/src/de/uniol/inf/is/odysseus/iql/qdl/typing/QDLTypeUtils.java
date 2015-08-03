package de.uniol.inf.is.odysseus.iql.qdl.typing;

import org.eclipse.xtext.common.types.JvmType;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.AbstractIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;

public class QDLTypeUtils extends AbstractIQLTypeUtils {
	
	
	@Override
	public boolean isUserDefinedType(JvmType type, boolean array) {
		JvmType innerType = getInnerType(type, array);
		return innerType instanceof IQLClass | innerType instanceof IQLInterface | innerType instanceof QDLQuery; 
	}
	

}
