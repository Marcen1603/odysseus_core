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
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author DGeesen
 *
 */
public class ConnectionRelativeToMidpointLocator extends ConnectionLocator {

	private int y;
	private int x;


	/**
	 * @param connection
	 */
	public ConnectionRelativeToMidpointLocator(Connection connection, int x, int y) {
		super(connection);
		this.x = x;
		this.y = y;
	}
	
	
	@Override
	protected Point getReferencePoint() {
		Connection conn = getConnection();
		Point p = Point.SINGLETON;
		
//		Point p1 = conn.getPoints().getPoint(0);
//		Point p2 = conn.getPoints().getPoint(1);
//		conn.translateToAbsolute(p1);
//		conn.translateToAbsolute(p2);
//		p.x = (p2.x - p1.x) / 2 + p1.x;
//		p.y = (p2.y - p1.y) / 2 + p1.y;
		p.x = conn.getPoints().getMidpoint().x + x;
		p.y = conn.getPoints().getMidpoint().y + y;
		return p;
	}


}
