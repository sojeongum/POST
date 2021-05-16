package android.example.post;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class WriteinfoAdapter extends FirestoreRecyclerAdapter<Writeinfo,WriteinfoAdapter.WriteinfoHolder>{
    private Date date;
    private OnItemClickListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("post");

    public WriteinfoAdapter(@NonNull FirestoreRecyclerOptions<Writeinfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WriteinfoHolder holder, int position, @NonNull Writeinfo model) {
        holder.postTitle.setText(model.getTitle());
        holder.postContents.setText(model.getContents());
        Glide.with(holder.itemView.getContext())
                .load(model.getImage())
                .into(holder.postPic);

        long milliseconds = model.getTime().getTime();
        String date = DateFormat.format("yyyy/MM/dd", new Date(milliseconds)).toString();
        holder.postDate.setText(date);

        String userId = "사용자";
        holder.postUsername.setText(userId);

        //String postId = writeinfo.PostId;
        //String currentUserId = auth.getCurrentUser().getUid();

    }


    @NonNull
    @Override
    public WriteinfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_post,
                parent,false);
        return new WriteinfoHolder(v);
    }

    class WriteinfoHolder extends RecyclerView.ViewHolder{
        ImageView postPic, commentPic;
        TextView postUsername, postDate, postCaptions, postContents, postTitle;

        public WriteinfoHolder(final View itemView){
            super(itemView);
            postPic = itemView.findViewById(R.id.post_image);
     //       Glide.with(context).load(urlPost).into(postPic);
            postUsername = itemView.findViewById(R.id.username);
            postDate = itemView.findViewById(R.id.date);
            postTitle = itemView.findViewById(R.id.title_view);
            postContents = itemView.findViewById(R.id.contents_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!= null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}

    /*
    *
    private List<Writeinfo> mList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public WriteinfoAdapter(Activity context, List<Writeinfo> mList){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public WriteinfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_post,parent,false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new WriteinfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteinfoViewHolder holder, int position) {
        Writeinfo writeinfo = mList.get(position);
        holder.setPostPic(writeinfo.getImage());
        holder.setPostTitle(writeinfo.getTitle());
        holder.setPostContents(writeinfo.getContents());

        long milliseconds = writeinfo.getTime().getTime();
        String date = DateFormat.format("yyyy/MM/dd", new Date(milliseconds)).toString();
        holder.setPostDate(date);

        String userId = "사용자";
        holder.setPostUsername(userId);
                //get user id

        //String postId = writeinfo.PostId;
        //String currentUserId = auth.getCurrentUser().getUid();


    }

    @Override
    public int getItemCount() {
        if(mList == null)
            return 0;
        else
            return mList.size();
    }

    public class WriteinfoViewHolder extends RecyclerView.ViewHolder{
        ImageView postPic, commentPic;
        TextView postUsername, postDate, postCaptions, postContents, postTitle;
        View mView;

        public WriteinfoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setPostPic(String urlPost){
            postPic = mView.findViewById(R.id.post_image);
            Glide.with(context).load(urlPost).into(postPic);
        }
        public void setPostUsername(String username){
            postUsername = mView.findViewById(R.id.username);
            postUsername.setText(username);
        }
        public void setPostDate(String date){
            postDate = mView.findViewById(R.id.date);
            postDate.setText(date);
        }
        public void setPostTitle(String title){
            postTitle = mView.findViewById(R.id.title_view);
            postTitle.setText(title);
        }
        public void setPostContents(String contents){
            postContents = mView.findViewById(R.id.contents_view);
            postContents.setText(contents);
        }
    }
    * */