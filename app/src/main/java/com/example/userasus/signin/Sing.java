package com.example.userasus.signin;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userasus.signin.Data.Conexion;
import com.example.userasus.signin.Data.LoginContract;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sing extends AppCompatActivity {

    EditText user;
    EditText passwd;

    Conexion con;
    SQLiteDatabase db;

    String clase = getClass().toString();//solo para pruebas no serecomienda mostrar estoen productivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);
        user = (EditText) findViewById(R.id.eTLogin);
        passwd = (EditText) findViewById(R.id.eTPass);

        con = new Conexion(this,"proyecto",null,1);
        db = con.getWritableDatabase();

        if(db!=null){
            //Insert user default app
            String passwdDefault="1";
            try {
                passwdDefault=this.toMd5(passwdDefault);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String userAdmin="INSERT INTO "+ LoginContract.LoginEntry.TABLE_NAME+" ("+ LoginContract.LoginEntry.EMAIL+","+ LoginContract.LoginEntry.PASSWD+")" +
                    "VALUES('admin@correo.com','"+passwdDefault+"');";
            db.execSQL(userAdmin);
        }else{
            Toast.makeText(this,"Error en la conexion "+clase,Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void iniciar(View v) throws NoSuchAlgorithmException {

        String userTex=user.getText().toString().trim().toLowerCase();
        String passwdText=passwd.getText().toString().trim().toLowerCase();
        Integer userId=0;
        String passwdMd5 = this.toMd5(passwdText);

        if(userTex.matches("") | passwdText.matches("")){
            Toast.makeText(this,"Please insert all data required",Toast.LENGTH_LONG).show();
        }else{
            Cursor c = db.rawQuery("select a."+LoginContract.LoginEntry.USERID+" " +
                    " from "+LoginContract.LoginEntry.TABLE_NAME+" as a where a."+LoginContract.LoginEntry.EMAIL+"='"+userTex+"' and "+LoginContract.LoginEntry.PASSWD+"='"+passwdMd5+"';",null,null);
            if (c.moveToFirst()) {//es por que hay fila resultante
                userId = c.getInt(0);
            }
            c.close();
            if(userId!=0){
                Bundle datos=new Bundle();
                datos.putString("user",userTex);
                datos.putString("passwd",passwdText);

                Intent ir = new Intent(Sing.this,Home.class);
                ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_SINGLE_TOP);

                ir.putExtras(datos);

                startActivity(ir);
            }else{
                Toast.makeText(this,"You not is registred",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            con.BD_backup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toMd5(String texto) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(texto.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        //ahora sb.toString(); es la contraseña cifrada
        return  sb.toString();
    }
}

