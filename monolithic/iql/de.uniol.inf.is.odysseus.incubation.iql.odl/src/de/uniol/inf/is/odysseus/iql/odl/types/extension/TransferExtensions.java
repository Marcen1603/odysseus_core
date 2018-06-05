package de.uniol.inf.is.odysseus.iql.odl.types.extension;

import de.uniol.inf.is.odysseus.core.physicaloperator.ITransfer;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class TransferExtensions implements IIQLTypeExtensions {

	public static <T> void sendStreamElement(ITransfer<T> t, T element) {
		t.transfer(element);
	}
	
	public static <T> void sendStreamElement(ITransfer<T> t, T element, int sourceOutPort) {
		t.transfer(element, sourceOutPort);
	}
	
	@Override
	public Class<?> getType() {
		return ITransfer.class;
	}

}
