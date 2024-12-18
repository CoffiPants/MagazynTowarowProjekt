module org.example.magazyntowarowprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens org.example.magazyntowarowprojekt to javafx.fxml, org.hibernate.orm.core;
    exports org.example.magazyntowarowprojekt;
}