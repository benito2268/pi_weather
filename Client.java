/*
 * Client.java ~ the client to recieve data from the sensors
 * author: Ben Staehle
 * date: 8/6/23
 */

import java.net.*;
import java.io.*;

public class Client
{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public void start_connection(String addr, int port) throws IOException {
        socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public WeatherData.DStruct read_data() throws Exception {
        WeatherData.DStruct resp = (WeatherData.DStruct)in.readObject();
        return resp;
    }

    public void disconnect() throws Exception {
        in.close();
        out.close();

        socket.close();
    }

    public static void main(String[] args) {
        String address = "192.168.1.61";
        int port = 6666;
        try {
            Client c = new Client();
            c.start_connection(address, port);
            WeatherData.DStruct a = c.read_data();
            System.out.println(a.toString());
            c.disconnect();
        } catch(ConnectException e1) {
            System.out.println("connection error " + e1.getMessage());
            System.out.println("tried to connect to " + address + ":" + port);
        } catch(SocketException e2) {
            System.out.println("socket error " + e2.getMessage());
            System.out.println("tried to connect to " + address + ":" + port);
        } catch(IOException e3) {
            System.out.println("io/stream error " + e3.getMessage());
        } catch(Exception e4) {
            System.out.println("nonspecific oops " + e4.getMessage());
        }
    }
}