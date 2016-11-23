package gpstracker.developers.abi.com.abigps;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView;
    LinearLayout line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        G.overrideFont(getApplicationContext(), "SERIF", "Yekan.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        G.context=this;
        ViewGroup group = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        Font.setAllTextView(group);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CustomAlert();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView=(TextView) findViewById(R.id.textView);

        line = (LinearLayout)findViewById(R.id.line);

        mainUI();
    }
    //84467
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //httpReq();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void gps(String title,String message) {
        LocationManager lm = null;
        LocationListener ll = new gps();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();

        if(gps.lat==0.0 || gps.lon==0.0 )
        {
            Toast.makeText(getApplicationContext(), "اطلاعات ماهواره ای هنوز دریافت نشده است", Toast.LENGTH_LONG).show();
        }

        else
        {
            SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currentDateandTime = databaseDateTimeFormate.format(new Date());

            GpsLoger gpsLoger = new GpsLoger();
            gpsLoger.setTitle(title);
            gpsLoger.setMessage(message);
            gpsLoger.setLatitude(gps.lat);
            gpsLoger.setLongitude(gps.lon);
            gpsLoger.setTimestamp(currentDateandTime);
            gpsLoger.setJalaliTime("1395/8/15");
            gpsLoger.setContact_name("ایمان بهمنی");
            gpsLoger.setContact_number("09179168433");
            gpsLoger.setStatus("تحویل داده شد");
            gpsLoger.save();
            Toast.makeText(getApplicationContext(), ""+gps.lat+","+gps.lon, Toast.LENGTH_LONG).show();
            System.out.println(""+gps.lat+","+gps.lon);
        }
    }
    private  void geocoding(double LATITUDE,double LONGITUDE)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)+"\n"+returnedAddress.getSubLocality()+"\n"+returnedAddress.getFeatureName()).append("");
                }
                //et_lugar.setText(strReturnedAddress.toString());
                Toast.makeText(getApplicationContext(),""+strReturnedAddress.toString(),Toast.LENGTH_LONG).show();
            }
            else {
                //et_lugar.setText("No Address returned!");
                Toast.makeText(getApplicationContext(),"No Address returned!",Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //et_lugar.setText("Canont get Address!");
            Toast.makeText(getApplicationContext(),"Canont get Address!",Toast.LENGTH_LONG).show();
        }
    }
    private  void httpReq()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://shirazapp.com/gps.php", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String value = new String(response);
                System.out.println("GPS: "+value.toString());
                textView.setText(value.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    private  void CustomAlert()
    {
        G.overrideFont(getApplicationContext(), "SERIF", "Yekan.ttf");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_alert);
        LinearLayout linearlayoutContact  = (LinearLayout) dialog.findViewById(R.id.linearlayoutContact);
        final EditText title    = (EditText) dialog.findViewById(R.id.title);
        final EditText message  = (EditText) dialog.findViewById(R.id.message);

        G.context=this;
        ViewGroup group = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        Font.setAllTextView(group);

        dialog.show();
        final Button buttonSelection = (Button) dialog.findViewById(R.id.email_sign_in_button);
        buttonSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps(title.getText().toString(),message.getText().toString());
                dialog.dismiss();
            }
        });
    }
    private void mainUI()
    {
        final List<GpsLoger> books = GpsLoger.listAll(GpsLoger.class);

        for(int i = 0; i <books.size(); i++)
        {
                TableRow tr2 = new TableRow(MainActivity.this);
                line.addView(tr2);

                ImageView imageDate = new ImageView(MainActivity.this);
                imageDate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams par = (LinearLayout.LayoutParams)imageDate.getLayoutParams();
                par.setMargins(5, 5, 5, 5);
                imageDate.setImageResource(R.drawable.ic_menu_camera);
                imageDate.setLayoutParams(par);


                TextView TextDate = new TextView(MainActivity.this);
                TextDate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams p3 = (LinearLayout.LayoutParams)TextDate.getLayoutParams();
                p3.setMargins(5, 5, 5, 5);
                TextDate.setGravity(Gravity.CENTER);
                TextDate.setTextSize(10);
                TextDate.setText(books.get(i).getTimestamp());
                TextDate.setLayoutParams(p3);

                ImageView ImgCount = new ImageView(MainActivity.this);
                ImgCount.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams parImgCount = (LinearLayout.LayoutParams)ImgCount.getLayoutParams();
                parImgCount.setMargins(5, 5, 5, 5);
                ImgCount.setImageResource(R.drawable.ic_menu_gallery);
                ImgCount.setLayoutParams(parImgCount);


                TextView TextCount = new TextView(MainActivity.this);
                TextCount.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams p334 = (LinearLayout.LayoutParams)TextCount.getLayoutParams();
                p334.setMargins(5, 5, 5, 5);
                TextCount.setGravity(Gravity.CENTER);
                TextCount.setTextSize(10);
                TextCount.setText(""+books.get(i).getStatus());
                TextCount.setLayoutParams(p334);


                TextView TextTitle = new TextView(MainActivity.this);
                TextTitle.setPadding(5,5,10,5);
                TextTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)TextTitle.getLayoutParams();
                param.setMargins(5, 5, 5, 5);
                TextTitle.setGravity(Gravity.RIGHT);
                TextTitle.setTextSize(14);
                TextTitle.setText(books.get(i).getTitle());
                TextTitle.setLayoutParams(param);

                tr2.addView(imageDate);
                tr2.addView(TextDate);
                tr2.addView(ImgCount);
                tr2.addView(TextCount);
                tr2.addView(TextTitle);
        }
    }
}

