/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.updater.business.resource.FileSystemResource;

import junit.framework.TestCase;


/**
 * PluginManagerService Test
 */
public class PluginManagerServiceTest extends TestCase
{
    private static final String PLUGIN_NAME = "contact";
    private static final String PATH_WEBAPP = "c:/java/projets/lutece/target/lutece";

    public PluginManagerServiceTest( String testName )
    {
        super( testName );
    }

    @Override
    protected void setUp(  ) throws Exception
    {
        super.setUp(  );
    }

    @Override
    protected void tearDown(  ) throws Exception
    {
        super.tearDown(  );
    }

    /**
     * Test of backupPlugin method, of class PluginManagerService.
     */
    public void testBackupPlugin(  )
    {
        System.out.println( "backupPlugin" );

        String strPluginName = PLUGIN_NAME;
        PluginManagerService instance = PluginManagerService.getInstance(  );
        instance.setWebAppPath( PATH_WEBAPP );
        instance.backupPlugin( strPluginName );
    }

    /**
     * Test of updatePlugin method, of class PluginManagerService.
     */
    public void testUpdatePlugin(  )
    {
        /*
        System.out.println("updatePlugin");
        String strPluginName = PLUGIN_NAME;
        PluginManagerService instance = PluginManagerService.getInstance();
        instance.setWebAppPath(WEBAPP_PATH);
        instance.updatePlugin(strPluginName);
         */
    }

    /**
     * Test of removePlugin method, of class PluginManagerService.
     */
    public void testRemovePlugin(  )
    {
        System.out.println( "removePlugin" );

        String strPluginName = PLUGIN_NAME;
        PluginManagerService instance = PluginManagerService.getInstance(  );
        instance.setWebAppPath( PATH_WEBAPP );
        instance.removePlugin( strPluginName, FileSystemResource.DELETE_NOW );
    }
}
