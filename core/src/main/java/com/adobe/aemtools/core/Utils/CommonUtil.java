package com.adobe.aemtools.core.Utils;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommonUtil {
    private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil() {

    }

    public static  String getComponentTitle(String path, ResourceResolver resourceResolver){
       String title = null;
       try{
           Resource resource = resourceResolver.getResource(path);
           if(resource != null) {
               title = resource.getValueMap().get(JcrConstants.JCR_TITLE, String.class);
               return title != null ? title : getStringAfterLastSlash(path);
           }
       }
       catch(Exception e){
           logger.error("Exception in getComponentTitle method");
       }
        return getStringAfterLastSlash(path);
    }

    public  static String getStringAfterLastSlash (String path){
        return path != null ? path.substring(path.lastIndexOf('/') + 1) : path;
    }
    public static  String[] getStringArray(List<String> list)
    {
        String[] str =null;
        try{
            Object[] objArr = list.toArray();
            str = Arrays
                    .copyOf(objArr, objArr
                                    .length,
                            String[].class);
            return str;
        } catch (Exception e) {

            logger.error("Exception in getCurrentDate method.",e);
        }
        return  str;
    }
    public static Calendar getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        try{
            Date date = new Date();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {

            logger.error("Exception in getCurrentDate method.",e);
        }
        return cal;
    }
    public static  List < String > populateParameterList(final RequestParameterMap params, final String parameterName, List < String > parameterValues) {
        try{
            if (parameterValues == null) {
                parameterValues = new ArrayList<>();
            }
            if (params.containsKey((Object) parameterName)) {
                final RequestParameter[] values =params.getValues(parameterName);

                for (final RequestParameter cond: values) {
                    if (StringUtils.isNotBlank(cond.getString())) {
                        parameterValues.add(cond.getString());
                    }
                }
            }
            return parameterValues;
        } catch (Exception e) {

            logger.error("Exception in populateParameterList method.",e);
        }
        return parameterValues;
    }

    public static boolean excludeChildrenPages(final RequestParameterMap params,String parameterName){
        try {
            if (params.containsKey((Object)parameterName )) {
                return   Boolean.valueOf(params.getValue(parameterName).getString());
            }

        }
        catch (Exception e){
            logger.error("Exception in excludeChildrenPages method.",e);
        }
        return false;
    }
}
