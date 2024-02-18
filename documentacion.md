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

## 3. Configuración SonarQube

Antes de comenzar a desarrollar es necesario configurar SonarQube para que si el proyecto tiene más major issues que 20 no se pueda desplegar la aplicación a producción.

Para realizar esta configuración accedemos al panel de administración de SonarQube y en la pestaña Quality Gates modificamos el valor "Major Issues is greater than" a 20:

![SonarQube major issues a 20](/images/sonarqube-major-20.png)

De esta forma el job "qa" del workflow fallará si los Major Issues son mayores a 20, y el equipo de aprovación deberá rechazarlo al ver que ha fallado el workflow, evitando que pase a producción.

Finalmente marcamos este issue "QA" como cerrado.

## 4. REQ-1 Poner votos a cero

Antes de comenzar a desarrollar la nueva funcionalidad del requisito 1, se crea una nueva rama en el repositorio llamada REQ-1\_\_poner-votos-a-cero. Además, como se va a empezar a trabajar con el issue REQ-1, este pasa al estado "In Progress" en el proyecto del repositorio.

![REQ-1 en progreso](/images/req-1-in-progress.png)

Tras haber realizado un commit con la implementación del botón que pone los votos a cero y la corrección de algunos major issues, el resultado del workflow para este commit es satisfactorio, habiendo pasado también el job de qa:

![Workflow añadir votos](/images/workflow-añadir-votos.png)

Ahora que esta tarea está terminada se cierra el Issue, y se mueve la tarea PU (Realizar caso de prueba para actualizarJugador) a la columna "In progress":

![Proyecto tras REQ-1](/images/proyecto-tras-REQ-1.png)

Tras subir el commit con el test simulando la comprobación de que se incrementen los votos en 1 cuando se ejecuta actualizarJugador(), y comprobar que pasa los checks que debería, se crea una pull request contra la rama main y se mergea.

Además, se cierra el issue y se mueve a "Done".

Tras mergearse contra main comienza un nuevo workflow donde se desplegará la aplicación en el servidor de preproducción, y tras comprobar que funciona correctamente se da la aprovación para incluirla en el servidor de producción.

Así se ve la página en preproducción con el botón nuevo (bajo el título de la página) para resetear votos:

![Nuevo botón](/images/pre-tras-req-1.png)

Y este es el resultado de SonarQube:

![Sonar tras REQ-1](/images/sonar-tras-req-1.png)

Por lo tanto, se puede autorizar el paso de la aplicación a producción.

## 4. REQ-2 Visualización de votos

Tras la implementación del primer requisito y su fusión con la rama main, se continúa con la visualización, para ello se crea una rama llamada REQ-2\_\_ver-votos, y se añade a la columna "In progress" del proyecto el issue REQ-2.

Tras la implementación de la página de resultados y el botón en la página principal así es como se ve la página:

![Pagina tras REQ-2](/images/pagina-tras-req-2.png)

Y esta es la página de los resultados:

![Resultados tras REQ-2](/images/votaciones-tras-req-2.png)

Como se puede apreciar la página se muestra en la IP 192.168.1.147, esto es por que estoy ejecutando el contenedor de ubuntu donde se ejecutan los workflows "test" y "qa" en una máquina Windows en mi red local, distinta al PC donde estoy desarrollando, ya que mi PC personal tiene una arquitectura ARM64 y la imagen de ubuntu era x86-64, lo que provocaba problemas si lo ejecutaba bajo ARM64. Concretamente el problema que tenía era que se bloqueaban los jobs sin ningún motivo.

Tras terminar de implementar esta funcionalidad se cierra el issue REQ-2 y se añaden los tests PF-A y PF-B a "In progress".

