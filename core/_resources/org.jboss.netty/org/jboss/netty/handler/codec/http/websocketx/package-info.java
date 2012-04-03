/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 * Encoder, decoder, handshakers and their related message types for
 * <a href="http://en.wikipedia.org/wiki/Web_Sockets">Web Socket</a> data frames.
 * <p>
 * This package supports different web socket specification versions (hence the X suffix).
 * The specification current supported are:
 * <ul>
 * <li><a href="http://tools.ietf.org/html/draft-ietf-hybi-thewebsocketprotocol-00">draft-ietf-hybi-thewebsocketprotocol-00</a></li>
 * <li><a href="http://tools.ietf.org/html/draft-ietf-hybi-thewebsocketprotocol-10">draft-ietf-hybi-thewebsocketprotocol-10</a></li>
 * <li><a href="http://tools.ietf.org/html/rfc6455 ">RFC 6455</a> (originally <a href="http://tools.ietf.org/html/draft-ietf-hybi-thewebsocketprotocol-17">draft-ietf-hybi-thewebsocketprotocol-17</a>)</li>
 * </ul>
 * </p>
 * <p>
 * For the detailed instruction on adding add Web Socket support to your HTTP
 * server, take a look into the <tt>WebSocketServerX</tt> example located in the
 * {@code org.jboss.netty.example.http.websocket} package.
 * </p> 
 * 
 * @apiviz.exclude OneToOne(Encoder|Decoder)$
 * @apiviz.exclude \.codec\.replay\.
 * @apiviz.exclude \.Default
 */
package org.jboss.netty.handler.codec.http.websocketx;

