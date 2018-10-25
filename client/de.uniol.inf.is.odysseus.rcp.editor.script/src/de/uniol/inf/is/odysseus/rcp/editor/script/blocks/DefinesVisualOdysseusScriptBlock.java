package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;
import java.util.Objects;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.impl.VisualOdysseusScriptPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ImageButton;

public class DefinesVisualOdysseusScriptBlock implements IVisualOdysseusScriptBlock, IDefinitionsListListener {

	private static final String TITLE = "Definitions";

	private final DefinitionsList definitionsList = new DefinitionsList();

	private IVisualOdysseusScriptContainer container;
	private DefinitionsTableViewer tableViewer;

	public DefinesVisualOdysseusScriptBlock(List<Definition> keyValuePairs) {
		Objects.requireNonNull(keyValuePairs, "keyValuePairs must not be null!");

		definitionsList.addAll(keyValuePairs);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		this.container = container;

		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout());

		Composite contentComposite = new Composite(topComposite, SWT.NONE);
		contentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout contentLayout = new GridLayout(2, false);
		contentLayout.horizontalSpacing = 0;
		contentComposite.setLayout(contentLayout);

		Composite buttonComposite = new Composite(contentComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginTop = -5;
		buttonComposite.setLayout(gridLayout);
		GridData buttonCompositeGridData = new GridData();
		buttonCompositeGridData.verticalAlignment = SWT.TOP;
		buttonComposite.setLayoutData(buttonCompositeGridData);

		tableViewer = new DefinitionsTableViewer(contentComposite, definitionsList);

		/*** Buttons ***/
		ImageButton addButton = createImageButtonWithLayout(buttonComposite, "add", "Add new definition");
		addButton.getButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				definitionsList.addNewDefinition();
			}
		});

		ImageButton removeButton = createImageButtonWithLayout(buttonComposite, "remove", "Remove definition");
		removeButton.getButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(definitionsList.size() == 1 ) {
					// do not remove the last one
					return;
				}
				
				Optional<Definition> optPair = tableViewer.getSelection();
				if (optPair.isPresent()) {
					definitionsList.remove(optPair.get());
				}
			}
		});

		ImageButton moveUpButton = createImageButtonWithLayout(buttonComposite, "moveUp", "Move definition up");
		moveUpButton.getButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<Definition> optPair = tableViewer.getSelection();
				if (optPair.isPresent()) {
					definitionsList.moveUp(optPair.get());
				}
			}
		});

		ImageButton moveDownButton = createImageButtonWithLayout(buttonComposite, "moveDown", "Move definition down");
		moveDownButton.getButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<Definition> optPair = tableViewer.getSelection();
				if (optPair.isPresent()) {
					definitionsList.moveDown(optPair.get());
				}
			}
		});
		
		definitionsList.addListener(this);			
	}

	@Override
	public void definitionListChanged(DefinitionsList sender) {
		markChanged();
	}

	@Override
	public void dispose() {
		tableViewer.dispose();
		definitionsList.removeListener(this);
	}

	private static ImageButton createImageButtonWithLayout(Composite parent, String imageKey, String tooltipText) {
		ImageButton imageButton = new ImageButton(parent, VisualOdysseusScriptPlugIn.getImageManager().get(imageKey), tooltipText);
		imageButton.getButton().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return imageButton;
	}

	private void markChanged() {
		container.layoutAll();
		container.setDirty(true);
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder scriptBuilder = new StringBuilder();

		for (int i = 0; i < definitionsList.size(); i++) {
			Definition pair = definitionsList.get(i);

			if (i != 0) {
				scriptBuilder.append("\n");
			}

			scriptBuilder.append("#DEFINE ").append(pair.getE1()).append(" ").append(pair.getE2());
		}

		return scriptBuilder.toString();
	}
}
