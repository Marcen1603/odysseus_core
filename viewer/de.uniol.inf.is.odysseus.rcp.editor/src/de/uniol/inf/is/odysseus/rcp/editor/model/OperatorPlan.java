package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OperatorPlan {

	public static final String PROPERTY_OPERATORS = "operators";
	
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
		operators.add(op);
		listeners.firePropertyChange(PROPERTY_OPERATORS, null, operators);
	}
	
	public void removeOperator( Operator op ) {
		operators.remove(op);
		listeners.firePropertyChange(PROPERTY_OPERATORS, null, operators);
	}
	
	public List<Operator> getOperators() {
		return Collections.unmodifiableList(operators);
	}
}
