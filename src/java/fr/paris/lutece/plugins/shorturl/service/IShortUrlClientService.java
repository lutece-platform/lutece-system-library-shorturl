package fr.paris.lutece.plugins.shorturl.service;

public interface IShortUrlClientService
{
    ShortUrlJsonResponse createShortener( String strUrl );

    ShortUrlJsonResponse createShortener( String strUrl, boolean bUseOnce );

    boolean deleteShortener( String strKey );
    
    void setServerUrl( String _strServerUrl );
}
