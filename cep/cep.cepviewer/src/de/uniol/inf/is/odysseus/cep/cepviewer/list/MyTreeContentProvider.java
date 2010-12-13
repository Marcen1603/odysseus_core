package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class MyTreeContentProvider extends ArrayContentProvider implements ITreeContentProvider {

	   public Object[] getChildren(Object parent) {
	      CEPTreeItem ex1 = (CEPTreeItem) parent;
	      return ex1.getChildren().toArray();
	   }

	   public Object getParent(Object element) {
		   CEPTreeItem ex1 = (CEPTreeItem) element;
	      return ex1.parent;
	   }


	   public boolean hasChildren(Object element) {
		   CEPTreeItem ex1 = (CEPTreeItem) element;
	      return ex1.getChildren().size() > 0;
	   }
	}