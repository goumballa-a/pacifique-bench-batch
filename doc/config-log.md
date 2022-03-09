# Configuration du logging

## Présentation
Le framework de logging retenu est **logback**.
> La documentation de logback est disponible sur le [site officiel](https://logback.qos.ch/manual/index.html).

Spring Boot propose un certain nombre d'extensions pour Logback qui permettent une configuration avancée. Pour activer cette fonctionnalité, il faut utiliser ces extensions dans le fichier de configuration logback-spring.xml.
C'est le format retenu dans les applications Spring Boot HM.
> Pour plus d'information sur les extensions proposées par Spring Boot, voir la section [Logback extensions](https://docs.spring.io/spring-boot/docs/2.0.5.RELEASE/reference/htmlsingle/#boot-features-logback-extensions) dans la documentation officielle.

Il y a un fichier profilé en fonction du type d'environnement ciblé :
* [logback-spring-dev.xml](cfg/src/main/resources/logback-spring-dev.xml), pour l'exécution sur un environnement de développement
* [logback-spring-int.xml](cfg/src/main/resources/logback-spring-int.xml), pour l'exécution sur l'environnement d'intégration (via la PIC)
* [logback-spring-liv.xml](cfg/src/main/resources/logback-spring-liv.xml), pour la livraison en vue d'un déploiement en recette, préproduction, production...

Après sélection, en fonction du profil, le fichier final produit par Maven est dépourvu du suffixe.

## Standard
_logback_ a l’avantage de faire déjà parti de la stack Java depuis plusieurs années et d’être compatible avec les standards de la production.
Ces standards sont décrits dans le document [Standard Logs SIHM.pdf](http://ergon/sihm/1/StandardsdeProduction/Biblio/Standards%20logs%20SIHM.pdf).

## Usage
Le fichier de log produit ne cumulera que les **logs applicatifs** et sera unique pour chaque noeud de l'application (c'est-à-dire un fichier par instance du module Spring Boot).

## Configuration
Chaque application configure les niveaux de log de ces packages en fonction des profils.
> Il est conseillé d'affiner la configuration par package applicatif.

Le tableau suivant détaille les niveaux de logs proposés dans le seed :

|       Package       |  dev  |  int  |  liv  |
|---------------------|:-----:|:-----:|:-----:|
| fr.hm.seed          | TRACE |  INFO |  WARM |
| org.springframework | DEBUG |  INFO | ERROR |
| org.hibernate       |  INFO |  INFO | ERROR |
| org.xnio            |  INFO |  INFO | ERROR |
| io.undertow         |  INFO |  INFO | ERROR |
| com.jayway.jsonpath |  INFO |  INFO | ERROR |
| org.jboss.logging   |  WARN |  WARN | ERROR |
| ch.qos.logback      |  WARN |  WARN | ERROR |
| root                |  WARN | ERROR | ERROR |

> Chaque application peut ajuster les niveaux en fonction de ses besoins.

## Accès JMX
L’accès aux logs via JMX est actif également. Pour ce faire, la ligne suivante est ajoutée dans le [logback.xml](cfg/src/main/resources/logback-spring-int.xml) :

    <jmxConfigurator />


## Modification des niveaux de logs à chaud

La modification du niveau de logs à chaud est possible grâce au endpoint _loggers_.

### Usage

La consultation et la modification du niveau de log se fait grâce à une requête HTTP (en **GET**) sur le end-point loggers.

**Exemple de consultation :**

    GET http://localhost:8080/seed-springboot-back/actuator/loggers

_Résultat_:

    {
        "levels": [
            "OFF",
            "ERROR",
            "WARN",
            "INFO",
            "DEBUG",
            "TRACE"
        ],
        "loggers": {
            "ROOT": {
                "configuredLevel": "WARN",
                "effectiveLevel": "WARN"
            },
            "fr": {
                "configuredLevel": null,
                "effectiveLevel": "WARN"
            },
            "fr.hm": {
                "configuredLevel": null,
                "effectiveLevel": "WARN"
            },
            "fr.hm.seed": {
                "configuredLevel": "TRACE",
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.SpringBootDemoApplication": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.repository": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.repository.LdapUserRepository": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.Http401UnauthorizedEntryPoint": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.SecurityConfiguration": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.SecurityUtils": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.UserDetailsService": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.jwt": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.jwt.JWTFilter": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.security.jwt.TokenProvider": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.service": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.service.LdapUserService": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.service.ManifestService": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "CustomerService": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.web": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.web.CustomManifestEndpoint": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "fr.hm.seed.web.LdapUserJWTResource": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            },
            "CustomerResource": {
                "configuredLevel": null,
                "effectiveLevel": "TRACE"
            }
        }
    }

 **Exemple de modification du niveau de logs du package « fr.hm.seed » :**

    POST http://localhost:8080/seed-springboot-back/actuator/loggers/fr.hm.seed

    {
      "configuredLevel": "WARN"
    }

_Résultat_:

    Pas de retour JSON

**Exemple de consultation du niveau de logs du package « fr.hm.seed » :**

    GET http://localhost:8080/seed-springboot-back/actuator/loggers/fr.hm.seed

_Résultat_:

    {
        "configuredLevel": "WARN",
        "effectiveLevel": "WARN"
    }

## Localisation des fichiers de log
### Sur le poste du développement

| Logs applicatifs | `[Répertoire d'exécution]\logs\localhost_dev_[project.artifactId]_[port]_springboot.log`      |
|------------------|-----------------------------------------------------------------------------------------------|
| Logs techniques  | `[Répertoire d'exécution]\logs\localhost_dev_[project.artifactId]_[port]_undertow_access.log` |

Le répertoire d'exécution est le plus souvent `[projet Maven]\app\target`.

### Sur un serveur

| Logs applicatifs | `/data/log/springboot/[environnement]/[project.artifactId]/[project.artifactId]_[port]/[hostname]_[environnement]_[project.artifactId]_[port]_springboot.log`      |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Logs techniques  | `/data/log/springboot/[environnement]/[project.artifactId]/[project.artifactId]_[port]/[hostname]_[environnement]_[project.artifactId]_[port]_undertow_access.log` |

L'environnement, le hostname et le port sont valorisés au démarrage de l'application par le système.
Voir le paragraphe [Execution en environnement serveur](/doc/architecture-technique.md) pour plus d'information.
