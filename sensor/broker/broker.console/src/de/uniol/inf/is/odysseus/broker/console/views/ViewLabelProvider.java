package de.uniol.inf.is.odysseus.broker.console.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	@Override
	public String getColumnText(Object obj, int index) {
		Object[] arrObj = (Object[]) obj;
		return getText(arrObj[index]);
	}

	@Override
	public Image getColumnImage(Object obj, int index) {
		return null;
	}

	@Override
	public Image getImage(Object obj) {
		return null;
	}
}
