
# Guide de migration vers Spring Boot 2

_Pré-requis:_ 

* Vider son repository Maven pour éviter les conflits de versions de classes

**Etapes:**

* Modifier les versions de dépendances SpringBoot et Fwk HM dans les POM comme

`<spring.boot.version>2.0.5.RELEASE</spring.boot.version>
<hm.fwk.spring.api.version>2.0.x</hm.fwk.spring.api.version>`

* Ajouter, si ce n'est pas déjà le cas, la gestion du plugin `spring-boot-maven-plugin` dans le POM parent du projet dans la section `pluginManagement`

```
	<pluginManagement>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<version>${spring.boot.version}</version>
				<configuration>
					<classifier>exec</classifier>
				</configuration>
				<executions>
					<execution>
						<goals>
							<!-- Repackages les JAR et WAR en jar exécutable. -->
							<goal>repackage</goal>
							<!-- Génère le fichier build-info.properties pour le endpoint /actuator/info -->
							<goal>build-info</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</pluginManagement>
 ```

* Ajouter le plugin `spring-boot-maven-plugin` dans le POM app du projet dans la section `plugins`

```
<plugin>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

* Supprimer, si ce n'est pas déjà le cas, le plugin `spring-boot-maven-plugin` dans le POM parent du projet dans la section `plugins`
* Supprimer les dépendances et propriétés inutiles dans les POMs: `jjwt`, `spring-ldap-core`, `spring-security-ldap`, `spring-security-data` ou encore `spring-security-test`
* Certaines propriétés de configuration ont été modifiées et/ou supprimées (surtout concernant la sécurité)
    * La propriété ``management.security.roles`` devient ``security.ldapConfig.actuatorRoles``
    * Suppression de la propriété ``management.security.enabled``
    * Diverses autres ont été renommées
         * ``server.display-name`` devient ``server.servlet.application-display-name``
         * ``server.context-path`` devient ``server.servlet.context-path``
         * ``server.servlet-path`` devient ``server.servlet.path``
         * ...
* Si besoin, la règle de nommage des endpoints actuator custom a changé: Le nom des custom endpoint doit être en kebab-case (séparé par des '-'), en caractères alpha-numeriques minuscules et doit commencer avec une lettre. En conséquence, le endpoint `customManifest` devient `custom-manifest`.
* Si la factory Undertow est utilisée, elle a été renommé (`UndertowEmbeddedServletContainerFactory` devient en Spring Boot 2.0, `UndertowServletWebServerFactory`)
* Si les content type de retour des APIs par défaut sont testés, leur version a évolué (`application/vnd.spring-boot.actuator.v2+json;charset=UTF-8` au lieu de `application/vnd.spring-boot.actuator.v1+json;charset=UTF-8`)
* Modifier les url des repositories Actifactory dans le bloc `<distributionManagement>` du POM principal comme suit: 
```
        <repository>
            ...
            <url>${hm.repository.url}</url>
        </repository>
        <snapshotRepository>
            ...
            <url>${hm.repository.snapshot.url}</url>
        </snapshotRepository>
```

* Passage à [HikariCP](https://github.com/brettwooldridge/HikariCP) : le passage à Springboot 2 provoque l'utilisation par défaut d'HikariCP en lieu et place de Tomcat Pool DB, la configuration existante fonctionne comme précédement mais il est conseillé de changer la clef pour le driver : 
    ```properties
    # Driver pour une base de données Postgres
    spring.datasource.driver-class-name=org.postgresql.Driver
    ```
    * Pour plus d'informations lire la [documentation Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html), notamment la [partie](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource) expliquant comment configurer deux datasources si besoin
    * Le passage à HikariCP nécessite aussi l'ajout de deux clefs de configuration évitant une stacktrace au démaraage (non bloquante mais toujours disgracieuse) : 
    ```properties
    # Disable feature detection by this undocumented parameter. Check the org.hibernate.engine.jdbc.internal.JdbcServiceImpl.configure method for more details.
    spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false
    # Because detection is disabled you have to set correct dialect by hand.
    spring.jpa.database-platform=org.hibernate.dialect.Oracle10gDialect # ou org.hibernate.dialect.PostgreSQLDialect
    ``` 
    * Les clés faisants références au Tomcat Pool DB sont à supprimer:
    `spring.datasource.tomcat.*=`

**Rappel de configuration de la sécurité:**
* La propriété ``security.ldapConfig.actuatorRoles`` fixe les roles actuator nécessaires (Par défaut il faut être authentifié pour y accéder)
* la propriété ``security.ldapConfig.projectRoles`` fixe les roles applicatifs nécessaires (Par défaut aucun rôle n'est nécessaire pour y accéder)


_References:_
* https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide
* https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/index.html
