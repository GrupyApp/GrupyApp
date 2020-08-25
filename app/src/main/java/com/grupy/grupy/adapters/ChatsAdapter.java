package com.grupy.grupy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.models.Chat;
import com.grupy.grupy.providers.UserProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    UserProvider mUsersProvider;

    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context) {
        super(options);
        this.context = context;
        mUsersProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        //chatId is the Id of the person that we are chatting with
        final String chatId = document.getId();
        getUserInfo(chatId, holder);
    }

    private void getUserInfo(String idUser, final ViewHolder holder) {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    holder.textViewGroupName.setText(username.toUpperCase());
                }
                //OJO AL GROUP IMAGE AMB L'IMAGE PROFILE DEL RAYA AL PISO
                if (documentSnapshot.contains("image_profile")) {
                    String imageProfile = documentSnapshot.getString("image_profile");
                    if (imageProfile != null) {
                        if (!imageProfile.isEmpty()) {
                            Picasso.with(context).load(imageProfile).into(holder.circleImageViewChat);
                        }
                    }
                }
            }
        });

}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView textViewGroupName;
        TextView textViewLastMessage;
        CircleImageView circleImageViewChat;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewGroupName = view.findViewById(R.id.textViewGroupNameChat);
            textViewLastMessage = view.findViewById(R.id.textViewLastMessageChat);
            circleImageViewChat = view.findViewById(R.id.circleImageChat);
            viewHolder = view;
        }
    }
    }

