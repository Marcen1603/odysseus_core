package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;


abstract public class AbstractEnrichAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8958867427903122928L;

	/*
	 * Caching-Parameters
	 */

	/**
	 * Parameter for caching functionality
	 */
	private boolean caching = false;

	/**
	 * Parameter for the removal strategy
	 */
	private String removalStrategy = "FIFO";

	/**
	 * The expiration time of cache entrys
	 */
	private long expirationTime = 1000 * 60 * 5; // 5 minutes

	/**
	 * The number of tuples a cache can maximal hold
	 */
	private int cacheSize = 20;
	
	/**
	 * The position of the unique key in the input stream
	 */
	private List<Integer> uniqueKeys = null;

	
	/**
	 * If false, tuples with a null-response will be filtered (they will not
	 * appear in the output True, tuples with a null-response will appear in the
	 * output
	 */
	private boolean outerJoin = false;
	
	/**
	 * Enables multi tuple output
	 */
	private boolean multiTupleOutput = false;

	public AbstractEnrichAO() {
		super();
	}

	public AbstractEnrichAO(AbstractEnrichAO enrichAO) {
		super(enrichAO);
		this.outerJoin = enrichAO.outerJoin;
		this.multiTupleOutput = enrichAO.multiTupleOutput;
		this.caching = enrichAO.caching;
		this.removalStrategy = enrichAO.removalStrategy;
		this.expirationTime = enrichAO.expirationTime;
		this.cacheSize = enrichAO.cacheSize;
		this.uniqueKeys = enrichAO.uniqueKeys;
	}
	
	/**
	 * Setter for filtering Null Tuples. If true, Tuples with a null-response
	 * will not appear in the output stream, If false, Tuples with a
	 * null-response will appear in the output stream with "null"
	 * 
	 * @param filterNullTuples
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "outerJoin")
	public void setOuterJoin(boolean outerJoin) {
		this.outerJoin = outerJoin;
	}

	/**
	 * @return Null Tuples will be filtered or not
	 */
	public boolean getOuterJoin() {
		return this.outerJoin;
	}

	/**
	 * @return mulit tuple output
	 */
	public boolean getMultiTupleOutput() {
		return this.multiTupleOutput;
	}

	/**
	 * Setter to enable or disable multi tuple output
	 * 
	 * @param multiTupleOutput
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "multiTupleOutput")
	public void setMultiTupleOutput(boolean multiTupleOutput) {
		this.multiTupleOutput = multiTupleOutput;
	}
	
	/**
	 * @return true if caching is enable, false then
	 */
	public boolean getCache() {
		return this.caching;
	}

	/**
	 * Setter to enable or disable caching
	 * 
	 * @param cache
	 */
	@Parameter(type = BooleanParameter.class, optional = true, name = "caching")
	public void setCache(boolean cache) {
		this.caching = cache;
	}

	/**
	 * @return the removal strategy of the cache
	 */
	public String getRemovalStrategy() {
		return this.removalStrategy;
	}

	/**
	 * Setter for the removal strategy
	 * 
	 * @param removalStrategy
	 */
	@Parameter(type = StringParameter.class, optional = true, name = "removalStrategy")
	public void setRemovalStrategy(String removalStrategy) {
		this.removalStrategy = removalStrategy;
	}

	/**
	 * @return the expiration time of cache entrys
	 */
	public long getExpirationTime() {
		return this.expirationTime;
	}

	/**
	 * Setter for the expiration time. One minute is 1000 * 60
	 * 
	 * @param expirationTime
	 */
	@Parameter(type = LongParameter.class, optional = true, name = "expirationTime")
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * @return the max number of tuples the cache can hold
	 */
	public int getCacheSize() {
		return this.cacheSize;
	}

	/**
	 * Setter for the max number of tuples a cache can hold
	 * @param cacheSize
	 */
	@Parameter(type = IntegerParameter.class, optional = true, name = "cacheSize")
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
	
	/**
	 * @return the unique key attributes of the input stream
	 */
	public List<Integer> getUniqueKeysAsList() {
		return this.uniqueKeys;
	}
	
	/**
	 * @return the unique key attributes as a int-Array
	 */
	public int[] getUniqueKeysAsArray() {
		if(this.uniqueKeys == null) {
			return null;
		} else {
			int[] keys = new int[this.uniqueKeys.size()];
			for(int i = 0; i < this.uniqueKeys.size(); i++) {
				keys[i] = this.uniqueKeys.get(i);
			}
			return keys;
		}
	}
	
	/**
	 * Setter for the unique keys of the input stream
	 * @param keys the keys
	 */
	@Parameter(type = IntegerParameter.class, name = "uniqueKeys", isList = true, optional = true)
	public void setUniqueKey(List<Integer> keys) {
		this.uniqueKeys = keys;
	}
	
	@Override
	public boolean isValid() {
		boolean valid = true;
		
		if (getExpirationTime() < 0) {
			addError("ExpirationTime must be > 0");
			valid = false;
		}
		if (getCacheSize() <= 0) {
			addError("cacheSize must be > 0");
			valid = false;
		}
		
		return valid;
	}
	
}
