package de.uniol.inf.is.odysseus.rcp.editor.script.model;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptBlockCollapseStatus;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ClickableImage;
import de.uniol.inf.is.odysseus.rcp.util.IImageClickHandler;

public class VisualOdysseusScript {

	private static final Logger LOG = LoggerFactory.getLogger(VisualOdysseusScript.class);
	private static final String NO_TITLE_TEXT = "<No title>";

	private final Composite parent;
	private final VisualOdysseusScriptModel scriptModel;

	private final VisualOdysseusScriptBlockCollapseStatus collapseStatus = new VisualOdysseusScriptBlockCollapseStatus();

	public VisualOdysseusScript(Composite parent, VisualOdysseusScriptModel scriptModel, IVisualOdysseusScriptContainer container) {
		Preconditions.checkNotNull(scriptModel, "scriptModel must not be null!");
		Preconditions.checkNotNull(parent, "parent must not be null!");
		Preconditions.checkNotNull(container, "container must not be null!");

		this.parent = parent;
		this.scriptModel = scriptModel;

		createContents(container);
	}

	private void createContents(IVisualOdysseusScriptContainer container) {
		disposeContents();

		ImageManager imageManager = VisualOdysseusScriptPlugIn.getImageManager();
		List<IVisualOdysseusScriptBlock> visualTextBlocks = scriptModel.getVisualTextBlocks();

		collapseStatus.prepareSize(visualTextBlocks.size());
		collapseStatus.clearBlocks();
		
		for (int index = 0; index < visualTextBlocks.size(); index++) {
			IVisualOdysseusScriptBlock visualBlock = visualTextBlocks.get(index);

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
			});

			resizeImage.setClickHandler(new IImageClickHandler() {

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
			});
			collapseStatus.setIndex(visualBlock, index);
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
							
							createContents(container);
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
							
							createContents(container);
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
				if (!child.isDisposed()) {
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
