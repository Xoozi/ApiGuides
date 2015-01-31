package com.xoozi.apiguides.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.xoozi.apiguides.ActivityBase;
import com.xoozi.apiguides.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

public class ActivityActionBarShareProvider extends ActivityBase{

    private static final String SHARED_FILE_NAME = "shared.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _createShareFile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().
            inflate(R.menu.action_bar_share_action_provider, menu);

        MenuItem actionItem = 
            menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = 
            (ShareActionProvider)actionItem.getActionProvider();
        /**
         * actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
         * i don't kown the effect of set history file
         * */
        actionProvider.setShareIntent(_createShareIntent());


        MenuItem overflowItem =
            menu.findItem(R.id.menu_item_share_action_provider_overflow);
        ShareActionProvider overflowProvider = 
            (ShareActionProvider) overflowItem.getActionProvider();
        /**
         * overflowProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
         * i don't kown the effect of set history file
         * */
        overflowProvider.setShareIntent(_createShareIntent());
        return true;
    }


    private Intent _createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(getFileStreamPath(SHARED_FILE_NAME));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }

    @SuppressWarnings("deprecation")
    private void    _createShareFile(){
        InputStream is = null;
        FileOutputStream fos = null;

        try{
            try{
                is = getResources().openRawResource(R.raw.robot);
                fos = openFileOutput(SHARED_FILE_NAME, 
                        Context.MODE_WORLD_WRITEABLE | Context.MODE_APPEND);

                byte[] buf = new byte[1024];
                int length = 0;
                while((length = is.read(buf)) > 0){
                    fos.write(buf, 0, length);
                }
            }finally{
                is.close();
                fos.close();
            }
        }catch(IOException e){
            /** ignore*/
        }
    }
}
