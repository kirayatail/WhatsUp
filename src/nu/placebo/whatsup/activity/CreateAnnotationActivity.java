package nu.placebo.whatsup.activity;

import com.google.android.maps.GeoPoint;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.MenuHandler;
import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.service.model.DataProvider;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreateAnnotationActivity extends Activity implements
		OnClickListener, NetworkOperationListener<Annotation> {

	private GeoPoint gp;
	private TextView titleField;
	private TextView descField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setResult(RESULT_CANCELED);

		Bundle bundle = this.getIntent().getExtras();
		if (GeoPointUtil.bundleHasGeoPoint(bundle)) {
			this.gp = GeoPointUtil.popGeoPoint(bundle);

		} else {
			this.finish();
		}

		this.setContentView(R.layout.create_annotation);
		this.titleField = (TextView) this.findViewById(R.id.create_annot_title);
		this.descField = (TextView) this.findViewById(R.id.create_annot_desc);
		TextView debugLat = (TextView) this
				.findViewById(R.id.create_annot_debug_lat);
		TextView debugLong = (TextView) this
				.findViewById(R.id.create_annot_debug_long);

		debugLat.setText(Integer.toString(gp.getLatitudeE6()));
		debugLong.setText(Integer.toString(gp.getLongitudeE6()));

		Button submitBtn = (Button) this.findViewById(R.id.create_annot_submit);
		submitBtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		if (v.getId() == R.id.create_annot_submit) {

			String title = titleField.getText().toString();
			String desc = descField.getText().toString();
			SessionInfo sInfo = SessionHandler.getInstance(this).getSession();
			String author = SessionHandler.getInstance(this).getUserName();

			DataProvider.getDataProvider(getApplicationContext())
					.createAnnotation(title, desc, author, gp, sInfo, this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void operationExcecuted(OperationResult<Annotation> result) {
		// TODO Auto-generated method stub
		if (!result.hasErrors()) {
			setResult(Constants.ACTIVITY_FINISHED_OK);
			this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuHandler.inflate(menu, this.getMenuInflater());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHandler.onOptionsItemSelected(item, this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return MenuHandler.onPrepareOptionsMenu(menu, this);
	}
}
