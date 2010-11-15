package de.uniol.inf.is.odysseus.rcp.sources.view.part;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.sources.view.activator.Activator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.usermanagement.User;

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
		if (element instanceof Entry) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>) element;
			if (DataDictionary.getInstance().isView(entry.getKey())){
				return Activator.getDefault().getImageRegistry().get("view");				
			}else{
				return Activator.getDefault().getImageRegistry().get("source");
			}
		}
		if (element instanceof SDFAttribute) {
			return Activator.getDefault().getImageRegistry().get("attribute");
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Entry) {
			@SuppressWarnings("unchecked")
			Entry<String, ILogicalOperator> entry = (Entry<String, ILogicalOperator>) element;
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey()).append(" [")
					.append(entry.getValue().getClass().getSimpleName())
					.append("]");
			User user = DataDictionary.getInstance().getUserForViewOrStream(
					entry.getKey());
			if (user != null) {
				sb.append(" created by ").append(user.getUsername());
			} else {
				sb.append(" created by no user ??");
			}
			return sb.toString();
		}
		if (element instanceof SDFAttribute) {
			SDFAttribute a = (SDFAttribute) element;
			StringBuffer name = new StringBuffer(a.getAttributeName());
			name.append(":").append(a.getDatatype().getURI());
			return name.toString();
		}
		return null;
	}

}
