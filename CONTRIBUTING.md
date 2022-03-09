
# Guide des bonnes pratiques de contribution au projet

Cette page présente les bonnes pratiques pour la contribution au projet.

## LeadDevs
Les leaddevs du projet sont les membres de l'équipe Architecture et Technologie:
* Charlie Martinet (martinet-c)
* Kenny Bolivard (bolivard-k)
* Stéphane Philippart (philippart-p)

## Propositions d'évolution
Par évolution, nous entendons correctif de bug, complément de documentation, ou amélioration ou nouvelle fonctionnalité.
Une proposition doit être soumise via une issue. <br>
Le développeur à l'origine d'une proposition est encouragé à soumettre une pull request référençant l'issue.

## Contribution
Les évolutions se font exclusivement sur des branches créées pour l'occasion et soummises à la communauté par pull request. 
Le projet peut être forké pour soumettre la branche.

### Pull request
Une pull request doit idéalement référencer une issue.
Toutes les PRs doivent être revue par au moins 1 des leaddevs.

### Merge
Le merge de la PR sera réalisé par le leaddev reviewer.
Après chaque merge, la branche sera supprimée.

### Conventions
| Catégories    |  Conventions                                                                                                                                                                        |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Branches      |  **feature**/xxx<br> **bug**/xxx<br> **quality**/xxx (pour la documentation, amélioration du code, etc.)<br> **test**/xxx (pour les branches qui n'ont pas vocation à être mergées) |
| Issues        | **[FEATURE]** xxx<br> **[BUG]** xxx<br> **[QUALITY]** xxx<br> **[TEST]** xxx                                                                                                        |
| Pull requests | nommage libre, avec les labels suivants :<br> * WIP (fond rouge)<br> * Ready To Merge (fond bleu)                                                                                   |

