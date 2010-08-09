package de.uniol.inf.is.odysseus.rcp.sources.view.part;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.sources.view.activator.Activator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class SourcesLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if( element instanceof Entry ) 
			return Activator.getDefault().getImageRegistry().get("source");
		if( element instanceof SDFAttribute ) 
			return Activator.getDefault().getImageRegistry().get("attribute");
		
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof Entry ) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>)element;
			return entry.getKey();
		}
		if( element instanceof SDFAttribute ) {
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getAttributeName());
			name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}
		return null;
	}

}
