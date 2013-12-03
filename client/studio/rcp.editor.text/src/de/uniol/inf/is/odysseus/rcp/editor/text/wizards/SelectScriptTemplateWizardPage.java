package de.uniol.inf.is.odysseus.rcp.editor.text.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;
import de.uniol.inf.is.odysseus.rcp.editor.text.templates.OdysseusScriptTemplateRegistry;

public class SelectScriptTemplateWizardPage extends WizardPage {

	private static final String DEFAULT_TEMPLATE_CFG_KEY = "defaultScriptTemplate";
	private static final OdysseusScriptTemplateRegistry TEMPLATE_REGISTRY = OdysseusScriptTemplateRegistry.getInstance();
	
	private final IOdysseusScriptTemplate defaultTemplate;
	
	private Text descriptionText;
	private Text scriptText;
	private Combo templateNameChooser;
	

	protected SelectScriptTemplateWizardPage(String pageName) {
		super(pageName);
		
		setTitle("Template selection");
		setDescription("Choose a template for your odysseus script to be preentered.");
		
		defaultTemplate = determineDefaultTemplate();
	}

	private static IOdysseusScriptTemplate determineDefaultTemplate() {
		Optional<String> optDefaultTemplateName = determineDefaultTemplateFromConfig();
		if( optDefaultTemplateName.isPresent() ) {
			return TEMPLATE_REGISTRY.getTemplate(optDefaultTemplateName.get());
		}
		
		return TEMPLATE_REGISTRY.getTemplate(determineFirstTemplateName());
	}

	private static String determineFirstTemplateName() {
		ImmutableCollection<String> templateNames = TEMPLATE_REGISTRY.getTemplateNames();
		
		for( String templateName : templateNames ) {
			if( !templateName.equals(OdysseusScriptTemplateRegistry.EMPTY_TEMPLATE_NAME)) {
				return templateName;
			}
		}
		
		return OdysseusScriptTemplateRegistry.EMPTY_TEMPLATE_NAME;
	}

	private static Optional<String> determineDefaultTemplateFromConfig() {
		String template = OdysseusRCPConfiguration.get(DEFAULT_TEMPLATE_CFG_KEY, "PQL Basic");
		if( template.equals("PQL Basic")) {
			if( TEMPLATE_REGISTRY.isRegistered(template)) {
				return Optional.of("PQL Basic");
			}
			return Optional.absent();
		}
		
		return Optional.of(template);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));
		
		createLabel(rootComposite, "Template");
		templateNameChooser = createTemplateNameChooser(rootComposite);
		createLabel(rootComposite, "Description");
		descriptionText = createDescriptionText(rootComposite);
		createLabel(rootComposite, "Script");
		scriptText = createScriptText(rootComposite);
		
		templateNameChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo)e.widget;
				selectTemplate(combo.getItem(combo.getSelectionIndex()));
			}
		});
		selectTemplate(templateNameChooser.getItem(templateNameChooser.getSelectionIndex()));
		
		finishCreation(rootComposite);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if( visible == true ) {
			setPageComplete(true);
		}
	}
	
	public IOdysseusScriptTemplate getSelectedTemplate() {
		return TEMPLATE_REGISTRY.getTemplate(templateNameChooser.getItem(templateNameChooser.getSelectionIndex()));
	}
	
	private void selectTemplate(String templateName) {
		IOdysseusScriptTemplate template = TEMPLATE_REGISTRY.getTemplate(templateName);
		
		String description = template.getDescription();
		descriptionText.setText(description != null ? description : "");
		
		String script = template.getText();
		scriptText.setText(script != null ? script : "");
	}

	private Text createScriptText(Composite rootComposite) {
		Text text = new Text(rootComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.BORDER );
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		text.setText(defaultTemplate.getText());
		
		return text;
	}

	private Text createDescriptionText(Composite rootComposite) {
		Text text = new Text(rootComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.BORDER );
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 150;
		text.setLayoutData(gd);
		
		text.setText(defaultTemplate.getDescription());
		
		return text;
	}

	private static Label createLabel(Composite rootComposite, String text) {
		Label label = new Label(rootComposite, SWT.NONE);
		label.setText(text != null ? text : "");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return label;
	}

	private Combo createTemplateNameChooser(Composite rootComposite) {
		OdysseusScriptTemplateRegistry registry = TEMPLATE_REGISTRY;
		ImmutableCollection<String> templateNames = registry.getTemplateNames();

		Combo templateNameChooser = createCombo(rootComposite, templateNames.toArray(new String[0]), defaultTemplate.getName());
		templateNameChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return templateNameChooser;
	}

	private static Combo createCombo(Composite topComposite, String[] items, String selectedItem) {
		Combo combo = new Combo(topComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (items != null) {
			combo.setItems(items);
			if (selectedItem != null) {
				int index = indexOf(items, selectedItem);
				if (index != -1) {
					combo.select(index);
				}
			}
		}
		return combo;
	}
	
	private static int indexOf(String[] items, String selectedItem) {
		for( int i = 0; i < items.length; i++ ) {
			if( items[i].equalsIgnoreCase(selectedItem)) {
				return i;
			}
		}
		return -1;
	}
	
	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}
	
}
