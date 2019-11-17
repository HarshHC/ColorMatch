package com.hc.apps.colormatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ColorsActivity extends AppCompatActivity {

    private Button startButton, nextColor;
    private List<String> colors, colorValues;
    private String currentColor;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        colors= Arrays.asList("Red", "Orange", "Yellow", "Green", "Blue", "Violet", "Brown", "Black", "Grey", "White");
        colorValues= Arrays.asList("#FF0000", "#FFA500", "#FFFF00", "#008000", "#0000FF", "#EE82EE", "#A52A2A", "#000000", "#808080", "#FFFFFF");


        startButton = findViewById(R.id.startButton);
        nextColor = findViewById(R.id.nextColor);
        img = findViewById(R.id.image);

        newColor();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColorsActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);//default is true
                startActivityForResult(intent, 1213);
            }
        });

        nextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newColor();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

            startButton.setVisibility(View.GONE);
            img.setImageBitmap(selectedImage);
            img.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(ColorsActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);

            boolean correct = false;
            View dialogView;
            Button btn;

            if(correct){
                dialogView = LayoutInflater.from(this).inflate(R.layout.correct_dialog_layout, viewGroup, false);
                btn = dialogView.findViewById(R.id.next_color);
            }else{
                dialogView = LayoutInflater.from(this).inflate(R.layout.wrong_dialog_layout, viewGroup, false);
                btn = dialogView.findViewById(R.id.try_again);
            }


            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            if(correct){
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newColor();
                        alertDialog.dismiss();
                    }
                });
            }else{
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startButton.setVisibility(View.VISIBLE);
                        img.setVisibility(View.GONE);
                        alertDialog.dismiss();
                    }
                });
            }

        }
    }

    public void newColor(){
        Random rand = new Random();
        int random= rand.nextInt(colors.size());

        String colorName = colors.get(random);
        String colorValue = colorValues.get(random);
        TextView colorNameText = findViewById(R.id.color_name);
        colorNameText.setText(colorName);

        if(colorName == currentColor){
            newColor();
            return;
        }

        ImageView colorImage= findViewById(R.id.colorImage);

        Drawable drawable= getResources().getDrawable(R.drawable.rounded_rect);
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);

        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(colorValue));

        colorImage.setImageDrawable(drawable);

        startButton.setVisibility(View.VISIBLE);
        img.setVisibility(View.GONE);

        currentColor= colorName;
    }

}
