package com.tianyigps.online.utils;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

/**
 * Created by cookiemouse on 2017/10/10.
 */

public class CoordinateConverterU {

    public static LatLng baiduToGaodeLatlng(Context context, com.baidu.mapapi.model.LatLng sourceLatLng) {
        LatLng latLng = new LatLng(sourceLatLng.latitude, sourceLatLng.longitude);
        CoordinateConverter converter = new CoordinateConverter(context);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.BAIDU);
        // sourceLatLng待转换坐标点 LatLng类型
        converter.coord(latLng);
        // 执行转换操作
        return converter.convert();
    }
}
