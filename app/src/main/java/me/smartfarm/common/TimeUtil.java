package me.smartfarm.common;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TimeUtil {
    public static long getNetworkTime() {
        try {
            // URL of a public time server
            URL url = new URL("http://worldtimeapi.org/api/timezone/Etc/UTC");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Clean up
            in.close();
            connection.disconnect();

            // Parse the response (example with simple JSON parsing)
            String jsonResponse = content.toString();
            long epochTime = new JSONObject(jsonResponse).getLong("unixtime") * 1000;

            return epochTime;

        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis(); // Fallback to local time if network call fails
        }
    }
}
