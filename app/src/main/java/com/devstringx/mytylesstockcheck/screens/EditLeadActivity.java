package com.devstringx.mytylesstockcheck.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.UploadFileAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityEditLeadBinding;

public class EditLeadActivity extends AppCompatActivity {
    ArrayAdapter<String> adapterItems;
    String[] item={"a","b","c","d"};
    ActivityEditLeadBinding binding;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_lead);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        adapterItems=new ArrayAdapter<>(this,R.layout.dropdown_item_list,item);
        binding.assignOwner.setAdapter(adapterItems);
        binding.leadSource.setAdapter(adapterItems);
        binding.leadType.setAdapter(adapterItems);
        binding.requirement.setAdapter(adapterItems);
        binding.selectCity.setAdapter(adapterItems);
        binding.selectState.setAdapter(adapterItems);
        binding.assignOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(EditLeadActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });
        binding.leadType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Common.showToast(EditLeadActivity.this,item);
            }
        });
        binding.leadSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(EditLeadActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });
        binding.selectState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(EditLeadActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });
        binding.selectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(EditLeadActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });
        binding.requirement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(EditLeadActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });
        binding.chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Set MIME type to all files
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });

        setupAdapter();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the selected file's URI
            Uri selectedFileUri = data.getData();

            // Now you can use the URI to perform operations on the selected file
            // For example, display the file path:
            String filePath = selectedFileUri.getPath();
            // or perform further actions, such as uploading the file to a server, etc.

            // Note: Ensure you have proper permissions in your AndroidManifest.xml to read external storage.
        }
    }
    private void setupAdapter() {
//        UploadFileAdapter adapter = new UploadFileAdapter();
//        binding.uploadFileRv.setHasFixedSize(true);
//        binding.uploadFileRv.setLayoutManager(new GridLayoutManager(this,3));
//        binding.uploadFileRv.setAdapter(adapter);
    }
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}