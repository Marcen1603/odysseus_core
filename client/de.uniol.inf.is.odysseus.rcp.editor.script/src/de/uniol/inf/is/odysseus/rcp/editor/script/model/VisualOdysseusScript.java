package de.uniol.inf.is.odysseus.rcp.editor.script.model;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlockProvider;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptBlockCollapseStatus;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.script.provider.VisualOdysseusScriptBlockProviderRegistry;
import de.uniol.inf.is.odysseus.rcp.util.ClickableImage;
import de.uniol.inf.is.odysseus.rcp.util.DropDownSelectionListener;
import de.uniol.inf.is.odysseus.rcp.util.DropDownSelectionListener.ICallback;
import de.uniol.inf.is.odysseus.rcp.util.IImageClickHandler;

public class VisualOdysseusScript {

	private static final Logger LOG = LoggerFactory.getLogger(VisualOdysseusScript.class);
	private static final String NO_TITLE_TEXT = "<No title>";

	private final Composite parent;
	private final IVisualOdysseusScriptContainer container;
	private final VisualOdysseusScriptModel scriptModel;

	private final VisualOdysseusScriptBlockCollapseStatus collapseStatus = new VisualOdysseusScriptBlockCollapseStatus();
	private final Map<IVisualOdysseusScriptBlock, IImageClickHandler> handlerMap = Maps.newHashMap();
	
	private ToolBar toolBar;

	public VisualOdysseusScript(Composite parent, VisualOdysseusScriptModel scriptModel, IVisualOdysseusScriptContainer container) {
		Objects.requireNonNull(scriptModel, "scriptModel must not be null!");
		Objects.requireNonNull(parent, "parent must not be null!");
		Objects.requireNonNull(container, "container must not be null!");

		this.parent = parent;
		this.scriptModel = scriptModel;
		this.container = container;

		createContents();
	}

	private void createContents() {
		disposeContents();
		
		if( toolBar == null ) {
			toolBar = createToolBar();
		}

		List<IVisualOdysseusScriptBlock> visualTextBlocks = scriptModel.getVisualTextBlocks();

		collapseStatus.prepareSize(visualTextBlocks.size());
		collapseStatus.clearBlocks();
		handlerMap.clear();
		
		for (int index = 0; index < visualTextBlocks.size(); index++) {
			IVisualOdysseusScriptBlock visualBlock = visualTextBlocks.get(index);

			createBlockContents(index, visualBlock);
		}		
	}

	private void createBlockContents(int blockIndex, IVisualOdysseusScriptBlock visualBlock) {
		ImageManager imageManager = VisualOdysseusScriptPlugIn.getImageManager();

		Composite topBlockComposite = new Composite(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = parent.getBounds().width;
		topBlockComposite.setLayoutData(gd);
		topBlockComposite.setLayout(new GridLayout(5, false));
		topBlockComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));

		ClickableImage resizeImage = createClickableImageWithToolTip(topBlockComposite, "collapse", "collapse");
		Label titleLabel = createTitleHeader(visualBlock, topBlockComposite);
		ClickableImage moveUpImage = createClickableImageWithToolTip(topBlockComposite, "moveUp", "Move up");
		ClickableImage moveDownImage = createClickableImageWithToolTip(topBlockComposite, "moveDown", "Move down");
		ClickableImage deleteImage = createClickableImageWithToolTip(topBlockComposite, "remove", "Delete");

		Composite visualBlockComposite = new Composite(parent, SWT.NONE);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.widthHint = parent.getBounds().width;
		visualBlockComposite.setLayoutData(gd2);
		visualBlockComposite.setLayout(new FillLayout());

		visualBlock.createPartControl(visualBlockComposite, new IVisualOdysseusScriptContainer() {
			@Override
			public void setTitleText(String title) {
				titleLabel.setText(title);
			}

			@Override
			public void setDirty(boolean dirty) {
				// delegate
				container.setDirty(dirty);
			}

			@Override
			public void layoutAll() {
				// delegate
				container.layoutAll();
			}

			@Override
			public IFile getFile() {
				// delegate
				return container.getFile();
			}
		});

		IImageClickHandler handler = new IImageClickHandler() {
			private int oldHeight;

			@Override
			public void onClick() {
				if (gd2.heightHint == 0) {
					gd2.heightHint = oldHeight;
					resizeImage.getLabel().setImage(imageManager.get("collapse"));
					resizeImage.getLabel().setToolTipText("Collapse");
					collapseStatus.setStatus(visualBlock, false);
				} else {
					oldHeight = gd2.heightHint;
					gd2.heightHint = 0;
					resizeImage.getLabel().setImage(imageManager.get("expand"));
					resizeImage.getLabel().setToolTipText("Expand");
					collapseStatus.setStatus(visualBlock, true);
				}
				container.layoutAll();
			}
		};
		
		resizeImage.setClickHandler(handler);
		handlerMap.put(visualBlock, handler);
		
		collapseStatus.setIndex(visualBlock, blockIndex);
		if( collapseStatus.getStatus(visualBlock)) {
			// click one time to collapse programmatically
			resizeImage.getClickHandler().onClick();
		}
		
		moveUpImage.setClickHandler(new IImageClickHandler() {
			@Override
			public void onClick() {
				try {
					if (scriptModel.moveUp(visualBlock)) {
						// model changed now, so we have to create the gui
						// again and update the collapsing status accordingly
						collapseStatus.moveUp(visualBlock);
						
						createContents();
						container.layoutAll();
						container.setDirty(true);
					}
				} catch (VisualOdysseusScriptException e) {
					LOG.error("Could not move visual odysseus script block up", e);
				}
			}
		});

		moveDownImage.setClickHandler(new IImageClickHandler() {
			@Override
			public void onClick() {
				try {
					if (scriptModel.moveDown(visualBlock)) {
						// model changed now, so we have to create the gui
						// again and update the collapsing status accordingly
						collapseStatus.moveDown(visualBlock);
						
						createContents();
						container.layoutAll();
						container.setDirty(true);
					}
				} catch (VisualOdysseusScriptException e) {
					LOG.error("Could not move visual odysseus script block up", e);
				}
			}
		});

		deleteImage.setClickHandler(new IImageClickHandler() {
			@Override
			public void onClick() {
				scriptModel.dispose(visualBlock);
				collapseStatus.dispose(visualBlock);

				visualBlockComposite.dispose();
				topBlockComposite.dispose();

				container.setDirty(true);
				container.layoutAll();
			}
		});
	}

	private ToolBar createToolBar() {
		ImageManager imageManager = VisualOdysseusScriptPlugIn.getImageManager();

		ToolBar toolBar = new ToolBar(parent, SWT.NONE);
		
		ToolItem collapseAllItem = new ToolItem( toolBar, SWT.PUSH);
		collapseAllItem.setImage(imageManager.get("collapseAll"));
		collapseAllItem.setToolTipText("Collapse all");
		collapseAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( IVisualOdysseusScriptBlock visualBlock : scriptModel.getVisualTextBlocks() ) {
					if( !collapseStatus.getStatus(visualBlock)) {
						handlerMap.get(visualBlock).onClick();
					}
				}
			}
		});

		ToolItem expandAllItem = new ToolItem( toolBar, SWT.PUSH);
		expandAllItem.setImage(imageManager.get("expandAll"));
		expandAllItem.setToolTipText("Expand all");
		expandAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( IVisualOdysseusScriptBlock visualBlock : scriptModel.getVisualTextBlocks() ) {
					if( collapseStatus.getStatus(visualBlock)) {
						handlerMap.get(visualBlock).onClick();
					}
				}				
			}
		});
		
		ToolItem addItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		addItem.setImage(imageManager.get("add"));
		addItem.setToolTipText("Add");
		DropDownSelectionListener addListener = new DropDownSelectionListener(addItem);
		VisualOdysseusScriptBlockProviderRegistry registry = VisualOdysseusScriptBlockProviderRegistry.getInstance();
		for( String provName : registry.getProviderNames()) {
			IVisualOdysseusScriptBlockProvider prov = registry.getProvider(provName).get();
			addListener.add(provName, prov.getImage(), new ICallback() {
				@Override
				public void itemSelected() {
					Optional<IVisualOdysseusScriptBlock> optBlock = registry.create(provName);
					if( optBlock.isPresent() ) {
						addBlock(optBlock.get());
						
						container.setDirty(true);
						container.layoutAll();
					}
				}
			});
		}
		return toolBar;
	}

	private void addBlock(IVisualOdysseusScriptBlock newBlock) {
		scriptModel.addBlock(newBlock);
		List<IVisualOdysseusScriptBlock> visualTextBlocks = scriptModel.getVisualTextBlocks();

		collapseStatus.prepareSize(visualTextBlocks.size());
		createBlockContents(visualTextBlocks.size() - 1, newBlock);
	}
	
	private static Label createTitleHeader(IVisualOdysseusScriptBlock visualBlock, Composite topBlockComposite) {
		String title = visualBlock.getTitle();
		Label titleLabel = new Label(topBlockComposite, SWT.NONE);
		titleLabel.setText(Strings.isNullOrEmpty(title) ? NO_TITLE_TEXT : title);
		titleLabel.setAlignment(SWT.CENTER);
		titleLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
		titleLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return titleLabel;
	}

	private static ClickableImage createClickableImageWithToolTip(Composite parent, String imageID, String tooltip) {
		ClickableImage resizeImage = new ClickableImage(parent, VisualOdysseusScriptPlugIn.getImageManager().get(imageID));
		resizeImage.getLabel().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
		resizeImage.getLabel().setToolTipText(tooltip);
		return resizeImage;
	}

	private void disposeContents() {
		if (!parent.isDisposed()) {
			for (Control child : parent.getChildren()) {
				if (!child.isDisposed() && !(child instanceof ToolBar)) {
					child.dispose();
				}
			}
		}
	}

	public void dispose() {
		scriptModel.dispose();

		disposeContents();
	}
	
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		return scriptModel.generateOdysseusScript();
	}
}
