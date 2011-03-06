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
package measure.windperformancercp.controller;

import java.util.ArrayList;
import java.util.List;

import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.SourceModel;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.views.IPresenter;
import measure.windperformancercp.views.sources.ManageSourcePresenter;
import measure.windperformancercp.views.sources.SourceDialogPresenter;

public class SourceController implements IController {
	
	private static SourceController instance = new SourceController();
	private static ArrayList<ManageSourcePresenter> msrcPresenters = new ArrayList<ManageSourcePresenter>();
	private static ArrayList<SourceDialogPresenter> srcDPresenters = new ArrayList<SourceDialogPresenter>();
	
	IController _control;
	static PMController pcontrol;
	static SourceModel smodel;
	

	private SourceController(){
		//System.out.println(this.toString()+": sourceController says hello!");
		smodel = SourceModel.getInstance();
	}
	
	public static void setBrotherControl(PMController pcont){
		pcontrol = pcont;
		if(pcontrol == null){
			//TODO Error message
		}
	}
	
	public static <E extends IPresenter> void registerPresenter(ArrayList<E> list, E pres){
		pres.subscribeToAll(presenterListener);
		list.add(pres);
	}
	
	
	public static <E extends IPresenter> void deregisterPresenter(ArrayList<E> list, E pres){
		while(list.contains(pres)){
			list.remove(pres);
		}
		//pres.unSubscribeFromAll(presenterListener);
	}
	
//source Model modification wird hier ausgefuehrt
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog registry event");
			}
			
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ 
				//System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getPresenter();
							
				if(pres instanceof ManageSourcePresenter) 
					deregisterPresenter(msrcPresenters, (ManageSourcePresenter)pres);
				if(pres instanceof SourceDialogPresenter)
					deregisterPresenter(srcDPresenters, (SourceDialogPresenter)pres);
			}
			
			if(event.getEventType().equals(InputDialogEventType.NewSourceItem)){
				//System.out.println(this.toString()+": Received new source event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				ISource src = (ISource) ideEvent.getValue();
				if(src instanceof WindTurbine){
					ISource newsrc = new WindTurbine((WindTurbine)src);
					smodel.addElement(newsrc);
				}
				if(src instanceof MetMast){
					ISource newsrc = new MetMast((MetMast)src);
					smodel.addElement(newsrc);
				}
								
				
			}
			if(event.getEventType().equals(InputDialogEventType.DeleteSourceItem)){
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				ISource src = (ISource) ideEvent.getValue();
				//int c = smodel.removeAllOccurences(src);				
				smodel.removeAllOccurences(src.getName());
				//System.out.println(this.toString()+": Received delete source event for "+src.toString()+" from "+event.getSender().toString()+"\n Deleted "+c);
			}			
		}
	};
	
	public List<Boolean> getConnectionList(ArrayList<String> srcKeys){
		List<Boolean> result = new ArrayList<Boolean>();
		for(ISource src : getSourcesFromModel(srcKeys)){
			result.add(src.getConnectState());
		}		
		return result;
	}
	
	public void tellWhichAreConnected(ArrayList<String> srcKeys){
		for(ISource src : getSourcesFromModel(srcKeys)){
			src.setConnectState(true);
			//System.out.println(src.toString() +" in list: "+smodel.getSourcesList().contains(src));
		}
		smodel.somethingChanged(null);
	}
	
	public void tellWhichAreDisconnected(ArrayList<String> srcKeys){
		for(ISource src : getSourcesFromModel(srcKeys)){
			src.setConnectState(false);
		}
		smodel.somethingChanged(null);

	}
	

	@Override
	public ArrayList<ISource> getContent(){
		return smodel.getSourcesListB();
	}
	
	public static <E extends IPresenter> SourceController getInstance(E pres){
		if(pres instanceof SourceDialogPresenter) 
			registerPresenter(srcDPresenters, (SourceDialogPresenter) pres);
		if(pres instanceof ManageSourcePresenter)
			registerPresenter(msrcPresenters, (ManageSourcePresenter) pres);
		return instance;
	}

	
	public static SourceController getInstance(){
		return instance;
	}
	
	public ArrayList<String> getContentKeyList(){
		return smodel.getSourcesKeyList();
	}
	
	public ISource getSourceFromModel(String key){
		return smodel.getElementByName(key);
	}
	
	public ArrayList<ISource> getSourcesFromModel(ArrayList<String> keys){
		ArrayList<ISource> srcs = new ArrayList<ISource>();
		for(String key: keys){
			srcs.add(smodel.getElementByName(key));
		}
		return srcs;
	}
	
	public ArrayList<Attribute> getAttributesFromSource(String key){
		return smodel.getElementByName(key).getAttributeList();
	}

	
} //end_SourceControler
