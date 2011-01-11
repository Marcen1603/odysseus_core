package windperformancercp.views;

import java.util.ArrayList;

import windperformancercp.controller.IController;
import windperformancercp.controller.SourceController;
import windperformancercp.event.EventHandler;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.MetMast;
import windperformancercp.model.sources.WindTurbine;

public class SourceDialogPresenter extends EventHandler implements IPresenter{
	SourceDialog dialog;
	ISource source;
	IController _cont;
	
	//TODO: auslagern
	int MMId = 0;
	int WTId = 1;
	
	//public SourceDialogPresenter(IControler caller){
	public SourceDialogPresenter(SourceDialog caller){
		System.out.println("source dialog presenter says hi!");
		dialog = caller;
		_cont = SourceController.getInstance(this);
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
	
	public void attBtnClick(){
		
	}
	
	public void srcTypeClick(){
		if(dialog.getSourceType() == MMId) 
			source = new MetMast(dialog.getNameValue(),
					dialog.getStrIdValue(),
					dialog.getHostValue(),
					Integer.parseInt(dialog.getPortValue()), 
					new ArrayList<Attribute>());
		if(dialog.getSourceType() == WTId) 
			source = new WindTurbine(dialog.getNameValue(),
					dialog.getStrIdValue(),
					dialog.getHostValue(),
					Integer.parseInt(dialog.getPortValue()), 
					new ArrayList<Attribute>(),
					Double.parseDouble(dialog.getHubHeightValue()),	//zum Zeitpunkt des Klicks sind hubheight und powercontrol noch nicht gesetzt
					dialog.getPowerControl());
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
		//TODO: abfragemethode, die auf korrekte ausfuellung prueft
		
		if(sourceIsOk(source)){
			System.out.println("source is ok!");
			source.toString();
			fire(new InputDialogEvent(this, InputDialogEventType.NewSourceItem, source));
			System.out.println("fired new source event!");
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
		
	}
	
	/*
	public void update(Object arg0, Object arg1){
		ISource src = (ISource) arg1;
		dialog.setNameValue(src.getName());
		dialog.setStrIdValue(src.getStreamIdentifier());
		dialog.setHostValue(src.getHost());
		dialog.setPortValue(Integer.toString(src.getPort()));

		if(src instanceof WindTurbine){
			dialog.setSourceType(WTId);
			WindTurbine wt = (WindTurbine) src;
			dialog.setHubHeightValue(Double.toString(wt.getHubHeight()));
			dialog.setPowerControl(wt.getPowerControl());
		}
		if(src instanceof MetMast){
			MetMast mm = (MetMast) src;
		}

	}*/




}
