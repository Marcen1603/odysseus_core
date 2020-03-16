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
package de.uniol.inf.is.odysseus.script.executor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.misc.ShutdownCommand;
import de.uniol.inf.is.odysseus.script.executor.permission.GrantPermissionPreParserKeyword;
import de.uniol.inf.is.odysseus.script.executor.permission.GrantRolesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.executor.permission.RevokePermissionPreParserKeyword;
import de.uniol.inf.is.odysseus.script.executor.permission.RevokeRolesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.RunCommandPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.ShutdownPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.SleepPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.ActivateRewriteRulePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.BufferPlacementPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.DeActivateRewriteRulePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.ParserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.PlanGenerationMethodPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.PreTransformationHandlerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.configuration.TransCfgPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.feature.RequiredFeaturePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.feature.ResetUpdateSitePreparserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.feature.UpdateFeaturePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.feature.UpdateSitePreparserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.ACQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.AddQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.DropAllQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.ExecuteQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.PartialQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.RemoveQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.ResumeQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.StartAllClosedQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.StartQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.StopAllQueriesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.StopQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.SuspendQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.query.WaitForQueryPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.scheduler.SchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.scheduler.StartSchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.scheduler.StopSchedulerPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.sourcessinks.DropAllSinksPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.sourcessinks.DropAllSourcesPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.sourcessinks.DropSinkPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.sourcessinks.DropSourcePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.AlterUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.CreateRolePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.CreateUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.DropRolePreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.DropUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.LoginUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.keyword.user.LogoutUserPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeywordProvider;

@SuppressWarnings("deprecation")
public class KeywordProvider implements IPreParserKeywordProvider {

	@Override
	public Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		Map<String, Class<? extends IPreParserKeyword>> keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();

		keywords.put(LoginUserPreParserKeyword.LOGIN, LoginUserPreParserKeyword.class);
		keywords.put(LogoutUserPreParserKeyword.LOGOUT, LogoutUserPreParserKeyword.class);
		keywords.put(AddQueryPreParserKeyword.QUERY, AddQueryPreParserKeyword.class);
		keywords.put(AddQueryPreParserKeyword.ADDQUERY, AddQueryPreParserKeyword.class);
		keywords.put(ExecuteQueryPreParserKeyword.RUNQUERY, ExecuteQueryPreParserKeyword.class);
		keywords.put(ACQueryPreParserKeyword.NAME, ACQueryPreParserKeyword.class);
		keywords.put(StartAllClosedQueriesPreParserKeyword.STARTQUERIES, StartAllClosedQueriesPreParserKeyword.class);
		keywords.put(StopAllQueriesPreParserKeyword.NAME, StopAllQueriesPreParserKeyword.class);
		keywords.put(SchedulerPreParserKeyword.SCHEDULER, SchedulerPreParserKeyword.class);
		keywords.put(DropAllQueriesPreParserKeyword.DROPALLQUERIES, DropAllQueriesPreParserKeyword.class);
		keywords.put(DropAllSinksPreParserKeyword.DROPALLSINKS, DropAllSinksPreParserKeyword.class);
		keywords.put(DropAllSourcesPreParserKeyword.DROPALLSOURCES, DropAllSourcesPreParserKeyword.class);
		keywords.put(StartSchedulerPreParserKeyword.KEYWORD, StartSchedulerPreParserKeyword.class);
		keywords.put(StopSchedulerPreParserKeyword.KEYWORD, StopSchedulerPreParserKeyword.class);
		keywords.put(BufferPlacementPreParserKeyword.BUFFERPLACEMENT, BufferPlacementPreParserKeyword.class);
		keywords.put(ParserPreParserKeyword.PARSER, ParserPreParserKeyword.class);
		keywords.put(TransCfgPreParserKeyword.TRANSCFG, TransCfgPreParserKeyword.class);
		//keywords.put(CreateStoredProcedureKeyword.STORED_PROCEDURE, CreateStoredProcedureKeyword.class);
		//keywords.put(DropStoredProcedure.DROPPROCEDURE, DropStoredProcedure.class);
		keywords.put(PlanGenerationMethodPreParserKeyword.PLANGENERATIONMETHOD, PlanGenerationMethodPreParserKeyword.class);
		keywords.put(RequiredFeaturePreParserKeyword.REQUIRED, RequiredFeaturePreParserKeyword.class);
		keywords.put(UpdateFeaturePreParserKeyword.UPDATE, UpdateFeaturePreParserKeyword.class);
		keywords.put(PreTransformationHandlerPreParserKeyword.KEYWORD, PreTransformationHandlerPreParserKeyword.class);
		keywords.put(ActivateRewriteRulePreParserKeyword.ACTIVATEREWRITERULE, ActivateRewriteRulePreParserKeyword.class);
		keywords.put(DeActivateRewriteRulePreParserKeyword.DEACTIVATEREWRITERULE, DeActivateRewriteRulePreParserKeyword.class);
		keywords.put(SleepPreParserKeyword.NAME, SleepPreParserKeyword.class);
		keywords.put(PartialQueryPreParserKeyword.NAME, PartialQueryPreParserKeyword.class);
		keywords.put(ResumeQueryPreParserKeyword.NAME,ResumeQueryPreParserKeyword.class);
		keywords.put(StartQueryPreParserKeyword.NAME,StartQueryPreParserKeyword.class);
		keywords.put(StopQueryPreParserKeyword.NAME, StopQueryPreParserKeyword.class);
		keywords.put(SuspendQueryPreParserKeyword.NAME, SuspendQueryPreParserKeyword.class);
		keywords.put(RemoveQueryPreParserKeyword.NAME, RemoveQueryPreParserKeyword.class);
		keywords.put(WaitForQueryPreParserKeyword.NAME, WaitForQueryPreParserKeyword.class);
		keywords.put(UpdateSitePreparserKeyword.NAME, UpdateSitePreparserKeyword.class);
		keywords.put(ResetUpdateSitePreparserKeyword.NAME, ResetUpdateSitePreparserKeyword.class);
		keywords.put(RunCommandPreParserKeyword.NAME, RunCommandPreParserKeyword.class);
		
		keywords.put(DropSourcePreParserKeyword.NAME, DropSourcePreParserKeyword.class);
		keywords.put(DropSinkPreParserKeyword.NAME, DropSinkPreParserKeyword.class);
		keywords.put(CreateUserPreParserKeyword.NAME, CreateUserPreParserKeyword.class);
		keywords.put(DropUserPreParserKeyword.NAME, DropUserPreParserKeyword.class);
		keywords.put(AlterUserPreParserKeyword.NAME, AlterUserPreParserKeyword.class);
		keywords.put(AlterUserPreParserKeyword.NAME2, AlterUserPreParserKeyword.class);
		
		keywords.put(CreateRolePreParserKeyword.NAME, CreateRolePreParserKeyword.class);
		keywords.put(DropRolePreParserKeyword.NAME, DropRolePreParserKeyword.class);	
		
		keywords.put(GrantRolesPreParserKeyword.NAME, GrantRolesPreParserKeyword.class);
		keywords.put(GrantPermissionPreParserKeyword.NAME, GrantPermissionPreParserKeyword.class);
		keywords.put(RevokePermissionPreParserKeyword.NAME, RevokePermissionPreParserKeyword.class);
		keywords.put(RevokeRolesPreParserKeyword.NAME, RevokeRolesPreParserKeyword.class);
		
		keywords.put(ShutdownPreParserKeyword.NAME, ShutdownPreParserKeyword.class);
		
		return keywords;
	}
}
