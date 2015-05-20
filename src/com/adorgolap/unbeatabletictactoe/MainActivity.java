package com.adorgolap.unbeatabletictactoe;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adorgolap.utils.Board;

public class MainActivity extends Activity implements OnClickListener {
	private Context context;
	private static final int SIZE = 3;
	private GridView gridView;
	private TextView tvStatus;
	private ImageButton ibExit, ibRestart;
	public String[] VALUES = new String[] { "E", "E", "E", "E", "E", "E", "E",
			"E", "E" };
	Board b = new Board();
	boolean aiMovesFirst = false;
	boolean gameEnded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = getIntent();
		aiMovesFirst = i.getBooleanExtra("whoMove", true);
		// new AlertDialog.Builder(this)
		// .setTitle("Start")
		// .setMessage("Who will move first?")
		// .setPositiveButton(R.string.me, new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// // do Nothing
		// }
		// })
		// .setNegativeButton(R.string.ai, new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// Random r = new Random();
		// b.setAIMove(r.nextInt(3), r.nextInt(3));
		// manipulateGrigView();
		// gridView.invalidate();
		// }
		// })
		// .setIcon(android.R.drawable.ic_dialog_alert)
		// .show();
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/chiller.ttf");
		tvStatus.setTypeface(tf);
		ibRestart = (ImageButton) findViewById(R.id.ibRestart);
		ibExit = (ImageButton) findViewById(R.id.ibExit);
		ibRestart.setOnClickListener(this);
		ibExit.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.gridView1);
		final ImageAdapter iadap = new ImageAdapter(this, VALUES);
		gridView.setAdapter(iadap);
		if (aiMovesFirst) {
			Random r = new Random();
			b.setAIMove(r.nextInt(3), r.nextInt(3));
			manipulateGrigView();
		}
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (!gameEnded) {
					boolean isDraw = false;
					if (VALUES[position].equals("E")) {
						ImageView iv = (ImageView) v
								.findViewById(R.id.grid_item_image);
						iv.setImageResource(R.drawable.o);
						b.giveUserMove(position);
						manipulateGrigView();
						if (b.isGoalFor('o')) {
							// Toast.makeText(getApplicationContext(),
							// "Impossible",
							// Toast.LENGTH_SHORT).show();
							tvStatus.setText("Impossible");
							gameEnded = true;
						} else if (b.isTerminal()) {
							isDraw = true;
							// Toast.makeText(getApplicationContext(), "Draw",
							// Toast.LENGTH_SHORT).show();
							tvStatus.setText("Game Drawn !");
							gameEnded = true;
						}
						if (!isDraw) {
							DoIt x = new DoIt();
							x.execute();
						}

					} else {
						Toast.makeText(getApplicationContext(),
								"Please give valid move", Toast.LENGTH_SHORT)
								.show();
					}
					// iv.invalidate();
					// Toast.makeText(getApplicationContext(), "clicked",
					// Toast.LENGTH_SHORT).show();
				}else
				{
					Toast.makeText(getApplicationContext(),
							"Game Ended ! Please Restart.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	protected int getUpdatePos() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (!VALUES[i * SIZE + j].equals("" + b.getHolder()[i][j])) {
					return i * SIZE + j;
				}
			}
		}
		return -1;
	}

	private void manipulateGrigView() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				VALUES[i * SIZE + j] = "" + b.getHolder()[i][j];
			}
		}
	}

	private class DoIt extends AsyncTask<Void, Void, Void> {
		private ProgressDialog prgDialog;

		public DoIt() {
			prgDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			prgDialog.setMessage("Please be patient......");
			prgDialog.setMax(36);
			prgDialog.setTitle("AI is thinking");
			prgDialog.setCanceledOnTouchOutside(false);
			prgDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			b.giveThatAlphaBetaPruningMove();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			prgDialog.dismiss();
			int updatePos = getUpdatePos();
			if (updatePos == -1) {
				Toast.makeText(getApplicationContext(),
						"Error in update position", Toast.LENGTH_SHORT).show();
			} else {
				// Toast.makeText(getApplicationContext(),
				// "Updated pos " + updatePos,
				// Toast.LENGTH_SHORT).show();
				ImageView x = (ImageView) gridView.getChildAt(updatePos)
						.findViewById(R.id.grid_item_image);
				x.setImageResource(R.drawable.x);
			}
			manipulateGrigView();
			if (b.isGoalFor('x')) {
				// Toast.makeText(getApplicationContext(), "AI wins",
				// Toast.LENGTH_SHORT).show();
				tvStatus.setText("AI won !");
				gameEnded = true;
			} else if (b.isTerminal()) {
				// Toast.makeText(getApplicationContext(), "Draw",
				// Toast.LENGTH_SHORT).show();
				tvStatus.setText("Game Drawn !");
				gameEnded = true;
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		gridView = null;
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ibRestart) {
			for (int i = 0; i < 9; i++) {
				VALUES[i] = "E";
				ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(
						R.id.grid_item_image);
				iv.setImageResource(R.drawable.blank);
			}
			b = new Board();
			if (aiMovesFirst) {
				Random r = new Random();
				int row = r.nextInt(3);
				int col = r.nextInt(3);
				b.setAIMove(row, col);
				ImageView iv = (ImageView) gridView.getChildAt(row * 3 + col)
						.findViewById(R.id.grid_item_image);
				iv.setImageResource(R.drawable.x);
				manipulateGrigView();
			}
			tvStatus.setText("Game Started !");
			gameEnded = false;
		} else if (v.getId() == R.id.ibExit) {
			finish();
		}
	}
}
