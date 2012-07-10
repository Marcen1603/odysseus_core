/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.test.itestcomp;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * This BundleTracker should observe new installed Bundles 
 * which change their state to RESOLVED. For any Bundle 
 * activated it looks for the "tests"-Folder and if found assumes 
 * this Bundle, Fragment-Bundle normally, contains test-cases and 
 * then tests those found in the Folder.
 * 
 * @author Work
 *
 */
public class TestBundleTracker implements BundleTrackerCustomizer<Object> {

	private BundleTracker<Object> track;
	private FragmentTestsComponent testComp;
	
	public TestBundleTracker(BundleContext context, FragmentTestsComponent testComponent) {
		track = new BundleTracker<Object>(context, Bundle.RESOLVED, this);
		this.testComp = testComponent;
	}

	@Override
	public Object addingBundle(Bundle bundle, BundleEvent event) {
		// look for "tests" Folder
		URL fileUrl = bundle.getResource("tests");
		
		try {
			File testsDir = new File(FileLocator.toFileURL(fileUrl).getPath());
			
			if(testsDir != null){				
				// ... and start testing if found
//				testComp.addTestsDirectory(testsDir);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		// TODO Auto-generated method stub
		
	}

}
