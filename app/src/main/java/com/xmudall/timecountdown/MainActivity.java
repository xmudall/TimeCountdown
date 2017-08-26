package com.xmudall.timecountdown;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmudall.timecountdown.storage.SettingDao;
import com.xmudall.timecountdown.storage.SettingDaoSpImpl;
import com.xmudall.timecountdown.storage.Target;
import com.xmudall.timecountdown.storage.TargetDao;
import com.xmudall.timecountdown.storage.TargetDaoSPImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddTargetDialogFragment.AddTargetListener,
        MyListAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getName();

    private TextView labelCountdownHint;
    private ListView listView;
    private MyListAdapter listAdapter;

    private TargetDao targetDao;
    private SettingDao settingDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init dao
        targetDao = new TargetDaoSPImpl(this);
        settingDao = new SettingDaoSpImpl(this);

        // init view
        listView = (ListView) findViewById(R.id.listView);
        labelCountdownHint = (TextView) findViewById(R.id.label_countdown_hint);

        // init listview
        listAdapter = new MyListAdapter(loadData(), this);
        listAdapter.setListener(this);
        listView.setAdapter(listAdapter);

        setCountDownHint();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listAdapter.getData().size() >= 3) {
                    NoticeDialogFragment alertFragment = new NoticeDialogFragment()
                            .withMessageResourceId(R.string.message_too_many_targets);
                    alertFragment.show(getFragmentManager(), "tooManyTargets");
                } else {
                    DialogFragment newFragment = new AddTargetDialogFragment();
                    newFragment.show(getFragmentManager(), "addTarget");
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setCountDownHint() {
        // set countdown hint
        Map<String, String> setting = settingDao.load();
        if (setting.containsKey("age") && setting.containsKey("birth")) {
            try {
                int age = Integer.valueOf(setting.get("age"));
                String birth = setting.get("birth");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(birth));
                calendar.add(Calendar.YEAR, age);
                long end = calendar.getTimeInMillis();
                int diff = (int) ((end - System.currentTimeMillis()) / 1000 / 3600 / 24);
                String hint = String.format(
                        getResources().getString(R.string.label_countdown_hint),
                        String.valueOf(age),
                        String.valueOf(diff)
                );
                labelCountdownHint.setText(hint);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

    private List<Target> loadData() {
        List<Target> targets = targetDao.getAll();
        return targets;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSetting() {
        View view = getLayoutInflater().inflate(R.layout.dialog_setting, null);

        Map<String, String> setting = settingDao.load();
        final EditText editTextAge = (EditText) view.findViewById(R.id.editText_age);
        final EditText editTextBirth = (EditText) view.findViewById(R.id.editText_birth);
        editTextAge.setText(setting.get("age"));
        editTextBirth.setText(setting.get("birth"));
        NoticeDialogFragment dialogFragment = new NoticeDialogFragment()
                .withCustomView(view)
                .withListener(new NoticeDialogFragment.NoticeDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog) {
                        String age = editTextAge.getText().toString();
                        String birth = editTextBirth.getText().toString();
                        Map<String, String> setting = new HashMap<>();
                        setting.put("age", age);
                        setting.put("birth", birth);
                        settingDao.save(setting);
                        setCountDownHint();
                    }
                });
        dialogFragment.show(getFragmentManager(), "setting");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void addTarget(String targetContent) {
        if (targetContent == null || targetContent.isEmpty()) {
            DialogFragment alertFragment = new NoticeDialogFragment()
                    .withMessageResourceId(R.string.message_not_empty);
            alertFragment.show(getFragmentManager(), "shouldNotEmpty");
            return;
        }
        Target target = new Target();
        target.setContent(targetContent);
        target = targetDao.insert(target);
        listAdapter.getData().add(target);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(final int position) {
        Log.d(TAG, "item clicked " + position);
        DialogFragment alertFragment = new NoticeDialogFragment()
                .withMessageResourceId(R.string.message_confirm_delete)
                .withListener(new NoticeDialogFragment.NoticeDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog) {
                        deleteTarget(position);
                    }
                });
        alertFragment.show(getFragmentManager(), "deleteTarget");
    }

    public void deleteTarget(int position) {
        Target target = listAdapter.getData().get(position);
        if (targetDao.delete(target.getId())) {
            listAdapter.getData().remove(position);
            listAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.message_delete_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
