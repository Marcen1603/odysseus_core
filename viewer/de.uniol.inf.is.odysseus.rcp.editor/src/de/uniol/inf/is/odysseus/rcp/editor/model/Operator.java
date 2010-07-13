package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class Operator {

	public static final String PROPERTY_X = "x";
	public static final String PROPERTY_Y = "y";
	
	private ILogicalOperator operator;
	private IOperatorExtensionDescriptor descriptor;
	private int x = 0;
	private int y = 0;
	private PropertyChangeSupport listeners;
	
	public Operator( IOperatorExtensionDescriptor desc ) {
		this.descriptor = desc;
		this.operator = desc.getExtensionClass().create();
		listeners = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener listener ) {
		listeners.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
	
	public ILogicalOperator getLogicalOperator() {
		return operator;
	}
	
	public IOperatorExtensionDescriptor getOperatorExtensionDescriptor() {
		return descriptor;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if( x != this.x ) {
			int oldX = this.x;
			this.x = x;
			listeners.firePropertyChange(PROPERTY_X, oldX, x);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if( y != this.y ) {
			int oldY = this.y;
			this.y = y;
			listeners.firePropertyChange(PROPERTY_Y, oldY, y);
		}
	}

}
