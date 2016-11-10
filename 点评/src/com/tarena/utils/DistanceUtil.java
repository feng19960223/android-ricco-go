package com.tarena.utils;

import com.baidu.mapapi.model.LatLng;

public class DistanceUtil {

	private static double EARTH_RADIUS = 6378.137 * 1000;// 米

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两个位置的经纬度，来计算两地的距离（单位为KM） 参数为String类型
	 * 
	 * @param lat1
	 *            用户经度
	 * @param lng1
	 *            用户纬度
	 * @param lat2
	 *            商家经度
	 * @param lng2
	 *            商家纬度
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2,
			double lng2) {// 有问题的计算
		double radLat1 = lat1;
		double radLat2 = lat2;
		double difference = radLat1 - radLat2;
		double mdifference = rad(lng1) - rad(lng2);
		double distance = 2 * Math.asin(Math.sqrt(Math.pow(
				Math.sin(difference / 2), 2)
				+ Math.cos(radLat1)
				* Math.cos(radLat2)
				* Math.pow(Math.sin(mdifference / 2), 2)));
		distance = distance * EARTH_RADIUS;
		distance = Math.round(distance * 10000) / 10000;
		return distance;
	}

	public double getDistance(LatLng l1, LatLng l2) {
		return getDistance(l1.latitude, l1.longitude, l2.latitude, l2.longitude);
	}
}