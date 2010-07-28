package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class Operator {

	public static final String PROPERTY_X = "x";
	public static final String PROPERTY_Y = "y";
	public static final String PROPERTY_CONNECTION_AS_SOURCE_ADDED = "connection_src_add";
	public static final String PROPERTY_CONNECTION_AS_TARGET_ADDED = "connection_tgt_add";
	public static final String PROPERTY_CONNECTION_AS_SOURCE_REMOVED = "connection_src_remove";
	public static final String PROPERTY_CONNECTION_AS_TARGET_REMOVED = "connection_tgt_remove";
	
	private ILogicalOperator operator;
	private IOperatorExtensionDescriptor descriptor;
	private int x = 0;
	private int y = 0;
	private PropertyChangeSupport listeners;
	private List<OperatorConnection> connectionsAsSource = new ArrayList<OperatorConnection>();
	private List<OperatorConnection> connectionsAsTarget = new ArrayList<OperatorConnection>();
	
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
	
	public void addConnection( OperatorConnection connection ) {
		if( connection.getSource() == this ) {
			connectionsAsSource.add(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_SOURCE_ADDED, null, connectionsAsSource);
		} else if( connection.getTarget() == this ) {
			connectionsAsTarget.add(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_TARGET_ADDED, null, connectionsAsTarget);
		}
	}
	
	public void removeConnection( OperatorConnection connection ) {
		if( connection.getSource() == this ) {
			connectionsAsSource.remove(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_SOURCE_REMOVED, null, connectionsAsSource);
		} else if( connection.getTarget() == this ) {
			connectionsAsTarget.remove(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_TARGET_REMOVED, null, connectionsAsTarget);
		}
	}
	
	public List<OperatorConnection> getConnectionsAsSource() {
		return connectionsAsSource;
	}
	
	public List<OperatorConnection> getConnectionsAsTarget() {
		return connectionsAsTarget;
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
