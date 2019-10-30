package es.pulimento.fakecamera;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private Uri requestedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if (intent.getAction() != null && "android.media.action.IMAGE_CAPTURE".equals(intent.getAction()))
        {
            ClipData clip = intent.getClipData();

            // Gets the first item from the clipboard data
            ClipData.Item item = clip.getItemAt(0);

            // Tries to get the item's contents as a URI pointing to a note
            requestedUri = item.getUri();

            // Pick photo
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();

            long readBytes = copyPickedImageToDestination(MainActivity.this, selectedImage, requestedUri);

            if (readBytes > 0) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setDataAndType(this.requestedUri,"image/jpeg");
                this.setResult(Activity.RESULT_OK, shareIntent);
                this.finish();
            }
        }
    }

    private long copyPickedImageToDestination(Context context, Uri source, Uri destination) {

        long nread = 0L;

        try {
            InputStream sourceStream = context.getContentResolver().openInputStream(source);
            OutputStream destinationStream = context.getContentResolver().openOutputStream(destination);

            byte[] buf = new byte[8192];
            int n;
            while ((n = sourceStream.read(buf)) > 0) {
                destinationStream.write(buf, 0, n);
                nread += n;
            }

            sourceStream.close();
            destinationStream.close();

            Toast.makeText(context, "NRead: " + nread, Toast.LENGTH_LONG).show();



        } catch (Exception e) {
            Toast.makeText(context, "Catch!: " + Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }

        return nread;
    }
}

