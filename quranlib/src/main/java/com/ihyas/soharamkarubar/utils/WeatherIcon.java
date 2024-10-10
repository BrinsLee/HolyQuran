package com.ihyas.soharamkarubar.utils;

import com.ihyas.soharamkaru.R;

public class WeatherIcon {

    public static int get_icon_id_white(String icon) {
        //clear sky
        if (icon.equals("01d"))
            return R.drawable.clearskyday;
        else if (icon.equals("01n"))
            return R.drawable.clearskynight;
            //few clouds
        else if (icon.equals("02d"))
            return R.drawable.fewcloudsday;
        else if (icon.equals("02n"))
            return R.drawable.fewcloudsnight;
            //scattered clouds
        else if (icon.equals("03d") || icon.equals("03n") || icon.equals("04d") || icon.equals("04n"))
            return R.drawable.scatteredcloudsday;
        else if (icon.equals("09d") || icon.equals("09n"))
            return R.drawable.scatteredcloudsnight;
            //rain
        else if (icon.equals("10d"))
            return R.drawable.rainday;
        else if (icon.equals("10n"))
            return R.drawable.rainnight;
            //thunderstorm
        else if (icon.equals("11d") || icon.equals("11n"))
            return R.drawable.thunderstormday;
            //snow
        else if (icon.equals("13d") || icon.equals("13n"))
            return R.drawable.snow;
            //mist
        else if (icon.equals("50d") || icon.equals("50n"))
            return R.drawable.mist;

        return R.drawable.scatteredcloudsday;

    }

}
