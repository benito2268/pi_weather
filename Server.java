/*
 * Server.java ~ defines server code to run on the raspberry pi
 * author: Ben Staehle
 * date: 8/6/23
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Server
{
    private Socket socket;
    private ServerSocket server_socket;

    private ExecutorService pool;
    private Thread listen_thr;

    private int port;

    public Server(int port) {
        this.port = port;
        System.out.println("sever: starting server on " + Integer.toString(port));

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    shutdown();
                } catch(IOException e) {
                    //do nothing
                }
            }
        });

        this.start();
    }

    public synchronized void start() {
        pool = Executors.newFixedThreadPool(4);

        listen_thr = new Thread(() -> {
            try {
                server_socket = new ServerSocket(port);
                System.out.println("waiting for a client...");

                while(!listen_thr.isInterrupted()) {
                     await_connection();
                     if(!Thread.currentThread().isInterrupted()) {
                        pool.submit(new SRecordTask(socket));
                     }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        }, "ListenThread");

        listen_thr.start();
    }

    public synchronized void await_connection() {
        try {
            socket = server_socket.accept();
            System.out.println("client connected");
        } catch(Exception e) {
             e.printStackTrace();
        }
    }

    private class SRecordTask implements Runnable
    {
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private final Socket client_socket;

        SRecordTask(Socket client_socket) {
             this.client_socket = client_socket;
             try {
                 in = new ObjectInputStream(this.client_socket.getInputStream());
                 out = new ObjectOutputStream(this.client_socket.getOutputStream());

             } catch(Exception e) {
                e.printStackTrace();
             }
        }

        @Override
        public void run() {
            //record the sensor data and send a packet over the output stream
            try {
                write_data(get_reading().getDataStruct());

                //once done free the streams
                cleanup();

                //now close the client's connection
                client_socket.close();
                System.out.println("client disconnected");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        public WeatherData get_reading() throws Exception {
            return SensorRead.read_sensors_py("sensor.py");
        }

        public String read_data() throws Exception {
            return (String)in.readObject();
        }

        public void write_data(WeatherData.DStruct data) throws Exception {
            out.writeObject(data);
        }

        public void cleanup() throws IOException {
            in.close();
            out.close();
        }
    }

    public void shutdown() throws IOException {
        if(pool != null) {
            pool.shutdown();
            pool = null;
        }

        if(listen_thr != null) {
            listen_thr.interrupt();

            try(Socket voidSocket = new Socket("localhost", port)) {

            } catch(Exception e) {
                e.printStackTrace();
            }

            listen_thr = null;
        }

        socket.close();
        server_socket.close();
    }

    public static void main(String[] args) throws Exception {
        Server s = new Server(6666);

    }
}