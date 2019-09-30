package com.adobe.aemtools.core.services;

import com.day.cq.search.Query;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Iterator;
import java.util.List;

/**
 * The interface Component usage report service.
 */
public interface ComponentUsageReportService {

    /**
     * Gets report view.
     *
     * @return the report view
     */
    String getReportView();

    /**
     * Base path string.
     *
     * @return the string
     */
    String basePath();

    /**
     * Tool path string.
     *
     * @return the string
     */
    String toolPath();

    /**
     * Include libs component boolean.
     *
     * @return the boolean
     */
    boolean includeLibsComponent();

    /**
     * Open page in edit mode boolean.
     *
     * @return the boolean
     */
    boolean openPageInEditMode();

    /**
     * Gets used component list.
     *
     * @param resourceResolver     the resource resolver
     * @param conditions           the conditions
     * @param excludeChildrenPages the exclude children pages
     * @return the used component list
     */
    Query getUsedComponentList(final ResourceResolver resourceResolver, List<String> conditions,boolean excludeChildrenPages);

    /**
     * Gets component usage number.
     *
     * @param componentPath        the component path
     * @param resourceResolver     the resource resolver
     * @param paths                the paths
     * @param excludeChildrenPages the exclude children pages
     * @return the component usage number
     */
    int getComponentUsageNumber(final String componentPath, final ResourceResolver resourceResolver,String[] paths,boolean excludeChildrenPages);

    /**
     * Gets nodes component used at.
     *
     * @param componentPath        the component path
     * @param resourceResolver     the resource resolver
     * @param paths                the paths
     * @param excludeChildrenPages the exclude children pages
     * @return the nodes component used at
     */
    Iterator<Resource> getNodesComponentUsedAt(final String componentPath, final ResourceResolver resourceResolver, String[] paths,boolean excludeChildrenPages) ;
}
