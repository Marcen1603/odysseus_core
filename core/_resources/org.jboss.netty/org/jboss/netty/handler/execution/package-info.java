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
 * {@link java.util.concurrent.Executor}-based implementation of various
 * thread models that separate business logic from I/O threads
 *
 * @apiviz.exclude ^java\.lang\.
 * @apiviz.exclude \.netty\.channel\.
 * @apiviz.exclude \.ExternalResourceReleasable$
 * @apiviz.exclude \.Channel[A-Za-z]*EventRunnable[A-Za-z]*$
 */
package org.jboss.netty.handler.execution;
