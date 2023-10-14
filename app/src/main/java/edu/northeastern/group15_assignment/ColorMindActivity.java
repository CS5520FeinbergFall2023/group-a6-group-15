package edu.northeastern.group15_assignment;

import static java.util.List.of;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

public class ColorMindActivity extends AppCompatActivity {

    ColorMindBackendImpl colorMindBackendImpl = new ColorMindBackendImpl();

    private List<Integer> getColorsFromSliders() {
        Slider redSlider = findViewById(R.id.slider_color1R);
        Slider greenSlider = findViewById(R.id.slider_color1G);
        Slider blueSlider = findViewById(R.id.slider_color1B);

        int red = (int) redSlider.getValue();
        int green = (int) greenSlider.getValue();
        int blue = (int) blueSlider.getValue();

        List<Integer> colors = new ArrayList<>();
        colors.add(red);
        colors.add(green);
        colors.add(blue);

        return colors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_mind);

        // Set the listener for the button
        findViewById(R.id.b_generate).setOnClickListener(v -> {
            List<Integer> sliderVals = getColorsFromSliders();
            String color = String.format("%02x%02x%02x", sliderVals.get(0), sliderVals.get(1), sliderVals.get(2));
            colorMindBackendImpl.initiatePaletteGeneration(color);

            while (colorMindBackendImpl.isInProgess()) {
                // Wait for the thread to finish
            }

            System.out.println("OUTPUT : " + colorMindBackendImpl.retrievePalette().toString());
            }
        );
    }
}