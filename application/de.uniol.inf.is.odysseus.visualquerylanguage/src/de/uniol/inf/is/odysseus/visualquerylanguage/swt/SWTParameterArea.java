package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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

public class SWTParameterArea {
	
	private final Logger log = LoggerFactory.getLogger(SWTParameterArea.class);
	
	private Composite comp;
	private INodeView<INodeContent> nodeView;
	
	public SWTParameterArea(Composite parent, INodeView<INodeContent> nodeView) {
		
		this.nodeView = nodeView;
		INodeContent content = nodeView.getModelNode().getContent();
		
		comp = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		comp.setLayout( gridLayout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL);
		comp.setLayoutData( data );
		
		Label nameLabel = new Label(comp, SWT.LEFT);
		if(content instanceof DefaultSourceContent) {
			nameLabel.setText("Sourcename: ");
		}else if(content instanceof DefaultSinkContent) {
			nameLabel.setText("Sinkname: ");
		}else if(content instanceof DefaultPipeContent) {
			nameLabel.setText("Pipename: ");
		}
		
		
		Text modelName = new Text( comp, SWT.SINGLE );
		modelName.setText( content.getName());
		modelName.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		modelName.setEditable(false);
		
		Label typLabel = new Label(comp, SWT.LEFT);
		typLabel.setText("Typ: ");
		
		Text modelTyp = new Text(comp, SWT.SINGLE);
		modelTyp.setText(content.getTyp());
		modelTyp.setLayoutData( new GridData(GridData.FILL_HORIZONTAL) );
		modelTyp.setEditable(false);
		
		Table table = new Table(comp, SWT.BORDER);
		
		TableColumn tc1 = new TableColumn(table, SWT.CENTER);
	    TableColumn tc2 = new TableColumn(table, SWT.CENTER);
	    TableColumn tc3 = new TableColumn(table, SWT.CENTER);
	    TableColumn tc4 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Name");
	    tc2.setText("Typ");
	    tc3.setText("Wert des Typs");
	    tc4.setText("Position");
	    tc1.setWidth(70);
	    tc2.setWidth(70);
	    tc3.setWidth(80);
	    table.setHeaderVisible(true);

	    TableItem pcItem;
	    
	    
		for (IParamConstruct<?> param : content.getConstructParameterList()) {
			pcItem = new TableItem(table, SWT.NONE);
			pcItem.setText(new String[] { param.getName(), param.getType(), param.getValue().toString(), Integer.toString(param.getPosition()) });
		}
	}
	
	public void dispose() {
		this.comp.dispose();
		
		log.debug("ParameterArea for " + nodeView + " disposed");
	}

}
