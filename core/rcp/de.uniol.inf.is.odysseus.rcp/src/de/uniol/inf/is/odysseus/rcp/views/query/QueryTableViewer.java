package de.uniol.inf.is.odysseus.rcp.views.query;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public class QueryTableViewer extends TableViewer {

	public QueryTableViewer(Composite parent, int style) {
		super(parent, style);
		
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
	}

	@Override
	protected List<?> getSelectionFromWidget() {

		List<Integer> queryIds = new ArrayList<Integer>();
		for (Object item : super.getSelectionFromWidget()) {
			queryIds.add(((IQueryViewData) item).getId());
		}

		return queryIds;
	}
}