package windperformancercp.views.sources;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import windperformancercp.controller.IController;
import windperformancercp.controller.SourceController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.event.UpdateEvent;
import windperformancercp.event.UpdateEventType;
import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.IDialogResult;
import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.MetMast;
import windperformancercp.model.sources.WindTurbine;
import windperformancercp.views.AbstractUIDialog;
import windperformancercp.views.IPresenter;

public class SourceDialogPresenter extends EventHandler implements IPresenter{
	SourceDialog dialog;
	ISource source;
	ArrayList<Attribute> tmpAttList;
	IController _cont;
	final SourceDialogPresenter boss;

	//TODO: auslagern
	int MMId = 0;
	int WTId = 1;
	
	public SourceDialogPresenter(SourceDialog caller){
		boss = this;
		//System.out.println(this.toString()+": source dialog presenter says hi!");
		dialog = caller;
		_cont = SourceController.getInstance(this);
		tmpAttList = new ArrayList<Attribute>();
		
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
	}
		
	public void nameEntered(){
		if(source != null)
			source.setName(dialog.getNameValue());
	}
	
	public void streamIdEntered(){
		if(source != null)
			source.setStreamIdentifier(dialog.getStrIdValue());
	}
	
	public void hostEntered(){
		if(source != null)
			source.setHost(dialog.getHostValue());
	}
	
	public void portEntered(){
		if(source != null)
			source.setPort(Integer.parseInt(dialog.getPortValue()));
	}
	
	public void attBtnClick(String btn, int index){

		if(btn.equals("Add")){
			Shell attShell = dialog.getShell();
			AbstractUIDialog attDialog = new AttributeDialog(attShell, Attribute.AttributeType.values());
			attDialog.subscribe(attListener, InputDialogEventType.NewAttributeItem);
			attDialog.open();
		}
		if(btn.equals("Up")){
			if(index > 0){ //if it's 0, it is the topmost element
				swapEntries(index, index-1);
				updateDialog();
			}
		}
		if(btn.equals("Down")){
			if(index >= 0 && index < tmpAttList.size()-1){ //if it's 0, it is the bottommost element
        		swapEntries(index, index+1);
        		updateDialog();
			}
		}
		if(btn.equals("Delete")){
			if((index>=0)&&(index<tmpAttList.size())){
        		tmpAttList.remove(index);
        		updateDialog();
			}
		}
	}
	
	
	IEventListener attListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> idevent){
			//if(idevent.getEventType().equals(InputDialogEventType.NewAttributeItem)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
				InputDialogEvent newAttevent = (InputDialogEvent) idevent;
				Attribute att = (Attribute)newAttevent.getValue();
			//	fire(new InputDialogEvent(boss, InputDialogEventType.NewAttributeItem, att));
				tmpAttList.add(att);
				if(source != null)
					source.setAttributeList(tmpAttList);
				updateDialog();
			//}
		}
	};
	
	
	public void srcTypeClick(){
		source = buildSource();
	}
	
	public void hubheightEntered(){
		if(source != null && source instanceof WindTurbine){
			((WindTurbine) source).setHubHeight(Double.parseDouble(dialog.getHubHeightValue()));
		}
	}
	
	public void powerControlTypeClick(){
		if(source != null && source instanceof WindTurbine){
			((WindTurbine) source).setPowerControl(dialog.getPowerControl());
		}
	}
	
	public void okPressed(){
		//TODO: Validation
		if(source == null)
			source = buildSource();
		if(sourceIsOk(source)){
			System.out.println("source is ok!"+source.toString());
			fire(new InputDialogEvent(this, InputDialogEventType.NewSourceItem, source));
			//System.out.println("fired new source event!");
			dialog.close();
			fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
		}
		else{
			System.out.println("source is not ok!");
		}
	}
	
	public void cancelPressed(){
		dialog.close();
		fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
	}
	
	private ISource buildSource(){
		if(dialog.getSourceType() == MMId) 
			source = new MetMast(dialog.getNameValue(),
					dialog.getStrIdValue(),
					dialog.getHostValue(),
					Integer.parseInt(dialog.getPortValue()), 
					tmpAttList);
		if(dialog.getSourceType() == WTId) 
			source = new WindTurbine(dialog.getNameValue(),
					dialog.getStrIdValue(),
					dialog.getHostValue(),
					Integer.parseInt(dialog.getPortValue()), 
					tmpAttList,
					Double.parseDouble(dialog.getHubHeightValue()),
					dialog.getPowerControl());
		return source;
	}
	
	
	//vorlaeufige Validation
	public boolean sourceIsOk(ISource tocheck){
		if(tocheck != null){
			if(!tocheck.getName().equals("")){
				if(!tocheck.getStreamIdentifier().equals("")){
					if(!tocheck.getHost().equals("")){
						if(!(tocheck.getPort() < 1024 || tocheck.getPort() > 65536)){
							if(tocheck instanceof MetMast){
								return true;
							}
							if(tocheck instanceof WindTurbine){
								WindTurbine wt = (WindTurbine)tocheck;
								if((wt.getHubHeight() > 1)&&(wt.getHubHeight()< 250)){
									if(wt.getPowerControl() == 0||wt.getPowerControl() == 1){
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public void updateDialog(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,getContent()));
		//System.out.println(this.toString()+":fired update event!");
	}
	
	@Override
	public void feedDialog(IDialogResult input){
		
		ISource src = (ISource) input;
		dialog.setNameValue(src.getName());
		dialog.setStrIdValue(src.getStreamIdentifier());
		dialog.setHostValue(src.getHost());
		dialog.setPortValue(Integer.toString(src.getPort()));
		dialog.setTableContent(src.getAttributeList());
		tmpAttList = src.getAttributeList();
		if(src.isWindTurbine()){
			dialog.setSourceType(WTId);
			dialog.setHubHeightValue(Double.toString(((WindTurbine)src).getHubHeight()));
			dialog.setPowerControl(((WindTurbine)src).getPowerControl());
		}
		if(src.isMetMast()){
			dialog.setSourceType(MMId);
		}
	}
	

	public ArrayList<Attribute> getContent(){
		return tmpAttList;
	}
	
	public void swapEntries(int ind1, int ind2){
		Attribute tmp = tmpAttList.get(ind1);
		tmpAttList.set(ind1, tmpAttList.get(ind2));
		tmpAttList.set(ind2, tmp);
	}

}
