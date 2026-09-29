import java.io.*;
import java.net.*;

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: Servidor
* Funcao...........: Recebe as mensagens dos cliente e gerencia os grupos
************************************************************************************************ */
public class Servidor {

  /************************************************************************************************
  * Metodo: servidorTCP
  * Funcao: inicia a thread do servidor TCP
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void servidorTCP() {
    ServidorTCP servidorTCP = new ServidorTCP();
    servidorTCP.start();
  }// fim servidorTCP

  /************************************************************************************************
  * Metodo: servidorUDP
  * Funcao: Inicia a thread do servidor UDP
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void servidorUDP() {
    ServidorUDP servidorUDP = new ServidorUDP();
    servidorUDP.start();
  }// fim servidorUDP

  /************************************************************************************************
  * Metodo: clienteUDP
  * Funcao: Envia o datagrama 
  * Parametros: mensagem = APDU; IP = IP para ser enviado
  * Retorno: void
  ********************************************************************************************** */
  public static void clienteUDP(String mensagem, String IP) throws Exception {
    int portaLocal = 6790;
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
}// fim class

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: ServidorTCP
* Funcao...........: Mantem o loop do servidor TCP
************************************************************************************************ */
class ServidorTCP extends Thread {
  public void run() {
    try {
      int portaLocal = 6789;
      ServerSocket servidor = new ServerSocket(portaLocal);
      
      while (true) {
        Socket conexao = null;
        conexao = servidor.accept();
        ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());
        String mensagem = (String)entrada.readObject();
        mensagem += "#" + conexao.getInetAddress().getHostAddress();
        System.out.println(mensagem);
        TratamentoTCP tratamento = new TratamentoTCP(mensagem);
        tratamento.start();
      }// fim while
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
* Nome.............: TratamentoTCP
* Funcao...........: Trata as mensagens enviadas pelo TCP em outra thread
************************************************************************************************ */
class TratamentoTCP extends Thread {
  private String mensagem;
  public TratamentoTCP(String mensagem) {
    this.mensagem = mensagem;
  } // fim construtor

  public void run() {
    Controller.tratarMensagemTCP(mensagem);
  } // fim run
} // fim class

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: TratamentoUDP
* Funcao...........: Trata as mensagens enviadas pelo UDP em outra thread
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

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: ServidorUDP
* Funcao...........: Mantem o loop do servidor UDP
************************************************************************************************ */
class ServidorUDP extends Thread {
  public void run() {
    try {
      int portaLocal = 6789;
      DatagramSocket servidor = new DatagramSocket(portaLocal);
      byte[] dadosRecebidos = new byte[1024];
      while(true) {
        DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos,
		                                                        dadosRecebidos.length);
        servidor.receive(pacoteRecebido);
        String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
        mensagem += "#" + pacoteRecebido.getAddress().getHostAddress();
		    System.out.println(mensagem+" Classe servidor");
        TratamentoUDP tratamento = new TratamentoUDP(mensagem);
        tratamento.start();
	    }
    } catch (Exception e) {
      e.getMessage();
    } // fim try catch
  } // fim run
}// fim class
