/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.p2p.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
/**
 * Abstraktion der grafischen Darstellung der Peer GUIs
 * 
 * @author Mart Köhler
 *
 */
public abstract class AbstractMainWindow extends JFrame implements ActionListener{
	

	
	private static final long serialVersionUID = 1L;

	public AbstractMainWindow(String title) {
		super(title);
	}

	public abstract void addTab(String queryId);

	public abstract void addAction(String queryId, String action);

	public abstract void addEvent(String queryId, String event);

	public abstract void addSubplans(String queryId, int s);

	public abstract void addStatus(String queryId, String s);

	public abstract void addBids(String queryId, String s);

	public abstract void addSplitting(String queryId, String s);
	
	public abstract void addScheduler(String queryId, String scheduler);
	
	public abstract void addSchedulerStrategy(String queryId, String strategy);
	
	public abstract void addOperation(String queryId, String operation);
	
	public abstract boolean isQuery(String queryId);
	
	
	//Thin-Peer spezifisch
	public abstract void addResult(String queryId, Object o);	
	public abstract void addTab(String queryId, String queryAsString);
	@Override
	public abstract void actionPerformed(ActionEvent e);
	public abstract void addAdminPeer(String queryId, String adminPeer);

	public abstract void removeTab(String queryId) ;

}
