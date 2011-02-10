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
package fr.paris.lutece.plugins.updater.service.catalog;

import java.util.ArrayList;
import java.util.List;


/**
 * CatalogInfos
 */
public class CatalogInfos
{
    // Variables declarations 
    private String _strPluginName;
    private String _strDescription;
    private String _strVersion;
    private String _strAuthor;
    private String _strDownloadUrl;
    private String _strHomepageUrl;
    private String _strCoreVersionMin;
    private String _strCoreVersionMax;
    private List<UpgradeInfos> _listUpgrades = new ArrayList<UpgradeInfos>(  );

    /**
     * Constructor
     */
    public CatalogInfos(  )
    {
    }

    /**
     * Returns the PluginName
     * @return The PluginName
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the PluginName
     * @param strPluginName The PluginName
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
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
     * Returns the Version
     * @return The Version
     */
    public String getVersion(  )
    {
        return _strVersion;
    }

    /**
     * Sets the Version
     * @param strVersion The Version
     */
    public void setVersion( String strVersion )
    {
        _strVersion = strVersion;
    }

    /**
     * Sets the DownloadUrl
     * @param strDownloadUrl The DownloadUrl
     */
    public void setDownloadUrl( String strDownloadUrl )
    {
        _strDownloadUrl = strDownloadUrl;
    }

    /**
     * Returns the DownloadUrl
     * @return The DownloadUrl
     */
    public String getDownloadUrl(  )
    {
        return _strDownloadUrl;
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
     * Returns the CoreVersionMin
     * @return The CoreVersionMin
     */
    public String getCoreVersionMin(  )
    {
        return _strCoreVersionMin;
    }

    /**
     * Sets the CoreVersionMin
     * @param strCoreVersionMin The CoreVersionMin
     */
    public void setCoreVersionMin( String strCoreVersionMin )
    {
        _strCoreVersionMin = strCoreVersionMin;
    }

    /**
     * Returns the CoreVersionMax
     * @return The CoreVersionMax
     */
    public String getCoreVersionMax(  )
    {
        return _strCoreVersionMax;
    }

    /**
     * Sets the CoreVersionMax
     * @param strCoreVersionMax The CoreVersionMax
     */
    public void setCoreVersionMax( String strCoreVersionMax )
    {
        _strCoreVersionMax = strCoreVersionMax;
    }

    /**
     * Add upgrade
     * @param ui The upgrade
     */
    public void addUpgrade( UpgradeInfos ui )
    {
        _listUpgrades.add( ui );
    }

    /**
     * Returns available upgrades
     * @return An upgrades list
     */
    public List<UpgradeInfos> getUpgrades(  )
    {
        return _listUpgrades;
    }
}
