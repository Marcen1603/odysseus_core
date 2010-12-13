package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MyTreeLabelProvider extends LabelProvider {
	//TODO Image zuweisen
	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		CEPTreeItem ex1 = (CEPTreeItem) element;
		return ex1.getName();
	}
}