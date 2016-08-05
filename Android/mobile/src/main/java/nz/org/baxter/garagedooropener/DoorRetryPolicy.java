package nz.org.baxter.garagedooropener;

import com.android.volley.DefaultRetryPolicy;

/**
 * Created by chris on 3/08/2016.
 */
public class DoorRetryPolicy extends DefaultRetryPolicy {

    public int retryCount = 0;

    @Override
    public int getCurrentTimeout() {
        return 60000;
    }

    @Override
    public int getCurrentRetryCount() {
        return 0;
    }
}
