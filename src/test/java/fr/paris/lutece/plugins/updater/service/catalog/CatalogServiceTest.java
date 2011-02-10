/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.updater.service.catalog;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


/**
 * CatalogService Test
 */
public class CatalogServiceTest extends LuteceTestCase
{
    public CatalogServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * Test of getCatalogInfos method, of class CatalogService.
     */
    public void testGetCatalogInfos(  )
    {
        System.out.println( "getCatalogInfos" );

        CatalogService instance = new CatalogService(  );
        List<CatalogInfos> list = instance.getCatalogInfos(  );

        for ( CatalogInfos ci : list )
        {
            System.out.println( ci.getPluginName(  ) + " " + ci.getVersion(  ) + " " + ci.getDescription(  ) );
        }

        assertTrue( list.size(  ) > 0 );
    }
}
