package de.uniol.inf.is.odysseus.rcp.views;

import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.AbstractResourceInformation;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class ResourceInformationLabelProvider extends AbstractViewLabelProvider {

	private String operatorImage;

	public ResourceInformationLabelProvider(String operatorImage) {
		this.operatorImage = operatorImage;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Entry) {
			return OdysseusRCPPlugIn.getImageManager().get(operatorImage);
		}
		if (element instanceof AbstractResourceInformation) {
			String image = ((AbstractResourceInformation)element).getType();
			if (Strings.isNullOrEmpty(image)){
				image = operatorImage;
			}
			return OdysseusRCPPlugIn.getImageManager().get(image);
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) element;
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey()).append(" [")
					.append(entry.getValue().getClass().getSimpleName())
					.append("]");
			return sb.toString();
		}
		if (element instanceof AbstractResourceInformation) {
			AbstractResourceInformation vi = (AbstractResourceInformation) element;
			StringBuilder sb = new StringBuilder();
			sb.append(vi.getName());
			return sb.toString();
		}

		return super.getText(element);
	}

}
