// 
// Decompiled by Procyon v0.5.36
// 

package com.adobe.aemtools.core.services.impl;

import com.adobe.aemtools.core.services.ComponentUsageReportService;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Component usage report service.
 */
@Component(service = { ComponentUsageReportService.class })
@Designate(ocd = ComponentUsageReportConfig.class)
@ServiceDescription("Component Usage Report service")
@ServiceRanking(1001)
@ServiceVendor("Shubham Garg")
public class ComponentUsageReportServiceImpl implements ComponentUsageReportService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Activate
    private ComponentUsageReportConfig config;
    @Reference
    private QueryBuilder builder;
    
    @Override
    public String getReportView() {
        return this.config.reportView();
    }
    
    @Override
    public String basePath() {
        return this.config.basePath();
    }


    @Override
    public String toolPath(){
        return this.config.toolPath();
    }

    
    @Override
    public boolean includeLibsComponent() {
        return this.config.includeLibsComponent();
    }

    @Override
    public boolean openPageInEditMode() {
        return this.config.openPageInEditMode();
    }
    

    @Override
    public int getComponentUsageNumber(final String componentPath, final ResourceResolver resourceResolver, final String[] paths,boolean excludeChildrenPages) {
        int hitsPerPage = 0;
        try {
            final Query quesry = this.getQuery(componentPath, resourceResolver, paths,excludeChildrenPages);
            if (quesry != null) {
                hitsPerPage = quesry.getResult().getHits().size();
            }
        }
        catch (Exception ex) {
            logger.error("Exception in getComponentUsageNumber method.",ex);
        }
        return hitsPerPage;
    }
    
    @Override
    public Iterator<Resource> getNodesComponentUsedAt(final String componentPath, final ResourceResolver resourceResolver, final String[] paths,boolean excludeChildrenPages) {
        Iterator<Resource> itreator = null;
        try {
            final Query query = this.getQuery(componentPath, resourceResolver, paths,excludeChildrenPages);
            if (query != null) {
                itreator = query.getResult().getResources();
            }
        }
        catch (Exception ex) {
            logger.error("Exception in getNodesComponentUsedAt method.",ex);
        }
        return itreator;
    }
    
    private Query getQuery(final String componentPath, final ResourceResolver resourceResolver, final String[] paths,boolean excludeChildrenPages) {
        Query query = null;
        try {
            final Session session = (Session)resourceResolver.adaptTo((Class)Session.class);
            final Map<String, String> map = new HashMap<>();
            for (int i = 0; i < paths.length; i++) {
                String itemNumber = Integer.toString(i+1);
                if(excludeChildrenPages) {
                    map.put("group." + itemNumber + "_group." +itemNumber+ "_path", paths[i]);
                    map.put("group." + itemNumber + "_group.2_path", paths[i].concat("/" + JcrConstants.JCR_CONTENT));
                    map.put("group." + itemNumber + "_group.2_path.self", "true");
                    map.put("group." + itemNumber + "_group.p.and", "true");
                }
                else{
                    map.put("group." + itemNumber + "_path", paths[i]);
                }
            }
            map.put("group.p.or", "true");
            map.put("1_property", ResourceResolver.PROPERTY_RESOURCE_TYPE);
            map.put("1_property.value", componentPath);
            map.put("p.limit", "-1");
            this.builder = (QueryBuilder)resourceResolver.adaptTo((Class)QueryBuilder.class);
            if( this.builder != null) {
                query = this.builder.createQuery(PredicateGroup.create((Map)map), session);
                return query;
            }
        }
        catch (Exception ex) {
            logger.error("Exception in getQuery method.",ex);
        }
        return query;
    }
    @Override
    public Query getUsedComponentList(final ResourceResolver resourceResolver, final List<String> conditions,boolean excludeChildrenPages) {
        Query query = null;
        try {
            final Session session = (Session)resourceResolver.adaptTo((Class)Session.class);
            final Map<String, String> map = new HashMap<>();
            for (int i = 0; i < conditions.size(); i++) {
                String itemNumber = Integer.toString(i+1);
                if(excludeChildrenPages) {
                    map.put("group." +itemNumber+ "_group." + i + 1 + "_path", conditions.get(i));
                    map.put("group." +itemNumber+ "_group.2_path", conditions.get(i).concat("/" + JcrConstants.JCR_CONTENT));
                    map.put("group." +itemNumber+ "_group.2_path.self", "true");
                    map.put("group." +itemNumber+ "_group.p.and", "true");
                }
                else{
                    map.put("group."+itemNumber+"_path", conditions.get(i));
                }
            }
            map.put("group.p.or", "true");
            map.put("property", ResourceResolver.PROPERTY_RESOURCE_TYPE);
            map.put("property.operation", "exists");
            map.put("p.limit", "-1");

            this.builder = (QueryBuilder)resourceResolver.adaptTo((Class)QueryBuilder.class);
            if( this.builder != null) {
                query = this.builder.createQuery(PredicateGroup.create((Map) map), session);
                return query;
            }
        }
        catch (Exception ex) {
            logger.error("Exception in getUsedComponentList method.",ex);
        }
        return query;
    }

}
