package ro.pub.cs.systems.eim.practic.myapplication;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private String address;
    private int port;
    private String url;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(String address, int port, String url, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            if (socket == null) {
                Utilities.debug("CLIENT", "Client socket is null");
                return;
            }

            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            if (reader == null || writer == null) {
                Utilities.debug("CLIENT", "Reader or writer is null");
                return;
            }

            writer.println(url);
            writer.flush();

            String weatherInformation;

            while ((weatherInformation = reader.readLine()) != null) {
                final String finalizedWeateherInformation = weatherInformation;
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(finalizedWeateherInformation);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
