package de.uniol.inf.is.odysseus.broker.console.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
		Object[] arrObj = (Object[]) obj;
		return getText(arrObj[index]);
	}

	public Image getColumnImage(Object obj, int index) {
		return null;
	}

	public Image getImage(Object obj) {
		return null;
	}
}
