package fr.paris.lutece.plugins.shorturl.service;

public class ShortUrlResponse 
{
    private String _strUrl;
    private String _strKey;
    
    public String getUrl( )
    {
        return _strUrl;
    }
    public void setUrl( String _strUrl )
    {
        this._strUrl = _strUrl;
    }
    public String getKey( )
    {
        return _strKey;
    }
    public void setKey( String _strKey )
    {
        this._strKey = _strKey;
    }
    public ShortUrlResponse( String strUrl, String strKey )
    {
        super( );
        this._strUrl = strUrl;
        this._strKey = strKey;
    }
    

}
