/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.updater.service;

import fr.paris.lutece.plugins.updater.service.catalog.CatalogInfos;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;


/**
 * UpdateService tests
 */
public class UpdateServiceTest extends LuteceTestCase
{
    public UpdateServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * Test of getUpdateInfos method, of class UpdateService.
     */
    public void testGetUpdateInfos(  )
    {
        System.out.println( "getUpdateInfos" );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        UpdateService instance = new UpdateService(  );
        instance.getUpdateInfos( listPlugins );
    }

    /**
     * Test of getNewPluginsInfos method, of class UpdateService.
     */
    public void testGetNewPluginsInfos(  )
    {
        System.out.println( "getNewPluginsInfos" );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        UpdateService instance = new UpdateService(  );
        instance.getNewPluginsInfos( listPlugins );
    }

    /**
     * Test of deployPlugin method, of class UpdateService.
     */
    public void testDeployPlugin(  )
    {
        System.out.println( "deployPlugin" );

        String strPluginName = "calendar";
        String strVersion = "2.0.1";
        UpdateService instance = new UpdateService(  );
        instance.deployPlugin( strPluginName, strVersion );
    }

    /**
     * Test of isCompliantWithCurrentCore method, of class UpdateService.
     */
    public void testIsCompliantWithCurrentCore(  )
    {
        System.out.println( "isCompliantWithCurrentCore" );

        UpdateService instance = new UpdateService(  );

        CatalogInfos ci = new CatalogInfos(  );
        ci.setCoreVersionMin( "2.0.0" );
        ci.setCoreVersionMax( "3.0.0" );
        assertTrue( instance.isCompliantWithCurrentCore( ci ) );

        ci.setCoreVersionMin( "2.0.0" );
        ci.setCoreVersionMax( null );
        assertTrue( instance.isCompliantWithCurrentCore( ci ) );

        ci.setCoreVersionMin( "4.0.0" );
        ci.setCoreVersionMax( "5.0.0" );
        assertFalse( instance.isCompliantWithCurrentCore( ci ) );
    }

    /**
     * Test of getStatus method, of class UpdateService.
     */
    public void testGetStatus(  )
    {
        System.out.println( "getStatus" );

        UpdateService instance = new UpdateService(  );
        int result = instance.getStatus(  );
    }

    /**
     * Test of checkUpdate method, of class UpdateService.
     */
    public void testCheckUpdate(  )
    {
        System.out.println( "checkUpdate" );

        Collection<Plugin> listPlugins = null;
        UpdateService instance = new UpdateService(  );
        instance.checkUpdate( listPlugins );
    }

    /**
     * Test of downloadPlugin method, of class UpdateService.
     */
    public void testDownloadPlugin(  ) throws UpdaterDownloadException
    {
        System.out.println( "downloadPlugin" );

        String strPluginName = "calendar";
        String strVersion = "2.0.2";
        UpdateService instance = new UpdateService(  );
        instance.downloadPlugin( strPluginName, strVersion );
    }

    /**
     * Test of downloadPluginUpgrade method, of class UpdateService.
     */
    public void testDownloadPluginUpgrade(  ) throws UpdaterDownloadException
    {
        System.out.println( "downloadPluginUpgrade" );

        String strPluginName = "calendar";
        String strVersion = "2.0.2";
        String strVersionFrom = "2.0.1";
        UpdateService instance = new UpdateService(  );
        instance.downloadPluginUpgrade( strPluginName, strVersion, strVersionFrom );
    }
}
