package de.uniol.inf.is.odysseus.rcp.dashboard.controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.DefaultStreamConnection;

public final class DashboardPartController {

	private static enum Status {
		RUNNING, STOPPED, PAUSED
	}

	private final IDashboardPart dashboardPart;
	private List<Integer> queryIDs;
	private DefaultStreamConnection<Object> streamConnection;

	private Status status = Status.STOPPED;

	public DashboardPartController(IDashboardPart dashboardPart) {
		this.dashboardPart = Preconditions.checkNotNull(dashboardPart, "DashboardPart for container must not be null!");
	}

	public IDashboardPart getDashboardPart() {
		return dashboardPart;
	}

	public void start() throws Exception {
		Preconditions.checkState(status != Status.RUNNING, "Container for DashboardParts already started");
		Preconditions.checkState(status != Status.PAUSED, "Container for DashboardParts is paused and cannot be started.");

		IFile file = dashboardPart.getQueryFile();
		if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		Scanner lineScanner = new Scanner(file.getContents());

		StringBuilder sb = new StringBuilder();
		while (lineScanner.hasNextLine()) {
			sb.append(lineScanner.nextLine()).append("\n");
		}

		List<?> results = DashboardPlugIn.getScriptParser().parseAndExecute(sb.toString(), OdysseusRCPPlugIn.getActiveSession(), null);
		queryIDs = getExecutedQueryIDs(results);

		List<IPhysicalOperator> roots = Lists.newArrayList();
		for (Integer id : queryIDs) {
			List<IPhysicalOperator> rootsOfQuery = DashboardPlugIn.getExecutor().getPhysicalRoots(id);
			for( IPhysicalOperator rootOfQuery : rootsOfQuery ) {
				if( !(rootOfQuery instanceof DefaultStreamConnection)) {
					roots.add(rootOfQuery);
				}
			}
		}

		streamConnection = new DefaultStreamConnection<Object>(getSubscriptions(roots));
		
		dashboardPart.onStart(roots);
		streamConnection.addStreamElementListener(dashboardPart);
		streamConnection.connect();

		status = Status.RUNNING;

	}

	public void pause() {
		if (status == Status.PAUSED) {
			return;
		}

		streamConnection.disable();
		dashboardPart.onPause();

		status = Status.PAUSED;
	}

	public void unpause() {
		Preconditions.checkState(status == Status.PAUSED, "Container for DashboardParts cannot be unpaused.");

		dashboardPart.onUnpause();
		streamConnection.enable();

		status = Status.RUNNING;
	}

	public void stop() {
		if (status == Status.STOPPED) {
			return;
		}

		streamConnection.disconnect();
		dashboardPart.onStop();

		for (Integer id : queryIDs) {
			DashboardPlugIn.getExecutor().removeQuery(id, OdysseusRCPPlugIn.getActiveSession());
		}
		queryIDs = null;

		status = Status.STOPPED;
	}

	@SuppressWarnings("unchecked")
	private static List<ISubscription<? extends ISource<Object>>> getSubscriptions(List<IPhysicalOperator> roots) {
		final List<ISubscription<? extends ISource<Object>>> subs = new LinkedList<ISubscription<? extends ISource<Object>>>();

		for (IPhysicalOperator operator : roots) {
			if (operator instanceof ISource<?>) {
				subs.add(new PhysicalSubscription<ISource<Object>>((ISource<Object>) operator, 0, 0, operator.getOutputSchema()));
			} else if (operator instanceof ISink<?>) {
				Collection<?> list = ((ISink<?>) operator).getSubscribedToSource();

				for (Object obj : list) {
					PhysicalSubscription<ISource<Object>> sub = (PhysicalSubscription<ISource<Object>>) obj;
					subs.add(new PhysicalSubscription<ISource<Object>>(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema()));
				}
			} else {
				throw new IllegalArgumentException("could not identify type of content of node " + operator);
			}
		}

		return subs;
	}

	private static List<Integer> getExecutedQueryIDs(List<?> results) {
		List<Integer> ids = Lists.newArrayList();

		for (Object result : results) {
			if (result instanceof List) {
				@SuppressWarnings("rawtypes")
				List list = (List) result;
				for (Object obj : list) {
					if (obj instanceof Integer) {
						ids.add((Integer) obj);
					}
				}
			}
		}

		return ids;
	}
}
