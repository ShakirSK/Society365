package main.society365.maneger.Sales_Register;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import main.society365.R;

import java.util.ArrayList;

import static main.society365.maneger.Sales_Register.SalesChildActivity.type;

public class SalesChildActivityAdapter extends RecyclerView.Adapter<SalesChildActivityAdapter.MyViewHolder>
{
    ArrayList text1,text2;
    Context context;
    private int[] mResources1;

    public SalesChildActivityAdapter(Context context,ArrayList text1,ArrayList text2) {
        this.text1 = text1;
        this.text2 = text2;
        this.context = context;
    }


    @NonNull
    @Override
    public SalesChildActivityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reportfragment,parent,false);
        return new SalesChildActivityAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesChildActivityAdapter.MyViewHolder holder, final int position) {
        /*ListItem listItem = name.get(position);
        holder.text1.setText(listItem.getText());*/
        holder.text1.setText((CharSequence) text1.get(position));
        holder.text3.setVisibility(View.VISIBLE);
        holder.text3.setText("\u20B9 "+(CharSequence) text2.get(position));
        holder.image1.setVisibility(View.GONE);
        //  holder.text3.setText((CharSequence) text3.get(position));
   //     holder.image1.setImageResource(mResources1[position]);
        // holder.image2.setImageResource(mResources2[position]);
/*
        Toast.makeText(context,text3.size(),Toast.LENGTH_SHORT).show();

        if(1.equals(text3.size()))
        {
            holder.text3.setVisibility(View.VISIBLE);
           holder.text3.setText((CharSequence) text3.get(position));
        }*/
  if (type.equals("from_receipt"))
            {
                holder.fullbody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SalesRegisterActivity.class);
                        intent.putExtra("type", "from_receipt");

                        intent.putExtra("partyledgertype", (CharSequence) text1.get(position));
                        context.startActivity(intent);
                    }
                });

            }
            else {
      holder.fullbody.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {


              Intent intent = new Intent(context, SalesRegisterActivity.class);
              intent.putExtra("type", "from_sales_wingwise");

              intent.putExtra("partyledgertype", (CharSequence) text1.get(position));
              context.startActivity(intent);
          }
      });

  }

    }

    @Override
    public int getItemCount() {
        return text1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView text1,text3;
        public ImageView image1,image2;
        LinearLayout fullbody;

        public MyViewHolder(View itemView) {
            super(itemView);

            image1=(ImageView) itemView.findViewById(R.id.image1);
            //  image2=(ImageView) itemView.findViewById(R.id.image2);

            text1=(TextView)itemView.findViewById(R.id.reportname);
            text3 =(TextView)itemView.findViewById(R.id.amountwingwise);

            fullbody = (LinearLayout)itemView.findViewById(R.id.fullbody);

        }


    }
}