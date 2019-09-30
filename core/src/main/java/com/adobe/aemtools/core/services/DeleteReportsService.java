package com.adobe.aemtools.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;

/**
 * The interface Delete reports service.
 */
public interface DeleteReportsService {
    /**
     * Do delete.
     *
     * @param request  the request
     * @param response the response
     * @param params   the params
     */
    void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params);
}
