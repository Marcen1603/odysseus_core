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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.CopyAction;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.GraphPalette;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.PasteAction;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Connection;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.GraphicsLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part.GraphicalEditPartFactory;

@SuppressWarnings("deprecation")
public class DashboardGraphicsPart extends AbstractDashboardPart implements
		CommandStackListener, ISelectionListener, Observer {

	private static final String BACKGROUND_FILE = "BACKGROUND_FILE";
	private static final String BACKGROUND_FILE_STRETCH = "BACKGROUND_FILE_STRETCH";
	private static final String GRAPHICS_CONTENT = "graphicscontent";

	private String backgroundfile;
	private Display display;
	private ActionRegistry actionRegistry;
	// private Collection<IPhysicalOperator> roots;
	private Canvas mainContainer;

	private EditDomain editDomain;
	private ScrollingGraphicalViewer viewer;
	private GraphicsLayer pictogramGroup;
	private boolean backgroundFileStretch = true;
	private boolean isModelInitialized = false;
	private PaletteViewerProvider provider;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {

		display = parent.getDisplay();
		mainContainer = new Canvas(parent, SWT.NONE);

		mainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		mainContainer.setLayout(new FillLayout());
		mainContainer.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		initModel();

		createGraphViewer(mainContainer);
		createPaletteViewer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart#onStart(
	 * java.util.Collection)
	 */
	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		this.pictogramGroup.open(physicalRoots);
		super.onStart(physicalRoots);
	}

	private void createGraphViewer(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.createControl(parent);
		viewer.getControl().setBackground(ColorConstants.white);
		viewer.setEditPartFactory(new GraphicalEditPartFactory());

		viewer.setContents(getRootPictogramGroup());
		editDomain.addViewer(viewer);
		getWorkbenchPart().getSite().setSelectionProvider(viewer);
		configureGraphicalViewer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart#dispose()
	 */
	@Override
	public void dispose() {
		editDomain.getCommandStack().removeCommandStackListener(this);
		super.dispose();
	}

	private GraphicsLayer getRootPictogramGroup() {
		return pictogramGroup;
	}

	private void createPaletteViewer() {
		editDomain.setPaletteRoot(getPaletteRoot());
	}

	private PaletteRoot getPaletteRoot() {
		return GraphPalette.createGraphPalette();
	}

	@Override
	public void streamElementRecieved(final IPhysicalOperator senderOperator,
			final IStreamObject<?> element, final int port) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				processElement(senderOperator, element);
			}
		});
	}

	protected void processElement(IPhysicalOperator senderOperator,
			IStreamObject<?> element) {
		// System.out.println(element);
		Tuple<?> tuple = (Tuple<?>) element;
		this.pictogramGroup.processTuple(senderOperator, tuple);
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {

	}

	@Override
	public void onLoad(Map<String, String> saved) {
		super.onLoad(saved);
		setBackgroundFile(saved.get(BACKGROUND_FILE));
		setBackgroundFileStretch(Boolean.parseBoolean(saved
				.get(BACKGROUND_FILE_STRETCH)));
		// Hint: Do not create a new GraphicsLayer because images will not be
		// loaded again!
		if (this.pictogramGroup == null) {
			this.pictogramGroup = new GraphicsLayer(getBackgroundFile(),
					isBackgroundFileStretch(), getProject());
			this.pictogramGroup.addObserver(this);
		}else{
			this.pictogramGroup.setBackgroundImagePath(getBackgroundFile());
			this.pictogramGroup.setBackgroundImageStretch(isBackgroundFileStretch());
			this.pictogramGroup.setProject(getProject());
		}
		// this part is only for downward compatibility...
		String xmlContent = saved.get(GRAPHICS_CONTENT);
		try {
			if (!(xmlContent == null) && !xmlContent.isEmpty()) {
				InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				onLoadXML(doc, doc.getDocumentElement());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoadXML(Document document, Element xmlElement) {
		if (xmlElement.getChildNodes().getLength() > 0) {
			NodeList pictogramList = xmlElement.getChildNodes();

			if (!xmlElement.getNodeName().equals("pictogramgroup")) {
				pictogramList = xmlElement.getChildNodes().item(0)
						.getChildNodes();
			}

			try {
				for (int i = 0; i < pictogramList.getLength(); i++) {
					Node pictogramNode = pictogramList.item(i);
					if (pictogramNode.getNodeType() == Node.ELEMENT_NODE) {
						String className = ((Element) pictogramNode)
								.getAttribute("type");
						if (className.isEmpty()) {
							className = ImagePictogram.class.getName();
						}

						AbstractPart part = (AbstractPart) Class.forName(
								className).newInstance();
						part.loadFromXML(pictogramNode, this.pictogramGroup);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSaveXML(Document doc, Element xmlElement) {
		Element root = doc.createElement("pictogramgroup");
		xmlElement.appendChild(root);
		if (viewer != null) {
			GraphicsLayer model = (GraphicsLayer) viewer.getContents()
					.getModel();
			List<Connection> connections = new ArrayList<>();
			for (AbstractPictogram p : model.getPictograms()) {
				Element picNode = doc.createElement("pictogram");
				picNode.setAttribute("type", p.getClass().getName());
				p.getXML(picNode, doc);
				root.appendChild(picNode);
				connections.addAll(p.getSourceConnections());
			}
			for (Connection c : connections) {
				Element conNode = doc.createElement("connection");
				conNode.setAttribute("type", c.getClass().getName());
				c.getXML(conNode, doc);
				root.appendChild(conNode);
			}
		}
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> save = super.onSave();

		save.put(BACKGROUND_FILE, this.backgroundfile);
		save.put(BACKGROUND_FILE_STRETCH,
				Boolean.toString(this.backgroundFileStretch));

		if (editDomain != null) {
			editDomain.getCommandStack().markSaveLocation();
		}
		return save;
	}

	public String getBackgroundFile() {
		return backgroundfile;
	}

	public void setBackgroundFile(String backgroundfile) {
		this.backgroundfile = backgroundfile;
		repaintBackground();
	}

	/**
	 *
	 */
	private void repaintBackground() {
		if (this.pictogramGroup != null) {
			this.pictogramGroup.setBackgroundImagePath(backgroundfile);
			this.pictogramGroup
					.setBackgroundImageStretch(backgroundFileStretch);
		}
	}

	/**
	 * @param selection
	 */
	public void setBackgroundFileStretch(boolean selection) {
		this.backgroundFileStretch = selection;
		repaintBackground();
	}

	public boolean isBackgroundFileStretch() {
		return this.backgroundFileStretch;
	}

	private void initModel() {
		if (!isModelInitialized) {
			editDomain = new DefaultEditDomain((IEditorPart) getWorkbenchPart());
			initActionRegistry();
			editDomain.getCommandStack().addCommandStackListener(this);
			getWorkbenchPart().getSite().getWorkbenchWindow()
					.getSelectionService().addSelectionListener(this);
		}
	}

	private void initActionRegistry() {
		IWorkbenchPart part = getWorkbenchPart();
		actionRegistry = new ActionRegistry();
		actionRegistry.registerAction(new UndoAction(part));
		actionRegistry.registerAction(new RedoAction(part));
		actionRegistry.registerAction(new DeleteAction(part));
		actionRegistry.registerAction(new CopyAction(part));
		actionRegistry.registerAction(new PasteAction(part));
	}

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	protected void configureGraphicalViewer() {
		// default actions
		registerAndBindingService("org.eclipse.ui.edit.undo", new UndoAction(
				getWorkbenchPart()));
		registerAndBindingService("org.eclipse.ui.edit.redo", new RedoAction(
				getWorkbenchPart()));
		registerAndBindingService("org.eclipse.ui.edit.delete",
				new DeleteAction(getWorkbenchPart()));
		registerAndBindingService("org.eclipse.ui.edit.copy", new CopyAction(
				getWorkbenchPart()));
		registerAndBindingService("org.eclipse.ui.edit.paste", new PasteAction(
				getWorkbenchPart()));

		// zooming
		ZoomManager zoomManager = ((ScalableFreeformRootEditPart) viewer
				.getRootEditPart()).getZoomManager();
		registerAndBindingService(new ZoomInAction(zoomManager));
		registerAndBindingService(new ZoomOutAction(zoomManager));
		List<String> zoomContributions = Arrays.asList(new String[] {
				ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT,
				ZoomManager.FIT_WIDTH });
		zoomManager.setZoomLevelContributions(zoomContributions);
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// layouting
		registerAndBindingService(GEFActionConstants.ALIGN_LEFT,
				new AlignmentAction(getWorkbenchPart(), PositionConstants.LEFT));
		registerAndBindingService(GEFActionConstants.ALIGN_CENTER,
				new AlignmentAction(getWorkbenchPart(),
						PositionConstants.CENTER));
		registerAndBindingService(
				GEFActionConstants.ALIGN_RIGHT,
				new AlignmentAction(getWorkbenchPart(), PositionConstants.RIGHT));
		registerAndBindingService(GEFActionConstants.ALIGN_TOP,
				new AlignmentAction(getWorkbenchPart(), PositionConstants.TOP));
		registerAndBindingService(GEFActionConstants.ALIGN_MIDDLE,
				new AlignmentAction(getWorkbenchPart(),
						PositionConstants.MIDDLE));
		registerAndBindingService(GEFActionConstants.ALIGN_BOTTOM,
				new AlignmentAction(getWorkbenchPart(),
						PositionConstants.BOTTOM));
	}

	private void registerAndBindingService(IAction action) {
		registerAndBindingService(action.getActionDefinitionId(), action);
	}

	private void registerAndBindingService(String actionDefinitionId,
			IAction action) {
		getActionRegistry().registerAction(action);
		IHandlerService service = getWorkbenchPart().getSite().getService(IHandlerService.class);
		service.activateHandler(actionDefinitionId, new ActionHandler(action));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java
	 * .util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		updateActions();
	}

	/**
	 *
	 */
	private void updateActions() {
		Iterator<?> iterator = actionRegistry.getActions();
		while (iterator.hasNext()) {
			Object action = iterator.next();
			if (action instanceof UpdateAction) {

				UpdateAction updateAction = (UpdateAction) action;
				updateAction.update();
			}
		}
		if (editDomain.getCommandStack().isDirty()) {
			fireChangeEvent();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == CommandStack.class) {
			return (T) editDomain.getCommandStack();
		}
		if (adapter == ActionRegistry.class) {
			return (T) getActionRegistry();
		}
		if (adapter == ZoomManager.class) {
			return (T) ((ScalableFreeformRootEditPart) viewer.getRootEditPart())
					.getZoomManager();
		}
		if (adapter == PalettePage.class) {
			return (T) createPalettePage();

		}
		return super.getAdapter(adapter);
	}

	/**
	 * @return
	 */
	private Object createPalettePage() {
		return new PaletteViewerPage(getPaletteViewerProvider());
	}

	/**
	 * @return
	 */
	private PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = new PaletteViewerProvider(editDomain);
		return provider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		try {
			if (part instanceof DashboardGraphicsPart) {
				updateActions();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg1) {
		if (o instanceof GraphicsLayer) {
			fireChangeEvent();
		}

	}

}
