/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public final class DashboardPartPlacement {

	private final IDashboardPart part;
	
	private int x;
	private int y;
	private int w;
	private int h;
	
	private String title;
	private final String filename;
	
	public DashboardPartPlacement( IDashboardPart part, String filename, String title, int x, int y, int w, int h ) {
		this.part = Preconditions.checkNotNull(part, "Dashboard Part for positioning must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(filename), "File of Dashboard Part must not be null!");
		
		Preconditions.checkArgument(x >= 0, "x must be positive instead of %s", x);
		Preconditions.checkArgument(y >= 0, "y must be positive instead of %s", y);
		Preconditions.checkArgument(w >= 0, "Width must be positive instead of %s", w);
		Preconditions.checkArgument(h >= 0, "Height must be positive instead of %s", h);
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.title = title;
		this.filename = filename;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean hasTitle() {
		return !Strings.isNullOrEmpty(title);
	}
	
	public void setTitle( String title ) {
		this.title = title;
	}
	
	public IDashboardPart getDashboardPart() {
		return part;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}
	
	public String getFilename() {
		return filename;
	}
}
