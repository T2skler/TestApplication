package com.example.testapplication;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int DELAY = 5000;
    private static final int UPDATE_INTERVAL = 1;

    private RelativeLayout container;
    private TextView textView;

    private int statusBarHeight, blue, red;
    private String locale;

    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        container = findViewById(R.id.container);
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(view -> {
            if (timer != null)
                timer.cancel();
        });
        locale = Locale.getDefault().getLanguage();
        blue = ContextCompat.getColor(this, R.color.blue);
        red = ContextCompat.getColor(this, R.color.red);
        int resId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resId);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX() - textView.getWidth() / 2;
            int y = (int) event.getY() - statusBarHeight - textView.getHeight() / 2;
            updatePosition(x, y);
            updateColor();
            startTimer();
        }
        return false;
    }

    private void updateColor() {
        switch (locale) {
            case "ru": {
                textView.setTextColor(blue);
                break;
            }
            case "en": {
                textView.setTextColor(red);
                break;
            }
        }
    }

    private void updatePosition(int x, int y) {
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(x, y, 0, 0);
        textView.setLayoutParams(layoutParams);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new AnimateTask(), DELAY, UPDATE_INTERVAL);
    }

    class AnimateTask extends TimerTask {
        private boolean directionDown = true;

        public void run() {
            runOnUiThread(() -> {
                int x = (int) textView.getX();
                int y = (int) textView.getY();
                if (y + textView.getHeight() >= container.getHeight()) {
                    directionDown = false;
                } else {
                    if (y <= 0) {
                        directionDown = true;
                    }
                }
                updatePosition(x, (directionDown) ? y + 1 : y - 1);
            });
        }
    }

}