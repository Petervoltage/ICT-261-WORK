module com.example.hellofx {
    requires javafx.controls;

    // Open model properties for TableView reflection & controls
    opens com.example.hellofx to javafx.base;
    exports com.example.hellofx;
}