import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: Controller
* Funcao...........: Faz o login gerencia a GUI e APDUs
************************************************************************************************ */
public class Controller {
  private static String nomeDeUsuario;
  private static String IPServidor;
  private static List<Grupo> grupos;
  private static String grupoAtivo = "";
  private static TelaInicial telaInicial;

  /************************************************************************************************
  * Metodo: login
  * Funcao: Realiza o login na aplicacao
  * Parametros: nome = nome de usuario; IP = ip do servidor
  * Retorno: void
  ********************************************************************************************** */
  public static void login(String nome, String IP) {
    nomeDeUsuario = nome;
    if (IP.equals("")) {
      IPServidor = "127.0.0.0";
    } else {
      IPServidor = IP;
    }
    grupos = new ArrayList<>();
    System.out.println(nomeDeUsuario + " logou com sucesso! (teste01)");
    Cliente.servidorUDP();
    //new TelaTeste2();
  }// fim login

  /************************************************************************************************
  * Metodo: join
  * Funcao: Envia as APDUs tipo JOIN
  * Parametros: nomeGrupo = grupo para qual sera enviado
  * Retorno: void
  ********************************************************************************************** */
  public static void join(String nomeGrupo) {
    String apdu = "JOIN#" + nomeDeUsuario + "#" + nomeGrupo;
    try {
      Cliente.clienteTCP(apdu, IPServidor);
    } catch (Exception e) {
      e.getMessage();
    }
    grupos.add(new Grupo(nomeGrupo));
  }// fim join

  public static List<Grupo> getGrupos() {
    return grupos;
  } // fim getGrupos

  public static String getGrupoAtivo() {
    return grupoAtivo;
  } // fim getGrupoAtivo

  /************************************************************************************************
  * Metodo: send
  * Funcao: Envia as APDUs tipo SEND
  * Parametros: mensagem = texto a ser enviado
  * Retorno: void
  ********************************************************************************************** */
  public static void send(String mensagem) {
    Grupo grupo = buscarGrupo(grupoAtivo);
    String apdu = grupo.getNome() + "#" + nomeDeUsuario + "#" + mensagem;
    try {
      Cliente.clienteUDP(apdu, IPServidor);
    } catch (Exception e) {
      e.getMessage();
    }
    grupo.getMensagens().add("(Você): " + mensagem);
  } // fim send

  /************************************************************************************************
  * Metodo: leave
  * Funcao: Envia as APDUs tipo LEAVE e sai do grupo no cliente
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  public static void leave() {
    Grupo grupo = buscarGrupo(grupoAtivo);
    grupoAtivo = "";
    String apdu = "LEAVE#" + nomeDeUsuario + "#" + grupo.getNome();
    try {
      Cliente.clienteTCP(apdu, IPServidor);
    } catch (Exception e) {
      e.getMessage();
    }
    grupos.remove(grupo);    
  } // fim leave

  /************************************************************************************************
  * Metodo: buscaGrupo
  * Funcao: Pesquisa grupo pelo nome na lista
  * Parametros: nome = nome do grupo
  * Retorno: Grupo
  ********************************************************************************************** */
  public static Grupo buscarGrupo(String nome) {
    int indexGrupo = 0;
    for (Grupo grupo : grupos) {
      if (grupo.getNome().equals(nome)) {
        break;
      }
      indexGrupo++;
    }
    return grupos.get(indexGrupo);
  } // fim buscarGrupo

  /************************************************************************************************
  * Metodo: tratarMensagemUDP
  * Funcao: Trata as mensagens enviadas pelo servidor
  * Parametros: mensagem = APDU SEND
  * Retorno: void
  ********************************************************************************************** */
  public static void tratarMensagemUDP(String mensagem) {
    String[] apdu = mensagem.split("#");
    int indexGrupo = buscarIDGrupo(apdu[0]);
    grupos.get(indexGrupo).getMensagens().add("(" + apdu[1] + "): " + apdu[2]);
    Platform.runLater(() -> {
      telaInicial.mensagemRecebida();
    });
  } // fim tratarMensagemUDP

  /************************************************************************************************
  * Metodo: buscaIDGrupo
  * Funcao: Pesquisa grupo pelo nome na lista
  * Parametros: nome = nome do grupo
  * Retorno: int
  ********************************************************************************************** */
  public static int buscarIDGrupo(String nome) {
    int indexGrupo = 0;
    for (Grupo grupo : grupos) {
      if (grupo.getNome().equals(nome)) {
        break;
      }
      indexGrupo++;
    }
    return indexGrupo;
  } // fim buscarGrupo

  /************************************************************************************************
  * Metodo: ouvinteMensagem
  * Funcao: recebe referencia da tela
  * Parametros: tela = referencia da tela
  * Retorno: void
  ********************************************************************************************** */
  public static void ouvinteMensagem(TelaInicial tela) {
    Controller.telaInicial = tela;
  } // fim ouvinteMensagem

  /************************************************************************************************
  * Metodo: grupoAtivo
  * Funcao: Verifica grupo selecionado pelo usuario
  * Parametros: gpAtivo = nome do grupo
  * Retorno: void
  ********************************************************************************************** */
  public static void grupoAtivo(String gpAtivo) {
    grupoAtivo = gpAtivo; 
  } // fim grupoAtivo
}// fim class
