package com.mercadolibre.melisearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.melisearch.R;
import com.mercadolibre.melisearch.model.Item;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Martin A. Heras on 07/02/14.
 */
public class ItemsAdapter extends BaseAdapter {

    private List<Item> mItems;
    private Context mContext;

    private static class ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView priceTextView;
    }

    public ItemsAdapter(Context context, List<Item> items) {
        mItems = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_item, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.list_view_item_image);
            viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.list_view_item_title);
            viewHolder.priceTextView = (TextView) rowView.findViewById(R.id.list_view_item_price);

            rowView.setTag(viewHolder);
        }

        Item item = mItems.get(position);
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        NumberFormat format = NumberFormat.getCurrencyInstance();

        viewHolder.titleTextView.setText(item.getTitle());

        if (item.getPrice() != null) {
            viewHolder.priceTextView.setText(format.format(item.getPrice()));
        } else {
            viewHolder.priceTextView.setText(mContext.getResources().getText(R.string.price_not_available));
        }

        viewHolder.imageView.setImageBitmap(null);

        String thumbnailURLStr = item.getThumbnail() != null ? item.getThumbnail().toString() : null;
        Picasso.with(mContext).load(thumbnailURLStr).placeholder(R.drawable.placeholder).fit().into(viewHolder.imageView);

        return rowView;
    }
}
