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
package measure.windperformancercp.views;

import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.IEvent;
import measure.windperformancercp.event.IEventListener;
import measure.windperformancercp.event.IEventType;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;


public class AbstractUIDialog extends TitleAreaDialog implements IUserInputDialog{

	
	public AbstractUIDialog(Shell parentShell) {
		super(parentShell);
	}

	/*public AbstractUIDialog(IShellProvider parentShell) {
		super(parentShell);
	}*/
	
	//	Event handling
	EventHandler eventHandler = new EventHandler();
	
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(listener, type);

	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(listener);

	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(listener);
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		eventHandler.fire(event);
	}

	@Override
	public AbstractUIDialog getInstance(){
		return this;
	}
	
	public void resetView(){}
	
	
	@Override
	public void okPressed(){
		close();
	}
	
	@Override
	public void cancelPressed(){
		close();
	}
	
	@Override
	public void update(Object c){}

	@Override
	public void setInput(Object input) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setContent(Object input){
		// TODO Auto-generated method stub
	}

	@Override
	public IPresenter getPresenter() {
		// TODO Auto-generated method stub
		return null;
	}


}
