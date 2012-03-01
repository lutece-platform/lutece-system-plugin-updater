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


/**
 * New plugins processing datas
 */
public class NewInfos
{
    private String _strPluginName;
    private String _strVersion;
    private String _strDescription;
    private String _strAuthor;
    private String _strHomepageUrl;
    private boolean _bInstallInProgress;
    private boolean _bDownloaded;

    /**
     * Constructor
     * @param pluginName The plugin name
     */
    public NewInfos( String pluginName )
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
     * Returns the Version
     *
     * @return The Version
     */
    public String getVersion(  )
    {
        return _strVersion;
    }

    /**
     * Sets the Version
     *
     * @param strVersion The Version
     */
    public void setVersion( String strVersion )
    {
        _strVersion = strVersion;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Sets the HomepageUrl
     * @param strHomepageUrl The HomepageUrl
     */
    public void setHomepageUrl( String strHomepageUrl )
    {
        _strHomepageUrl = strHomepageUrl;
    }

    /**
     * Returns the HomepageUrl
     * @return The HomepageUrl
     */
    public String getHomepageUrl(  )
    {
        return _strHomepageUrl;
    }

    /**
     * Sets the Author
     * @param strAuthor The Author
     */
    public void setAuthor( String strAuthor )
    {
        _strAuthor = strAuthor;
    }

    /**
     * Returns the Author
     * @return The Author
     */
    public String getAuthor(  )
    {
        return _strAuthor;
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
}
