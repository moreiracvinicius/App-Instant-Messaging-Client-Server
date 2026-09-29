import java.io.*;
import java.net.*;

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: Cliente
* Funcao...........: Envia e recebe e trata as APDUs
************************************************************************************************ */
public class Cliente {
  private static int portaLocal = 6789;
  
  /************************************************************************************************
  * Metodo: clienteTCP
  * Funcao: Envia as APDUs do tipo JOIN e LEAVE
  * Parametros: mensagem = APDU; IP = ip para ser enviado
  * Retorno: void
  ********************************************************************************************** */
  public static void clienteTCP(String mensagem, String IP) throws Exception {
    InetAddress ipServidor = InetAddress.getByName(IP);
    Socket cliente = new Socket(ipServidor, portaLocal);
    ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
    saida.writeObject(mensagem);
    saida.flush();
  } // fim clienteTCP

  /************************************************************************************************
  * Metodo: servidorUDP
  * Funcao: Inicia a thread de servidor UDP
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void servidorUDP() {
    ServidorUDP servidorUDP = new ServidorUDP();
    servidorUDP.start();
  }// fim servidorUDP

  /************************************************************************************************
  * Metodo: clienteUDP
  * Funcao: Envia as APDUs tipo SEND via datagrama
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void clienteUDP(String mensagem, String IP) throws Exception {
    InetAddress ipServidor = InetAddress.getByName(IP);
	  DatagramSocket clienteSocket = new DatagramSocket();
    byte[] dadosEnviados = new byte[1024];
    dadosEnviados = mensagem.getBytes();
    DatagramPacket datagramaEnviado = new DatagramPacket(dadosEnviados,
                                                          dadosEnviados.length,
                                                          ipServidor,
                                                          portaLocal);
    clienteSocket.send(datagramaEnviado);
  } // fim clienteUDP
} // fim class

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: Servidor UDP
* Funcao...........: Mantem um loop que recebe as mensagens do servidor
************************************************************************************************ */
class ServidorUDP extends Thread {
  public void run() {
    try {
      int portaLocal = 6790;
      DatagramSocket servidor = new DatagramSocket(portaLocal);
      byte[] dadosRecebidos = new byte[1024];
      while(true){
        DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos,
		                                                        dadosRecebidos.length);
        servidor.receive(pacoteRecebido);
        String mensagem = new String(pacoteRecebido.getData());
        mensagem += "#" + pacoteRecebido.getAddress().getHostAddress();
		    //System.out.println(mensagem);
        TratamentoUDP tratamento = new TratamentoUDP(mensagem);
        tratamento.start();
	    } // fim while
    } catch (Exception e) {
      e.getMessage();
    } // fim try catch
  } // fim run
}// fim class

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: TratamentoUDP
* Funcao...........: Trata as mensagens do UDP em outra thread
************************************************************************************************ */
class TratamentoUDP extends Thread {
  private String mensagem;
  public TratamentoUDP(String mensagem) {
    this.mensagem = mensagem;
  } // fim construtor

  public void run() {
    Controller.tratarMensagemUDP(mensagem);
  } // fim run
} // fim class