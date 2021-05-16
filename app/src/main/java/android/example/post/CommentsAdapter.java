package android.example.post;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Date;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comments,CommentsAdapter.CommentsHolder> {

    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<Comments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentsHolder holder, int position, @NonNull Comments model) {
        holder.commentView.setText(model.getComment());
//        holder.commentUser.setText(model.getUser());


        long milliseconds = model.getTime().getTime();
        String date = DateFormat.format("yyyy/MM/dd", new Date(milliseconds)).toString();
        holder.commentDate.setText(date);

        String userId = "사용자";
        holder.commentUser.setText(userId);

    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comments,parent,false);
        return new CommentsHolder(v);
    }

    class CommentsHolder extends RecyclerView.ViewHolder{
        TextView commentView;
        TextView commentUser;
        TextView commentDate;

        public CommentsHolder(View itemView){
            super(itemView);
            commentUser = itemView.findViewById(R.id.comment_user);
            commentView = itemView.findViewById(R.id.comment);
            commentDate = itemView.findViewById(R.id.commentDate);
        }
    }
}
