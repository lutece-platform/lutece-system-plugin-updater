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
package fr.paris.lutece.plugins.updater.service.catalog;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;


/**
 * CatalogService
 */
public class CatalogService implements ICatalogService
{
    private static final String FILE_CATALOGS_LIST_RULES = "/fr/paris/lutece/plugins/updater/service/catalog/catalogs-list-digester-rules.xml";
    private static final String FILE_CATALOG_RULES = "/fr/paris/lutece/plugins/updater/service/catalog/catalog-digester-rules.xml";
    private static final String FILE_CATALOGS_LIST = "/WEB-INF/plugins/updater/catalogs.xml";
    private static final String EXCEPTION_MESSAGE = "Error loading catalog : ";
    private List<CatalogInfos> _listInfos = new ArrayList<CatalogInfos>(  );
    private List<Catalog> _listCatalogs = new ArrayList<Catalog>(  );

    /**
     * Retrieve all plugin releases from a repository
     * @return A CatalogInfos list
     */
    public List<CatalogInfos> getCatalogInfos(  )
    {
        // clear catalogs data
        _listCatalogs.clear(  );
        _listInfos.clear(  );

        try
        {
            // load catalogs list
            File fileCatalogs = new File( AppPathService.getWebAppPath(  ) + FILE_CATALOGS_LIST );
            FileReader readerFile = new FileReader( fileCatalogs );
            loadCatalogsList( readerFile );

            HttpAccess httpAccess = new HttpAccess(  );

            for ( Catalog catalog : _listCatalogs )
            {
                String strXmlCatalog = httpAccess.doGet( catalog.getUrl(  ) );

                Reader reader = new StringReader( strXmlCatalog );
                load( reader );
            }
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
        catch ( HttpAccessException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e.getCause(  ) );
        }

        return _listInfos;
    }

    /**
     * Add an entry to the catalog
     * @param ci A CatalogInfos entry
     */
    public void addEntry( CatalogInfos ci )
    {
        _listInfos.add( ci );
    }

    /**
     * Add a catalog to the list
     * @param catalog A Catalog
     */
    public void addCatalog( Catalog catalog )
    {
        _listCatalogs.add( catalog );
    }

    /**
     * Load a catalog list from a reader object
     * @param reader The reader
     */
    private void loadCatalogsList( Reader reader )
    {
        // Configure Digester from XML ruleset
        URL rules = getClass(  ).getResource( FILE_CATALOGS_LIST_RULES );

        Digester digester = DigesterLoader.createDigester( rules );

        // Push empty List onto Digester's Stack
        digester.push( this );

        try
        {
            digester.parse( reader );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
        catch ( SAXException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
    }

    /**
     * Load a catalog from a reader object
     * @param reader The reader
     */
    private void load( Reader reader )
    {
        // Configure Digester from XML ruleset
        URL rules = getClass(  ).getResource( FILE_CATALOG_RULES );

        Digester digester = DigesterLoader.createDigester( rules );

        // Push empty List onto Digester's Stack
        digester.push( this );

        try
        {
            digester.parse( reader );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
        catch ( SAXException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( EXCEPTION_MESSAGE + e.getMessage(  ), e );
        }
    }
}
