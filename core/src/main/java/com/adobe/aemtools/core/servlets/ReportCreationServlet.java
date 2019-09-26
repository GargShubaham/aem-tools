package com.adobe.aemtools.core.servlets;

import com.adobe.aemtools.core.constants.Constants;
import com.adobe.aemtools.core.services.ComponentUsageReportCreationService;
import com.adobe.aemtools.core.services.DeleteReportsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = {Servlet.class}, property = {org.osgi.framework.Constants.SERVICE_DESCRIPTION + "=Report Creation Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "granite/ui/components/coral/foundation/page",
        "sling.servlet.selectors=" + "componentreport",
        "sling.servlet.extensions=" + "xml"
})
@ServiceDescription("Report Creation Servlet")
public class ReportCreationServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 8565934343267006374L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ComponentUsageReportCreationService componentUsageReportCreationService;

    @Reference
    private DeleteReportsService deleteReportsService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        try {

            final RequestParameterMap params = request.getRequestParameterMap();
            if (params.containsKey(Constants.CREATIONWIZARD_GET_USED_COMPONENT_LIST)) {
                logger.trace("Getting component list.");
                componentUsageReportCreationService.searchComponents(request, response, params);
            }
            else if (params.containsKey(Constants.DELETE_REPORTS_PARAMETER)) {
                logger.trace("Delete reports.");
                deleteReportsService.doDelete(request, response, params);
            }
            else if (!params.containsKey(Constants.DELETE_REPORTS_PARAMETER) && !params.containsKey(Constants.CREATIONWIZARD_GET_USED_COMPONENT_LIST) ){
                logger.trace("Submitting report creation form.");
                request.removeAttribute("dtBasicExample_length");
                componentUsageReportCreationService.submitReportCreationForm(request, response);
            }
        }
        catch (Exception e){
            logger.error("Exception in doGet method.",e);
        }

    }

}
