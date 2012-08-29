package org.txsys.droidloc;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements LocationListener {
    
    private LocationManager lmgr;
    private TextView outputView;
    private String bestProvider;

    private static final String[] A = { "n/a", "fine", "coarse" };
    private static final String[] P = { "n/a", "low", "medium", "high" };
    private static final String[] S = { "out of service", "temporarily unavailable", "available" };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        lmgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        outputView = (TextView) this.findViewById(R.id.output);
        
        log("Location providers: ");
        this.dumpProviders();
        
        Criteria criteria = new Criteria();
        bestProvider = lmgr.getBestProvider(criteria, true);
        log("\nBest Provider is: " + bestProvider);
        
        log("\nLocations :");
        Location location = lmgr.getLastKnownLocation(bestProvider);
        dumpLocation(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        lmgr.requestLocationUpdates(this.bestProvider, 15000, 1, this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        lmgr.removeUpdates(this);
    }
    
    public void onProviderEnabled(String provider) {
        log("\nProvider Enabled: "+provider);
    }
    
    public void onProviderDisabled(String provider) {
        log("\nProvider Disabled: "+provider);
    }
    
    public void onLocationChanged(Location location) {
        dumpLocation(location);
    }
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
        log("\nProvider status changed: " + provider + ", status="
                + S[status] + ", extras=" + extras);
    }
    
    private void log(String string) {
        outputView.append(string+"\n");
    }
    
    private void dumpProvider(String provider) {
        LocationProvider info = lmgr.getProvider(provider);
        StringBuilder sb = new StringBuilder();
        sb.append("LocationProvider[")
        .append("name=")
        .append(info.getName())
        .append(",enabled=")
        .append(lmgr.isProviderEnabled(provider))
        .append(",getAccuracy=")
        .append(A[info.getAccuracy()])
        .append("]");
        
        log(sb.toString());
    }
    
    private void dumpProviders() {
        List<String> providers = lmgr.getAllProviders();
        for (String provider:providers) {
            dumpProvider(provider);
        }
    }
    
    private void dumpLocation(Location location) {
        if (location==null) {
            log("\nLocation unkown.");
        } else {
            log("\n"+location.toString());
        }
    }

}
