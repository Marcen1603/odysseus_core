package de.uniol.inf.is.odysseus.peer.rcp.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SelectionHelper {

	private static final Logger LOG = LoggerFactory.getLogger(SelectionHelper.class);
	
	private SelectionHelper() {
	}
	
	public static <T> List<T> getSelection() {
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
		try {
			if (selection instanceof IStructuredSelection) {
				List<T> items = new ArrayList<T>();
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				Iterator<?> iter = structSelection.iterator();
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					T item = (T) iter.next();
					items.add(item);
				}
				return items;
			}
		} catch (Throwable t) {
			LOG.error("Could not determine selection", t);
		}
		return new ArrayList<T>();
	}
}
