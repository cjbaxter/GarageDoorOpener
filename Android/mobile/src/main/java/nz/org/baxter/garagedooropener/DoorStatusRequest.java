package nz.org.baxter.garagedooropener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 16/07/2016.
 */
public class DoorStatusRequest extends JsonObjectRequest {
    private String AuthHeader;
    public DoorStatusRequest(String authHeader, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, "http://192.168.1.14/arduino/status/0", null, listener, errorListener);
        AuthHeader = authHeader;
        this.setRetryPolicy(new DoorRetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }
        });
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AuthHeader);
        return headers;
    }
}
