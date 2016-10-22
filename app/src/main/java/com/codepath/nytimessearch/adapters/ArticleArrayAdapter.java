package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.codepath.nytimessearch.R.id.tvTitle;

/**
 * Created by vmiha on 10/20/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    // set up the view cache
    private class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get item for position
        Article article = this.getItem(position);

        ViewHolder viewHolder;

        // check to see if the existing view is being reused
        // if not using a recycled view, inflate the layout
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(tvTitle);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //clear our recycle image from recycled view from last time
        viewHolder.imageView.setImageResource(0);
        viewHolder.tvTitle.setText((article.getHeadline()));

        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(viewHolder.imageView);
        }

        return convertView;
    }

}
