package com.hznu.lin.project.fragment.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.hznu.lin.project.R;
import com.hznu.lin.project.dao.TestDao;
import com.hznu.lin.project.db.TestDataBase;
import com.hznu.lin.project.entity.Test;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class HistoryFragment extends Fragment {


    @BindView(R.id.line_chart_past)
    LineChartView lineChartPast;
    @BindView(R.id.background)
    LinearLayout background;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private Integer dayPast;
    private Integer dayFuture;

    TestDataBase dataBase;


    String[] date = {"12-18", "12-19", "12-20", "12-21", "12-22", "12-23", "12-24"};//X轴的标注
    int[] tempLowPast = {1, 2, -1, 3, 1, 3, 2};//图表的数据点
    int[] tempHighPast = {8, 9, 8, 10, 8, 10, 8};//图表的数据点
    private List<PointValue> pointLowPast = new ArrayList<>();
    private List<PointValue> pointHighPast = new ArrayList<>();
    private List<AxisValue> axisXPast = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        background.getBackground().setAlpha(50);
        spInit();
        getAxisXLables(dayPast);//获取x轴的标注
        getAxisPoints(dayPast);//获取坐标点
        HelloChartInit();
        return view;
    }


    private void spInit() {
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        dayPast = Integer.valueOf(sp.getString("day_past", "7"));
        dayFuture = Integer.valueOf(sp.getString("day_future", "7"));
    }

    private void HelloChartInit() {
        // low
        Line line = new Line(pointLowPast).setColor(Color.parseColor("#027AFF"));
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        // hight
        Line line2 = new Line(pointHighPast).setColor(Color.parseColor("#027AFF"));
        line2.setShape(ValueShape.CIRCLE);
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(true);//曲线的数据坐标是否加上备注
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line2);
        LineChartData data = new LineChartData();

        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName("过去7天天气预报");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(axisXPast);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        //设置行为属性，支持缩放、滑动以及平移
        lineChartPast.setInteractive(true);
        lineChartPast.setZoomType(ZoomType.HORIZONTAL);
        lineChartPast.setMaxZoom((float) 2);//最大方法比例
        lineChartPast.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartPast.setLineChartData(data);
        lineChartPast.setVisibility(View.VISIBLE);
    }

    /**
     * 设置X轴的显示
     */
    private void getAxisXLables(Integer dayPast) {
        for (int i = 0; i < dayPast; i++) {
            axisXPast.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(Integer dayPast) {
        for (int i = 0; i < dayPast; i++) {
            pointLowPast.add(new PointValue(i, tempLowPast[i]));
        }

        for (int i = 0; i < dayPast; i++) {
            pointHighPast.add(new PointValue(i, tempHighPast[i]));
        }
    }


    private void sqliteTest() {
        dataBase = Room.databaseBuilder(getContext(), TestDataBase.class, "testDataBase.db").allowMainThreadQueries().build();
        TestDao testDao = dataBase.userDao();

//        Test test1 = new Test(1, "111");
//        testDao.insertAll(test1);
        List<Test> all = testDao.getAll();
//        for (Test test : all) {
//            Log.i("DATABASE", test.toString());
//        }
    }

}