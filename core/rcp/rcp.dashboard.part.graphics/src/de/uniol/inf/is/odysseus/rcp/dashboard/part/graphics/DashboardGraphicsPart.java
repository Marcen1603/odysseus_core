package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog.NewPictogramGroupDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group.IPictogramGroup;

public class DashboardGraphicsPart extends AbstractDashboardPart {

	private static final String BACKGROUND_FILE = "BACKGROUND_FILE";
	private String backgroundfile;
	private boolean bgResize = true;
	private boolean bgFitIn = true;
	private Display display;
	private Image bgimg;
	private Composite drawingContainer;
	private Label bgLabel;
	private Collection<IPhysicalOperator> roots;
	private Map<IPictogramGroup<?>, Integer> groups = new HashMap<>();
	private Composite mainContainer;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		display = parent.getDisplay();
		//parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		// main container to allow stacked positioning --> it forces all to same location and size!
		mainContainer = new Composite(parent, SWT.NONE);
		mainContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		StackLayout mainContainerLayout = new StackLayout();
		mainContainer.setLayout(mainContainerLayout);
		mainContainer.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		
		// load background image	
		Composite backgroundContainer = new Composite(mainContainer, SWT.NONE);		
		backgroundContainer.setBounds(mainContainer.getBounds());			
		backgroundContainer.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		backgroundContainer.setLayout(new GridLayout(1, false));
		bgLabel = new Label(backgroundContainer, SWT.NONE);		
		bgimg = new Image(display, new ImageData(backgroundfile));
		bgLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		bgLabel.setImage(bgimg);
		if (this.bgResize) {
			resizeBackground();
			bgLabel.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					resizeBackground();
				}
			});
		}
		
		// the container where we draw our pictograms
		drawingContainer = new Composite(mainContainer, SWT.TRANSPARENCY_ALPHA);
		drawingContainer.setBackgroundMode(SWT.INHERIT_FORCE);
		// and this is our top-layout
		mainContainerLayout.topControl = drawingContainer;
		parent.layout();
		
		
		// add some buttons to the toolbar
		addButtons(toolbar);
	}

	private void addButtons(final ToolBar toolbar) {
		ToolItem itemPush = new ToolItem(toolbar, SWT.PUSH);
		itemPush.setText("Add Pictogram");
		itemPush.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewPictogramGroupDialog dialog = new NewPictogramGroupDialog(toolbar.getShell(), roots);
				if (Window.OK == dialog.open()) {
					IPictogramGroup<?> pgroup = dialog.getChoosenGroup();
					pgroup.init(drawingContainer);
					int position = dialog.getChoosenRoot().getOutputSchema().indexOf(pgroup.getAttribute());
					groups.put(pgroup, position);
					mainContainer.layout();
				}
			}
		});

	}

	private void resizeBackground() {
		int width = bgLabel.getSize().x;
		int height = bgLabel.getSize().y;
		if (width > 0 && height > 0) {
			Image img = loadImage(this.backgroundfile, width, height, bgFitIn);
			bgLabel.setImage(img);
			bgimg.dispose();
			bgimg = img;
		}
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
		for (Entry<IPictogramGroup<?>, Integer> entry : this.groups.entrySet()) {
			int pos = entry.getValue();
			IPictogramGroup<Object> group = (IPictogramGroup<Object>) entry.getKey();
			group.process(tuple.getAttribute(pos));			
		}
		drawingContainer.layout();

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
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> save = super.onSave();
		save.put(BACKGROUND_FILE, this.backgroundfile);
		return save;
	}

	public String getBackgroundFile() {
		return backgroundfile;
	}

	public void setBackgroundFile(String backgroundfile) {
		this.backgroundfile = backgroundfile;
	}

	private Image loadImage(String filename, int maxWidth, int maxHeight, boolean keepRatio) {
		ImageData data = new ImageData(filename);
		if (keepRatio) {
			double targetRatio = maxWidth / maxHeight;
			double sourceRatio = data.width / data.height;
			int requiredWidth = maxWidth;
			int requiredHeight = maxHeight;
			if (sourceRatio >= targetRatio) {
				requiredWidth = maxWidth;
				requiredHeight = (int) (requiredWidth / sourceRatio);
			} else {
				requiredHeight = maxHeight;
				requiredWidth = (int) (requiredHeight * sourceRatio);
			}
			data = data.scaledTo(requiredWidth, requiredHeight);
		} else {
			data = data.scaledTo(maxWidth, maxHeight);
		}
		return new Image(display, data);
	}

	public void setRoots(Collection<IPhysicalOperator> roots) {
		this.roots = roots;

	}

}
