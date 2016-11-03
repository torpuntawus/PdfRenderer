package com.example.nrtdemo_nb.pdfrenderer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.Os;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private  int currentPage = 0;
    private Button next,previus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        next = (Button) findViewById(R.id.next);
        previus = (Button) findViewById(R.id.previous);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                currentPage++;
                render();
            }
        });
        previus.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                currentPage++;
                render();
            }
        });
        render();
    }
    private  void render(){
        try{
            imageView = (ImageView) findViewById(R.id.image);
            int REQ_WIDTH = imageView.getWidth();
            int REQ_HEIHT = imageView.getHeight();

            //File file = new File(Environment.getRootDirectory(),"Download/tute1.pdf");

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Download/MarketLaw.pdf" );
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent = Intent.createChooser(target, "Open File");

            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY));
            Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH,REQ_HEIHT,Bitmap.Config.ARGB_4444);

            if(currentPage<0){
                currentPage=0;
            }else if(currentPage >renderer.getPageCount()){
                currentPage=renderer.getPageCount()-1;
            }
            Matrix m = imageView.getImageMatrix();
            Rect rect = new Rect(0,0,REQ_WIDTH,REQ_HEIHT);

            renderer.openPage(currentPage).render(bitmap,rect,m,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            imageView.setImageMatrix(m);
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
