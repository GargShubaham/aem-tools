# AEM-Tools
Component Usage Audit Manager

![Final_Component_Report](https://user-images.githubusercontent.com/33688281/65831446-0c141700-e2d7-11e9-9522-b2d735dae83f.gif)

## Modules

The main parts of the project are:
* core: Java bundle containing all core functionality like OSGi services, models, servlets etc.
* ui.apps: contains the /apps parts of the project, ie JS&CSS clientlibs, components.
* ui.content: contains sample content using the components from the ui.apps

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean install -PautoInstallPackage

Depending on your maven configuration, you may find it helpful to force the resolution of the Adobe public repo with

    mvn clean install -PautoInstallPackage -Padobe-public
    
Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallPackagePublish
    
Or alternatively

    mvn clean install -PautoInstallPackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

## Alternatively, Install package directly
    [aemtools.component.usage.audit.manager-1.0-SNAPSHOT.zip](https://github.com/GargShubaham/aem-tools/files/3669161/aemtools.component.usage.audit.manager-1.0-SNAPSHOT.zip)
    
## Compatibilty
Developer on AEM 6.5.
Tested and working fine on
AEM 6.5 and 6.4.
If you need it for lesser vesion. Make changes in POM accordingly or let me know by raising an issue :).

    
    


