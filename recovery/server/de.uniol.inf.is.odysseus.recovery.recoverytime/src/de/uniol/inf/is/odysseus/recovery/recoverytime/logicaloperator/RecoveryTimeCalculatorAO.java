package de.uniol.inf.is.odysseus.recovery.recoverytime.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;

/**
 * Uses the {@link IRecoveryTime} meta attribute to calculate the recovery time.
 * <br />
 * <br />
 * Recovery time is defined as the time between the first element after a
 * restart and the first trustworthy element. Recovery time is measured in
 * system time as well as in application time.
 * 
 * @author Michael Brand
 */
@LogicalOperator(category = {
		LogicalOperatorCategory.BENCHMARK }, doc = "If time interval, trust and recovery time are used as meta attributes and if recovery is used, this operator can calculate the recovery time (not the down time) based on the trust values.", maxInputPorts = 1, minInputPorts = 1, name = "RecoveryTime")
public class RecoveryTimeCalculatorAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -2240936911487736669L;

	/**
	 * Empty constructor.
	 */
	public RecoveryTimeCalculatorAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public RecoveryTimeCalculatorAO(RecoveryTimeCalculatorAO other) {
		super(other);
	}

	@Override
	public RecoveryTimeCalculatorAO clone() {
		return new RecoveryTimeCalculatorAO();
	}

}