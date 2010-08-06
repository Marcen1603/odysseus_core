package de.uniol.inf.is.odysseus.rcp.viewer.stream.editor;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;

public class StreamEditorInput implements IStreamEditorInput {

	private INodeModel<IPhysicalOperator> nodeModel;
	private IStreamConnection<Object> connection;
	private IStreamEditorType editorType;
	private String editorTypeID;
	private String editorLabel;
	
	public StreamEditorInput( INodeModel<IPhysicalOperator> nodeModel, IStreamEditorType type, String editorTypeID, String editorLabel ) {
		this.nodeModel = nodeModel;
		this.editorType = type;
		this.editorTypeID = editorTypeID;
		this.editorLabel = editorLabel;
		
		// Datenstromquellen identifizieren
		final Collection<ISource<?>> sources = new ArrayList<ISource<?>>();
		IPhysicalOperator content = nodeModel.getContent();
		if( content instanceof ISource<?> ) {
			sources.add( (ISource<?>)content );
		} else if( content instanceof ISink<?> ) {
			Collection<?> list = ((ISink<?>)content).getSubscribedToSource();
			for( Object obj : list ) 
				sources.add( (ISource<?>)((PhysicalSubscription<?>)obj).getTarget() );
		} else 
			throw new IllegalArgumentException("could not identify type of content of node " + nodeModel );
		
		connection = new DefaultStreamConnection<Object>(sources);
	}
	
	public String getEditorTypeID() {
		return editorTypeID;
	}
	
	@Override
	public INodeModel<IPhysicalOperator> getNodeModel() {
		return nodeModel;
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
		return nodeModel.getName() + "[" + editorLabel + "]";
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
			return nodeModel.equals(i.getNodeModel()) && editorTypeID.equals(((StreamEditorInput) obj).getEditorTypeID());
		}
		return false;	
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
