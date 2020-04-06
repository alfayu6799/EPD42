package home.com.epd42;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ThreeActivity extends AppCompatActivity {

    public ImageView goback2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //title隱藏
        setContentView(R.layout.activity_three);

        goback2 = (ImageView) findViewById(R.id.iv_back_2);
        goback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateUpTo(getIntent());
            }
        });
    }
}
