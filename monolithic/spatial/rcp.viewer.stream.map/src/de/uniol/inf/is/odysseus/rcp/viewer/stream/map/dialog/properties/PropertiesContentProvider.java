package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.properties;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * Content provider for a treeviewer
 * 
 * @author Stefan Bothe
 * 
 */
public class PropertiesContentProvider implements ITreeContentProvider {

  private PropertiesModel model;

  @Override
  public void dispose() {
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.model = (PropertiesModel) newInput;
  }

  @Override
  public Object[] getElements(Object inputElement) {
    return model.getCategories().toArray();
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof PropertiesCategory) {
      PropertiesCategory category = (PropertiesCategory) parentElement;
      return category.getList().toArray();
    }
    return null;
  }

  @Override
  public Object getParent(Object element) {
    return null;
  }

  @Override
  public boolean hasChildren(Object element) {
    if (element instanceof PropertiesCategory) {
      return true;
    }
    return false;
  }

} 