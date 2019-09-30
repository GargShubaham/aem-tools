package com.adobe.aemtools.core.services.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

/**
 * The interface Component usage report config.
 */
@ObjectClassDefinition(name = "Component Usage Report Configurations", description = "Component Usage Report Configurations")
public  @interface ComponentUsageReportConfig {


        /**
         * Base path string.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Path to store all reports configurations/metadata", description = "Configuration value", defaultValue = { "/content/component-audit-manager/report/component/config"})
        String basePath() default "/content/component-audit-manager/report/component/config";

        /**
         * Report view string.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Component report View", description = "Component report View", defaultValue = { "Tabs" }, options = { @Option(label = "Tabs", value = "Tabs"), @Option(label = "Accordion", value = "Accordion") })
        String reportView() default "Tabs";

        /**
         * Include libs component boolean.
         *
         * @return the boolean
         */
        @AttributeDefinition(name = "Include all Components e.g. from /libs,/etc ", description = "Important : By default only components available under /apps folder are included.Check the checkbox if you want to include all components e.g. /libs,/etc", defaultValue = { "false" }, type = AttributeType.BOOLEAN)
        boolean includeLibsComponent() default false;

        /**
         * Open page in edit mode boolean.
         *
         * @return the boolean
         */
        @AttributeDefinition(name = "Open page in edit mode", description = "This will append editor.html in the URL", defaultValue = { "false" }, type = AttributeType.BOOLEAN)
        boolean openPageInEditMode() default false;

        /**
         * Tool path string.
         *
         * @return the string
         */
        @AttributeDefinition(name = "Component Usage Audit Tool Page Path", description = "Strictly : Do Not-edit (Component Usage Audit Tool Page Path)", defaultValue = { "/content/component-audit-manager"})
        String toolPath() default "/content/component-audit-manager";



}
