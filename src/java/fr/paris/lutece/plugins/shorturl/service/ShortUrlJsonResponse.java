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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class ShortUrlJsonResponse implements Serializable
{
    /**
     * ShortUrlJsonResponse
     * 
     */
    private static final String STATUS_OK = "OK";
    @JsonProperty( "result" )
    private ShortUrlResponse _result;
    @JsonProperty( "status" )
    private String _strStatus;

    /**
     * JsonResponse constructor
     * 
     * @param result
     *            the result object
     */
    public ShortUrlJsonResponse( String strUrl, String strKey )
    {
        _strStatus = STATUS_OK;
        _result = new ShortUrlResponse( strUrl, strKey );
    }

    public ShortUrlJsonResponse( )
    {
    }

    /**
     *
     * @return the result object
     */
    @JsonProperty( "result" )
    public ShortUrlResponse getResult( )
    {
        return _result;
    }

    /**
     *
     * @param shortUrlResponse
     * @return the result object
     */
    @JsonProperty( "result" )
    public void setResult( ShortUrlResponse shortUrlResponse )
    {
        _result = shortUrlResponse;
    }

    /**
     *
     * @return the response status
     */
    @JsonProperty( "status" )
    public String getStatus( )
    {
        return _strStatus;
    }

    /**
     *
     * @return the response status
     */
    @JsonProperty( "status" )
    public void setStatus( String strStatus )
    {
        _strStatus = strStatus;
    }

}
