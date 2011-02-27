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
package org.apache.webbeans.corespi.scanner;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.webbeans.exception.WebBeansDeploymentException;
import org.apache.webbeans.logger.WebBeansLogger;
import org.apache.webbeans.spi.BDABeansXmlScanner;
import org.apache.webbeans.spi.ScannerService;
import org.apache.webbeans.util.ClassUtil;

import javax.enterprise.classscan.ClassScanner;


public abstract class AbstractMetaDataDiscovery implements ScannerService
{
    protected final WebBeansLogger logger = WebBeansLogger.getLogger(getClass());

    public static final String META_INF_BEANS_XML = "META-INF/beans.xml";

    /** Location of the beans.xml files. */
    private final Set<String> webBeansXmlLocations = new HashSet<String>();

    //private Map<String, InputStream> EJB_XML_LOCATIONS = new HashMap<String, InputStream>();


    protected boolean isBDAScannerEnabled = false;
    protected BDABeansXmlScanner bdaBeansXmlScanner;

    protected AbstractMetaDataDiscovery()
    {
    }
    
    /**
     * Configure the Web Beans Container with deployment information and fills
     * annotation database and beans.xml stream database.
     * 
     * @throws org.apache.webbeans.exception.WebBeansConfigurationException if any run time exception occurs
     */
    public void scan() throws WebBeansDeploymentException
    {
        try
        {
            configure();
        }
        catch (Exception e)
        {
            throw new WebBeansDeploymentException(e);
        }
    }
    
    
    abstract protected void configure() throws Exception;

    /**
     * Find the base paths of all available resources with the given
     * resourceName in the classpath.
     * The returned Strings will <i>NOT</i> contain the resourceName itself!
     *
     * @param resourceName the name of the resource, e.g. 'META-INF/beans.xml'
     * @param loader the ClassLoader which should be used
     * @return array of Strings with the URL path to the resources.
     */
    protected String[] findBeansXmlBases(String resourceName, ClassLoader loader)
    {
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            Enumeration<URL> urls = loader.getResources(resourceName);

            while (urls.hasMoreElements())
            {
                URL url = urls.nextElement();
                String urlString = url.toString();

                addWebBeansXmlLocation(urlString);

                int idx = urlString.lastIndexOf(resourceName);
                urlString = urlString.substring(0, idx);

                list.add(urlString);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return list.toArray(new String[list.size()]);

    }


    
    public void init(Object object)
    {
    }

    /**
     * @return the aNNOTATION_DB
     */
    /*X
    protected AnnotationDB getAnnotationDB()
    {
        return annotationDB;
    }
    */

    /**
     * add the given beans.xml path to the locations list 
     * @param beansXmlLocation location path
     */
    protected void addWebBeansXmlLocation(String beansXmlLocation)
    {
        if(this.logger.wblWillLogInfo())
        {
            this.logger.info("added beans.xml marker: " + beansXmlLocation);
        }
        webBeansXmlLocations.add(beansXmlLocation);
    }

    /* (non-Javadoc)
     * @see org.apache.webbeans.corespi.ScannerService#getBeanClasses()
     */
    @Override
    public Set<Class<?>> getBeanClasses()
    {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        Map<String,Set<String>> index = ClassScanner.getInstance().getClassesIndex(OwbClassScanClient.SCANNER_CLIENT_NAME);
        
        if(index != null)
        {
            Set<String> strSet = index.keySet();
            if(strSet != null)
            {
                for(String str : strSet)
                {
                    classSet.add(ClassUtil.getClassFromName(str));   
                }
            }   
        }    
        
        return classSet;
    }    


    /* (non-Javadoc)
     * @see org.apache.webbeans.corespi.ScannerService#getBeanXmls()
     */
    @Override
    public Set<String> getBeanXmls()
    {
        return Collections.unmodifiableSet(webBeansXmlLocations);
    }

    @Override
    public BDABeansXmlScanner getBDABeansXmlScanner()
    {
        return bdaBeansXmlScanner;
    }

    @Override
    public boolean isBDABeansXmlScanningEnabled()
    {
        return isBDAScannerEnabled;
    }
}
