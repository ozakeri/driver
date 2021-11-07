package gap.com.driver.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import gap.com.driver.R;
import gap.com.driver.widget.TouchImageView;

public class FullScreenActivity extends AppCompatActivity {
    private TouchImageView image_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        image_View = (TouchImageView) findViewById(R.id.image_View);
        final Bundle bundle = getIntent().getExtras();

        if (getIntent().getStringExtra("path") != null) {
           String path = getIntent().getStringExtra("path");
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            image_View.setImageBitmap(myBitmap);
        }
    }
}