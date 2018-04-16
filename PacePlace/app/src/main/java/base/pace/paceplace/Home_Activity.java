package base.pace.paceplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        CourseList_Fragement courseList_fragement = new CourseList_Fragement();
        getFragmentManager().beginTransaction().replace(R.id.home_fagement_view_RL,courseList_fragement).commit();
    }
}
