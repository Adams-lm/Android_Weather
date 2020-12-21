package com.hznu.lin.project.fragment.weather;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hznu.lin.project.R;
import com.hznu.lin.project.entity.Bing;
import com.hznu.lin.project.util.HttpUtil;
import com.hznu.lin.project.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecommendFragment extends Fragment {

    @BindView(R.id.iv_QR)
    ImageView ivQR;
    @BindView(R.id.btn_bing)
    Button btnBing;
    @BindView(R.id.iv_bing)
    ImageView ivBing;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_QR)
    Button btnQR;

    private String content = "https://www.hznu.edu.cn/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化生成hznu二维码
        Bitmap bitmap = createQECodeBitmap(content, 500, 500, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
        ivQR.setImageBitmap(bitmap);
    }

    @OnClick({R.id.btn_QR, R.id.btn_bing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_QR:
                String string = etContent.getText().toString();
                if (!string.equals("")) {
                    content = string;
                }
                Bitmap bitmap = createQECodeBitmap(content, 500, 500, "UTF-8", "H", "1", Color.BLACK, Color.WHITE);
                ivQR.setImageBitmap(bitmap);
                ToastUtil.showToast(getContext(), "二维码更新成功", Toast.LENGTH_SHORT);
                break;
            case R.id.btn_bing:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String getUrl = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=5&mkt=zh-CN";
                        String response = HttpUtil.get(getUrl, null);
                        String imageUrl = null;
                        // Json 处理
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray bingArray = jsonObject.getJSONArray("images");
                            ArrayList<Bing> bingList = new ArrayList<>();
                            for (int i = 0; i < bingArray.length(); i++) {
                                JSONObject bingObject = bingArray.getJSONObject(i);
                                Bing bing = new Bing(bingObject.getString("url"), bingObject.getString("copyright"));
                                bingList.add(bing);
                            }
                            // 随机数处理
                            int size = bingList.size();
                            int random = new Random().nextInt(size);
                            Bing bing = bingList.get(random);
                            imageUrl = "https://www.bing.com" + bing.getUrl();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message msg = Message.obtain();
                        msg.obj = imageUrl;
                        handlerBing.sendMessage(msg);
                    }
                }).start();
                break;
        }
    }


    // 获取Bing每日一图
    @SuppressLint("HandlerLeak")
    private final Handler handlerBing = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            String urlPath = (String) msg.obj;
            // UI处理
            ivBing.setVisibility(View.VISIBLE);
            // Glide
            Glide.with(getContext()).load(urlPath).into(ivBing);
            ToastUtil.showToast(getContext(), "Bing每日一图更新成功", Toast.LENGTH_SHORT);
            super.handleMessage(msg);
        }
    };

    /**
     * 创建二维码
     *
     * @param content
     * @param width
     * @param height
     * @param character_set
     * @param error_correction
     * @param margin
     * @param color_black
     * @param color_white
     * @return
     */
    public Bitmap createQECodeBitmap(String content, int width, int height, String character_set, String error_correction,
                                     String margin, int color_black, int color_white) {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();

        /** 1.设置二维码相关配置 */
        // 字符转码格式设置
        if (!TextUtils.isEmpty(character_set)) {
            hints.put(EncodeHintType.CHARACTER_SET, character_set);
        }
        // 容错率设置
        if (!TextUtils.isEmpty(error_correction)) {
            hints.put(EncodeHintType.ERROR_CORRECTION, error_correction);
        }
        // 空白边距设置
        if (!TextUtils.isEmpty(margin)) {
            hints.put(EncodeHintType.MARGIN, margin);
        }

        /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = color_black;//黑色色块像素设置
                } else {
                    pixels[y * width + x] = color_white;// 白色色块像素设置
                }
            }
        }
        /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}