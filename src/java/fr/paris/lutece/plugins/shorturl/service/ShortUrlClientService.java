/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.json.JsonResponse;

/**
 * 
 * ShortUrlClientService
 *
 */
public class ShortUrlClientService implements IShortUrlClientService
{
    private static ObjectMapper _mapper;
    private String _strShortServerUrl = null;

    private static final String STATUS_OK = "OK";

    private static final String ACTION_DO_CREATE_WS = "do_create_ws";
    private static final String ACTION_DO_DELETE_WS = "do_delete_ws";
    private static final String SHORT_XPAGE = "shorturl";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_URL = "url";
    private static final String PARAMETER_KEY = "key";
    private static final String PARAMETER_USE_ONCE = "use_once";
    private static final String PARAMETER_ACTION = "action";

    static
    {
        _mapper = new ObjectMapper( );
        // _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        // _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortUrlJsonResponse createShortener( String strUrl, HttpServletRequest request )
    {
        return createShortener( strUrl, false, request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortUrlJsonResponse createShortener( String strUrl, boolean bUseOnce, HttpServletRequest request )
    {

        ShortUrlJsonResponse shortUrlJsonResponse = null;
        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapParams = new HashMap<String, String>( );
        mapParams.put( PARAMETER_ACTION, ACTION_DO_CREATE_WS );
        mapParams.put( PARAMETER_URL, strUrl );
        mapParams.put( PARAMETER_PAGE, SHORT_XPAGE );

        mapParams.put( PARAMETER_USE_ONCE, Boolean.toString( bUseOnce ) );

        String serverUrl = StringUtils.isEmpty( _strShortServerUrl ) ? AppPathService.getBaseUrl( request ) : _strShortServerUrl;

        String strOutput = StringUtils.EMPTY;

        try
        {
            strOutput = clientHttp.doPost( serverUrl + "/jsp/site/Portal.jsp", mapParams, null, null, null, null );
            shortUrlJsonResponse = _mapper.readValue( strOutput, ShortUrlJsonResponse.class );
        }
        catch( Exception e )
        {
            AppLogService.error( "error calling shortener Url server", e );

        }

        return shortUrlJsonResponse;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteShortener( String strKey, HttpServletRequest request )
    {

        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapParams = new HashMap<String, String>( );
        mapParams.put( PARAMETER_ACTION, ACTION_DO_DELETE_WS );
        mapParams.put( PARAMETER_KEY, strKey );
        mapParams.put( PARAMETER_PAGE, SHORT_XPAGE );

        JsonResponse deleteResponse = null;

        String strOutput = StringUtils.EMPTY;
        String serverUrl = StringUtils.isEmpty( _strShortServerUrl ) ? AppPathService.getBaseUrl( request ) : _strShortServerUrl;

        try
        {
            strOutput = clientHttp.doPost( serverUrl, mapParams, null, null, null, null );
            deleteResponse = _mapper.readValue( strOutput, JsonResponse.class );
        }
        catch( Exception e )
        {
            AppLogService.error( "error calling shortener Url server", e );

        }

        return deleteResponse != null && deleteResponse.getStatus( ).equals( STATUS_OK );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServerBaseUrl( String _strServerUrl )
    {
        this._strShortServerUrl = _strServerUrl;
    }

}
