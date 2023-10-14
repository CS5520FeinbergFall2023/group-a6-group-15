package edu.northeastern.group15_assignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ColorMindBackendImpl implements ColorMindBackendConnector {

    // Thread to run the API call
    private class ColorMindThread extends Thread {
        private String color;
        private List<String> palette;
        private boolean inProgress;
        private URL url = new URL("http://colormind.io/api/");

        public ColorMindThread(String color) throws MalformedURLException {
            this.color = color;
            this.palette = null;
            this.inProgress = true;
        }

        @Override
        public void run() {

            // API Spec Format
            // {"input":[[255,255,255], [0,0,0], [0,0,0]],"model":"default"}
            // URL : http://colormind.io/api/
            // Response Format
            // {"result":[[44,43,44],[90,83,82],[191,184,175],[141,137,132],[171,165,159]]}

            inProgress = true;
            palette = new java.util.ArrayList<>();
            // TODO: Implement API call here
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                String colorString = "";
                // Convert HEX to RGB [r, g, b]
                for (int i = 0; i < color.length(); i += 2) {
                    String sub = color.substring(i, i + 2);
                    int num = Integer.parseInt(sub, 16);
                    colorString += num + ",";
                }
                colorString = colorString.substring(0, colorString.length() - 1);

                String inputString = "{\"input\":[[" + colorString + "], \"N\"],\"model\":\"default\"}";

                System.out.println("INPUT : " + inputString);

                connection.setDoOutput(true);
                connection.getOutputStream().write(inputString.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // TODO: Parse the response and store it in the palette variable

                    InputStream response = connection.getInputStream();

                    // Parse a HTML response using a HTML parser
                    java.util.Scanner s = new java.util.Scanner(response).useDelimiter("\\A");
                    String responseString = s.hasNext() ? s.next() : "";

                    // Ensure to close the input stream
                    response.close();
                    // Ensure proper termination of the response string
                    responseString += "\0";

                    System.out.println("RESPONSE : " + responseString);

                    JSONObject responseJSON = new JSONObject(responseString);
                    JSONArray result = responseJSON.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONArray color = (JSONArray) result.get(i);
                        String returnColor = "";

                        // RGB to HEX
                        for (int j = 0; j < color.length(); j++) {
                            int num = (int) color.get(j);
                            String hex = Integer.toHexString(num);
                            if (hex.length() == 1) {
                                hex = "0" + hex;
                            }
                            returnColor += hex;
                        }
                        palette.add(returnColor);
                    }
                } else {
                    throw new RuntimeException("Failed to retrieve response to API");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // TODO: Set the inProgress variable to false

            inProgress = false;

        }

        public List<String> getPalette() {
            return this.palette;
        }

        public boolean isInProgress() {
            return this.inProgress;
        }

        public String getColor() {
            return this.color;
        }
    }

    ColorMindThread thread = null;

    @Override
    public void initiatePaletteGeneration(String color) {
        // Run the thread
        try {
            thread = new ColorMindThread(color);
            thread.start();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isInProgess() {
        if (thread != null) {
            return thread.isInProgress();
        }
        return false;
    }

    @Override
    public List<String> retrievePalette() {
        if (thread == null) {
            return null;
        }
        return thread.getPalette();
    }
}
