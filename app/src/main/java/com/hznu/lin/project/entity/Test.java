package com.hznu.lin.project.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @author LIN
 * @date 2020/12/18 16:15
 */
@Entity(tableName = "test")
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "message")
    private String message;

    public Test() {
    }

    @Ignore
    public Test(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}