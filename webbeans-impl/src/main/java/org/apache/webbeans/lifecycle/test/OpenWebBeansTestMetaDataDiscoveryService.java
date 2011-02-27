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
package org.apache.webbeans.lifecycle.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.webbeans.corespi.scanner.AbstractMetaDataDiscovery;
import org.apache.webbeans.corespi.scanner.OwbClassScanClient;
import org.apache.webbeans.util.Asserts;

import javax.enterprise.classscan.ClassScanner;
import javax.enterprise.classscan.ScanJob;

/**
 * Used by each test. 
 * @version $Rev$ $Date$
 *
 */
public class OpenWebBeansTestMetaDataDiscoveryService extends AbstractMetaDataDiscovery
{
    private Set<Class<?>> classesToScan = new HashSet<Class<?>>();

    public OpenWebBeansTestMetaDataDiscoveryService()
    {
        
    }

    @Override
    protected void configure() throws Exception
    {
        ScanJob scanJob = new ScanJob(null, null, null, true, true, true, true);
        scanJob.setClassesToScan(classesToScan.toArray(new Class<?>[classesToScan.size()]));
        OwbTestClassScanClient.setScanJob(scanJob);
    }
    
    /**
     * Those classes will be scanned by container.
     * @param classes deployed classes
     */
    public void deployClasses(Collection<Class<?>> classes)
    {
        if(classes != null)
        {
            for(Class<?> clazz : classes)
            {
                addBeanClass(clazz);
            }
        }
    }
    
    /**
     * Those xmls will be scanned by container.
     * @param xmls beans xmls
     */
    public void deployXMLs(Collection<String> xmls)
    {
        if(xmls != null)
        {
            for(String xml : xmls)
            {
                addBeanXml(xml);
            }
        }
    }
    
    
    /**
     * Adds new class for scanning.
     * @param clazz new scanned class
     */
    private void addBeanClass(Class<?> clazz)
    {
        Asserts.assertNotNull(clazz);
        classesToScan.add(clazz);
    }
    
    /**
     * Adds new beans.xml url for scanning.
     * @param beansXmlPath new beans.xml path
     */
    private void addBeanXml(String beansXmlPath)
    {
        Asserts.assertNotNull(beansXmlPath);
        
        addWebBeansXmlLocation(beansXmlPath);
    }

    @Override
    public Set<Class<?>> getBeanClasses()
    {
        // first deregister the default OWB scanner client, so we don't pick up any unwanted stuff...
        ClassScanner.getInstance().vetoClient(OwbClassScanClient.SCANNER_CLIENT_NAME);

        return super.getBeanClasses();
    }

}
