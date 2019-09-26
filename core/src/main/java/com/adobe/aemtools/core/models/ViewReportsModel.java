package com.adobe.aemtools.core.models;

import com.adobe.aemtools.core.Utils.CommonUtil;
import com.adobe.aemtools.core.constants.Constants;
import com.adobe.aemtools.core.services.ComponentUsageReportService;
import com.adobe.cq.social.journal.client.api.Journal;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables =  { Resource.class, SlingHttpServletRequest.class })
public class ViewReportsModel {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject @Optional
    private String componentpath;

    @Inject @Optional
    private String nodePath;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    SlingHttpServletRequest request;

    @OSGiService
    private ComponentUsageReportService componentUsageReportService;

    String[] paths = null;
    String   pagePath = null;
    boolean excludeChildrenPages=false;
    @PostConstruct
    protected void init() {
         try {
             String reportPath = request.getRequestPathInfo().getSuffix();
             if (reportPath != null) {
                 ValueMap map = resourceResolver.resolve(reportPath).getValueMap();
                 paths = map.get(Constants.REPORT_PATHS, String[].class);
                 excludeChildrenPages=map.get(Constants.CREATIONWIZARD_EXCLUDECHIDLRENPAGE,Boolean.class) != null ? map.get(Constants.CREATIONWIZARD_EXCLUDECHIDLRENPAGE,Boolean.class) : false;

             }
         }
         catch (Exception e){
             logger.error("Exception in init method.",e);
         }

    }

    public String getReportview() {

        return componentUsageReportService.getReportView();
    }

    public List<HashMap<String,String>> getList() {
        List<HashMap<String,String>> list = new ArrayList<>();
        try {
            String reportPath = request.getRequestPathInfo().getSuffix();
            if (reportPath != null) {
                ValueMap map = resourceResolver.resolve(reportPath).getValueMap();
                String[] component =   map.get(Constants.REPORT_COMPONENTS,String[].class);
                for(String path : component){
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put(Constants.COMPONENT_PATH,path);
                    hashMap.put(Constants.COMPONENT_NAME, CommonUtil.getComponentTitle(path,resourceResolver));
                    hashMap.put(Constants.COMPONENT_USAGE,Integer.toString(componentUsageReportService.getComponentUsageNumber(path,resourceResolver,paths,excludeChildrenPages)));
                    list.add(hashMap);

                }
            }
        }
        catch (Exception e){
            logger.error("Exception in getList method.",e);
        }
        return  list;
    }

    public Iterator<Resource> getResources() {

        return componentUsageReportService.getNodesComponentUsedAt(componentpath,resourceResolver,paths,excludeChildrenPages);
    }

    public Map<String,String> getPageMap() {
         Map<String, String> myHashMap = new HashMap<>();
         try{
             nodePath = nodePath.substring(0, nodePath.indexOf("/"+ JcrConstants.JCR_CONTENT));
             myHashMap.put(Constants.PAGE_NAME,getPageName(nodePath));
             nodePath = componentUsageReportService.openPageInEditMode() ? Constants.EDITOR_MODE_URL.concat(nodePath) :  nodePath;
             myHashMap.put(Constants.PAGE_PATH,nodePath.concat(Journal.URL_SUFFIX));
         }
         catch (Exception e){
             logger.error("Exception in getMyHashMap method.",e);
         }
        return myHashMap;
    }
    private String getPageName(String pagePath) {
        String pagename=null;
        try{
         Page page = resourceResolver.resolve(pagePath).adaptTo(Page.class);
         if(page != null) {
             return page.getTitle();
         }
        }
        catch (Exception e){
            logger.error("Exception in getPageName method.",e);
        }
        return pagename;
    }

}
