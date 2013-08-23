package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.IPictogram;


public abstract class AbstractPictogramGroup<T> implements IPictogramGroup<T> {
	private SDFAttribute attribute;	
	private Composite composite;
	private List<IPictogram> pictograms = new ArrayList<>();
	
	
	@Override
	public void setAttribute(SDFAttribute attribute){
		if(!getAllowedDatatypes().contains(attribute.getDatatype())){
			throw new IllegalArgumentException("Attribute ist not allowed"); 
		}				
		this.attribute = attribute;
	}

	@Override
	public SDFAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void init(Composite parent){		
		this.composite = new Composite(parent, SWT.NONE);
		this.composite.setLayout(new GridLayout(1, true));
		this.composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		for(IPictogram p : this.pictograms){
			p.init(this.composite);
		}
		this.pictograms.get(0).activate();
	}
			
	@Override
	public Composite getComposite() {		
		return this.composite;
	}
	
	@Override
	public IPictogram getPictogram(int index) {
		return this.pictograms.get(index);
	}
	
	public List<IPictogram> getPictograms() {
		return this.pictograms;
	}
	
	@Override
	public void addPictogram(IPictogram pg) {
		this.pictograms.add(pg);
	}
		
}
