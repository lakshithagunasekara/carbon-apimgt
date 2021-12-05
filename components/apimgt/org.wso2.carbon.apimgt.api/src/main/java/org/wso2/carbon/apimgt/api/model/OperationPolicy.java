/*
 *
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package org.wso2.carbon.apimgt.api.model;

public class OperationPolicy {

    private String uuid;
    private String name;
    private String flow;
    private String specification;
    private String template;
    private boolean isGlobal;

    public OperationPolicy(){}

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getFlow() {

        return flow;
    }

    public void setFlow(String flow) {

        this.flow = flow;
    }

    public String getSpecification() {

        return specification;
    }

    public void setSpecification(String specification) {

        this.specification = specification;
    }

    public String getTemplate() {

        return template;
    }

    public void setTemplate(String template) {

        this.template = template;
    }

    public boolean isGlobal() {

        return isGlobal;
    }

    public void setGlobal(boolean global) {

        isGlobal = global;
    }
}
