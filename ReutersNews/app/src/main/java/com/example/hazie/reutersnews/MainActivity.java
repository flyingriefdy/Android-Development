package com.example.hazie.reutersnews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnParse;
    private ListView listApps;
    private String mFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnParse = (Button) findViewById(R.id.btnParse);
        listApps = (ListView) findViewById(R.id.xmlListView);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add parse activation code
                ParseApplication parseApplication = new ParseApplication(mFileContents);
                parseApplication.process();
                // array adapter
                ArrayAdapter<Application> adapter = new ArrayAdapter<Application>(MainActivity.this,
                        R.layout.list_item,
                        parseApplication.getApplications());
                listApps.setAdapter(adapter);
            }
        });
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            mFileContents = downloadXMLFile(strings[0]);
            if (mFileContents == null) {
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was " + result);
            // right place to put code to update interface
        }

        private String downloadXMLFile(String urlPath) {
            // need buffer, make string building more efficient
            StringBuilder tempBuffer = new StringBuilder();
            try {
                // open the file HTTP connection to see if its valid URL
                URL url = new URL(urlPath);
                // this one opens
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // its those 404 error codes etc
                int response = connection.getResponseCode();
                Log.d("DownloadData", "The response code was " + response);
                // two mechanisms needed to start reading data
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                // read 500 character at a time
                char[] inputBuffer = new char[500];
                // need loop to continue reading the file until to get the last text
                while (true) {
                    charRead = isr.read(inputBuffer);
                    if (charRead <= 0) {
                        break;
                    }
                    // copy from inputBuffer, starting at 0 up to charRead
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            } catch (IOException e) {
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.d("DownloadData", "Security Exception: " + e.getMessage());
            }

            return null;
        }
    }


}
