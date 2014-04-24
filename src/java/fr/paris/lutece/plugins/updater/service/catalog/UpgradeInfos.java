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
package fr.paris.lutece.plugins.updater.service.catalog;


/**
 * UpgradeInfos
 */
public class UpgradeInfos
{
    private String _strVersionFrom;
    private String _strDownloadUrl;
    private String _strReleaseNotesUrl;
    private boolean _bCriticalUpdate;

    /**
     * Sets the VersionFrom
     * @param strVersionFrom The VersionFrom
     */
    public void setVersionFrom( String strVersionFrom )
    {
        _strVersionFrom = strVersionFrom;
    }

    /**
     * Returns the VersionFrom
     * @return The VersionFrom
     */
    public String getVersionFrom(  )
    {
        return _strVersionFrom;
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
     * Sets the ReleaseNotesUrl
     * @param strReleaseNotesUrl The ReleaseNotesUrl
     */
    public void setReleaseNotesUrl( String strReleaseNotesUrl )
    {
        _strReleaseNotesUrl = strReleaseNotesUrl;
    }

    /**
     * Returns the ReleaseNotesUrl
     * @return The ReleaseNotesUrl
     */
    public String getReleaseNotesUrl(  )
    {
        return _strReleaseNotesUrl;
    }

    /**
    /**
     * Returns the CriticalUpdate
     *
     * @return The CriticalUpdate
     */
    public boolean getCriticalUpdate(  )
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
