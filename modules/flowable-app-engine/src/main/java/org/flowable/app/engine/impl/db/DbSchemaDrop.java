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

package org.flowable.app.engine.impl.db;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.flowable.app.engine.AppEngine;
import org.flowable.app.engine.AppEngineConfiguration;
import org.flowable.app.engine.test.FlowableAppTestCase;
import org.flowable.common.engine.impl.db.SchemaOperationsEngineDropDbCmd;
import org.flowable.common.engine.impl.interceptor.CommandConfig;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.test.ClosingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tijs Rademakwrs
 */
public class DbSchemaDrop {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DbSchemaDrop.class);

    public static void main(String[] args) {
        AppEngine appEngine = null;
        try (InputStream inputStream = FlowableAppTestCase.class.getClassLoader().getResourceAsStream("flowable.app.cfg.xml")) {
            appEngine = AppEngineConfiguration.createAppEngineConfigurationFromInputStream(inputStream).buildAppEngine();
            CommandExecutor commandExecutor = appEngine.getAppEngineConfiguration().getCommandExecutor();
            CommandConfig config = new CommandConfig().transactionNotSupported();
            commandExecutor.execute(config, new SchemaOperationsEngineDropDbCmd(appEngine.getAppEngineConfiguration().getEngineScopeType()));
            
        } catch (IOException e) {
            LOGGER.error("Could not create App engine", e);
        } finally {
            if (appEngine != null) {
                DataSource dataSource = appEngine.getAppEngineConfiguration().getDataSource();
                if (dataSource instanceof Closeable) {
                    try {
                        ((Closeable) dataSource).close();
                    } catch (IOException e) {
                        // Ignored
                    }
                } else if (dataSource instanceof ClosingDataSource) {
                    ((ClosingDataSource) dataSource).onEngineClosed(appEngine);
                }
            }
        }
    }
}
