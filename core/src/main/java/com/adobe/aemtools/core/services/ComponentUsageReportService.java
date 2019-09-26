package com.adobe.aemtools.core.services;

import com.day.cq.search.Query;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Iterator;
import java.util.List;

public interface ComponentUsageReportService {

    public String getReportView();
    public String basePath();
    public String toolPath();
    public boolean includeLibsComponent();
    public boolean openPageInEditMode();
    public Query getUsedComponentList(final ResourceResolver resourceResolver, List<String> conditions,boolean excludeChildrenPages);
    public int getComponentUsageNumber(final String componentPath, final ResourceResolver resourceResolver,String[] paths,boolean excludeChildrenPages);
    public Iterator<Resource> getNodesComponentUsedAt(final String componentPath, final ResourceResolver resourceResolver, String[] paths,boolean excludeChildrenPages) ;
}
