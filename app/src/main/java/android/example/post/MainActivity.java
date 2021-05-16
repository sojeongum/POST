package android.example.post;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.List;

//import firebase 주소 추가해야


public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("post");
    private RecyclerView postRecyclerView;
    //private WriteinfoAdapter.OnItemClickListener listener;
    private WriteinfoAdapter adapter;
    private List<Writeinfo> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();

        firebaseAuth = FirebaseAuth.getInstance();

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WritePostActivity.class));
            }
        });
    }
    private void setUpRecyclerView(){
        Query query = notebookRef.orderBy("time",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Writeinfo> options = new FirestoreRecyclerOptions.Builder<Writeinfo>()
                .setQuery(query, Writeinfo.class)
                .build();

        adapter = new WriteinfoAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WriteinfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Writeinfo writeinfo = documentSnapshot.toObject(Writeinfo.class);
                String id = documentSnapshot.getId();
                String title = documentSnapshot.getString("title");
                String contents = documentSnapshot.getString("contents");
                long time = writeinfo.getTime().getTime();
                String date = DateFormat.format("yyyy/MM/dd", new Date(time)).toString();
                String imgURI = documentSnapshot.getString("image");

                Intent commentIntent = new Intent(MainActivity.this, CommentsActivity.class); //if error occurs edit the context
                commentIntent.putExtra("postid",id);
                commentIntent.putExtra("title",title);
                commentIntent.putExtra("contents",contents);
                commentIntent.putExtra("image",imgURI);
                commentIntent.putExtra("time",date);

                startActivity(commentIntent);
            }
        });
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


/*
*
        if(firebaseAuth.getCurrentUser()!=null){

            postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !postRecyclerView.canScrollVertically(1);
                    if(isBottom){
                        Toast.makeText(MainActivity.this,"마지막 페이지 입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            query = db.collection("post").orderBy("time", Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(MainActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            Writeinfo writeinfo = doc.getDocument().toObject(Writeinfo.class);
                            list.add(writeinfo);
                            adapter.notifyDataSetChanged();
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }

    }*/