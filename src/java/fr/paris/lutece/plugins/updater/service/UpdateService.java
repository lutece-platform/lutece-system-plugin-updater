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

import fr.paris.lutece.plugins.updater.business.version.InvalidVersionException;
import fr.paris.lutece.plugins.updater.business.version.Version;
import fr.paris.lutece.plugins.updater.service.catalog.CatalogInfos;
import fr.paris.lutece.plugins.updater.service.catalog.ICatalogService;
import fr.paris.lutece.plugins.updater.service.catalog.UpgradeInfos;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * UpdateService
 */
public class UpdateService implements IUpdateService
{
    private static final String PATH_DOWNLOADED = "/WEB-INF/plugins/updater/downloaded/";
    private static final String FOLDER_WEBAPP = "/webapp";
    private static final String FOLDER_SQL = "/sql";
    private static final String PLUGIN_NAME = "updater";
    private static final String BEAN_CATALOG_SERVICE = "updater.catalogService";
    private static Version _currentCoreVersion;
    private static int _nStatus;

    /**
     * Gets available update for a list of plugins
     * @param listPlugins The list of installed plugins
     * @return Update infos for installed plugins
     */
    public List<UpdateInfos> getUpdateInfos( Collection<Plugin> listPlugins )
    {
        ICatalogService catalogService = (ICatalogService) SpringContextService.getPluginBean( PLUGIN_NAME, BEAN_CATALOG_SERVICE );
        List<CatalogInfos> listCatalogInfos = catalogService.getCatalogInfos(  );
        List<UpdateInfos> listUpdatesInfos = new ArrayList<UpdateInfos>(  );

        if ( listPlugins != null )
        {
            for ( Plugin plugin : listPlugins )
            {
                for ( CatalogInfos ci : listCatalogInfos )
                {
                    if ( ci.getPluginName(  ).equals( plugin.getName(  ) ) )
                    {
                        try
                        {
                            Version vCurrent = Version.parse( plugin.getVersion(  ) );
                            Version vRepository = Version.parse( ci.getVersion(  ) );

                            // Display only upgrade for plugins that have a more recent version number
                            if ( vRepository.compareTo( vCurrent ) > 0 )
                            {
                                for ( UpgradeInfos upgrade : ci.getUpgrades(  ) )
                                {
                                    // Find upgrade corresponding to the current version
                                    if ( upgrade.getVersionFrom(  ).equals( plugin.getVersion(  ) ) &&
                                            isCompliantWithCurrentCore( ci ) )
                                    {
                                        UpdateInfos ui = new UpdateInfos( ci.getPluginName(  ) );
                                        ui.setCurrentVersion( plugin.getVersion(  ) );
                                        ui.setTargetVersion( ci.getVersion(  ) );
                                        ui.setCriticalUpdate( upgrade.getCriticalUpdate(  ) );
                                        ui.setDownloaded( checkDownloaded( ci.getPluginName(  ), ci.getVersion(  ) ) );
                                        ui.setInstallInProgress( PluginManagerService.getInstance(  )
                                                                                     .checkInstallInProgress( ci.getPluginName(  ) ) );
                                        listUpdatesInfos.add( ui );
                                    }
                                }
                            }
                        }
                        catch ( InvalidVersionException ex )
                        {
                            AppLogService.error( "Invalid version number for plugin : " + ci.getPluginName(  ), ex );
                        }
                    }
                }
            }
        }

        return listUpdatesInfos;
    }

    /**
     * Gets available plugins not already installed
     * @param listPlugins The list of installed plugins
     * @return New plugins infos list
     */
    public List<NewInfos> getNewPluginsInfos( Collection<Plugin> listPlugins )
    {
        ICatalogService catalogService = (ICatalogService) SpringContextService.getPluginBean( PLUGIN_NAME, BEAN_CATALOG_SERVICE );
        List<CatalogInfos> listCatalogInfos = catalogService.getCatalogInfos(  );
        List<NewInfos> listNewPluginInfos = new ArrayList<NewInfos>(  );

        for ( CatalogInfos ci : listCatalogInfos )
        {
            if ( !isInstalled( ci.getPluginName(  ), listPlugins ) && isCompliantWithCurrentCore( ci ) )
            {
                NewInfos ni = new NewInfos( ci.getPluginName(  ) );
                ni.setDescription( ci.getDescription(  ) );
                ni.setVersion( ci.getVersion(  ) );
                ni.setAuthor( ci.getAuthor(  ) );
                ni.setHomepageUrl( ci.getHomepageUrl(  ) );
                ni.setDownloaded( checkDownloaded( ci.getPluginName(  ), ci.getVersion(  ) ) );
                ni.setInstallInProgress( PluginManagerService.getInstance(  )
                                                             .checkInstallInProgress( ci.getPluginName(  ) ) );
                listNewPluginInfos.add( ni );
            }
        }

        return listNewPluginInfos;
    }

    /**
     * Deploy a plugin (downloaded => deploy)
     * @param strPluginName The plugin name
     * @param strVersion The update version
     */
    public void deployPlugin( String strPluginName, String strVersion )
    {
        AppLogService.info( "deploy plugin : " + strPluginName );

        try
        {
            // Copy the webapp folder
            File fileSource = new File( AppPathService.getWebAppPath(  ) + PATH_DOWNLOADED + strPluginName + "/" +
                    strVersion + FOLDER_WEBAPP );
            File fileDest = new File( PluginManagerService.getInstance(  ).getDeployWebappPath( strPluginName ) );

            if ( fileDest.exists(  ) )
            {
                FileUtils.deleteDirectory( fileDest );
            }

            FileUtils.copyDirectory( fileSource, fileDest );

            // Copy the SQL folder
            fileSource = new File( AppPathService.getWebAppPath(  ) + PATH_DOWNLOADED + strPluginName + "/" +
                    strVersion + FOLDER_SQL );
            fileDest = new File( PluginManagerService.getInstance(  ).getDeploySqlPath( strPluginName ) );

            if ( fileDest.exists(  ) )
            {
                FileUtils.deleteDirectory( fileDest );
            }

            FileUtils.copyDirectory( fileSource, fileDest );
        }
        catch ( IOException ex )
        {
            AppLogService.error( "error deploying plugin : ", ex );
        }
    }

    /**
     * Checks if the plugin's catalog info is compliant with the current core version
     * @param ci CatalogInfos
     * @return true if OK, otherwise false
     */
    boolean isCompliantWithCurrentCore( CatalogInfos ci )
    {
        if ( _currentCoreVersion == null )
        {
            try
            {
                _currentCoreVersion = Version.parse( AppInfo.getVersion(  ) );
            }
            catch ( InvalidVersionException ex )
            {
                AppLogService.error( "Invalid core version ", ex );

                return false;
            }
        }

        try
        {
            // Checks that the current core version is higher than the min required
            Version requiredMinCoreVersion = Version.parse( ci.getCoreVersionMin(  ) );

            if ( _currentCoreVersion.compareTo( requiredMinCoreVersion ) < 0 )
            {
                return false;
            }

            // Checks that the current core version is lower than the max required
            if ( ci.getCoreVersionMax(  ) != null )
            {
                Version requiredMaxCoreVersion = Version.parse( ci.getCoreVersionMax(  ) );

                if ( _currentCoreVersion.compareTo( requiredMaxCoreVersion ) > 0 )
                {
                    return false;
                }
            }
        }
        catch ( InvalidVersionException ex )
        {
            AppLogService.error( "Invalid version : " + ci.getPluginName(  ), ex );

            return false;
        }

        return true;
    }

    /**
     * Returns the updater status : (no update available, regular updates
     * available or critical updates available.
     * @return The status
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * Check for updates and update the status
     * @param listPlugins The list of installed plugins
     */
    public void checkUpdate( Collection<Plugin> listPlugins )
    {
        _nStatus = STATUS_NO_UPDATE;

        List<UpdateInfos> listUpdatesInfos = getUpdateInfos( listPlugins );

        if ( listUpdatesInfos.size(  ) > 0 )
        {
            _nStatus = STATUS_REGULAR_UPDATE;

            for ( UpdateInfos ui : listUpdatesInfos )
            {
                if ( ui.isCriticalUpdate(  ) )
                {
                    _nStatus = STATUS_CRITICAL_UPDATE;

                    break;
                }
            }
        }
    }

    /**
     * Tells if a plugin is among of an installed plugin list
     * @param strPluginName The plugin name
     * @param listPlugins The installed plugins list
     * @return true if the plugin is in the list
     */
    private boolean isInstalled( String strPluginName, Collection<Plugin> listPlugins )
    {
        for ( Plugin plugin : listPlugins )
        {
            if ( strPluginName.equals( plugin.getName(  ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the release has been downloaded
     * @param strPluginName The plugin name
     * @param strVersion The release version
     * @return true if the release has been downloaded, otherwise false
     */
    private boolean checkDownloaded( String strPluginName, String strVersion )
    {
        String strReleasesDirectory = AppPathService.getWebAppPath(  ) + PATH_DOWNLOADED + strPluginName + "/" +
            strVersion;
        File fReleasesDirectory = new File( strReleasesDirectory );

        return fReleasesDirectory.exists(  );
    }

    /**
     * Download a plugin release
     * @param strPluginName The plugin name
     * @param  strVersion The version
     * @throws UpdaterDownloadException If an exception occurs during download
     */
    public void downloadPlugin( String strPluginName, String strVersion )
        throws UpdaterDownloadException
    {
        CatalogInfos ci = getCatalogInfos( strPluginName );

        if ( ci != null )
        {
            downloadPackage( strPluginName, strVersion, ci.getDownloadUrl(  ) );
        }
    }

    /**
     * Download a plugin release
     * @param strPluginName The plugin name
     * @param  strVersion The version
     * @param  strVersionFrom The version from which to upgrade
     * @throws UpdaterDownloadException If an exception occurs during download
    */
    public void downloadPluginUpgrade( String strPluginName, String strVersion, String strVersionFrom )
        throws UpdaterDownloadException
    {
        CatalogInfos ci = getCatalogInfos( strPluginName );

        if ( ci != null )
        {
            for ( UpgradeInfos ui : ci.getUpgrades(  ) )
            {
                if ( ui.getVersionFrom(  ).equals( strVersionFrom ) )
                {
                    downloadPackage( strPluginName, strVersion, ui.getDownloadUrl(  ) );
                }
            }
        }
    }

    /**
     * Download a package for a given plugin and a given version
     * @param strPluginName The plugin name
     * @param strVersion The version
     * @param strPackageFileUrl The package download url
     * @throws UpdaterDownloadException If an exception occurs during download
     */
    private void downloadPackage( String strPluginName, String strVersion, String strPackageFileUrl )
        throws UpdaterDownloadException
    {
        try
        {
            HttpAccess httpAccess = new HttpAccess(  );
            String strPluginDirectory = AppPathService.getWebAppPath(  ) + PATH_DOWNLOADED + strPluginName;
            File directory = new File( strPluginDirectory );

            if ( !directory.exists(  ) )
            {
                FileUtils.forceMkdir( directory );
            }

            String strPackageFile = strPluginDirectory + "/" + strPluginName + ".zip";
            httpAccess.downloadFile( strPackageFileUrl, strPackageFile );

            String strVersionDirectory = strPluginDirectory + "/" + strVersion;
            extractPackage( strPackageFile, strVersionDirectory );
        }
        catch ( HttpAccessException e )
        {
            AppLogService.error( "Error downloading file : " + e.getMessage(  ), e );
            throw new UpdaterDownloadException( "Error downloading file : " + e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error creating downloaded file : " + e.getMessage(  ), e );
            throw new UpdaterDownloadException( "Error creating downloaded file : " + e.getMessage(  ), e );
        }
    }

    /**
     * Extract a package
     * @param strZipFile The package zip file
     * @param strDirectory The target directory
     * @throws UpdaterDownloadException If an exception occurs during download
     */
    private void extractPackage( String strZipFile, String strDirectory )
        throws UpdaterDownloadException
    {
        try
        {
            File file = new File( strZipFile );
            ZipFile zipFile = new ZipFile( file );

            // Each zipped file is indentified by a zip entry :
            Enumeration zipEntries = zipFile.entries(  );

            while ( zipEntries.hasMoreElements(  ) )
            {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement(  );

                // Clean the name :
                String strEntryName = zipEntry.getName(  );

                // The unzipped file :
                File destFile = new File( strDirectory, strEntryName );

                // Create the parent directory structure if needed :
                destFile.getParentFile(  ).mkdirs(  );

                if ( !zipEntry.isDirectory(  ) )
                {
                    // InputStream from zipped data
                    InputStream inZipStream = null;

                    try
                    {
                        AppLogService.debug( "unzipping " + strEntryName + " to " + destFile.getName(  ) );
                        inZipStream = zipFile.getInputStream( zipEntry );

                        // OutputStream to the destination file
                        OutputStream outDestStream = new FileOutputStream( destFile );

                        // Helper method to copy data
                        copyStream( inZipStream, outDestStream );

                        inZipStream.close(  );
                        outDestStream.close(  );
                    }
                    catch ( IOException e )
                    {
                        AppLogService.error( "Error extracting file : " + e.getMessage(  ), e );
                    }
                    finally
                    {
                        try
                        {
                            inZipStream.close(  );
                        }
                        catch ( IOException e )
                        {
                            AppLogService.error( "Error extracting file : " + e.getMessage(  ), e );
                        }
                    }
                }
                else
                {
                    AppLogService.debug( "skipping directory " + strEntryName );
                }
            }
        }
        catch ( ZipException e )
        {
            AppLogService.error( "Error extracting file : " + e.getMessage(  ), e );
            throw new UpdaterDownloadException( "Error extracting package ", e );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error extracting file : " + e.getMessage(  ), e );
        }
    }

    /**
     * Copies data from an input stream to an output stream.
     * @param inStream The input stream
     * @param outStream The output stream
     * @throws IOException If an I/O error occurs
     */
    private static void copyStream( InputStream inStream, OutputStream outStream )
        throws IOException
    {
        BufferedInputStream inBufferedStream = new BufferedInputStream( inStream );
        BufferedOutputStream outBufferedStream = new BufferedOutputStream( outStream );

        int nByte = 0;

        while ( ( nByte = inBufferedStream.read(  ) ) > -1 )
        {
            outBufferedStream.write( nByte );
        }

        outBufferedStream.close(  );
        inBufferedStream.close(  );
    }

    /**
     * Get catalog infos for a given plugin
     * @param strPluginName The plugin name
     * @return A CatalogInfos object
     */
    private CatalogInfos getCatalogInfos( String strPluginName )
    {
        ICatalogService catalogService = (ICatalogService) SpringContextService.getPluginBean( PLUGIN_NAME, BEAN_CATALOG_SERVICE );
        List<CatalogInfos> listCatalogInfos = catalogService.getCatalogInfos(  );

        for ( CatalogInfos ci : listCatalogInfos )
        {
            if ( ci.getPluginName(  ).equals( strPluginName ) )
            {
                return ci;
            }
        }

        return null;
    }
}
