package com.adobe.aemtools.core.services.impl;

import com.adobe.aemtools.core.constants.Constants;
import com.adobe.aemtools.core.services.DeleteReportsService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;

@Component(service = { DeleteReportsService.class })
@ServiceDescription("Report deletion service")
@ServiceRanking(1001)
@ServiceVendor("Shubham Garg")
public class DeleteReportsServiceImpl implements DeleteReportsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

@Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params)
    {
        try {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            List < String > deletePaths = null;
            deletePaths = populateParameterList(params, Constants.DELETE_REPORTS_PATH, deletePaths);
            for (String path : deletePaths) {
                deletePath(path,session);
            }
            response.setStatus(SlingHttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception in doDelete method.",e);
        }
    }


    private void deletePath(String path , Session session){
        try {
            Node node = session.getNode(path);
            node.remove();
            session.save();
        }
        catch (Exception e){

            logger.error("Exception in deletePath method. Error occured in path --> "+path + "   "  ,e);
        }
    }

    private List< String > populateParameterList(final RequestParameterMap params, final String parameterName, List < String > parameterValues) {
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

}
