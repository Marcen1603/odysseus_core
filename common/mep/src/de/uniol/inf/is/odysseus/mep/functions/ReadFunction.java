/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ReadFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 4935935977218584148L;
    private static final Logger LOG = LoggerFactory.getLogger(ReadFunction.class);

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

    public ReadFunction() {
        super("read", 1, accTypes, SDFDatatype.STRING);
    }

    @Override
    public String getValue() {
        String path = getInputValue(0);
        File file = new File(path);
        StringBuilder sb = new StringBuilder();
        if (file.canRead()) {
            try {
                FileInputStream stream = new FileInputStream(file);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                catch (IOException e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
            catch (FileNotFoundException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return sb.toString();
    }

}
