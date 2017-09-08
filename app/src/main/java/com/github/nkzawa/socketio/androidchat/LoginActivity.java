package com.github.nkzawa.socketio.androidchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via username.
 */
public class LoginActivity extends Activity {

    private EditText mUsernameView;

    private  EditText returnText;

    private String mUsername;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();



        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username_input);
        returnText= (EditText) findViewById(R.id.return_input);


      /*  mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSocket.on("login", onLogin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("login", onLogin);
    }


    private void attemptLogin() {


        final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, "正在通信，请稍后","");
        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }

        mUsername = username;

        returnText.setText("nihao");
        // perform the user login attempt.
        mSocket.emit("add user", username);
        pd.dismiss();
    }




    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            try {
                JSONObject data = (JSONObject) args[0];
                String username;
                int numUsers;
                username = data.getString("hello");
                returnText.setText(username);

            } catch (Exception e) {
                return;
            }





//            int numUsers;
//            try {
//                numUsers = data.getInt("numUsers");
//            } catch (JSONException e) {
//                return;
//            }
//
//            Intent intent = new Intent();
//            intent.putExtra("username", mUsername);
//            intent.putExtra("numUsers", numUsers);
//            setResult(RESULT_OK, intent);
//            finish();
        }
    };
}



