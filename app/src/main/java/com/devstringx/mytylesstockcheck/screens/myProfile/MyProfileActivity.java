package com.devstringx.mytylesstockcheck.screens.myProfile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.ActivityMyProfileBinding;
import com.devstringx.mytylesstockcheck.databinding.ProfilePhotoBottomsheetBinding;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Images;
import com.devstringx.mytylesstockcheck.datamodal.requirenmentData.RequirenmentData;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.DataItem;
import com.devstringx.mytylesstockcheck.datamodal.userProfileData.Response;
import com.devstringx.mytylesstockcheck.screens.AddLeadActivity;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.ImageCropActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.VendorDashboardActivity;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyProfileActivity extends AppCompatActivity implements ResponseListner {
    private static final int REQUEST_PROFILE_PIC = 100;
    ActivityMyProfileBinding myProfileBinding;
    public boolean isEditable = false;
    public List<DataItem> profileData = new ArrayList<>();
    ProfilePhotoBottomsheetBinding profilePhotoBottomsheetBinding;
    private final int REQUEST_CODE_PERMISSIONS = 101;
    private Response response;
    private Common common;
    private Context mContext;
    private String IMAGE_FILEPATH = "";
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.READ_MEDIA_IMAGES"
    };
    private final String[] LOWER_VERSION_REQUIRED_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        if (getIntent().getSerializableExtra("profileData") != null) {
            profileData = (List<DataItem>) getIntent().getSerializableExtra("profileData");
        }

        mContext = this;
        common = new Common();
        Common.loadFragment(getSupportFragmentManager(), new MyProfileOptionsFragment(), R.id.profile_frag_container);
        setData();
        myProfileBinding.profilePicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });
        myProfileBinding.backLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.receivedSocketMessage(MyProfileActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setData() {

        myProfileBinding.roleTv.setText(Html.fromHtml("<b>" + profileData.get(0).getFirstName() + " " + profileData.get(0).getLastName() + "(" + profileData.get(0).getRole() + ")</b>"));
        if (profileData.get(0).getVendorCompanyName() != null)
            myProfileBinding.vendorCompany.setText(profileData.get(0).getVendorCompanyName().toString());
        if (profileData.get(0).getProfileImage() != null) {
            Glide.with(getBaseContext())
                    .load(profileData.get(0).getProfileImage())
                    .transform(new CenterCrop(), new CircleCrop())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(myProfileBinding.profilePicIv);
        } else {
            Glide.with(getBaseContext())
                    .load(R.drawable.profile_bg)
                    .transform(new CenterCrop(), new CircleCrop())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(myProfileBinding.profilePicIv);
        }

    }

    private void openBottomSheet() {
        myProfileBinding.backLl.setVisibility(View.GONE);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        profilePhotoBottomsheetBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.profile_photo_bottomsheet, null, false);
        bottomSheetDialog.setContentView(profilePhotoBottomsheetBinding.getRoot());
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                myProfileBinding.backLl.setVisibility(View.VISIBLE);
            }
        });
        profilePhotoBottomsheetBinding.uploadProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                ArrayList<String> mMimeTypesList = new ArrayList<>();
                mMimeTypesList.add("image/*");
                captureImageResultLauncher.launch(
                        new FilePicker.Builder(MyProfileActivity.this)
                                .pickDocumentFileBuild(
                                        new DocumentFilePickerConfig(
                                                null,
                                                null,
                                                false,
                                                1,
                                                mMimeTypesList,
                                                null,
                                                null,
                                                null,
                                                null
                                        )
                                )
                );
            }
        });
        profilePhotoBottomsheetBinding.removeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRemoveProfilePicApi(new PreferenceUtils(getBaseContext()).getStringFromPreference(PreferenceUtils.USERID, "").toString());
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void callRemoveProfilePicApi(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.REMOVEPROFILEPIC, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_REMOVEPROFILEPIC, map, true);
    }

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        fragmentManager
                .beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getName().toString())
                .commit();
    }

    private void uploadAttachment(String uri) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.USERID, ""));
        map.put("file", uri);
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_UPLOADPROFILEIMAGE, map, true);
    }

    private final ActivityResultLauncher<Intent> captureImageResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            try {
                                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                                    if (result.getData().getData() != null) {
                                        String listData = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                        File testFile = new File(listData);
                                        float size = testFile.length() / (1024 * 1024);
                                        if (size <= 2) {
                                            Intent intent = new Intent(MyProfileActivity.this, ImageCropActivity.class);
                                            intent.putExtra("FILEPATH",listData);
                                            ImageCropActivityResultLauncher.launch(intent);
                                        } else {
                                            Common.showToast(MyProfileActivity.this, "Please upload a file smaller than 2 MB.");
                                        }
                                    } else {
//                                        ArrayList<Uri> listData = getClipDataUris(result.getData());
//                                        // ArrayList<String> listData = result.getData().getStringArrayListExtra(Const.BundleExtras.FILE_PATH_LIST);
//                                        uriList.addAll(listData);
                                    }
                                }
                            } catch (Exception e) {
                                Common.showLog(e.getMessage());
                            }

                        }
                    });


    private void getUserProfile(String id) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERPROFILE, new Gson().toJson(map1));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETUSERPROFILE, map, true);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (!(manager.getFragments().get(0) instanceof MyProfileOptionsFragment)) {
            manager.popBackStack();
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_UPLOADPROFILEIMAGE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getUserProfile(new PreferenceUtils(getBaseContext()).getStringFromPreference(PreferenceUtils.USERID, "").toString());
                Common.showToast(this, jsonObject.optString("message", ""));
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_REMOVEPROFILEPIC) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getUserProfile(new PreferenceUtils(getBaseContext()).getStringFromPreference(PreferenceUtils.USERID, "").toString());
                Common.showToast(this, jsonObject.optString("message", ""));
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERPROFILE) {
                response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                Intent intent = new Intent(this, MyProfileActivity.class);
                Common.showLog("12345678====" + response.getData());
                List<DataItem> refreshData = response.getData();
                intent.putExtra("profileData", (Serializable) response.getData());
                startActivityForResult(intent, 123);
                finish();
            }
        }
    }

    ActivityResultLauncher<Intent> ImageCropActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        try {
                            if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                                if (result.getData().getData() != null) {
                                    Uri listData = result.getData().getData();
                                    File testFile = new File(listData.toString());
                                    float size = testFile.length() / (1024 * 1024);
                                    uploadAttachment(common.getRealPathFromURI(mContext,listData));
                                } else {
                                    Common.showToast(mContext,"Please try again");
                                }
                            }
                        } catch (Exception e) {
                            Common.showLog(e.getMessage());
                        }
                }
            });


}