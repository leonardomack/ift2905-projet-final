package com.example.nobelprize;

import java.util.List;

import com.example.nobelprize.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * permet d'avoir une structure solide pour étendre les fonctionnalités
 * @author locust
 *
 */
public class AchievementsAdapter extends BaseAdapter {

	public static class AchievementData
	{

		public final int numTrophy;
		public final String title;
		public final String description;
		public final boolean isWon;

		public AchievementData(int numTrophy, String title, String description, boolean isWon) {
			super();
			this.numTrophy = numTrophy;
			this.title = title;
			this.description = description;
			this.isWon = isWon;
		}
	}

	private Context _context;
	private List<AchievementData> _data;

	@Override
	public int getCount() {
		if(_data != null)
			return _data.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int at) {
		if(_data != null && at >= 0 && at < _data.size())
			return _data.get(at);
		else
			return null;
	}

	@Override
	public long getItemId(int at) {
		return at;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		AchievementData data = _data.get(position);

		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.achievement_layout, parent, false);
		}
		
		TextView title = (TextView)view.findViewById(R.id.TextView_achievement_title);
		title.setText(data.title);
		ImageView trophy = (ImageView)view.findViewById(R.id.ImageView_achievement_trophy);
		Drawable star = null;
		
		if(_data.get(position).isWon)
		{
			title.setVisibility(View.VISIBLE);
			//	title.setBackground(_context.getResources().getDrawable(R.drawable.back));
			/*
			TextView description = (TextView)view.findViewById(R.id.TextView_achievement_description);
			description.setText(data.description);					
			description.setVisibility(View.VISIBLE);*/

			//trophy.setVisibility(View.VISIBLE);

			trophy.setVisibility(View.INVISIBLE);
			star = _context.getResources().getDrawable(R.drawable.btn_star_big_on);			
		}
		else
		{
			//	title.setBackgroundColor(Color.DKGRAY);
			title.setVisibility(View.VISIBLE);
			/*TextView description = (TextView)view.findViewById(R.id.TextView_achievement_description);
			description.setText(data.description);	
			description.setBackgroundColor(Color.DKGRAY);
			description.setVisibility(View.VISIBLE);*/

			trophy.setVisibility(View.INVISIBLE);
			star = _context.getResources().getDrawable(R.drawable.btn_star_big_off);
		}

		star.setBounds(0, 0, star.getIntrinsicWidth(), star.getIntrinsicHeight());
		title.setCompoundDrawables(null, null, star, null);
		return view;
	}

	public AchievementsAdapter(Context context, List<AchievementData> data)
	{
		_context = context;
		_data = data;
	}

}
