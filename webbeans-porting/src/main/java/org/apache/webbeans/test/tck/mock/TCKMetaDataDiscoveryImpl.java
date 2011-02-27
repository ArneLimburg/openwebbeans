/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.test.tck.mock;

import java.util.HashSet;
import java.util.Set;

import org.apache.webbeans.corespi.scanner.AbstractMetaDataDiscovery;
import org.apache.webbeans.util.Asserts;

import javax.enterprise.classscan.ScanJob;

public class TCKMetaDataDiscoveryImpl extends AbstractMetaDataDiscovery
{
    private Set<Class<?>> classesToScan = new HashSet<Class<?>>();

    public TCKMetaDataDiscoveryImpl()
    {
        super();
    }
    
    @Override
    protected void configure() throws Exception
    {
        ScanJob scanJob = new ScanJob(null, null, null, true, true, true, true);
        scanJob.setClassesToScan(classesToScan.toArray(new Class<?>[classesToScan.size()]));
        OwbTckClassScanClient.setScanJob(scanJob);
    }

    public void addBeanClass(Class<?> clazz)
    {
        Asserts.assertNotNull(clazz);
        classesToScan.add(clazz);
    }
    
    public void addBeanXml(String url)
    {
        Asserts.assertNotNull(url);
        addWebBeansXmlLocation(url);
    }
    
}
