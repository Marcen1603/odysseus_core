package windperformancercp.controller;

import java.util.ArrayList;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.sources.AbstractSource;
import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.SourceModel;
import windperformancercp.views.AttributeDialogPresenter;
import windperformancercp.views.IPresenter;
import windperformancercp.views.ManageSourcePresenter;
import windperformancercp.views.SourceDialogPresenter;

public class SourceController implements IController {
	
	private static SourceController instance = new SourceController();
	private static ArrayList<ManageSourcePresenter> msrcPresenters = new ArrayList<ManageSourcePresenter>();
	private static ArrayList<SourceDialogPresenter> srcDPresenters = new ArrayList<SourceDialogPresenter>();
	private static ArrayList<AttributeDialogPresenter> attDPresenters = new ArrayList<AttributeDialogPresenter>();
	
	IController _control;
	static SourceModel model;
	
	

	private SourceController(){
		System.out.println("sourceController wurde erzeugt");
		model = SourceModel.getInstance();
		
		msrcPresenters = new ArrayList<ManageSourcePresenter>();
		srcDPresenters = new ArrayList<SourceDialogPresenter>();
		attDPresenters = new ArrayList<AttributeDialogPresenter>();
		
		
		/*ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("pogo",Attribute.AttributeType.WINDDIRECTION));
		WindTurbine wt = new WindTurbine("hallo","strId","host",12345,atts,55.3,1);
	
		model.addElement(wt);*/
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
	
	public static IEventListener presenterListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			if(event.getEventType().equals(InputDialogEventType.RegisterDialog)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
				//System.out.println(this.toString()+": Received new dialog registry event");
			}
			
			if(event.getEventType().equals(InputDialogEventType.DeregisterDialog)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
				//System.out.println(this.toString()+": Received new dialog deregistry event");
				
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				IPresenter pres = ideEvent.getDialog();
							
				if(pres instanceof ManageSourcePresenter) 
					deregisterPresenter(msrcPresenters, (ManageSourcePresenter)pres);
				if(pres instanceof SourceDialogPresenter)
					deregisterPresenter(srcDPresenters, (SourceDialogPresenter)pres);
				if(pres instanceof AttributeDialogPresenter)
					deregisterPresenter(attDPresenters, (AttributeDialogPresenter)pres);
			}
			
			if(event.getEventType().equals(InputDialogEventType.NewSourceItem)){
				//System.out.println(this.toString()+": Received new source event");
				InputDialogEvent ideEvent = (InputDialogEvent) event;
				ISource src = (ISource) ideEvent.getValue();
				
				model.addElement(src);
	//			JAXB.marshal(src, System.out);
				
			}
			if(event.getEventType().equals(InputDialogEventType.DeleteSourceItem)){
				System.out.println(this.toString()+": Received delete source event");
			}			
		}
	};
	
	
	//TODO source Model modification wird hier ausgefuehrt
	

	@Override
	public ArrayList<ISource> getContent(){
		return model.getSourcesList();
	}
	
	public static <E extends IPresenter> SourceController getInstance(E pres){
		if(pres instanceof SourceDialogPresenter) 
			registerPresenter(srcDPresenters, (SourceDialogPresenter) pres);
		if(pres instanceof ManageSourcePresenter)
			registerPresenter(msrcPresenters, (ManageSourcePresenter) pres);
		if(pres instanceof AttributeDialogPresenter)
			registerPresenter(attDPresenters, (AttributeDialogPresenter) pres);
		return instance;
	}
	
	public static SourceController getInstance(){
		return instance;
	}


	
} //end_SourceControler
