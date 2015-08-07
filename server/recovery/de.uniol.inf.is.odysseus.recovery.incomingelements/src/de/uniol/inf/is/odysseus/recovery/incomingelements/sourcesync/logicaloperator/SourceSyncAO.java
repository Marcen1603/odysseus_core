package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * Logical operator to be placed directly after source access operators. <br />
 * <br />
 * In the phase of backup, it controls a BaDaSt recorder by starting and closing
 * it. <br />
 * <br />
 * In the phase of recovery, it acts as a BaDaSt consumer and pushes data stream
 * elements from there into the DSMS. Elements from the original source will be
 * discarded in this time. But they are not lost, since this operator is also
 * always in backup mode.
 * 
 * @author Michael Brand
 *
 */
@LogicalOperator(name = "SOURCESYNC", minInputPorts = 1, maxInputPorts = 1, doc = "Should not be placed manually.", category = { LogicalOperatorCategory.ADVANCED })
public class SourceSyncAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -9156948424518889385L;

	/**
	 * The access to the source to synchronize.
	 */
	private AccessAO mSource;

	/**
	 * The name of the BaDaSt recorder to use.
	 */
	private String mRecorder;

	/**
	 * Creates a new {@link SourceSyncAO}.
	 */
	public SourceSyncAO() {
		super();
	}

	/**
	 * Creates a new {@link SourceSyncAO} as a copy of an existing one.
	 * 
	 * @param other
	 *            The {@link SourceSyncAO} to copy.
	 */
	public SourceSyncAO(SourceSyncAO other) {
		super(other);
		this.mSource = other.mSource;
		this.mRecorder = other.mRecorder;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SourceSyncAO(this);
	}

	/**
	 * Gets the BaDaSt recorder to use.
	 * 
	 * @return The name of the BaDaSt recorder to use.
	 */
	@GetParameter(name = "BaDaStRecorder")
	public String getBaDaStRecorder() {
		return this.mRecorder;
	}

	/**
	 * Sets the BaDaSt recorder to use.
	 * 
	 * @param name
	 *            The name of the BaDaSt recorder to use.
	 */
	@Parameter(type = StringParameter.class, name = "BaDaStRecorder", optional = false)
	public void setBaDaStRecorder(String name) {
		this.mRecorder = name;
	}

	/**
	 * Gets the source to synchronize.
	 * 
	 * @return The access to the source to synchronize.
	 */
	@GetParameter(name = "Source")
	public AccessAO getSource() {
		return this.mSource;
	}

	/**
	 * Sets the source to synchronize.
	 * 
	 * @param name
	 *            The access to the source to synchronize.
	 */
	@Parameter(type = SourceParameter.class, name = "Source", optional = false)
	public void setSource(AccessAO access) {
		this.mSource = access;
	}

}