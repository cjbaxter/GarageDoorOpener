package nz.org.baxter.garagedooropener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 18/07/2016.
 */
public class DoorOpenRequest extends StringRequest {
    private String AuthHeader;

    public DoorOpenRequest(String authHeader) {
        super(Request.Method.GET, "http://192.168.1.14/arduino/door/0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        AuthHeader = authHeader;
        this.setRetryPolicy(new DoorRetryPolicy());
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AuthHeader);
        return headers;
    }
}
