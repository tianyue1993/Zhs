package com.zhs.zhs.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.AdressData;
import com.zhs.zhs.entity.Citys;
import com.zhs.zhs.entity.D;
import com.zhs.zhs.views.QNumberPicker;

import java.lang.reflect.Field;
import java.util.List;

public class ChooseCityUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    AlertDialog dialog;
    ChooseCityInterface cityInterface;
    QNumberPicker npProvince, npCity, npCounty;
    TextView tvCancel, tvSure;
    String[] newCityArray = new String[4];
    String areaCode = "";
    private AdressData bean;

    public void createDialog(Context context, String[] oldCityArray, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;
        bean = JSON.parseObject(CityData.getJson().trim(), AdressData.class);
        newCityArray[0] = bean.data.get(0).n.toString();
        newCityArray[1] = bean.data.get(0).d.get(0).n.toString();
        newCityArray[2] = bean.data.get(0).d.get(0).d.get(0).n.toString();
        areaCode = bean.data.get(0).d.get(0).d.get(0).c.toString();
        newCityArray[3] = areaCode;
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_choose_city);
        //初始化控件  
        tvCancel = (TextView) window.findViewById(R.id.tvCancel);
        tvSure = (TextView) window.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        npProvince = (QNumberPicker) window.findViewById(R.id.npProvince);
        npCity = (QNumberPicker) window.findViewById(R.id.npCity);
        npCounty = (QNumberPicker) window.findViewById(R.id.npCounty);
        setNomal();
        //省：设置选择器最小值、最大值、初始值  
        String[] provinceArray = new String[bean.data.size()];//初始化省数组  
        for (int i = 0; i < provinceArray.length; i++) {//省数组填充数据  
            provinceArray[i] = bean.data.get(i).n;
        }
        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值  
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        for (int i = 0; i < provinceArray.length; i++) {
            if (provinceArray[i].equals(newCityArray[0])) {
                npProvince.setValue(i);
                changeCity(i);//联动市数据  
            }
        }
    }

    //根据省,联动市数据  
    private void changeCity(int provinceTag) {
        List<Citys> cityList = bean.data.get(provinceTag).d;
        String[] cityArray = new String[cityList.size()];
        for (int i = 0; i < cityArray.length; i++) {
            cityArray[i] = cityList.get(i).n;
        }
        try {
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
            npCity.setWrapSelectorWheel(false);
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值  
        } catch (Exception e) {
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值  
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
            npCity.setWrapSelectorWheel(false);
        }
        for (int i = 0; i < cityArray.length; i++) {
            if (cityArray[i].equals(newCityArray[1])) {
                npCity.setValue(i);
                changeCounty(provinceTag, i);//联动县数据  
                return;
            }
        }
        npCity.setValue(0);
        changeCounty(provinceTag, npCity.getValue());//联动县数据  
    }

    //根据市,联动县数据  
    private void changeCounty(int provinceTag, int cityTag) {
        List<D> countyList = bean.data.get(provinceTag).d.get(cityTag).d;
        String[] countyArray = new String[countyList.size()];
        for (int i = 0; i < countyArray.length; i++) {
            countyArray[i] = countyList.get(i).n.toString();
        }
        try {
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
            npCounty.setWrapSelectorWheel(false);
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值  
        } catch (Exception e) {
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值  
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
            npCounty.setWrapSelectorWheel(false);
        }
        for (int i = 0; i < countyArray.length; i++) {
            if (countyArray[i].equals(newCityArray[2])) {
                npCounty.setValue(i);
                return;
            }
        }
        npCounty.setValue(1);
    }

    //设置NumberPicker的分割线透明、字体颜色、设置监听  
    private void setNomal() {
        //设置监听  
        npProvince.setOnValueChangedListener(this);
        npCity.setOnValueChangedListener(this);
        npCounty.setOnValueChangedListener(this);
        //去除分割线  
        setNumberPickerDividerColor(npProvince);
        setNumberPickerDividerColor(npCity);
        setNumberPickerDividerColor(npCounty);
        //设置字体颜色  
        setNumberPickerTextColor(npProvince, context.getResources().getColor(R.color.mainColor));
        setNumberPickerTextColor(npCity, context.getResources().getColor(R.color.mainColor));
        setNumberPickerTextColor(npCounty, context.getResources().getColor(R.color.mainColor));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dialog.dismiss();
                break;
            case R.id.tvSure:
                dialog.dismiss();
                cityInterface.sure(newCityArray);
                break;
        }
    }

    //选择器选择值监听  
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.npProvince:
                List<AdressData.Data> dataList = bean.data;
                newCityArray[0] = dataList.get(npProvince.getValue()).n;
                changeCity(npProvince.getValue());
                newCityArray[1] = dataList.get(npProvince.getValue()).d.get(0).n;
                newCityArray[2] = dataList.get(npProvince.getValue()).d.get(0).d.get(0).n.toString();
                areaCode = dataList.get(npProvince.getValue()).d.get(0).d.get(0).c.toString();
                newCityArray[3] = areaCode;
                break;
            case R.id.npCity:
                List<Citys> cityList = bean.data.get(npProvince.getValue()).d;
                newCityArray[1] = cityList.get(npCity.getValue()).n;
                changeCounty(npProvince.getValue(), npCity.getValue());
                newCityArray[2] = cityList.get(npCity.getValue()).d.get(1).n.toString();
                areaCode = cityList.get(npCity.getValue()).d.get(1).c.toString();
                newCityArray[3] = areaCode;
                break;
            case R.id.npCounty:
                List<D> countyList = bean.data.get(npProvince.getValue()).d.get(npCity.getValue()).d;
                newCityArray[2] = countyList.get(npCounty.getValue()).n.toString();
                areaCode = countyList.get(npCounty.getValue()).c.toString();
                newCityArray[3] = areaCode;
                break;
        }
    }

    //设置分割线颜色  
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值  
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.transparent)));// pf.set(picker, new Div)  
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //设置选择器字体颜色  
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}  