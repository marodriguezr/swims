# MVN, EAR, EJB, JSF Boilerplate.

## How to  
This boilerplate is meant to be used with any distribution of wildfly that supports Jakarta EE 8.
## How to create new modules
1. Download the proper boilerplate code of the kind of module you want to add.  
[EJB](https://github.com/migonos01454f/jejb-boilerplate)  
[WAR](https://github.com/migonos01454f/jwar-boilerplate)  
2. Change the module's artifactId according to your needs.
3. Register the new module under the modules section of the pom.xml file.  
Eg:  
```xml
<module>new-war-module</module>
```
4. Register the new module under the dependencyManagement/dependencies section of the pom.xml file.  
Eg:  
```xml
<dependency>
	<groupId>org.sonatype.mavenbook.multi</groupId>
	<artifactId>new-war-module</artifactId>
	<version>1.0</version>
	<type>war</type>
</dependency>
```
5. Register the new module under the dependencies section of the ear-module/pom.xml file.  
Eg:  
```xml
<dependency>
	<groupId>org.sonatype.mavenbook.multi</groupId>
	<artifactId>new-war-module</artifactId>
	<type>war</type>
</dependency>
```
6. Register the new module under the configuration section of the maven-ear-plugin located in the ear-module/pom.xml file.
Eg:  
```xml
<webModule>
	<groupId>org.sonatype.mavenbook.multi</groupId>
	<artifactId>new-war-module</artifactId>
	<contextRoot>/new_jear_boilerplate</contextRoot>
	<bundleFileName>new-war-module-1.0.war</bundleFileName>
</webModule>
```

## Available commands  
1. Package:  
```bash
mvn package
```
2. Deploy to wildfly server:
	1. Start an instance of wildfly.
	2. Change directory to ear-module
	3. Execute:
	```bash
	mvn clean install && mvn wildfly:deploy
	```
3. Get help of wildfly plugin:
	1. Under the ear-module directory execute:
	```bash
	mvn wildfly:help
	```
4. Redeploy to widlfly server:
	1. Under the ear-module directory execute:  
	```bash
	mvn wildfly:redeploy
	```
	
## Msade possible by 
1. [Maven J2EE Archetype](https://pastebin.com/raw/TLTFh9aW)
2. [How to build an EAR project with EJB and WAR using Maven?](https://stackoverflow.com/questions/32990664/how-to-build-an-ear-project-with-ejb-and-war-using-maven)
3. [Apache Maven EAR Plugin Documentation](https://maven.apache.org/plugins/maven-ear-plugin/index.html)

