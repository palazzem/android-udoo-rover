package me.palazzetti.adkrover.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnector {
    private HttpURLConnection mConnector;

    public UrlConnector(String encodedUrl) throws IOException {
        URL url = new URL(encodedUrl);
        mConnector = (HttpURLConnection) url.openConnection();
        mConnector.setReadTimeout(10000);
        mConnector.setConnectTimeout(15000);
        mConnector.setUseCaches(false);
    }

    public void addHeader(String header, String content) {
        mConnector.setRequestProperty(header, content);
    }

    public int get() throws IOException {
        mConnector.setRequestMethod("GET");
        mConnector.setDoInput(true);

        return mConnector.getResponseCode();
    }

    public String getResponse() throws IOException {
        BufferedReader readerBuffer = new BufferedReader(new InputStreamReader(mConnector.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = readerBuffer.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    public void disconnect() {
        mConnector.disconnect();
    }
}
