package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ColorMindActivity extends AppCompatActivity {

    List<String> palette;
    private Handler viewHandler = new Handler();
    Slider redSlider;
    Slider greenSlider;
    Slider blueSlider;
    TextView rgbTV;
    Button generateButton;
    ProgressBar progressBar;

    TextView colorPanel1;
    TextView colorPanel2;
    TextView colorPanel3;
    TextView colorPanel4;
    TextView colorPanel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_mind);

        redSlider = findViewById(R.id.slider_color1R);
        greenSlider = findViewById(R.id.slider_color1G);
        blueSlider = findViewById(R.id.slider_color1B);
        generateButton = findViewById(R.id.b_generate);

        colorPanel1 = findViewById(R.id.tv_palette_color_1);
        colorPanel2 = findViewById(R.id.tv_palette_color_2);
        colorPanel3 = findViewById(R.id.tv_palette_color_3);
        colorPanel4 = findViewById(R.id.tv_palette_color_4);
        colorPanel5 = findViewById(R.id.tv_palette_color_5);


        progressBar = findViewById(R.id.simpleProgressBar);

//        = new ProgressBar(this);
//        progressBar.setIndeterminate(true);
//        progressBar.setVisibility(View.INVISIBLE);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        this.addContentView(progressBar,params);

        Slider.OnChangeListener onChangeListener = (slider, value, fromUser)->this.setRGBTextView();
        redSlider.addOnChangeListener(onChangeListener);
        greenSlider.addOnChangeListener(onChangeListener);
        blueSlider.addOnChangeListener(onChangeListener);

        rgbTV = findViewById(R.id.tv_color1_RGB);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_color_mind);
        } else{
            setContentView(R.layout.activity_color_mind_horizontal);
        }
    }

    public void setRGBTextView(){
        List<Integer> colorVals = this.getColorsFromSliders();
        rgbTV.setText("["+colorVals.get(0)+", "+colorVals.get(1)+", "+colorVals.get(2)+"]");
    }

    public void generatePalette(View view) throws MalformedURLException {
        List<Integer> sliderVals = this.getColorsFromSliders();
        String color = String.format("%02x%02x%02x", sliderVals.get(0), sliderVals.get(1), sliderVals.get(2));

        RunnableThread runnableThread = new RunnableThread(color);
        new Thread(runnableThread).start();
    }

    private void setPaletteColors(){
        if(this.palette!=null){
            System.out.println("BOOYA:"+ palette);
            colorPanel1.setBackgroundColor(Color.parseColor(palette.get(0)));
            colorPanel2.setBackgroundColor(Color.parseColor(palette.get(1)));
            colorPanel3.setBackgroundColor(Color.parseColor(palette.get(2)));
            colorPanel4.setBackgroundColor(Color.parseColor(palette.get(3)));
            colorPanel5.setBackgroundColor(Color.parseColor(palette.get(4)));
        }
    }

    private List<Integer> getColorsFromSliders() {
        List<Integer> colors = new ArrayList<>();
        int red = (int) redSlider.getValue();
        int green = (int) greenSlider.getValue();
        int blue = (int) blueSlider.getValue();

        colors.add(red);
        colors.add(green);
        colors.add(blue);
        return colors;
    }

    class RunnableThread implements Runnable {

        private URL url = new URL("http://colormind.io/api/");
        private String color;
        private List<String> paletteResponse = new ArrayList<>();

        RunnableThread(String color) throws MalformedURLException {
            this.color = color;
        }

        @Override
        public void run() {
            Log.d("THREAD", "Running on a different thread");
            viewHandler.post(() -> {
                generateButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            });

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
                        paletteResponse.add("#"+returnColor);
                    }
                } else {
                    throw new RuntimeException("Failed to retrieve response to API");
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            viewHandler.post(() -> {
                generateButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                palette = paletteResponse;
                setPaletteColors();
            });
        }

    }

}