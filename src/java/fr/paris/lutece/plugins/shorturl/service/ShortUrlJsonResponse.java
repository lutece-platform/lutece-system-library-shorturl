package fr.paris.lutece.plugins.shorturl.service;

import fr.paris.lutece.util.json.AbstractJsonResponse;

public class ShortUrlJsonResponse extends AbstractJsonResponse
{
    /**
     * ShortUrlJsonResponse
     * 
     */
    private static final long serialVersionUID = 5609068659762042379L;
    private ShortUrlResponse _result;

    /**
     * JsonResponse constructor
     * 
     * @param result
     *            the result object
     */
    public ShortUrlJsonResponse( String strUrl, String strKey )
    {
        super( STATUS_OK );
        _result = new ShortUrlResponse( strUrl, strKey );
    }

    /**
     *
     * @return the result object
     */
    public Object getResult( )
    {
        return _result;
    }
}

