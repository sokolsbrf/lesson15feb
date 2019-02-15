package com.example.sokol.mapslesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GeoView {

    private static GeoPresenter presenter;

    private TextView whereView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whereView = findViewById(R.id.where);

        if (presenter == null) {
            presenter = new GeoPresenter(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attach(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detach();

        if (isDestroyed()) {
            presenter.destroy();
            presenter = null;
        }
    }

    @Override
    public void showLocation(String address) {
        whereView.setText(address);
    }
}
