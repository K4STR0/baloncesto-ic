# DOCUMENTACIÓN

## 0. Punto de partida

Como punto de partida se ha realizado la práctica del tema 5, quedando en el repositorio de github, que se muestra a continuación, registrado todo el proceso:

[Enlace al repositorio de GitHub](https://github.com/K4STR0/baloncesto-ic)

Y el resultado de la aplicación desplegado en la siguiente URL en Azure:

[Enlace a la aplicación en Azure](https://baloncesto-castro.azurewebsites.net/Baloncesto/index.html)

## 1. Creación de un nuevo job - Stage

Se ha creado un nuevo job llamado stage en el workflow de GitHub Actions, para ello se ha modificado el fichero main.yaml incluyendo el siguiente contendo.

```yaml
stage:
  runs-on: ubuntu-latest
  needs: qa
  if: github.ref == 'refs/heads/main'
  steps:
    - name: Descargar repositorio
      uses: actions/checkout@v3
    - name: Crear el archivo .war
      run: |
        mvn package -DskipTests=true
    - name: Desplegar en Azure
      uses: Azure/webapps-deploy@v2
      with:
        app-name: baloncesto-castro-pre
        publish-profile: ${{ secrets.AZURE_WEBAPP_PUBLISH_PROFILE_PRE }}
        package: target/*.war
```

En esta configuración se indica que necesita esperar a que termine el job "qa", y que solo se ejecutará si es un commit en la rama main, rama principal del proyecto.

En cuanto a los pasos se establece que descargue el repositorio y construya el war. Tras ello desplegará el fichero war en una aplicación de Azure previamente creada, llamada baloncesto-castro-pre. Además, se ha añadido un nuevo secreto en el repositorio de github llamado "AZURE_WEBAPP_PUBLISH_PROFILE_PRE" con el perfil de publicación de esta aplicación de Azure.

Finalmente se ha modificado también el job de producción para que ya no tenga que esperar al job "qa" sino a este nuevo job "stage":

```yaml
deploy:
  runs-on: ubuntu-latest
  needs: stage
```

Como se puede apreciar, tras hacer un commit a la rama main con las modificaciones del fichero yaml, el workflow ahora tiene un job más:

![Workflow con el job "Stage"](/images/workflow-stage.png)

Tras completarse el flujo la página queda desplegada en el entorno de preproducción en el siguiente enlace:

[Enlace a la aplicación en preproducción](https://baloncesto-castro-pre.azurewebsites.net/Baloncesto/index.html)

## 2. Creación del nuevo sprint (milestone)

Desde la página del repositorio en GitHub se accede a la sección Issues y posteriormente a Milestones, donde se pulsa el botón de crear nuevo milestone y se configura con el nombre y la descripción.

![Nuevo milestone vacío](/images/nuevo-milestone.png)

A continuación se crea un nuevo issue para cada punto especificado en el enunciado, estos issues estarán asociados tanto al milestone que se acaba de crear como al proyecto "Baloncesto" creado en el punto 0. Además están asignados al único desarrollador de esta aplicación, que soy yo.

![Milestone configurado con los issues](/images/milestone-configurado.png)

Además, el proyecto "Baloncesto" del repositorio, tras mover los issues de "No asignados" a "Todo", queda de la siguiente forma:

![Proyecto tras crear milestone](/images/proyecto-tras-crear-milestone.png)
