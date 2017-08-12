package pick.option.com.optionalpick;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.MultiPicker;

/**
 * Created by Administrator on 2017/8/12.
 */

public class MultiPickerManager {

    private static Map<String, List<String>> sOptionalData = new HashMap<>();
    private static Gson sJsonParser = new Gson();

    public static MultiPicker build(Activity context) {
        MultiPicker mMultiPicker = new MultiPicker(context);
        return mMultiPicker;
    }

    public static Map<String, List<String>> getOptionalData(){
        return sOptionalData;
    }

    public static void init(final Context context, final String jsonFileName) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MultiPickerManager.loadJsonFileData(context, jsonFileName);
            }
        });
    }

    /**
     * 加载本地asserts目录下的Json数据
     *
     * @param context
     */
    private static void loadJsonFileData(final Context context, final String jsonFileName) {
        if (!sOptionalData.isEmpty()) {
            return;
        }
        InputStream fis = null;
        try {
            fis = context.getAssets().open(jsonFileName);
            Country country = sJsonParser.fromJson(new BufferedReader(new InputStreamReader(fis)), new TypeToken<Country>() {}.getType());
            MultiPickerManager.parseProvinceWith(country);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析省份数据
     * @param country
     */
    private static void parseProvinceWith( Country country){ 
        if (country != null) {
            sOptionalData.clear();
            List<Province> provinces = country.list;
            if (provinces != null && !provinces.isEmpty()) {
                for(Province province : provinces){
                    sOptionalData.put(province.name,parseCityWith(province.list));
                }
            }
        }
    }

    /**
     * 解析城市数据
     * @param list
     * @return
     */
    private static List<String> parseCityWith(List<City> list){
        List<String> cities = new ArrayList<>();
        if(list == null || list.isEmpty())return cities;
        for(City city :list){
            cities.add(city.name);
        }
        return  cities;
    }

    /**
     * 国家实体类
     */
    public static class Country implements Serializable {

        @SerializedName("parentId")
        public String parentId;

        @SerializedName("divisionId")
        public String id;

        @SerializedName("divisionName")
        public String name;

        @SerializedName("divisionLevel")
        public String level;

        @SerializedName("childDivisionList")
        public List<Province> list;
    }

    /**
     * 省份实体类
     */
    public static class Province implements Serializable {

        public String parentId;

        @SerializedName("divisionId")
        public String id;

        @SerializedName("divisionName")
        public String name;

        @SerializedName("divisionLevel")
        public String level;

        @SerializedName("childDivisionList")
        public List<City> list;

    }

    /**
     * 城市实体类
     */
    public static class City implements Serializable {

        public String parentId;

        @SerializedName("divisionId")
        public String id;

        @SerializedName("divisionName")
        public String name;

        @SerializedName("divisionLevel")
        public String level;

    }
}
