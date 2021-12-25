module org.joe.reem.president.vice {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.joe.reem.president.vice to javafx.fxml;
    exports org.joe.reem.president.vice;
}