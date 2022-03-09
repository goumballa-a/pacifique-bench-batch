# APIs d'un module Spring Boot

## Template des URLs

    http://[Alias DNS]/[Nom du projet maven]/[racine]/[ressources]

_Exemple:_
`http://beryl.sdw-int1.hm.dm.ad/beryl-api/api/interlocuteurs/1234`

## Racines à utiliser pour les endpoints
3 racines sont disponibles pour les api d'un module Spring Boot:
 * `/api` : ressources applicatives du module,
 * `/actuator` : ressources techniques du module,
 * `/public` : ressources publiques du module (facultatif).

## APIs applicatives
L'API d'un back end Spring Boot est obligatoirement une API RESTful. L'exposition d'une API via un autre protocole doit être étudiée avec les architectes HM.
Les ressources doivent être manipulées au travers des verbes et de codes retours HTTP.

_Exemple:_

| Verbe HTTP |           CRUD           |                                         Collection entière (ex. /adherents)                                        |                         Objet spécifique (ex /adherents/{id})                        |
|:----------:|:------------------------:|:------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------:|
| POST       | Création                 | 201 (Created), En-tête `location` avec lien vers /adherents/{id} contentant le ID.                                | 404 (Not Found), 409 (Conflict) si la ressource existe déjà..                        |
| GET        | Lecture                  | 200 (OK), liste des adherents. Utilisez la pagination, le tri et le filtrage pour naviguer dans les grosses listes.| 200 (OK), adherent unique. 404 (Not Found), si l'ID n'est pas trouvé ou invalide.    |
| PUT        | Mise à jour/Remplacement | 404 (Not Found), Sauf si vous souhaitez mettre à jour / remplacer chaque ressource de la collection entière.       | 200 (OK) ou 204 (No Content). 404 (Not Found), si l'ID n'est pas trouvé ou invalide. |
| PATCH      | Update/Modification      | 404 (Not Found), Sauf si vous souhaitez modifier la collection elle-même.                                          | 200 (OK) ou 204 (No Content). 404 (Not Found), si l'ID n'est pas trouvé ou invalide. |
| DELETE     | Delete                   | 404 (Not Found), Sauf si vous souhaitez supprimer la collection entière - pas souvent souhaitable.                 | 200 (OK) ou 204 (No Content). 404 (Not Found), si l'ID est invalide.                 |

**Remarque sur le retour suite à un PUT, un POST ou un PATCH**

Pour éviter au consommateur de devoir rappeler l'API pour obtenir une représentation mise à jour de la ressource, l'API peut retourner la ressource mise à jour (ou créée) dans le `boby` de la réponse.

L'objectif est de respecter le niveau 2 selon le [modèle de maturité de Richardson](https://martinfowler.com/articles/richardsonMaturityModel.html).
Pour plus de conseil sur le design d'API RESTful, voir l'excellente [OCTO Quick Reference Card](https://blog.octo.com/wp-content/uploads/2014/10/RESTful-API-design-OCTO-Quick-Reference-Card-2.2.pdf)
et l'article associé [Designer une API](https://blog.octo.com/designer-une-api-rest/).

La norme de versionning des APIs est décrite dans l'article Wikidev [Règles de nommage des URLs](https://harmoniemutuelle.sharepoint.com/sites/equipes/archi-app-logicielle/_layouts/WopiFrame.aspx?sourcedoc=%7B9CCC3766-9015-432F-96FD-424D229CD340%7D&file=Web%20et%20API%20-%20Evolution%20des%20r%C3%A8gles%20de%20nommage.pptx&action=default&DefaultItemOpen=1")

> En standard, une application Spring Boot HM expose des endpoints RESTful utilisant les verbes HTTP (Level 2).

## APIs techniques
| Endpoint                 | Origine                 | Description                                                                                                |
|--------------------------|-------------------------|------------------------------------------------------------------------------------------------------------|
| /actuator/customManifest | Custom HM               | Liste du contenu du fichier /META-INF/MANIFEST.MF                                                          |
| /actuator/info           | Enrichissement standard | Retourne la liste des valeurs  des clés préfixées `info.app.custom` dans le fichier application.properties |
| /actuator                | Standard Spring Boot    | Sets de endpoints qui permettent de surveiller et d'interagir avec l'application.                          |

### /actuator/customManifest
Ce endpoint retourne les informations contenues dans le fichier MANIFEST.MF.
Il permet de connaître la liste des starters activés dans l'application.  
La génération du fichier est dépendante de la configuration maven du plugin telle qu'elle est décrite dans [Modules embarqués dans l’appli](/doc/packaging.md).  

### /actuator/info
Ce endpoint enrichit le endpoint Actuator avec les valeurs des clés suivantes du fichier [application.properties](cfg/main/resources/application-int.properties):
* info.app.custom.projectCode, code applicatif de la cartographie
* info.app.custom.projectName, le _project.parent.artifactId_ déclaré dans Maven
* info.app.custom.projectDesc, le _project.parent.description_ déclaré dans Maven
* info.app.custom.projectVersion, la version _project.parent.version_ déclaré dans Maven
* info.app.custom.environment, le type d'environnement
* info.app.custom.hostname, le nom du serveur hote de l'instance
* info.app.custom.serverPort, le port d'écoute de l'instance

La valorisation de ces clés est détaillée dans la [configuration application.properties](doc/config-application.md).

Il est possible d'enrichir la liste avec d'autres données: pour plus d'explication, voir, dans la documentation officielle, le paragraphe [Custom application info information](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/production-ready-endpoints.html#production-ready-application-info-env)

### Endpoints /actuator
Le module Actuator est utilisé pour le health check : les endpoints JMX et HTTP seront activés.
Pour plus de détail, voir la [documentation officielle Spring Boot](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/production-ready-endpoints.html).

> L'activation des endpoints Actuator est obligatoire.

Exemples de endpoints HTTP :
* http://[serveur]:[port]/[module Spring Boot]/actuator/metrics
    * les métriques de l’application (système, JVM, threads, datasources)
* http://[serveur]:[port]/[module Spring Boot]/actuator/env  
    * les variables d’environnement système et les paramètres configurés Spring Boot (ceux dans l’application.properties / .yml)
* http://[serveur]:[port]/[module Spring Boot]/actuator/info
    * informations basiques sur l’application (issues du POM Maven + l’heure de build)
* http://[serveur]:[port]/[module Spring Boot]/actuator/health
    * health check sur l’espace disque et la base de données
* http://[serveur]:[port]/[module Spring Boot]/actuator/beans
    * liste des beans gérés par Spring Boot
* http://[serveur]:[port]/[module Spring Boot]/actuator/autoconfig
    * rapport d’auto-configuration des beans Spring, les conditions qui permettent de déclencher l’autoconfiguration et la liste des beans autoconfigurés et ceux qui ne le sont pas
* http://[serveur]:[port]/[module Spring Boot]/actuator/configprops
    * liste complète des propriétés Spring Boot configurées, soit volontairement, soit automatiquement
* http://[serveur]:[port]/[module Spring Boot]/actuator/trace
    * liste des 100 derniers appels HTTP

Il est à noter qu'une partie des endpoints ACTUATOR doit être sécurisée. Pour plus de détail, voir [Architecture technique - Monitoring et supervision](doc/architecture-technique.md#monitoring-et-supervision)
