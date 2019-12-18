package org.apache.cordova;

import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Tar extends CordovaPlugin{

    private static final String LOG_TAG = "Tar";

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if ("untar".equals(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    untar(args, callbackContext);
                }
            });

            return true;
        }


        return false;
    }


    private void untar(final JSONArray arguments, final CallbackContext callbackContext) {

        try {

            CordovaResourceApi resourceApi = webView.getResourceApi();

            final String tarFileNameParam = arguments.getString(0);
            final String extractPathParam = arguments.getString(1);

            final Uri tarUri = getUriForArg(tarFileNameParam);
            final Uri outputUri = getUriForArg(extractPathParam);

            CordovaResourceApi.OpenForReadResult zipFile = resourceApi.openForRead(tarUri);
            String extractPath = resourceApi.mapUriToFile(outputUri).getAbsolutePath();
            extractPath += extractPath.endsWith(File.separator) ? "" : File.separator;

            TarInputStream tarInputStream = new TarInputStream(new BufferedInputStream(zipFile.inputStream));
            TarEntry entry;

            while((entry = tarInputStream.getNextEntry()) != null) {
                byte data[] = new byte[2048];
                int count;

                BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(extractPath + entry.getName()));

                while((count = tarInputStream.read(data)) != -1) {
                    dest.write(data, 0, count);
                }

                dest.flush();
                dest.close();
            }

            tarInputStream.close();
            callbackContext.success();

        } catch (JSONException e) {
            String errorMessage = "An error occurred while extracting file. " + e.getMessage();
            callbackContext.error(errorMessage);
            Log.e(LOG_TAG, errorMessage, e);

        } catch (IOException e) {
            String errorMessage = "An error occurred while extracting file. " + e.getMessage();
            callbackContext.error(errorMessage);
            Log.e(LOG_TAG, errorMessage, e);
        }

    }

    private Uri getUriForArg(final String arg) {
        CordovaResourceApi resourceApi = webView.getResourceApi();
        Uri tmpTarget = Uri.parse(arg);
        return resourceApi.remapUri(
                tmpTarget.getScheme() != null ? tmpTarget : Uri.fromFile(new File(arg)));
    }
}
