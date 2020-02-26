# TenPinBowling-WebLogic
Author: Carlos Baene - Bogotá, Colombia (2020)

# Información y características

Esta aplicación está orientada a la web. La presentación es JSF (Java Server Faces). Se usa el framework PrimeFaces como apoyo para el front. Uso de JDK V8. El path de la aplicación es: '../TenPinBowling' -> El dominio y puerto pueden variar. Los archivos permitidos para cargar deben ser de extensión (.txt).

# Requerimientos previos

1. Instalar el servidor de aplicaciones WebLogic para el despliegue del proyecto
1.1. Tutorial para instalación en windows: 
1.2. Tutorial para instalación en linux: 

2. Integrar el servidor de aplicaciones WebLogic en algún IDE (Preferencia: NetBeans)
2.1. Tutorial para creación de servidor WebLogic en NetBeans: 

3. (Opcional) Subir manualmente los archivos ear en el servidor.

# Instrucciones de ejecución y despliegue

1. Bajar las dependencias maven que se encuentran en el archivo: pom.xml.
1.1. Si usa NetBeans: Click derecho al proyecto -> Cleand.

2. Compilar el proyecto y generar el archivo EAR.
2.1. Si usa NetBeans: Click derecho al proyecto -> Clean and Build.

3. Al compilar se creará la carpeta "target". Desplegar en el servidor de aplicaciones WebLogic el archivo EAR que se encuentra allí.
3.1. Si usa NetBeans: Click derecho al proyecto -> Debug.

4. Abrir el navegador en la ruta: 'http:localhost:7001/TenPinBowling'
4.1. El dominio y el puerto pueden variar de acuerdo a la instalación.
4.2. Si usa NetBeans: El proyecto desplegará automáticamente en el navegador web configurado en el IDE.

5. Se mostrará la pantalla inicial. Presionar el botón para ingresar a la aplicación.
5.1. Será redirigido a la pantalla de juego.
5.2. Escoger el archivo deseado presionando el botón "Cargar Archivo". Si escoge otra extensión el sistema tomará como que no ha cargado el archivo necesario.
5.3. Dar click en el botón "Procesar Archivo" para realizar las respectivas validaciones de estructura y contenido.
5.4. Si el archivo es procesado correctamente se habilitará el botón "Jugar".
5.5. Dar click en el botón "Jugar" para realizar el procesamiento del juego y ver la puntuación final.
5.6. Se podrá ver el resultado final del juego actual. También se puede ver una tabla con el histórico de los juegos que se hayan realizado.