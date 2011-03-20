/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.views.sources;

import java.util.ArrayList;

import measure.windperformancercp.controller.IController;
import measure.windperformancercp.controller.SourceController;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.IDialogResult;
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.swt.widgets.Shell;


public class SourceDialogPresenter extends EventHandler implements IPresenter{
	SourceDialog dialog;
	ISource source;
	IController _cont;
	final SourceDialogPresenter boss;
	
	//Attributes
	ArrayList<Attribute> tmpAttList;
	AttributeDialog attDialog;
	Attribute actAtt;

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
	
	public void frequencyEntered(){
		if(source != null)
			source.setFrequency(Integer.parseInt(dialog.getFrequencyValue()));
	}
	
	public void attBtnClick(String btn, int index){

		if(btn.equals("Add")){
			this.actAtt = new Attribute("",Attribute.AttributeType.VARIOUS);
			Shell attShell = dialog.getShell();
			attDialog = new AttributeDialog(attShell, this, Attribute.AttributeType.values());
			attDialog.open();
			aDTypeSelected();
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
	
	public void srcTypeClick(){
		source = buildSource();
	}
	
	public void hubheightEntered(){
		if(source != null && source instanceof WindTurbine){
			((WindTurbine) source).setHubHeight(Double.parseDouble(dialog.getHubHeightValue()));
		}
	}
	
	public void cutinEntered(){
		if(source != null && source instanceof WindTurbine){
			((WindTurbine) source).setCutInWS(Double.parseDouble(dialog.getCutInValue()));
		}
	}
	
	public void eightyfiveEntered(){
		if(source != null && source instanceof WindTurbine){
			((WindTurbine) source).setEightyFiveWS(Double.parseDouble(dialog.getEigthyfiveValue()));
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
			fire(new InputDialogEvent(this, InputDialogEventType.NewSourceItem, source));
			dialog.close();
			fire(new InputDialogEvent(this,InputDialogEventType.DeregisterDialog,null));
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
					tmpAttList,
					Integer.parseInt(dialog.getFrequencyValue()));
		if(dialog.getSourceType() == WTId) 
			source = new WindTurbine(dialog.getNameValue(),
					dialog.getStrIdValue(),
					dialog.getHostValue(),
					Integer.parseInt(dialog.getPortValue()), 
					tmpAttList,
					Double.parseDouble(dialog.getHubHeightValue()),
					dialog.getPowerControl(),
					Double.parseDouble(dialog.getCutInValue()),
					Double.parseDouble(dialog.getEigthyfiveValue()),
					Integer.parseInt(dialog.getFrequencyValue()));
		return source;
	}
	
	
	//vorlaeufige Validation
	public boolean sourceIsOk(ISource tocheck){
		String errorMsg = "";
		if(tocheck != null){
			if(!tocheck.getName().equals("")){
				if(!tocheck.getStreamIdentifier().equals("")){
					if(!tocheck.getHost().equals("")){
						if(!(tocheck.getPort() < 1024 || tocheck.getPort() > 65536)){
							if(!(tocheck.getFrequency() < 1)){
								if(tocheck instanceof MetMast){
								//MetMast mm = (MetMast)tocheck;
									return true;
								}
								if(tocheck instanceof WindTurbine){
									WindTurbine wt = (WindTurbine)tocheck;
									if((wt.getHubHeight() > 1)&&(wt.getHubHeight()<= 250)){
										if(wt.getCutInWS() > 0){
											if(wt.getEightyFiveWS() > 0){
												if(wt.getPowerControl() == 0||wt.getPowerControl() == 1){
													return true;
												}
												else
													errorMsg = errorMsg+" Power control must be set \n";
											}
											else
											errorMsg = errorMsg+" Value for 85% of P rated must be set\n";
										}
										else
										errorMsg = errorMsg+" Value for cut in speed must be set \n";
									}
									else
									errorMsg = errorMsg+" Hub heigt must be between 2 and 250 m\n ";
								}
								else
								errorMsg = errorMsg+" Source type is missing\n ";
							}
							else
							errorMsg = errorMsg+" Frequency must be at least 1\n ";
						}
						else
						errorMsg = errorMsg+" Port is empty or not between 1025 and 65535\n";
					}
					else
					errorMsg = errorMsg+" Host name may not be empty\n";
				}
				else
				errorMsg = errorMsg+" Stream identifier may not be empty\n";
			}
			else
			errorMsg = errorMsg+" Name may not be empty\n";
		}
		if(errorMsg.length()>250){
			errorMsg = "";
		}
		dialog.setErrorMessage("Source is not valid: "+ errorMsg);
		return false;
}
	
	
	public void updateDialog(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,getContent()));
	//	System.out.println(this.toString()+":fired update event!");
	}
	
	@Override
	public void feedDialog(IDialogResult input){
		
		ISource src = (ISource) input;
		dialog.setNameValue(src.getName());
		dialog.setStrIdValue(src.getStreamIdentifier());
		dialog.setHostValue(src.getHost());
		dialog.setPortValue(Integer.toString(src.getPort()));
		dialog.setTableContent(src.getAttributeList());
		dialog.setFrequencyValue(Integer.toString(src.getFrequency()));
		tmpAttList = src.getAttributeList();
		if(src.isWindTurbine()){
			dialog.setSourceType(WTId);
			dialog.setHubHeightValue(Double.toString(((WindTurbine)src).getHubHeight()));
			dialog.setCutInValue(Double.toString(((WindTurbine)src).getCutInWS()));
			dialog.setEigthyfiveValue(Double.toString(((WindTurbine)src).getEightyFiveWS()));
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
	
	//AttributeDialog handler
	public void aDNameEntered(){
		actAtt.setName(attDialog.getNameValue());
	}
	
	public void aDTypeSelected(){ //TODO throws exception
		try{
		System.out.println(this.toString()+" type selected: "+attDialog.getComboValue()+" and value of is: "+Attribute.AttributeType.valueOf(attDialog.getComboValue()));	
			actAtt.setAttType(Attribute.AttributeType.valueOf(attDialog.getComboValue()));
		}
		catch(Exception e){
			//
		}
	}
	
	public void aDOkPressed(){
		if(! attDialog.getNameValue().equals("")){
			//check if attribute name is unique, if not, send error message to user
			boolean taken = false;
			for(Attribute resident: tmpAttList){
					taken = resident.getName().equals(attDialog.getNameValue());
					if(taken)
						break;
				}
			if(taken) attDialog.setErrorMessage("Name must be unique for source");
			else{	//name is ok
				actAtt = new Attribute(actAtt); //fuer Zuweisung des Datatyps
				tmpAttList.add(actAtt);
				attDialog.close();
				if(source != null)
					source.setAttributeList(tmpAttList);
				updateDialog();
			}
		}
		else
			attDialog.setErrorMessage("Name field may not be empty");
	}
	
	public void aDCancelPressed(){
		attDialog.close();
	}
	
	public void aDfeedDialog(IDialogResult input) {
		Attribute att = (Attribute) input;
		attDialog.setNameValue(att.getName());
		attDialog.setComboValue(att.getAttType().toString());
		
	}
	
	@Override
	public void notifyUser(String message){
		dialog.setMessage(message);
	}

}
