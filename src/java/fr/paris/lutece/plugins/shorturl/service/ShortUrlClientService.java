package fr.paris.lutece.plugins.shorturl.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.json.JsonResponse;

public class ShortUrlClientService implements IShortUrlClientService
{
    private static ObjectMapper _mapper;
    private String _strServerUrl=null;
    
    private static final String STATUS_OK = "OK";
    private static final String ACTION_DO_CREATE_WS = "do_create_ws";
    private static final String ACTION_DO_DELETE_WS = "do_delete_ws";

    private static final String PARAMETER_URL = "url";
    private static final String PARAMETER_KEY = "key";
    private static final String PARAMETER_USE_ONCE = "use_once";
    private static final String PARAMETER_ACTION= "action";
    
    
    static
    {
        _mapper = new ObjectMapper( );
        _mapper.enable( DeserializationFeature.UNWRAP_ROOT_VALUE );
        _mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        _mapper.enable( SerializationFeature.WRAP_ROOT_VALUE );
    }
     
    public  ShortUrlJsonResponse createShortener(  String strUrl)
    {
        return createShortener( strUrl,false);
    }

    
    public  ShortUrlJsonResponse createShortener(String strUrl ,boolean bUseOnce)
    {

        ShortUrlJsonResponse shortUrlJsonResponse=null;
        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapParams = new HashMap<String, String>( );
        mapParams.put( PARAMETER_ACTION, ACTION_DO_CREATE_WS );
        mapParams.put( PARAMETER_URL, strUrl );
        mapParams.put( PARAMETER_USE_ONCE, Boolean.toString( bUseOnce ) );
        
        String strOutput = StringUtils.EMPTY;

        try
        {
            strOutput = clientHttp.doPost( _strServerUrl, mapParams, null, null, null, null );
            shortUrlJsonResponse = _mapper.readValue( strOutput, ShortUrlJsonResponse.class );
        }
        catch( Exception e )
        {   
            AppLogService.error( "error calling shortener Url server",e );
           
        }

        return  shortUrlJsonResponse;
        
    }


    public   boolean deleteShortener( String strKey )
    {
    
        HttpAccess clientHttp = new HttpAccess( );
        Map<String, String> mapParams = new HashMap<String, String>( );
        mapParams.put( PARAMETER_ACTION, ACTION_DO_DELETE_WS );
        mapParams.put( PARAMETER_KEY, strKey );
        JsonResponse deleteResponse=null;
        
        String strOutput = StringUtils.EMPTY;

        try
        {
            strOutput = clientHttp.doPost( _strServerUrl, mapParams, null, null, null, null );
            deleteResponse = _mapper.readValue( strOutput, JsonResponse.class );
        }
        catch( Exception e )
        {   
            AppLogService.error( "error calling shortener Url server",e );
           
        }
       
        return   deleteResponse!=null && deleteResponse.getStatus( ).equals( STATUS_OK );


    }



    public void setServerUrl( String _strServerUrl )
    {
        this._strServerUrl = _strServerUrl;
    }

 

}
