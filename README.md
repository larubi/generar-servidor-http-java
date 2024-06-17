# generar-servidor-http-java
Ejercicio
Crearemos un servidor HTTP similar al del ejemplo del servidor HTTP (Proyecto java ServerHTTP, apartado 5.1 de los contenidos) Pero un servidor HTTP realista tendrá que atender varias peticiones simultáneamente. Para ello, tenemos que ser capaces de modificar su código para que pueda utilizar varios hilos de ejecución. Esto se hace de la siguiente manera:
El hilo principal (o sea, el que inicia la aplicación) creará el socket servidor que permanecerá a la espera de que llegue alguna petición.
Cuando se reciba una, la aceptará y le asignará un socket cliente para enviarle la respuesta. Pero en lugar de atenderla él mismo, el hilo principal creará un nuevo hilo para que la despache por el socket cliente que le asignó. De esta forma, podrá seguir a la espera de nuevas peticiones.
Por lo tanto modifica el ejemplo del servidor HTTP (Proyecto java ServerHTTP, apartado 5.1 de los contenidos) para que implemente multihilo, y pueda gestionar la concurrencia de manera eficiente.
