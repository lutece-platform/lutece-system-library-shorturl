package fr.paris.lutece.plugins.shorturl.service;

import javax.servlet.http.HttpServletRequest;

public interface IShortUrlClientService
{
    ShortUrlJsonResponse createShortener( String strUrl,HttpServletRequest request);

    ShortUrlJsonResponse createShortener( String strUrl, boolean bUseOnce,HttpServletRequest request );

    boolean deleteShortener( String strKey,HttpServletRequest request );
    
    void setServerBaseUrl( String _strServerUrl );
}
