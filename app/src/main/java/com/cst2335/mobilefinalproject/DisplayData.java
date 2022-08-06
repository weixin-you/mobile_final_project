package com.cst2335.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DisplayData extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private JSONArray data = Pexels.retrievedData;
    public static List<PhotoInfo> photoInfoList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        recyclerView=findViewById(R.id.recyclerView);
        for (int i = 0; i < data.length(); i++) {
            PhotoInfo photoInfo = new PhotoInfo();
            JSONObject objFromArray = null;
            try {
                objFromArray = data.getJSONObject(i);
                photoInfo.setWidth(objFromArray.getInt("width"));
                photoInfo.setHeight(objFromArray.getInt("height"));
                photoInfo.setImageUrl(objFromArray.getJSONObject("src").getString("original"));
                photoInfo.setPhotographerName(objFromArray.getString("photographer"));
                photoInfo.setUrl(objFromArray.getString("url"));
                photoInfoList.add(photoInfo);

            } catch (JSONException  e) {
                e.printStackTrace();
            }
        }
        Log.i("obj", String.valueOf(photoInfoList.size()));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), photoInfoList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        // private Bitmap[] localDataSet;
        private List<PhotoInfo> dataSet;
        private Context mContext;
        public String imageF;
        public int heightF;
        public int widthF;
        public String urlF;

        public RecyclerAdapter(List<PhotoInfo> photoInfoList) {
        }


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final ImageView imageView;
            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                imageView = (ImageView) view.findViewById(R.id.photo);
                textView = (TextView) view.findViewById(R.id.author_name);

            }

            public TextView getTextView() {
                return textView;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public RecyclerAdapter(Context mContext, List<PhotoInfo> dataSet) {
            this.dataSet = dataSet;
            this.mContext=mContext;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_items, viewGroup, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element

            viewHolder.getTextView().setText(dataSet.get(position).getPhotographerName());
            Picasso.get()
                    .load(dataSet.get(position).getImageUrl())
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.imageView);

            viewHolder.itemView.setOnClickListener((input) -> {

                Context context = getApplicationContext();
                CharSequence text = "Show details";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Bundle bundle = new Bundle();
                bundle.putInt("widthF", dataSet.get(position).getWidth());
                bundle.putInt("heightF", dataSet.get(position).getHeight());
                bundle.putString("imageUrlF", dataSet.get(position).getImageUrl());
                bundle.putString("urlF", dataSet.get(position).getUrl());

                DetailsPhoto dFragment = new DetailsPhoto();
                dFragment.setArguments(bundle);
                Intent nextActivity = new Intent(DisplayData.this, EmptyActivity.class);
                nextActivity.putExtras(bundle);
                startActivity(nextActivity);
            });

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }
}