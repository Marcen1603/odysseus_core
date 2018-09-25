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
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class StreamEditorInput implements IStreamEditorInput {

	private final IStreamConnection<Object> connection;
	private final IStreamEditorType editorType;
	private final String editorTypeID;
	private final String editorLabel;
	private final IPhysicalOperator operator;
	private final Optional<Integer> queryIDToManage;
	
	public StreamEditorInput( IPhysicalOperator operator, IStreamEditorType type, String editorTypeID, String editorLabel ) {
		this(operator, type, editorTypeID, editorLabel, -1);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StreamEditorInput( IPhysicalOperator operator, IStreamEditorType type, String editorTypeID, String editorLabel, int queryIDToManage) {
		this.editorType = type;
		this.editorTypeID = editorTypeID;
		this.editorLabel = editorLabel;
		this.operator = operator;
		this.queryIDToManage = (Optional<Integer>) (queryIDToManage < 0 ? Optional.absent() : Optional.of(queryIDToManage)) ;

		connection = new DefaultStreamConnection(operator);
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
		return operator.getName()+" (Q:"+operator.getOwnerIDs()+") [" + editorLabel + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("unchecked")
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
	
	@Override
	public Optional<Integer> getManagedQueryID() {
		return queryIDToManage;
	}
}
