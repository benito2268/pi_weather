import java.io.Serializable;

public class WeatherData implements Serializable {

    //acts like a c struct that holds only the data
    //can be serialized and sent over network
    private DStruct _data;

    public WeatherData(double temp, double humid, double press) {
        _data = new DStruct();
        _data.temp = temp;
        _data.humid = humid;
        _data.press = press;
    }

    public DStruct getDataStruct() {
        return this._data;
    }

    class DStruct implements Serializable {
        public double temp;
        public double humid;
        public double press;

        @Override
        public String toString() {
            return temp + " " + humid + " " + press;
        }
    }
}