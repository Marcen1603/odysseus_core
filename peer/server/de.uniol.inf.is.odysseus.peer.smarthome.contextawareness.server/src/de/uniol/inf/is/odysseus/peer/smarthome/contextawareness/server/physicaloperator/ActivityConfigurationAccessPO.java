package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.physicaloperator;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.logicaloperator.ActivityConfigurationAccessAO;

public class ActivityConfigurationAccessPO extends AbstractSource<Tuple<?>> {
	private static org.slf4j.Logger LOG = LoggerFactory.getLogger(ActivityConfigurationAccessPO.class);
	private String entityName;
	private String activityName;

	public ActivityConfigurationAccessPO(ActivityConfigurationAccessAO operator) {
		this.entityName = operator.getEntityName();
		this.activityName = operator.getActivityName();
	}

	public ActivityConfigurationAccessPO(
			ActivityConfigurationAccessPO other) {
		this.entityName = other.entityName;
		this.activityName = other.activityName;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		LOG.debug("process_open");
		
	}

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new ActivityConfigurationAccessPO(this);
	}
}
