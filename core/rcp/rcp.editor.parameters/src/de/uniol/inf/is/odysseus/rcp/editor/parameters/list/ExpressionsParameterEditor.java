package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractListParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingSupport;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class ExpressionsParameterEditor extends AbstractListParameterEditor<SDFExpression> implements IParameterEditor {

	private static class ExpressionString {
		public String expression = "";
		
		public ExpressionString( String expr ) {
			expression = expr;
		}
	}
	
	private TableViewer tableViewer;
	private List<ExpressionString> expressions = new ArrayList<ExpressionString>();

	@Override
	public void createControl(Composite parent) {
		
		GridLayout gridLayout = new GridLayout();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gridLayout);

		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setText(getListParameter().getName());
		titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		load();
		createTable( container );
		createButtons( container );
	}
	
	private void createTable(Composite parent) {
		// Layout
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Tabelle erzeugen
		tableViewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		// Spalte: Expression
		TableViewerColumn originalNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		originalNameColumn.getColumn().setText("Input");
		tableLayout.setColumnData(originalNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		originalNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				ExpressionString item = (ExpressionString) cell.getElement();
				cell.setText(item.expression);
			}
		});
		originalNameColumn.setEditingSupport(new TextEditingSupport(tableViewer) {

			@Override
			protected void doSetValue(Object element, Object value) {
				((ExpressionString)element).expression = value.toString();
				save();
			}

			@Override
			protected Object doGetValue(Object element) {
				return ((ExpressionString)element).expression;
			}
			
		});

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(expressions);

	}
	
	private void createButtons(Composite container) {
		Composite buttonContainer = new Composite(container, SWT.NONE);
		buttonContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		buttonContainer.setLayout(gridLayout);
		
		Button addButton = new Button( buttonContainer, SWT.PUSH);
		addButton.setImage( Activator.getDefault().getImageRegistry().get("addIcon"));
		addButton.setToolTipText("Add aggregation");
		addButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewRow();
				
				refreshAll();
				save();
			}
		});
		
		Button removeButton = new Button( buttonContainer, SWT.PUSH );
		removeButton.setImage( Activator.getDefault().getImageRegistry().get("removeIcon"));
		removeButton.setToolTipText("Remove aggregation");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				ExpressionString item = (ExpressionString)selection.getFirstElement();
				
				expressions.remove(item);
				
				// neu zeichnen
				refreshAll();
				
				save();
			}
		});
		
	}
	
	protected void addNewRow() {
		expressions.add(new ExpressionString("New_Expression"));
		refreshAll();
		save();
	}
	
	private void refreshAll() {
		tableViewer.refresh();
		getView().layout();
	}

	private void save() {
		ArrayList<String> exprList = new ArrayList<String>();
		
		for( ExpressionString expr : expressions) {
			exprList.add(expr.expression);
		}
		if( !exprList.isEmpty())
			setValue(exprList);
		else
			setValue(null);
	}
	
	private void load() {
		List<SDFExpression> exprs = getValue();
		
		if( exprs != null )
			for( int i = 0; i < exprs.size(); i++ ) 
				expressions.add( new ExpressionString(exprs.get(i).toString()) );
	}

	@Override
	public void close() {
		save();
	}

}
