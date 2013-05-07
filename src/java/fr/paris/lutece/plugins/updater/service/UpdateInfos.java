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


/**
 * Update processing datas
 */
public class UpdateInfos
{
    private String _strPluginName;
    private String _strCurrentVersion;
    private String _strTargetVersion;
    private boolean _bRestorable;
    private boolean _bInstallInProgress;
    private boolean _bDownloaded;
    private boolean _bCriticalUpdate;

    /**
     * Constructor
     * @param pluginName The plugin name
     */
    public UpdateInfos( String pluginName )
    {
        _strPluginName = pluginName;
    }

    /**
     * Gets the plugin name
     *
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Returns the CurrentVersion
     *
     * @return The CurrentVersion
     */
    public String getCurrentVersion(  )
    {
        return _strCurrentVersion;
    }

    /**
     * Sets the CurrentVersion
     *
     * @param strCurrentVersion The CurrentVersion
     */
    public void setCurrentVersion( String strCurrentVersion )
    {
        _strCurrentVersion = strCurrentVersion;
    }

    /**
     * Returns the TargetVersion
     *
     * @return The TargetVersion
     */
    public String getTargetVersion(  )
    {
        return _strTargetVersion;
    }

    /**
     * Sets the TargetVersion
     *
     * @param strTargetVersion The TargetVersion
     */
    public void setTargetVersion( String strTargetVersion )
    {
        _strTargetVersion = strTargetVersion;
    }

    /**
     * Is there a backup available
     * @return true if the plugin can be restored otherwise false
     */
    public boolean isRestorable(  )
    {
        return _bRestorable;
    }

    /**
     * Sets the restorable flag
     * @param bRestorable true if the plugin can be restored otherwise false
     */
    public void setRestorable( boolean bRestorable )
    {
        _bRestorable = bRestorable;
    }

    /**
     * Tells wether or not the release has been downloaded
     * @return true if downloaded, otherwise false
     */
    public boolean isDownloaded(  )
    {
        return _bDownloaded;
    }

    /**
     * Sets the downloaded flag
     * @param bDownloaded The downloaded flag : true if downloaded, otherwise false
     */
    public void setDownloaded( boolean bDownloaded )
    {
        _bDownloaded = bDownloaded;
    }

    /**
     * Tells if the install is in progress (process planed at shutdown and next startup)
     * @return true if an install is in progress, otherwise false
     */
    public boolean isInstallInProgress(  )
    {
        return _bInstallInProgress;
    }

    /**
     * Sets the InstallInProgress flag
     * @param bInstallInProgress The InstallInProgress flag
     */
    public void setInstallInProgress( boolean bInstallInProgress )
    {
        _bInstallInProgress = bInstallInProgress;
    }

    /**
     * Returns the CriticalUpdate
     *
     * @return The CriticalUpdate
     */
    public boolean isCriticalUpdate(  )
    {
        return _bCriticalUpdate;
    }

    /**
     * Sets the CriticalUpdate
     *
     * @param bCriticalUpdate The CriticalUpdate
     */
    public void setCriticalUpdate( boolean bCriticalUpdate )
    {
        _bCriticalUpdate = bCriticalUpdate;
    }
}
