/*******************************************************************************
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.CopyAction;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands.PasteAction;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dnd.OperatorDropListener;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.OperatorNodeEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.factories.GraphEditPartFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator.ScriptGenerator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.GraphProblem;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorGraphEditor extends GraphicalEditorWithFlyoutPalette implements ISelectionChangedListener {

	private static String MARKER_ID = "de.uniol.inf.is.odysseus.rcp.editor.graph.marker.notsatisfied";

	private Graph graph;
	private DefaultEditDomain editDomain;

	private static final String DEFAULT_HEADING = "#TRANSCFG Standard";
	private String heading;

	

	public OperatorGraphEditor() {
		editDomain = new DefaultEditDomain(this);
		setEditDomain(editDomain);
		graph = new Graph();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initGraphEditorListener();
		if (input instanceof FileEditorInput) {
			loadFromXML((FileEditorInput) input);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(
	 * java.lang.Class)
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		if (type == ActionRegistry.class) {
			return getActionRegistry();
		}
		if (type == ZoomManager.class) {
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		}
		return super.getAdapter(type);
	}

	@Override
	public void setFocus() {
		super.setFocus();
		updateGraph();
	}

	public void updateGraph() {
		if (graph != null) {	
			List<GraphProblem> problems = graph.updateInformation();
			updateMarkers(problems);
		}
	}

	private void updateMarkers(List<GraphProblem> problems) {
		try {
			IResource target = graph.getGraphFile();
			target.deleteMarkers(MARKER_ID, true, IResource.DEPTH_INFINITE);

			for (GraphProblem prob : problems) {
				IMarker marker = graph.getGraphFile().createMarker(MARKER_ID);
				marker.setAttribute(IMarker.MESSAGE, prob.getMessage());
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				marker.setAttribute(IMarker.LOCATION, prob.getNode().getOperatorInformation().getOperatorName());
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void saveToXML(FileEditorInput fileInput) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element root = doc.createElement("graph");
			doc.appendChild(root);
			// first the operators and its layout
			Element operatorsElement = doc.createElement("operators");
			root.appendChild(operatorsElement);

			List<Connection> connections = new ArrayList<>();
			for (OperatorNode p : graph.getNodes()) {
				Element opNode = doc.createElement("operatornode");
				opNode.setAttribute("type", p.getOperatorInformation().getOperatorName());
				opNode.setAttribute("id", Integer.toString(p.getId()));
				p.getXML(opNode, doc);
				operatorsElement.appendChild(opNode);
				connections.addAll(p.getSourceConnections());
			}
			// we have to handle connections separately since we don't know the
			// end and start point before
			Element connectionsElement = doc.createElement("connections");
			root.appendChild(connectionsElement);
			for (Connection c : connections) {
				Element conNode = doc.createElement("connection");
				c.getXML(conNode, doc);
				connectionsElement.appendChild(conNode);
			}
			// the heading data
			Element headingElement = doc.createElement("heading");
			headingElement.setTextContent(getHeading());
			root.appendChild(headingElement);
			
			// now, PQL for other purposes
			String pql = ScriptGenerator.buildPQL(graph);

			CDATASection pqldata = doc.createCDATASection(pql);
			Element pqlElement = doc.createElement("pql");
			pqlElement.appendChild(pqldata);
			root.appendChild(pqlElement);

			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			InputStream inputStream = new ByteArrayInputStream(sw.toString().getBytes());

			fileInput.getFile().setContents(inputStream, IResource.FORCE, null);
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getHeading() {
		return this.heading;
	}

	private void loadFromXML(FileEditorInput input) {

		try {
			this.graph.setProject(input.getFile().getProject());
			this.graph.setGraphFile(input.getFile());
			InputStream is = input.getFile().getContents();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			Element root = doc.getDocumentElement();
			// first load the operators

			Map<String, Element> mainNodes = getChildNodes(root);

			Map<String, OperatorNode> currentIdToNodes = new TreeMap<>();

			if(mainNodes.get("heading")!=null){
				Element heading = mainNodes.get("heading");
				setHeading(heading.getTextContent());
			}else{
				setHeading(DEFAULT_HEADING);
			}
			
			List<Element> operatorNodes = getChildList(mainNodes.get("operators"));
			for (Element opNode : operatorNodes) {
				String typeName = opNode.getAttributes().getNamedItem("type").getNodeValue();
				String idName = opNode.getAttributes().getNamedItem("id").getNodeValue();
				LogicalOperatorInformation loi = getLogicalOperatorInformationByName(typeName);
				OperatorNode operatorNode = new OperatorNode(loi);
				operatorNode.loadFromXML(opNode);
				currentIdToNodes.put(idName, operatorNode);
				this.graph.addNode(operatorNode);
			}
			// then, load the connections
			List<Element> connectionNodes = getChildList(mainNodes.get("connections"));
			for (Element conElement : connectionNodes) {
				Map<String, String> values = getChildElements(conElement);
				OperatorNode sourceNode = currentIdToNodes.get(values.get("source"));
				OperatorNode targetNode = currentIdToNodes.get(values.get("target"));
				int sourcePort = 0;
				int targetPort = 0;
				if (values.get("sourcePort") != null) {
					sourcePort = Integer.parseInt(values.get("sourcePort"));
				}
				if (values.get("targetPort") != null) {
					targetPort = Integer.parseInt(values.get("targetPort"));
				}
				Connection con = new Connection();
				con.setGraph(graph);
				con.setSourcePort(sourcePort);
				con.setTargetPort(targetPort);
				con.reconnect(sourceNode, targetNode);
			}
			this.graph.updateInformation();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Element> getChildList(Element element) {
		List<Element> list = new ArrayList<>();
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				list.add((Element) nl.item(i));
			}
		}
		return list;
	}

	private Map<String, Element> getChildNodes(Node node) {
		Map<String, Element> list = new TreeMap<>();
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				Element e = (Element) nl.item(i);
				list.put(e.getNodeName(), e);
			}
		}
		return list;
	}

	private Map<String, String> getChildElements(Node node) {
		Map<String, String> map = new TreeMap<>();
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) child;
				map.put(element.getNodeName(), element.getTextContent());
			}
		}
		return map;
	}

	private LogicalOperatorInformation getLogicalOperatorInformationByName(String name) {
		return Activator.getDefault().getExecutor().getOperatorInformation(name, Activator.getDefault().getCaller());
	}

	private void initGraphEditorListener() {
		// editDomain.getCommandStack().addCommandStackListener(new
		// GraphEditorListener(actionRegistry));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GraphEditPartFactory());
		viewer.setContents(graph);
		viewer.addSelectionChangedListener(this);
		viewer.getControl().setBackground(ColorConstants.white);
		viewer.addDropTargetListener(new OperatorDropListener(getGraphicalViewer()));
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		getSite().setSelectionProvider(viewer);
				
		// actions
		registerAndBindingService("org.eclipse.ui.edit.undo", new UndoAction(this));
		registerAndBindingService("org.eclipse.ui.edit.redo", new RedoAction(this));
//		registerAndBindingService(ActionFactory.COPY.getId(), new CopyAction(this));
//		registerAndBindingService(ActionFactory.PASTE.getId(), new PasteAction(this));
		registerAndBindingService("org.eclipse.ui.edit.copy", new CopyAction(getEditorSite().getPart()));
		registerAndBindingService("org.eclipse.ui.edit.paste", new PasteAction(getEditorSite().getPart()));
		registerAndBindingService("org.eclipse.ui.edit.delete", new DeleteAction(getEditorSite().getPart()));
		
		@SuppressWarnings("unchecked")
		List<Object> list = super.getSelectionActions();
		list.add(ActionFactory.COPY.getId());
		list.add(ActionFactory.PASTE.getId());		
		
		ZoomManager zoomManager = ((ScalableRootEditPart) viewer.getRootEditPart()).getZoomManager();
		registerAndBindingService(new ZoomInAction(zoomManager));
		registerAndBindingService(new ZoomOutAction(zoomManager));

		List<String> zoomContributions = Arrays.asList(new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH });
		zoomManager.setZoomLevelContributions(zoomContributions);
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
	}

	private void registerAndBindingService(IAction action) {
		registerAndBindingService(action.getActionDefinitionId(), action);
	}

	private void registerAndBindingService(String actionDefinitionId, IAction action) {
		getActionRegistry().registerAction(action);
		IHandlerService service = getSite().getService(IHandlerService.class);
		service.activateHandler(actionDefinitionId, new ActionHandler(action));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot
	 * ()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		return GraphPalette.createGraphPalette();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		IEditorInput input = getEditorInput();
		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;
			saveToXML(fileInput);
		}
	}

	public EditDomain getCurrentEditDomain() {
		return this.editDomain;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @return
	 */
	public ActionRegistry getCurrentActionRegistry() {
		return getActionRegistry();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions(getSelectionActions());
		super.selectionChanged(part, selection);
	}

	
	@Override
	public void dispose() {	
		super.dispose();
		OperatorGraphSelectionProvider.getInstance().setCurrentlySelected(null);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof StructuredSelection) {
			OperatorGraphSelectionProvider.getInstance().setCurrentlySelected(null);
			StructuredSelection strucSel = (StructuredSelection) event.getSelection();
			if (strucSel.getFirstElement() != null && strucSel.getFirstElement() instanceof OperatorNodeEditPart) {
				OperatorNodeEditPart opnodeeditpart = (OperatorNodeEditPart) strucSel.getFirstElement();
				if (opnodeeditpart.getModel() instanceof OperatorNode) {
					OperatorGraphSelectionProvider.getInstance().setCurrentlySelected((OperatorNode) opnodeeditpart.getModel());
				}
			}
		}
	}

	public void setHeading(String heading) {
		this.heading = heading;		
	}

	public String createFullHeading(){
		if(getHeading()==null || getHeading().trim().isEmpty()){
			setHeading(DEFAULT_HEADING);
		}
		String pql = getHeading()+System.lineSeparator();
		pql = pql + "#PARSER PQL"+System.lineSeparator();				
		pql = pql+"#ADDQUERY"+System.lineSeparator();
		return pql;
	}
	
}
