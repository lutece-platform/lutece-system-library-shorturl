/*
 * Copyright (c) 2002-2026, Mairie de Paris
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
package fr.paris.lutece.plugins.shorturl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Tests of ShortUrlClientService against a local HTTP server standing for the shorturl plugin.
 */
public class ShortUrlClientServiceTest extends LuteceTestCase
{
    private static final String PATH_PORTAL = "/lutece/jsp/site/Portal.jsp";
    private static final String KEY = "a1B2c3";
    private static final String SHORT_URL = "http://localhost/lutece/servlet/plugins/shorturl/" + KEY;

    private HttpServer _server;
    private String _strBaseUrl;
    private String _strResponse;
    private int _nStatus;
    private String _strPath;
    private Map<String, String> _mapParams;

    /**
     * Starts the local server.
     *
     * @throws IOException
     *             if the server cannot bind
     */
    @BeforeEach
    public void startServer( ) throws IOException
    {
        _nStatus = 200;
        _server = HttpServer.create( new InetSocketAddress( "localhost", 0 ), 0 );
        _server.createContext( "/", this::handle );
        _server.start( );
        _strBaseUrl = "http://localhost:" + _server.getAddress( ).getPort( ) + "/lutece";
    }

    /**
     * Stops the local server.
     */
    @AfterEach
    public void stopServer( )
    {
        _server.stop( 0 );
    }

    /**
     * Records the request and answers the prepared response.
     *
     * @param exchange
     *            the exchange
     * @throws IOException
     *             if the exchange fails
     */
    private void handle( HttpExchange exchange ) throws IOException
    {
        _strPath = exchange.getRequestURI( ).getPath( );
        _mapParams = new HashMap<>( );
        String strBody = new String( exchange.getRequestBody( ).readAllBytes( ), StandardCharsets.UTF_8 );
        for ( String strPair : strBody.split( "&" ) )
        {
            String [ ] tab = strPair.split( "=", 2 );
            _mapParams.put( URLDecoder.decode( tab [0], StandardCharsets.UTF_8 ), tab.length > 1 ? URLDecoder.decode( tab [1], StandardCharsets.UTF_8 ) : "" );
        }
        byte [ ] response = _strResponse.getBytes( StandardCharsets.UTF_8 );
        exchange.getResponseHeaders( ).add( "Content-Type", "application/json" );
        exchange.sendResponseHeaders( _nStatus, response.length );
        try ( OutputStream out = exchange.getResponseBody( ) )
        {
            out.write( response );
        }
    }

    /**
     * Builds a client pointing at the local server.
     *
     * @return the client
     */
    private IShortUrlClientService client( )
    {
        IShortUrlClientService client = new ShortUrlClientService( );
        client.setServerBaseUrl( _strBaseUrl );
        return client;
    }

    /**
     * A creation posts the create action to the portal and reads the short url and its key.
     */
    @Test
    public void testCreateShortener( )
    {
        _strResponse = "{\"status\":\"OK\",\"result\":{\"url\":\"" + SHORT_URL + "\",\"key\":\"" + KEY + "\"}}";

        ShortUrlJsonResponse response = client( ).createShortener( "https://www.paris.fr/a?b=c&d=e", true, null );

        assertEquals( PATH_PORTAL, _strPath );
        assertEquals( "do_create_ws", _mapParams.get( "action" ) );
        assertEquals( "shorturl", _mapParams.get( "page" ) );
        assertEquals( "https://www.paris.fr/a?b=c&d=e", _mapParams.get( "url" ) );
        assertEquals( "true", _mapParams.get( "use_once" ) );
        assertNotNull( response );
        assertEquals( "OK", response.getStatus( ) );
        assertEquals( SHORT_URL, response.getResult( ).getUrl( ) );
        assertEquals( KEY, response.getResult( ).getKey( ) );
    }

    /**
     * A creation without use once posts use_once=false.
     */
    @Test
    public void testCreateShortenerReusable( )
    {
        _strResponse = "{\"status\":\"OK\",\"result\":{\"url\":\"" + SHORT_URL + "\",\"key\":\"" + KEY + "\"}}";

        ShortUrlJsonResponse response = client( ).createShortener( "https://www.paris.fr", null );

        assertEquals( "false", _mapParams.get( "use_once" ) );
        assertEquals( KEY, response.getResult( ).getKey( ) );
    }

    /**
     * A base url ending with a slash still posts to the portal path.
     */
    @Test
    public void testCreateShortenerBaseUrlWithSlash( )
    {
        _strResponse = "{\"status\":\"OK\",\"result\":{\"url\":\"" + SHORT_URL + "\",\"key\":\"" + KEY + "\"}}";
        IShortUrlClientService client = new ShortUrlClientService( );
        client.setServerBaseUrl( _strBaseUrl + "/" );

        client.createShortener( "https://www.paris.fr", null );

        assertEquals( PATH_PORTAL, _strPath );
    }

    /**
     * A server error gives no response instead of an exception.
     */
    @Test
    public void testCreateShortenerServerError( )
    {
        _nStatus = 500;
        _strResponse = "error";

        assertNull( client( ).createShortener( "https://www.paris.fr", null ) );
    }

    /**
     * A deletion posts the delete action to the portal and reads the OK status.
     */
    @Test
    public void testDeleteShortener( )
    {
        _strResponse = "{\"status\":\"OK\",\"result\":\"\"}";

        assertTrue( client( ).deleteShortener( KEY, null ) );
        assertEquals( PATH_PORTAL, _strPath );
        assertEquals( "do_delete_ws", _mapParams.get( "action" ) );
        assertEquals( "shorturl", _mapParams.get( "page" ) );
        assertEquals( KEY, _mapParams.get( "key" ) );
    }

    /**
     * A deletion answered with an error status is reported as not done.
     */
    @Test
    public void testDeleteShortenerError( )
    {
        _strResponse = "{\"status\":\"ERROR\",\"errorCode\":\"1\"}";

        assertFalse( client( ).deleteShortener( KEY, null ) );
    }
}
