package dirtybloom.smartsensing.sys;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dirtybloom on 30/06/2015.
 */
public class SysBundle {

    public static final String INTENT_TO_BROADCAST_KEY = SysConstants.KEY + "EXTRA_INTENT";

    protected Bundle bundle;

    public SysBundle(Bundle bundle){
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Intent getIntentToBroadcast(){
        return (Intent) bundle.getParcelable(INTENT_TO_BROADCAST_KEY);
    }

    public void setIntentToBroadcast(Intent intent){
        bundle.putParcelable(INTENT_TO_BROADCAST_KEY, intent);
    }
}
