/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorcalc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
public class ServidorCalc {

    public static void main(String[] args) {
      int puerto = Integer.parseInt(JOptionPane.showInputDialog("Introduce el puerto?"));

        try {
            //Socket establecido
            System.out.println("Iniciando socket servidor");
            ServerSocket serverSocket = new ServerSocket(puerto);

            // A la espera
            while (true) {
                System.out.println("Esperando conexiones");
                Socket newSocket = serverSocket.accept();
                //crea thread
                new ClienteConectado(newSocket).start();
            }
        } catch (IOException ex) {
            System.out.println("Error en conexiones entrantes");
        }
    }
}
//Cliente conectado
class ClienteConectado extends Thread {
//Transferencia de datos
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public ClienteConectado(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        System.out.println("Conexión recibida");
    }
    //Metodo Sumar
    public static double sumar(double n1, double n2) {
        System.out.println("Sumar " + n1 + " + " + n2);
        return n1 + n2;
    }
    //Metodo Restar
    public static double restar(double n1, double n2) {
        System.out.println("Resta " + n1 + " - " + n2);
        return n1 - n2;
    }
    //Metodo Multiplicar
    public static double multiplicar(double n1, double n2) {
        System.out.println("Multiplicar " + n1 + " * " + n2);
        return n1 * n2;
    }
    //Metodo Dividir
    public static double dividir(double n1, double n2) {
        System.out.println("Dividir " + n1 + " / " + n2);
        return n1 / n2;
    }

    //Metodo Raiz cuadrada
    public static double raizCuadrada(double n1) {
        System.out.println("raizCuadrada " + n1 + "√");
        double numRaiz = n1;
        return Math.sqrt(numRaiz);
    }

    @Override
    
    //Metodo que se ejecuta y recibe los datos. Notifica los datos por consola
    public void run() {
        try {
            
            byte[] datoRecibido = new byte[25];
            is.read(datoRecibido);
            System.out.println("Recibido: " + new String(datoRecibido));

            String[] operacion = new String(datoRecibido).split(" ");
            double resultado = 0;
            //equalsIgnoreCase sirve para realizar el split de manera correcta
            if (operacion[0].equalsIgnoreCase("Cerrar")) {
                socket.close();
            } else {
                //Selector de operaciones
                switch (operacion[1]) {
                    
                    case "+":
                        resultado = sumar(Double.valueOf(operacion[0]), Double.valueOf(operacion[2]));
                        break;
                        
                    case "-":
                        resultado = restar(Double.valueOf(operacion[0]), Double.valueOf(operacion[2]));
                        break;
                        
                    case "*":
                        resultado = multiplicar(Double.valueOf(operacion[0]), Double.valueOf(operacion[2]));
                        break;
                        
                    case "/":
                        resultado = dividir(Double.valueOf(operacion[0]), Double.valueOf(operacion[2]));
                        break;
                        
                    case "√":
                        resultado = raizCuadrada(Double.valueOf(operacion[0]));
                        break;
                }
            }
            //Notificaciones
            System.out.println("Envia " + resultado);
            String mensajeEnviado = String.valueOf(resultado);
            os.write(mensajeEnviado.getBytes());
            System.out.println("Enviado");
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                //Cierra la conexion del socket
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }
}
