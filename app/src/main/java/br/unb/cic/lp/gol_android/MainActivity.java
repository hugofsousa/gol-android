package br.unb.cic.lp.gol_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements CellsSizePickerDialog.SizePickerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.rule_options);
        Button mStartButton = (Button) findViewById(R.id.start);

        RadioButton rb = new RadioButton(this);
        rb.setText("REGRA");
        mRadioGroup.addView(rb);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CellsSizePickerDialog dialog = new CellsSizePickerDialog();
                dialog.setListener(MainActivity.this);
                dialog.show(getFragmentManager(), "CellsSizePickerDialog");
            }
        });
    }

    @Override
    public void onChooseSize(int width, int height) {
        Intent intent = new Intent(MainActivity.this, PerformanceActivity.class);
        intent.putExtra("WIDTH", width);
        intent.putExtra("HEIGHT", height);
        startActivity(intent);
    }
}
