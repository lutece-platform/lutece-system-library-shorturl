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
