import java.util.ArrayList;
import java.util.List;

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: Controller
* Funcao...........: Age no envio e tratamento de dados
************************************************************************************************ */
public class Controller {
  private static List<List<String>> grupos; // ED que armazena nome dos grupos e seus clientes
  
  /************************************************************************************************
  * Metodo: iniciarServidor
  * Funcao: Chama os metodos que inicial os servidores TCP e UDP
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void iniciarServidor() {
    grupos = new ArrayList<>();
    Servidor.servidorTCP();
    Servidor.servidorUDP();
  } // fim iniciarServidor

  /************************************************************************************************
  * Metodo: tratarMensagemUDP
  * Funcao: Trata as mensagens que chegam da APDU SEND
  * Parametros: mensagem = APDU SEND
  * Retorno: void
  ********************************************************************************************** */
  public static void tratarMensagemUDP(String mensagem) {
    String[] apdu = mensagem.split("#"); // divide os campos da APDU pela flag
    int indexGrupo = buscarIDGrupo(apdu[0]); // recebe o index do grupo da APDU 
    List<String> IPClientes = new ArrayList<>(); // lista de IPs para encaminhar mensagem
    for (String clientes : grupos.get(indexGrupo)) {
      if (!clientes.equals(apdu[0]) && !clientes.equals(apdu[1] + "#" + apdu[3])) {
        // adiciona o IP ah lista se for diferente de quem o enviou e do nome do grupo
        String[] cliente = clientes.split("#");
        IPClientes.add(cliente[1]);
      } // fim if
    } // fim for
    for (String ip : IPClientes) { // para cada IP da lista encaminha a mensagem
      System.out.println("Enviando mensagem para clientes");
      send(apdu[0] + "#" + apdu[1] + "#" + apdu[2], ip);
    } // fim for
  } // fim tratarMensagemUDP

  /************************************************************************************************
  * Metodo: buscarIDGrupo
  * Funcao: retorna o ID do grupo buscando se pelo nome
  * Parametros: nome = nome do grupo pesquisado
  * Retorno: int
  ********************************************************************************************** */
  public static int buscarIDGrupo(String nome) {
    int indexGrupo = 0;
    for (List<String> grupo : grupos) {
      if (grupo.get(0).equals(nome)) {
        break; // para quando encontra o grupo buscado
      }
      indexGrupo++;
    }
    return indexGrupo;
  } // fim buscarGrupo

  /************************************************************************************************
  * Metodo: send
  * Funcao: Encaminha a mensagem para os clientes
  * Parametros: mensagem = APDU; ip = ip para qual deve enviar
  * Retorno: void
  ********************************************************************************************** */
  public static void send(String mensagem, String ip) {
    try {
      Servidor.clienteUDP(mensagem, ip);
    } catch (Exception e) {
      e.getMessage();
    } // fim try catch
  } // fim send
  
  /************************************************************************************************
  * Metodo: tratarMensagemTCP
  * Funcao: Trata as mensagens que chegam da APDU JOIN e LEAVE via TCP
  * Parametros: mensagem = APDU JOIN ou LEAVE
  * Retorno: void
  ********************************************************************************************** */
  public static void tratarMensagemTCP(String mensagem) {
    String[] apdu = mensagem.split("#"); // divide a APDU
    if (apdu[0].equals("JOIN")) { // se for JOIN
      int indexGrupo = 0;
      if (!grupos.isEmpty()) { // se a lista nao estiver vazia
        for (List<String> grupo : grupos) {
          if (grupo.get(0).equals(apdu[2])) {
            break;
          } // fim if
          indexGrupo++; // caso o grupo nao esteja na lista o contador ficara maior que o tamanho
        } // fim for
      } // fim if
      if (indexGrupo < grupos.size()) { // se o grupo estah na lista
        grupos.get(indexGrupo).add(apdu[1] + "#" + apdu[3]); // adiciona o cliente a ela
      } else { // caso nao esteja na lista
        List<String> novoGrupo = new ArrayList<>(); // cria nova lista ou seja grupo
        novoGrupo.add(apdu[2]); // o nome do grupo eh adicionado como primeiro elemento da lista
        grupos.add(novoGrupo); // o grupo eh adicionado a lista de grupos
        System.out.println(apdu[1]+" criou grupo "+apdu[2]+" (teste 2)");
        grupos.get(indexGrupo).add(apdu[1] + "#" + apdu[3]); // o cliente eh adicionado no grupo
      } // fim if else
      System.out.println(apdu[1]+" entrou no grupo "+apdu[2]+" (teste 3)");
    } else if (apdu[0].equals("LEAVE")) { // se eh do tipo LEAVE
      int indexGrupo = buscarIDGrupo(apdu[2]); // busca o index do grupo
      grupos.get(indexGrupo).remove(apdu[1] + "#" + apdu[3]); // remove o cliente do grupo
      System.out.println(apdu[1]+" saiu do grupo "+apdu[2]+" (teste 4)");
      if (grupos.get(indexGrupo).size() == 1) { // verifica se nao a mais clientes no grupo
        grupos.remove(indexGrupo); // remove o grupo da lista
        System.out.println("Grupo "+apdu[2]+" foi removido (teste 2)");
      } // fim if
    } // fim if else
  } // fim tratarMensagemTCP
} // fim class
