package de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.viewer.model.graph.DefaultConnectionModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.DefaultGraphModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.DefaultNodeModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.viewer.model.graph.IGraphModelChangeListener;
import de.uniol.inf.is.odysseus.viewer.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.viewer.swt.SWTStatusLine;
import de.uniol.inf.is.odysseus.viewer.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.viewer.swt.select.ISelectListener;
import de.uniol.inf.is.odysseus.viewer.swt.select.ISelector;
import de.uniol.inf.is.odysseus.viewer.swt.symbol.SWTArrowSymbolElement;
import de.uniol.inf.is.odysseus.viewer.swt.symbol.SWTImageSymbolElement;
import de.uniol.inf.is.odysseus.viewer.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.viewer.view.graph.DefaultConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.DefaultGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.DefaultNodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.viewer.view.position.INodePositioner;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.visualquerylanguage.Activator;
import de.uniol.inf.is.odysseus.visualquerylanguage.ReflectionException;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.DefaultModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.AbstractOperator;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.resource.XMLParameterParser;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.ISWTParameterListener;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.ISWTTreeChangedListener;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTMainWindow;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTOutputEditor;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTParameterArea;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.cursor.CursorManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.view.position.SugiyamaPositioner;

public class DefaultGraphArea extends Composite implements
		IGraphArea<INodeContent>, IGraphModelChangeListener<INodeContent>,
		ISelectListener<INodeView<INodeContent>>, ISWTTreeChangedListener, ISWTParameterListener {

	private static final String XML_FILE = "editor_cfg/parameter.xml";

	private IModelController<INodeContent> controller;
	private IGraphView<INodeContent> viewGraph;
	private SWTRenderManager<INodeContent> renderManager;

	private boolean connectionStarted = false;
	private ArrayList<INodeView<INodeContent>> connNodeList = new ArrayList<INodeView<INodeContent>>();
	private boolean connectionChosen = false;

	private ScrolledComposite infoScroll;
	private Composite infoArea;
	private final Map<INodeView<INodeContent>, SWTParameterArea> parameterAreasShown = new HashMap<INodeView<INodeContent>, SWTParameterArea>();

	private Tree tree;
	private boolean leftMouseClicked = false;

	private ISymbolElementFactory<INodeContent> symFac;
	private INodePositioner<INodeContent> positioner;

	private XMLParameterParser parser = null;

	private Composite upperGraphArea = null;
	SWTStatusLine status = null;

	private int queryID = -1;

	private final Logger log = LoggerFactory.getLogger(DefaultGraphArea.class);

	public DefaultGraphArea(Composite parent, int style, IAdvancedExecutor exec) {
		super(parent, style);

		GridData graphAreaData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		graphAreaData.horizontalIndent = 0;
		graphAreaData.horizontalSpan = 2;
		this.setLayoutData(graphAreaData);

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		this.setLayout(gl);
		upperGraphArea = new Composite(this, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		upperGraphArea.setLayout(new FormLayout());
		upperGraphArea.setLayoutData(gd);

		try {
			URL xmlFile = Activator.getContext().getBundle().getEntry(XML_FILE);
			this.parser = new XMLParameterParser(xmlFile);
		} catch (IOException e) {
			log.error("Parameter couldn't be parsed. Becaus of: ");
			e.printStackTrace();
		}

		this.symFac = new SWTSymbolElementFactory<INodeContent>();
		this.positioner = new SugiyamaPositioner(symFac);

		DefaultGraphModel<INodeContent> graphModel = new DefaultGraphModel<INodeContent>();
		this.controller = new DefaultModelController<INodeContent>(graphModel);

		controller.getModel().addGraphModelChangeListener(this);
		this.viewGraph = new DefaultGraphView<INodeContent>(controller
				.getModel());

		buildGraphArea();
	}

	private void buildGraphArea() {

		// Trenner innerhalb des Graphenbereichs von Timo
		final Sash sash = new Sash(upperGraphArea, SWT.VERTICAL);
		FormData sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 0);
		sashFormData.bottom = new FormAttachment(100, 0);
		sashFormData.left = new FormAttachment(60, 0);
		sash.setLayoutData(sashFormData);
		sash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// damit es auch mit dem Resizen des Fensters klappt
				final int val = (int) (((float) event.x / getSize().x) * 100) + 1;
				((FormData) sash.getLayoutData()).left = new FormAttachment(
						val, 0);
				sash.getParent().layout();
			}
		});

		Composite comp = new Composite(upperGraphArea, SWT.BORDER);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(sash, 0);
		comp.setLayoutData(formData);

		status = new SWTStatusLine(this);
		status.setText("Anfrageerstellung bereit.");

		this.renderManager = new SWTRenderManager<INodeContent>(comp, status,
				positioner);
		this.renderManager.setDisplayedGraph(viewGraph);
		renderManager.getSelector().addSelectListener(this);

		// Informationsbereich von Timo
		infoScroll = new ScrolledComposite(upperGraphArea, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		FormData infoFormData = new FormData();
		infoFormData.top = new FormAttachment(0, 0);
		infoFormData.bottom = new FormAttachment(100, 0);
		infoFormData.left = new FormAttachment(sash, 0);
		infoFormData.right = new FormAttachment(100, 0);
		infoScroll.setLayoutData(infoFormData);

		infoArea = new Composite(infoScroll, 0);
		GridData gd = new GridData(GridData.FILL_BOTH);
		infoArea.setLayoutData(gd);
		GridLayout infoLayout = new GridLayout();
		infoLayout.numColumns = 1;
		infoLayout.verticalSpacing = 10;
		infoArea.setLayout(infoLayout);
		infoScroll.setContent(infoArea);
		infoScroll.setMinSize(200, 200);
		infoScroll.setExpandHorizontal(true);
		infoScroll.setExpandVertical(true);

		renderManager.getCanvas().setBackground(
				Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		renderManager.getCanvas().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					addNewNode(e);
					renderManager.refreshView();
				} else {
					setCursor(CursorManager.setCursor(null));
					status.setText("Anfragestellung bereit.");
					connectionChosen = false;
					connectionStarted = false;
				}
			}
		});

		try {
			tree = getTree();
		} catch (ReflectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					leftMouseClicked = true;
				} else {
					leftMouseClicked = false;
					setCursor(CursorManager.setCursor(null));
					status.setText("Anfragestellung bereit.");
					connNodeList.clear();
					connectionChosen = false;
					connectionStarted = false;
				}
			}
		});
		tree.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelection() != null
						&& tree.getSelection().length > 0
						&& e.item instanceof TreeItem && leftMouseClicked) {

					if (tree.getSelection()[0].getText().equals("Verbindung")) {
						connectionChosen();
					} else {
						connectionChosen = false;
						connectionStarted = false;
						if (tree.getSelection()[0].getData() instanceof INodeContent) {
							setCursor(CursorManager
									.setCursor((INodeContent) tree
											.getSelection()[0].getData()));
						} else {
							setCursor(CursorManager.setCursor(null));
						}
					}
				}
			}
		});
		infoScroll.setMinSize(infoArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		infoArea.layout();
		infoScroll.layout();
	}

	@Override
	public IModelController<INodeContent> getController() {
		return controller;
	}

	@Override
	public IGraphView<INodeContent> getGraphView() {
		return viewGraph;
	}

	public SWTRenderManager<INodeContent> getRenderer() {
		return renderManager;
	}

	@Override
	public void graphModelChanged(IGraphModel<INodeContent> sender) {
		renderManager.refreshView();
	}

	private void addNewNode(MouseEvent e) {
		INodeContent con = null;
		if (tree.getSelectionCount() != 0
				&& !CursorManager.getIsStandardCursor()) {
			if (tree.getSelection()[0].getData() instanceof INodeContent) {
				if (con == null) {
					con = (INodeContent) tree.getSelection()[0].getData();
				}
				if (con != null) {
					INodeContent content = null;
					try {
						content = createNewINodeContentInstance(con);
					} catch (ReflectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (content != null) {
						INodeModel<INodeContent> node = new DefaultNodeModel<INodeContent>(
								content);
						INodeView<INodeContent> nodeView = new DefaultNodeView<INodeContent>(
								node);
						controller.addNode(node);
						CursorManager.isNotConnection();
						nodeView.setPosition(this
								.getRealNodePosition(new Vector(e.x, e.y)));
						if (SWTResourceManager.getInstance().getImage(
								(content).getImageName()) != null) {
							SWTImageSymbolElement<INodeContent> sym = new SWTImageSymbolElement<INodeContent>(
									(content).getImageName());
							nodeView.getSymbolContainer().add(sym);
							nodeView.setWidth(sym.getImageWidth());
							nodeView.setHeight(sym.getImageHeight());
						} else {
							nodeView.getSymbolContainer().add(
									new SWTImageSymbolElement<INodeContent>(
											"default"));
						}
						viewGraph.insertViewedNode(nodeView);
						if ((e.stateMask & SWT.SHIFT) == 0
								&& !CursorManager.getIsConnection()) {
							this.setCursor(CursorManager.setCursor(null));
						}
					}
				}
			}
		}
	}

	private void addNewConnection() {
		if (!renderManager.getSelector().getSelected().isEmpty()) {
			if (!connectionStarted) {
				this.status.setText("Verbindung gestartet.");
				for (INodeView<INodeContent> nodeView : renderManager
						.getSelector().getSelected()) {
					if (nodeView.getModelNode().getContent().isOnlySink()) {
						this.status
								.setErrorText("Von einer Senke darf keine Verbindung ausgehen.");
						return;
					}
				}
				connNodeList = new ArrayList<INodeView<INodeContent>>(
						renderManager.getSelector().getSelected());
				connectionStarted = true;
			} else if (connectionStarted
					&& renderManager.getSelector().getSelected().size() == 1) {
				INodeView<INodeContent> selectedNode = (((ArrayList<INodeView<INodeContent>>) (renderManager
						.getSelector().getSelected())).get(0));
				ILogicalOperator endOp = selectedNode.getModelNode()
						.getContent().getOperator();

				if (connNodeList.size() > 2) {
					connectionStarted = false;
					this.status
							.setErrorText("Es dürfen maximal 2 Knoten mit einem anderen Knoten verbunden werden.");
					renderManager.getSelector().unselectAll();
					return;
				}
				if (endOp instanceof UnaryLogicalOp
						&& (!selectedNode.getModelNode()
								.getConnectionsAsEndNode().isEmpty() || connNodeList
								.size() != 1)) {
					this.status
							.setErrorText("Zu diesem Knoten darf es nur eine Verbindung geben.");
					connectionStarted = false;
					renderManager.getSelector().unselectAll();
					return;
				} else if (endOp instanceof BinaryLogicalOp
						&& ((selectedNode.getModelNode()
								.getConnectionsAsEndNode().size() == 1 && connNodeList
								.size() != 1) || selectedNode.getModelNode()
								.getConnectionsAsEndNode().size() == 2)) {
					this.status
							.setErrorText("Zu diesem Knoten darf es nur zwei Verbindungen geben.");
					connectionStarted = false;
					renderManager.getSelector().unselectAll();
					return;
				} else {
					Collection<ILogicalOperator> opList = new ArrayList<ILogicalOperator>();
					for (INodeView<INodeContent> nodeView : connNodeList) {
						if (nodeView.getModelNode().getContent().getOperator()
								.getOutputSchema() == null
								|| nodeView.getModelNode().getContent()
										.getOperator().getOutputSchema()
										.isEmpty()) {
							this.status
									.setErrorText("Einer der ausgewählten Startknoten besitzt kein Ausgabeschema.");
							opList = new ArrayList<ILogicalOperator>();
							connectionStarted = false;
							renderManager.getSelector().unselectAll();
							break;
						} else {
							opList.add(nodeView.getModelNode().getContent()
									.getOperator());
						}
					}

					if (!opList.isEmpty()) {
						for (INodeView<INodeContent> nodeView : connNodeList) {

							if (!((INodeModel<INodeContent>) (selectedNode
									.getModelNode())).getContent()
									.isOnlySource()) {
								if(endOp instanceof OutputSchemaSettable) {
									SWTOutputEditor editor = new SWTOutputEditor(SWTMainWindow.getShell(), opList, endOp, selectedNode.getModelNode().getContent());
									editor.addParameterListener(this);
								}
								
								if (endOp instanceof UnaryLogicalOp) {
									endOp.subscribeTo(nodeView.getModelNode()
											.getContent().getOperator(), 0, 0, nodeView.getModelNode()
											.getContent().getOperator().getOutputSchema());
								} else if (endOp instanceof BinaryLogicalOp) {
									if (endOp.getSubscribedTo().isEmpty()) {
										endOp.subscribeTo(nodeView
												.getModelNode().getContent()
												.getOperator(), 0, 0, nodeView.getModelNode()
												.getContent().getOperator().getOutputSchema());
									} else {
										endOp.subscribeTo(nodeView
												.getModelNode().getContent()
												.getOperator(), 1, 0, nodeView.getModelNode()
												.getContent().getOperator().getOutputSchema());
									}
								}
								connectionStarted = false;
								IConnectionModel<INodeContent> connModel = new DefaultConnectionModel<INodeContent>(
										nodeView.getModelNode(), selectedNode
												.getModelNode());
								IConnectionView<INodeContent> connView = new DefaultConnectionView<INodeContent>(
										connModel, nodeView, selectedNode);
								SWTArrowSymbolElement<INodeContent> ele = new SWTArrowSymbolElement<INodeContent>(
										new Color(Display.getDefault(),
												new RGB(0, 0, 0)));
								ele.setConnectionView(connView);
								connView.getSymbolContainer().add(ele);

								controller.getModel().addConnection(connModel);
								viewGraph.insertViewedConnection(connView);
								CursorManager.isNotConnection();
								this.status.setText("Anfragestellung bereit.");
							} else {
								this.status
										.setErrorText("Fehlerhafte Auswahl. Anderen Knoten auswählen.");
								connNodeList = new ArrayList<INodeView<INodeContent>>(
										renderManager.getSelector()
												.getSelected());
							}
						}
					}
				}
			} else if (renderManager.getSelector().getSelected().size() != 1) {
				this.status
						.setErrorText("Es darf nur ein Endknoten ausgewählt werden. Anderen Knoten auswählen.");
				connNodeList = new ArrayList<INodeView<INodeContent>>(
						renderManager.getSelector().getSelected());
			}
		}
	}

	public Tree getTree() throws ReflectionException {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 300;
		Tree singleTree = new Tree(infoArea, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		singleTree.setLayoutData(gd);
		IParamConstruct<String> param;
		Collection<IParamConstruct<?>> conParams;
		TreeItem item;
		TreeItem sources = new TreeItem(singleTree, 0);
		sources.setText("Quellen");
		INodeContent source;
		for (Entry<String, ILogicalOperator> entry : DataDictionary
				.getInstance().getViews()) {
			item = new TreeItem(sources, 0);
			param = new DefaultParamConstruct<String>("java.lang.String",
					new ArrayList<String>(), 0, entry.getKey());
			param.setValue(entry.getKey());
			conParams = new ArrayList<IParamConstruct<?>>();
			conParams.add(param);
			if (SWTResourceManager.getInstance().getImage("source") != null) {
				source = new DefaultSourceContent(
						entry.getKey(),
						"de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO",
						SWTResourceManager.getInstance().getImage("source"),
						conParams, new ArrayList<IParamSetter<?>>());
				source.setImageName("source");
			} else {
				source = new DefaultSourceContent(
						entry.getKey(),
						"de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO",
						SWTResourceManager.getInstance().getImage("default"),
						conParams, new ArrayList<IParamSetter<?>>());
				source.setImageName("default");
			}
			item.setData(source);
			item.setText(source.getName());

		}
		TreeItem sinks = new TreeItem(singleTree, 0);
		sinks.setText("Senken");
		for (DefaultSinkContent sink : parser.getSinks()) {
			item = new TreeItem(sinks, 0);
			item.setData(sink);
			item.setText(sink.getName());
		}
		TreeItem pipes = new TreeItem(singleTree, 0);
		pipes.setText("Pipes");
		for (DefaultPipeContent pipe : parser.getPipes()) {
			item = new TreeItem(pipes, 0);
			item.setData(pipe);
			item.setText(pipe.getName());
		}
		TreeItem connection = new TreeItem(singleTree, 0);
		connection.setText("Verbindung");
		return singleTree;
	}

	public void removeNodes() {
		for (INodeView<INodeContent> nodeView : renderManager.getSelector()
				.getSelected()) {
			removeConnections(nodeView);
		}
		for (INodeView<INodeContent> nodeView : renderManager.getSelector()
				.getSelected()) {
			controller.getModel().removeNode(nodeView.getModelNode());
			viewGraph.removeViewedNode(nodeView);
		}
		renderManager.getSelector().unselectAll();
	}

	private void removeConnections(INodeView<INodeContent> nodeView) {
		for (LogicalSubscription subscription : nodeView.getModelNode()
				.getContent().getOperator().getSubscriptions()) {
			nodeView.getModelNode().getContent().getOperator().unsubscribe(
					subscription);
		}
		for (LogicalSubscription subscription : nodeView.getModelNode()
				.getContent().getOperator().getSubscribedTo()) {
			nodeView.getModelNode().getContent().getOperator().unsubscribeTo(
					subscription);
		}
		ArrayList<IConnectionView<INodeContent>> connViewDeleteList = new ArrayList<IConnectionView<INodeContent>>(
				nodeView.getAllConnections());
		for (IConnectionView<INodeContent> connView : connViewDeleteList) {
			controller.getModel().removeConnection(
					connView.getModelConnection());
			viewGraph.removeViewedConnection(connView);
		}
	}

	public void removeSingelConnection(INodeView<INodeContent> nodeView1,
			INodeView<INodeContent> nodeView2,
			IConnectionModel<INodeContent> startConn,
			IConnectionModel<INodeContent> endConn) {
		ArrayList<IConnectionView<INodeContent>> connViewDeleteList = new ArrayList<IConnectionView<INodeContent>>(
				nodeView1.getAllConnections());
		if (endConn != null) {
			for (LogicalSubscription subscription : nodeView1.getModelNode()
					.getContent().getOperator().getSubscriptions()) {
				if (nodeView2.getModelNode().getContent().getOperator().equals(
						subscription.getTarget())) {
					nodeView1.getModelNode().getContent().getOperator()
							.unsubscribe(subscription);
				}
			}
			for (IConnectionView<INodeContent> connView : connViewDeleteList) {
				if (connView.getModelConnection().equals(endConn)) {
					controller.getModel().removeConnection(
							connView.getModelConnection());
					viewGraph.removeViewedConnection(connView);
				}
			}
		} else if (startConn != null) {
			for (LogicalSubscription subscription : nodeView1.getModelNode()
					.getContent().getOperator().getSubscribedTo()) {
				if (nodeView2.getModelNode().getContent().getOperator().equals(
						subscription.getTarget())) {
					nodeView1.getModelNode().getContent().getOperator()
							.unsubscribeTo(subscription);
				}
			}
			for (IConnectionView<INodeContent> connView : connViewDeleteList) {
				if (connView.getModelConnection().equals(startConn)) {
					controller.getModel().removeConnection(
							connView.getModelConnection());
					viewGraph.removeViewedConnection(connView);
				}
			}
		}
	}

	private void connectionChosen() {
		connectionChosen = true;
		renderManager.getSelector().unselectAll();
	}

	private void addParameterArea(INodeView<INodeContent> nodeView) {
		if (!parameterAreasShown.containsKey(nodeView)) {

			SWTParameterArea p = new SWTParameterArea(this, infoArea,
					(DefaultNodeView<INodeContent>) nodeView);
			parameterAreasShown.put(nodeView, p);
			log.debug("ParameterArea for " + nodeView + " created");
		}
	}

	private void removeParameterArea(INodeView<INodeContent> unselected) {
		if (parameterAreasShown.containsKey(unselected)) {
			SWTParameterArea panel = parameterAreasShown.get(unselected);
			panel.dispose();
			parameterAreasShown.remove(unselected);

			log.debug("ParameterArea for " + unselected + " destroyed");
		}
	}

	@Override
	public void selectObject(ISelector<INodeView<INodeContent>> sender,
			Collection<? extends INodeView<INodeContent>> selected) {
		for (INodeView<INodeContent> iNodeView : renderManager.getSelector()
				.getSelected()) {
			addParameterArea(iNodeView);
		}
		infoScroll.setMinSize(infoArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		infoArea.layout();
		infoScroll.layout();
		if (connectionChosen) {
			CursorManager.connectionCursor(true, "connection");
			addNewConnection();
		}

	}

	@Override
	public void unselectObject(ISelector<INodeView<INodeContent>> sender,
			Collection<? extends INodeView<INodeContent>> unselected) {
		for (INodeView<INodeContent> iNodeView : unselected) {
			removeParameterArea(iNodeView);
		}
		infoScroll.setMinSize(infoArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		infoArea.layout();
		infoScroll.layout();
	}

	// Canvaskoordinaten in Graphkoordinaten umrechnen
	private Vector getRealNodePosition(Vector v) {
		return v.div(renderManager.getZoomFactor()).sub(
				renderManager.getGraphOffset());
	}

	private INodeContent createNewINodeContentInstance(INodeContent con)
			throws ReflectionException {
		INodeContent content = null;
		if (con instanceof DefaultSourceContent) {
			if (con.getConstructParameterList().size() == 1) {
				IParamConstruct<String> newParam;
				Collection<IParamConstruct<?>> conParams;
				for (IParamConstruct<?> param : con.getConstructParameterList()) {

					if (param.getValue() instanceof String) {
						newParam = new DefaultParamConstruct<String>(param
								.getType(), param.getTypeList(), param
								.getPosition(), param.getName());
						conParams = new ArrayList<IParamConstruct<?>>();
						newParam.setValue((String) param.getValue());
						conParams.add(newParam);
						content = new DefaultSourceContent(con.getName(), con
								.getType(), con.getImage(), conParams, con
								.getNewSetterParameterListInstance());
					}
				}
			} else {
				content = new DefaultSourceContent(con.getName(),
						con.getType(), con.getImage(), con
								.getNewConstructParameterListInstance(), con
								.getNewSetterParameterListInstance());
			}
			try {
				content.setOperator(createOperator(content));
			} catch (Exception e) {
				throw new ReflectionException();
			}
			content.setImageName(((AbstractOperator) con).getImageName());
			content.setEditor(((AbstractOperator) con).getEditor());
		}
		if (con instanceof DefaultSinkContent) {
			content = new DefaultSinkContent(con.getName(), con.getType(), con
					.getImage(), con.getNewConstructParameterListInstance(),
					con.getNewSetterParameterListInstance());
			try {
				content.setOperator(createOperator(content));
			} catch (Exception e) {
				throw new ReflectionException();
			}
			content.setImageName(((AbstractOperator) con).getImageName());
			content.setEditor(((AbstractOperator) con).getEditor());
		} else if (con instanceof DefaultPipeContent) {
			content = new DefaultPipeContent(con.getName(), con.getType(), con
					.getImage(), con.getNewConstructParameterListInstance(),
					con.getNewSetterParameterListInstance());
			try {
				content.setOperator(createOperator(content));
			} catch (Exception e) {
				throw new ReflectionException();
			}
			content.setImageName(((AbstractOperator) con).getImageName());
			content.setEditor(((AbstractOperator) con).getEditor());
		}
		return content;
	}

	public SWTStatusLine getStatusLine() {
		return this.status;
	}

	public Tree getCreatedTree() {
		return this.tree;
	}

	public void refreshTree() {
		TreeItem treeItem;
		INodeContent source;
		IParamConstruct<String> param;
		Collection<IParamConstruct<?>> conParams;
		tree.getItem(0).removeAll();
		for (Entry<String, ILogicalOperator> entry : DataDictionary
				.getInstance().getViews()) {
			treeItem = new TreeItem(tree.getItem(0), 0);
			param = new DefaultParamConstruct<String>("java.lang.String",
					new ArrayList<String>(), 0, entry.getKey());
			param.setValue(entry.getKey());
			conParams = new ArrayList<IParamConstruct<?>>();
			conParams.add(param);
			if (SWTResourceManager.getInstance().getImage("source") != null) {
				source = new DefaultSourceContent(
						entry.getKey(),
						"de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO",
						SWTResourceManager.getInstance().getImage("source"),
						conParams, new ArrayList<IParamSetter<?>>());
				source.setImageName("source");
			} else {
				source = new DefaultSourceContent(
						entry.getKey(),
						"de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO",
						SWTResourceManager.getInstance().getImage("default"),
						conParams, new ArrayList<IParamSetter<?>>());
				source.setImageName("default");
			}
			treeItem.setData(source);
			treeItem.setText(source.getName());
		}
		tree.layout();
	}

	@Override
	public void treeChanged() {
		refreshTree();
	}

	public void setQueryID(int ID) {
		this.queryID = ID;
	}

	public int getQueryID() {
		return this.queryID;
	}

	@SuppressWarnings("unchecked")
	private ILogicalOperator createOperator(INodeContent content)
			throws ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Class clazz = null;
		Class[] constructParameters = null;
		Constructor con = null;
		Object logOp = null;
		ILogicalOperator sourceOp = null;

		ArrayList<Object> parameterValues = null;

		clazz = Class.forName(content.getType());

		if (!content.getConstructParameterList().isEmpty()) {
			constructParameters = new Class[content.getConstructParameterList()
					.size()];
			parameterValues = new ArrayList<Object>();
			for (IParamConstruct<?> param : content.getConstructParameterList()) {
				Class paramClazz = Class.forName(param.getType());
				constructParameters[param.getPosition()] = paramClazz;
				parameterValues.add(param.getValue());
			}
			if (!content.isOnlySource()) {
				con = clazz.getConstructor(constructParameters);
			} else if (content.isOnlySource() && parameterValues.size() == 1
					&& parameterValues.get(0) instanceof String) {
				Class sourceClass = Class
						.forName("de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource");
				Class[] sourceParameters = new Class[1];
				sourceParameters[0] = sourceClass;
				con = clazz.getConstructor(sourceParameters);
				SDFSource source = DataDictionary.getInstance().getSource(
						(String) parameterValues.get(0));
				sourceOp = (ILogicalOperator) con.newInstance(source);
				if (sourceOp instanceof OutputSchemaSettable) {
					((OutputSchemaSettable) sourceOp)
							.setOutputSchema(DataDictionary.getInstance()
									.getView((String) parameterValues.get(0))
									.getOutputSchema());
				}
			} else {
				if (con != null) {
					logOp = con.newInstance(parameterValues);
				}

			}
		} else {
			logOp = (ILogicalOperator) clazz.newInstance();
		}
		for (IParamSetter<?> param : content.getSetterParameterList()) {
			if (param.getValue() != null) {
				Method method = logOp.getClass().getMethod(param.getSetter(),
						new Class[] { param.getValue().getClass() });
				method.invoke(logOp, new Object[] { param.getValue() });
			}
		}
		if (sourceOp != null) {
			return sourceOp;
		} else if (logOp != null) {
			return (ILogicalOperator) logOp;
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Object value) {
		
		INodeView<INodeContent> nodeView = ((ArrayList<INodeView<INodeContent>>) (renderManager
				.getSelector().getSelected())).get(0);
		ILogicalOperator operator = nodeView.getModelNode().getContent().getOperator();
		if(operator instanceof OutputSchemaSettable) {
			((OutputSchemaSettable)operator).setOutputSchema((SDFAttributeList)value);
		}
	}
}
