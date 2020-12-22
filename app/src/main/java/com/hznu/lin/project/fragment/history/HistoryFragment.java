package com.hznu.lin.project.fragment.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.hznu.lin.project.R;
import com.hznu.lin.project.dao.WeatherDataDao;
import com.hznu.lin.project.db.WeatherDataBase;
import com.hznu.lin.project.entity.WeatherData;
import com.hznu.lin.project.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HistoryFragment extends Fragment {


    @BindView(R.id.line_chart_past)
    LineChartView lineChartPast;
    @BindView(R.id.backgroundPast)
    LinearLayout backgroundPast;
    @BindView(R.id.line_chart_future)
    LineChartView lineChartFuture;
    @BindView(R.id.backgroundFuture)
    LinearLayout backgroundFuture;

    private SharedPreferences sp;

    private Integer dayPast;
    private Integer dayFuture;

    public static String[] datePast = new String[8];//X轴的标注
    public static String[] dateFuture = new String[8];
    public static int[] tempLowPast = new int[8];//图表的数据点
    public static int[] tempHighPast = new int[8];//图表的数据点
    public static int[] tempLowFuture = new int[8];//图表的数据点
    public static int[] tempHighFuture = new int[8];//图表的数据点
    private List<PointValue> pointLowPast = new ArrayList<>();
    private List<PointValue> pointHighPast = new ArrayList<>();
    private List<PointValue> pointLowFuture = new ArrayList<>();
    private List<PointValue> pointHighFuture = new ArrayList<>();
    private List<AxisValue> axisXPast = new ArrayList<>();
    private List<AxisValue> axisXFuture = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        pastDateInit();
        init();
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        HelloChartInit();
        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        // 设置背景透明
        backgroundPast.getBackground().setAlpha(50);
        backgroundFuture.getBackground().setAlpha(50);
        // sp初始化
        sp = getActivity().getSharedPreferences("com.hznu.lin.project_preferences", Context.MODE_PRIVATE);
        dayPast = Integer.valueOf(sp.getString("day_past", "7"));
        dayFuture = Integer.valueOf(sp.getString("day_future", "7"));
    }

    /**
     * HelloChart初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void HelloChartInit() {
        FutureInit();
        pastInit();
    }

    /**
     * 设置X轴的显示
     */
    private void getAxisXLables() {
        try {
            // Past
            for (int i = 0; i < dayPast; i++) {
                axisXPast.add(new AxisValue(i).setLabel(datePast[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }
        try {
            // Future
            for (int i = 0; i < dayFuture; i++) {
                axisXFuture.add(new AxisValue(i).setLabel(dateFuture[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }

    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        try {
            // Past
            for (int i = 0; i < dayPast; i++) {
                pointLowPast.add(new PointValue(i, tempLowPast[i]));
            }

            for (int i = 0; i < dayPast; i++) {
                pointHighPast.add(new PointValue(i, tempHighPast[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "历史天数不足，无法显示历史天气", Toast.LENGTH_SHORT);
        }

        try {
            // Future
            for (int i = 0; i < dayFuture; i++) {
                pointLowFuture.add(new PointValue(i, tempLowFuture[i]));
            }

            for (int i = 0; i < dayFuture; i++) {
                pointHighFuture.add(new PointValue(i, tempHighFuture[i]));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 历史图表初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pastInit() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLowPast).setColor(Color.parseColor("#0099ff"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHighPast).setColor(Color.parseColor("#0099ff"));
        lineHigh.setShape(ValueShape.CIRCLE);
        lineHigh.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineHigh.setFilled(false);//是否填充曲线的面积
        lineHigh.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineHigh.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineHigh.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineHigh);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelBackgroundEnabled(false);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setName("历史" + dayPast + "天天气信息");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
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

        // 视角设置
        Viewport v = new Viewport(lineChartPast.getMaximumViewport());
        v.top = Arrays.stream(tempHighPast).max().getAsInt() + 1; //最高点为最大值+1
        lineChartPast.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartPast.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }

    /**
     * 未来图表初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void FutureInit() {
        List<Line> lines = new ArrayList<>();
        // low
        Line lineLow = new Line(pointLowFuture).setColor(Color.parseColor("#027AFF"));
        lineLow.setShape(ValueShape.CIRCLE);
        lineLow.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineLow);

        // high
        Line lineHigh = new Line(pointHighFuture).setColor(Color.parseColor("#027AFF"));
        lineHigh.setShape(ValueShape.CIRCLE);
        lineHigh.setCubic(true);//曲线是否平滑，即是曲线还是折线
        lineHigh.setFilled(false);//是否填充曲线的面积
        lineHigh.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineHigh.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lineHigh.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(lineHigh);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelBackgroundEnabled(false);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setName("未来" + dayFuture + "天天气信息");  //表格名称
        axisX.setTextSize(13);//设置字体大小
        axisX.setTextColor(Color.WHITE);
        axisX.setValues(axisXFuture);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线


        //设置行为属性，支持缩放、滑动以及平移
        lineChartFuture.setInteractive(true);
        lineChartFuture.setZoomType(ZoomType.HORIZONTAL);
        lineChartFuture.setMaxZoom((float) 2);//最大方法比例
        lineChartFuture.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartFuture.setLineChartData(data);
        lineChartFuture.setVisibility(View.VISIBLE);

        // 视角设置
        Viewport v = new Viewport(lineChartFuture.getMaximumViewport());
        v.top = Arrays.stream(tempHighFuture).max().getAsInt() + 1; //最高点为最大值+1
        lineChartFuture.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartFuture.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图

    }

    /**
     * 历史图标初始化
     */
    private void pastDateInit() {
        WeatherDataBase weatherDataBase = Room.databaseBuilder(getContext(), WeatherDataBase.class, "WeatherDataBase.db").allowMainThreadQueries().build();
        WeatherDataDao weatherDataDao = weatherDataBase.weatherDataDao();
        WeatherData byDate = weatherDataDao.findByDate("12-22");

        List<WeatherData> all = weatherDataDao.getRecently();
        int count = 7;
        for (WeatherData data : all) {
            Log.i("WEATHER", data.toString());
            datePast[count] = data.getDate();
            tempLowPast[count] = Integer.parseInt(data.getLow());
            tempHighPast[count] = Integer.parseInt(data.getHigh());
            count--;
        }

    }

}