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
package de.uniol.inf.is.odysseus.wrapper.nmea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;

import de.offis.nmea.python.PythonScript;
import de.offis.nmea.sentence.Sentence;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class NMEAProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
    protected BufferedReader reader;
    private KeyValueObject<? extends IMetaAttribute> next = null;

    public NMEAProtocolHandler() {
    }

    public NMEAProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        super(direction, access, dataHandler);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
        reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
    }

    @Override
    public void close() throws IOException {
        reader.close();
        getTransportHandler().close();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (!reader.ready()) {
        	return false;
        }
        String nmea = reader.readLine();
        Sentence sentence = (Sentence) PythonScript.getInstance().parseNmea(nmea);
        if (sentence == null) {
        	return false;
        }
        Map<String, Object> event = new HashMap<String, Object>();
        toMap(event, "", (BasicEObjectImpl) sentence);
        next = new KeyValueObject<>(event);
        next.setMetadata("object", sentence);
        return true;
    }

    @Override
    public KeyValueObject<? extends IMetaAttribute> getNext() throws IOException {
        return next;
    }

    @Override
    public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options,
            IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        NMEAProtocolHandler instance = new NMEAProtocolHandler(direction, access, dataHandler);
        instance.setOptionsMap(options);
        return instance;
    }

    @Override
    public String getName() {
        return "NMEA";
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof NMEAProtocolHandler)) {
            return false;
        }
        else {
            // the datahandler was already checked in the
            // isSemanticallyEqual-Method of AbstracProtocolHandler
            return true;
        }
    }
    
    public static Map<String, Object> toMap(Map<String, Object> res, String baseName, BasicEObjectImpl eObj) {
		for (EAttribute attr : eObj.eClass().getEAllAttributes()) {
			if (attr.getName().equals("nmea")) {
				continue;
			}
			int id = attr.getFeatureID();
			Object o = eObj.eGet(id, true, true);
			if (attr.getEType() instanceof EEnum) {
				res.put(baseName + attr.getName(), o.toString());
			} else {
				res.put(baseName + attr.getName(), o);
			}
		}
		
		for (EReference ref : eObj.eClass().getEAllReferences()) {
			int id = ref.getFeatureID();
			Object o = eObj.eGet(id, true, true);
			if (o == null || !(o instanceof BasicEObjectImpl)) {
				continue;
			}
			toMap(res, ref.getName() + ".", (BasicEObjectImpl) o);
		}
		return res;
	}
}
