package com.saurov.attendancemanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.orm.SugarApp;
import com.orm.SugarConfig;
import com.orm.SugarCursorFactory;
import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.saurov.attendancemanager.R;
import com.saurov.attendancemanager.adapters.CourseAdapter;
import com.saurov.attendancemanager.backup.RemoteBackup;
import com.saurov.attendancemanager.database.Course;
import com.saurov.attendancemanager.dialogs.CourseBottomSheetDialogFragment;
import com.saurov.attendancemanager.util.PdfUtils;
import com.saurov.attendancemanager.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.add_course_fab)
    FloatingActionButton fab;

    @BindView(R.id.course_recycler_view)
    RecyclerView courseRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CourseAdapter adapter;

    public static final String EDIT_COURSE_FLAG = "EDIT_COURSE_FLAG";
    public static final String COURSE_ID_TAG = "COURSE_ID_TAG";

    RemoteBackup remoteBackup;
    private boolean isBackup = true;

    private static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("My Courses");

        showSideDrawer();

        remoteBackup = new RemoteBackup(this);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CourseActivity.this, AddEditCourseActivity.class);

                startActivity(i);
            }
        });

        List<Course> courseList = Course.listAll(Course.class);

        adapter = new CourseAdapter(this, courseList);
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Course course, int position) {
                Intent i = new Intent(CourseActivity.this, CourseDetailActivity.class);
                i.putExtra(CourseDetailActivity.TAG_COURSE_ID, course.getId());
                startActivity(i);
            }

            @Override
            public void onMenuClick(Course course, int position) {
                CourseBottomSheetDialogFragment bottomSheet = new CourseBottomSheetDialogFragment();
                bottomSheet.show(CourseActivity.this.getSupportFragmentManager(), "example");

                bottomSheet.setOnItemClickListener(new CourseBottomSheetDialogFragment.BottomSheetListener() {
                    @Override
                    public void onItemClicked(String selectionId) {

                        Intent intent;

                        switch (selectionId) {

                            case CourseBottomSheetDialogFragment.ITEM_OPEN:

                                intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
                                intent.putExtra(CourseDetailActivity.TAG_COURSE_ID, course.getId());

                                startActivity(intent);

                                break;

                            case CourseBottomSheetDialogFragment.ITEM_DELETE:
//                                course.delete();
                                course.deleteCascade();

                                refreshCourseRecyclerView();
                                break;

                            case CourseBottomSheetDialogFragment.ITEM_EDIT:

                                intent = new Intent(CourseActivity.this, AddEditCourseActivity.class);

                                intent.putExtra(EDIT_COURSE_FLAG, "EDIT");

                                intent.putExtra(COURSE_ID_TAG, course.getId());

                                startActivity(intent);

                                break;
                            case CourseBottomSheetDialogFragment.ITEM_TAKE_ATTENDANCE:

                                intent = new Intent(CourseActivity.this, AddEditAttendanceActivity.class);
                                intent.putExtra(AddEditAttendanceActivity.TAG_COURSE_ID, course.getId());
                                startActivity(intent);
                                break;
                            case CourseBottomSheetDialogFragment.ITEM_EXPORT_PDF:
                                if (Utils.isStoragePermissionGranted(CourseActivity.this)) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CourseActivity.this);

                                    Map<String, String> teacherInfo = new HashMap<>();
                                    teacherInfo.put("Your Name", prefs.getString("name", ""));
                                    teacherInfo.put("Your Designation", prefs.getString("designation", ""));
                                    teacherInfo.put("Your Department", prefs.getString("department", ""));

                                    StringBuilder msg = new StringBuilder();

                                    List<String> missingSettings = new ArrayList<>();

                                    for (String key : teacherInfo.keySet()) {

                                        if (teacherInfo.get(key) == null || teacherInfo.get(key).trim().isEmpty()) {
                                            missingSettings.add(key);
                                        }

                                    }

                                    if (missingSettings.size() == 0) {
                                        PdfUtils.createAttendanceReport(CourseActivity.this, course);
                                    } else {

                                        showErrorMessage(msg, missingSettings);
                                    }

                                }
                                break;
                        }
                    }

                    private void showErrorMessage(StringBuilder msg, List<String> missingSettings) {
                        for (int i = 0; i < missingSettings.size(); i++) {

                            if (i < missingSettings.size() - 1) {
                                msg.append(missingSettings.get(i)).append(", ");
                            } else {
                                msg.append(missingSettings.get(i)).append(" ");
                            }

                        }


                        msg.append("is not set. Go to Settings and set them to Export PDF");

                        new AlertDialog.Builder(CourseActivity.this)
                                .setTitle("Can't Export PDF")
                                .setMessage(msg.toString())
                                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(CourseActivity.this, SettingsActivity.class));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                });
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        courseRecyclerView.setLayoutManager(layoutManager);
        courseRecyclerView.setItemAnimator(new

                DefaultItemAnimator());
        courseRecyclerView.setAdapter(adapter);
    }

    private void showSideDrawer() {

        IProfile profileDefault = new ProfileDrawerItem().withName("Attendance Manager").withEmail("Manage your course attendances").withIcon(R.mipmap.ic_launcher_round);

        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .addProfiles(profileDefault)
                .withProfileImagesClickable(false)
                .withCurrentProfileHiddenInList(true)
                .build();


        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .withDelayDrawerClickEvent(200)
                .withAccountHeader(drawerHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Courses").withIcon(FontAwesome.Icon.faw_book).withIdentifier(1).withSelectedTextColor(Color.BLACK).withSelectedIconColor(Color.BLACK).withSelectedColor(getResources().getColor(R.color.drawer_item_selected_color)),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Backup to Drive").withIdentifier(5),
                        new PrimaryDrawerItem().withName("Restore from Drive").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings).withSelectedIconColor(Color.BLACK).withIdentifier(2).withSelectedTextColor(Color.BLACK).withSelectedColor(getResources().getColor(R.color.drawer_item_selected_color)),
                        new PrimaryDrawerItem().withName("About").withIcon(GoogleMaterial.Icon.gmd_error).withSelectedIconColor(Color.BLACK).withSelectedTextColor(Color.BLACK).withSelectedColor(getResources().getColor(R.color.drawer_item_selected_color))
                )
                .withOnDrawerItemClickListener(drawerItemClickListener)
                .build();

        drawer.getActionBarDrawerToggle().getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

    }

    private Drawer.OnDrawerItemClickListener drawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem != null) {

                Intent intent = null;

                if (drawerItem.getIdentifier() == 1) {
//                    intent = new Intent(CourseActivity.this, CourseActivity.class);
                } else if (drawerItem.getIdentifier() == 2) {
                    intent = new Intent(CourseActivity.this, SettingsActivity.class);
                }else if (drawerItem.getIdentifier() == 5){
                    //Backup to drive
                    isBackup = true;
                    remoteBackup.connectToDrive(isBackup);
                }else if (drawerItem.getIdentifier() == 6){
                    isBackup = false;
                    remoteBackup.connectToDrive(isBackup);
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
            return false;
        }
    };

    private void refreshCourseRecyclerView() {
        adapter.refreshData(SugarRecord.listAll(Course.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:
                Log.i("Saurov", "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    remoteBackup.connectToDrive(isBackup);
                }
                break;

            case REQUEST_CODE_CREATION:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i("Saurov", "Backup successfully saved.");
                    Toast.makeText(this, "Backup successufly loaded!", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_OPENING:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    remoteBackup.mOpenItemTaskSource.setResult(driveId);
                } else {
                    remoteBackup.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCourseRecyclerView();

    }


}
