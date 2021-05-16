package android.example.post;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class WritePostActivity extends AppCompatActivity{
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;
    private ImageView imageBtn;
    private Uri postImageUri = null;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId;
    private String post_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("글쓰기");

        imageBtn = findViewById(R.id.imageButton);
        post_id = getIntent().getStringExtra("postid");

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
 //       auth = FirebaseAuth.getInstance();
//        currentUserId = auth.getCurrentUser().getUid();

        //image permission 받아오기
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(WritePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(3, 2)
                                .setMinCropResultSize(512, 512)
                                .start(WritePostActivity.this);

                    }
                }
            }
        });
    }

//store image into the storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                postImageUri = result.getUri();
                imageBtn.setImageURI(postImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this,result.getError().toString(),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void postButtonClicked(){
        //immutable read-only variable
        final String title = ((EditText)findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.contentsEditText)).getText().toString();

        if(!title.isEmpty() && postImageUri!=null){
            final StorageReference postRef = storageReference.child("post_images").child(FieldValue.serverTimestamp().toString()+".jpg");
            postRef.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String, Object> writeinfoMap = new HashMap<>();
                                writeinfoMap.put("image", uri.toString());
                                writeinfoMap.put("title", title);
                                writeinfoMap.put("contents", contents);
                                writeinfoMap.put("time", FieldValue.serverTimestamp());
// postMap.put("user", user);
                                firestore.collection("post").add(writeinfoMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(WritePostActivity.this, "게시 완료!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(WritePostActivity.this, MainActivity.class));
                                            finish();
                                        }else{
                                            String error = task.getException().getMessage();
                                            Toast.makeText(WritePostActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(WritePostActivity.this,"Error: "+error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this,"제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_note:
                postButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
