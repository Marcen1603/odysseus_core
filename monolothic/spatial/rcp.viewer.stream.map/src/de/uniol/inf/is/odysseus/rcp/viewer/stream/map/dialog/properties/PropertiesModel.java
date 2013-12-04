package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.GroupLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

/**
 * 
 * Model class for a treeviewer
 * 
 * @author Stefan Bothe
 * 
 */
public class PropertiesModel {

	private Object[] array;
	private Object root;

	public PropertiesModel(Object[] array, Object root) {
		this.array = array;
		this.root = root;
	}

	public List<PropertiesCategory> getCategories() {
		List<PropertiesCategory> categories = new ArrayList<PropertiesCategory>();

		PropertiesCategory category = new PropertiesCategory();
		category.setName("root");
		if (root instanceof GroupLayer) {
			category.setName(((GroupLayer) root).getName());
		}
		if (root instanceof ILayer) {
			category.setName(((ILayer) root).getName());
		}
		if (root instanceof IFile) {
			category.setName(((IFile) root).getName());
		}

		categories.add(category);
		for (Object child : array) {
			if (child instanceof GroupLayer) {
				PropertiesCategory newCat = new PropertiesCategory();
				newCat.setName(((GroupLayer) child).getName());
				category.getList().add(newCat);
				if (!((GroupLayer) child).isEmpty()) {
					for (int i = 0; i < ((GroupLayer) child).size(); i++) {
						newCat.getList().add(
								((GroupLayer) child).get(i));
					}
				}
			} else {
				category.getList().add(child);
			}
		}
		return categories;
	}
}