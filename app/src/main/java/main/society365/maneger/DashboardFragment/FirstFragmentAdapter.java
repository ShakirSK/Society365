package main.society365.maneger.DashboardFragment;


        import android.content.Context;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import main.society365.R;
        import main.society365.maneger.Cash_Bank_Balance.CashBankBalanceActivity;
        import main.society365.maneger.Outstanding_Receivable.ReceivableActivity;
        import main.society365.maneger.PaymentReports.PaymentReportActivity;
        import main.society365.maneger.Sales_Register.SalesChildActivity;
        import main.society365.maneger.Trail_Balance.TrailBalanceActivity;

        import java.util.ArrayList;

public class FirstFragmentAdapter extends RecyclerView.Adapter<FirstFragmentAdapter.MyViewHolder>
{
    ArrayList text1;
    ArrayList text3;
    Context context;
    private int[] mResources1;


    public FirstFragmentAdapter(Context context,ArrayList text1,ArrayList text3,int[] resources1) {
        this.text1 = text1;
        this.text3 = text3;
        this.context = context;
        this.mResources1 = resources1;

    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_firstfragment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        /*ListItem listItem = name.get(position);
        holder.text1.setText(listItem.getText());*/
        holder.reportname.setText((CharSequence) text1.get(position));
        holder.amount.setText("\u20B9 "+(CharSequence) text3.get(position));
        holder.image1.setImageResource(mResources1[position]);

        if(position==3 || position==4 || position==5 ||position==6 )
        {
            holder.amount.setVisibility(View.GONE);

        }

        // holder.image2.setImageResource(mResources2[position]);
/*
        Toast.makeText(context,text3.size(),Toast.LENGTH_SHORT).show();

        if(1.equals(text3.size()))
        {
            holder.text3.setVisibility(View.VISIBLE);
           holder.text3.setText((CharSequence) text3.get(position));
        }*/
        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onItemClick(View v, int pos) {

                //INTENT OBJ
     /*           if(position==1)
                {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.container,YOUR_FRAGMENT_NAME,YOUR_FRAGMENT_STRING_TAG);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else*/ if(position==1)
                {
                    Intent intent = new Intent(context,SalesChildActivity.class);
                    intent.putExtra("type","from_sales");
                    context.startActivity(intent);
                }
                else   if(position==2)
                {
                    /*
                        RecieptActivity*/
                    Intent intent = new Intent(context,SalesChildActivity.class);
                    intent.putExtra("type","from_receipt");
                    context.startActivity(intent);
                }
                else if(position==0)
                {
                    Intent intent = new Intent(context,PaymentReportActivity.class);
                    context.startActivity(intent);
                }
                else if(position==3)
                {
                    Intent intent = new Intent(context,CashBankBalanceActivity.class);
                    context.startActivity(intent);
                }
                else   if(position==4)
                {
                    Intent intent = new Intent(context,TrailBalanceActivity.class);
                    intent.putExtra("type","ledger");
                    context.startActivity(intent);
                }
                else   if(position==5) {
                    Intent intent = new Intent(context, TrailBalanceActivity.class);
                    intent.putExtra("type", "accounts");
                    context.startActivity(intent);
                }
                else if(position==6)
                {
                    Intent intent = new Intent(context,ReceivableActivity.class);
                    context.startActivity(intent);
                }
             /*   else if(position==3)
                {

                    Intent intent = new Intent(context,DayBookActivity.class);
                    context.startActivity(intent);

                   *//* MainActivity mainActivity = new MainActivity();
                    mainActivity.callpartyledger_fragment();*//*
                }
                Log.d("itemcliked", String.valueOf(text1.get(0)) + "    "+ String.valueOf(text3.get(0)));

*/

                //ADD DATA TO OUR INTENT
                // i.putExtra("Name", (Bundle) text1.get(position));
                // i.putExtra("Position", (Bundle) text3.get(position));

                //START DETAIL ACTIVITY
                //context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return text1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public TextView amount,reportname;
        private ItemClickListener itemClickListener;
        public ImageView image1,image2;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image1=(ImageView) itemView.findViewById(R.id.image1);
            //  image2=(ImageView) itemView.findViewById(R.id.image2);

            amount=(TextView)itemView.findViewById(R.id.amount);
            reportname =(TextView)itemView.findViewById(R.id.reportname);


        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;

        }


    }
}

/*
public class FirstFragmentAdapter extends RecyclerView.Adapter {
    ArrayList name, image;
    TabActivity_1 context;

    public FirstFragmentAdapter(TabActivity_1 context, ArrayList name, ArrayList image) {

        this.context = context;
        this.name = name;
        this.image = image;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabfirstitems, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
       MyViewHolder.name.setText((Integer) name.get(position));
        MyViewHolder.image.setImageResource((Integer) image.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                */
/*Toast.makeText(context, name.get(position), Toast.LENGTH_SHORT).show();*//*

            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       static TextView name;
       static ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

}
*/



/*
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabfirstitems, parent, false);

        return new FirstFragmentAdapter.MyViewHolder(view);
    }



    @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //set the data in items
            holder.name.setText(name.get(position));
            holder.image.setImageResource(image.get(position));
// implement setOnClickListener event on item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
// open another activity on item click
               *//* Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("image", personImages.get(position)); // put image data in Intent
                context.startActivity(intent); // start Intent*//*
                }
            });


        }

        @Override
        public int getItemCount() {
            return name.size();
        }*/












