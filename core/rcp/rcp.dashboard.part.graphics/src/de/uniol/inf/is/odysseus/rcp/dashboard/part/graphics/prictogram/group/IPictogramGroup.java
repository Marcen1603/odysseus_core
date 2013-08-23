package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.IPictogram;


public interface IPictogramGroup<T> {	
	public String getName();
	
	public void process(T value);
	public Set<SDFDatatype> getAllowedDatatypes();
			
	public void setAttribute(SDFAttribute attribute);	
	public SDFAttribute getAttribute();
	
	public void addPictogram(IPictogram pg);
	public List<IPictogram> getPictograms();
	public IPictogram getPictogram(int index);
	
	
	
	public int getMinimumPictograms();
	public int getMaximumPictograms();
	
	public void init(Composite parent);
	public Composite getComposite();
}
