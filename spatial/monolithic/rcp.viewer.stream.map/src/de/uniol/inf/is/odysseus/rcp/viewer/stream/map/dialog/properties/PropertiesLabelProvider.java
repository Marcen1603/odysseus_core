package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import org.eclipse.jface.viewers.LabelProvider;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.GroupLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.LineStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;

/**
 * 
 * Label provider for a treeviewer
 * 
 * @author Stefan Bothe
 * 
 */
public class PropertiesLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof PropertiesCategory) {
			PropertiesCategory category = (PropertiesCategory) element;
			return category.getName();
		}
		if (element instanceof PointStyle) {
			return "PointStyle";
		}
		if (element instanceof LineStyle) {
			return "LineStyle";
		}
		if (element instanceof PolygonStyle) {
			return "PolygonStyle";
		}
		if (element instanceof ILayer) {
			return ((ILayer) element).getName();
		}
		if (element instanceof GroupLayer) {
			return ((GroupLayer) element).getName();
		}
		return "undefined";
	}
}