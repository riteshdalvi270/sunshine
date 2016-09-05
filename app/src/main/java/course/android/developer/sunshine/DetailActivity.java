package course.android.developer.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceHolderFragment())
                    .commit();
        }
    }

    public static class PlaceHolderFragment extends Fragment {

        public PlaceHolderFragment() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            final Intent intent = getActivity().getIntent();

            final View view = inflater.inflate(R.layout.fragment_detail, container, false);

            final String forecast = intent.getExtras().getString("forecast");

            final TextView textView = (TextView)view.findViewById(R.id.textView);

            textView.setText(forecast);

            return view;
        }
    }
}
