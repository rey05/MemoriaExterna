package com.example.angel.memoriaexterna;

import android.os.Environment;
import  android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Principal extends ActionBarActivity implements OnClickListener {

    //Declaramos los atributos que se van a utilizar
    private EditText txtTexto;
    private Button btnGuardar, btnAbrir;
    private static final int READ_BLOCK_SIZE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

//Aqui enlazamos los elementos graficos con el codigo
        txtTexto = (EditText) findViewById(R.id.txtArchivo);
       btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnAbrir = (Button) findViewById(R.id.btnAbrir);
        btnGuardar.setOnClickListener(this);
        btnAbrir.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        File sdCard, directory, file = null;
        try {
            //validamos si se encuentra montada nuestra memoria externa
            if (Environment.getExternalStorageState().equals("mounted")){

            //obtenemos el directorio de la memoria externa
                sdCard = Environment.getExternalStorageDirectory();

                if (v.equals(btnGuardar)) {
                    String str = txtTexto.getText().toString();

                    //clase que pemrmite grabar texto en un archivo
                    FileOutputStream fout = null;
                    try {
                        //instanciamos un objeto file para crear uno nuevo
                        //directorio
                        //la memoria externa
                        directory = new File(sdCard.getAbsolutePath() + "/Mis archivos");
                        //se crea el nuevo  directorio donde se creara el archivo
                        directory.mkdirs();


                        //creamos el archivo en el nuevo directorio creado
                        file = new File(directory, "MiArchivo.txt");

                        fout = new FileOutputStream(file);

                        //convierte un stream de caracteress en un stream de bytes
                        OutputStreamWriter ows = new OutputStreamWriter(fout);
                        ows.write(str);//Esribe em eñ buffer la cadena de caracteres
                        ows.flush();//Volca lo que hay en el buffer al archivo
                        ows.close();//cierra eñ archivo de texto

                        Toast.makeText(getBaseContext(), "El archivo se ha almacenado!!", Toast.LENGTH_SHORT).show();

                        txtTexto.setText("");

                    } catch (IOException e) {
                        //TODO: Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (v.equals(btnAbrir)){
                    try {
                        //obtenemos el directorio donde se encuentra nuestro archivo a leer
                        directory = new File(sdCard.getAbsolutePath() + "/Mis archivos");

                        //creamos un objeto File de nuestro archivo a leer
                        file = new File(directory, "MiArchivo.txt");


                        //creamos un objeto de la clase FileInputStream
                        //el cual representa un stream del archivo que vamos a leer
                        FileInputStream fin = new FileInputStream(file);

                        //creamos un objeto InputStreamReader que nos permitira
                        //leer el stream del archivo abierto
                        InputStreamReader isr = new InputStreamReader(fin);

                        char[] inputBuffer = new char[READ_BLOCK_SIZE];
                        String str = "";

                        //se lee el archivo de texto mientras no se llegue al
                        //final de el
                        int charRead;
                        while ((charRead = isr.read(inputBuffer)) > 0) {
                            //se lee por bloques de 100 caracteres
                            //ya que se desconoce el tamaño del texto
                            //y se va copiando a una cadena de texto
                            String strRead = String.copyValueOf(inputBuffer, 0, charRead);

                            str += strRead;

                            inputBuffer = new char[READ_BLOCK_SIZE];

                        }

                        //se muestra el texto leido en la caja de texto
                        txtTexto.setText(str);
                        isr.close();

                        Toast.makeText(getBaseContext(), "El archivo ha sido cargado", Toast.LENGTH_SHORT).show();
                    } catch (IOException e){
                        //TODO: handle exception
                        e.printStackTrace();
                    }
                    }
                } else {
                Toast.makeText(getBaseContext(), "El almacenamiento externo no se encuentra disponible", Toast.LENGTH_LONG).show();

            }

        }catch (Exception e){
            //TODO : handle exception
        }

    }
}
