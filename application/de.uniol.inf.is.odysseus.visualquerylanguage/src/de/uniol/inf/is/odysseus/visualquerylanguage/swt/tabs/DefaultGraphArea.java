package de.uniol.inf.is.odysseus.visualquerylanguage.swt.tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParam;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.DefaultQuery;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.resource.XMLParameterParser;
import de.uniol.inf.is.odysseus.visualquerylanguage.swt.cursor.CursorManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.view.position.SugiyamaPositioner;
import de.uniol.inf.is.odysseus.vqlinterfaces.ctrl.IController;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.DefaultConnectionModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.DefaultNodeModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.IGraphModelChangeListener;
import de.uniol.inf.is.odysseus.vqlinterfaces.model.graph.INodeModel;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.SWTStatusLine;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.renderer.SWTRenderManager;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.select.ISelectListener;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.select.ISelectSender;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.symbol.SWTArrowSymbolElement;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.symbol.SWTImageSymbolElement;
import de.uniol.inf.is.odysseus.vqlinterfaces.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.DefaultConnectionView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.DefaultGraphView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.DefaultNodeView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.IGraphView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.INodeView;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.graph.Vector;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.position.INodePositioner;
import de.uniol.inf.is.odysseus.vqlinterfaces.view.symbol.ISymbolElementFactory;

public class DefaultGraphArea extends Composite implements
		IGraphArea<INodeContent>, IGraphModelChangeListener<INodeContent>,
		ISelectListener<INodeView<INodeContent>> {

	private Canvas canvas = null;

	private IController<INodeContent> controller;
	private IGraphView<INodeContent> viewGraph;
	private SWTRenderManager<INodeContent> renderManager;

	private boolean connectionStarted = false;
	private ArrayList<INodeView<INodeContent>> connNodeList = new ArrayList<INodeView<INodeContent>>();
	private boolean connectionChosen = false;

	private ScrolledComposite infoScroll;
	private Composite infoArea;

	private Composite param;

	private Tree tree;
	private boolean leftMouseClicked = false;

	private ISymbolElementFactory<INodeContent> symFac;
	private INodePositioner<INodeContent> positioner;

	private XMLParameterParser parser = null;

	public DefaultGraphArea(Composite parent, DefaultQuery query, int style) {
		super(parent, style);

		this.setLayout(new FormLayout());
		GridData graphAreaData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		graphAreaData.horizontalIndent = 0;
		graphAreaData.horizontalSpan = 2;
		this.setLayoutData(graphAreaData);

		try {
			this.parser = new XMLParameterParser(
					"C:/Informatik/Odysseus/de.uniol.inf.is.odysseus.visualquerylanguage/editor_cfg/parameter.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		buildGraphArea();

		this.symFac = new SWTSymbolElementFactory<INodeContent>();
		this.positioner = new SugiyamaPositioner(symFac);

		controller = query.getController();
		controller.getModel().addGraphModelChangeListener(this);
		this.viewGraph = new DefaultGraphView<INodeContent>(controller
				.getModel());

		this.renderManager = new SWTRenderManager<INodeContent>(canvas,
				new SWTStatusLine(this.getParent()), positioner);
		this.renderManager.setDisplayedGraph(viewGraph);
		renderManager.getSelector().addSelectListener(this);
	}

	private void buildGraphArea() {

		// Trenner innerhalb des Graphenbereichs von Timo
		final Sash sash = new Sash(this, SWT.VERTICAL);
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

		// Informationsbereich von Timo
		infoScroll = new ScrolledComposite(this, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
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

		canvas = new Canvas(this, SWT.BORDER);
		FormData canvasFormData = new FormData();
		canvasFormData.top = new FormAttachment(0, 0);
		canvasFormData.bottom = new FormAttachment(100, 0);
		canvasFormData.left = new FormAttachment(0, 0);
		canvasFormData.right = new FormAttachment(sash, 0);
		canvas.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		canvas.setLayoutData(canvasFormData);
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					addNewNode(e);
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

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tree.getSelection() != null && e.item instanceof TreeItem
						&& leftMouseClicked) {

					if (tree.getSelection()[0].getText().equals("Verbindung")) {
						connectionChosen();
					} else {
						connectionChosen = false;
						connectionStarted = false;
						if (tree.getSelection()[0].getData() instanceof DefaultSourceContent) {
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
	}

	@Override
	public IController<INodeContent> getController() {
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
		if (tree.getSelectionCount() != 0
				&& !CursorManager.getIsStandardCursor()) {
			INodeContent content = (INodeContent) (tree.getSelection()[0]
					.getData());
			if (content != null) {
				INodeModel<INodeContent> node = new DefaultNodeModel<INodeContent>(
						content);
				INodeView<INodeContent> nodeView = new DefaultNodeView<INodeContent>(
						node);
				controller.addNode(node);
				CursorManager.isNotConnection();
				nodeView.setPosition(renderManager
						.getRealNodePosition(new Vector(e.x, e.y)));
				if (content.isSource()) {
					nodeView.getSymbolContainer().add(
							new SWTImageSymbolElement<INodeContent>("source"));
				} else if (content.isSink()) {
					nodeView.getSymbolContainer().add(
							new SWTImageSymbolElement<INodeContent>("sink"));
				} else if (content.isPipe()) {
					nodeView.getSymbolContainer().add(
							new SWTImageSymbolElement<INodeContent>("pipe"));
				}
				viewGraph.insertViewedNode(nodeView);
				if ((e.stateMask & SWT.SHIFT) == 0
						&& !CursorManager.getIsConnection()) {
					this.setCursor(CursorManager.setStandardCursor());
				}
			}
		}
	}

	private void addNewConnection() {
		if (!renderManager.getSelector().getSelected().isEmpty()) {
			if (!connectionStarted
					&& !((INodeModel<INodeContent>) ((ArrayList<INodeView<INodeContent>>) (renderManager
							.getSelector().getSelected())).get(0)
							.getModelNode()).getContent().isSink()) {
				connNodeList = new ArrayList<INodeView<INodeContent>>(
						renderManager.getSelector().getSelected());
				connectionStarted = true;

			} else if (connectionStarted
					&& renderManager.getSelector().getSelected().size() == 1) {
				INodeView<INodeContent> selectedNode = (((ArrayList<INodeView<INodeContent>>) (renderManager
						.getSelector().getSelected())).get(0));
				for (INodeView<INodeContent> nodeView : connNodeList) {
					if (!((INodeModel<INodeContent>) (selectedNode
							.getModelNode())).getContent().isSource()) {
						if (!nodeView.getModelNode().getContent().isSink()) {
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
								.getModelNode())).getContent().isSink()) {
							if (!nodeView.getModelNode().getContent()
									.isSource()) {
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
					}
				}
			}
		}
	}

	public Tree getTree() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Tree singleTree = new Tree(infoArea, SWT.SINGLE | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		singleTree.setLayoutData(gd);
		TreeItem item;
		TreeItem sources = new TreeItem(singleTree, 0);
		sources.setText("Quellen");
		for (DefaultSourceContent source : parser.getSources()) {
			item = new TreeItem(sources, 0);
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

	@Override
	public void selectObject(ISelectSender<INodeView<INodeContent>> sender,
			Collection<? extends INodeView<INodeContent>> selected) {
//		refreshParameterView();
		if (connectionChosen) {
			CursorManager.connectionCursor(true, "connection");
			addNewConnection();
		}
	}

	@Override
	public void unselectObject(ISelectSender<INodeView<INodeContent>> sender,
			Collection<? extends INodeView<INodeContent>> unselected) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	private Composite refreshParameterView() {
		if (renderManager.getSelector().getSelectionCount() == 1) {
			for (Object object : renderManager.getSelector().getSelected()) {
				if (!param.equals(object)) {
					if (object instanceof INodeView<?>) {
						param = new Composite(infoArea, 0);
						GridData gd = new GridData(GridData.FILL_HORIZONTAL);
						param.setLayoutData(gd);
						param.setLayout(new GridLayout());
						param.setBackground(tree.getBackground());
						Label header = new Label(param, SWT.LEAD);
						Label cType = new Label(param, SWT.LEAD);
						cType.setText("Parametertyp");
						Text type = new Text(param, SWT.LEAD);
						Label pos = new Label(param, SWT.LEAD);
						pos.setText("Konstruktorposition");
						Text cPos = new Text(param, SWT.LEAD);
						INodeContent content = ((INodeView<INodeContent>) (object))
								.getModelNode().getContent();
						for (IParam<?> cParam : content
								.getConstructParameterList()) {
							header.setText(cParam.getName());
							type.setText(cParam.getType());
							cPos.setText(Integer
									.toString(((IParamConstruct<?>) (cParam))
											.getPosition()));
						}
						return param;
					}
				}
			}
			return null;
		} else {
			param.dispose();
			return null;
		}
	}

}
