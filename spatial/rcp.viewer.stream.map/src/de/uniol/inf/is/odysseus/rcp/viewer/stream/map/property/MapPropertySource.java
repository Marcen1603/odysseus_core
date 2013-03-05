package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;

public class MapPropertySource implements IPropertySource {

	private final StreamMapEditorPart editor;
	

	
	public MapPropertySource(StreamMapEditorPart editor) {
	    this.editor = editor;
    }
	
	
	@Override
    public Object getEditableValue() {
	    return null;
    }

	@Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
	    return null;
    }

	@Override
    public Object getPropertyValue(Object arg0) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public boolean isPropertySet(Object arg0) {
	    return false;
    }

	@Override
    public void resetPropertyValue(Object arg0) {
	
    }

	@Override
    public void setPropertyValue(Object arg0, Object arg1) {
	
    }

	

}
