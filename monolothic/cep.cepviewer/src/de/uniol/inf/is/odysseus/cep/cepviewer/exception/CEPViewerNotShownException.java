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
package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the exception thrown if the CEPViewer perspective could
 * not be shown in the workbench
 * 
 * @author Christian
 */
@SuppressWarnings("serial")
public class CEPViewerNotShownException extends RuntimeException {

	// the constructor
	public CEPViewerNotShownException() {
		super(StringConst.EXCEPTION_CEPVIEWER_NOT_SHOWN);
	}
}
