package main.society365.maneger.Sales_Register;

/**
 * Created by Anas on 2/7/2019.
 */

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

import main.society365.R;

import static main.society365.maneger.Sales_Register.SalesRegisterActivity.ledgertype;


public class SalesRegisterActivityAdapter extends RecyclerView.Adapter<SalesRegisterActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<sales_register_model> list;

    private ContactsAdapterListener listener;
    private List<sales_register_model> contactListFiltered;

    public SalesRegisterActivityAdapter(Context context, List<sales_register_model> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_salesregister, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final sales_register_model movie = contactListFiltered.get(position);
        holder.flatno.setText(movie.getFlatno());
        holder.amount.setText("\u20B9"+movie.getAmount());
        holder.textTitle.setText(movie.getName());

        if (ledgertype.equals("from_receipt"))
        {
            holder.vno.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.vno.setText(movie.getVn());
            holder.date.setText(" | "+movie.getDate());
        }

/*

        if(movie.getName().contains("(")) {
            holder.flatno.setVisibility(View.GONE);
        }

*/


if (ledgertype.equals("from_sales_wingwise_summary")||ledgertype.equals("from_sales_account"))
{

}
else   if (ledgertype.equals("from_receipt"))
{
    holder.partylayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,PartySummary.class);
            intent.putExtra("vouchertype",movie.getType());
            intent.putExtra("vouchernumber",movie.getVn());
            intent.putExtra("ledgername",movie.getName());
            intent.putExtra("Period",movie.getDate1()+" - "+movie.getDate2());

            context.startActivity(intent);
        }
    });

}
else
{
    holder.partylayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String partname = movie.getName();
            Intent intent = new Intent(context,SalesRegisterDetailPage.class);
            intent.putExtra("partyledgername",partname);
            intent.putExtra("showpdfstatus","0");
            intent.putExtra("ledgerid",ledgertype);
            intent.putExtra("date1",movie.getDate1());
            intent.putExtra("date2",movie.getDate2());
            context.startActivity(intent);
        }
    });
}

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle,amount,flatno,date,vno;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            amount = itemView.findViewById(R.id.amount);

            flatno = itemView.findViewById(R.id.flatno);
            date = itemView.findViewById(R.id.date);
            vno = itemView.findViewById(R.id.vno);


            partylayout = itemView.findViewById(R.id.partylayout);
            if (ledgertype.equals("from_sales_wingwise_summary")||ledgertype.equals("from_sales_account"))
            {

            }
            else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // send selected contact in callback
                        listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                    }
                });
            }
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
                    List<sales_register_model> filteredList = new ArrayList<>();
                    for (sales_register_model row : list) {

                        if (ledgertype.equals("from_receipt"))
                        {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getAmount().toLowerCase().contains(charString.toLowerCase())
                                    ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())
                                    ||row.getVn().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        else { // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getAmount().toLowerCase().contains(charString.toLowerCase())
                                    ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

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
                contactListFiltered = (ArrayList<sales_register_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(sales_register_model contact);
    }


}