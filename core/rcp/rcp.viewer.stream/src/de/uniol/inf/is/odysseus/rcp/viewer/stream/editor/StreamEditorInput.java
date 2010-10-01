package de.uniol.inf.is.odysseus.rcp.viewer.stream.editor;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamConnection;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;

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
