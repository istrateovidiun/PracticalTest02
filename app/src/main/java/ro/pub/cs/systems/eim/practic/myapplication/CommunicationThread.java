package ro.pub.cs.systems.eim.practic.myapplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;


public class CommunicationThread extends Thread {

    private Socket socket;
    private ServerThread serverThread;

    public CommunicationThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        if(socket == null) {
            Utilities.debug("COMMUNICATION", "Communication Socket is null");
            return;
        }

        try {
            BufferedReader reader = Utilities.getReader(socket);
            PrintWriter writer = Utilities.getWriter(socket);

            if(reader == null || writer == null) {
                Utilities.debug("COMMUNICATION", "Reader or writer is null");
                return;
            }


            //Receive city & infoType

            String url = reader.readLine();
            Utilities.debug("SERVER", "url is " + url);

            boolean retval = url.contains("bad");

            if (retval == true) {

                writer.println("block");
                writer.flush();

            } else {

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(url);
                HttpResponse response = httpClient.execute(request);

                String html = "";
                InputStream in = response.getEntity().getContent();
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(in));
                StringBuilder str = new StringBuilder();
                String line = null;
                while((line = reader2.readLine()) != null)
                {
                    str.append(line);
                }
                in.close();
                html = str.toString();
                writer.println(html);
                writer.flush();
            }
            
            } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
