package com.adobe.aemtools.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;

public interface DeleteReportsService {
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params);
}
