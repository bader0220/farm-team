package me.smartfarm.ui.main.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.Query;

import me.smartfarm.R;
import me.smartfarm.adapters.AuthPagerAdapter;
import me.smartfarm.adapters.FarmsAdapter;
import me.smartfarm.adapters.HomePagerAdapter;
import me.smartfarm.common.Constants;
import me.smartfarm.common.Util;
import me.smartfarm.data.models.Farm;
import me.smartfarm.data.repositories.FarmRepository;
import me.smartfarm.databinding.FragmentHomeBinding;
import me.smartfarm.ui.auth.AuthActivity;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private HomePagerAdapter homePagerAdapter;
    private String[] tabTitles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Get tab titles from strings array
        tabTitles = getResources().getStringArray(R.array.crops);

        homePagerAdapter = new HomePagerAdapter(getActivity(), tabTitles);
        binding.viewPager.setAdapter(homePagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
        intiAddButton();
        return binding.getRoot();
    }

    private void intiAddButton() {
        Util.makeFarmerView(binding.btnAddNewFarm);
        binding.btnAddNewFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
                navController.navigate(R.id.action_home_to_addFarmFragment);
            }
        });
    }
}
//
//public class HomeFragment extends Fragment {
//
//    private TabLayout tabLayout;
//    private ViewPager2 viewPager;
//    private ViewPagerAdapter viewPagerAdapter;
//    private String[] tabTitles;
//    FragmentHomeBinding binding;
////    FarmsAdapter farmsAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager(), requireContext());
//        binding.viewPager.setAdapter(adapter);
//        binding.tabLayout.setupWithViewPager(binding.viewPager);
//
////        for (int i = 0; i <  binding.tabLayout.getTabCount(); i++) {
////            //noinspection ConstantConditions
////            TextView tv=(TextView)LayoutInflater.from(getContext()).inflate(R.layout.item_title_tab,null);
////            binding.tabLayout.getTabAt(i).setCustomView(tv);
////
////        }
////        binding.tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimaryText),
////                getResources().getColor(R.color.colorPrimaryTextLight));
//// Iterate through all the tabs and set the custom view
////        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
////            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
////            if (tab != null) {
////                // Inflate the custom tab layout and set it
////                @SuppressLint("InflateParams") TextView customTab = (TextView) LayoutInflater.from(requireContext()).inflate(R.layout.item_title_tab, null);
////                customTab.setText(tab.getText()); // Set the tab text
////                tab.setCustomView(customTab);
////            }
////        }
//        intiAddButton();
//        initRecentlyAdded();
//        return binding.getRoot();
//    }
//
//    private void initRecentlyAdded() {
////        FirestoreRecyclerOptions<Farm> options = new FirestoreRecyclerOptions.Builder<Farm>()
////                .setQuery(FarmRepository.getInstance(requireActivity()).getCollectionReference()
////                        .orderBy("creationDate", Query.Direction.DESCENDING).limit(5), Farm.class)
////                .build();
////        farmsAdapter = new FarmsAdapter(options);
////        binding.recyclerRecentlyAdded.setLayoutManager(new LinearLayoutManager(requireContext()));
////        binding.recyclerRecentlyAdded.setAdapter(farmsAdapter);
//    }
//
////    @Override
////    public void onStart() {
////        super.onStart();
////        farmsAdapter.startListening();
////    }
////
////    @Override
////    public void onStop() {
////        super.onStop();
////        farmsAdapter.startListening();
////    }
//
//    private void intiAddButton() {
//        if (Util.getCurrentUser().getUserTypeId() == Constants.FARMER_ROLE) {
//            binding.btnAddNewFarm.setVisibility(View.VISIBLE);
//        } else {
//            binding.btnAddNewFarm.setVisibility(View.GONE);
//
//        }
//        binding.btnAddNewFarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.content_frame);
//                navController.navigate(R.id.action_home_to_addFarmFragment);
//            }
//        });
//    }
//}