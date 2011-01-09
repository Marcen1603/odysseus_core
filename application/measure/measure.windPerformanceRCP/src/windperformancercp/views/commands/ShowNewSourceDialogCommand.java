package windperformancercp.views.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import windperformancercp.event.IEvent;
import windperformancercp.event.IEventListener;
import windperformancercp.event.InputDialogEvent;
import windperformancercp.event.InputDialogEventType;
import windperformancercp.model.Attribute;
import windperformancercp.model.SourceTable;
import windperformancercp.views.AbstractUIDialog;
import windperformancercp.views.SourceDialog;

public class ShowNewSourceDialogCommand extends AbstractHandler implements
		IHandler {
	public static final String ID = "measure.windPerformanceRCP.ShowNewSourceDialog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		final Shell dialogShell = new Shell(parent);
		try{
			AbstractUIDialog dialog = new SourceDialog(dialogShell);
			
			
			IEventListener sourceListener = new IEventListener(){
    			public void eventOccured(IEvent<?, ?> idevent){
    				if(idevent.getEventType().equals(InputDialogEventType.NewSourceItem)){ //doppelt gemoppelt? ich registriere ja nur fuer newattitem
    					System.out.println(this.toString()+": Received new source item event");
    					InputDialogEvent newItemevent = (InputDialogEvent) idevent;
    					String[] ideValue = newItemevent.getValue();
    					if(ideValue[5].equals("0")){
    						System.out.println("MetMast");
    						//ISource source = new MetMast();
    					}
    					if(ideValue[5].equals("1")){
    						System.out.println("windTurbine");
    						//ISource source = new WindTurbine(ideValue[0],ideValue[1],ideValue[2],Integer.parseInt(ideValue[3]),,Double.parseDouble(ideValue[6]),Integer.parseInt(ideValue[7]));
    						//WindTurbine(String name, String identifier, String hostName, int portId, Attribute[] attList, double hubHeight, boolean actPowerControl){
    					}
    					for(String str : ideValue){
    						System.out.print(str+"#");
    					}
    					System.out.println("!");
    					//ISource source = new AbstractSource();
    					//Attribute att = new Attribute((String)newAttevent.getValue()[0],newAttevent.getValue()[1]);
    					//model.addAttribute(att);
    					//update(model.getAllElements());
    				
    				}
    				
    			}
    		};
    		
    		dialog.subscribe(sourceListener, InputDialogEventType.NewSourceItem);
		
		//TODO: generate
			if(dialog.open()== Window.OK){
			
			}
		}
		catch(Exception e){
			System.out.println("Exception in newsourcehandler: "+e);
		}

		return null;
	}

}
