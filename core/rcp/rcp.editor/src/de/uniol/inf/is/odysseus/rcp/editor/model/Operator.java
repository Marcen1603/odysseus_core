/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;

public class Operator implements Serializable {

	private static final long serialVersionUID = -8457946635244480941L;
	
	public static final String PROPERTY_X = "x";
	public static final String PROPERTY_Y = "y";
	public static final String PROPERTY_BUILD = "build";
	public static final String PROPERTY_CONNECTION_AS_SOURCE_ADDED = "connection_src_add";
	public static final String PROPERTY_CONNECTION_AS_TARGET_ADDED = "connection_tgt_add";
	public static final String PROPERTY_CONNECTION_AS_SOURCE_REMOVED = "connection_src_remove";
	public static final String PROPERTY_CONNECTION_AS_TARGET_REMOVED = "connection_tgt_remove";
	
	private IOperatorBuilder builder;
	private transient ILogicalOperator logicalOperator;
	private String builderName;
	private int x = 0;
	private int y = 0;
	private PropertyChangeSupport listeners;
	private List<OperatorConnection> connectionsAsSource = new ArrayList<OperatorConnection>();
	private List<OperatorConnection> connectionsAsTarget = new ArrayList<OperatorConnection>();
	
	public Operator( IOperatorBuilder builder, String builderName ) {
		this.builder = builder;
		this.builderName = builderName;
		listeners = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener listener ) {
		listeners.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
	
	public IOperatorBuilder getOperatorBuilder() {
		return builder;
	}
	
	public String getOperatorBuilderName() {
		return builderName;
	}
	
	public ILogicalOperator getLogicalOperator() {
		return logicalOperator;
	}
	
	public boolean hasLogicalOperator() {
		return logicalOperator != null;
	}
	
	public void addConnection( OperatorConnection connection ) {
		if( connection.getSource() == this ) {
			connectionsAsSource.add(connection);
			build();
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_SOURCE_ADDED, null, connectionsAsSource);
		} else if( connection.getTarget() == this ) {
			connectionsAsTarget.add(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_TARGET_ADDED, null, connectionsAsTarget);
			build();
		}
	}
	
	public void removeConnection( OperatorConnection connection ) {
		if( connection.getSource() == this ) {
			connectionsAsSource.remove(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_SOURCE_REMOVED, null, connectionsAsSource);
			build();
		} else if( connection.getTarget() == this ) {
			connectionsAsTarget.remove(connection);
			listeners.firePropertyChange(PROPERTY_CONNECTION_AS_TARGET_REMOVED, null, connectionsAsTarget);
			build();
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

	private boolean onBuild = false;
	public void build() {
		if( !onBuild ) { // verhindert unendliche rekursive aufrufe im ablaufplan
			onBuild = true;
//			System.out.println("Build Logical Operator : " + getOperatorBuilderName());
			
			// logicalOperator aus vorg�nger holen, falls m�glich
			int i = 0;
			((AbstractOperatorBuilder)builder).clearInputOperators();
			for( OperatorConnection connection : getConnectionsAsTarget()) {
				Operator source = connection.getSource();
				if( source.hasLogicalOperator() ) {
					ILogicalOperator op = connection.getSource().getLogicalOperator();
					builder.setInputOperator(i, op, i);
				} else {
					builder.setInputOperator(i, null, i); // l�scht operator
				}
				i++;
			}
			
			if( builder != null ) {
				if( builder.validate() ) {
					if( logicalOperator != null ) {
						// vorhande Verbindungen trennen
						logicalOperator.unsubscribeFromAllSources();
					}
					logicalOperator = builder.createOperator();
									
					// nachfolger auch bauen, falls m�glich
					for( OperatorConnection connection : getConnectionsAsSource() ) {
						connection.getTarget().build();
					}
					
					listeners.firePropertyChange(PROPERTY_BUILD, null, null);
					onBuild = false;
					return;
				} 
			}
			logicalOperator = null;
		}
		onBuild = false;
		listeners.firePropertyChange(PROPERTY_BUILD, null, null);
	}
	
}
