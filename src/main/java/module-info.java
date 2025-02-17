module com.username1419.jpprac {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires net.synedra.validatorfx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;
	requires jdk.jsobject;
	requires org.json;
	requires java.logging;
	requires jdk.compiler;
	requires java.desktop;

	opens com.username1419.jpprac to javafx.fxml;
	exports com.username1419.jpprac;
}