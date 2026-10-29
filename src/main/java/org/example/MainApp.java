package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** JavaFX-інтерфейс: додавання товарів, короткий список із UUID та пошук за UUID. */
public class MainApp extends Application {

    private final Store store = new Store("Магазин одягу");

    private final ComboBox<String> typeBox = new ComboBox<>();
    private final TextField brandField = new TextField();
    private final ComboBox<Size> sizeBox = new ComboBox<>();
    private final TextField colorField = new TextField();
    private final TextField materialField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField quantityField = new TextField();
    private final Label numberLabel = new Label();
    private final TextField numberField = new TextField();
    private final Label choiceLabel = new Label();
    private final ComboBox<String> choiceBox = new ComboBox<>();
    private final CheckBox hoodedBox = new CheckBox("З капюшоном");
    private final Label statusLabel = new Label();

    private final ListView<String> listView = new ListView<>();
    private final TextField uuidField = new TextField();
    private final TextArea resultArea = new TextArea();

    /** Запускає JavaFX-застосунок. */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FileStorage.load(FileStorage.FILE_NAME, store);
        VBox form = createForm();
        VBox right = createViewPanel();
        HBox root = new HBox(20, form, right);
        root.setPadding(new Insets(16));
        refreshList();
        stage.setTitle("Магазин одягу");
        stage.setScene(new Scene(root, 900, 560));
        stage.show();
    }

    @Override
    public void stop() {
        FileStorage.save(FileStorage.FILE_NAME, store);
    }

    /** Будує форму додавання товару. */
    private VBox createForm() {
        typeBox.getItems().addAll(ClothesFactory.categories());
        typeBox.setValue(ClothesFactory.PANTS);
        typeBox.setOnAction(e -> updateExtraFields());
        sizeBox.getItems().addAll(Size.values());
        sizeBox.setValue(Size.M);
        brandField.setPromptText("Бренд");
        colorField.setPromptText("Колір");
        materialField.setPromptText("Матеріал");
        priceField.setPromptText("Ціна");
        quantityField.setPromptText("Кількість");

        Button addButton = new Button("Додати");
        addButton.setOnAction(e -> addClothes());
        updateExtraFields();

        VBox form = new VBox(6);
        form.getChildren().addAll(new Label("Додати товар"), new Label("Тип"), typeBox,
                new Label("Бренд"), brandField, new Label("Розмір"), sizeBox,
                new Label("Колір"), colorField, new Label("Матеріал"), materialField,
                new Label("Ціна"), priceField, new Label("Кількість"), quantityField,
                numberLabel, numberField, choiceLabel, choiceBox, hoodedBox, addButton, statusLabel);
        return form;
    }

    /** Будує панель зі списком, пошуком за UUID і результатом. */
    private VBox createViewPanel() {
        listView.setPrefHeight(300);
        listView.setOnMouseClicked(e -> copySelectedUuid());
        uuidField.setPromptText("UUID");
        Button findButton = new Button("Знайти");
        findButton.setOnAction(e -> findByUuid());
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(140);

        HBox searchRow = new HBox(8, uuidField, findButton);
        VBox panel = new VBox(6);
        panel.getChildren().addAll(new Label("Усі товари (коротко)"), listView,
                new Label("Пошук за UUID"), searchRow,
                new Label("Повна інформація"), resultArea);
        return panel;
    }

    /** Змінює підписи додаткових полів залежно від обраного типу. */
    private void updateExtraFields() {
        String type = typeBox.getValue();
        choiceBox.getItems().clear();
        boolean jacket = ClothesFactory.JACKET.equals(type);
        numberLabel.setVisible(!jacket);
        numberLabel.setManaged(!jacket);
        numberField.setVisible(!jacket);
        numberField.setManaged(!jacket);
        hoodedBox.setVisible(jacket);
        hoodedBox.setManaged(jacket);
        if (ClothesFactory.PANTS.equals(type)) {
            numberLabel.setText("Довжина, см");
            choiceLabel.setText("Крій");
            fillChoices(Fit.values());
        } else if (ClothesFactory.SHIRTS.equals(type)) {
            numberLabel.setText("Розмір коміра, см");
            choiceLabel.setText("Рукав");
            fillChoices(Sleeve.values());
        } else if (jacket) {
            choiceLabel.setText("Сезон");
            fillChoices(Season.values());
        } else {
            numberLabel.setText("Довжина, см");
            choiceLabel.setText("Призначення");
            fillChoices(Occasion.values());
        }
    }

    /** Заповнює список вибору іменами значень перерахування. */
    private void fillChoices(Enum<?>[] values) {
        for (Enum<?> value : values) {
            choiceBox.getItems().add(value.name());
        }
        choiceBox.setValue(values[0].name());
    }

    /** Створює товар зі значень форми й додає його в магазин. */
    private void addClothes() {
        Map<String, String> fields = new HashMap<>();
        fields.put("brand", brandField.getText());
        fields.put("size", sizeBox.getValue() == null ? "" : sizeBox.getValue().name());
        fields.put("color", colorField.getText());
        fields.put("material", materialField.getText());
        fields.put("price", priceField.getText());
        fields.put("quantity", quantityField.getText());
        fields.put("number", numberField.getText());
        fields.put("choice", choiceBox.getValue() == null ? "" : choiceBox.getValue());
        fields.put("hooded", String.valueOf(hoodedBox.isSelected()));
        try {
            Clothes created = ClothesFactory.create(typeBox.getValue(), fields);
            store.addNewClothes(created, created.getQuantity());
            statusLabel.setText("Додано. UUID: " + created.getUuid());
            clearForm();
            refreshList();
        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Помилка: " + ex.getMessage());
        }
    }

    /** Очищає текстові поля форми після додавання. */
    private void clearForm() {
        brandField.clear();
        colorField.clear();
        materialField.clear();
        priceField.clear();
        quantityField.clear();
        numberField.clear();
        hoodedBox.setSelected(false);
    }

    /** Оновлює короткий список товарів. */
    private void refreshList() {
        listView.getItems().clear();
        for (Clothes item : store.getItems()) {
            listView.getItems().add(item.toShortString());
        }
    }

    /** Підставляє UUID вибраного в списку товару в поле пошуку. */
    private void copySelectedUuid() {
        int index = listView.getSelectionModel().getSelectedIndex();
        List<Clothes> items = store.getItems();
        if (index >= 0 && index < items.size()) {
            uuidField.setText(items.get(index).getUuid().toString());
        }
    }

    /** Шукає товар за UUID із поля й показує повну інформацію або повідомлення. */
    private void findByUuid() {
        resultArea.clear();
        try {
            Clothes item = store.findByUuid(uuidField.getText());
            if (item == null) {
                statusLabel.setText("Не знайдено: товару з таким UUID немає.");
            } else {
                statusLabel.setText("Знайдено.");
                resultArea.setText(item.toString());
            }
        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Помилка: " + ex.getMessage());
        }
    }
}