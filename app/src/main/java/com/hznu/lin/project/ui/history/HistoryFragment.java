package com.hznu.lin.project.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.hznu.lin.project.R;
import com.hznu.lin.project.dao.TestDao;
import com.hznu.lin.project.db.TestDataBase;
import com.hznu.lin.project.entity.Test;

import java.util.List;

public class HistoryFragment extends Fragment {

    HistoryViewModel historyViewModel;
    TestDataBase dataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBase = Room.databaseBuilder(getContext(), TestDataBase.class, "testDataBase.db").allowMainThreadQueries().build();
        TestDao testDao = dataBase.userDao();

//        Test test1 = new Test(1, "111");
//        testDao.insertAll(test1);
        List<Test> all = testDao.getAll();
        for (Test test : all) {
            Log.i("DATABASE",test.toString());
        }
    }


}