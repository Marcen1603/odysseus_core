/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package de.uniol.inf.is.odysseus.rest2.server.exception;

import org.osgi.service.component.annotations.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * ExceptionMapper which handled SymbolNotFoundException.
 */
@Component(
        name = "org.wso2.msf4j.stockquote.exception.SymbolNotFoundMapper",
        service = ExceptionMapper.class,
        immediate = true
)
public class SymbolNotFoundMapper implements ExceptionMapper<SymbolNotFoundException> {

    public Response toResponse(SymbolNotFoundException ex) {
        return Response.status(404).
                entity(ex.getMessage() + " [from SymbolNotFoundMapper]").
                type("text/plain").
                build();
    }
}
