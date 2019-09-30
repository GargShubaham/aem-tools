package com.adobe.aemtools.core.models;

import com.adobe.aemtools.core.services.ComponentUsageReportService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * The type Report lister model.
 */
@Model(adaptables =  { Resource.class, SlingHttpServletRequest.class })
public class ReportListerModel {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * The Component usage report service.
     */
    @OSGiService
    ComponentUsageReportService componentUsageReportService;

    /**
     * Gets all reports.
     *
     * @return the all reports
     */
    public Iterator<Resource> getAllReports() {
        Iterator<Resource> reports =null;

        try {
            reports = resourceResolver.resolve(componentUsageReportService.basePath()).listChildren();
            return  reports;
        }
        catch (Exception e){
            logger.error("Exception in getAllReports method.",e);
        }

        return reports;
    }

    /**
     * Get tool path string.
     *
     * @return the string
     */
    public String getToolPath(){
       return componentUsageReportService.toolPath();
    }
}
