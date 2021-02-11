package com.example.lo.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InserirFotoPerfil extends AppCompatActivity {

    private ImageView imgUsuario;
    private Uri localImagemSelecionada;
    private byte[] byteArray;
    private FirebaseFirestore banco;
    private StorageReference storageReference;
    private FotoPerfil fotoPerfil;
    private ArrayList<FotoPerfil> fotos;
    private String idUsuario;
    private TextView lblInserirFoto;
    private TextView lblProgresso;
    private Animation animacao;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inserir_foto_perfil);

        imgUsuario = findViewById(R.id.img_foto_perfil);
        lblInserirFoto = findViewById(R.id .lbl_foto_perfil);
        lblProgresso = findViewById(R.id.lbl_progresso_upload);
        btnOk = findViewById(R.id.btn_foto_perfil);

        storageReference = FirebaseStorage.getInstance().getReference();

        banco = ConfiguracaoFirebase.getFirebase();

        inserirFoto();

        animacao = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacao);
    }

    private void inserirFoto(){

        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);

            }
        });

        lblInserirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOk.startAnimation(animacao);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            localImagemSelecionada = data.getData();

            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                final StorageReference pastaReferencia = storageReference.child("imagens/"+byteArray);
                UploadTask uploadTask = pastaReferencia.putFile(localImagemSelecionada);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "deu ruim meu bom", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "deu bom meu bom", Toast.LENGTH_SHORT).show();

                        pastaReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                FotoPerfil fotoPerfil = new FotoPerfil();
                                fotoPerfil.setFotoPerfil(uri.toString());

                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                idUsuario = preferencias.getIdentificador();

                                banco.collection("Usuario").document(idUsuario).collection("Foto Perfil").add(fotoPerfil).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        String url = uri.toString();

                                        Glide.with(getApplicationContext()).load(url).into(imgUsuario);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "deu ruim merda", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), "error 301", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        long progresso = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        lblProgresso.setText("Upload está " + progresso + "% completo");
                    }
                });



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
