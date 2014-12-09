package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.AbstractActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.AbstractSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.SmartDevice;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ColumnPixelData;

public class ActivityInterpreterShowView extends ViewPart {
	public ActivityInterpreterShowView() {
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(ActivityInterpreterShowView.class);
	ISmartDeviceListener smartDeviceListener = new ISmartDeviceListener() {
		@Override
		public void fieldDeviceConnected(ASmartDevice sender, FieldDevice device) {
			if (device instanceof AbstractSensor) {
				LOG.debug("ActivityInterpreterShowView fieldDeviceConnected Sensor:" + device.getName());

			} else if (device instanceof AbstractActor) {
				LOG.debug("ActivityInterpreterShowView fieldDeviceConnected Actor:" + device.getName());

			} else {
				LOG.debug("ActivityInterpreterShowView fieldDeviceConnected device:" + device.getName());

			}
			refresh();
		}

		@Override
		public void fieldDeviceRemoved(ASmartDevice smartDevice,
				FieldDevice device) {
			refresh();
		}

		@Override
		public void smartDeviceReadyStateChanged(ASmartDevice smartDevice, boolean state) {
		}

		@Override
		public void smartDevicesUpdated(ASmartDevice newSmartDevice,
				ASmartDevice oldDevice) {
			refresh();
		}
	};
	private ArrayList<ASmartDevice> refreshing = Lists.newArrayList();
	private ArrayList<ASmartDevice> foundSmartDevices = Lists.newArrayList();
	private TreeViewer treeViewer;
	private Tree tree;
	private static ActivityInterpreterShowView instance;

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		TreeColumnLayout tcl_composite = new TreeColumnLayout();
		composite.setLayout(tcl_composite);

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer,
				SWT.NONE);
		TreeColumn trclmnSmartdevice = treeViewerColumn.getColumn();
		tcl_composite.setColumnData(trclmnSmartdevice, new ColumnPixelData(150,
				true, true));
		trclmnSmartdevice.setText("SmartDevice / Sensor");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer,
				SWT.NONE);
		TreeColumn trclmnActivity = treeViewerColumn_1.getColumn();
		tcl_composite.setColumnData(trclmnActivity, new ColumnPixelData(150,
				true, true));
		trclmnActivity.setText("Activity");

		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(treeViewer,
				SWT.NONE);
		TreeColumn trclmnActivityInterpreter = treeViewerColumn_3.getColumn();
		tcl_composite.setColumnData(trclmnActivityInterpreter, new ColumnPixelData(150,
				true, true));
		trclmnActivityInterpreter.setText("ActivityInterpreterDescription");

		
		
		if (treeViewer != null && foundSmartDevices != null) {
			treeViewer.setContentProvider(new ViewContentProvider());
			treeViewer.setLabelProvider(new ViewLabelProvider());
			treeViewer.setInput(foundSmartDevices);
		} else {
			LOG.debug("something null");
		}

		refresh();
		treeViewer.refresh();

		treeViewer.expandAll();

		setPartName("Smart Device Activity Interpreter");

		refreshLoopAsync();

		instance = this;
	}

	private void refreshLoopAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						refresh();
						
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					} catch(NullPointerException ex){
						
					}
				}
			}
		});
		t.setName("Smart Device ActivityInterpreter refresher thread.");
		t.setDaemon(true);
		t.start();
	}

	public void refresh() {
		if (foundSmartDevices == null) {
			return;
		}
		ArrayList<ASmartDevice> foundSmartDevicesCopy = null;
		synchronized (foundSmartDevices) {
			foundSmartDevices.clear();
			foundSmartDevices.add(SmartHomeRCPActivator.getSmartDeviceService()
					.getLocalSmartDevice());
			foundSmartDevices.addAll(SmartHomeRCPActivator
					.getSmartDeviceService()
					.getSmartDeviceDictionaryDiscovery()
					.getFoundSmartDeviceList());
			foundSmartDevicesCopy = Lists.newArrayList(foundSmartDevices);
		}
		refreshTableAsync();

		for (final ASmartDevice remotePeerID : foundSmartDevicesCopy) {
			synchronized (refreshing) {
				if (refreshing.contains(remotePeerID)) {
					continue;
				}
				refreshing.add(remotePeerID);
			}
		}
	}

	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {
				@Override
				public void run() {
					synchronized (treeViewer) {
						if (!treeViewer.getTree().isDisposed()) {
							treeViewer.refresh();
						}
					}
				}
			});
		}
	}

	@Override
	public void setFocus() {

	}

	public static Optional<ActivityInterpreterShowView> getInstance() {
		return Optional.fromNullable(instance);
	}

	public List<AbstractActivityInterpreter> getSelectedActivityInterpreters() {
		ImmutableList.Builder<AbstractActivityInterpreter> resultBuilder = new ImmutableList.Builder<>();

		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		if (!selection.isEmpty()) {
			for (Object selectedObj : selection.toList()) {
				if (selectedObj instanceof AbstractActivityInterpreter) {
					// selection.getFirstElement();
					resultBuilder.add(((AbstractActivityInterpreter) selectedObj));
				}
			}
		}

		return resultBuilder.build();
	}

	class ViewContentProvider implements ITreeContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<ASmartDevice> list = (ArrayList<ASmartDevice>) inputElement;
				@SuppressWarnings("rawtypes")
				ASmartDevice[] devices = new ASmartDevice[((ArrayList) inputElement)
						.size()];

				int i = 0;
				for (ASmartDevice dev : list) {
					devices[i++] = dev;
				}

				// LOG.error("getElements return ASmartDevice[]");
				return devices;
			} else if (inputElement instanceof ASmartDevice) {
				ASmartDevice device = (ASmartDevice) inputElement;
				AbstractSensor[] list = new AbstractSensor[device.getConnectedSensors().size()];
				int i = 0;
				for (AbstractSensor sensor : device.getConnectedSensors()) {
					list[i++] = sensor;
				}
				LOG.error("getElements return Sensor[].length:"+list.length);
				return list;
			} else if (inputElement instanceof AbstractSensor) {
				AbstractSensor sensor = (AbstractSensor) inputElement;
				Collection<? extends AbstractActivityInterpreter> activityInterpreter = sensor.getActivityInterpreters();
				AbstractActivityInterpreter[] list = new AbstractActivityInterpreter[activityInterpreter.size()];
				int i = 0;
				for (AbstractActivityInterpreter interpret : activityInterpreter) {
					list[i++] = interpret;
				}
				LOG.error("getElements return ActivityInterpreter[].length:"+list.length);
				return list;
			} else {
				//LOG.error("getElements return null");
				return null;
			}
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof SmartDevice) {
				SmartDevice smartDevice = (SmartDevice) parentElement;
				Collection<AbstractSensor> sensors = smartDevice.getConnectedSensors();
				AbstractSensor[] list = new AbstractSensor[sensors.size()];
				int i = 0;
				for (AbstractSensor sensor : sensors) {
					list[i++] = sensor;
				}
				return list;
			} else if (parentElement instanceof AbstractSensor) {
				AbstractSensor sensor = (AbstractSensor) parentElement;
				Collection<? extends AbstractActivityInterpreter> activityInterpreters = sensor.getActivityInterpreters();
				AbstractActivityInterpreter[] list = new AbstractActivityInterpreter[activityInterpreters.size()];
				int i = 0;
				for (AbstractActivityInterpreter interpr : activityInterpreters) {
					list[i++] = interpr;
				}
				return list;
			} else if (parentElement instanceof AbstractActivityInterpreter) {
				return null;
			}

			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof ArrayList) {
				return null;
			} else if (element instanceof ASmartDevice) {
				return null;
			} else if (element instanceof AbstractSensor) {
				AbstractSensor sensor = (AbstractSensor) element;
				return sensor.getSmartDevice();
			} else if (element instanceof AbstractActivityInterpreter) {
				AbstractActivityInterpreter interpret = (AbstractActivityInterpreter) element;
				return interpret.getSensor();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof ArrayList) {
				return true;
			} else if (element instanceof ASmartDevice) {
				ASmartDevice device = (ASmartDevice) element;
				return device.getConnectedSensors().size() > 0;
			} else if (element instanceof AbstractSensor) {
				AbstractSensor sensor = (AbstractSensor) element;
				return sensor.getActivityInterpreters().size() > 0;
			}
			return false;
		}

	}

	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			StyledString text = new StyledString();

			switch (cell.getColumnIndex()) {
			case 0:
				if (cell.getElement() instanceof ASmartDevice) {// SmartDevice
					ASmartDevice device = (ASmartDevice) cell.getElement();
					if (SmartDevicePublisher.isLocalPeer(device.getPeerID())) {
						text.append("<local>" + device.getPeerName());
					} else {
						text.append(device.getPeerName());
					}
				} else if (cell.getElement() instanceof AbstractSensor) {//Sensor
					AbstractSensor sensor = (AbstractSensor) cell.getElement();
					text.append(sensor.getName());
				}
				break;
			case 1:// Activity
				if (cell.getElement() instanceof AbstractActivityInterpreter) {
					AbstractActivityInterpreter interpreter = (AbstractActivityInterpreter) cell.getElement();
					text.append(interpreter.getActivityName());
				}
				break;
			case 2:// ActivityInterpreter
				if (cell.getElement() instanceof AbstractActivityInterpreter) {
					AbstractActivityInterpreter interpret = (AbstractActivityInterpreter) cell.getElement();
					text.append(interpret.getActivityInterpreterDescription());
				}
				break;

			default:
				break;
			}

			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);
		}
	}
}
