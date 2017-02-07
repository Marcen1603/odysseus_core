package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public abstract class AbstractViewLabelProvider extends StyledCellLabelProvider {

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public Image getImage(Object element) {
		if (element instanceof SDFAttribute) {
			return OdysseusRCPPlugIn.getImageManager().get("attribute");
		}
		if (element instanceof SDFConstraint) {
			return OdysseusRCPPlugIn.getImageManager().get("constraint");
		}

		if (element instanceof SDFUnit) {
			return OdysseusRCPPlugIn.getImageManager().get("unit");
		}
		return null;
	}

	@Override
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();
		String text = getText(obj);
		if (text == null) {
			text = "";
		}
		StyledString styledString = new StyledString(text);

		if (obj instanceof SDFAttribute) {
			SDFAttribute attribute = (SDFAttribute) obj;
			styledString.append("  " + attribute.getDatatype().toString(), StyledString.QUALIFIER_STYLER);
		}

		cell.setText(styledString.toString());
		cell.setStyleRanges(styledString.getStyleRanges());
		cell.setImage(getImage(obj));
		super.update(cell);

	}

	public String getText(Object element) {
		if (element instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) element;
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey()).append(" [").append(entry.getValue().getClass().getSimpleName()).append("]");
			return sb.toString();
		}
		if (element instanceof SDFAttribute) {
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getAttributeName());
			// name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}

		if (element instanceof SDFConstraint) {
			StringBuilder sb = new StringBuilder();
			SDFConstraint dt = (SDFConstraint) element;
			return sb.append(dt.getURI()).append(" = " + dt.getValue()).toString();
		}

		if (element instanceof SDFUnit) {
			StringBuilder sb = new StringBuilder();
			SDFUnit unit = (SDFUnit) element;
			return sb.append(unit.getClass().getSimpleName() + ": " + unit.getURI()).toString();
		}

		return null;
	}

}
