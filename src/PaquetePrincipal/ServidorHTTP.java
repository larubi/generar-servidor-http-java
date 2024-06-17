package PaquetePrincipal;

import java.io.BufferedReader;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * *****************************************************************************
 * Servidor HTTP que atiende peticiones de tipo 'GET' recibidas por el puerto 
 * 8066
 *
 * NOTA: para probar este código, comprueba primero de que no tienes ningún otro
 * servicio por el puerto 8066 (por ejemplo, con el comando 'netstat' si estás
 * utilizando Windows)
 *
 * @author IMCG
 */
class ServidorHTTP {
    

//DATOS MIOS----------------------------------------------------------------------
    //constante puerto para un mejor manejo 
    private static final int PORT=8066; // Puerto en el que el servidor escucha
    //tamaño de la pool de hilos
    private static final int THREAD_POOL_SIZE = 10;  // Tamaño del pool de hilos
//---------------------------------------------------------------------------------   
    
  public static void main(String[] args) throws IOException, Exception {

    //Asociamos al servidor el puerto 8066
    ServerSocket socServidor = new ServerSocket(8066);
    imprimeDisponible();
    
    
      //DATOS MIOS ------------------------------------------------------------------ 
    //para crear un pool de hilos del tamaño definido en THREAD_POOL_SIZE.
     ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
  //---------------------------------------------------------------------------------
  
   // Socket socCliente; COMENTO ESTA VARIABLE Y LA DECLARO HE INICIALIZO JUNTO, ABAJO

    //ante una petición entrante, procesa la petición por el socket cliente
    //por donde la recibe
    while (true) {
      //a la espera de peticiones
      Socket  socCliente = socServidor.accept();
      //atiendo un cliente
      System.out.println("Atendiendo al cliente "); 
      
    //DATOS MIOS-------------------------------------------------- 
       threadPool.execute(() -> {
                try {
                    procesaPeticion(socCliente);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socCliente.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
      
 //----------------------------------------------------------------------     
    
    }
  }

  /**
   *****************************************************************************
   * procesa la petición recibida
   *
   * @throws IOException
   */
  private static void procesaPeticion(Socket socketCliente) throws IOException {
    //variables locales
    String peticion;
    String html;

    //Flujo de entrada
    InputStreamReader inSR = new InputStreamReader(
            socketCliente.getInputStream());
    //espacio en memoria para la entrada de peticiones
    BufferedReader bufLeer = new BufferedReader(inSR);

    //objeto de java.io que entre otras características, permite escribir 
    //'línea a línea' en un flujo de salida
    PrintWriter printWriter = new PrintWriter(
            socketCliente.getOutputStream(), true);

    //mensaje petición cliente
    peticion = bufLeer.readLine();

    //para compactar la petición y facilitar así su análisis, suprimimos todos 
    //los espacios en blanco que contenga
    peticion = peticion.replaceAll(" ", "");

    //si realmente se trata de una petición 'GET' (que es la única que vamos a
    //implementar en nuestro Servidor)
    if (peticion.startsWith("GET")) {
      //extrae la subcadena entre 'GET' y 'HTTP/1.1'
      peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));

      //si corresponde a la página de inicio
      if (peticion.length() == 0 || peticion.equals("/")) {
        //sirve la página
        html = Paginas.html_index;
        printWriter.println(Mensajes.lineaInicial_OK);
        printWriter.println(Paginas.primeraCabecera);
        printWriter.println("Content-Length: " + (html.length() + 1));
        printWriter.println("\n");
        printWriter.println(html);
      } //si corresponde a la página del Quijote
      else if (peticion.equals("/quijote")) {
        //sirve la página
        html = Paginas.html_quijote;
        printWriter.println(Mensajes.lineaInicial_OK);
        printWriter.println(Paginas.primeraCabecera);
        printWriter.println("Content-Length: " + (html.length() + 1));
        printWriter.println("\n");
        printWriter.println(html);
      } //en cualquier otro caso
      else {
        //sirve la página
        html = Paginas.html_noEncontrado;
        printWriter.println(Mensajes.lineaInicial_NotFound);
        printWriter.println(Paginas.primeraCabecera);
        printWriter.println("Content-Length: " + (html.length() + 1));
        printWriter.println("\n");
        printWriter.println(html);
      }
    
    }
  }

  /**
   * **************************************************************************
   * muestra un mensaje en la Salida que confirma el arranque, y da algunas
   * indicaciones posteriores
   */
  private static void imprimeDisponible() {

    System.out.println("El Servidor WEB se está ejecutando y permanece a la "
            + "escucha por el puerto 8066.\nEscribe en la barra de direcciones "
            + "de tu explorador preferido:\n\nhttp://localhost:8066\npara "
            + "solicitar la página de bienvenida\n\nhttp://localhost:8066/"
            + "quijote\n para solicitar una página del Quijote,\n\nhttp://"
            + "localhost:8066/q\n para simular un error");
  }
}
