package de.uniol.inf.is.odysseus.rcp.logging.container;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;
import de.uniol.inf.is.odysseus.rcp.logging.view.filter.AllRCPLogFilter;
import de.uniol.inf.is.odysseus.rcp.logging.view.filter.IRCPLogFilter;

public final class LogContentProvider implements IStructuredContentProvider {

	private IRCPLogFilter filter = AllRCPLogFilter.INSTANCE;
	
	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if( inputElement instanceof List ) {
			List<RCPLogEntry> entries = toRCPLogEntries(inputElement);	
			List<RCPLogEntry> filteredEntries = applyFilter(entries);
			return filteredEntries.toArray();
		}
		
		return null;
	}

	public List<RCPLogEntry> applyFilter(List<RCPLogEntry> entries) {
		List<RCPLogEntry> filteredEntries = Lists.newLinkedList();
		
		for( RCPLogEntry logEntry : entries ) {
			if( filter.isShown(logEntry)) {
				filteredEntries.add(logEntry);
			}
		}
		return filteredEntries;
	}
	
	@SuppressWarnings("unchecked")
	private static List<RCPLogEntry> toRCPLogEntries(Object inputElement) {
		return (List<RCPLogEntry>)inputElement;
	}

	public void setFilter(IRCPLogFilter filter) {
		if( filter == null ) {
			filter = AllRCPLogFilter.INSTANCE;
		}
		
		this.filter = filter;
	}
}
