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

package org.flowable.app.spring.autodeployment;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.zip.ZipInputStream;

import org.flowable.app.api.repository.AppDeploymentBuilder;
import org.flowable.app.engine.AppEngine;
import org.flowable.common.engine.api.lock.LockManager;
import org.flowable.common.spring.CommonAutoDeploymentProperties;
import org.flowable.common.spring.CommonAutoDeploymentStrategy;
import org.springframework.core.io.Resource;

/**
 * Abstract base class for implementations of {@link org.flowable.common.spring.AutoDeploymentStrategy AutoDeploymentStrategy}.
 *
 * @author Tiese Barrell
 * @author Filip Hrisafov
 */
public abstract class AbstractAppAutoDeploymentStrategy extends CommonAutoDeploymentStrategy<AppEngine> {

    public AbstractAppAutoDeploymentStrategy() {
    }

    public AbstractAppAutoDeploymentStrategy(CommonAutoDeploymentProperties deploymentProperties) {
        super(deploymentProperties);
    }

    @Override
    protected LockManager getLockManager(AppEngine engine, String deploymentNameHint) {
        // The app engine does not have deployment name
        return engine.getAppEngineConfiguration().getLockManager(determineLockName("appDeploymentsLock"));
    }

    protected void addResource(Resource resource, AppDeploymentBuilder deploymentBuilder) {
        String resourceName = determineResourceName(resource);
        addResource(resource, resourceName, deploymentBuilder);
    }

    protected void addResource(Resource resource, String resourceName, AppDeploymentBuilder deploymentBuilder) {
        try (InputStream inputStream = resource.getInputStream()) {
            if (resourceName.endsWith(".bar") || resourceName.endsWith(".zip")) {
                try (ZipInputStream zipStream = new ZipInputStream(inputStream)) {
                    deploymentBuilder.addZipInputStream(zipStream);
                }
            } else {
                deploymentBuilder.addInputStream(resourceName, inputStream);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to read resource " + resource, ex);
        }
    }
}
