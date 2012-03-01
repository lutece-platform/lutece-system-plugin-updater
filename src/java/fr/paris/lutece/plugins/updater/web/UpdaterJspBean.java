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
package fr.paris.lutece.plugins.updater.web;

import fr.paris.lutece.plugins.updater.business.resource.FileSystemResource;
import fr.paris.lutece.plugins.updater.service.IUpdateService;
import fr.paris.lutece.plugins.updater.service.NewInfos;
import fr.paris.lutece.plugins.updater.service.PluginManagerService;
import fr.paris.lutece.plugins.updater.service.UpdateInfos;
import fr.paris.lutece.plugins.updater.service.UpdaterDownloadException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Updater JSP Bean
 */
public class UpdaterJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_UPDATES_MANAGEMENT = "UPDATER_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_MANAGE_UPDATES = "admin/plugins/updater/manage_updates.html";
    private static final String TEMPLATE_MANAGE_NEW = "admin/plugins/updater/manage_new.html";
    private static final String TEMPLATE_MANAGE_INSTALLED = "admin/plugins/updater/manage_installed.html";

    // Bookmarks
    private static final String MARK_PLUGINS_LIST = "plugins_list";

    // Parameters
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PARAMETER_VERSION = "version";
    private static final String PARAMETER_VERSION_FROM = "version_from";

    // URL
    private static final String JSP_URL_BACKUP_PLUGIN = "jsp/admin/plugins/updater/DoBackupPlugin.jsp";
    private static final String JSP_URL_RESTORE_PLUGIN = "jsp/admin/plugins/updater/DoRestorePlugin.jsp";
    private static final String JSP_URL_REMOVE_PLUGIN = "jsp/admin/plugins/updater/DoRemovePlugin.jsp";
    private static final String JSP_URL_DEPLOY_PLUGIN = "jsp/admin/plugins/updater/DoDeployPlugin.jsp";
    private static final String JSP_URL_CANCEL_INSTALL = "jsp/admin/plugins/updater/DoCancelInstall.jsp";
    private static final String JSP_URL_MANAGE_UPDATES = "jsp/admin/plugins/updater/ManageUpdates.jsp";
    private static final String JSP_URL_MANAGE_NEW = "jsp/admin/plugins/updater/ManageNew.jsp";
    private static final String URL_MANAGE_UPDATES = "ManageUpdates.jsp";
    private static final String URL_MANAGE_NEW = "ManageNew.jsp";
    private static final String URL_MANAGE_INSTALLED = "ManageInstalled.jsp";

    // Messages
    private static final String MESSAGE_CONFIRM_BACKUP_PLUGIN = "updater.message.confirmBackup";
    private static final String MESSAGE_CONFIRM_RESTORE_PLUGIN = "updater.message.confirmRestore";
    private static final String MESSAGE_CONFIRM_REMOVE_PLUGIN = "updater.message.confirmRemove";
    private static final String MESSAGE_CONFIRM_DEPLOY_PLUGIN = "updater.message.confirmDeploy";
    private static final String MESSAGE_CONFIRM_CANCEL_INSTALL = "updater.message.confirmCancelInstall";
    private static final String MESSAGE_DOWNLOAD_ERROR = "updater.message.downloadError";
    private static final String PLUGIN_NAME = "updater";
    private static final String BEAN_UPDATE_SERVICE = "updater.updateService";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_UPDATES = "updater.pageTitle.manageUpdates";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_NEW = "updater.pageTitle.manageNew";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_INSTALLED = "updater.pageTitle.manageInstalled";
    private static IUpdateService _updateService = (IUpdateService) SpringContextService.getPluginBean( PLUGIN_NAME,
            BEAN_UPDATE_SERVICE );
    private String _strManageUrl;

    /**
     * Provides the manage updates page
     * @param request The HTTP request
     * @return The page
     */
    public String getManageUpdates( HttpServletRequest request )
    {
        setManageUrl( URL_MANAGE_UPDATES );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_UPDATES );

        // Initialize PluginManagerService Webapp Path
        PluginManagerService.getInstance(  ).setWebAppPath( AppPathService.getWebAppPath(  ) );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );

        List<UpdateInfos> listUpdates = _updateService.getUpdateInfos( listPlugins );

        HashMap model = new HashMap(  );
        model.put( MARK_PLUGINS_LIST, listUpdates );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_UPDATES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Provides the manage new plugins page
     * @param request The HTTP request
     * @return The page
     */
    public String getManageNew( HttpServletRequest request )
    {
        setManageUrl( URL_MANAGE_NEW );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_NEW );

        // Initialize PluginManagerService Webapp Path
        PluginManagerService.getInstance(  ).setWebAppPath( AppPathService.getWebAppPath(  ) );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );

        List<NewInfos> listNewPlugins = _updateService.getNewPluginsInfos( listPlugins );

        HashMap model = new HashMap(  );
        model.put( MARK_PLUGINS_LIST, listNewPlugins );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_NEW, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Provides the manage installed plugins page
     * @param request The HTTP request
     * @return The page
     */
    public String getManageInstalled( HttpServletRequest request )
    {
        setManageUrl( URL_MANAGE_INSTALLED );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_INSTALLED );

        // Initialize PluginManagerService Webapp Path
        PluginManagerService.getInstance(  ).setWebAppPath( AppPathService.getWebAppPath(  ) );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );

        HashMap model = new HashMap(  );
        model.put( MARK_PLUGINS_LIST, listPlugins );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_INSTALLED, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Confirm the plugin's backup
     * @param request The HTTP request
     * @return The forward URL
     */
    public String backupPlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        UrlItem url = new UrlItem( JSP_URL_BACKUP_PLUGIN );
        url.addParameter( PARAMETER_PLUGIN_NAME, strPluginName );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_BACKUP_PLUGIN, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process the plugin's backup
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doBackupPlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        PluginManagerService.getInstance(  ).backupPlugin( strPluginName );

        return getManageUrl(  );
    }

    /**
     * Confirm the plugin removal
     * @param request The HTTP request
     * @return The forward URL
     */
    public String removePlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        UrlItem url = new UrlItem( JSP_URL_REMOVE_PLUGIN );
        url.addParameter( PARAMETER_PLUGIN_NAME, strPluginName );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PLUGIN, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process the plugin removal
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doRemovePlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        PluginManagerService.getInstance(  ).removePlugin( strPluginName, FileSystemResource.DELETE_NOW );

        return getManageUrl(  );
    }

    /**
     * Confirm the plugin's restore
     * @param request The HTTP request
     * @return The forward URL
     */
    public String restorePlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        UrlItem url = new UrlItem( JSP_URL_RESTORE_PLUGIN );
        url.addParameter( PARAMETER_PLUGIN_NAME, strPluginName );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_RESTORE_PLUGIN, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process the plugin's restore
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doRestorePlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        PluginManagerService.getInstance(  ).restorePlugin( strPluginName );

        return getManageUrl(  );
    }

    /**
     * Confirm the plugin's deployment
     * @param request The HTTP request
     * @return The forward URL
     */
    public String deployPlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
        String strVersion = request.getParameter( PARAMETER_VERSION );

        UrlItem url = new UrlItem( JSP_URL_DEPLOY_PLUGIN );
        url.addParameter( PARAMETER_PLUGIN_NAME, strPluginName );
        url.addParameter( PARAMETER_VERSION, strVersion );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DEPLOY_PLUGIN, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process the plugin's deployment
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doDeployPlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
        String strVersion = request.getParameter( PARAMETER_VERSION );
        _updateService.deployPlugin( strPluginName, strVersion );

        return getManageUrl(  );
    }

    /**
     * Confirm the installation cancelling
     * @param request The HTTP request
     * @return The forward URL
     */
    public String cancelInstall( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        UrlItem url = new UrlItem( JSP_URL_CANCEL_INSTALL );
        url.addParameter( PARAMETER_PLUGIN_NAME, strPluginName );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_INSTALL, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process the installation cancelling
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doCancelInstall( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        PluginManagerService.getInstance(  ).cancelInstallInProgress( strPluginName );

        return getManageUrl(  );
    }

    /**
     * Process the download
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doDownload( HttpServletRequest request )
    {
        try
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            String strVersion = request.getParameter( PARAMETER_VERSION );
            _updateService.downloadPlugin( strPluginName, strVersion );
            PluginManagerService.getInstance(  ).cancelInstallInProgress( strPluginName );

            return getManageUrl(  );
        }
        catch ( UpdaterDownloadException e )
        {
            Object[] args = { e.getCause(  ).getMessage(  ), };

            String strForwardUrl = getManageUrl(  ).equals( URL_MANAGE_UPDATES ) ? JSP_URL_MANAGE_UPDATES
                                                                                 : JSP_URL_MANAGE_NEW;

            return AdminMessageService.getMessageUrl( request, MESSAGE_DOWNLOAD_ERROR, args, strForwardUrl,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Process the download
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doDownloadUpgrade( HttpServletRequest request )
    {
        try
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            String strVersion = request.getParameter( PARAMETER_VERSION );
            String strVersionFrom = request.getParameter( PARAMETER_VERSION_FROM );
            _updateService.downloadPluginUpgrade( strPluginName, strVersion, strVersionFrom );
            PluginManagerService.getInstance(  ).cancelInstallInProgress( strPluginName );

            return getManageUrl(  );
        }
        catch ( UpdaterDownloadException e )
        {
            Object[] args = { e.getCause(  ).getMessage(  ), };

            return AdminMessageService.getMessageUrl( request, MESSAGE_DOWNLOAD_ERROR, args, JSP_URL_MANAGE_UPDATES,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Sets the ManageUrl
     * @param strManageUrl The ManageUrl
     */
    private void setManageUrl( String strManageUrl )
    {
        _strManageUrl = strManageUrl;
    }

    /**
     * Returns the ManageUrl
     * @return The ManageUrl
     */
    private String getManageUrl(  )
    {
        return _strManageUrl;
    }
}
