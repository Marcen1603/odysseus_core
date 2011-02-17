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
package de.uniol.inf.is.odysseus.action.demoapp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public static void main(String[] args) {
		Application app = new Application();
		app.start(null);
	}
	private Display display;

	private Shell shell;

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) {
		this.display = new Display();
		this.shell = AuctionMonitor.getInstance().runApplication(display);
		while(!shell.isDisposed()){
			if(! display.readAndDispatch()) {
		        display.sleep();
		    }
		}
		display.dispose();
		return IApplication.EXIT_OK;
	}
	
	@Override
	public void stop() {
		if (this.shell != null && !this.shell.isDisposed()){
			this.display.syncExec(new Runnable() {
				@Override
				public void run() {
					Application.this.shell.close();
				}
			});
			this.display.dispose();
		}
	}
}
