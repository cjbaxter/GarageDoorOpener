package nz.org.baxter.garagedooropener;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DoorActivity extends AppCompatActivity {

    private static final int ON_THRESHOLD = 100;

    private Handler mHandler;

    public DoorStatusRequest statusRequest;
    public DoorOpenRequest openRequest;
    public RequestQueue queue;

    public ImageView NetStatusIcon;
    public ImageView DoorStatusIcon;
    public TextView NetStatusText;
    public TextView DoorStatusText;
    public ImageButton DoorButton;

    private boolean Init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        NetStatusIcon = (ImageView)findViewById(R.id.net_status_image);
        DoorStatusIcon = (ImageView)findViewById(R.id.door_status_image);
        NetStatusText = (TextView)findViewById(R.id.net_status_text);
        DoorStatusText = (TextView)findViewById(R.id.door_status_text);
        DoorButton = (ImageButton)findViewById(R.id.door_button);
        DoorButton.setEnabled(false);

        queue = Volley.newRequestQueue(this);
        mHandler = new Handler();
        openRequest = new DoorOpenRequest(getString(R.string.auth_header_string));
        statusRequest = new DoorStatusRequest(
                getString(R.string.auth_header_string),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject status) {
                        try {
                            int redValue = status.getInt("red");

                            if (redValue > ON_THRESHOLD) {
                                DoorStatusIcon.setImageResource(R.drawable.garage_open);
                                DoorStatusText.setText(R.string.RedSensorOnValue);
                            } else {
                                DoorStatusIcon.setImageResource(R.drawable.garage_closed);
                                DoorStatusText.setText(R.string.RedSensorOffValue);
                            }

                            NetStatusIcon.setImageResource(R.drawable.wifi_connected);
                            NetStatusText.setText(R.string.CommsStatusOk);

                            if (Init) {
                                DoorButton.setImageResource(R.drawable.door_icon_blue);
                                DoorButton.setEnabled(true);
                                Init = false;
                            }

                        } catch (JSONException e) {
                            NetStatusIcon.setImageResource(R.drawable.wifi_disconnected);
                            NetStatusText.setText(R.string.CommsStatusJsonError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetStatusIcon.setImageResource(R.drawable.wifi_disconnected);
                        NetStatusText.setText(R.string.CommsStatusFail);
                    }
                }

        );

        DoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(openRequest);
            }
        });

        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (queue != null && statusRequest != null) {
                    queue.add(statusRequest);
                }
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                int mInterval = 5000;
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }
}
