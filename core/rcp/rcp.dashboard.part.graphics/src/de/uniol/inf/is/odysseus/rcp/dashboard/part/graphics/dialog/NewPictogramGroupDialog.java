package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.IPictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group.IPictogramGroup;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group.PictogramGroupRegistry;

public class NewPictogramGroupDialog extends TitleAreaDialog {

	private Collection<IPhysicalOperator> roots;
	private SDFAttribute attribute;
	private IPictogramGroup<?> choosenGroup;
	private Combo comboAttribute;
	private Combo comboVisualize;
	private Composite tableComposite;
	private List<IPictogram> pictograms = new ArrayList<IPictogram>();
	private org.eclipse.swt.widgets.List list;
	private Button btnAddPictogram;
	private Button btnRemovePictogram;	
	private IPhysicalOperator choosenRoot;

	/**
	 * Create the wizard.
	 * 
	 * @param roots
	 */
	public NewPictogramGroupDialog(Shell shell, Collection<IPhysicalOperator> roots) {
		super(shell);
		// TODO: more than one root!
		this.roots = roots;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	protected Control createDialogArea(final Composite parent) {
		setTitle("Add New Pictogram Group");
		parent.setLayout(new GridLayout(1, false));
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(2, false));

		Label lblChooseAttribute = new Label(container, SWT.NONE);
		lblChooseAttribute.setText("Choose Attribute ");

		comboAttribute = new Combo(container, SWT.READ_ONLY);
		GridData gd_comboAttribute = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboAttribute.widthHint = 110;
		comboAttribute.setLayoutData(gd_comboAttribute);

		Label lblChooseVisulize = new Label(container, SWT.NONE);
		lblChooseVisulize.setText("Choose Visualize Grouping");

		comboVisualize = new Combo(container, SWT.READ_ONLY);
		comboVisualize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboVisualize.setEnabled(false);

		tableComposite = new Composite(container, SWT.NONE);
		GridData gd_tableComposite = new GridData(GridData.FILL_BOTH);
		gd_tableComposite.heightHint = 300;
		gd_tableComposite.horizontalSpan = 2;
		tableComposite.setLayoutData(gd_tableComposite);
		tableComposite.setLayout(new GridLayout(1, false));
		
		list = new org.eclipse.swt.widgets.List(tableComposite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list.heightHint = 259;
		gd_list.widthHint = 427;
		list.setLayoutData(gd_list);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnAddPictogram = new Button(composite, SWT.NONE);
		btnAddPictogram.setEnabled(false);
		btnAddPictogram.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewPictogramDialog dialog = new NewPictogramDialog(parent.getShell());
				if(Window.OK==dialog.open()){
					addPictogram(dialog.getPictogram());
				}
			}
		});
		btnAddPictogram.setBounds(0, 10, 140, 25);
		btnAddPictogram.setText("Add Pictogram");

		btnRemovePictogram = new Button(composite, SWT.NONE);
		btnRemovePictogram.setEnabled(false);
		btnRemovePictogram.setBounds(145, 10, 140, 25);
		btnRemovePictogram.setText("Remove Pictogram");
		btnRemovePictogram.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {			
				for(int nr : list.getSelectionIndices()){
					removePictogram(nr);
				}
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		for (IPhysicalOperator r : roots) {
			for (SDFAttribute attribute : r.getOutputSchema()) {
				comboAttribute.add(attribute.getAttributeName());
				comboAttribute.setData(attribute.getAttributeName(), attribute);
				choosenRoot = r;
			}
		}
		comboAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = ((Combo) e.getSource());
				if (!c.getText().isEmpty()) {
					attribute = (SDFAttribute) c.getData(c.getText());
				} else {
					attribute = null;
				}
				updateComboVisualize();
			}

		});

		comboVisualize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = ((Combo) e.getSource());
				if (!c.getText().isEmpty()) {
					// TODO: clone!!!
					choosenGroup = PictogramGroupRegistry.getGroups().get(c.getText());
					choosenGroup.setAttribute(attribute);
				} else {
					choosenGroup = null;				
				}
				updateList();
			}
		});

		return container;

	}

	protected void removePictogram(int i) {
		this.pictograms.remove(i);
		this.list.remove(i);
	}

	protected void addPictogram(IPictogram pictogram) {
		this.pictograms.add(pictogram);
		this.list.add(pictogram.toString());		
	}

	private void updateComboVisualize() {
		if (attribute != null) {
			comboVisualize.removeAll();
			for (Entry<String, IPictogramGroup<?>> entry : PictogramGroupRegistry.getGroups().entrySet()) {
				if (entry.getValue().getAllowedDatatypes().contains(attribute.getDatatype())) {
					comboVisualize.add(entry.getKey());
				}
			}
			if (comboVisualize.getItemCount() == 0) {
				comboVisualize.setEnabled(false);
				setErrorMessage("No suitable visualizing group");
			} else {
				comboVisualize.setEnabled(true);
			}
		}
	}
	
	
	private void updateList(){
		if(choosenGroup!=null){
			btnAddPictogram.setEnabled(true);
			btnRemovePictogram.setEnabled(true);
		}else{
			btnAddPictogram.setEnabled(false);
			btnRemovePictogram.setEnabled(false);
		}
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	public IPictogramGroup<?> getChoosenGroup() {
		return choosenGroup;
	}

	public void setChoosenGroup(IPictogramGroup<?> choosenGroup) {
		this.choosenGroup = choosenGroup;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 600);
	}

	@Override
	protected void okPressed() {
		for(IPictogram pg : this.pictograms){
			choosenGroup.addPictogram(pg);
		}		
		super.okPressed();
	}

	public IPhysicalOperator getChoosenRoot() {
		return choosenRoot;
	}

	public void setChoosenRoot(IPhysicalOperator choosenRoot) {
		this.choosenRoot = choosenRoot;
	}
	
}
