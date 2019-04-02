package fr.paris.lutece.plugins.shorturl.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortUrlResponse
{
    @JsonProperty( "url" )
    private String _strUrl;
    @JsonProperty( "key" )
    private String _strKey;

    @JsonProperty( "url" )
    public String getUrl( )
    {
        return _strUrl;
    }

    @JsonProperty( "url" )
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    @JsonProperty( "key" )
    public String getKey( )
    {
        return _strKey;
    }

    @JsonProperty( "key" )
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    public ShortUrlResponse( String strUrl, String strKey )
    {
        _strUrl = strUrl;
        _strKey = strKey;
    }
    
    public ShortUrlResponse( )
    {
    }

}
