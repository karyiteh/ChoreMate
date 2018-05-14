package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * HousemateAdapter is a class that links the data from the dataset into the UI.
 * Basically creates the items in the list show on the screen.
 */
public class HousemateAdapter extends RecyclerView.Adapter<HousemateAdapter.ViewHolder> {

    ArrayList<User> housemates;

    /**
     * Provides reference to the views for each data item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Each item in this case is just a text view.
        public LinearLayout row;
        public TextView housemateView;

        /**
         * Default constructor. Creates the view of the data item.
         * @param itemView  View used to display content.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView.findViewById(R.id.a_row);
            housemateView = (TextView) itemView.findViewById(R.id.a_task);

            // IMPORTANT: Set the onclick listener.
            row.setOnClickListener(this);
        }

        /**
         * On Click listener method. Called when an item is clicked on.
         * @param v The view that is clicked on.
         */
        @Override
        public void onClick(View v) {

            // Starts a new activity that shows the item in detail.
            Intent taskIntent = new Intent(v.getContext(), DisplayTaskActivity.class);

            // Gets the task that is tapped on.
            int position = getAdapterPosition();
            User housemate = housemates.get(position);
            //Toast.makeText(v.getContext(), task, Toast.LENGTH_LONG).show();

            // Passes the task that is tapped on to the next Activity.
            taskIntent.putExtra(MainActivity.TASK, housemate.getFirst_name());

            // Start the next activity.
            (v.getContext()).startActivity(taskIntent);

        }
    }

    /**
     * Constructor to feed the task list data.
     * @param myDataset The dataset that contains the housemates.
     */
    public HousemateAdapter(ArrayList<User> myDataset) {
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
    public HousemateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Creates a new view.
        // Need to create another xml file just for the item in the lists.
        // Then link it to here through inflate!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new HousemateAdapter.ViewHolder(view);
    }

    /**
     * Binds the viewholder to its data. Invoked by the layout manager.
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull HousemateAdapter.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.housemateView.setText(housemates.get(position).getFirst_name());
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
