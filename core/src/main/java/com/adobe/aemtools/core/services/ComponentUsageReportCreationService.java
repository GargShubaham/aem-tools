package com.adobe.aemtools.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;

/**
 * The interface Component usage report creation service.
 */
public interface ComponentUsageReportCreationService {
    /**
     * Submit report creation form.
     *
     * @param request  the request
     * @param response the response
     */
    void submitReportCreationForm(SlingHttpServletRequest request, SlingHttpServletResponse response);

    /**
     * Search components.
     *
     * @param request  the request
     * @param response the response
     * @param params   the params
     */
    void searchComponents(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params);
}
