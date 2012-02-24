/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamConnection;

public class StreamEditorInput implements IStreamEditorInput {

	private IStreamConnection<Object> connection;
	private IStreamEditorType editorType;
	private String editorTypeID;
	private String editorLabel;
	private IPhysicalOperator operator;
	
	public StreamEditorInput( IPhysicalOperator operator, IStreamEditorType type, String editorTypeID, String editorLabel ) {
		this.editorType = type;
		this.editorTypeID = editorTypeID;
		this.editorLabel = editorLabel;
		this.operator = operator;
		
		// Datenstromquellen identifizieren
		final Collection<ISource<?>> sources = new ArrayList<ISource<?>>();
		if( operator instanceof ISource<?> ) {
			sources.add( (ISource<?>)operator );
		} else if( operator instanceof ISink<?> ) {
			Collection<?> list = ((ISink<?>)operator).getSubscribedToSource();
			for( Object obj : list ) 
				sources.add( (ISource<?>)((PhysicalSubscription<?>)obj).getTarget() );
		} else 
			throw new IllegalArgumentException("could not identify type of content of node " + operator );
		
		connection = new DefaultStreamConnection<Object>(sources);
	}
	
	public String getEditorTypeID() {
		return editorTypeID;
	}
	
	@Override
	public IStreamConnection<Object> getStreamConnection() {
		return connection;
	}
	
	public IStreamEditorType getEditorType() {
		return editorType;
	}
	
	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return operator.getName() + "[" + editorLabel + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof StreamEditorInput) {
			StreamEditorInput i = (StreamEditorInput)obj;
			return operator.equals(i.operator) && editorTypeID.equals(((StreamEditorInput) obj).getEditorTypeID());
		}
		return false;	
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public IPhysicalOperator getPhysicalOperator() {
		return operator;
	}
}
