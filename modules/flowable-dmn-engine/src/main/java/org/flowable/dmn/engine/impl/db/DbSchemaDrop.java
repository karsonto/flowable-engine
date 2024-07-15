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

package org.flowable.dmn.engine.impl.db;

import java.io.Closeable;
import java.io.IOException;

import javax.sql.DataSource;

import org.flowable.common.engine.impl.db.SchemaOperationsEngineDropDbCmd;
import org.flowable.common.engine.impl.interceptor.CommandConfig;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.test.ClosingDataSource;
import org.flowable.dmn.engine.DmnEngines;
import org.flowable.dmn.engine.impl.DmnEngineImpl;

/**
 * @author Tijs Rademakers
 */
public class DbSchemaDrop {

    public static void main(String[] args) {
        DmnEngineImpl dmnEngine = null;
        try {
            dmnEngine = (DmnEngineImpl) DmnEngines.getDefaultDmnEngine();
            CommandExecutor commandExecutor = dmnEngine.getDmnEngineConfiguration().getCommandExecutor();
            CommandConfig config = new CommandConfig().transactionNotSupported();
            commandExecutor.execute(config, new SchemaOperationsEngineDropDbCmd(dmnEngine.getDmnEngineConfiguration().getEngineScopeType()));
        } finally {
            if (dmnEngine != null) {
                DataSource dataSource = dmnEngine.getDmnEngineConfiguration().getDataSource();
                if (dataSource instanceof Closeable) {
                    try {
                        ((Closeable) dataSource).close();
                    } catch (IOException e) {
                        // Ignored
                    }
                } else if (dataSource instanceof ClosingDataSource) {
                    ((ClosingDataSource) dataSource).onEngineClosed(dmnEngine);
                }
            }
        }
    }
}
