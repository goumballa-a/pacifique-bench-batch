# Architecture technique d'une application Spring Boot

## Présentation
Ce fichier décrit l'architecture technique mise en place pour une application Spring Boot. Les éléments détaillés ici sont liés au runtime principalement.

## Scalling
Une application Spring Boot est déployée au minimum sur 2 noeuds. En cas de nécessité (par exemple, pic de charge), les équipes infrastructures peuvent augmenter le nombre de noeuds.
Donc c'est essentiel que l'application respecte le modèle des APIs stateless.

## Déploiement
Par défaut, les équipes de production effectuent un déploiement sans arrêt de service lorsqu'il n'y a pas d'évolution du schéma de base de données.

> Pour garantir le bon fonctionnement de l'application, il est recommandé de **garantir la rétrocompatibilité des endpoints avec la version n-1**.

Si ce mode de déploiement n'est pas souhaité, la DMEX devra le préciser.

## Execution en environnement serveur
Les environnements d'execution sont des serveurs Linux.
Les modules Spring Boot sont lancés en ligne de commande par des services: chaque instance a son propre service.

Le template de nom de service : `[ENV]_[MAVEN ARTEFACTID]_[PORT].service`.

Les paramètres suivants sont passés en paramètre de l'execution du module:
 * hostname
 * environment
 * port

Exemple d'une ligne de commande:

`java -Xms128m -Xmx256m -jar /logiciel/springboot/int1/seed-springboot-back/seed-springboot-back-app-exec.jar --server.exec.hostname=SR072607CTI3700 --server.exec.environment=int1 --server.port=8081`

## Gestion des OutOfMemory
En cas d'OutOfMemory, la JVM est killée et redémarrée automatiquement.

Un fichier dump est généré : `/data/dump/java_[PID du processus].hprof`.

## Monitoring et supervision
La supervision s'appuie sur les [endpoints ACTUATOR](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/html/production-ready-endpoints.html).

Par configuration, toutes les ressources catégorisées _"sensitive"_ par Spring Boot sont soumises à authentification.
Les comptes habilités à ces ressources doivent faire partie du groupe `HM_ADM_SPB_DEV` ou `HM_ADM_SPB_PROD`. Voir [Configuration de l’application](doc/config-application.md#liste-des-variables).
En développement et en intégration, il possible de d'utiliser le role `AUTHENTICATED`.

Pour être compatible avec les outils de supervision HM, le comportement du endpoint `/actuator/health` attendu est le suivant :
* Appel non authentifié > réponse JSON contenant le status simple (un seul status UP ou DOWN qui est une synthèse des retours de chaque composante);
* Appel authentifié > réponse JSON détaillée contenant le status "full" (les status UP ou DOWN de toutes les dataSources + autres détails).
(Voir [documentation officielle](https://docs.spring.io/spring-boot/docs/1.5.13.RELEASE/reference/html/production-ready-endpoints.html#production-ready-health) pour plus de détails)

A noter que la ressource `shutdown` est désactivée et doit le rester.

## Configuration des logs techniques
Le parttern retenu par le production est :
`%v %{i,X-Forwarded-For} %l %u %t \"%r\" %s %b \"%{i,Referer}\" \"%{i,User-Agent}\" %T %D`

Pour plus d'info sur le formatage, voir l'article [Configuration_-_access](https://kb.novaordis.com/index.php/Undertow_WildFly_Subsystem_Configuration_-_access-log#Pattern_Elements).
