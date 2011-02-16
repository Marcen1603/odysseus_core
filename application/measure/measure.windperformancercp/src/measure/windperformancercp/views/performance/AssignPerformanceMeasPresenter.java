package measure.windperformancercp.views.performance;

import measure.windperformancercp.controller.IController;
import measure.windperformancercp.controller.PMController;
import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.InputDialogEvent;
import measure.windperformancercp.event.InputDialogEventType;
import measure.windperformancercp.event.ModelEventType;
import measure.windperformancercp.event.UpdateEvent;
import measure.windperformancercp.event.UpdateEventType;
import measure.windperformancercp.model.query.PerformanceModel;
import measure.windperformancercp.model.sources.IDialogResult;
import measure.windperformancercp.model.sources.SourceModel;
import measure.windperformancercp.views.IPresenter;

import org.eclipse.ui.IActionBars;

public class AssignPerformanceMeasPresenter extends EventHandler implements IPresenter{
	
	AssignPerformanceMeasView view;
	IController _cont;
	//TODO
	SourceModel smodel;
	PerformanceModel pmodel;
	
	public AssignPerformanceMeasPresenter(AssignPerformanceMeasView view){	
		//System.out.println(this.toString()+": manage source presenter says hi!");
		this.view = view;
		_cont = PMController.getInstance(this);
		//smodel = SourceModel.getInstance();
		//smodel.subscribeToAll(modelListener);
		pmodel = PerformanceModel.getInstance();
		pmodel.subscribeToAll(modelListener);
		fire(new InputDialogEvent(this,InputDialogEventType.RegisterDialog,null));
		
	}
	
	/**
	 * 'Updates' the AssignPerformanceMeasView via update event
	 */
	public void updateView(){
		fire(new UpdateEvent(this,UpdateEventType.GeneralUpdate,_cont.getContent()));
	}
	
	/**
	 * Returns query text from query i from <code>PerformanceModel</code>
	 * @param i the position of the query in <code>PerformanceModel</code> List
	 * @return String in human readable form (run through <code>parseItBeautiful</code>)
	 */
	public String getQueryText(int i){
	int j = pmodel.getElemCount();
		if(j!=0){
			if((i!=-1)&&(j>i))
				return parseItBeautiful(pmodel.getIthElement(i).getQueryText());
			else
				return parseItBeautiful(pmodel.getIthElement(0).getQueryText());
		}
		return "";
	}
	
	/**
	 * Takes a query and parses it for human readability
	 * (Fills tabs and newlines)
	 * @param query string
	 * @return the beautificated query as string
	 */
	public String parseItBeautiful(String query){
		
		String bQ = query.replaceAll("\\(\\{", "\\(\\{\n\t");
		bQ = bQ.replaceAll("],", "],\n\t");
		bQ = bQ.replaceAll("\\},", "\\},\n");
		// take care, the number of blanks is important in following line (here are no blanks, between aggregations is one)
		bQ = bQ.replaceAll("','", "',\n\t'");	 
		bQ = bQ.replaceAll("= \\[", "= \\[\n\t");
		bQ = bQ.replaceAll("RelationalPredicate\\(", "\n\tRelationalPredicate\\(");
		
				
		bQ = bQ.replaceAll("\\)\n", "\\)\n\n");		
		return bQ;
	}
	
	@Override
	public void subscribeToAll(IEventListener listener) {
		super.subscribeToAll(listener);
		if(listener.equals(view.updateListener)){
			updateView();
		}
	}
	
	public IEventListener modelListener = new IEventListener(){
		public void eventOccured(IEvent<?, ?> event){
			
			if(event.getEventType().equals(ModelEventType.NewItem)){ 
				//System.out.println("received new measurement event, updating view!");
				updateView();
				setStatusLine("Added measurement successfully. ");
			}
			
			if(event.getEventType().equals(ModelEventType.DeletedItem)){ 
				//System.out.println("received delete measurement event, updating view!");
				updateView();
				setStatusLine("Deleted measurement successfully. ");
			}
			
			if(event.getEventType().equals(ModelEventType.ModifyItem)){ 
				//System.out.println("received delete measurement event, updating view!");
				updateView();
				setStatusLine("Changed measurement successfully. ");
			}
			
		}
	};

	private void setStatusLine(String message) {
		// Get the status line and set the text
		IActionBars bars = view.getViewSite().getActionBars();
		bars.getStatusLineManager().setMessage(message);
	}


	@Override
	public void feedDialog(IDialogResult input) {
		// TODO Auto-generated method stub
		
	}
}