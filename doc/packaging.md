# Packaging

## Outils
Maven est l’outil retenu pour le packaging des applications Spring Boot au format **Fat Jar** (= Jar exécutable).

La génération du Fat Jar est assurée par le plugin Maven **spring-boot-maven-plugin** qui est déclaré dans le [pom.xml](pom.xml) comme suit :

    <!-- Package as an executable jar -->
    <build>
        <plugins>
              <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
              </plugin>
        </plugins>
    </build>

> Le packaging d'une application Spring Boot en jar exécutable est une exigence production

## Liste des livrables
Une application Spring Boot Harmonie Mutuelle est composée des livrables suivants :
* Un .tar, non compressé et unique (multi environnement) qui contient un jar exécutable ;
    * Format : [artifactId]-app-[version]-exec.tar
    * Contenu :
        * [artifactId]-app-exec.tar
* Un .tar, non compressé et unique (multi environnement) qui contient les fichiers de configuration variabilisés.
    * Format : [artifactId]-cfg-[version].tar
    * Nommage des variables : [nom variable]
    * Contenu :
        * config/application.properties
        * config/logback.xml
        * config/banner.xml

## Information de build
Le livrable .jar doit contenir le fichier META-INF/build-info.properties qui liste des informations de base comme le numéro de version ou encore le nom du projet maven.
Ces informations sont accessibles via les endpoints Actuator HTTP `info` et JMX `infoEndpoint`.
Ce fichier est généré au moment du build par maven grâce à la configuration du plugin `spring-boot-maven-plugin` positionnée dans le pom.xml parent l’ajout des propriétés.

> La génération du fichier `META-INF/build-info.properties` est obligatoire.

## Modules embarqués dans l’appli
Afin d’avoir à tout moment la liste des modules embarqués dans l’application, le classpath complet (et donc les starters Spring Boot utilisés) est ajouté ajouter au fichier MANIFEST.
La configuration du plugin `maven-jar-plugin` est surchargée dans le [pom.xml du module app](app/pom.xml) de l’application comme suit :

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.0.2</version>
        <configuration>
            <archive>
                <index>true</index>
                <manifest>
                    <addClasspath>true</addClasspath>
                </manifest>
            </archive>
        </configuration>
    </plugin>

Il est possible d'accéder au contenu via un end-point
