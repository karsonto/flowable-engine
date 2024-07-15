/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.spring.test;

import java.io.Closeable;
import java.io.IOException;

import javax.sql.DataSource;

import org.flowable.common.engine.impl.db.SchemaOperationsEngineDropDbCmd;
import org.flowable.common.engine.impl.interceptor.CommandConfig;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.test.ClosingDataSource;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;

/**
 * @author Filip Hrisafov
 */
public class DbSchemaDrop {

    public static void main(String[] args) {
        ProcessEngine processEngine = null;
        try {
            processEngine = ProcessEngineConfigurationImpl.createProcessEngineConfigurationFromResource("flowable.cfg.xml")
                    .buildProcessEngine();
            CommandExecutor commandExecutor = processEngine.getProcessEngineConfiguration().getCommandExecutor();
            CommandConfig config = new CommandConfig().transactionNotSupported();
            commandExecutor.execute(config, new SchemaOperationsEngineDropDbCmd(processEngine.getProcessEngineConfiguration().getEngineScopeType()));
        } finally {
            if (processEngine != null) {
                DataSource dataSource = processEngine.getProcessEngineConfiguration().getDataSource();
                if (dataSource instanceof Closeable) {
                    try {
                        ((Closeable) dataSource).close();
                    } catch (IOException e) {
                        // Ignored
                    }
                } else if (dataSource instanceof ClosingDataSource) {
                    ((ClosingDataSource) dataSource).onEngineClosed(processEngine);
                }
            }
        }
    }

}
