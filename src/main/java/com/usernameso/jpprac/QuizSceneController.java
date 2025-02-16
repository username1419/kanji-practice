package com.usernameso.jpprac;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.im.InputContext;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class QuizSceneController {
	private final Random random = new Random();
	@FXML private Text kanjiDisplay;
	@FXML private TextField kanjiInput;
	@FXML private Text kanjiResult;
	private HashMap<Integer, HashMap<String, ArrayList<String>>> kanjiList = new HashMap<>();
	private long startTime;
	private int gradeLevel;

	private static JSONObject timeDelay = new JSONObject();

	@FXML
	public void initialize() {
		kanjiList = StartSceneController.getKanjiList();
		try {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/username1419/jpprac/fsrs.json"))) {
				sb.append(reader.readLine());
				sb.append(System.lineSeparator());
			}

			timeDelay = new JSONObject(sb.toString());
		} catch (IOException | JSONException e) { }
		try {
			gradeLevel = random.nextInt(1, MainApplication.gradeLevel);
		} catch (IllegalArgumentException e) {
			gradeLevel = 1;
		}
		kanjiDisplay.setText(requestKanji(gradeLevel));

		startTime = System.currentTimeMillis();

		InputContext.getInstance().selectInputMethod(Locale.JAPANESE);
	}

	public String requestKanji(int gradeLevel) {
		Calendar c = Calendar.getInstance();
		while (true) {
			String requestedKanji = (String) kanjiList.get(gradeLevel).keySet().toArray()[random.nextInt(kanjiList.size())];

			for (int i = 0; i < 10; i++) {
				try {
					LocalDate recordedDate = (LocalDate) timeDelay.getJSONObject(String.valueOf(i + 1)).get(requestedKanji);
					if (recordedDate.plusDays(i+1).compareTo(LocalDate.now()) >= i + 1 ) {
						return requestedKanji;
					}

				} catch (JSONException _) { }
			}
			return requestedKanji;
		}
	}

	public void delayKanji(String kanji, int days) {
		JSONObject kanjiDict;
		try {

			kanjiDict = timeDelay.getJSONObject(String.valueOf(days));
			kanjiDict.append(kanji, LocalDate.now());
		} catch (JSONException e) {
			kanjiDict = new JSONObject();
			kanjiDict.append(kanji, LocalDate.now());
			timeDelay.append(String.valueOf(days), kanjiDict);
		}

	}

	public static void saveTimeDelay() {
		try (FileWriter writer = new FileWriter("src/main/resources/com/username1419/jpprac/fsrs.json")) {
			writer.write(timeDelay.toString());
		} catch (IOException e) {
			File file = new File("src/main/resources/com/username1419/jpprac/fsrs.json");
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter("src/main/resources/com/username1419/jpprac/fsrs.json");
				writer.write(timeDelay.toString());
				writer.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	Thread waitBetween;

	@FXML
	public void onResultSubmit(ActionEvent actionEvent) {
		if (Objects.equals(kanjiInput.getText(), "")) return;
		long time = (System.currentTimeMillis() - startTime) / 100;

		ArrayList<String> kanjiReadings = kanjiList.get(gradeLevel).get(kanjiDisplay.getText());
		if (kanjiReadings.contains(kanjiInput.getText())) {
			kanjiResult.setText("Correct");
			if (time < 5) {
				delayKanji(kanjiDisplay.getText(), 5);
			} else if (time < 10) {
				delayKanji(kanjiDisplay.getText(), 3);
			} else if (time < 20) {
				delayKanji(kanjiDisplay.getText(), 2);
			} else {
				delayKanji(kanjiDisplay.getText(), 1);
			}

		} else {
			kanjiResult.setText("Incorrect");
			boolean match = false;
			outer :
			for (String reading : kanjiReadings) {
				for (char letter : kanjiInput.getText().toCharArray()) {
					if (reading.contains(String.valueOf(letter))) {
						match = true;
						break outer;
					}
				}
			}
			if (match) {
				delayKanji(kanjiDisplay.getText(), 1);
			}
		}

		try {
			if (waitBetween.isAlive()) return;
		} catch (NullPointerException e) {
			waitBetween = new Thread(() -> {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException _) {
				}

				kanjiResult.setText("");
				try {
					gradeLevel = random.nextInt(1, MainApplication.gradeLevel);
				} catch (IllegalArgumentException _) {
					gradeLevel = 1;
				}
				kanjiDisplay.setText(requestKanji(gradeLevel));
				kanjiInput.setText("");
			});
			waitBetween.start();
		}
	}
}
