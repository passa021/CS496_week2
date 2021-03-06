package com.example.mealparty;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PopupActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    ArrayList<ListData> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int place;
    int DEFAULT_VAL = 100;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        numberPicker = findViewById(R.id.number_picker);

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("Today is"+dayOfWeek+"\n");

        if(dayOfWeek == 1){ //일요일일떄
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(1);
        } else {
            numberPicker.setMaxValue(8);
            numberPicker.setMinValue(dayOfWeek);
        }

        numberPicker.setWrapSelectorWheel(false);

        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        NumberPicker.Formatter mFormatter = new NumberPicker.Formatter() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public String format(int i) {

                String pickerdate;

                if(dayOfWeek == 1){
                    pickerdate = nowDate.format(formatter);
                } else{
                    int cur = i-dayOfWeek;//0이면 당일, 1이면 내일 ,...최대 6
                    LocalDate curDate = nowDate.plusDays(cur);
                    pickerdate = curDate.format(formatter);
                }
                return pickerdate;
            }
        };
        numberPicker.setFormatter(mFormatter);



        Intent intent = getIntent();

        ///나중에 datepicker로 수

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        ////

        models = (ArrayList<ListData>) intent.getSerializableExtra("menu");
        place = intent.getIntExtra("식당",DEFAULT_VAL);
        ArrayList<Fragment> fragments = new ArrayList<>();




        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ListData MenuList = null;
                int cur = i1 - dayOfWeek;
                LocalDate curDate = nowDate.plusDays(cur);
                String selectedDate = curDate.format(formatter);

                for(ListData tmpList : models){
                    String datatime = tmpList.getDate();
                    System.out.println(datatime+"\n\n\n");
                    if(datatime != null) {
                        if (datatime.equals(selectedDate)) {
                            MenuList = tmpList;
                            break;
                        }
                    }
                }

                switch(place){
                    case 0:
                        fragments.clear();
                        fragments.add(MealItem.newInstance(MenuList.getKama_lunch(),1));
                        fragments.add(MealItem.newInstance(MenuList.getKama_dinner(),2));

                        break;
                    case 1:
                        fragments.clear();
                        fragments.add(MealItem.newInstance(MenuList.getWest_breakfast(),0));
                        fragments.add(MealItem.newInstance(MenuList.getWest_lunch(),1));
                        fragments.add(MealItem.newInstance(MenuList.getWest_dinner(),2));
                        break;
                    case 2:
                        fragments.clear();
                        fragments.add(MealItem.newInstance(MenuList.getEast_breakfast(),0));
                        fragments.add(MealItem.newInstance(MenuList.getEast_lunch(),1));
                        fragments.add(MealItem.newInstance(MenuList.getEast_dinner(),2));
                        break;
                }

                ModelAdapter modelAdapter = new ModelAdapter(PopupActivity.this, place, fragments);

                viewPager = (ViewPager2) findViewById(R.id.popup_viewpager);
                viewPager.setAdapter(modelAdapter);





            }
        });

        //ArrayList<Fragment> fragments = new ArrayList<>();
        //ArrayList<String[]> menudata = new ArrayList<>();

        ListData MenuList = null;

        if(models.isEmpty()==true){
            System.out.println("EMPTY");
        }else{
            System.out.print("NONO\n\n\n\n");
        }

        for(ListData tmpList : models){
            String datatime = tmpList.getDate();
            System.out.println(datatime+"\n\n\n");
            if(datatime != null) {
                if (datatime.equals(getTime)) {
                    MenuList = tmpList;
                    break;
                }
            }
        }

        switch(place){
            case 0:
                fragments.clear();
                fragments.add(MealItem.newInstance(MenuList.getKama_lunch(),1));
                fragments.add(MealItem.newInstance(MenuList.getKama_dinner(),2));
                break;
            case 1:
                fragments.clear();
                fragments.add(MealItem.newInstance(MenuList.getWest_breakfast(),0));
                fragments.add(MealItem.newInstance(MenuList.getWest_lunch(),1));
                fragments.add(MealItem.newInstance(MenuList.getWest_dinner(),2));
                break;
            case 2:
                fragments.clear();
                fragments.add(MealItem.newInstance(MenuList.getEast_breakfast(),0));
                fragments.add(MealItem.newInstance(MenuList.getEast_lunch(),1));
                fragments.add(MealItem.newInstance(MenuList.getEast_dinner(),2));
                break;
        }


        //models
        //Toast.makeText(getApplicationContext(),Integer.toString(place),Toast.LENGTH_SHORT).show();

        ModelAdapter modelAdapter = new ModelAdapter(this, place, fragments);

        viewPager = (ViewPager2) findViewById(R.id.popup_viewpager);
        viewPager.setAdapter(modelAdapter);

    }
}