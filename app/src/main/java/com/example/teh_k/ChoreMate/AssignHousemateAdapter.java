package com.example.teh_k.ChoreMate;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignHousemateAdapter extends RecyclerView.Adapter<AssignHousemateAdapter.ViewHolder> {

    //public static final int LARGE_ARRAY_SIZE = 50;

    private ArrayList<User> housemates;

    // sparse boolean array for checking the state of the items
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    /**
     * Clears the data in the adapter.
     */
    public void clear() {
        housemates.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout row;
        public TextView name;
        public CheckBox checkBox;

        private CircleImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView.findViewById(R.id.row_assign_housemate);
            name = (TextView) itemView.findViewById(R.id.txt_housemate_name);
            avatar = itemView.findViewById(R.id.img_avatar);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            // Set the onclick listener.
            row.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Log.d("AssignHousemateAdapter", "Adapter position chosen is: " + adapterPosition);
            if (!itemStateArray.get(adapterPosition, false)) {
                checkBox.setChecked(true);
                itemStateArray.put(adapterPosition, true);
                Log.d("AssignHousemateAdapter", "Check box at position " + adapterPosition +
                        " set to true.");
            }
            else {
                checkBox.setChecked(false);
                itemStateArray.put(adapterPosition, false);
                Log.d("AssignHousemateAdapter", "Check box at position " + adapterPosition +
                        " set to false.");
            }
        }

        void bind(int position) {
            // Use the boolean array to check
            if(!itemStateArray.get(position, false)) {
                checkBox.setChecked(false);
            }
            else {
                checkBox.setChecked(true);
            }
        }
    }

    /**
     * Getter method for itemStateArray.
     * @return  The item state array.
     */
    public SparseBooleanArray getItemStateArray() {
        return itemStateArray;
    }

    /**
     * Constructor to feed the housemate list data.
     * @param myDataset The dataset that contains the housemates.
     */
    public AssignHousemateAdapter(ArrayList<User> myDataset) {

        housemates = myDataset;

    }

    /**
     * Create new views. This method is invoked by the LayoutManager.
     * Does not actually set the view's contents.
     * @param parent    The ViewGroup into which the new View will be added after it is bound to an
     *                  adapter position.
     * @param viewType  The view type of the new view.
     * @return  A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public AssignHousemateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Creates a new view.
        // Need to create another xml file just for the item in the lists.
        // Then link it to here through inflate!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assign_housemate, parent, false);
        return new AssignHousemateAdapter.ViewHolder(view);
    }

    /**
     * Binds the viewholder to its data. Invoked by the layout manager.
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AssignHousemateAdapter.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(housemates.get(position).getFirst_name());
        Picasso.get().load(Uri.parse(housemates.get(position).getAvatar())).into(holder.avatar);
        holder.bind(position);
    }

    /**
     * Gets the number of items in the list. Invoked by the layout manager.
     * @return  The number of items in the dataset.
     */
    @Override
    public int getItemCount() {
        return housemates.size();
    }

}
