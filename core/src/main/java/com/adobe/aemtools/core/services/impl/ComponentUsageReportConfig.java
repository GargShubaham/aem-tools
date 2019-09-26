package com.adobe.aemtools.core.services.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "Component Usage Report Configurations", description = "Component Usage Report Configurations")
public  @interface ComponentUsageReportConfig {


        @AttributeDefinition(name = "Path to store all reports configurations/metadata", description = "Configuration value", defaultValue = { "/conf/global/settings/report/component/config"})
        String basePath() default "/conf/global/settings/report/component/config";

        @AttributeDefinition(name = "Component report View", description = "Component report View", defaultValue = { "Tabs" }, options = { @Option(label = "Tabs", value = "Tabs"), @Option(label = "Accordion", value = "Accordion") })
        String reportView() default "Tabs";

        @AttributeDefinition(name = "Include Components From /libs", description = "Include Components From /libs", defaultValue = { "false" }, type = AttributeType.BOOLEAN)
        boolean includeLibsComponent() default false;

        @AttributeDefinition(name = "Open page in edit mode", description = "This will append editor.html in the URL", defaultValue = { "false" }, type = AttributeType.BOOLEAN)
        boolean openPageInEditMode() default false;

        @AttributeDefinition(name = "Component Usage Audit Tool Page Path", description = "Strictly : Do Not-edit (Component Usage Audit Tool Page Path)", defaultValue = { "/content/component-audit-manager"})
        String toolPath() default "/content/component-audit-manager";



}
