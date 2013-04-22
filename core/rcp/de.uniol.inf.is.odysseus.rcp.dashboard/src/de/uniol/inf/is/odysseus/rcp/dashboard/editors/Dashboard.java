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
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public final class Dashboard implements PaintListener, MouseListener, KeyListener, ISelectionListener, ISelectionProvider {

	private static final Logger LOG = LoggerFactory.getLogger(Dashboard.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();
	private static final int DEFAULT_PART_WIDTH = 500;
	private static final int DEFAULT_PART_HEIGHT = 300;

	// 1 = LMB, 2 = MMB, 3 = RMB
	private static final int SELECT_MOUSE_BUTTON_ID = 1;
	private static final int SELECTION_BORDER_MARGIN_PIXELS = 3;
	private static final int MOVE_SELECTION_STEP_SIZE_PIXELS = 10;
	private static final int RESIZE_SELECTION_STEP_SIZE_PIXELS = 10;

	private Composite dashboardComposite;
	private ToolBar toolBar;
	private DropTarget dropTarget;
	private boolean isLocked = false;

	private final List<DashboardPartPlacement> dashboardParts = Lists.newArrayList();
	private IStructuredSelection selectedDashboardPart;
	private final Map<Control, DashboardPartPlacement> controlsMap = Maps.newHashMap();
	private final Map<DashboardPartPlacement, Composite> containers = Maps.newHashMap();

	private final List<IDashboardListener> listeners = Lists.newArrayList();
	private final List<ISelectionChangedListener> selectionChangedListeners = Lists.newArrayList();

	public void add(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");
		Preconditions.checkArgument(!dashboardParts.contains(partPlace), "Dashboard part placement %s already added!", partPlace);

		dashboardParts.add(partPlace);

		if (dashboardComposite != null && toolBar != null) {
			insertDashboardPart(partPlace);
			dashboardComposite.layout();
		}

		fireAddedEvent(partPlace.getDashboardPart());
	}

	public void addListener(IDashboardListener listener) {
		Preconditions.checkNotNull(listener, "Dashboardlistener to add must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!selectionChangedListeners.contains(listener)) {
			selectionChangedListeners.add(listener);
		}
	}

	public void createPartControl(Composite parent, ToolBar toolBar) {
		this.toolBar = toolBar;

		dashboardComposite = new Composite(parent, SWT.BORDER);
		dashboardComposite.setLayout(new FormLayout());
		dashboardComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		dashboardComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		dashboardComposite.addMouseListener(this);
		dashboardComposite.addKeyListener(this);
		dashboardComposite.addPaintListener(this);

		for (final DashboardPartPlacement dashboardPartPlace : dashboardParts) {
			insertDashboardPart(dashboardPartPlace);
		}

		if (dropTarget != null) {
			dropTarget.dispose();
			dropTarget = null;
		}

		dropTarget = new DropTarget(dashboardComposite, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				if(!isLocked()) {
					processDropEvent(event);
				}
			}
		});

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);

		parent.layout();
	}

	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);

		if (dropTarget != null) {
			dropTarget.dispose();
		}
	}
	
	public boolean isLocked() {
		return isLocked;
	}
	
	public void setLock( boolean lock ) {
		isLocked = lock;
	}

	public Control getControl() {
		return dashboardComposite;
	}

	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return ImmutableList.copyOf(dashboardParts);
	}

	@Override
	public ISelection getSelection() {
		if (isLocked() || selectedDashboardPart == null) {
			return StructuredSelection.EMPTY;
		}
		return selectedDashboardPart;
	}

	public boolean hasSelection() {
		return !isLocked() && selectedDashboardPart != null;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isLocked() && hasSelection() && (e.stateMask & SWT.CTRL) != 0) {
			DashboardPartPlacement selectedDashboardPart = getSelectedDashboardPart();

			if (e.keyCode == SWT.DEL) {
				remove(selectedDashboardPart);
				setSelection((DashboardPartPlacement) null);

			} else if ((e.stateMask & SWT.SHIFT) != 0) {

				if (e.keyCode == SWT.ARROW_UP) {
					selectedDashboardPart.setHeight(selectedDashboardPart.getHeight() - RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_DOWN) {
					selectedDashboardPart.setHeight(selectedDashboardPart.getHeight() + RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_LEFT) {
					selectedDashboardPart.setWidth(selectedDashboardPart.getWidth() - RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					selectedDashboardPart.setWidth(selectedDashboardPart.getWidth() + RESIZE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.TAB) {
					final int pos = dashboardParts.indexOf(selectedDashboardPart);
					final int newPos = (pos + 1) % dashboardParts.size();
					selectedDashboardPart = dashboardParts.get(newPos);
					dashboardComposite.redraw();
					fireChangedEvent();
				}
			} else {
				if (e.keyCode == SWT.ARROW_UP) {
					selectedDashboardPart.setY(selectedDashboardPart.getY() - MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_DOWN) {
					selectedDashboardPart.setY(selectedDashboardPart.getY() + MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_LEFT) {
					selectedDashboardPart.setX(selectedDashboardPart.getX() - MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					selectedDashboardPart.setX(selectedDashboardPart.getX() + MOVE_SELECTION_STEP_SIZE_PIXELS);
					updateSelection();

				} else if (e.keyCode == SWT.TAB) {
					final int pos = dashboardParts.indexOf(selectedDashboardPart);
					final int newPos = (pos + 1) % dashboardParts.size();
					selectedDashboardPart = dashboardParts.get(newPos);
					dashboardComposite.redraw();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (!isLocked() && e.button == SELECT_MOUSE_BUTTON_ID) {
			setSelection(controlsMap.get(e.widget));
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// do nothing
	}

	@Override
	public void paintControl(PaintEvent e) {
		renderSelectionBorder();
	}
	
	public void update() {
		for( DashboardPartPlacement partPlace : dashboardParts) {
			update(partPlace);
		}
		fireChangedEvent();
	}

	public void remove(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Dashboardpart to remove (as placement) must not be null!");

		final Composite compToRemove = containers.get(partPlace);
		removeListeners(compToRemove);

		partPlace.getDashboardPart().dispose();
		compToRemove.dispose();

		dashboardParts.remove(partPlace);

		fireRemovedEvent(partPlace.getDashboardPart());
	}

	public void removeListener(IDashboardListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			final IStructuredSelection structSelection = (IStructuredSelection) selection;
			final Object selectedObject = structSelection.getFirstElement();
			if (selectedObject instanceof DashboardPartPlacement) {
				final DashboardPartPlacement placement = (DashboardPartPlacement) selectedObject;
				if (dashboardParts.contains(placement)) {
					selectedDashboardPart = structSelection;
					dashboardComposite.redraw();
					return;
				}
			}
		}

		selectedDashboardPart = null;
		if (!dashboardComposite.isDisposed()) {
			dashboardComposite.redraw();
		}
	}

	public boolean setFocus() {
		return dashboardComposite.setFocus();
	}

	@Override
	public void setSelection(ISelection selection) {
		if (selection.isEmpty()) {
			selectedDashboardPart = null;
			dashboardComposite.redraw();

			for (final ISelectionChangedListener listener : selectionChangedListeners) {
				if (listener != null) {
					listener.selectionChanged(new SelectionChangedEvent(this, selection));
				}
			}
		}

		if (selection instanceof IStructuredSelection) {
			final Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof DashboardPartPlacement) {
				selectedDashboardPart = (IStructuredSelection) selection;
				dashboardComposite.redraw();

				for (final ISelectionChangedListener listener : selectionChangedListeners) {
					if (listener != null) {
						listener.selectionChanged(new SelectionChangedEvent(this, selection));
					}
				}
			}
		}
	}

	private void addListeners(Control base) {
		base.addMouseListener(this);
		base.addKeyListener(this);
		if (base instanceof Composite) {
			for (final Control ctrl : ((Composite) base).getChildren()) {
				addListeners(ctrl);
			}
		}
	}

	private void fireAddedEvent(IDashboardPart part) {
		synchronized (listeners) {
			for (final IDashboardListener listener : listeners) {
				try {
					listener.dashboardPartAdded(this, part);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void fireChangedEvent() {
		synchronized (listeners) {
			for (final IDashboardListener listener : listeners) {
				try {
					listener.dashboardChanged(this);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private void fireRemovedEvent(IDashboardPart part) {
		synchronized (listeners) {
			for (final IDashboardListener listener : listeners) {
				try {
					listener.dashboardPartRemoved(this, part);
				} catch (final Throwable t) {
					LOG.error("Exception during executing DashboardListener", t);
				}
			}
		}
	}

	private DashboardPartPlacement getSelectedDashboardPart() {
		final ISelection selection = getSelection();
		return selection.isEmpty() ? null : (DashboardPartPlacement) (((IStructuredSelection) selection).getFirstElement());
	}

	private void insertDashboardPart(DashboardPartPlacement dashboardPartPlace) {
		final Composite outerContainer = createDashboardPartOuterContainer(dashboardComposite, dashboardPartPlace);
		containers.put(dashboardPartPlace, outerContainer);

		final Composite innerContainer = getDashboardPartInnerContainer(outerContainer);

		dashboardPartPlace.getDashboardPart().createPartControl(innerContainer, toolBar);

		addControlsToMap(outerContainer, dashboardPartPlace, controlsMap);

		addListeners(outerContainer);
	}

	private void processDropEvent(DropTargetEvent event) {
		try {
			if (event.data instanceof IStructuredSelection) {
				final IStructuredSelection selection = (IStructuredSelection) event.data;
				if (selection.size() == 1) {
					final Object selectedObject = selection.getFirstElement();
					if (selectedObject instanceof IFile) {
						final IFile selectedFile = (IFile) selectedObject;
						if (selectedFile.getFileExtension().equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {
							final Point position = dashboardComposite.toControl(event.x, event.y);
							final IDashboardPart part = DASHBOARD_PART_HANDLER.load(FileUtil.read(selectedFile));
							final DashboardPartPlacement place = new DashboardPartPlacement(part, selectedFile.getFullPath().toString(), position.x, position.y, DEFAULT_PART_WIDTH,
									DEFAULT_PART_HEIGHT);

							add(place);
						}
					}
				}
			}
		} catch (final Throwable ex) {
			LOG.error("Could not load dashboard part", ex);
		}
	}

	private void removeListeners(Control base) {
		base.removeMouseListener(this);
		base.removeKeyListener(this);
		if (base instanceof Composite) {
			for (final Control ctrl : ((Composite) base).getChildren()) {
				addListeners(ctrl);
			}
		}
	}

	private void renderSelectionBorder() {
		if (hasSelection()) {
			final DashboardPartPlacement selectedDashboardPart = getSelectedDashboardPart();
			final int x = selectedDashboardPart.getX();
			final int y = selectedDashboardPart.getY();
			final int w = selectedDashboardPart.getWidth();
			final int h = selectedDashboardPart.getHeight();

			final GC gc = new GC(dashboardComposite);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(3);
			gc.drawRectangle(x - SELECTION_BORDER_MARGIN_PIXELS, y - SELECTION_BORDER_MARGIN_PIXELS, w + SELECTION_BORDER_MARGIN_PIXELS * 2, h + SELECTION_BORDER_MARGIN_PIXELS * 2);
		}
	}

	private void setSelection(DashboardPartPlacement placement) {
		if (placement == null) {
			setSelection(StructuredSelection.EMPTY);
		} else {
			setSelection(new StructuredSelection(placement));
		}
	}

	private void updateSelection() {
		update( getSelectedDashboardPart() );
		fireChangedEvent();
	}
	
	private void update( DashboardPartPlacement placement ) {
		final Composite comp = containers.get(placement);
		final FormData fd = (FormData) comp.getLayoutData();
		updateFormData(fd, placement);
		dashboardComposite.layout();
		dashboardComposite.redraw();
	}

	private static void addControlsToMap(Control innerContainer, DashboardPartPlacement placement, Map<Control, DashboardPartPlacement> controlsMap) {
		controlsMap.put(innerContainer, placement);

		if (innerContainer instanceof Composite) {
			final Composite comp = (Composite) innerContainer;
			for (final Control compControl : comp.getChildren()) {
				addControlsToMap(compControl, placement, controlsMap);
			}
		}
	}

	private static Composite createDashboardPartOuterContainer(Composite dashboardComposite, DashboardPartPlacement dashboardPartPlace) {
		final Composite container = new Composite(dashboardComposite, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		final FormData fd = new FormData();
		updateFormData(fd, dashboardPartPlace);
		container.setLayoutData(fd);
		return container;
	}

	private static Composite getDashboardPartInnerContainer(Composite container) {
		final Composite containerDummy = new Composite(container, SWT.NONE);
		containerDummy.setLayout(new GridLayout());
		containerDummy.setLayoutData(new GridData(GridData.FILL_BOTH));
		containerDummy.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		return containerDummy;
	}

	private static void updateFormData(FormData fd, DashboardPartPlacement dashboardPartPlace) {
		fd.height = dashboardPartPlace.getHeight();
		fd.width = dashboardPartPlace.getWidth();
		fd.top = new FormAttachment(0, dashboardPartPlace.getY());
		fd.left = new FormAttachment(0, dashboardPartPlace.getX());
		fd.bottom = new FormAttachment(0, dashboardPartPlace.getY() + fd.height);
		fd.right = new FormAttachment(0, dashboardPartPlace.getX() + fd.width);
	}
}
