package com.adobe.aemtools.core.services;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;

public interface ComponentUsageReportCreationService {
    public void submitReportCreationForm(SlingHttpServletRequest request, SlingHttpServletResponse response);
    public void searchComponents(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params);
}
