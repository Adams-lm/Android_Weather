package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.TestDao;
import com.hznu.lin.project.entity.Test;

/**
 * @author LIN
 * @date 2020/12/18 16:17
 */
@Database(entities = {Test.class}, version = 1)
public abstract class TestDataBase extends RoomDatabase {

    public abstract TestDao userDao();

}