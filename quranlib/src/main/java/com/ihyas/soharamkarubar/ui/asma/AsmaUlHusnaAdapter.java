package com.ihyas.soharamkarubar.ui.asma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.List;
import com.ihyas.soharamkaru.R;

public class AsmaUlHusnaAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> nameList;
    private Typeface tf;
    private final int selectedIndex;
    private final Activity activity;

    private final String[] text = {"&#xe900", "&#xe901", "&#xe902", "&#xe903", "&#xe904", "&#xe905", "&#xe906", "&#xe907", "&#xe908", "&#xe909", "&#xe90a", "&#xe90b", "&#xe90c", "&#xe90d", "&#xe90e", "&#xe90f", "&#xe910", "&#xe911", "&#xe912", "&#xe913", "&#xe914", "&#xe915", "&#xe916", "&#xe917", "&#xe918", "&#xe919", "&#xe91a", "&#xe91b", "&#xe91c", "&#xe91d", "&#xe91e", "&#xe91f", "&#xe920", "&#xe921", "&#xe922", "&#xe923", "&#xe924", "&#xe925", "&#xe926", "&#xe927", "&#xe928", "&#xe929", "&#xe92a", "&#xe92b", "&#xe92c", "&#xe92d", "&#xe92e", "&#xe92f", "&#xe930", "&#xe931", "&#xe932", "&#xe933", "&#xe934", "&#xe935", "&#xe936", "&#xe937", "&#xe938", "&#xe939", "&#xe93a", "&#xe93b", "&#xe93c", "&#xe93d", "&#xe93e", "&#xe93f", "&#xe940", "&#xe941", "&#xe942", "&#xe943", "&#xe944", "&#xe945", "&#xe946", "&#xe947", "&#xe948", "&#xe949", "&#xe94a", "&#xe94b", "&#xe94c", "&#xe94d", "&#xe94e", "&#xe94f", "&#xe950", "&#xe951", "&#xe952", "&#xe953", "&#xe954", "&#xe955", "&#xe956", "&#xe957", "&#xe958", "&#xe959", "&#xe95a", "&#xe95b", "&#xe95c", "&#xe95d", "&#xe95e", "&#xe95f", "&#xe960", "&#xe961", "&#xe962"};


    public AsmaUlHusnaAdapter(Activity activity, List<String> nameList, int selectedIndex) {
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedIndex = selectedIndex;
        this.activity = activity;
        this.nameList = nameList;
        setTypeface();
    }

    private void setTypeface() {
        tf = Typeface.createFromAsset(activity.getAssets(), "Font/asma_ul_husna.ttf");
    }


    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int location) {
        return nameList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void getMyView() {

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("checkflow", "39");
        View vi = convertView;
        Holder holder;
        try {

            if (convertView == null) {

                holder = new Holder();
                vi = inflater.inflate(R.layout.item_names, parent, false);
                holder.layout = vi.findViewById(R.id.layout);
                holder.engText = vi.findViewById(R.id.engText);
                holder.counterText = vi.findViewById(R.id.countText);
                holder.urduText = vi.findViewById(R.id.urduText);
                holder.urduText.setTypeface(tf);
                holder.description = vi.findViewById(R.id.description);
                vi.setTag(holder);
                Log.d("checkflow", "82");
            } else {
                holder = (Holder) vi.getTag();
            }

            if (position == selectedIndex) {

                Log.d("checkflow", "89");
                vi.setBackgroundColor(ContextCompat.getColor(this.activity, R.color.primaryVariant));

            } else {
                Log.d("checkflow", "93");
                vi.setBackgroundColor(ContextCompat.getColor(this.activity, R.color.primaryColor));

            }

            String[] nameData = nameList.get(position).split("###");
            holder.engText.setText(removeCharacter(nameData[1]));
            holder.counterText.setText("" + (position + 1));
            holder.urduText.setText("\t" + Html.fromHtml(removeCharacter(text[position].trim())));
            holder.description.setText(getStringResourceByName(getNameFromIndex(position)));
        } catch (Exception ex) {

        }
        return vi;
    }

    private String getNameFromIndex(int index) {
        if (index < 9)
            return "allah_name_0" + (index + 1);
        else
            return "allah_name_" + (index + 1);
    }


    private String getStringResourceByName(String aString) {
        String packageName = activity.getPackageName();
        int resId = activity.getResources().getIdentifier(aString, "string", packageName);
        return activity.getString(resId);
    }

    static class Holder {
        TextView engText, urduText, description, counterText;
        LinearLayout layout;
    }


    private String removeCharacter(String str) {
        str = str.replace("\r", "");
        str = str.replace("\n", "");
        return str;
    }

}