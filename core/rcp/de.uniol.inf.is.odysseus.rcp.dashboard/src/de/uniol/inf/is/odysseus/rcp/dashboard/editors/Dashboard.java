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
import java.util.Objects;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.ControlPoint;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.ctrlPoint.ControlPointManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.ImageUtil;

public final class Dashboard implements PaintListener, MouseListener, MouseMoveListener, ISelectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(Dashboard.class);

	private static final int SELECT_MOUSE_BUTTON_ID = 1; 
	private static final int SELECTION_BORDER_MARGIN_PIXELS = 3;

	private Composite dashboardComposite;
	private ToolBar toolBar;
	private DashboardDropTarget dropTarget;
	
	private final PartDragger partDragger = new PartDragger();
	private final DashboardPartSelector selector = new DashboardPartSelector();
	private final DashboardPartPlacementContainer partContainer = new DashboardPartPlacementContainer();

	private final List<IDashboardListener> dashboardListeners = Lists.newArrayList();

	private ControlPointManager controlPointManager;
	private DashboardKeyHandler keyHandler;
	private DashboardSettings settings = DashboardSettings.getDefault();

	private IFile loadedBackgroundImageFile;
	private Image loadedBackgroundImage;
	private boolean isLoadedBackgroundImageStretched = false;
	
	public void add(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for Dashboard Part must not be null!");
		Preconditions.checkArgument(!partContainer.contains(partPlace), "Dashboard part placement %s already added!", partPlace);

		partContainer.add(partPlace);

		if (dashboardComposite != null && toolBar != null) {
			insertDashboardPart(partPlace);
			dashboardComposite.layout();
		}

		fireAddedEvent(partPlace.getDashboardPart());
	}

	public void remove(DashboardPartPlacement partPlace) {
		Preconditions.checkNotNull(partPlace, "Placement for dashboard part must not be null!");
		
		Optional<Composite> optComposite = partContainer.getComposite(partPlace);

		if (optComposite.isPresent()) {
			Composite composite = optComposite.get();
			partContainer.remove(partPlace);

			removeListenersRecursive(composite);
			composite.dispose();
			partPlace.getDashboardPart().dispose();
			
			if( selector.isSelected(partPlace)) {
				selector.clearSelection();
			}

			fireRemovedEvent(partPlace.getDashboardPart());
		} else {
			throw new IllegalArgumentException("Composite for dashboard part not found");
		}
	}

	public void setSettings(DashboardSettings settings) {
		Preconditions.checkNotNull(settings, "Settings to set must not be null!");

		this.settings = settings;
		selector.setLock(settings.isLocked());
	}

	public DashboardSettings getSettings() {
		return settings;
	}

	public void createPartControl(Composite parent, ToolBar toolBar, IWorkbenchPartSite site) {
		this.toolBar = toolBar;
		
		dashboardComposite = createTopComposite(parent);
		controlPointManager = new ControlPointManager(this);
		keyHandler = new DashboardKeyHandler(this);
		
		dropTarget = new DashboardDropTarget(dashboardComposite) {
			@Override
			protected boolean isDropAllowed() {
				return !settings.isLocked();
			}

			@Override
			protected void dropDashboardPartPlacement(DashboardPartPlacement place) {
				add(place);
			}
		};
		
		addListeners();

		createPreAddedDashboardParts();
		createContextMenu(site);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(this);
		site.setSelectionProvider(selector);

		updateBackgroundImage();
		parent.layout();
	}

	public Control getControl() {
		return dashboardComposite;
	}
	
	private void createPreAddedDashboardParts() {
		for (final DashboardPartPlacement dashboardPartPlace : partContainer.getDashboardPartPlacements()) {
			insertDashboardPart(dashboardPartPlace);
		}
	}

	private void insertDashboardPart(DashboardPartPlacement dashboardPartPlace) {
		final Composite outerContainer = createDashboardPartOuterContainer(dashboardComposite, dashboardPartPlace);
		final Composite innerContainer = getDashboardPartInnerContainer(outerContainer);
		dashboardPartPlace.getDashboardPart().createPartControl(innerContainer, toolBar);

		partContainer.addContainer(dashboardPartPlace, outerContainer);

		addListenersRecursive(outerContainer);
	}

	private void createContextMenu(IWorkbenchPartSite site) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(dashboardComposite);
		dashboardComposite.setMenu(contextMenu);
		site.registerContextMenu(menuManager, selector);
	}

	private static Composite createTopComposite(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.BORDER);
		topComposite.setLayout(new FormLayout());
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		return topComposite;
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

	public void dispose() {
		keyHandler.dispose();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		dropTarget.dispose();
		disposeBackgroundImage();
	}

	public ImmutableList<DashboardPartPlacement> getDashboardPartPlacements() {
		return partContainer.getDashboardPartPlacements();
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
				addListenersRecursive(ctrl);
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

	private void updateDashboardParts() {
		for (DashboardPartPlacement partPlace : partContainer.getDashboardPartPlacements()) {
			update(partPlace);
		}
	}

	private void updateBackgroundImage() {
		if (isImageSettingsChanged()) {
			disposeBackgroundImage();

			try {
				if (settings.getBackgroundImageFile() != null) {
					Image image = new Image(Display.getCurrent(), settings.getBackgroundImageFile().getContents());

					if (settings.isBackgroundImageStretched()) {
						Image stretchedImage = ImageUtil.resizeImage(image, dashboardComposite.getSize().x, dashboardComposite.getSize().y);
						image.dispose();
						image = stretchedImage;
					}
					isLoadedBackgroundImageStretched = settings.isBackgroundImageStretched();

					dashboardComposite.setBackgroundImage(image);
					loadedBackgroundImageFile = settings.getBackgroundImageFile();
					loadedBackgroundImage = image;
				} else {
					dashboardComposite.setBackgroundImage(null);
					loadedBackgroundImageFile = null;
					loadedBackgroundImage = null;
				}

			} catch (CoreException e) {
				LOG.error("Could not load background image", e);
			}
		}
	}

	private boolean isImageSettingsChanged() {
		return !Objects.equals(loadedBackgroundImageFile, settings.getBackgroundImageFile()) 
				|| settings.isBackgroundImageStretched() != isLoadedBackgroundImageStretched;
	}

	public boolean setFocus() {
		return dashboardComposite.setFocus();
	}

	public Optional<DashboardPartPlacement> getSelectedDashboardPartPlacement() {
		return selector.getSelectedDashboardPartPlacement();
	}

	private void disposeBackgroundImage() {
		if( loadedBackgroundImage != null ) {
			loadedBackgroundImage.dispose();
			loadedBackgroundImage = null;
		}
	}

	private void addListeners() {
		dashboardComposite.addMouseListener(this);
		dashboardComposite.addPaintListener(this);
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

			final GC gc = new GC(dashboardComposite);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(3);
			gc.drawRectangle(x - SELECTION_BORDER_MARGIN_PIXELS, y - SELECTION_BORDER_MARGIN_PIXELS, width + SELECTION_BORDER_MARGIN_PIXELS * 2, height + SELECTION_BORDER_MARGIN_PIXELS * 2);

			controlPointManager.render(gc);
		}
	}

	private void update(DashboardPartPlacement placement) {
		Optional<Composite> optComp = partContainer.getComposite(placement);
		if (optComp.isPresent()) {
			Composite comp = optComp.get();

			final FormData fd = (FormData) comp.getLayoutData();
			updateFormData(fd, placement);
			dashboardComposite.layout();
			dashboardComposite.redraw();
		} else {
			throw new IllegalArgumentException("Dashboardpart placement has no composite");
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		selector.setSelection(selection, false);
		dashboardComposite.redraw();
	}

	public boolean hasSelection() {
		return selector.hasSelection();
	}

	public IStructuredSelection getSelection() {
		return selector.getSelection();
	}
}
