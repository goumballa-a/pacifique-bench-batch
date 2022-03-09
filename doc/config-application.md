# Configuration de l’application

## Présentation
La configuration d’une application Spring Boot est concentrée dans le fichier `application.properties`.  
La documentation [Common application properties](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/common-application-properties.html) détaille le contenu du fichier.   

Ce fichier est externalisé du fatjar et géré dans un projet Maven propre. Voir la documentation sur le [packaging](doc/packaging.md) d'un projet Spring Boot pour plus de détail.

## Format du fichier
Le format `.properties` est retenu pour la configuration des propriétés des applications.

> Seul le format `.properties` est compatible avec GESLIV. Le format yaml est donc interdit.

## Liste des fichiers
Il y a un fichier profilé en fonction du type d'environnement ciblé :
* [application-dev.properties](cfg/src/main/resources/application-dev.properties), pour l'exécution sur un environnement de développement
* [application-int.properties](cfg/src/main/resources/application-int.properties), pour l'exécution sur l'environnement d'intégration (via la PIC)
* [application-liv.properties](cfg/src/main/resources/application-liv.properties), pour la livraison en vue d'un déploiement en recette, préproduction, production...

Après sélection en fonction du profil, le fichier final produit par Maven est dépourvu du suffixe.

> Il est important de maintenir en même temps les 3 fichiers.

### application-dev.properties
Ce fichier est utilisé pour le poste de développement.

### application-int.properties
Ce fichier est utilisé sur les environnements d'intégration INT1 et INT2.

### application-liv.properties
Ce fichier est utilisé pour les livraisons via GESLIV. Il contient des clés variabilisées qui seront valorisées lors du déploiement.
Le set des valeurs à positionner est à définir dans le DEX de l'application.

## Clés variabilisées
La liste des clés variabilisées définie par le trame de livraison, telle qu'elle a été fixée avec l'équipe GCL.
La valorisation de ces variables est faite au moment du déploiement: les valeurs injectées sont choisies en fonction de l'environnement ciblé.

Le variable respecte le format suivant : `[NOM_VARIABLE]`  

> L'ajout variable doit faire l'objet d'une évolution de la trame de livraison.

### Liste des variables
| Variable                   | Clé                                                        | Description                               |
|----------------------------|------------------------------------------------------------|-------------------------------------------|
| SERVEUR_BASE               | spring.datasource.url                                      | Alias du serveur de la base de données    |
| SERVEUR_BASE_PORT          | spring.datasource.url                                      | Port du serveur de la base de données     |
| BASE_NOM                   | spring.datasource.url spring.datasource.name               | Nom de la base de données                 |
| BASE_USERNAME              | spring.datasource.username                                 | User de connexion à la base de données    |
| BASE_PASSWORD              | spring.datasource.password                                 | Password du user                          |
| JWT_SECRET_KEY             | security.authentication.jwt.secret                         | Clé de création/vérification du jeton JWT |
| JWT_TOKEN_VALIDITY_SECONDS | security.authentication.jwt.tokenValidityInSeconds         | Durée de validation du jeton JWT          |
| LDAP_SERVER                | security.ldapConfig.server                                 | Alias du serveur DNS ou du domaine AD     |
| LDAP_PORT                  | security.ldapConfig.port                                   | Port sur lequel le serveur LDAP écoute
| LDAP_USER                  | security.ldapConfig.connectionName                         | Compte de service d'interrogation de l'AD |
| LDAP_PASSWORD              | security.ldapConfig.connectionPassword                     | Password du compte de service             |
| GROUPE_LDAP_ACTUATOR       | management.security.roles                                  | Valeur hors prod `HM_ADM_SPB_DEV` <br /> Valeur prod `HM_ADM_SPB_PROD` |

## Configuration pour le batch
La section `BATCH CORE PROPERTIES` dans la *Danger Zone* concerne les batchs réactif, elle n'est activée que si un bach réactif est [configuré](config-batch.md).
> Il toutefois possible de la supprimer si vous le souhaitez mais attention à ne pas supprimer autre chose !

## Code projet
Le code applicatif doit être positionné dans la clé _info.app.custom.projectCode_. Ce champs est remonté dans le endpoint _/actuator/info_.
Le code est à récupérer dans la [Cartographie](http://cartographie/).
