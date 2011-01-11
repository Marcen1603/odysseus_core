package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperatorPlan implements Serializable, PropertyChangeListener {

	private static final long serialVersionUID = -3927705609150720811L;

	public static final String PROPERTY_OPERATOR_ADD = "operator_add";
	public static final String PROPERTY_OPERATOR_REMOVE = "operator_remove";
	public static final String PROPERTY_OPERATOR_CHANGE = "operator_change";
	
	private List<Operator> operators = new ArrayList<Operator>();
	private PropertyChangeSupport listeners;
	
	public OperatorPlan() {
		listeners = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener listener ) {
		listeners.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
	
	public void addOperator( Operator op ) {
		op.addPropertyChangeListener(this);
		operators.add(op);
		
		listeners.firePropertyChange(PROPERTY_OPERATOR_ADD, null, op);
	}
	
	public void removeOperator( Operator op ) {
		op.removePropertyChangeListener(this);
		operators.remove(op);
		listeners.firePropertyChange(PROPERTY_OPERATOR_REMOVE, null, op);
	}
	
	public List<Operator> getOperators() {
		return Collections.unmodifiableList(operators);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		listeners.firePropertyChange(PROPERTY_OPERATOR_CHANGE, null, evt);
	}
}
