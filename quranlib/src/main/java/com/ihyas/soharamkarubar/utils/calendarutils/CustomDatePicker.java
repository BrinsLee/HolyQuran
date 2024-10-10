package com.ihyas.soharamkarubar.utils.calendarutils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

import com.ihyas.soharamkaru.R;

class CustomDatePicker extends DatePicker
{
	public CustomDatePicker(Context context, AttributeSet attrs)
	{    	
		super(context, attrs);
		StringBuilder name= new StringBuilder();
		Field[] fields = DatePicker.class.getDeclaredFields();
		try {
			String[] s={getResources().getString(R.string.text_muharram),getResources().getString(R.string.text_safar),getResources().getString(R.string.text_rabi_ul_awal),getResources().getString(R.string.text_rabi_ul_thani),getResources().getString(R.string.text_jumad_al_awal),getResources().getString(R.string.text_jumad_al_thani),getResources().getString(R.string.text_rajab),getResources().getString(R.string.text_shaban),getResources().getString(R.string.text_ramdan),getResources().getString(R.string.text_shawwal),getResources().getString(R.string.text_zul_qaadah),getResources().getString(R.string.text_zul_hajj)};//{"Muharram","Safar","Rabi'ul Awwal","Rabi'ul Akhir","Jumadal Ula","Jumadal Akhira","Rajab","Sha'ban","Ramadan","Shawwal","Dhul Qa'ada","Dhul Hijja"};
			String[]  years=new String[200];
			for(int i=0;i<200;i++)
			{
				years[i]=(1400+(i))+"";
			}

			for (Field field : fields)
			{
				name.append(" , ").append(field.getName());
				field.setAccessible(true);

				if (TextUtils.equals(field.getName(), "mMonthSpinner"))
				{
					NumberPicker monthPicker = (NumberPicker) field.get(this);
					monthPicker.setMinValue(0);
					monthPicker.setMaxValue(11);
					monthPicker.setDisplayedValues(s);
				} 

				if (TextUtils.equals(field.getName(), "mShortMonths"))
				{
					field.set(this, s);
				}


				if (TextUtils.equals(field.getName(), "mYearSpinner"))
				{
					NumberPicker monthPicker = (NumberPicker) field.get(this);
					monthPicker.setDisplayedValues(years);
				} 

			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}