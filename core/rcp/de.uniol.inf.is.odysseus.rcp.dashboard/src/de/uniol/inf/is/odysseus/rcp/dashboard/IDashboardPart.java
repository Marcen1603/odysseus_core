package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.nio.ByteBuffer;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;

public interface IDashboardPart extends IStreamElementListener<Object> {
	
	public boolean init( Configuration configuration );
	public Configuration getConfiguration();
	
	public void createPartControl( Composite parent );
	
	public void save( ByteBuffer buffer );
	public void load( ByteBuffer buffer );
	
	public void setQuery(List<String> queryLines );
	public ImmutableList<String> getQuery();
	
}
