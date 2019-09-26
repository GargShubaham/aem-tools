/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aemtools.core.servlets;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import static javax.jcr.Node.JCR_CONTENT;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
           property={
                   "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.extensions=json",
                   "sling.servlet.resourceTypes=apps/pageditor"
           })
@ServiceDescription("Simple Demo Servlet")
public class SimpleServlet extends SlingAllMethodsServlet {
    @Reference
    ResourceResolverFactory resolverFactory;
    private static Logger LOGGER = LoggerFactory
            .getLogger(SimpleServlet.class);
    private static final long serialVersionUID = 1L;
  //  String path ="/content/wknd/en/jcr:content";
    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {

        final Resource resource = req.getResource();

        Session session = req.getResourceResolver().adaptTo(Session.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            session.exportDocumentView(getPagePath(req),out,true,false);
            resp.getWriter().write(prettyPrint(out.toString()));
        } catch (Exception  e) {
            resp.getWriter().write(ExceptionUtils.getFullStackTrace(e));
        }
    }
    private Document parseXml(String xml)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(IOUtils.toInputStream(xml, "utf-8"));
    }

    // https://examples.javacodegeeks.com/core-java/xml/dom/pretty-print-xml-in-java/
    private String prettyPrint(String xml)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = parseXml(xml);
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(doc), new StreamResult(out));
        return out.toString();
    }
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {
        try {
        Map<String, Object> authInfoParam = new HashMap<String, Object>();
        authInfoParam.put(ResourceResolverFactory.SUBSERVICE, "sysUserService");
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfoParam);
        Session session = resolver.adaptTo(Session.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

            InputStream in  = IOUtils.toInputStream(IOUtils.toString(request.getReader()), "utf-8");

            String pagePath = getStringBeforeSlash(getPagePath(request));
            session.getNode(getPagePath(request)).remove();
            session.save();
            session.getWorkspace().importXML(pagePath,in,
                    ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);
            session.save();
            Node page = session.getNode(pagePath);
            page.orderBefore(JCR_CONTENT,getStringAfterSlash(getFirstNode(page)));
            session.save();
            response.setStatus(SlingHttpServletResponse.SC_OK);


        } catch (Exception  e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ExceptionUtils.getFullStackTrace(e));
        }
    }
    private String getFirstNode(Node node){

        try {
            NodeIterator it = node.getNodes();
            while(it.hasNext()){
                return it.nextNode().getPath();
            }
        }
        catch (Exception  e) {
        }
        return "";
    }
     private String getStringBeforeSlash (String path){
        return StringUtils.substringBeforeLast(path, "/");
    }
    private String getStringAfterSlash (String path){
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getPagePath (SlingHttpServletRequest request){

        String pagePath = null;
        try {
            if (request.getRequestPathInfo().getSuffix() != null) {
                pagePath = request.getRequestPathInfo().getSuffix();
                return pagePath;
                //return pagePath.endsWith(JCR_CONTENT) ? pagePath : pagePath.concat(JCR_CONTENT);
            }
        }
        catch(Exception  e){

        }
        return  pagePath;
    }
}
