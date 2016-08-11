package course.android.developer.sunshine;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivitySunshine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_sunshine);

        if(savedInstanceState!=null) {
            return;
        }

        final SunshineFragment sunshineFragment = new SunshineFragment();

        int container = R.id.container;

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(container,sunshineFragment).commit();
    }
}
