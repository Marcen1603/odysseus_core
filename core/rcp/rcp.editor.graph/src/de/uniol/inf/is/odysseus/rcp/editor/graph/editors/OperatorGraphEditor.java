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
import java.util.HashMap;
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.OperatorNodeEditPart;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.factories.GraphEditPartFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator.ScriptGenerator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorGraphEditor extends GraphicalEditorWithFlyoutPalette implements ISelectionChangedListener {

	private Graph graph;
	private DefaultEditDomain editDomain;

	public OperatorGraphEditor() {
		editDomain = new DefaultEditDomain(this);
		setEditDomain(editDomain);
		graph = new Graph();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initGraphEditorListener();	
		if (input instanceof FileEditorInput) {
			loadFromXML((FileEditorInput) input);
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
				p.getXML(opNode, doc);
				operatorsElement.appendChild(opNode);
				connections.addAll(p.getSourceConnections());
			}
			// we have to handle connections separately since we don't know the end and start point before
			Element connectionsElement = doc.createElement("connections");
			root.appendChild(connectionsElement);
			for (Connection c : connections) {
				Element conNode = doc.createElement("connection");
				c.getXML(conNode, doc);
				connectionsElement.appendChild(conNode);
			}

			// now, PQL for other purposes
			ScriptGenerator generator = new ScriptGenerator(graph, new HashMap<String, String>());
			String pql = generator.buildPQL();

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

	private void loadFromXML(FileEditorInput input) {

		try {
			InputStream is = input.getFile().getContents();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			Element root = doc.getDocumentElement();
			// first load the operators

			Map<String, Element> mainNodes = getChildNodes(root);

			List<Element> operatorNodes = getChildList(mainNodes.get("operators"));
			for (Element opNode : operatorNodes) {
				String typeName = opNode.getAttributes().getNamedItem("type").getNodeValue();
				LogicalOperatorInformation loi = getLogicalOperatorInformationByName(typeName);
				OperatorNode operatorNode = new OperatorNode(loi);
				operatorNode.loadFromXML(opNode);
				this.graph.addNode(operatorNode);
			}
			// then, load the connections
			List<Element> connectionNodes = getChildList(mainNodes.get("connections"));
			for (Element conElement : connectionNodes) {
				Map<String, String> values = getChildElements(conElement);				
				OperatorNode sourceNode = graph.getOperatorNodeById(Integer.parseInt(values.get("source")));
				OperatorNode targetNode = graph.getOperatorNodeById(Integer.parseInt(values.get("target")));
				Connection con = new Connection();
				con.reconnect(sourceNode, targetNode);
			}
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
		// editDomain.getCommandStack().addCommandStackListener(new GraphEditorListener(actionRegistry));
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
	}
	
	// /**
	// * @param form
	// */
	// private void createPaletteViewer(Composite parent) {
	// PaletteViewer viewer = new PaletteViewer();
	// viewer.createControl(parent);
	// editDomain.setPaletteViewer(viewer);
	// editDomain.setPaletteRoot(GraphPalette.createGraphPalette());
	//
	// }
	//
	//
	// /**
	// * @param form
	// */
	// private void createGraphViewer(Composite parent) {
	// ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
	// viewer.createControl(parent);
	// viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	// viewer.getControl().setBackground(ColorConstants.white);
	// viewer.setEditPartFactory(new GraphEditPartFactory());
	//
	// viewer.setContents(graph);
	// editDomain.addViewer(viewer);
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		return GraphPalette.createGraphPalette();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
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

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions(getSelectionActions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof StructuredSelection) {
			StructuredSelection strucSel = (StructuredSelection) event.getSelection();
			if (strucSel.getFirstElement() != null && strucSel.getFirstElement() instanceof OperatorNodeEditPart) {
				OperatorNodeEditPart opnodeeditpart = (OperatorNodeEditPart) strucSel.getFirstElement();
				if (opnodeeditpart.getModel() instanceof OperatorNode) {
					OperatorGraphSelectionProvider.getInstance().setCurrentlySelected((OperatorNode) opnodeeditpart.getModel());
				}
			}
		}
	}

}
