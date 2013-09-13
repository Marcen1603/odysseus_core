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
import java.io.StringWriter;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.command.GraphPalette;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.PictogramGroup;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.part.GraphicalEditPartFactory;

public class DashboardGraphicsPart extends AbstractDashboardPart implements CommandStackListener, ISelectionListener {

	private static final String BACKGROUND_FILE = "BACKGROUND_FILE";
	private static final String GRAPHICS_CONTENT = "graphicscontent";

	private String backgroundfile;
	private Display display;
	private ActionRegistry actionRegistry;
//	private Collection<IPhysicalOperator> roots;
//	private Tuple<?> lastTuple = null;
	private Canvas mainContainer;

	private EditDomain editDomain;
	private ScrollingGraphicalViewer viewer;
	private PictogramGroup pictogramGroup;	

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		display = parent.getDisplay();
		mainContainer = new Canvas(parent, SWT.NONE);

		mainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		mainContainer.setLayout(new FillLayout());
		mainContainer.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		if (getWorkbenchPart() instanceof IEditorPart) {
			editDomain = new DefaultEditDomain((IEditorPart) getWorkbenchPart());
		} else {
			editDomain = new DefaultEditDomain((IEditorPart) getWorkbenchPart());
		}

		SashForm form = new SashForm(mainContainer, SWT.HORIZONTAL);
		createPaletteViewer(form);
		createGraphViewer(form);
		form.setWeights(new int[] { 15, 85 });
		initActionRegistry();
		editDomain.getCommandStack().addCommandStackListener(this);
		getWorkbenchPart().getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		loadActionBars();
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart#dispose()
	 */
	@Override
	public void dispose() {
		editDomain.getCommandStack().removeCommandStackListener(this);
		super.dispose();
	}

	/**
	 * @return
	 */
	private PictogramGroup getRootPictogramGroup() {
		return pictogramGroup;
	}

	private void createPaletteViewer(Composite parent) {
		PaletteViewer viewer = new PaletteViewer();
		viewer.createControl(parent);
		editDomain.setPaletteViewer(viewer);
		editDomain.setPaletteRoot(new GraphPalette());
	}

	@Override
	public void streamElementRecieved(final IStreamObject<?> element, final int port) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				processElement(element, port);
			}
		});
	}

	protected void processElement(IStreamObject<?> element, int port) {
		System.out.println(element);
		Tuple<?> tuple = (Tuple<?>) element;		
		this.pictogramGroup.processTuple(tuple);
	}

	@Override
	public void punctuationElementRecieved(IPunctuation point, int port) {

	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {

	}

	@Override
	public void onLoad(Map<String, String> saved) {
		super.onLoad(saved);
		setBackgroundFile(saved.get(BACKGROUND_FILE));

		this.pictogramGroup = new PictogramGroup();
		String xmlContent = saved.get(GRAPHICS_CONTENT);
		try {
			if (!xmlContent.isEmpty()) {
				InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				NodeList pictogramList = doc.getDocumentElement().getChildNodes();
				for (int i = 0; i < pictogramList.getLength(); i++) {
					Node pictogramNode = pictogramList.item(i);
					if (pictogramNode.getNodeType() == Node.ELEMENT_NODE) {
						Pictogram pictogram = new ImagePictogram();
						pictogram.initFromXML(pictogramNode);
						this.pictogramGroup.addPictogram(pictogram);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> save = super.onSave();

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			PictogramGroup model = (PictogramGroup) viewer.getContents().getModel();
			Element root = doc.createElement("pictogramgroup");
			doc.appendChild(root);
			for (Pictogram p : model.getPictograms()) {
				Element picNode = doc.createElement("pictogram");
				p.getXML(picNode, doc);
				root.appendChild(picNode);
			}
			System.out.println("---------------------------");
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			System.out.println(sw.toString());
			System.out.println("---------------------------");
			save.put(GRAPHICS_CONTENT, sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		save.put(BACKGROUND_FILE, this.backgroundfile);
		return save;
	}

	public String getBackgroundFile() {
		return backgroundfile;
	}

	public void setBackgroundFile(String backgroundfile) {
		this.backgroundfile = backgroundfile;
	}

	public void setRoots(Collection<IPhysicalOperator> roots) {
		//this.roots = roots;
	}

	private void initActionRegistry() {
		IWorkbenchPart part = getWorkbenchPart();
		actionRegistry = new ActionRegistry();
		actionRegistry.registerAction(new UndoAction(part));
		actionRegistry.registerAction(new RedoAction(part));
		actionRegistry.registerAction(new DeleteAction(part));
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java.util.EventObject)
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
			if (action instanceof UpdateAction)
				((UpdateAction) action).update();
		}
		if (editDomain.getCommandStack().isDirty()) {
			fireChangeEvent();
		}
		
	}

	public Object getAdapter(Class<?> adapter) {
		if (adapter == CommandStack.class) {
			return editDomain.getCommandStack();
		}
		return super.getAdapter(adapter);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {		
		updateActions();
	}

	private void loadActionBars() {
		EditorPart part = (EditorPart) (getWorkbenchPart());		
		IActionBars actionBars = part.getEditorSite().getActionBars();
		if (actionBars == null)
			return;
		String undoId = ActionFactory.UNDO.getId();
		String redoId = ActionFactory.REDO.getId();
		String deleteId = ActionFactory.DELETE.getId();		
		actionBars.setGlobalActionHandler(undoId, actionRegistry.getAction(undoId));
		actionBars.setGlobalActionHandler(redoId, actionRegistry.getAction(redoId));
		actionBars.setGlobalActionHandler(deleteId, actionRegistry.getAction(deleteId));
		actionBars.updateActionBars();
	}
	
}
