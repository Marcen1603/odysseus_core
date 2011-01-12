package windperformancercp.views;

import windperformancercp.controller.IController;
import windperformancercp.controller.SourceController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.sources.Attribute;

public class AttributeDialogPresenter extends EventHandler implements IPresenter{
	AttributeDialog dialog;
	IController _cont;
	Attribute actAtt;

	public AttributeDialogPresenter(AttributeDialog dialog){
		this.actAtt = new Attribute("",Attribute.AttributeType.VARIOUS);
		this.dialog = dialog;
		_cont = SourceController.getInstance(this);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}
	
	public void nameEntered(){
		actAtt.setName(dialog.getNameValue());
	}
	
	public void typeSelected(){ //TODO throws exception
		try{
			actAtt.setAttType(Attribute.AttributeType.valueOf(dialog.getComboValue()));
		}
		catch(Exception e){
			//
		}
	}
	
	public void okPressed(){
		if(! dialog.getNameValue().equals("")){
			actAtt = new Attribute(actAtt); //fuer Zuweisung des Datatyps
			
			fire(new InputDialogEvent(this, InputDialogEventType.NewAttributeItem, actAtt));
			System.out.println(this.toString()+"fired new attribute event");
			dialog.close();
			fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
		}
	}
	
	public void cancelPressed(){
		dialog.close();
		fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
	}
}
