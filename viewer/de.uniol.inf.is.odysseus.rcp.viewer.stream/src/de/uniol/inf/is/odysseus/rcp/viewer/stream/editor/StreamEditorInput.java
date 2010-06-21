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
	
	public StreamEditorInput( INodeModel<IPhysicalOperator> nodeModel, IStreamEditorType type ) {
		this.nodeModel = nodeModel;
		this.editorType = type;
		
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
		return nodeModel.getName();
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
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof StreamEditorInput) {
			return nodeModel.equals(((StreamEditorInput)obj).getNodeModel());
		}
		return false;	
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
