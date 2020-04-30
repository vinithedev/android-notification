package com.example.vinizeroum;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.text.HtmlCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String audioPath = "";
    Intent audioIntent;
    Button buttonNotificar, buttonPlus, buttonEscolhersom;
    EditText editTitle, editText, editInterval, editInicioem, editQtd;

    ArrayList<String> sEditTitle = new ArrayList<String>();
    ArrayList<String> sEditText = new ArrayList<String>();
    ArrayList<Integer> iEditQtd = new ArrayList<>();
    ArrayList<Boolean> isLoop =  new ArrayList<Boolean>();

    String sEditInterval, sEditInicioem;
    int iEditInterval, iEditInicioem;
    public static boolean firstTime = false;

    int iconColorHotmart = Color.rgb(3, 85, 110);
    int iconColorMonetizze = Color.rgb(6, 25, 200);
    int iconColorGmail = Color.rgb(200, 26, 21);
    int iconColorNubank = Color.rgb(43, 11, 47);
    int titleColor = Color.rgb(59, 58, 50); // 595850
    int backgroundColor = Color.rgb(242, 241, 247); //  F2F1F7
//    int iconColor = Color.rgb(1, 1, 244); // ???
    int uniqueId = 0;

    private static final String CHANNEL_ID = "MyChannelID";

    public static boolean hasGreater(ArrayList<Integer> iArray,int i){
        for(int x : iArray){
            if(x > i) return true;
        }
        return false;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

//            AudioAttributes alarmAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
//            channel.setd(Sounuuu, alarmAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_nubank_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    // CUSTOM SOUND
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    audioPath = data.getData().getPath();
//                    audioPath = data.getData().toString();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder.setColor(iconColorNubank);
//        builder.setColor(R.drawable.color_gradient);////////////////////////////////////////
        createNotificationChannel();
        editTitle = (EditText) findViewById(R.id.edittextTitulo);
        editText = (EditText) findViewById(R.id.edittextTexto);
        editInterval = (EditText) findViewById(R.id.edittextIntervalo);
        editInicioem = (EditText) findViewById(R.id.edittextInicioem);
        editQtd = (EditText) findViewById(R.id.edittextQtd);

        //  BUTTON NOTIFICAR
        firstTime = true;
        buttonNotificar = findViewById(R.id.buttonNotificar);
        buttonNotificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEditInterval = String.valueOf(editInterval.getText());
                iEditInterval = Integer.parseInt(sEditInterval);
                sEditInicioem = String.valueOf(editInicioem.getText());
                iEditInicioem = Integer.parseInt(sEditInicioem);

                try {
                    TimeUnit.SECONDS.sleep(iEditInicioem);

                while( isLoop.contains(true) || (!isLoop.contains(true) && hasGreater(iEditQtd, 0)) ) {
                    for (int i = 0; i <= sEditTitle.size() - 1; i++) {
//                        if (i != 0) TimeUnit.SECONDS.sleep(iEditInterval);

                        TimeUnit.SECONDS.sleep(iEditInterval);

                        if (isLoop.get(i)) {
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(sEditText.get(i)));
                            builder.setContentTitle(HtmlCompat.fromHtml("<font color=\"" + titleColor + "\">" + sEditTitle.get(i) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            if (audioPath != "") builder.setSound(Uri.parse(audioPath));
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                            notificationManager.notify(uniqueId++, builder.build());

                        } else if (!isLoop.get(i) && iEditQtd.get(i) == 0) {
                            //  STOP

                        } else if (!isLoop.get(i) && iEditQtd.get(i) > 0) {
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(sEditText.get(i)));
                            builder.setContentTitle(HtmlCompat.fromHtml("<font color=\"" + titleColor + "\">" + sEditTitle.get(i) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            if (audioPath != "") builder.setSound(Uri.parse(audioPath));
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                            notificationManager.notify(uniqueId++, builder.build());
                            iEditQtd.set(i, iEditQtd.get(i) - 1);
                        }

                    }
                }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        //  BUTTON PLUS
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEditTitle.add(String.valueOf(editTitle.getText()));
                sEditText.add(String.valueOf(editText.getText()));

                int qtd = Integer.parseInt(String.valueOf(editQtd.getText()));
                isLoop.add(qtd == 0 ? true : false);
                iEditQtd.add(qtd);

                editQtd.setText("");
                editTitle.setText("");
                editText.setText("");

                Toast.makeText(MainActivity.this, "Título, Texto e Qtd adicionados com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        //  BUTTON ESCOLHER SOM
        buttonEscolhersom = findViewById(R.id.buttonEscolhersom);
        buttonEscolhersom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioIntent = new Intent(Intent.ACTION_GET_CONTENT);
                audioIntent.setType("*/*");

                startActivityForResult(audioIntent, 10);
            }
        });
    }
}
