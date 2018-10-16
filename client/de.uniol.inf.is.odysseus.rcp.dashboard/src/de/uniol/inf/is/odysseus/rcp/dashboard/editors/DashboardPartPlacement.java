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
	private final String filename;

	private int x;
	private int y;
	private int w;
	private int h;

	public DashboardPartPlacement(IDashboardPart part, String filename, int x, int y, int w, int h) {
		// Preconditions.checkNotNull(part, "Dashboard Part for positioning must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(filename), "File of Dashboard Part must not be null!");

		// Preconditions.checkArgument(w >= 0, "Width must be positive instead of %s", w);
		// Preconditions.checkArgument(h >= 0, "Height must be positive instead of %s", h);

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

		this.part = part;
		this.filename = filename;
	}

	public IDashboardPart getDashboardPart() {
		return part;
	}

	public String getFilename() {
		return filename;
	}

	public int getHeight() {
		return h;
	}

	public int getWidth() {
		return w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
