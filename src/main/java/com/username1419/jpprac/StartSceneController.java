package com.username1419.jpprac;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StartSceneController {
	public Stage root;

	private HashMap<Integer, HashMap<String, ArrayList<String>>> kanjiList = new HashMap<>();

	@FXML
	private ChoiceBox<Integer> levelSelector;

	public static HashMap<Integer, HashMap<String, ArrayList<String>>> getKanjiList() {
		HashMap<Integer, HashMap<String, ArrayList<String>>> kanjiList = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(MainApplication.class.getResource("data.json").getFile()))) {
			String s = br.readLine();
			while (s != null) {
				sb.append(s);
				sb.append(System.lineSeparator());
				s = br.readLine();
			}
		} catch (IOException _) { }

		JSONObject fileData = new JSONObject(sb.toString());
		for (Object key : fileData.names()) {
			if (key == null) { }

			JSONObject data = fileData.getJSONObject((String) key);
			HashMap<String, ArrayList<String>> kanjiData = new HashMap<>();
			data.toMap().forEach((k, v) -> kanjiData.put(k, (ArrayList<String>) v));
			kanjiList.put(Integer.valueOf((String) key), kanjiData);
		}
		return kanjiList;
	}

	@FXML
	public void initialize() {
		kanjiList = getKanjiList();
		ObservableList<Integer> list = FXCollections.observableList(kanjiList.keySet().stream().toList());
		levelSelector.setItems(list);
	}

	public void onStart(ActionEvent actionEvent) {
		if (levelSelector.getValue() == null) return;
		MainApplication.gradeLevel = levelSelector.getValue();

		root = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("quiz-view.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 320, 240);
			root.setScene(scene);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}