package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ProductListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.screens.AddProductActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    ProductListItemBinding binding;
    List<RecordsItem> allProducts=new ArrayList<>();
    Context mContext;

    public ProductFragment(Context context,List<RecordsItem> records) {
        mContext=context;
        allProducts=records;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.product_list_item,container,false);
        View view=binding.getRoot();


//        binding.addNewProductLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.addNewProductLl.setVisibility(View.GONE);
//                ((AddProductActivity)mContext).getAdapter().addFrag(new ProductFragment(mContext,allProducts));
//                ((AddProductActivity)mContext).getAdapter().notifyDataSetChanged();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((AddProductActivity)mContext).getBinding().viewPager.setCurrentItem(((AddProductActivity)mContext).getAdapter().getmFragmentList().size()-1);
//                    }
//                }, 3000);
//                }
//        });
        return view;
    }

    public ProductListItemBinding getBinding() {
        return binding;
    }
}