package com.example.teh_k.ChoreMate;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * TaskAdapter is a class that links the data from the dataset into the UI.
 * Basically creates the items in the list show on the screen.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<Task> tasks;


    /**
     * Database references.
     */
    private DatabaseReference mDatabase;
    private Uri avatarUri;

    /**
     * Provides reference to the views for each data item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // The UI elements of each row entry.
        public LinearLayout row;
        private TextView taskTitle;
        private CircleImageView avatar;

        /**
         * Default constructor. Creates the view of the data item.
         * @param itemView  View used to display content.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            // Linking the UI elements.
            row = (LinearLayout) itemView.findViewById(R.id.view_task_row);
            taskTitle = (TextView) itemView.findViewById(R.id.text_task_title);
            avatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);

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
            Intent taskIntent = new Intent(v.getContext(), ViewTaskActivity.class);

            // Gets the task that is tapped on.
            int position = getAdapterPosition();
            Task task = tasks.get(position);
            //Toast.makeText(v.getContext(), task, Toast.LENGTH_LONG).show();

            // Passes the task that is tapped on to the next Activity.
            taskIntent.putExtra(MainActivity.TASK, task);

            // Start the next activity.
            (v.getContext()).startActivity(taskIntent);

        }
    }

    /**
     * Constructor to feed the task list data.
     * @param myDataset The dataset that contains the tasks.
     */
    public TaskAdapter(ArrayList<Task> myDataset) {
        tasks= myDataset;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Creates a new view.
        // Need to create another xml file just for the item in the lists.
        // Then link it to here through inflate!
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_task, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the viewholder to its data. Invoked by the layout manager.
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {

        // Get element from dataset at this position
        Task currentTask = tasks.get(position);
        List<String> currentAssignedUsers =  currentTask.getUser_list();

        // Replace the contents of the view with that element
        holder.taskTitle.setText(currentTask.getTask_name());
        mDatabase.child("Users").child(currentAssignedUsers.get(0))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        avatarUri = dataSnapshot.getValue(User.class).getAvatar();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TaskAdapterAvatar", "Uri retrieving error");
                    }
                });

        holder.avatar.setImageURI(avatarUri);
    }

    /**
     * Gets the number of items in the list. Invoked by the layout manager.
     * @return  The number of items in the dataset.
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }


}
