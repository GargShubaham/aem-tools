package com.adobe.aemtools.core.services.impl;

import com.adobe.aemtools.core.Utils.CommonUtil;
import com.adobe.aemtools.core.constants.Constants;
import com.adobe.aemtools.core.services.ComponentUsageReportCreationService;
import com.adobe.aemtools.core.services.ComponentUsageReportService;
import com.adobe.cq.social.journal.client.api.Journal;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.search.Query;
import com.day.cq.wcm.api.NameConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.servlets.post.HtmlResponse;
import org.apache.sling.servlets.post.PostOperation;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component(service = ComponentUsageReportCreationService.class)
@ServiceDescription("Component Usage Report Creation service")
@ServiceRanking(1002)
@ServiceVendor("Shubham Garg")
public class ComponentUsageReportCreationServiceImpl implements ComponentUsageReportCreationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Reference
    ResourceResolverFactory resolverFactory;


    @Reference
    private ComponentUsageReportService componentUsageReportService;

    @Reference(target = "(sling.post.operation=modify)")
    private PostOperation modifyOperation;

    @Override
    public void submitReportCreationForm(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        try {
            ResourceResolver resolver = request.getResourceResolver();
            Session userSession = resolver.adaptTo(Session.class);
            String path = componentUsageReportService.basePath();
            Resource storedResource = ResourceUtil.getOrCreateResource(resolver, path, (String) null, JcrResourceConstants.NT_SLING_FOLDER,
                    false);
            resolver.commit();
            AtomicInteger uniqueIdCounter = new AtomicInteger();
            String uniqueId = System.currentTimeMillis() + "_" + uniqueIdCounter.addAndGet(1);
            Node parent = storedResource.adaptTo(Node.class);
            Node reportNode = parent.addNode(uniqueId, JcrConstants.NT_UNSTRUCTURED);
            userSession.save();
            reportNode.setProperty(JcrConstants.JCR_CREATED_BY, userSession.getUserID());
            userSession.save();
            reportNode.setProperty(JcrConstants.JCR_CREATED, CommonUtil.getCurrentDate());
            userSession.save();
            HtmlResponse htmlResponse = new HtmlResponse();
            ComponentUsageReportCreationServiceImpl.FormStoreRequestWrapper requestWrapper = new ComponentUsageReportCreationServiceImpl.FormStoreRequestWrapper(request, resolver, resolver.resolve(reportNode.getPath()));
            modifyOperation.run(requestWrapper, htmlResponse, null);
            response.sendRedirect(componentUsageReportService.toolPath().concat(Journal.URL_SUFFIX));
        } catch (Exception e) {
            logger.error("Exception in submitReportCreationForm method.",e);
        }
    }

    @Override
    public void searchComponents(SlingHttpServletRequest request, SlingHttpServletResponse response, RequestParameterMap params) {
        List < String > conditions = null;
        conditions = CommonUtil.populateParameterList(params, Constants.REPORT_PATHS, conditions);
        try {
            String[] conditionsarray  = CommonUtil.getStringArray(conditions);
            ResourceResolver resolver = request.getResourceResolver();
            boolean excludeChildrenPages = CommonUtil.excludeChildrenPages(params,Constants.CREATIONWIZARD_EXCLUDECHIDLRENPAGE);

            Set < String > testSet = new HashSet <> ();
            Query query = componentUsageReportService.getUsedComponentList(resolver, conditions, excludeChildrenPages);
            Iterator < Resource > itreator = query.getResult().getResources();
            while (itreator.hasNext()) {
                testSet.add(itreator.next().getResourceType());
            }
            Iterator < String > itr = testSet.iterator();
            JsonArray jsonArray = new JsonArray();
            while (itr.hasNext()) {
                String resourceType=null;
                try {
                    if (resolver != null) {
                        JsonObject jsonObject = new JsonObject();
                         resourceType = itr.next();
                         Resource resource = resolver.getResource(resourceType);
                         if(resource != null){
                             String fullPath = resource.getPath();

                                 if (resource.isResourceType(NameConstants.NT_COMPONENT) && (fullPath.startsWith(DamConstants.APPS)|| (!fullPath.startsWith(DamConstants.APPS) && componentUsageReportService.includeLibsComponent()))) {
                                     ValueMap valueMap = resource.getValueMap();
                                     jsonObject.addProperty("COMPONENT_FULL_PATH", fullPath);
                                     jsonObject.addProperty("COMPONENT_RESOURCETYPE", resourceType);
                                     jsonObject.addProperty("COMPONENT_USAGE", componentUsageReportService.getComponentUsageNumber(resourceType,resolver,conditionsarray,excludeChildrenPages ));
                                     jsonObject.addProperty("JCR_CREATED", valueMap.get("jcr:created",String.class));
                                     jsonObject.addProperty("JCR_CREATEDBY", valueMap.get("jcr:createdBy",String.class));
                                     jsonObject.addProperty("JCR_DESCRIPTION", valueMap.get("jcr:description",String.class));
                                     jsonObject.addProperty("JCR_COMPONENT_GROUP", valueMap.get("componentGroup",String.class));
                                     jsonObject.addProperty("JCR_TITLE", CommonUtil.getComponentTitle(resourceType,resolver));
                                     jsonArray.add(jsonObject);
                                 }

                         }
                         else{
                             logger.error("Unable to find this component in JCR. It might has been deleted,moved or renamed. Path is--->{}",resourceType);
                         }

                    }
                } catch (Exception e) {
                    logger.error("Exception in searchComponents method. Error occured inside loop for path --->"+resourceType,e);
                }
            }
            if (!jsonArray.isJsonNull()) {
                response.getWriter().write(jsonArray.toString());

            }

        } catch (Exception e) {
            logger.error("Exception in searchComponents method.",e);
        }

    }

    private class FormStoreRequestWrapper extends SlingHttpServletRequestWrapper {
        private ResourceResolver resolver;

        private Resource resource;

        public FormStoreRequestWrapper(SlingHttpServletRequest request, ResourceResolver resolver, Resource resource) {
            super(request);
            this.resolver = resolver;
            this.resource = resource;
        }
        @Override
        public Resource getResource() {
            return resource;
        }
        @Override
        public ResourceResolver getResourceResolver() {
            return resolver;
        }
    }

}