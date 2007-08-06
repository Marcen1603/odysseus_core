package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A stripped down representation of a planoperator
 * @author Jonas Jacobi
 * @see mg.dynaquest.queryexecution.po.base.PlanOperator
 */
public class PlanOperator implements Serializable {
	private static final long serialVersionUID = 470597608192767119L;

	private String guid;

	private String type;

	private List<PlanOperator> subOperators;

	private boolean isAccessPlanOperator;

	private String source;

	private PlanOperator parent;
	
	private int bufferSize;

	/**
	 * Constructor
	 * 
	 * Use this c'tor for non-access plans 
	 * @param guid unique identifier of the planoperator
	 * @param type type of the planoperator
	 * @param bufferSize buffersize used by the planoperator
	 */
	public PlanOperator(String guid, String type, int bufferSize) {
		super();
		this.guid = guid;
		this.type = type;
		this.bufferSize = bufferSize;
		this.subOperators = new ArrayList<PlanOperator>();
		this.source = null;
		determineIsAccess();
		if (isAccessPlanOperator()) {
			throw new IllegalArgumentException(
					"Planoperator is AccessPlanOpertor but doesn't provide a source");
		}
	}

	/**
	 * Constructor
	 * 
	 * Use this c'tor for access plans
	 * @param guid unique identifier of the planoperator
	 * @param type type of the planoperator
	 * @param bufferSize buffersize used by the planoperator
	 * @param source the source the planoperator accesses (null for
	 * non-access operators).
	 */
	public PlanOperator(String guid, String type, int bufferSize, String source) {
		super();
		this.guid = guid;
		this.type = type;
		this.bufferSize = bufferSize;
		this.subOperators = new ArrayList<PlanOperator>();
		this.source = source;
		determineIsAccess();
		if (!isAccessPlanOperator() && source != null) {
			throw new IllegalArgumentException(
					"Planoperator is NOT an AccessPlanOperator but provides a source");
		}
	}
	
	/**
	 * Check wether the planoperator gets its data from
	 * other planoperators
	 * @return true if the planoperator has sub operators
	 */
	public boolean hasSubOperators() {
		return this.subOperators.size() > 0;
	}

	/**
	 * Add a sub operator
	 * @param po operator to add
	 */
	public void addSubOperator(PlanOperator po) {
		this.subOperators.add(po);
	}

	/**
	 * Get the unique identifier of this planoperator
	 * @return the uniqe identifier
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Get the source of the planoperator
	 * @return the source of the planoperator (null for non-access operators)
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the type of the planoperator (this is the simplified class name of 
	 * a {@link mg.dynaquest.queryexecution.po.base.PlanOperator} subclass)
	 * @return the type of the planoperator
	 * @see mg.dynaquest.queryexecution.po.base.PlanOperator
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get the size of the buffer used by this planoperator
	 * @return
	 */
	public int getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * Check if planoperator is an access operator
	 * @return true if planoperator is an access operator, false otherwise
	 */
	public boolean isAccessPlanOperator() {
		return isAccessPlanOperator;
	}

	/**
	 * Get all suboperators of this planoperator
	 * @return the suboperators of this planoperator or null
	 * if it is an access operator.
	 */
	public List<PlanOperator> getSubOperators() {
		return subOperators;
	}

	/**
	 * Get the parent planoperator
	 * @return the parent planoperator, null if it has no parent
	 */
	public PlanOperator getParent() {
		return this.parent;
	}

	/**
	 * Set the parent planoperator
	 * @param p the parent
	 */
	public void setParent(PlanOperator p) {
		this.parent = p;
	}

	/**
	 * calculate if planoperator is an access operator.
	 * this is determined by the type.
	 * ATM only RMIDataAccessPOs are considered access operators,
	 * because they are the only working ones.
	 */
	private void determineIsAccess() {
		this.isAccessPlanOperator = false;

		if (type.equals("RMIDataAccessPO")) {
			this.isAccessPlanOperator = true;
			return;
		}
	}
	
	@Override
	public int hashCode() {
		return this.guid.hashCode();
	}
	
	public boolean equals(Object o) {
		return this.getGuid().equals(((PlanOperator)o).getGuid());
	}

}
