/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.updater.business.resource.DirectoryTreeResource;
import fr.paris.lutece.plugins.updater.business.resource.FilePatternResource;
import fr.paris.lutece.plugins.updater.business.resource.FileResource;
import fr.paris.lutece.plugins.updater.business.resource.FileSystemResource;
import fr.paris.lutece.plugins.updater.util.sql.SqlUtils;
import fr.paris.lutece.util.filesystem.FileNameComparator;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


/**
 * Plugin Manager Service
 */
public final class PluginManagerService
{
    private static final int FILE = 1;
    private static final int DIRECTORY_TREE = 2;
    private static final int FILE_PREFIX = 3;
    private static final String PATH_BACKUP = "/plugins/updater/backup/";
    private static final String PATH_DEPLOY = "/plugins/updater/deploy/";
    private static final String FOLDER_WEBAPP = "/webapp";
    private static final String FOLDER_SQL = "/sql";
    private static PluginManagerService _singleton = new PluginManagerService(  );
    private static String _strWebAppPath;
    private static Logger _logger = Logger.getLogger( "lutece.plugins.updater" );

    /**
     * Private constructor
     */
    private PluginManagerService(  )
    {
    }

    /**
     * Returns the unique instance of the service
     * @return The unique instance of the service
     */
    public static PluginManagerService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the WebAppPath
     *
     * @return The WebAppPath
     */
    public String getWebAppPath(  )
    {
        // For unit testing _strWebAppPath can be initialized manualy without 
        // using AppPathService.getWebAppPath()
        return _strWebAppPath;
    }

    /**
     * Sets the WebAppPath
     *
     * @param strWebAppPath The WebAppPath
     */
    public void setWebAppPath( String strWebAppPath )
    {
        _strWebAppPath = strWebAppPath;
    }

    /**
     * Clean plugins before update
     * @param nDeleteMode The deletion mode
     */
    public void cleanPluginsMarkedForUpdate( int nDeleteMode )
    {
        _logger.info( "Searching for plugin to remove before update ..." );

        String strDeployDirectory = getWebAppPath(  ) + PATH_DEPLOY;
        File fDeployDirectory = new File( strDeployDirectory );
        boolean bFound = false;

        if ( fDeployDirectory.exists(  ) )
        {
            File[] files = fDeployDirectory.listFiles(  );

            if ( files.length > 0 )
            {
                for ( int i = 0; i < files.length; i++ )
                {
                    File fPluginDirectory = files[i];
                    String strPluginName = fPluginDirectory.getName(  );
                    _logger.info( "An update has been found for plugin : " + strPluginName +
                        ". The installed plugin will be deleted" );
                    bFound = true;
                    cleanPlugin( strPluginName, nDeleteMode, true );
                }
            }
        }

        if ( !bFound )
        {
            _logger.info( "No plugin to removed was found." );
        }
    }

    /**
     * Install new plugins or updates (deploy => webapp)
     */
    public void installPlugins(  )
    {
        _logger.info( "Searching for plugin to install ..." );

        String strDeployDirectory = getWebAppPath(  ) + PATH_DEPLOY;
        File fDeployDirectory = new File( strDeployDirectory );
        boolean bFound = false;

        if ( fDeployDirectory.exists(  ) )
        {
            File[] files = fDeployDirectory.listFiles(  );

            if ( files.length > 0 )
            {
                for ( int i = 0; i < files.length; i++ )
                {
                    File fPluginDirectory = files[i];
                    String strPluginName = fPluginDirectory.getName(  );

                    try
                    {
                        _logger.info( "Installation data has been found for plugin : " + strPluginName );
                        installPlugin( strPluginName );
                        bFound = true;
                        _logger.info( "Plugin '" + strPluginName + "' installed" );
                    }
                    catch ( UpdaterInstallException ex )
                    {
                        _logger.info( "Plugin '" + strPluginName + "'  installation failed : " + ex.getMessage(  ) );
                        _logger.info( "Plugin '" + strPluginName + "'  installation rolled back" );
                        cleanPlugin( strPluginName, 0, false );
                    }
                }
            }
        }

        if ( !bFound )
        {
            _logger.info( "No plugin to install or update was found." );
        }
    }

    /**
     * Clean an installed version of a plugin in the webapp. The process backup
     * and remove the current version of the plugin.
     * @param strPluginName The plugin name
     * @param nDeleteMode The deletion mode
     * @param bBackup Backup before cleaning
     */
    public void cleanPlugin( String strPluginName, int nDeleteMode, boolean bBackup )
    {
        if ( bBackup )
        {
            // backup the plugin
            backupPlugin( strPluginName );
        }

        try
        {
            // remove it from the webapp
            removePlugin( strPluginName, nDeleteMode );
        }
        catch ( Exception e )
        {
            restorePlugin( strPluginName );
        }
    }

    /**
     * Remove a plugin from the webapp
     * @param strPluginName The plugin name
     * @param nDeleteMode The deletion mode
     */
    public void removePlugin( String strPluginName, int nDeleteMode )
    {
        _logger.info( "remove plugin : " + strPluginName );

        List<FileSystemResource> listResources = getPluginResources( strPluginName );

        try
        {
            for ( FileSystemResource resource : listResources )
            {
                resource.delete( nDeleteMode );
                _logger.debug( "resource removed : " + resource.getFullPath(  ) );
            }
        }
        catch ( IOException ex )
        {
            _logger.error( "error removing plugin : ", ex );
        }
    }

    /**
     * Backup the plugin
     * @param strPluginName The plugin name
     */
    public void backupPlugin( String strPluginName )
    {
        _logger.info( "backup plugin : " + strPluginName );

        List<FileSystemResource> listResources = getPluginResources( strPluginName );
        String strBackupPath = PATH_BACKUP + strPluginName + FOLDER_WEBAPP;

        try
        {
            for ( FileSystemResource resource : listResources )
            {
                _logger.debug( "resource copied : " + resource.getFullPath(  ) );
                resource.copy( strBackupPath );
            }
        }
        catch ( IOException ex )
        {
            _logger.error( "error backup plugin : ", ex );
        }
    }

    /**
     * Restore a plugin (backup => webapp)
     * @param strPluginName The plugin name
     */
    public void restorePlugin( String strPluginName )
    {
        _logger.info( "restore plugin : " + strPluginName );

        try
        {
            File fileSource = new File( getWebAppPath(  ) + PATH_BACKUP + strPluginName + FOLDER_WEBAPP );
            File fileDest = new File( getWebAppPath(  ) );

            FileUtils.copyDirectory( fileSource, fileDest );
        }
        catch ( IOException ex )
        {
            _logger.error( "error restoring plugin : ", ex );
        }
    }

    /**
     * Checks if a backup is available for a restore process
     * @param strPluginName The plugin name
     * @return true if the plugin can be restored
     */
    public boolean checkRestorable( String strPluginName )
    {
        String strBackupDirectory = getWebAppPath(  ) + PATH_BACKUP + strPluginName;
        File fileBackupDirectory = new File( strBackupDirectory );

        return fileBackupDirectory.exists(  );
    }

    /**
     * Checks if the installation is in progress
     * @param strPluginName The plugin name
     * @return true if the installation is in progress
     */
    public boolean checkInstallInProgress( String strPluginName )
    {
        String strDeployDirectory = getWebAppPath(  ) + PATH_DEPLOY + strPluginName;
        File fileDeployDirectory = new File( strDeployDirectory );

        return fileDeployDirectory.exists(  );
    }

    /**
     * Cancel an installation
     * @param strPluginName The plugin name
     */
    public void cancelInstallInProgress( String strPluginName )
    {
        // Cancel is performed by removing the plugin directory in the DEPLOY directory
        String strDeployDirectory = getWebAppPath(  ) + PATH_DEPLOY + strPluginName;
        File fileDeployDirectory = new File( strDeployDirectory );

        if ( fileDeployDirectory.exists(  ) )
        {
            try
            {
                FileUtils.deleteDirectory( fileDeployDirectory );
            }
            catch ( IOException ex )
            {
                _logger.error( "error installing plugin : ", ex );
            }
        }
    }

    /**
     * Return the PATH to deploy webapp files of a plugin
     * @param strPluginName The plugin
     * @return The path
     */
    public String getDeployWebappPath( String strPluginName )
    {
        return getWebAppPath(  ) + PATH_DEPLOY + strPluginName + FOLDER_WEBAPP;
    }

    /**
     * Return the PATH to deploy webapp files of a plugin
     * @param strPluginName The plugin
     * @return The path
     */
    public String getDeploySqlPath( String strPluginName )
    {
        return getWebAppPath(  ) + PATH_DEPLOY + strPluginName + FOLDER_SQL;
    }

    /**
     * Runs SQL scripts
     * @param strScriptsDirectory The scripts directory
     * @throws UpdaterScriptException If a script exception occurs
     */
    private void executeScripts( String strScriptsDirectory )
        throws UpdaterScriptException
    {
        File fileScriptsDirectory = new File( strScriptsDirectory );

        if ( fileScriptsDirectory.exists(  ) )
        {
            try
            {
                // Use a treeset to order files with a comparator
                TreeSet<File> set = new TreeSet<File>( new FileNameComparator(  ) );

                File[] files = fileScriptsDirectory.listFiles(  );

                if ( files != null )
                {
                    for ( int i = 0; i < files.length; i++ )
                    {
                        set.add( files[i] );
                    }

                    for ( File file : set )
                    {
                        SqlUtils.executeSqlFileScript( file.getAbsolutePath(  ), null );
                    }
                }
            }
            catch ( IOException e )
            {
                throw new UpdaterScriptException( "file not found", e );
            }
            catch ( SQLException e )
            {
                throw new UpdaterScriptException( "SQL Error", e );
            }
        }
    }

    /**
     * Gives all files of a plugin
     * @param strPluginName The plugin name
     * @return A list of all file resources owned by a plugin
     */
    private List<FileSystemResource> getPluginResources( String strPluginName )
    {
        List<FileSystemResource> listResources = new ArrayList<FileSystemResource>(  );
        addResource( "/css/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/js/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/images/admin/skin/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/images/local/skin/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/jsp/site/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/jsp/admin/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/WEB-INF/classes/fr/paris/lutece/plugins/" + strPluginName + "/resources", DIRECTORY_TREE,
            listResources );
        addResource( "/WEB-INF/conf/plugins/" + strPluginName + ".properties", FILE, listResources );
        addResource( "/WEB-INF/conf/plugins/" + strPluginName + "_context.xml", FILE, listResources );
        addResource( "/WEB-INF/plugins/" + strPluginName + ".xml", FILE, listResources );
        //        addResource( "/WEB-INF/plugins/" + strPluginName, DIRECTORY_TREE, listResources );   // Plugin's data
        addResource( "/WEB-INF/sql/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/WEB-INF/templates/admin/plugins/" + strPluginName, DIRECTORY_TREE, listResources );
        addResource( "/WEB-INF/templates/skin/plugins/" + strPluginName, DIRECTORY_TREE, listResources );

        addResource( "/WEB-INF/lib/", "plugin-" + strPluginName, FILE_PREFIX, listResources );
        addResource( "/WEB-INF/xsl/normal/", "portlet_" + strPluginName, FILE_PREFIX, listResources );

        return listResources;
    }

    /**
     * Add a resource to a list
     * @param strResource The resource to add
     * @param nType The resource type
     * @param listResources The list
     */
    private void addResource( String strResource, int nType, List<FileSystemResource> listResources )
    {
        addResource( strResource, "", nType, listResources );
    }

    /**
     * Add a resource to a list
     * @param strResourceDirectory The resource's directory
     * @param strResourcePattern The resource's pattern
     * @param nType The resource type
     * @param listResources The list
     */
    private void addResource( String strResourceDirectory, String strResourcePattern, int nType,
        List<FileSystemResource> listResources )
    {
        try
        {
            File file = new File( getWebAppPath(  ) + "/" + strResourceDirectory );

            if ( file.exists(  ) )
            {
                FileSystemResource resource = null;

                switch ( nType )
                {
                    case FILE:
                        resource = new FileResource( getWebAppPath(  ), strResourceDirectory );
                        _logger.debug( "plugin resource 'file' found : " + file.getPath(  ) );

                        break;

                    case DIRECTORY_TREE:
                        resource = new DirectoryTreeResource( getWebAppPath(  ), strResourceDirectory );
                        _logger.debug( "plugin resource 'directory tree' found : " + file.getPath(  ) );

                        break;

                    case FILE_PREFIX:
                        resource = new FilePatternResource( getWebAppPath(  ), strResourceDirectory,
                                strResourcePattern, FilePatternResource.PATTERN_PREFIX );
                        _logger.debug( "plugin resource 'file pattern' found : " + file.getPath(  ) );

                        break;

                    default:
                        break;
                }

                listResources.add( resource );
            }
            else
            {
                _logger.debug( "plugin resource not found : " + file.getPath(  ) );
            }
        }
        catch ( Exception e )
        {
            _logger.error( "PluginManagerService:AddResource error : " + e.getMessage(  ), e );
        }
    }

    /**
     * Install a plugin
     * @param strPluginName The plugin name
     * @throws UpdaterInstallException If an install exception occurs
     */
    private void installPlugin( String strPluginName )
        throws UpdaterInstallException
    {
        _logger.info( "install plugin : " + strPluginName );

        try
        {
            // Copy webapp's deployment data into the webapp
            File fileSource = new File( getWebAppPath(  ) + PATH_DEPLOY + strPluginName + FOLDER_WEBAPP );
            File fileDest = new File( getWebAppPath(  ) );

            FileUtils.copyDirectory( fileSource, fileDest );

            // Execute sql scripts
            executeScripts( getWebAppPath(  ) + PATH_DEPLOY + strPluginName + FOLDER_SQL );

            // If the installation is OK, then delete deployment data
            File fileDeploy = new File( getWebAppPath(  ) + PATH_DEPLOY + strPluginName );
            FileUtils.deleteDirectory( fileDeploy );
        }
        catch ( IOException ex )
        {
            _logger.error( "error installing plugin : ", ex );
            throw new UpdaterInstallException( "Installation exception", ex );
        }
        catch ( UpdaterScriptException ex )
        {
            throw new UpdaterInstallException( "Installation exception", ex );
        }
    }
}
