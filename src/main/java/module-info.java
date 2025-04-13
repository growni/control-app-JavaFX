module com.example.control {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires spring.web;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.gluonhq.attach.util;

    opens com.example.control to javafx.fxml;
    exports com.example.control;
    exports com.example.control.controllers;
    exports com.example.control.DTO;
    opens com.example.control.controllers to javafx.fxml;
    opens com.example.control.DTO to com.fasterxml.jackson.databind;
}