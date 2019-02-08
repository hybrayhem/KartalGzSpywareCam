package com.ilhan.kartalgz_sentez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
//import android.widget.VideoView;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import android.app.ProgressDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

//import java.util.ArrayList;
//import android.content.ActivityNotFoundException;
//import android.speech.RecognizerIntent;;
//import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    //Malzemelerin tanıtıldığı bölüm
    Button btnUp;
    Button btnDown;
    Button btnLeft;
    Button btnRight;
    Button btnOne;
    Button btnTwo;
    Button btnThree;
    Button btnFour;
    Button btnFive;
    Button btnSix;
    Button btnSeven;
    Button btnTurbo;
    Button btnGlide;
    //Button btnClose;
    //Button btnCenter; v1.4 de kaldırıldı
    Button btnUpRight;
    Button btnDownRight;
    Button btnUpLeft;
    Button btnDownLeft;
    ImageButton btnVoice;
    ImageButton btnClose;
    ImageButton OpenCV;

    TextView txtPower;
    TextView txtRotate;
    TextView txtD;
    //TextView txtPop;

    //MediaController mediaController;
    //VideoView streamView;
    ImageButton btnConnect;
    //EditText addrField;

    WebView web_stream;

    EditText txtAddress;
    Socket myAppSocket = null;

    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------------------------------------------------------------//
        checkPermission();

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    //editText.setText(matches.get(0));
                    getIPandPort();
                    CMD = matches.get(0) + ".";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();
                    Toast.makeText(getApplicationContext(), CMD, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        //--------------------------------------------------------------//

        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        btnFive = (Button) findViewById(R.id.btnFive);
        btnSix = (Button) findViewById(R.id.btnSix);
        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnTurbo = (Button) findViewById(R.id.btnTurbo);
        btnGlide = (Button) findViewById(R.id.btnGlide);
        //btnClose = () findViewById(R.id.btnClose);
        //btnCenter = (Button) findViewById(R.id.btnCenter);
        btnUpRight = (Button) findViewById(R.id.btnUpRight);
        btnDownRight = (Button) findViewById(R.id.btnDownRight);
        btnUpLeft = (Button) findViewById(R.id.btnUpLeft);
        btnDownLeft = (Button) findViewById(R.id.btnDownLeft);

        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnVoice = (ImageButton) findViewById(R.id.btnVoice);
        OpenCV = (ImageButton) findViewById(R.id.OpenCV);

        web_stream = (WebView) findViewById(R.id.web_stream);
        web_stream.getSettings().setJavaScriptEnabled(true); //
        web_stream.setWebViewClient(new WebViewClient());
        web_stream.setWebChromeClient(new WebChromeClient());


        /*final ProgressDialog progress = ProgressDialog.show(this, "KartalGözü Project by Halil İbrahim İLHAN", "Yükleniyor....", true);
        progress.show();
        web_stream.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Toast.makeText(getApplicationContext(), "Sayfa yüklendi", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });*/

        /*final ProgressDialog progressDialog = ProgressDialog.show(this, "KartalGözü Project by Halil İbrahim İLHAN",
                "Sayfa Yükleniyor...", true);

        web_stream.setWebViewClient(new WebViewClient() {

            // Sayfa Yüklenirken bir hata oluşursa kullanıcıyı uyarıyoruz.
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Sayfa Yüklenemedi!",
                        Toast.LENGTH_SHORT).show();
            }

            // Sayfanın yüklenme işlemi bittiğinde progressDialog'u kapatıyoruz.
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });*/
        //üstteki kodlar webview in çalışırken yükleniyor demesi ve hataları bildirmesi içindir ama her açılışta yükleniyor deyip kalıyor


        //addrField = (EditText)findViewById(R.id.addrField);
        btnConnect = (ImageButton) findViewById(R.id.btnConnect);
        //streamView = (VideoView)findViewById(R.id.streamView);

        txtAddress = (EditText) findViewById(R.id.ipAddress);
        txtPower = (TextView) findViewById(R.id.txtPower);
        txtRotate = (TextView) findViewById(R.id.txtRotate);
        txtD = (TextView) findViewById(R.id.txtD);
        //txtPop = (TextView) findViewById(R.id.txtPop);

        //streamView.setVisibility(View.INVISIBLE);

        web_stream.setVisibility(View.INVISIBLE);
        btnDownLeft.setVisibility(View.INVISIBLE);
        btnDownRight.setVisibility(View.INVISIBLE);
        btnUpLeft.setVisibility(View.INVISIBLE);
        btnUpRight.setVisibility(View.INVISIBLE);

//-------------------------------------------------------------------------------------//
        btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return false;
            }
        });
        OpenCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "OpenCV.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("!!!!AUTO-Pilot!!!!");
                txtPower.setText("!!!!  Devrede !!!!");

            }
        });
        /*btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.stopListening();
                        break;
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return false;
            }
        });*/
//-------------------------------------------------------------------------------------//
       /* btnConnect.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                streamView.setVisibility(View.VISIBLE);
                String s = txtAddress.getEditableText().toString();
                String x ="";
                int i = 0;
                while (true) {

                    if (x.contains(":")) {
                        break;
                    } else {
                        x += s.charAt(i);
                        i++;
                        continue;
                    }
                }
                String y = "http://"+x+"8090";
                playStream(y);

            }}); */
//**************************************************************************************//
        btnConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //streamView.setVisibility(View.VISIBLE);
                web_stream.setVisibility(View.VISIBLE);
                String s = txtAddress.getEditableText().toString();
                String x = "";
                int i = 0;
                while (true) {

                    if (x.contains(":")) {
                        break;
                    } else {
                        x += s.charAt(i);
                        i++;
                        continue;
                    }
                }
                //webview(python-flask) için adres hazırlama
                int c = x.length();
                String y = "http://" + x.substring(0, c - 1) + "/video_feed"; //+ "/view"; yayın view yeri boş bırakılınca veya video_feed ile birlikte çalışabiliyor
                web_stream.loadUrl(y); //eğer 2 kere basınca tekrar yüklemiyorsa bir de refresh ekle--yüklüyor
                //web_stream.setWebViewClient(new WebViewClient());
                //web_stream.setWebChromeClient(new WebChromeClient());
                //Toast.makeText(MainActivity.this,y, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),y, Toast.LENGTH_LONG).show();

                //String y = "http://"+x.substring(0, c-1)+"/view";

            }
        });

//-------------------------------------------------------------------------------------//
        /*btnConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //streamView.setVisibility(View.VISIBLE);
                web_stream.setVisibility(View.VISIBLE);
                String s = txtAddress.getEditableText().toString();
                web_stream.loadUrl(s);
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();


            }
        });*/


        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "Up.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: YUKARI");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "Down.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: AŞAĞI");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "Left.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SOL");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "Right.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SAĞ");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnDownLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "DownLeft.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SOL-ALT");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnUpRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "UpRight.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SAĞ-ÜST");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnUpLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "UpLeft.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SOL-ÜST");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        btnDownRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        getIPandPort();
                        CMD = "DownRight.";
                        Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: SAĞ-ALT");
                        txtD.setText("<<  >>");
                        return true;
                    case MotionEvent.ACTION_UP:
                        getIPandPort();
                        CMD = "Center.";
                        cmd_increase_servo = new Socket_AsyncTask();//Socket_AsyncTask silindi sorun çıkarabilir
                        cmd_increase_servo.execute();
                        txtRotate.setText("YÖN: MERKEZ");
                        txtD.setText(" ");
                        return true;
                }
                return false;
            }
        });
        /*
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Up";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("YÖN: YUKARI");
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Down";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("YÖN: AŞAĞI");
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Left";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("YÖN: SOL");
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Right";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("YÖN: SAĞ");
            }
        });
        */
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "One.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 1");
                //Toast.makeText(getApplicationContext(),"Hava açık, Rüzgar az, Güzel bir uçuş olacak ", 1200).show();
                //Toast.makeText(getApplicationContext(),"İyi uçuşlar, Kaptan!", 800).show();
                //txtPower.setText("KartalGözü-Spyware by Halil İbrahim İLHAN");

                btnDownLeft.setVisibility(View.VISIBLE);
                btnDownRight.setVisibility(View.VISIBLE);
                btnUpLeft.setVisibility(View.VISIBLE);
                btnUpRight.setVisibility(View.VISIBLE);

            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Two.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 2");
                //Toast.makeText(getApplicationContext(),"İyi uçuşlar, Kaptan!", 800).show();
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Three.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 3");
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Four.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 4");
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Five.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 5");
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Six.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 6");
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Seven.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("GÜÇ SEVİYESİ: 7");
            }
        });
        btnTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Turbo.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("!TURBO DEVREDE!");
                //Toast.makeText(getApplicationContext(),"Acele giden Ecele gider!", 800).show();
            }
        });
        btnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Glide.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("-SÜZÜLME MODU-");
                //Toast.makeText(getApplicationContext(),"İniş izni verildi, Kaptan!", 800).show();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Close.";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtPower.setText("!!Durduruldu!!");
                txtRotate.setText("!!Yeniden başlatın.!!");
                btnDownLeft.setVisibility(View.INVISIBLE);
                btnDownRight.setVisibility(View.INVISIBLE);
                btnUpLeft.setVisibility(View.INVISIBLE);
                btnUpRight.setVisibility(View.INVISIBLE);
            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Center";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
                txtRotate.setText("YÖN: MERKEZ");
            }
        });
        */
    }


    public void getIPandPort() {
        String iPandPort = txtAddress.getText().toString();
        Log.d("MYTEST", "IP String: " + iPandPort);
        String temp[] = iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST", "IP:" + wifiModuleIp);
        Log.d("MY TEST", "PORT:" + wifiModulePort);
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress, MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}
/*    private void playStream(String src){
        Uri UriSrc = Uri.parse(src);
        if(UriSrc == null){
            Toast.makeText(MainActivity.this,
                    "UriSrc == null", Toast.LENGTH_LONG).show();
        }else{
            streamView.setVideoURI(UriSrc);
            mediaController = new MediaController(this);
            streamView.setMediaController(mediaController);
            streamView.start();

            Toast.makeText(MainActivity.this,
                    "Bağlantı: " + src,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamView.stopPlayback();
    }
}*/

