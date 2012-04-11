package de.uniol.inf.is.odysseus.rcp.queryview.physical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewDataProvider;
import de.uniol.inf.is.odysseus.rcp.views.query.QueryView;

public class PhysicalQueryViewDataProvider implements IQueryViewDataProvider, IPlanModificationListener, IDoubleClickListener {

	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryViewDataProvider.class);
	private QueryView view;
	
	@Override
	public void init(QueryView view) {
		this.view = view;
		
        view.getTableViewer().addDoubleClickListener(this);
        listenToExecutor();
	}

	@Override
	public void dispose() {
		view.getTableViewer().removeDoubleClickListener(this);
		this.view = null;
		unlistenToExecutor();
	}
	
	private void unlistenToExecutor() {
		IServerExecutor executor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
		executor.removePlanModificationListener(this);
	}

	private void listenToExecutor() {
		IServerExecutor executor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
		executor.addPlanModificationListener(this);
	}

	@Override
    public Collection<? extends IQueryViewData> getData() {

        IServerExecutor serverExecutor = PhysicalQueryViewDataProviderPlugIn.getServerExecutor();
        Collection<IPhysicalQuery> queries = serverExecutor.getExecutionPlan().getQueries();

        List<IQueryViewData> result = new ArrayList<IQueryViewData>();
        for (IPhysicalQuery query : queries) {
            result.add( new PhysicalQueryViewData(
                    query.getID(), 
                    getQueryStatus(query),
                    query.getPriority(), 
                    query.getLogicalQuery().getParserId(),
                    getQueryUser(query), 
                    query.getLogicalQuery().getQueryText()) );
        }

        return result;
    }
    
    private static String getQueryStatus(IPhysicalQuery q) {
        return q.isOpened() ? "Running" : "Inactive";
    }
    
    private static String getQueryUser( IPhysicalQuery query ) {
//        if( query.getUser() != null && query.getUser().getUser() != null && query.getUser().getUser().getName() != null ) {
//            return query.getUser().getUser().getName();
//        }
        return "[No user]";
    }

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		view.refreshTable();
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
        IHandlerService handlerService = (IHandlerService) view.getSite().getService(IHandlerService.class);
        try {
            handlerService.executeCommand("de.uniol.inf.is.odysseus.rcp.commands.CallGraphEditorCommand", null);
        } catch (Exception ex) {
        	LOG.error("Exception during calling graph editor", ex);
        }
	}

}
