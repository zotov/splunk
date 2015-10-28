1) mvn archetype:generate \ 
   -DgroupId=com.buildua.maven.plugins \
   -DartifactId=buildua-maven-plugin  \
   -Dversion=1.0-SNAPSHOT \
   -DarchetypeGroupId=org.apache.maven.archetypes \
   -DarchetypeArtifactId=maven-archetype-mojo
   
2) mvn com.chernigov.ua.maven.build.plugin:build-ua-maven-plugin:1.0-SNAPSHOT:compress \
   -Decho.message="The Eagle has Landed"
   
   
[maven settings]
settings.xml -> <pluginGroups>
			   	   <pluginGroup>com.buildua.maven.plugins</pluginGroup>
			    </pluginGroups>
   
   
   
[compress files]
jar cvfm  server-node-release.jar server-node/META-INF/MANIFEST.MF -C server-node .

[extract files]
jar xvf  server-node.war


mvn com.buildua.maven.plugins:buildua-maven-plugin:1.0-SNAPSHOT:compress
mvn buildua:compress

mvn clean package buildua:compress -Dmaven.test.skip=true