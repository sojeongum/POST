package android.example.post;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference comRef;

    private EditText commentEdit;
    private ImageButton mAddCommentBtn;
    private RecyclerView mCommentRecyclerView;
    private String id;
    private String userId, eachTitle, eachContents, eachTime, eachIMG;
    private FirebaseFirestore firestore;
    private ImageView postImageView;
    private TextView puserId, pstatus, ptitle, pdate, pcontents;
    private CommentsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.each_comments);

        postImageView = findViewById(R.id.pimageView);
        puserId = findViewById(R.id.userID);
        pstatus = findViewById(R.id.userStatus);
        ptitle = findViewById(R.id.eachTitle);
        pdate = findViewById(R.id.eachDate);
        pcontents = findViewById(R.id.eachContents);


        commentEdit = findViewById(R.id.comment_editText);
        mAddCommentBtn = findViewById(R.id.add_comment);

        firestore = FirebaseFirestore.getInstance();


        id = getIntent().getStringExtra("postid");

        comRef = db.collection("post/" +id + "/comments");

        eachTitle = getIntent().getStringExtra("title");
        eachContents = getIntent().getStringExtra("contents");
        eachTime = getIntent().getStringExtra("time");
        eachIMG = getIntent().getStringExtra("image");

        ptitle.setText(eachTitle);
        pcontents.setText(eachContents);
        pdate.setText(eachTime);
        Glide.with(this)
                .load(eachIMG)
                .into(postImageView);


        setUpcomRecyclerView();

        mAddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEdit.getText().toString();
                if(!comment.isEmpty()){
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("comment",comment);
                    commentsMap.put("time", FieldValue.serverTimestamp());
                 //   commentsMap.put("user",user);
                    firestore.collection("post/" +id + "/comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this,"댓글이 작성되었습니다!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CommentsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(CommentsActivity.this,"댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpcomRecyclerView(){
        Query query = comRef.orderBy("time",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Comments> options = new FirestoreRecyclerOptions.Builder<Comments>()
                .setQuery(query, Comments.class)
                .build();

        adapter = new CommentsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.comment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}