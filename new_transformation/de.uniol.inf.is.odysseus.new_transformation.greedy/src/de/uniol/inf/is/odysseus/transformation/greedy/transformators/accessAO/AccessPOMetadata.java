package de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class AccessPOMetadata {
	public static MetadataCreationPO createMetadata(ISource<?> accessPO, TransformationConfiguration config) {
		Class<?> type = MetadataRegistry.getMetadataType(config.getMetaTypes());
		MetadataCreationPO po = null;
		Collection<?> subscriptions = accessPO.getSubscriptions();
		for (Object o : subscriptions) {
			PhysicalSubscription<ISink> sub = (PhysicalSubscription<ISink>) o;
			if ((sub.getTarget() instanceof MetadataCreationPO)
					&& ((MetadataCreationPO) sub.getTarget()).getType() == type) {
				po = (MetadataCreationPO) sub.getTarget();
				break;
			}
		}
		if (po == null) {
			po = new MetadataCreationPO(type);
			po.setOutputSchema(accessPO.getOutputSchema());
			accessPO.subscribeSink(po, 0, 0, accessPO.getOutputSchema()); // TODO: Hier wird ignoriert, dass die
			// Source evtl. mehrere Ausgänge hat
		}

		return po;
	}
}
