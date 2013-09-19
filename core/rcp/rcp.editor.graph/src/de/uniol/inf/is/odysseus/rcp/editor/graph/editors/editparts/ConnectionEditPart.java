/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;

/**
 * @author DGeesen
 * 
 */
public class ConnectionEditPart extends AbstractConnectionEditPart {
	public ConnectionEditPart(Connection connection) {
		setModel(connection);
	}

	protected void createEditPolicies() {
	}

	protected IFigure createFigure() {
		PolylineConnection con = new PolylineConnection();
		con.setLineWidth(2);
		PolygonDecoration deco = new PolygonDecoration();
		deco.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		con.setTargetDecoration(deco);
		return con;
	}

}
