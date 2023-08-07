import java.awt.*;

public class GUI 
{
    private Frame frame;
    private Label temp_label;

    GUI() {
        frame = new Frame();
        temp_label = new Label("0.0\u00B0");
        temp_label.setSize(100, 100);
        temp_label.setBounds(50, 50, 100, 100);

        frame.add(temp_label);
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    void update(WeatherData.DStruct data) {
        temp_label.setText(((Double)(data.temp)).toString() + "\u00B0");
    }
}
