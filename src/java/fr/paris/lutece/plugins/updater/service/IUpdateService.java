/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;
import java.util.List;


/**
 * Update Service Interface
 */
public interface IUpdateService
{
    int STATUS_NO_UPDATE = 0;
    int STATUS_REGULAR_UPDATE = 1;
    int STATUS_CRITICAL_UPDATE = 2;

    /**
     * Download the plugin package
     * @param strPluginName The plugin name
     * @param strVersion The plugin version
     * @throws UpdaterDownloadException If an exception occurs during download
     */
    void downloadPlugin( String strPluginName, String strVersion )
        throws UpdaterDownloadException;

    /**
     * Download the plugin package
     * @param strPluginName The plugin name
     * @param strVersion The new plugin version
     * @param strVersionFrom The current plugin version
     * @throws UpdaterDownloadException If an exception occurs during download
     */
    void downloadPluginUpgrade( String strPluginName, String strVersion, String strVersionFrom )
        throws UpdaterDownloadException;

    /**
     * Gets available update for a list of plugins
     * @param listPlugins The list of plugins
     * @return A list of updates available for the given list of plugins
     */
    List<UpdateInfos> getUpdateInfos( Collection<Plugin> listPlugins );

    /**
     * Gets available plugins not already installed
     * @param listPlugins The list of installed plugins
     * @return New plugins infos list
     */
    List<NewInfos> getNewPluginsInfos( Collection<Plugin> listPlugins );

    /**
    * Returns the updater status : (no update available, regular updates
    * available or critical updates available.
    * @return The status
    */
    int getStatus(  );

    /**
     * Check for updates and update the status
     * @param listPlugins The list of installed plugins
     */
    void checkUpdate( Collection<Plugin> listPlugins );

    /**
     * Deploy a plugin (downloaded => deploy)
     * @param strPluginName The plugin name
     * @param strVersion The update version
     */
    void deployPlugin( String strPluginName, String strVersion );
}
