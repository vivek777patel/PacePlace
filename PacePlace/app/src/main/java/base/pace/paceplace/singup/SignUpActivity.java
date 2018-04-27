package base.pace.paceplace.singup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import base.pace.paceplace.R;
import base.pace.paceplace.user.UserProfileFragment;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.user_registration);
        setContentView(R.layout.activity_register_user);
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        getFragmentManager().beginTransaction().add(R.id.userProfileScrollView, userProfileFragment).commit();
    }

}
