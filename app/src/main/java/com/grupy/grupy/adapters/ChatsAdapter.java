package com.grupy.grupy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.grupy.grupy.R;
import com.grupy.grupy.activities.ChatActivity;
import com.grupy.grupy.models.Chat;
import com.grupy.grupy.providers.AuthProvider;
import com.grupy.grupy.providers.ChatsProvider;
import com.grupy.grupy.providers.MessageProvider;
import com.grupy.grupy.providers.UserProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    MessageProvider mMessageProvider;
    ListenerRegistration mListener;
    ListenerRegistration mListenerLastMessage;

    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context) {
        super(options);
        this.context = context;
        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();
        mMessageProvider = new MessageProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Chat chat) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        //chatId is the Id of the person that we are chatting with
        final String chatId = document.getId();
        if (mAuthProvider.getUid().equals(chat.getIdUser1())) {
            getUserInfo(chat.getIdUser2(), holder);
        }
        else {
            getUserInfo(chat.getIdUser1(), holder);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChatActivity(chatId, chat.getIdUser1(), chat.getIdUser2());
            }
        });

        getLastMessage(chatId, holder.textViewLastMessage);

        String idSender = "";
        if (mAuthProvider.getUid().equals(chat.getIdUser1())) {
            idSender = chat.getIdUser2();
        }
        else {
            idSender = chat.getIdUser1();
        }
        getMessageNotRead(chatId, idSender, holder.textViewMessageNotRead, holder.frameLayoutMessageNotRead);

    }

    private void getMessageNotRead(String chatId, String idSender, final TextView textViewMessageNotRead, final FrameLayout frameLayoutMessageNotRead) {
        mListener = mMessageProvider.getMessageByChatAndSender(chatId, idSender).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null) {
                    int size = queryDocumentSnapshots.size();
                    if (size > 0) {
                        frameLayoutMessageNotRead.setVisibility(View.VISIBLE);
                        textViewMessageNotRead.setText(String.valueOf(size));
                    }
                    else {
                        frameLayoutMessageNotRead.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public ListenerRegistration getListener() {
        return mListener;
    }

    public ListenerRegistration getListenerLastMessage() {
        return mListenerLastMessage;
    }

    private void getLastMessage(String chatId, final TextView textViewLastMessage) {
        mListenerLastMessage = mMessageProvider.getLastMessage(chatId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null) {
                    int size = queryDocumentSnapshots.size();
                    if (size > 0) {
                        String lastMessage = queryDocumentSnapshots.getDocuments().get(0).getString("message");
                        textViewLastMessage.setText(lastMessage);
                    }
                }
            }
        });
    }

    private void gotoChatActivity(String chatId, String idUser1, String idUser2) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idChat", chatId);
        intent.putExtra("idUser1", idUser1);
        intent.putExtra("idUser2", idUser2);
        context.startActivity(intent);
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
        TextView textViewMessageNotRead;
        CircleImageView circleImageViewChat;
        FrameLayout frameLayoutMessageNotRead;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewGroupName = view.findViewById(R.id.textViewGroupNameChat);
            textViewLastMessage = view.findViewById(R.id.textViewLastMessageChat);
            textViewMessageNotRead = view.findViewById(R.id.textViewMessageNotRead);
            circleImageViewChat = view.findViewById(R.id.circleImageChat);
            frameLayoutMessageNotRead = view.findViewById(R.id.frameLayoutMessageNotRead);
            viewHolder = view;
        }
    }
}

