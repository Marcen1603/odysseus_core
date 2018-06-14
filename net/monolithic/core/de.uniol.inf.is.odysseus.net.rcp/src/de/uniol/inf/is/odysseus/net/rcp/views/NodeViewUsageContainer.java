package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.net.util.RepeatingJobThread;

public class NodeViewUsageContainer implements IOdysseusNodeConnectionManagerListener{

	private static final Logger LOG = LoggerFactory.getLogger(NodeViewUsageContainer.class);
	private static final long REFRESH_INTERVAL_MILLIS = 5000;

	private final Map<IOdysseusNode, IResourceUsage> usageMap = Maps.newHashMap();
	private final Map<String, IOdysseusNode> nameMap = Maps.newConcurrentMap();
	private final List<IOdysseusNode> foundNodes = Lists.newArrayList();
	private final Collection<IOdysseusNode> refreshing = Lists.newLinkedList();

	private TableViewer tableViewer;
	private RepeatingJobThread refresher;

	public final void init(TableViewer table) {
		Preconditions.checkNotNull(table, "Table must not be null!");

		this.tableViewer = table;

		OdysseusNetRCPPlugIn.getOdysseusNodeConnectionManager().addListener(this);

		refresher = new RepeatingJobThread(REFRESH_INTERVAL_MILLIS, "Refresher of node view resource usages") {
			@Override
			public void doJob() {
				refreshAllNodes();
			}
		};
		refresher.start();
	}

	public final void dispose() {
		if (refresher != null) {
			refresher.stopRunning();
			refresher = null;
		}

		OdysseusNetRCPPlugIn.getOdysseusNodeConnectionManager().removeListener(this);
	}

	// for nodeview to present the data
	Collection<IOdysseusNode> getFoundNodesList() {
		return foundNodes;
	}

	public boolean isNodeActive(IOdysseusNode node) {
		return OdysseusNetRCPPlugIn.getOdysseusNodeConnectionManager().isConnected(node);
	}

	public Optional<IResourceUsage> determineResourceUsage(IOdysseusNode node) {
		synchronized (usageMap) {
			return Optional.fromNullable(usageMap.get(node));
		}
	}
	
	public void cleanNodesList() {
		synchronized( usageMap ) {
			synchronized( foundNodes ) {
				synchronized( nameMap ) {
					usageMap.clear();
					nameMap.clear();
					foundNodes.clear();
				}
			}
		}
		
		refreshAllNodes();
	}

	public void refreshAllNodes() {
		Collection<IOdysseusNodeConnection> remoteNodes = OdysseusNetRCPPlugIn.getOdysseusNodeConnectionManager().getConnections();
		synchronized (foundNodes) {
			for (IOdysseusNodeConnection remoteNode : remoteNodes) {
				if (!foundNodes.contains(remoteNode.getOdysseusNode())) {
					foundNodes.add(remoteNode.getOdysseusNode());
					nameMap.put(remoteNode.getOdysseusNode().getName(), remoteNode.getOdysseusNode());
				}
			}
		}
		
		refreshTableAsync();

		for (final IOdysseusNodeConnection remoteNode : remoteNodes) {
			refreshNode(remoteNode.getOdysseusNode());
		}
	}

	public void refreshNode(final IOdysseusNode node) {
		final IOdysseusNodeResourceUsageManager usageManager = OdysseusNetRCPPlugIn.getOdysseusNodeResourceUsageManager();

		synchronized (refreshing) {
			if (refreshing.contains(node)) {
				return;
			}

			refreshing.add(node);
		}

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Future<Optional<IResourceUsage>> futureUsage = usageManager.getRemoteResourceUsage(node, false);

					try {

						Optional<IResourceUsage> optResourceUsage = futureUsage.get();
						if (optResourceUsage.isPresent()) {

							IResourceUsage resourceUsage = optResourceUsage.get();
							synchronized (usageMap) {
								usageMap.put(node, resourceUsage);
							}
							refreshTableAsync();
						}

					} catch (InterruptedException | ExecutionException e) {
						LOG.error("Could not get resource usage", e);
					}

				} finally {
					synchronized (refreshing) {
						refreshing.remove(node);
					}
				}
			}
		});

		t.setDaemon(true);
		t.setName("NodeView updater");
		t.start();
	}

	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (tableViewer) {
						if (!tableViewer.getTable().isDisposed()) {
							tableViewer.refresh();
						}
					}
				}
			});
		}
	}

	@Override
	public void nodeConnected(IOdysseusNodeConnection nodeConnection) {
		String nodeName = nodeConnection.getOdysseusNode().getName();

		synchronized (foundNodes) {
			if (!foundNodes.contains(nodeConnection.getOdysseusNode())) {
				foundNodes.add(nodeConnection.getOdysseusNode());
			}
		}

		if (nameMap.containsKey(nodeName)) {
			IOdysseusNode oldNode = nameMap.get(nodeName);
			synchronized (usageMap) {
				usageMap.remove(oldNode);
			}
			synchronized (foundNodes) {
				foundNodes.remove(oldNode);
			}
		}
		nameMap.put(nodeName, nodeConnection.getOdysseusNode());

		refreshTableAsync();
		refreshNode(nodeConnection.getOdysseusNode());
	}


	@Override
	public void nodeDisconnected(IOdysseusNodeConnection nodeConnection) {
		refreshTableAsync();
	}

	public void clear() {
		synchronized (foundNodes) {
			foundNodes.clear();
			Collection<IOdysseusNodeConnection> connections = OdysseusNetRCPPlugIn.getOdysseusNodeConnectionManager().getConnections();
			for( IOdysseusNodeConnection connection : connections ) {
				foundNodes.add(connection.getOdysseusNode());
			}

			nameMap.clear();
			for (IOdysseusNode foundNode : foundNodes) {
				nameMap.put(foundNode.getName(), foundNode);
			}

			synchronized (usageMap) {
				Collection<IOdysseusNode> nodesToRemove = Lists.newArrayList();
				for (IOdysseusNode node : usageMap.keySet()) {
					if (!foundNodes.contains(node)) {
						nodesToRemove.add(node);
					}
				}
				
				for (IOdysseusNode nodeToRemove : nodesToRemove) {
					usageMap.remove(nodeToRemove);
				}
			}
		}
		
		refreshTableAsync();
	}
	
	public int indexOf( IOdysseusNode node ) {
		Preconditions.checkNotNull(node, "node must not be null!");

		return foundNodes.indexOf(node);
	}
}
