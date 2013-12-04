package de.uniol.inf.is.odysseus.rcp.server.views;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.server.OdysseusRCPServerPlugIn;

public abstract class AbstractViewLabelProvider extends StyledCellLabelProvider {

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public Image getImage(Object element) {		
		if (element instanceof SDFAttribute) {
			return OdysseusRCPServerPlugIn.getImageManager().get("attribute");
		}
		return null;
	}

	@Override
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();
		StyledString styledString = new StyledString(getText(obj));

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
		return null;
	}

}
