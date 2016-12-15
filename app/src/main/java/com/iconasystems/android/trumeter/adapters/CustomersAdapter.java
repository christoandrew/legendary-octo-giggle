package com.iconasystems.android.trumeter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.android.trumeter.AppPreferences;
import com.iconasystems.android.trumeter.api.ApiModule;
import com.iconasystems.android.trumeter.api.ApiService;
import com.iconasystems.android.trumeter.model.Customer;
import com.iconasystems.android.trumeter.model.MeterReading;
import com.iconasystems.android.trumeter.utils.InterfaceUtils;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trumeter.iconasystems.com.trumeter.R;

/**
 * Created by christoandrew on 10/7/16.
 */

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {
    private List<Customer> customerList;
    private Context _context;
    private OnItemClickListener onItemClickListener;
    private AppPreferences preferences;

    public CustomersAdapter(List<Customer> customerList, Context _context, AppPreferences preferences,
                            OnItemClickListener onItemClickListener) {
        this.customerList = customerList;
        this._context = _context;
        this.onItemClickListener = onItemClickListener;
        this.preferences = preferences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_item,
                parent, false);
        return new ViewHolder(v, preferences, _context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(customerList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCustomerName;
        public TextView mCustomerAddress;
        public View itemView;
        public TextView mCustomerId;
        public TextView mCustomerRoute;
        public TextView mCustomerRouteId;
        public TextView mCustomerZoneName;
        public ImageView mReadingIndicator;
        public ImageView redIndicator;
        public ImageView darkBlueIndicator;
        public ImageView lightBlueIndicator;
        public ImageView blackIndicator;
        public ImageView pinkIndicator;
        public ImageView defaultIndicator;
        public AppPreferences prefs;
        public Context context;
        private MeterReading lastReading;
        private TextView mPostingGroup;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, AppPreferences prefs, Context context) {
            super(itemView);
            InterfaceUtils.resetUI(itemView);
            this.prefs = prefs;
            this.itemView = itemView;
            this.context = context;
            mCustomerName = (TextView) itemView.findViewById(R.id.customer_name);
            mCustomerAddress = (TextView) itemView.findViewById(R.id.customer_address);
            mCustomerId = (TextView) itemView.findViewById(R.id.cust_id);
            mCustomerRoute = (TextView) itemView.findViewById(R.id.route_name);
            mCustomerRouteId = (TextView) itemView.findViewById(R.id.route_id);
            mCustomerZoneName = (TextView) itemView.findViewById(R.id.zone_name);
            mPostingGroup = (TextView) itemView.findViewById(R.id.posting_group);
            //mReadingIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator);
            redIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_red);
            darkBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_dark_blue);
            lightBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_light_blue);
            blackIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_black);
            pinkIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_pink);
            defaultIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_default);
        }

        public void bind(final Customer customer, final OnItemClickListener onItemClickListener) {
            mCustomerName.setText(customer.getName());
            mCustomerAddress.setText(customer.getAddress());
            mCustomerId.setText(String.valueOf(customer.getId()));
            mCustomerRoute.setText(customer.getMeter_number());
            mCustomerRouteId.setText(String.valueOf(customer.getId()));
            mPostingGroup.setText(customer.getBilling_group());

            final ApiService apiService = ApiModule.getClient().create(ApiService.class);
            Call<MeterReading> readingCall = apiService.getLastReading(customer.getMeter_id());
            readingCall.enqueue(new Callback<MeterReading>() {
                @Override
                public void onResponse(Call<MeterReading> call, Response<MeterReading> response) {
                    if (response.body() != null) {
                        lastReading = response.body();

                        final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();
                        //LocalDate createdAt = LocalDate.parse(lastReading.getCreated_at(), dateTimeFormatter);
                        //LocalDate now = LocalDate.now();

                        int readingCode = lastReading.getReading_code();

                        if (readingCode == 1) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 4) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 7) {
                            darkBlueIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 8) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 10) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 11) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 13) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 15) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 17) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 18) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        } else if (readingCode == 19) {
                            redIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        }else{
                            blackIndicator.setVisibility(View.VISIBLE);
                            defaultIndicator.setVisibility(View.GONE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<MeterReading> call, Throwable t) {
                    Log.d("Trumeter Error", t.getMessage());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(customer);
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }
}
