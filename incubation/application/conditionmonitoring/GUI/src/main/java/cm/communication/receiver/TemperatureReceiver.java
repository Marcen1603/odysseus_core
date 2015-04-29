package cm.communication.receiver;

import cm.model.Sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tobias
 * @since 06.04.2015.
 */
public class TemperatureReceiver {

    public TemperatureReceiver(Sensor sensor, String ip, int port) {
        new ListenerThread(sensor, ip, port).start();
    }
}

class ListenerThread extends Thread {

    private Sensor sensor;
    private String ip;
    private int port;

    public ListenerThread(Sensor sensor, String ip, int port) {
        this.sensor = sensor;
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            BufferedReader bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            while (!isInterrupted()) {
                String message = bufferedReader.readLine();
                List<String> items = Arrays.asList(message.split("\\s*,\\s*"));
                String temperatureValueString = items.get(1);
                double temperatureValue = Double.valueOf(temperatureValueString);
                sensor.setCurrentValue(temperatureValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
