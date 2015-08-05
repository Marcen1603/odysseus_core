package de.uniol.inf.is.odysseus.recovery.incomingelements.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
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
@LogicalOperator(name = "SOURCESYNC", minInputPorts = 1, maxInputPorts = 1, doc = "Should not be placed manually.", category = {
		LogicalOperatorCategory.ADVANCED })
public class SourceSyncAO extends UnaryLogicalOp {

	/**
	 * The version of this class for seruialization.
	 */
	private static final long serialVersionUID = -9156948424518889385L;

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
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SourceSyncAO(this);
	}

	@GetParameter(name = "BaDaStRecorder")
	public String getBaDaStRecorder() {
		return this.mRecorder;
	}

	@Parameter(type = StringParameter.class, name = "BaDaStRecorder", optional = false)
	public void setBaDaStRecorder(String name) {
		this.mRecorder = name;
	}

}