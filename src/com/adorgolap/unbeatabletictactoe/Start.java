package com.adorgolap.unbeatabletictactoe;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.ImageButton;

public class Start extends Activity implements OnClickListener {
	ImageButton ibIMoveFirst, ibAIMovesFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		ibIMoveFirst = (ImageButton) findViewById(R.id.ibImoveFirst);
		ibAIMovesFirst = (ImageButton) findViewById(R.id.ibAImovesFirst);
		ibAIMovesFirst.setOnClickListener(this);
		ibIMoveFirst.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ibImoveFirst)
		{
			Intent i = new Intent(Start.this,MainActivity.class);
			i.putExtra("whoMove", false);
			startActivity(i);
		}
		else if (v.getId() == R.id.ibAImovesFirst)
		{
			Intent i = new Intent(Start.this,MainActivity.class);
			i.putExtra("whoMove", true);
			startActivity(i);
		}
	}
}
