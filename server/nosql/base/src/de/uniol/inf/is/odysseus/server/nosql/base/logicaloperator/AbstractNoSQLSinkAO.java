package de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNoSQLSinkAO extends AbstractNoSQLAO {

	private static final long serialVersionUID = 42770418904359282L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractNoSQLSinkAO.class);

	public static final int DEFAULT_BATCHSIZE = 10;
	public static final int DEFAULT_BATCHTIMEOUT = 5000;
	protected int batchSize = DEFAULT_BATCHSIZE;
	protected int batchTimeout = DEFAULT_BATCHTIMEOUT;

	protected AbstractNoSQLSinkAO(AbstractNoSQLSinkAO old) {
		super(old);

		this.batchSize = old.batchSize;
		this.batchTimeout = old.batchTimeout;
	}

	protected AbstractNoSQLSinkAO() {
		super();
	}

	@Parameter(name = "BATCHSIZE", type = IntegerParameter.class, optional = true, doc = "Max size of the batch")
	public void setBatchSize(int batchSize) {
		if(batchSize <= 0){
			LOG.warn("Given batchsize was {}. Using default batchsize of {} instead", batchSize, DEFAULT_BATCHSIZE);
			batchSize = DEFAULT_BATCHSIZE;
		}
		this.batchSize = batchSize;
	}

	@Parameter(name = "BATCHTIMEOUT", type = IntegerParameter.class, optional = true, doc = "Max expired time in ms before flushing queue")
	public void setBatchTimeout(int batchTimeout) {
		if(batchTimeout <= 0){
			LOG.warn("Given batchTimeout was {}ms. Using default batchTimeout of {}ms instead", batchTimeout, DEFAULT_BATCHTIMEOUT);
			batchTimeout = DEFAULT_BATCHTIMEOUT;
		}
		this.batchTimeout = batchTimeout;
	}

	public int getBatchTimeout() {
		return batchTimeout;
	}

	public int getBatchSize() {
		return batchSize;
	}
}
