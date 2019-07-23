package main.society365.maneger.partyledger;


/**
 * Created by Anas on 1/30/2019.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import main.society365.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by ankit on 27/10/17.
 */

public class PartyLedgerDetailPageAdapter extends RecyclerView.Adapter<PartyLedgerDetailPageAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partydetailmodel> list;

    private ContactsAdapterListener listener;
    private List<partydetailmodel> contactListFiltered;

    public PartyLedgerDetailPageAdapter(Context context, List<partydetailmodel> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.partydetailitempage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final partydetailmodel movie = contactListFiltered.get(position);

        holder.textTitle.setText(movie.getVoucher_number());
      /*  if(movie.getVoucher_number().equals(""))
        {
            holder.partylayout.setVisibility(View.GONE);
        }*/
        holder.textRating.setText(String.valueOf(movie.getDate()) +" | "+ String.valueOf(movie.getVoucher_type()));

        holder.amount.setText("\u20B9 "+movie.getAmount()+" | "+ String.valueOf(movie.getDr_cr()));
        if(movie.getDr_cr().equals("-"))
        {
            holder.amount.setText("\u20B9 "+movie.getAmount()+" | "+ "Cr");
        }
        holder.preamount.setText("("+"\u20B9 "+movie.getPrev_balance()+")");
        holder.preamount.setTextColor(Color.parseColor("#42C0FB"));

        ArrayList arrayVT = new ArrayList<>(Arrays.asList("Bill","Receipt","Journal"));
        if("Bill".equals(movie.getVoucher_type()))
        {
            holder.amount.setTextColor(Color.parseColor("#008000"));
        }
        else if("Receipt".equals(movie.getVoucher_type())||"Journal".equals(movie.getVoucher_type()))
        {
            if(movie.getDr_cr().equals("Dr"))
            {
                holder.amount.setTextColor(Color.parseColor("#008000"));
            }
            else
            {
                holder.amount.setTextColor(Color.parseColor("#FF0000"));
            }
        }
        else if(!arrayVT.contains(movie.getVoucher_type()))
        {
            if(movie.getDr_cr().equals("Dr"))
            {
                holder.amount.setTextColor(Color.parseColor("#008000"));
            }
            else
            {
                holder.amount.setTextColor(Color.parseColor("#FF0000"));
            }
        }

        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = movie.getVoucher_type();
                String number = movie.getVoucher_number();

                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",type);
                intent.putExtra("vouchernumber",number);
                intent.putExtra("ledgername","test");
                intent.putExtra("Period",movie.getPeriod());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating,amount,preamount;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            textRating = itemView.findViewById(R.id.flatno);
            amount = itemView.findViewById(R.id.amount);
            preamount = itemView.findViewById(R.id.preamount);

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
                    List<partydetailmodel> filteredList = new ArrayList<>();
                    for (partydetailmodel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVoucher_type().toLowerCase().contains(charString.toLowerCase()) || row.getVoucher_number().contains(charSequence) ||
                                row.getNarration().contains(charSequence) ) {
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
                contactListFiltered = (ArrayList<partydetailmodel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(partydetailmodel contact);
    }


}