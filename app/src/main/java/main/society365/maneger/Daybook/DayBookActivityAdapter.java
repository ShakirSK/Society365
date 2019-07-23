package main.society365.maneger.Daybook;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import main.society365.R;
import main.society365.maneger.partyledger.PartySummary;

import java.util.ArrayList;
import java.util.List;


public class DayBookActivityAdapter extends RecyclerView.Adapter<DayBookActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<daybookmodel> list;

    private ContactsAdapterListener listener;
    private List<daybookmodel> contactListFiltered;

    public DayBookActivityAdapter(Context context, List<daybookmodel> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_daybookactivity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final daybookmodel movie = contactListFiltered.get(position);

        holder.textTitle.setText(movie.getPartyname());
        holder.date.setText(String.valueOf(movie.getDate()));

        holder.amount.setText("\u20B9"+movie.getAmount());
        holder.vochurtype.setText(movie.getVouchertype());
        holder.vouchernumber.setText(movie.getVouchernumber());

        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = movie.getVouchertype();
                String number = movie.getVouchernumber();
                String name = movie.getPartyname();

                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",type);
                intent.putExtra("vouchernumber",number);
                intent.putExtra("ledgername",name);
                intent.putExtra("Period",movie.getDate1()+" - "+movie.getDate2());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, vouchernumber,vochurtype,amount,date;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            vouchernumber = itemView.findViewById(R.id.vouchernumber);
            vochurtype = itemView.findViewById(R.id.vochurtype);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);

            partylayout = itemView.findViewById(R.id.partylayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<daybookmodel> filteredList = new ArrayList<>();
                    for (daybookmodel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPartyname().toLowerCase().contains(charString.toLowerCase()) || row.getVouchernumber().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<daybookmodel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(daybookmodel contact);
    }


}