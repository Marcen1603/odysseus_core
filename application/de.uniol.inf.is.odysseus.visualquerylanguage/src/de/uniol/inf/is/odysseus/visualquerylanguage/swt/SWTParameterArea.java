package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;

public class SWTParameterArea {
	
	private final Logger log = LoggerFactory.getLogger(SWTParameterArea.class);
	
	private Composite comp;
	private INodeView<INodeContent> nodeView;
	
	public SWTParameterArea(Composite parent, INodeView<INodeContent> nodeView) {
		
		this.nodeView = nodeView;
		INodeContent content = nodeView.getModelNode().getContent();
		
		comp = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		comp.setLayout( gridLayout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL);
		comp.setLayoutData( data );
		
		Label nameLabel = new Label(comp, SWT.LEFT);
		if(content instanceof DefaultSourceContent) {
			nameLabel.setText("Sourcename: " + content.getName());
		}else if(content instanceof DefaultSinkContent) {
			nameLabel.setText("Sinkname: " + content.getName());
		}else if(content instanceof DefaultPipeContent) {
			nameLabel.setText("Pipename: " + content.getName());
		}
		
		Label typLabel = new Label(comp, SWT.LEFT);
		typLabel.setText("Typ: " + content.getTyp());
		
		final Table table = new Table(comp, SWT.BORDER);
		
		TableColumn tc1 = new TableColumn(table, SWT.LEFT);
	    TableColumn tc2 = new TableColumn(table, SWT.LEFT);
	    tc1.setText("Name");
	    tc2.setText("Wert");
	    tc1.setWidth(70);
	    tc2.setWidth(70);
	    
	    final TableEditor editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.minimumWidth = 70;
	    final int EDITABLECOLUMN = 1;

	    table.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	        Control oldEditor = editor.getEditor();
	        if (oldEditor != null)
	          oldEditor.dispose();

	        TableItem item = (TableItem) e.item;
	        if (item == null)
	          return;

	        Text newEditor = new Text(table, SWT.NONE);
	        newEditor.setText(item.getText(EDITABLECOLUMN));
	        newEditor.addModifyListener(new ModifyListener() {
	          public void modifyText(ModifyEvent me) {
	            Text text = (Text) editor.getEditor();
	            editor.getItem()
	                .setText(EDITABLECOLUMN, text.getText());
	          }
	        });
	        System.out.println(item.getData());
	        newEditor.selectAll();
	        newEditor.setFocus();
	        editor.setEditor(newEditor, item, EDITABLECOLUMN);
	      }
	    });
	    
	    table.setHeaderVisible(true);

	    TableItem pcItem;
	    TableItem scItem;
	    
	    
		for (IParamConstruct<?> cParam : content.getConstructParameterList()) {
			pcItem = new TableItem(table, SWT.NONE);
			String value = "";
			if(cParam.getValue() != null) {
				value = cParam.getValue().toString();
			}
			pcItem.setText(new String[] { cParam.getName(), value});
			pcItem.setData(cParam);
		}
		
		for (IParamSetter<?> sParam : content.getSetterParameterList()) {
			scItem = new TableItem(table, SWT.NONE);
			String value = "";
			if(sParam.getValue() != null) {
				sParam.getValue().toString();
			}
			scItem.setText(new String[] { sParam.getName(), value});
			scItem.setData(sParam);
		}
	}
	
	public void dispose() {
		this.comp.dispose();
		
		log.debug("ParameterArea for " + nodeView + " disposed");
	}

}
