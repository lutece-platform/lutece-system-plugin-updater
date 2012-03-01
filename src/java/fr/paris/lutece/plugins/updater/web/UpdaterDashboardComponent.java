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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.updater.service.IUpdateService;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;


/**
 * Updater Dashboard Component : displays weither upgrades are available
 */
public class UpdaterDashboardComponent extends DashboardComponent
{
    private static final String TEMPLATE_DASHBOARD = "/admin/plugins/updater/updater_dashboard.html";
    private static final String MARK_URL = "url";
    private static final String MARK_STATUS = "status";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PLUGIN_NAME = "updater";
    private static final String BEAN_UPDATE_SERVICE = "updater.updateService";
    private static final IUpdateService _updateService = (IUpdateService) SpringContextService.getPluginBean( PLUGIN_NAME,
            BEAN_UPDATE_SERVICE );

    /**
     * The HTML code of the component
     * @param user The Admin User
     * @return The dashboard component
     */
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Right right = RightHome.findByPrimaryKey( getRight(  ) );
        UrlItem url = new UrlItem( right.getUrl(  ) );
        url.addParameter( PARAMETER_PLUGIN_NAME, right.getPluginName(  ) );

        int nStatus = _updateService.getStatus(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_URL, url.getUrl(  ) );
        model.put( MARK_STATUS, nStatus );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD, user.getLocale(  ), model );

        return t.getHtml(  );
    }
}
