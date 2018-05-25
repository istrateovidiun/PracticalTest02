package ro.pub.cs.systems.eim.practic.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText server_port;
    private Button server_connect;
    private EditText client_address;
    private EditText client_port;
    private EditText client_url;
    private Button client_connect;
    private TextView client_result_view;
    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server_port = (EditText) findViewById(R.id.server_port);
        server_connect = (Button) findViewById(R.id.server_button);

        client_address = (EditText) findViewById(R.id.client_address);
        client_url = (EditText) findViewById(R.id.client_url);
        client_port = (EditText) findViewById(R.id.client_port);

        client_connect = (Button) findViewById(R.id.client_get);

        client_connect.setOnClickListener(new ButtonClickListener());
        server_connect.setOnClickListener(new ButtonClickListener());
        client_result_view = (TextView) findViewById(R.id.client_resultview);
    }

    class ButtonClickListener implements View.OnClickListener {

        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.server_button:
                    if (isPortValid()) {
                        serverThread = new ServerThread(Integer.parseInt(server_port.getText().toString()));
                        serverThread.start();
                    } else {
                        Toast.makeText(MainActivity.this, "You cannot let port empty!",
                                Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.client_get:


                    if (isQueryValid()) {
                        String clientAddress = client_address.getText().toString();
                        String clientPort = client_port.getText().toString();
                        String url = client_url.getText().toString();

                        clientThread = new ClientThread(
                                clientAddress, Integer.parseInt(clientPort), url, client_result_view
                        );
                        clientThread.start();


                    } else {
                        Toast.makeText(MainActivity.this,
                                "You cannot let query with an empty value!",
                                Toast.LENGTH_LONG).show();


                    }
                    break;

                default:
                    break;
            }
        }

    }

    private boolean isPortValid() {
        return server_port.getText() != null && !server_port.getText().toString().isEmpty();
    }

    private boolean isQueryValid() {
        return client_address.getText() != null && !client_address.getText().toString().isEmpty();
    }
}
