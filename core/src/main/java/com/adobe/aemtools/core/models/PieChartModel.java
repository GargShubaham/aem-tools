package com.adobe.aemtools.core.models;

import com.adobe.aemtools.core.Utils.CommonUtil;
import com.adobe.aemtools.core.constants.Constants;
import com.adobe.aemtools.core.services.ComponentUsageReportService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables =  { Resource.class, SlingHttpServletRequest.class })
public class PieChartModel {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    SlingHttpServletRequest request;

    @OSGiService
    private ComponentUsageReportService componentUsageReportService;

    String[] paths = null;
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
        catch(Exception e){
            logger.error("Exception in init method.",e);
        }
    }

    public String getPieChartJson() {
        JsonArray array = new JsonArray();
        try {


            String reportPath = request.getRequestPathInfo().getSuffix();
            if (reportPath != null) {
                ValueMap map = resourceResolver.resolve(reportPath).getValueMap();
                String[] component =   map.get(Constants.REPORT_COMPONENTS,String[].class);
                for(String path : component){
                    JsonObject json = new JsonObject();
                    json.addProperty(Constants.PIE_CHART_NAME, CommonUtil.getComponentTitle(path,resourceResolver));
                    json.addProperty(Constants.PIE_CHART_Y,componentUsageReportService.getComponentUsageNumber(path,resourceResolver,paths,excludeChildrenPages));
                    array.add(json);
                }
            }

        }
        catch(Exception e){
            logger.error("Exception in PieChartJson method.",e);
        }
        return array.toString();
    }
}
