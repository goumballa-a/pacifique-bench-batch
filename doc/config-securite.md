# Configuration et fonctionnement de la sécurité

## Utilisation
La sécurité est préconfigurée dans le [fwk-web-hm-api](http://pichm/artifactory/webapp/#/artifacts/browse/tree/General/m2-hm-snapshot-local/fr/hm/fwk/fwk-web-hm-api).
Pour plus de détail, voir le [README.md du framework](https://github.com/hm-it/fwk-web-hm-api/releases/latest).

## JWT

### Configuration
La configuration JWT est positionnée  dans le fichier [application.properties](cfg/src/main/resources/).
Voir [Configuration de l’application](config-application.md).

La liste suivante détaille les clés nécessaires pour la configuration JWT :

| Clé                                                | Description                        |
|----------------------------------------------------|------------------------------------|
| security.authentication.jwt.secret                 | Clef secrète pour signer le jeton. |
| security.authentication.jwt.tokenValidityInSeconds | Durée de validité du jeton.        |

### Configuration
La configuration LDAP est positionnée dans le fichier [application.properties](cfg/src/main/resources/).
Voir [Configuration de l’application](config-application.md).

La liste suivante détaille les clés nécessaires pour une délégation d'authentification à l'AD :

| Clé                                    | Description                                                                                                                  |
|----------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| security.ldapConfig.enabled            | Activation/désactivation de la configuration.                                                                                |
| security.ldapConfig.server             | Nom d'hôte ou adresse IP du serveur LDAP.                                                                                    |
| security.ldapConfig.port               | Port sur lequel le serveur LDAP écoute.                                                                                      |
| security.ldapConfig.connectionName     | Nom distinctif (Distinguished Name) de l'utilisateur LDAP que l'application doit employer pour se connecter au serveur LDAP. |
| security.ldapConfig.connectionPassword | Mot de passe permettant de se connecter au serveur LDAP                                                                      |
| security.ldapConfig.projectRoles*      | Définition des rôles, spécifique à l'application.                                                                            |

### Compte de service d'interrogation de l'AD
Chaque application doit utiliser un compte de service qui lui est spécifique pour interroger l'AD.
 
    Il faut prévoir 2 comptes: un pour les environnements hors production et un pour la production.
      
Pour demander la création de ce compte de service, il faut faire une EV _**Création de compte AD Spéciaux**_.

:warning: :warning: :warning: :warning: :warning: 
Le compte proposé par le seed n'est pas à utiliser dans les applications. Il n'est là que pour exemple.
**Il est interdit de l'utiliser autrement que dans ce seed.**