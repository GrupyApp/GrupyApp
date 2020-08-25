package com.grupy.grupy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.activities.GroupDetailActivity;
import com.grupy.grupy.models.Post;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.PostProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;

        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String groupId = document.getId();
        holder.textViewName.setText(post.getName().toUpperCase());

        if (post.getIdUser().equals(mAuthProvider.getUid())) {
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageViewGroup);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra("id", groupId);
                context.startActivity(intent);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(groupId);
            }
        });
    }

    private void showConfirmDelete(final String groupId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete group")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(groupId);
                    }
                })
        .setNegativeButton("No", null)
                .show();
    }

    private void deletePost(String groupId) {
        mPostProvider.delete(groupId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Group deleted.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Group could not be deleted.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_group, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        View viewHolder;
        CircleImageView circleImageViewGroup;
        ImageView imageViewDelete;

        public ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewMyGroupName);
            circleImageViewGroup = view.findViewById(R.id.circleImageViewMyGroup);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyGroup);
            viewHolder = view;
        }
    }
}
