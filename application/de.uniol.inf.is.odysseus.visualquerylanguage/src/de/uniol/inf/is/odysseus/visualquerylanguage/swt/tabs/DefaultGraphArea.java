package de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs;

import java.io.IOException;
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
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
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
import de.uniol.inf.is.odysseus.viewer.swt.symbol.SWTFillCircleSymbolElement;
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
import de.uniol.inf.is.odysseus.visualquerylanguage.ISWTTreeChangedListener;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.DefaultModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.controler.IModelController;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.resource.XMLParameterParser;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.SWTParameterArea;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.cursor.CursorManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.view.position.SugiyamaPositioner;

public class DefaultGraphArea extends Composite implements
		IGraphArea<INodeContent>, IGraphModelChangeListener<INodeContent>,
		ISelectListener<INodeView<INodeContent>> , ISWTTreeChangedListener{

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

	public DefaultGraphArea(Composite parent, int style,
			IAdvancedExecutor exec) {
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
					setCursor(CursorManager.setStandardCursor());
					connectionChosen = false;
					connectionStarted = false;
				}
			}
		});
		
		tree = getTree();
		tree.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					leftMouseClicked = true;
				} else {
					leftMouseClicked = false;
					setCursor(CursorManager.setStandardCursor());
					connNodeList.clear();
					connectionChosen = false;
					connectionStarted = false;
				}
			}
		});
		tree.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelection() != null && tree.getSelection().length > 0 && e.item instanceof TreeItem
						&& leftMouseClicked) {

					if (tree.getSelection()[0].getText().equals("Verbindung")) {
						connectionChosen();
					} else {
						connectionChosen = false;
						connectionStarted = false;
						if (tree.getSelection()[0].getData() instanceof Entry) {
							setCursor(CursorManager.dragButtonCursor("source"));
						} else if (tree.getSelection()[0].getData() instanceof DefaultSinkContent) {
							setCursor(CursorManager.dragButtonCursor("sink"));
						} else if (tree.getSelection()[0].getData() instanceof DefaultPipeContent) {
							setCursor(CursorManager.dragButtonCursor("pipe"));
						} else {
							setCursor(CursorManager.setStandardCursor());
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

	@SuppressWarnings("unchecked")
	private void addNewNode(MouseEvent e) {
		INodeContent con = null;
		if (tree.getSelectionCount() != 0
				&& !CursorManager.getIsStandardCursor()) {
			if (tree.getSelection()[0].getData() instanceof Entry) {
				Collection<IParamConstruct<?>> conParamList = new ArrayList<IParamConstruct<?>>();
				DefaultParamConstruct<String> param = new DefaultParamConstruct<String>(
						"de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource",
						0, "URI");
				param.setValue((String) ((Entry) (tree.getSelection()[0]
						.getData())).getKey());
				conParamList.add(param);
				Collection<IParamSetter<?>> setParamList = new ArrayList<IParamSetter<?>>();
				con = new DefaultSourceContent(
						(String) ((Entry) tree.getSelection()[0].getData())
								.getKey(),
						"de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO",
						SWTResourceManager.getInstance().getImage("Source"),
						conParamList, setParamList);
			}
			if (con != null
					|| tree.getSelection()[0].getData() instanceof INodeContent) {
				if (con == null) {
					con = (INodeContent) tree.getSelection()[0].getData();
				}
				if (con != null) {
					INodeContent content = null;
					if (!con.isOnlySource()) {
						content = createNewINodeContentInstance(con);
					} else {
						content = con;
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
						if (content.isOnlySource()) {
							if (new SWTImageSymbolElement<INodeContent>(
									"source") != null) {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTImageSymbolElement<INodeContent>(
														"source"));
							} else {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTFillCircleSymbolElement<INodeContent>(
														new Color(
																Display
																		.getDefault(),
																new RGB(0, 0, 0))));
							}
						} else if (content.isOnlySink()) {
							if (new SWTImageSymbolElement<INodeContent>("sink") != null) {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTImageSymbolElement<INodeContent>(
														"sink"));
							} else {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTFillCircleSymbolElement<INodeContent>(
														new Color(
																Display
																		.getDefault(),
																new RGB(0, 0, 0))));
							}
						} else if (content.isPipe()) {
							if (new SWTImageSymbolElement<INodeContent>("pipe") != null) {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTImageSymbolElement<INodeContent>(
														"pipe"));
							} else {
								nodeView
										.getSymbolContainer()
										.add(
												new SWTFillCircleSymbolElement<INodeContent>(
														new Color(
																Display
																		.getDefault(),
																new RGB(0, 0, 0))));
							}
						}
						viewGraph.insertViewedNode(nodeView);
						if ((e.stateMask & SWT.SHIFT) == 0
								&& !CursorManager.getIsConnection()) {
							this.setCursor(CursorManager.setStandardCursor());
						}
					}
				}
			}
		}
	}

	private void addNewConnection() {
		if (!renderManager.getSelector().getSelected().isEmpty()) {
			if (!connectionStarted
					&& !((INodeModel<INodeContent>) ((ArrayList<INodeView<INodeContent>>) (renderManager
							.getSelector().getSelected())).get(0)
							.getModelNode()).getContent().isOnlySink()) {
				connNodeList = new ArrayList<INodeView<INodeContent>>(
						renderManager.getSelector().getSelected());
				connectionStarted = true;

			} else if (connectionStarted
					&& renderManager.getSelector().getSelected().size() == 1) {
				INodeView<INodeContent> selectedNode = (((ArrayList<INodeView<INodeContent>>) (renderManager
						.getSelector().getSelected())).get(0));
				for (INodeView<INodeContent> nodeView : connNodeList) {
					if (!((INodeModel<INodeContent>) (selectedNode
							.getModelNode())).getContent().isOnlySource()) {
						if (!nodeView.getModelNode().getContent().isOnlySink()) {
							connectionStarted = false;
							IConnectionModel<INodeContent> connModel = new DefaultConnectionModel<INodeContent>(
									nodeView.getModelNode(), selectedNode
											.getModelNode());
							IConnectionView<INodeContent> connView = new DefaultConnectionView<INodeContent>(
									connModel, nodeView, selectedNode);
							connView.getSymbolContainer().add(
									new SWTArrowSymbolElement<INodeContent>(
											new Color(Display.getDefault(),
													new RGB(0, 0, 0))));
							controller.getModel().addConnection(connModel);
							viewGraph.insertViewedConnection(connView);
							CursorManager.isNotConnection();
						} else if (!((INodeModel<INodeContent>) (selectedNode
								.getModelNode())).getContent().isOnlySink()) {
							if (!nodeView.getModelNode().getContent()
									.isOnlySource()) {
								connectionStarted = false;
								IConnectionModel<INodeContent> connModel = new DefaultConnectionModel<INodeContent>(
										selectedNode.getModelNode(), nodeView
												.getModelNode());
								IConnectionView<INodeContent> connView = new DefaultConnectionView<INodeContent>(
										connModel, selectedNode, nodeView);
								connView
										.getSymbolContainer()
										.add(
												new SWTArrowSymbolElement<INodeContent>(
														new Color(
																Display
																		.getDefault(),
																new RGB(0, 0, 0))));
								controller.getModel().addConnection(connModel);
								viewGraph.insertViewedConnection(connView);
								CursorManager.isNotConnection();
							}
						}
					} else {
						connNodeList = new ArrayList<INodeView<INodeContent>>(
								renderManager.getSelector().getSelected());
					}
				}
			}
		}
	}

	public Tree getTree() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 300;
		Tree singleTree = new Tree(infoArea, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		singleTree.setLayoutData(gd);
		TreeItem item;
		TreeItem sources = new TreeItem(singleTree, 0);
		sources.setText("Quellen");
		for (Entry<String, ILogicalOperator> entry : DataDictionary
				.getInstance().getViews()) {
			item = new TreeItem(sources, 0);
			item.setData(entry);
			item.setText(entry.getKey());
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
		ArrayList<IConnectionView<INodeContent>> connViewDeleteList;
		for (INodeView<INodeContent> nodeView : renderManager.getSelector()
				.getSelected()) {
			connViewDeleteList = new ArrayList<IConnectionView<INodeContent>>(
					nodeView.getAllConnections());
			for (IConnectionView<INodeContent> connView : connViewDeleteList) {
				controller.getModel().removeConnection(
						connView.getModelConnection());
				viewGraph.removeViewedConnection(connView);
			}
		}
		for (INodeView<INodeContent> nodeView : renderManager.getSelector()
				.getSelected()) {
			controller.getModel().removeNode(nodeView.getModelNode());
			viewGraph.removeViewedNode(nodeView);
		}
	}

	private void connectionChosen() {
		connectionChosen = true;
		renderManager.getSelector().unselectAll();
	}

	private void addParameterArea(INodeView<INodeContent> nodeView) {
		if (!parameterAreasShown.containsKey(nodeView)) {

			SWTParameterArea p = new SWTParameterArea(infoArea,
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

	private INodeContent createNewINodeContentInstance(INodeContent con) {
		INodeContent content = null;
		if (con instanceof DefaultSourceContent) {
			content = new DefaultSourceContent(con.getName(), con.getType(),
					con.getImage(), con.getNewConstructParameterListInstance(),
					con.getNewSetterParameterListInstance());
		} else if (con instanceof DefaultSinkContent) {
			content = new DefaultSinkContent(con.getName(), con.getType(), con
					.getImage(), con.getNewConstructParameterListInstance(),
					con.getNewSetterParameterListInstance());
		} else if (con instanceof DefaultPipeContent) {
			content = new DefaultPipeContent(con.getName(), con.getType(), con
					.getImage(), con.getNewConstructParameterListInstance(),
					con.getNewSetterParameterListInstance());
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
		boolean exists = false; 
		if(tree.getItem(0).getItems().length == 0) {
			for(Entry<String, ILogicalOperator> entry : DataDictionary.getInstance().getViews()) {
				treeItem = new TreeItem(tree.getItem(0), 0);
				treeItem.setData(entry);
				treeItem.setText(entry.getKey());
			}
		}else {
			for(Entry<String, ILogicalOperator> entry : DataDictionary.getInstance().getViews()) {
				for(TreeItem item : tree.getItem(0).getItems()) {
					if(item.getData().equals(entry)) {
						exists = true;
					}
				}
				if(exists == false) {
					treeItem = new TreeItem(tree.getItem(0), 0);
					treeItem.setData(entry);
					treeItem.setText(entry.getKey());
				}
			}
			tree.pack();
			tree.layout();
		}
		
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
}
