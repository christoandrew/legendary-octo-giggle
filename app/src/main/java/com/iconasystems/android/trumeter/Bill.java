package com.iconasystems.android.trumeter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.Customer;
import com.iconasystems.android.trumeter.model.Meter;
import com.iconasystems.android.trumeter.model.MeterReader;
import com.iconasystems.android.trumeter.model.MeterReading;
import com.iconasystems.android.trumeter.utils.MapUtils;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;


public class Bill extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView mMeterPicture;
    private Button mTakePicture;
    private Spinner spinner;
    private TextView mCustomerName;
    private TextView mAddress;
    private EditText mCurrentReading;
    private TextView mMeterNumber;
    private FloatingActionButton mBill;
    private TextView mBillingPeriod;
    private int meterId;
    private float currentReading = 0;
    private float previousReading = 0;
    private float readingLatitude = 0;
    private float readingLongitude = 0;
    private float meterLatitude = 0;
    private float meterLongitude = 0;
    private float distance = 0;
    private float quantity = 0;
    private int meterReaderId;
    private int customerId;
    private int readingCode;
    private String meterPhoto = null;
    private Meter meter = null;
    private MeterReading lastReading = null;
    private int billingPeriod;
    private AppPreferences prefs;
    private int billingPeriodId;
    private ProgressDialog progressDialog;
    private TextView mBillingGroup;
    private boolean isRegularReading;
    private float lastReadingQuantity;
    private EditText mReason;
    private float minus10percent;
    private float plus10percent;
    private SwitchCompat isMetered;
    private String isMeteredEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        final ApiService apiService = ApiModule.getClient().create(ApiService.class);
        mCustomerName = (TextView) findViewById(R.id.customer_bill_name);
        mAddress = (TextView) findViewById(R.id.customer_bill_address);
        mBill = (FloatingActionButton) findViewById(R.id.fab_bill);
        mCurrentReading = (EditText) findViewById(R.id.meter_reading);
        mMeterNumber = (TextView) findViewById(R.id.meter_number);
        mBillingGroup = (TextView) findViewById(R.id.posting_group);
        isMetered = (SwitchCompat) findViewById(R.id.is_metered);
        isMetered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isMeteredEntry = "Yes";
                }else{
                    isMeteredEntry = "No";
                }
            }
        });
        mReason = (EditText) findViewById(R.id.reason);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving meter reading");
        progressDialog.setCancelable(true);


        prefs = AppPreferences.get(this);
        final MeterReader reader = prefs.getUser();
        billingPeriodId = prefs.getBillingPeriod().getId();

        Location location = MapUtils.getLocation(this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, this);

        readingLatitude = (float) location.getLatitude();
        readingLongitude = (float) location.getLongitude();
        String customerJson = getIntent().getStringExtra("customer");

        Gson gson = new Gson();
        Customer customer = gson.fromJson(customerJson, Customer.class);

        mCustomerName.setText(customer.getName());
        mAddress.setText(customer.getAddress());
        mMeterNumber.setText(customer.getMeter_number());
        mBillingGroup.setText(customer.getBilling_group());
        customerId = customer.getId();

        spinner = (Spinner) findViewById(R.id.billing_code);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.billing_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        readingCode = 1;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.VISIBLE);

                        break;
                    case 1:
                        readingCode = 4;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        readingCode = 7;
                        mCurrentReading.setVisibility(View.VISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        readingCode = 8;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        readingCode = 10;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        readingCode = 11;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                        readingCode = 13;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        readingCode = 15;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 8:
                        readingCode = 17;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 9:
                        readingCode = 18;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    case 10:
                        readingCode = 19;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        readingCode = 0;
                        mCurrentReading.setVisibility(View.INVISIBLE);
                        mReason.setVisibility(View.INVISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMeterPicture = (ImageView) this.findViewById(R.id.meter_picture);
        mTakePicture = (Button) this.findViewById(R.id.photo_button);

        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        mBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progressDialog.show();
                Call<Meter> getMeterCall = apiService.getMeter(customerId);
                getMeterCall.enqueue(new Callback<Meter>() {
                    @Override
                    public void onResponse(Call<Meter> call, Response<Meter> response) {
                        meter = response.body();
                        //Log.d("Meter Details", meter.toString());
                        meterId = meter.getId();
                        meterLatitude = meter.getLatitude();
                        meterLongitude = meter.getLongitude();

                        float[] results = new float[1];
                        Location.distanceBetween(meterLatitude, meterLongitude, readingLatitude, readingLongitude, results);
                        distance = results[0];
                        meterReaderId = reader.getId();

                        Call<MeterReading> meterReadingCall = apiService.getLastReading(meter.getId());
                        meterReadingCall.enqueue(new Callback<MeterReading>() {
                            @Override
                            public void onResponse(Call<MeterReading> call,
                                                   Response<MeterReading> response) {
                                if (response.body() != null) {
                                    lastReading = response.body();
                                    lastReadingQuantity = lastReading.getQuantity();

                                    Log.d("Meter Reading Response", lastReading.toString());
                                    previousReading = lastReading.getCurrent_reading();
                                }

                                String currentReadingRead = mCurrentReading.getText().toString();
                                if (!TextUtils.isEmpty(currentReadingRead)) {
                                    currentReading = Float.parseFloat(mCurrentReading.getText().toString());
                                    if (currentReading < previousReading) {
                                        Snackbar.make(v, "Current reading is less than previous reading!",
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                } else if (TextUtils.isEmpty(currentReadingRead)) {
                                    currentReading = previousReading;
                                }

                                Log.d("Previous Reading", "= " + previousReading);
                                if (currentReading > previousReading) {
                                    quantity = currentReading - previousReading;
                                    plus10percent = (float) (lastReadingQuantity + (0.1 * lastReadingQuantity));
                                    minus10percent = (float) (lastReadingQuantity - (0.1 * lastReadingQuantity));
 
                                    Log.d("Range", " " + plus10percent + " " + minus10percent);

                                    if (quantity > plus10percent || quantity < minus10percent) {
                                        isRegularReading = false;
                                    } else {
                                        isRegularReading = true;
                                    }
                                } else {
                                    quantity = 0;
                                    isRegularReading = true;

                                }


                                String reason = null;
                                if (mReason.getVisibility() == View.VISIBLE) {
                                    reason = mReason.getText().toString();
                                }
                              
                                String expected_range = String.valueOf(minus10percent).concat(" to ").concat(String.valueOf(plus10percent));

                                MeterReading newReading = new MeterReading();
                                newReading.setMeter_id(meterId);
                                newReading.setCurrent_reading(currentReading);
                                newReading.setPrevious_reading(previousReading);
                                newReading.setPhoto(meterPhoto);
                                newReading.setReading_code(readingCode);
                                newReading.setLatitude(readingLatitude);
                                newReading.setLongitude(readingLongitude);
                                newReading.setMeter_reader_id(meterReaderId);
                                newReading.setDistance(distance);
                                newReading.setQuantity(quantity);
                                newReading.setBilling_period_id(billingPeriodId);
                                newReading.setRegular(isRegularReading);
                                newReading.setReason(reason);
                                newReading.setPosted("No");
                                newReading.setCustomer_details("Correct");
                                newReading.setPrevious_consumption(lastReadingQuantity);
                                newReading.setExpected_range(expected_range);
                                newReading.setIs_metered_entry("No");

                                Log.d("Meter Reading", newReading.toString());
                                //Log.d("Meter reading data", newReading.toString());
                                final Call<MeterReading> postReadingCall = apiService.saveReading(newReading);
                                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.dismiss();
                                        postReadingCall.cancel();
                                    }
                                });
                                postReadingCall.enqueue(new Callback<MeterReading>() {
                                    @Override
                                    public void onResponse(Call<MeterReading> call, Response<MeterReading> response) {
                                        if (response.body() != null) {
                                            progressDialog.dismiss();
                                            Snackbar snackbar = Snackbar.make(v, "Successfully posted"
                                                    , Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                            snackbar.setCallback(new Snackbar.Callback() {
                                                @Override
                                                public void onDismissed(Snackbar snackbar, int event) {
                                                    super.onDismissed(snackbar, event);
                                                    startActivity(new Intent(getApplicationContext(), Towns.class));
                                                }
                                            });
                                        } else {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MeterReading> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<MeterReading> call, Throwable t) {

                            }
                        });
                    }


                    @Override
                    public void onFailure(Call<Meter> call, Throwable t) {

                    }
                });

            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mMeterPicture.setImageBitmap(photo);
            meterPhoto = encodeTobase64(photo);

        }
    }

    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.billing_code:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
