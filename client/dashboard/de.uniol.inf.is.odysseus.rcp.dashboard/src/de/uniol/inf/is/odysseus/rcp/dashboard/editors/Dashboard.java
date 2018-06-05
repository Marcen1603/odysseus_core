/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.ControlPoint;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.ControlPointManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

public final class Dashboard implements PaintListener, MouseListener, MouseMoveListener, ISelectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(Dashboard.class);

	private static final int SELECT_MOUSE_BUTTON_ID = 1;
	private static final int SELECTION_BORDER_MARGIN_PIXELS = 3;
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();
	private static final int DEFAULT_PART_WIDTH = 500;
	private static final int DEFAULT_PART_HEIGHT = 300;

	private final PartDragger partDragger = new PartDragger();
	private final DashboardPartSelector selector = new DashboardPartSelector();
	private final DashboardPartPlacementContainer partContainer = new DashboardPartPlacementContainer();
	private final List<IDashboardListener> dashboardListeners = Lists.newArrayList();

	private DashboardControl dashboardControl;
	private ToolBar toolBar;
	private DashboardDropTarget dropTarget;
	private ControlPointManager controlPointManager;
	private DashboardKeyHandler keyHandler;
	private DashboardSettings settings = DashboardSettings.getDefault();

	public void createPartControl(Composite parent, ToolBar toolBar, final IWorkbenchPartSite site) {
		this.toolBar = toolBar;

		dashboardControl = new DashboardControl(parent);
		updateBackgroundImage();

		controlPointManager = new ControlPointManager(this);
		keyHandler = new DashboardKeyHandler(this);

		dropTarget = new DashboardDropTarget(dashboardControl.getComposite()) {
			@Override
			protected boolean isDropAllowed() {
				return !settings.isLocked();
			}

			@Override
			protected void dropDashboardPartPlacement(IFile dashboardPartFile, DropTargetEvent event) {
				try {
					IDashboardPart part = DASHBOARD_PART_HANDLER.load(dashboardPartFile, site.getPart());
					
					Point position = dashboardControl.getComposite().toControl(event.x, event.y);
					Point size = determinePreferredSize(part);
					
					DashboardPartPlacement place = new DashboardPartPlacement(part, dashboardPartFile.getFullPath().toString(), position.x, position.y, size.x, size.y);

					add(place);
				} catch (Throwable t) {
					LOG.error("Exception during dropping dashboard part placement", t);
				}
			}

		};

		addListeners();

		createPreAddedDashboardParts();
		createContextMenu(site);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
		site.setSelectionProvider(selector);

		parent.layout();
	}

	private Point determinePreferredSize(final IDashboardPart part) {
		Point size = part.getPreferredSize();
		if( size == null || size.x <= 0 || size.y <= 0 ) {
			LOG.error("Invalid preferred size delivered from part {}", part);
			size = new Point(DEFAULT_PART_WIDTH, DEFAULT_PART_HEIGHT);
		}
		return size;
	}

	public Object getAdapter(Class<?> adapter) {
		if (getSelectedDashboardPartPlacement().isPresent()) {
			return getSelectedDashboardPartPlacement().get().getDashboardPart().getAdapter(adapter);
		}
		// TODO: what to do if no one is selected?!
		for (DashboardPartPlacement placement : getDashboardPartPlacements()) {
			Object subAdapt = placement.getDashboardPart().getAdapter(adapter);
			if (subAdapt != null) {
				return subAdapt;
			}
		}
		return null;
	}

	public Control getControl() {
		return dashboardControl.getComposite();
	}

	private void createPreAddedDashboardParts() {
		for (final DashboardPartPlacement dashboardPartPlace : partContainer.getDashboardPartPlacements()) {
			insertDashboardPart(dashboardPartPlace);
		}
	}

	private void insertDashboardPart(DashboardPartPlacement dashboardPartPlace) {
		DashboardPartControl dashboardPartControl = new DashboardPartControl(dashboardControl.getComposite(), toolBar, dashboardPartPlace);
		addListenersRecursive(dashboardPartControl.getComposite());

		partContainer.addContainer(dashboardPartPlace, dashboardPartControl);
	}

	private void createContextMenu(IWorkbenchPartSite site) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(dashboardControl.getComposite());
		dashboardControl.getComposite().setMenu(contextMenu);
		site.registerContextMenu(menuManager, selector);
	}

	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		dropTarget.dispose();
		dashboardControl.dispose();
	}

	public void add(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");
		Preconditions.checkArgument(!partContainer.contains(partPlace), "Dashboard part placement %s already added!", partPlace);

		partContainer.add(partPlace);

		if (dashboardControl != null && toolBar != null) {
			insertDashboardPart(partPlace);
			dashboardControl.update();
		}

		fireAddedEvent(partPlace.getDashboardPart());
	}

	public void remove(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for dashboard part must not be null!");

		Optional<DashboardPartControl> optDashboardPartControl = partContainer.getComposite(partPlace);

		if (optDashboardPartControl.isPresent()) {
			DashboardPartControl dashboardPartControl = optDashboardPartControl.get();
			removeListenersRecursive(dashboardPartControl.getComposite());

			partContainer.remove(partPlace);
			dashboardPartControl.dispose();
			partPlace.getDashboardPart().dispose();

			if (selector.isSelected(partPlace)) {
				selector.clearSelection();
			}

			fireRemovedEvent(partPlace.getDashboardPart());
		} else {
			throw new IllegalArgumentException("Composite for dashboard part not found");
		}
	}

	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return partContainer.getDashboardPartPlacements();
	}

	public void setSettings(DashboardSettings settings) {
		Preconditions.checkNotNull(settings, "Settings to set must not be null!");

		boolean oldLock = this.settings.isLocked();
		
		this.settings = settings;
		selector.setLock(settings.isLocked());
		
		if( settings.isLocked() != oldLock ) {
			if( settings.isLocked() ) {
				fireOnLockEvent();
			} else {
				fireOnUnlockEvent();
			}
		}
	}

	private void fireOnUnlockEvent() {
		for( DashboardPartPlacement placement : partContainer.getDashboardPartPlacements() ) {
			try {
				placement.getDashboardPart().onUnlock();
			} catch( Throwable t ) {
				LOG.error("Exception during unlock-event for dashboard part");
			}
		}
	}

	private void fireOnLockEvent() {
		for( DashboardPartPlacement placement : partContainer.getDashboardPartPlacements() ) {
			try {
				placement.getDashboardPart().onLock();
			} catch( Throwable t ) {
				LOG.error("Exception during lock-event for dashboard part");
			}
		}
	}

	public DashboardSettings getSettings() {
		return settings;
	}

	private void addListenersRecursive(Control base) {
		base.addMouseListener(this);
		base.addMouseMoveListener(this);
		base.addKeyListener(keyHandler);
		if (base instanceof Composite) {
			for (final Control ctrl : ((Composite) base).getChildren()) {
				addListenersRecursive(ctrl);
			}
		}
	}

	private void removeListenersRecursive(Control base) {
		base.removeMouseListener(this);
		base.removeMouseMoveListener(this);
		base.removeKeyListener(keyHandler);
		if (base instanceof Composite) {
			for (final Control ctrl : ((Composite) base).getChildren()) {
				removeListenersRecursive(ctrl);
			}
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (!settings.isLocked() && e.button == SELECT_MOUSE_BUTTON_ID) {

			Optional<ControlPoint> optControlPoint = controlPointManager.getControlPoint(e.x, e.y);
			if (optControlPoint.isPresent()) {
				// prohibit selection change
				return;
			}

			Optional<DashboardPartPlacement> optPlace = partContainer.getByControl(e.widget);

			if (optPlace.isPresent()) {
				if (!selector.isSelected(optPlace.get())) {
					partDragger.beginDrag(optPlace.get(), e.x, e.y);
				}
				selector.setSelection(optPlace.get());
			} else {
				selector.clearSelection();
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		partDragger.endDrag();
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (partDragger.isDragging()) {
			partDragger.drag(e.x, e.y);
			update();
		}
	}

	@Override
	public void paintControl(PaintEvent e) {
		renderSelectionBorder();
	}

	public void update() {
		updateBackgroundImage();
		updateDashboardParts();

		fireChangedEvent();
	}

	private void updateBackgroundImage() {
		dashboardControl.setBackgroundImageFile(settings.getBackgroundImageFile(), settings.isBackgroundImageStretched());
	}

	private void updateDashboardParts() {
		for (DashboardPartPlacement partPlace : partContainer.getDashboardPartPlacements()) {
			update(partPlace);
		}
	}

	public boolean setFocus() {
		return dashboardControl.getComposite().setFocus();
	}

	public Optional<DashboardPartPlacement> getSelectedDashboardPartPlacement() {
		return selector.getSelectedDashboardPartPlacement();
	}

	private void addListeners() {
		dashboardControl.getComposite().addMouseListener(this);
		dashboardControl.getComposite().addPaintListener(this);
	}

	public void addListener(IDashboardListener listener) {
		Preconditions.checkNotNull(listener, "Dashboardlistener to add must not be null!");

		synchronized (dashboardListeners) {
			dashboardListeners.add(listener);
		}
	}

	public void removeListener(IDashboardListener listener) {
		synchronized (dashboardListeners) {
			dashboardListeners.remove(listener);
		}
	}

	private void fireAddedEvent(IDashboardPart part) {
		synchronized (dashboardListeners) {
			for (final IDashboardListener listener : dashboardListeners) {
				try {
					listener.dashboardPartAdded(this, part);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void fireChangedEvent() {
		synchronized (dashboardListeners) {
			for (final IDashboardListener listener : dashboardListeners) {
				try {
					listener.dashboardChanged(this);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void fireRemovedEvent(IDashboardPart part) {
		synchronized (dashboardListeners) {
			for (final IDashboardListener listener : dashboardListeners) {
				try {
					listener.dashboardPartRemoved(this, part);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void renderSelectionBorder() {
		Optional<DashboardPartPlacement> optSelectedDashboardPart = selector.getSelectedDashboardPartPlacement();
		if (optSelectedDashboardPart.isPresent()) {
			final DashboardPartPlacement selectedDashboardPart = optSelectedDashboardPart.get();
			final int x = selectedDashboardPart.getX();
			final int y = selectedDashboardPart.getY();
			final int width = selectedDashboardPart.getWidth();
			final int height = selectedDashboardPart.getHeight();

			final GC gc = new GC(dashboardControl.getComposite());
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(3);
			gc.drawRectangle(x - SELECTION_BORDER_MARGIN_PIXELS, y - SELECTION_BORDER_MARGIN_PIXELS, width + SELECTION_BORDER_MARGIN_PIXELS * 2, height + SELECTION_BORDER_MARGIN_PIXELS * 2);

			controlPointManager.render(gc);
		}
	}

	private void update(DashboardPartPlacement placement) {
		Optional<DashboardPartControl> optDashboardPartControl = partContainer.getComposite(placement);
		if (optDashboardPartControl.isPresent()) {
			DashboardPartControl dashboardPartControl = optDashboardPartControl.get();
			dashboardPartControl.update();
		} else {
			throw new IllegalArgumentException("Dashboardpart placement has no composite");
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		selector.setSelection(selection, false);
		dashboardControl.update();
	}

	public boolean hasSelection() {
		return selector.hasSelection();
	}

	public IStructuredSelection getSelection() {
		return selector.getSelection();
	}
}
