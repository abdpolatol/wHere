package com.example.gspot.Post;

import java.util.Random;

import com.example.gspot.R;

import de.svenjacobs.loremipsum.LoremIpsum;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class newPostFragment extends Fragment {
	private com.example.gspot.Post.postAdapter adapter;
	private ListView lv;
	private LoremIpsum ipsum;
	private EditText editText1;
	private static Random random;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.activity_discuss, container, false);
	        return rootView;
	    }
	 public void onStart() {
	        super.onStart();
	        random = new Random();
			ipsum = new LoremIpsum();

			lv = (ListView) getView().findViewById(R.id.listView1);

			adapter = new postAdapter(getActivity().getApplicationContext(), R.layout.listitem_discuss);

			lv.setAdapter(adapter);

			editText1 = (EditText) getView().findViewById(R.id.editText1);
			editText1.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// If the event is a key-down event on the "enter" button
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
						// Perform action on key press
						adapter.add(new comment(false, editText1.getText().toString()));
						editText1.setText("");
						return true;
					}
					return false;
				}
			});

			addItems();
	 }
	 private void addItems() {
			adapter.add(new comment(true, "Hello bubbles!"));

			for (int i = 0; i < 30; i++) {
				boolean left = getRandomInteger(0, 1) == 0 ? true : false;
				int word = getRandomInteger(1, 50);
				int start = getRandomInteger(1, 40);
				String words = ipsum.getWords(word, start);

				adapter.add(new comment(left, words));
			}
		}

		private static int getRandomInteger(int aStart, int aEnd) {
			if (aStart > aEnd) {
				throw new IllegalArgumentException("Start cannot exceed End.");
			}
			long range = (long) aEnd - (long) aStart + 1;
			long fraction = (long) (range * random.nextDouble());
			int randomNumber = (int) (fraction + aStart);
			return randomNumber;
		}
	

}
