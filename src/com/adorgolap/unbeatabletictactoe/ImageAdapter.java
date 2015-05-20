package com.adorgolap.unbeatabletictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] boardValues;

	public ImageAdapter(Context context, String[] boardValues) {
		this.context = context;
		this.boardValues = boardValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.move, null);


			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			String state = boardValues[position];

			if (state.equals("E")) {
				imageView.setImageResource(R.drawable.blank);
			} else if (state.equals("o")) {
				imageView.setImageResource(R.drawable.o);
			} else if (state.equals("x")) {
				imageView.setImageResource(R.drawable.x);
			} 

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return boardValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
