package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import model.Colecao;
import start.App;

public class EstatisticaController implements Initializable {

    @FXML
    private PieChart grafPizza;
    @FXML
    private PieChart grafPizza2;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnVoltar1;

    private static List<Colecao> listaColecao = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atualizarDados();
    }

    @FXML
    private void btnVoltarOnAction(ActionEvent event) {
        try {
            App.setRoot("Principal");
        } catch (IOException e) {
            Alert alertErro = new Alert(Alert.AlertType.INFORMATION);
            alertErro.setTitle("Aviso");
            alertErro.setContentText("Ocorreu um erro: " + e.getMessage());
            alertErro.showAndWait();
        }
    }

    private float contGame(String param) {
        int cont = 0;
        int outros = 0;
        int total = 0;
        float contpc = 0;
        for (int i = 0; i < listaColecao.size(); i++) {
            if (listaColecao.get(i).getCardGameName().equals(param)) {
                cont++;
            } else {
                outros++;
            }
        }

        total = outros + cont;

        contpc = (cont * 100) / total;
        return contpc;
    }

    private float contPossuir() {
        int possui = 0;
        int total = listaColecao.size();

        for (int i = 0; i < total; i++) {
            if (listaColecao.get(i).isPossuir()) {
                possui++;
            }
        }

        if (total > 0) {
            return (float) possui / total * 100;
        } else {
            return 0;
        }
    }

    private void atualizarDados() {
        grafPizza.getData().clear();
        grafPizza2.getData().clear();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Yu-Gi-Oh!", contGame("Yu-Gi-Oh!")),
                new PieChart.Data("Magic", contGame("Magic")),
                new PieChart.Data("Pokemon TCG", contGame("Pokemon TCG")));

        pieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " porcentagem ", data.pieValueProperty())));

        grafPizza.getData().addAll(pieChartData);

        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList(
                new PieChart.Data("Cartas possuidas", contPossuir()),
                new PieChart.Data("Cartas nao possuidas", 100 - contPossuir()));

        pieChartData2.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " porcentagem ", data.pieValueProperty())));

        grafPizza2.getData().addAll(pieChartData2);
    }

    public static void setListaColecao(List<Colecao> listaColecao) {
        EstatisticaController.listaColecao = listaColecao;
    }
}
