package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.nio.ByteBuffer;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<Object>, IConfigurationListener {
	
	public boolean init( Configuration configuration );
	public Configuration getConfiguration();
	
	public void createPartControl( Composite parent );
	
	public void save( ByteBuffer buffer );
	public void load( ByteBuffer buffer );
	
	public void setQueryFile( IFile file );
	public IFile getQueryFile();
}
