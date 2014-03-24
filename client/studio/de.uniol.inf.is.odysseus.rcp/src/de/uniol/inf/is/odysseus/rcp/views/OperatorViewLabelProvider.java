package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class OperatorViewLabelProvider extends AbstractViewLabelProvider {

	
	private String operatorImage;

	public OperatorViewLabelProvider(String operatorImage){
		this.operatorImage = operatorImage;
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof Entry) {
			return OdysseusRCPPlugIn.getImageManager().get(operatorImage);
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) element;
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey()).append(" [").append(entry.getValue().getClass().getSimpleName()).append("]");
			return sb.toString();
		}
		return super.getText(element);
	}
	
}
