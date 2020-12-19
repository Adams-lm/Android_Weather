package com.hznu.lin.project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hznu.lin.project.entity.Test;

import java.util.List;

/**
 * @author LIN
 * @date 2020/12/18 16:15
 */
@Dao
public interface TestDao {

    @Query("SELECT * FROM test")
    List<Test> getAll();

    @Query("SELECT * FROM test WHERE id IN (:testIds)")
    List<Test> loadAllByIds(int[] testIds);

    @Query("SELECT * FROM test WHERE message LIKE :message")
    Test findByMessage(String message);

    @Insert
    void insertAll(Test... tests);

    @Delete
    void delete(Test test);
}
