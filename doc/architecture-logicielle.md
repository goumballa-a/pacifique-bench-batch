# Architecture logicielle d'une application Spring Boot

## Présentation
Ce fichier décrit le **standard Harmonie Mutuelle** pour l'architecture logicielle d'une application Spring Boot.
Les choix détaillés ici tiennent compte des exigences Production.

## Choix structurants
Pour connaitre les choix qui ont structurés l'emploi de cette stack, voir l'article [Web et API - Choix structurants](https://harmoniemutuelle.sharepoint.com/sites/equipes/archi-app-logicielle/SitePages/Web%20et%20API/Filière%20Web%20et%20API%20-%20Choix%20structurants.aspx) du wiki Architecture Applicative et Logicielle.

## Version de Spring Boot
La version minimale supportée par ce seed est la version `1.5.13`.
Afin de s’adapter à la fréquence de sortie des versions mineures (une dizaine par an), le choix de la version mineure sera laissé à l’équipe projet en charge de l’application, en concertation avec l’architecte applicatif.
Concernant la version majeure, le fonctionnement sera différent: pour des raisons évidente de respects des standards HM, le changement de version majeure doit faire l'objet d'une publication au catalogue préalablement à toute utilisation.

### Liste des dépendances tirées par Spring boot et leurs versions
Recherchez dans Artifactory, l'artefact maven `org.springframework.boot:spring-boot-dependencies` avec la version de Spring Boot et parcourez le POM.

Exemple : [spring-boot-dependencies/1.5.13](http://pichm/artifactory/webapp/#/artifacts/browse/tree/PomView/jcenter-cache/org/springframework/boot/spring-boot-dependencies/1.5.13.RELEASE/spring-boot-dependencies-1.5.13.RELEASE.pom)

## Liste des starters
Les listes suivantes répertorient les starters Spring Boot obligatoires et les starters conseillés en cas de besoin applicatif.

> Il est à noter qu'il ne s'agit pas de listes exhaustives des starters proposés par Spring Boot. En cas besoin complémentaire, chaque application pourra activer le ou les starters nécessaires en vaillant bien à respecter la documentation.

### Starters obligatoires et conseillés
|             Starter            |                                              Description                                              | Utilisation |                                                                                                 Complément,d’explication                                                                                                 |
|--------------------------------|-------------------------------------------------------------------------------------------------------|:-----------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-boot-starter-web        | APIsation de l’application avec Spring MVC REST                                                       | Obligatoire | Permet de réaliser une API Rest et utilisé par le starter spring-boot-starter-actuator.                                                                                                                                  |
| spring-boot-starter-data-jpa   | Accès aux données avec JPA et fonctionnalités étendues de Spring Data JPA                             |  Conseillé  | _Utiliser de préférence Hibernate car cette implémentation est privilégié par Spring._<br/> Permet aussi de bénéficier d’un certain nombre de méthodes d’accès à la base sans écrire une ligne de code d’implémentation. |
| spring-boot-starter-validation | Validation des données avant utilisation en base                                                      |  Conseillé  | Utilise Hibernate validator.                                                                                                                                                                                             |
| spring-boot-starter-logging    | Gestion des logs via logback                                                                          | Obligatoire | La configuration des logs doit respecter le [standard production](http://ergon/sihm/1/StandardsdeProduction/Biblio/Standards%20logs%20SIHM.pdf).                                                                             |
| spring-boot-starter-security   | Gestion centralisée de la sécurité,sur laquelle s’adosse JWT & Spring LDAP                            |  Conseillé  | En cas de besoins de sécurisation du back end.                                                                                                                                                                           |
| spring-boot-starter-test       | Pour les tests `¯\_(ツ)_/¯`                                                                           | Obligatoire | L’écriture des tests est nécessaire, pour garantir la non régression de l’API utilisée par le front Angular.                                                                                                              |
| spring-boot-starter-undertow   | Moteur de servlet                                                                                     | Obligatoire | Choix lié au standard de production.                                                                                                                                                                                     |
| spring-boot-starter-actuator   | API de monitoring et d’administration à destination de la production (audit, supervision, métriques)  | Obligatoire | Permet à l’application d’être compatible avec les outils de production.                                                                                                                                                  |
| spring-boot-starter-mail       | Envoi d’emails                                                                                        |  Conseillé  | En cas de besoin fonctionnel.                                                                                                                                                                                            |    

### Librairies Spring complémentaires
En fonction des besoins, il peut être utile d’utiliser des projets de la galaxie Spring pour couvrir des besoins fonctionnels précis. Le tableau suivant propose une liste de suggestion :

|                    Starter                    |                         Description                        | Utilisation |                                                                               Complément,d’explication                                                                               |
|-----------------------------------------------|------------------------------------------------------------|:-----------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-ldap                                   | Librairie qui simplifie les opérations sur un serveur LDAP |  Conseillé  | [https://spring.io/projects/spring-ldap/](https://spring.io/projects/spring-ldap/)                                                                                                     |
| spring-jms                                    | Utilisation de l'API JMS                                   |  Conseillé  | [https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/integration.html#jms](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/integration.html#jms)             |
| spring-ws-core                                | Client ou exposition de web services SOAP                  |  Conseillé  | Utilisable pour la consommation de service SOAP. [https://spring.io/projects/spring-ws/](https://spring.io/projects/spring-ws/)                                                        |
| spring-websocket<br/> +<br/> spring-messaging | Client ou serveur websocket                                |  Conseillé  | [https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/web.html#websocket](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/web.html#websocket) |

### Complément d'information
L’utilisation d’autres frameworks ou librairies, comme par exemple Jersey ou EclipseLink, est possible mais cela implique souvent une ou plusieurs configurations spécifiques supplémentaires (exclusion de dépendances Maven, inclusion partielle de starter, clés supplémentaires dans le fichier `application.properties`, codage de classe technique,…).
Cela est dû aux choix fait par défaut dans Spring Boot.
Pour cette raison, les choix obligatoires qui dérogent aux choix par défaut de Spring Boot sont limités au strict minimum.

## Moteur de servlet
Le moteur de servlet retenu comme standard par les équipe de production est Undertow.

> Le moteur de servlet Undertow est une exigence de production; les équipes ne sont pas autorisées à en choisir un autre de façon unilatérale et sans validation préalable de la production.

Tomcat étant le choix par défaut de Spring, l'utilisation de Undertow nécessite l’exclusion du starter _spring-boot-starter-tomcat_ et l’inclusion du starter _spring-boot-starter-undertow_ à la place. Cette exclusion est en place dans le [pom.xml principal](pom.xml).
Voir la documentation officielle pour plus d’information : [Use Undertow instead of Tomcat](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/howto-embedded-web-servers.html#howto-use-another-web-server).

## Gestion du pool de connexion
Le choix retenu est le **pool de connexion HikariCP** (choix par défaut Spring Boot).

Le pool de connexion est paramétré en suivant les recommendations des DBA, il est possible de spécifier d'autres valeurs (en demandant conseil aux DBA), pour cela variabiliser les paramètres tels que décrit dans l'article de [configuration de l'application](config-application.md).
Les options de configuration sont détaillées dans la [documentation](https://github.com/brettwooldridge/HikariCP) d'HikariCP avec notamment les [clefs les plus utilisées](https://github.com/brettwooldridge/HikariCP#frequently-used).

:warning:
**Attention**
Un projet qui utilise plusieurs Datasources doit les déclarer de manière explicite (en utilisant les clefs de configuration HikariCP), les articles suivants expliquent comment faire : 
 - https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html
 - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-a-datasource

## Sécurité
La sécurité applicative est déléguée à l'annuaire d'entreprise Active Directory pour les applications Intranet.  
Concernant les applications Extranet/Internet, il est recommandé de s'appuyer au maximum sur une solution LDAP plutôt qu'une solution locale.

Les end-points de l'application sont sécurisés avec Spring Security et [JWT](https://jwt.io/). La sécurité applicative est déléguée à l’annuaire d’entreprise Active Directory.
Voir [Configuration et fonctionnement de la sécurité](doc/config-securite.md).

## Tests
Avec spring-boot-starter-test, les librairies suivantes sont disponibles :
* JUnit — The de-facto standard for unit testing Java applications.
* Spring Test & Spring Boot Test — Utilities and integration test support for Spring Boot applications.
* AssertJ — A fluent assertion library.
* Hamcrest — A library of matcher objects (also known as constraints or predicates).
* Mockito — A Java mocking framework.
* JSONassert — An assertion library for JSON.
* JsonPath — XPath for JSON.


Compte tenu de l’importance de l’API Rest (qui est la seule adhérence entre le front et le back), l’écriture de tests des appels REST sera fortement encouragée.

Pour améliorer la couverture de test des getter/setter, il possible d'utiliser un framework comme [OpenPojo](http://openpojo.com).

## Logs
Voir [Configuration du loggeur](doc/config-log.md).

## Cache
Spring Boot, avec le `spring-boot-starter-cache`, permet d’apporter la gestion de cache pour mettre en cache les réponses des méthodes.

Les annotations _Spring cache_ ou celles de la _JSR-107_ (`JCache`) peuvent être utilisées, mais il est recommandé de ne pas mixer les 2. Aucun choix n’est imposé.

Spring Boot est capable de configurer automatiquement un CacheManager sur la base de l’annotation `@EnableCaching`. Si aucun bean de type CacheManager n’est défini ou de type CacheResolver (nommé cacheResolver), Spring Boot essaye de configurer automatiquement le provider de cache, en prenant, dans l’ordre :
* Generic Cache
* JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, etc)
* EhCache 2.x
* Hazelcast
* Infinispan
* Couchbase
* Redis
* Caffeine
* Guava
* Simple (ConcurrentHashMap)

Etant donné que le type de cache est lié au besoin fonctionnel (cache distribué ou local, volumétrie, durée de rétention, etc…), aucun provider ne sera imposé.
Aucune implémentation ou solution de cache n’est imposée. Il est néanmoins fortement conseillé de forcer le provider de cache avec la propriété `spring.cache.type`.
