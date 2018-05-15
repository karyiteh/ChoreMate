package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignTaskAdapter extends RecyclerView.Adapter<AssignTaskAdapter.ViewHolder> {

    private ArrayList<User> housemates;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout row;
        public TextView name;
        public CheckBox checkBox;

        private CircleImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView.findViewById(R.id.row_assign_task);
            name = (TextView) itemView.findViewById(R.id.txt_housemate_name);
            avatar = itemView.findViewById(R.id.img_avatar);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            // Set the onclick listener.
            row.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
            else {
                checkBox.setChecked(true);
            }
        }
    }

    /**
     * Constructor to feed the housemate list data.
     * @param myDataset The dataset that contains the housemates.
     */
    public AssignTaskAdapter(ArrayList<User> myDataset) {
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
    public AssignTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Creates a new view.
        // Need to create another xml file just for the item in the lists.
        // Then link it to here through inflate!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_assign_task, parent, false);
        return new AssignTaskAdapter.ViewHolder(view);
    }

    /**
     * Binds the viewholder to its data. Invoked by the layout manager.
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AssignTaskAdapter.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(housemates.get(position).getFirst_name());
        holder.avatar.setImageURI(housemates.get(position).getAvatar());
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
