package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Colecao;
import model.dao.ColecaoDaoJdbc;
import model.dao.DaoFactory;
import start.App;

public class PrincipalController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Colecao> tblCardGame;
    @FXML
    private TableColumn<Colecao, String> tblColNome;
    @FXML
    private TableColumn<Colecao, Double> tblColValor;
    @FXML
    private TableColumn<Colecao, String> tblColTipo;
    @FXML
    private TableColumn<Colecao, String> tblColCardGame;
    @FXML
    private TableColumn<Colecao, Boolean> tblColPossui;
    @FXML
    private Button btnIncluir;
    @FXML
    private Button btnExcluir;
    @FXML
    private TextField txtFiltro;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnLimpar;
    @FXML
    private ImageView imgView;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Rectangle pnView;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtValor;
    @FXML
    private TextField txtTipo;
    @FXML
    private RadioButton rdYugi;
    @FXML
    private ToggleGroup tglGroupCardGame;
    @FXML
    private RadioButton rdMagic;
    @FXML
    private RadioButton rdPokemon;
    @FXML
    private Button btnGravar;
    @FXML
    private Button btnCancelar;
    @FXML
    private CheckBox chkBxPossuir;
    @FXML
    private Group grupoRadio;
    @FXML
    private Button btnEstatistica;

    private String diretorioImagens = "src/main/resources/imagens";
    private String caminhoImagem;
    private Colecao colecaoSelecionada;
    private List<Colecao> listaColecao;
    private ObservableList<Colecao> observableListColecao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cursorTratamento();
        carregarCarta("");
    }

    @FXML
    private void tblElementoOnAction(MouseEvent event) {
        limparCampos();
        colecaoSelecionada = tblCardGame.selectionModelProperty().getValue().getSelectedItem();
        if (colecaoSelecionada != null) {
            txtNome.setText(colecaoSelecionada.getNome());
            txtValor.setText(colecaoSelecionada.getValor());
            txtTipo.setText(colecaoSelecionada.getTipo());
            caminhoImagem = colecaoSelecionada.getLocalImage();
            if (caminhoImagem != null) {
                try {
                    Image image = new Image(new File(diretorioImagens, caminhoImagem).toURI().toString());
                    imgView.setImage(image);
                    pnView.setVisible(false);
                } catch (Exception e) {
                    Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
                    alertErro.setTitle("Aviso");
                    alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
                    alertErro.showAndWait();
                }
            }
            if (colecaoSelecionada.getCardGameName().equals("Yu-Gi-Oh!")) {
                rdYugi.setSelected(true);
            } else if (colecaoSelecionada.getCardGameName().equals("Magic")) {
                rdMagic.setSelected(true);
            } else if (colecaoSelecionada.getCardGameName().equals("Pokemon TCG")) {
                rdPokemon.setSelected(true);
            }

            if (colecaoSelecionada.isPossuir()) {
                chkBxPossuir.setSelected(true);
            }
        }
    }

    @FXML
    private void btnIncluirOnAction(ActionEvent event) {
        limparCampos();
        colecaoSelecionada = null;
        carregarCarta(txtFiltro.getText());
    }

    @FXML
    private void btnExcluirOnAction(ActionEvent event) {
        colecaoSelecionada = tblCardGame.selectionModelProperty().getValue().getSelectedItem();

        if (colecaoSelecionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso");
            alert.setContentText("Confirma exclusão de " + colecaoSelecionada.getNome() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    ColecaoDaoJdbc dao = DaoFactory.novoColecaoDaoJdbc();

                    File imagemParaExcluir = new File(
                            diretorioImagens + File.separator + colecaoSelecionada.getLocalImage());
                    if (imagemParaExcluir.exists()) {
                        imagemParaExcluir.delete();
                    }

                    dao.excluir(colecaoSelecionada);
                    limparCampos();
                    carregarCarta(txtFiltro.getText());
                } catch (Exception e) {
                    Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
                    alertErro.setTitle("Aviso");
                    alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
                    alertErro.showAndWait();
                }
            }
        }
    }

    @FXML
    private void btnFiltrarOnAction(ActionEvent event) {
        carregarCarta(txtFiltro.getText());
    }

    @FXML
    private void btnLimparOnAction(ActionEvent event) {
        txtFiltro.clear();
        carregarCarta("");
    }

    @FXML
    private void btnAdicionarOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png", "*.jpeg"));
        java.io.File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                File diretorioImagensFile = new File(diretorioImagens);
                if (!diretorioImagensFile.exists()) {
                    diretorioImagensFile.mkdirs();
                }

                Path destino = Paths.get(diretorioImagens + File.separator + file.getName());
                Files.copy(file.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                caminhoImagem = file.getName();
                Image image = new Image(new File(diretorioImagens, file.getName()).toURI().toString());
                if (image != null) {
                    imgView.setImage(image);
                    pnView.setVisible(false);
                }
            } catch (IOException e) {
                Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
                alertErro.setTitle("Aviso");
                alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
                alertErro.showAndWait();
            }
        }
    }

    @FXML
    private void btnGravarOnAction(ActionEvent event) {
        Colecao colecao = new Colecao();
        colecao.setNome(txtNome.getText());
        colecao.setValor(txtValor.getText());
        colecao.setTipo(txtTipo.getText());
        colecao.setLocalImage(caminhoImagem);
        if (rdYugi.isSelected()) {
            colecao.setCardGameName("Yu-Gi-Oh!");
        } else if (rdMagic.isSelected()) {
            colecao.setCardGameName("Magic");
        } else if (rdPokemon.isSelected()) {
            colecao.setCardGameName("Pokemon TCG");
        }

        if (chkBxPossuir.isSelected()) {
            colecao.setPossuir(true);
        } else {
            colecao.setPossuir(false);
        }

        try {
            ColecaoDaoJdbc dao = DaoFactory.novoColecaoDaoJdbc();

            if (colecaoSelecionada == null) {
                dao.incluir(colecao);
            } else {
                colecao.setId(colecaoSelecionada.getId());
                dao.editar(colecao);
                colecaoSelecionada = null;
            }

            limparCampos();
            carregarCarta(txtFiltro.getText());
        } catch (Exception e) {
            Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
            alertErro.setTitle("Aviso");
            alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
            alertErro.showAndWait();
        }
    }

    @FXML
    private void btnCancelarOnAction(ActionEvent event) {
        limparCampos();
        colecaoSelecionada = null;
        carregarCarta(txtFiltro.getText());
    }

    @FXML
    private void btnEstatisticaOnAction(ActionEvent event) {
        EstatisticaController.setListaColecao(listaColecao);

        try {
            App.setRoot("Estatistica");
        } catch (Exception e) {
            Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
            alertErro.setTitle("Aviso");
            alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
            alertErro.showAndWait();
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtValor.clear();
        txtTipo.clear();
        tglGroupCardGame.selectToggle(null);
        imgView.setImage(null);
        caminhoImagem = null;
        pnView.setVisible(true);
        chkBxPossuir.setSelected(false);
    }

    private void carregarCarta(String param) {
        tblColNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tblColValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tblColTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tblColCardGame.setCellValueFactory(new PropertyValueFactory<>("cardGameName"));
        tblColPossui.setCellValueFactory(new PropertyValueFactory<>("possuir"));

        try {
            ColecaoDaoJdbc dao = DaoFactory.novoColecaoDaoJdbc();
            listaColecao = dao.listar(param);
        } catch (Exception e) {
            Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
            alertErro.setTitle("Aviso");
            alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
            alertErro.showAndWait();
        }

        observableListColecao = FXCollections.observableArrayList(listaColecao);
        tblCardGame.setItems(observableListColecao);
    }

    private void cursorTratamento() {
        txtNome.setTooltip(new Tooltip("Insira o nome da carta para registro"));
        txtValor.setTooltip(new Tooltip("Insira o valor cotado da carta em questao para registro"));
        txtTipo.setTooltip(new Tooltip("Insira o tipo de raridade da carta em questao para registro"));
        btnAdicionar.setTooltip(new Tooltip("Selecione a imagem desejada para a carta"));
        rdMagic.setTooltip(new Tooltip("Selecione qual Card Game se trata"));
        rdPokemon.setTooltip(new Tooltip("Selecione qual Card Game se trata"));
        rdYugi.setTooltip(new Tooltip("Selecione qual Card Game se trata"));
        chkBxPossuir.setTooltip(new Tooltip("marque caso ja possua a carta a ser registrada"));
    }

}
