package com.example.pritam.topdowloadedapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by pritam on 2/4/17.
 */

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        new DownloadImageTask(viewHolder.feedIcon).execute(currentApp.getImageURL());
        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());

        return convertView;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    private class ViewHolder {
        final ImageView feedIcon;
        final TextView tvName;
        final  TextView tvArtist;

        ViewHolder(View v) {
            this.feedIcon = (ImageView) v.findViewById(R.id.feedIcon);
            this.tvName = (TextView) v.findViewById(R.id.tvName);
            this.tvArtist = (TextView) v.findViewById(R.id.tvArtist);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public DownloadImageTask(ImageView feedIcon) {
            this.bitmapImage = feedIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bitmapImage.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlDisplay = params[0];
            Bitmap mIcon = null;

            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIcon;
        }
    }
}

