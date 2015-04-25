/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.prototyping.logicaloperator;

import java.io.File;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "JAVA", doc = "This operator supports Java for data processing", category = { LogicalOperatorCategory.BASE })
public class JavaAO extends UnaryLogicalOp {

    /**
     *
     */
    private static final long serialVersionUID = 3096990017096323L;
    private String path;

    /**
     * Class constructor.
     *
     */
    public JavaAO() {
    }

    /**
     * Class constructor.
     *
     * @param operator
     */
    public JavaAO(final JavaAO operator) {
        this.path = operator.path;
    }

    @Parameter(name = "PATH", optional = false, type = StringParameter.class, isList = false, doc = "Path to file")
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * @return the path
     */
    @GetParameter(name = "PATH")
    public String getPath() {
        return this.path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaAO clone() {
        return new JavaAO(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        this.addError(this.path == null, "Path undefined");
        this.addError(!new File(this.path).canRead(), String.format("File %s not found", this.path));

        return !this.hasErrors();
    }

}
