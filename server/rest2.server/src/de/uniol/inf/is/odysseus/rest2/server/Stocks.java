/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
 */

package de.uniol.inf.is.odysseus.rest2.server;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a list of Stock objects. This wrapper is
 * required for proper xml list marshaling.
 */
@XmlRootElement
public class Stocks {

    @XmlElement(name = "stock")
    private Collection<Stock> stocks;

    /**
     * No arg constructor is required for xml marshalling
     */
    public Stocks() {
    }

    public Stocks(Collection<Stock> stocks) {
        this.stocks = stocks;
    }

    public Collection<Stock> getStocks() {
        return stocks;
    }
}
