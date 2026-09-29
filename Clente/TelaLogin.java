import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* ************************************************************************************************
* Autor............: Vinicius Castro Moreira
* Matricula........: 202310357
* Inicio...........: 01/07
* Ultima alteracao.: 05/07 
* Nome.............: TelaLogin
* Funcao...........: Gerencia a GUI da tela de login
************************************************************************************************ */
public class TelaLogin {
  private AnchorPane pane;
  private TextField nomeUsuario;
  private TextField IP;
  private Button entrar;
  private Scene scene;
  private static Stage stage;
  private VBox vBox;
  private final int ESPACAMENTO = 10;
  
  public TelaLogin(Stage stage) {
    TelaLogin.stage = stage;
    setScene();
  } // fim construtor

  private void iniciarComponentes() {
    pane = new AnchorPane();
    pane.setPrefSize(400, 300);
    nomeUsuario = new TextField();
    nomeUsuario.setPromptText("Nome de usuário");
    IP = new TextField();
    IP.setPromptText("IP do servidor (ex: 127.0.0.0)");
    entrar = new Button("Entrar");
    vBox = new VBox();
    vBox.getChildren().addAll(nomeUsuario, IP, entrar);
    vBox.setSpacing(ESPACAMENTO);
    pane.getChildren().add(vBox);
    scene = new Scene(pane);
  } // fim iniciarComponentes

  private void iniciarLayout() {
    vBox.setLayoutX((pane.getPrefWidth() - vBox.getWidth()) / 2);
    vBox.setLayoutY((pane.getPrefHeight() - vBox.getHeight()) / 2);
    entrar.setMinWidth(vBox.getWidth());
    pane.requestFocus();
  } // fim iniciarLayout

  private void iniciarOuvintes() {
    entrar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        validarLogin();
      }
    });
    stage.setOnCloseRequest(e -> fecharPrograma());
  }// fim iniciarOuvintes

  private void fecharPrograma() {
    stage.close();
    System.exit(0);
  }// fim fecharPrograma
  
  /************************************************************************************************
  * Metodo: validarLogin
  * Funcao: valida o login na aplicacao
  * Parametros: 
  * Retorno: void
  ********************************************************************************************** */
  private void validarLogin() {
    Controller.login(nomeUsuario.getText(), IP.getText());
    new TelaInicial(stage);
  } // fim validarLogin

  private void setScene() {
    iniciarComponentes();
    iniciarOuvintes();
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("Login");
    stage.show();
    iniciarLayout();
  } // fim setScene
} // fim class
