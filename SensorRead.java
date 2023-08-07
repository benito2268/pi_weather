import java.io.*;

public class SensorRead {
    public static WeatherData read_sensors_py(String pyscript) throws Exception {
        //solid code right here
        String cmd = "python sensor.py";
        Process p = Runtime.getRuntime().exec(cmd);

        //read the data into a WeatherData
        double temp = 0.0;
        double humid = 0.0;
        double press = 0.0;

        BufferedReader br = new BufferedReader(new FileReader("weather.txt"));

        if(br.ready()) {
                String str = "";
                String line = "";
                while((str = br.readLine()) != null) {
                        line += str;
                }

                String[] arr = line.split("~");

                temp = Double.parseDouble(arr[0]);
                humid = Double.parseDouble(arr[1]);
                press = Double.parseDouble(arr[2]);
        }

        return new WeatherData(temp, humid, press);
    }
}