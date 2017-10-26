package com.huosuapp.imagedemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片实体类
 * 
 */
public class Photos implements Parcelable {
	public static final String DATA="data";
	public static final String POSITION="position";
	public String max;// 大图地址
	public String min;// 小图地址

	public Photos() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(max);
		out.writeString(min);
	}

	public static final Creator<Photos> CREATOR = new Creator<Photos>() {
		@Override
		public Photos[] newArray(int size) {
			return new Photos[size];
		}
		@Override
		public Photos createFromParcel(Parcel in) {
			return new Photos(in);
		}
	};

	public Photos(Parcel in) {
		max = in.readString();
		min = in.readString();
	}
}
