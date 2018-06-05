package de.uniol.inf.is.odysseus.iql.odl.types.useroperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;

public interface IODLPO<R extends IStreamObject<?>, W extends IStreamObject<?>> extends IPipe<R, W> {

}
