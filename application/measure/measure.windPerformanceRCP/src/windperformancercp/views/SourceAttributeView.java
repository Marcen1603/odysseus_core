package windperformancercp.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.*;


public class SourceAttributeView extends ViewPart {
	
	Text nameInputField;
	
	class labeledDialog extends Composite{
		Label label;
		Text description;
		
		
		labeledDialog(Composite parent, String text, int style){
			super(parent,style);
			FormLayout layout = new FormLayout();
			//Composite comp = new Composite(parent,style);
			this.setLayout(layout);
			
			FormData data = new FormData();
			label = new Label(this, SWT.NONE);
			label.setLayoutData(data);
			label.setText(text);
			data = new FormData();
			data.left = new FormAttachment(label,3);
			data.top = new FormAttachment(label,0,SWT.TOP);
			description = new Text(this, SWT.SINGLE | SWT.BORDER);
			description.setLayoutData(data);
		}
	}
	
	public SourceAttributeView() {
	}
	public static final String ID = "measure.windPerformanceRCP.sourceAttributeView";
	@Override
	public void createPartControl(Composite parent) {
		
		
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		//###upper composite
		
		SashForm upperComposite = new SashForm(mainComposite, SWT.FILL);
		
		Group streamInfoGroup = new Group(upperComposite,SWT.NONE);
		streamInfoGroup.setLayout(new FormLayout());
		streamInfoGroup.setText("Stream Information");
		
		Composite nameComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData nameCompFD = new FormData();
		nameCompFD.top = new FormAttachment(streamInfoGroup,0);
		nameComp.setLayoutData(nameCompFD);
		nameComp.setLayout(new FillLayout());
		Label nameLabel = new Label(nameComp, SWT.BORDER);
		nameLabel.setText("Name:");
		nameInputField = new Text(nameComp, SWT.SINGLE | SWT.BORDER);
		
		Composite hostComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData hostCompFD = new FormData();
		hostCompFD.top = new FormAttachment(nameComp,5);
		hostComp.setLayoutData(hostCompFD);
		hostComp.setLayout(new FillLayout());
		Label hostLabel = new Label(hostComp, SWT.NONE);
		hostLabel.setText("Host:");
		Text hostInputField = new Text(hostComp, SWT.SINGLE | SWT.BORDER);
		
		Composite portComp = new Composite(streamInfoGroup,SWT.NONE);
		FormData portCompFD = new FormData();
		portCompFD.top = new FormAttachment(hostComp,5);
		portComp.setLayoutData(portCompFD);
		portComp.setLayout(new FillLayout());
		Label portLabel = new Label(portComp, SWT.NONE);
		portLabel.setText("Port:");
		Text portInputField = new Text(portComp, SWT.SINGLE | SWT.BORDER);
		
		//### upper right composite: attribute table
		Composite attributeComp = new Composite(upperComposite, SWT.RIGHT);
		//GridLayout attributeCompL = new GridLayout();
		//attributeComp.setLayout(attributeCompL);
		attributeComp.setLayout(new FillLayout());
		
		Table attributeTable = new Table(attributeComp, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION);
		attributeTable.setHeaderVisible(true);
		attributeTable.setLinesVisible(true);
		String[] titles ={"Attribute","Type"};
		for (int i=0;i<titles.length;i++){
			TableColumn col = new TableColumn(attributeTable, SWT.NONE);
			col.setText(titles[i]);
		}
		
				
		//### lower sash form with WT and MetMast Information
		SashForm lowerSash = new SashForm(mainComposite, SWT.FILL);
		//FillLayout llCompoLayout = new FillLayout();
		//lowerSash.setLayoutData(lowerSashLayout);
		
		//## WindTurbine
		//GridLayout llGgridLayout = new GridLayout();

		Group lowerLeftGroup = new Group(lowerSash, SWT.LEFT);
		lowerLeftGroup.setLayout(new FormLayout());
		lowerLeftGroup.setText("WindTurbine");
		
		//# hub height
		Composite hhComp = new Composite(lowerLeftGroup, SWT.FILL);
		FormData hhCompFD = new FormData();
		hhCompFD.top = new FormAttachment(lowerLeftGroup,5);
		hhComp.setLayoutData(hhCompFD);
		hhComp.setLayout(new FillLayout());
		
		Label hubHeightLbl = new Label(hhComp, SWT.NONE);
		hubHeightLbl.setText("Hub height:");
		Text hhInputField = new Text(hhComp, SWT.BORDER | SWT.FILL);
		Label mLabel = new Label(hhComp, SWT.NONE);
		mLabel.setText("m");
		
		//# power control
		Composite pcComposite = new Composite(lowerLeftGroup, SWT.FILL);
		FormData pcCompFD = new FormData();
		pcCompFD.top = new FormAttachment(hhComp,15);
		pcComposite.setLayoutData(pcCompFD);
		pcComposite.setLayout(new FormLayout());
		Label powerControlLbl = new Label(pcComposite, SWT.NONE);
		powerControlLbl.setLayoutData(new FormData());
		
		powerControlLbl.setText("Power Control Type:");
		Button btnRActive =	new Button(pcComposite, SWT.RADIO);
		FormData btnRActiveFD = new FormData();
		btnRActiveFD.top = new FormAttachment(powerControlLbl,7);
		btnRActiveFD.left = new FormAttachment(pcComposite,5,SWT.LEFT);
		btnRActive.setLayoutData(btnRActiveFD);
		btnRActive.setText("active(pitch)");

		Button btnRPassive = new Button(pcComposite, SWT.RADIO);
		FormData btnRPassiveFD = new FormData();
		btnRPassiveFD.left = new FormAttachment(btnRActive,5);
		btnRPassiveFD.top = new FormAttachment(btnRActive,0,SWT.TOP);
		btnRPassive.setLayoutData(btnRPassiveFD);
		btnRPassive.setText("passive(stall)");
		
		//## MetMast
		GridLayout lrGgridLayout = new GridLayout();
		Group lowerRightGroup = new Group(lowerSash, SWT.RIGHT);
		lowerRightGroup.setText("MetMast");
		lowerRightGroup.setLayout(lrGgridLayout);
		
		//### Buttons
		Composite lowerButtonComp = new Composite(mainComposite, SWT.BOTTOM);
		RowLayout lButtonLayout = new RowLayout();
		lButtonLayout.marginRight = 2;
		//lButtonLayout.type = 
		lowerButtonComp.setLayout(lButtonLayout);
		
		
		//FormData btnOKFD = new FormData();
		RowData btnOKRD = new RowData();
		//btnOKFD.right = new FormAttachment(lowerButtonComp,100,SWT.RIGHT);
		Button btnOK = new Button(lowerButtonComp, SWT.PUSH);
		btnOK.setLayoutData(btnOKRD);
		btnOK.setText("&Ok");
		
		//FormData btnCancelFD = new FormData();
		//btnCancelFD.right = new FormAttachment(btnOK,5);
		Button btnCancel = new Button(lowerButtonComp, SWT.PUSH);
		//btnCancel.setLayoutData(btnCancelFD);
		btnCancel.setText("&Cancel");
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		nameInputField.setFocus();

	}

}
