package com.example.userasus.signin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView userTv;
    EditText campo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userTv = (TextView) findViewById(R.id.tvUser);
        campo= (EditText) findViewById(R.id.campo);

        Bundle datos = getIntent().getExtras();


        String user = datos.getString("user").toString().trim();
        String passwd = datos.getString("passwd");

        userTv.setText("You are: "+user +" And your password is: " +passwd);

    }

    public  void  Volver(View v){
        Intent ir = new Intent(Home.this,Sing.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle datos=new Bundle();
        datos.putString("retorno",campo.getText().toString());
        ir.putExtras(datos);
        startActivity(ir);
    }
}
