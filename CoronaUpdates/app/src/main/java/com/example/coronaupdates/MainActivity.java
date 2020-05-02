package com.example.coronaupdates;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    Spinner spnStates;

    PieChart pcChart;
    TextView tvResults;
    TextView tvName;
    TextView tvTemp;
    Button btnIndia;
    Button btnWorld;

    Button btnCases,btnTest,btnPrevention;

    CaseObject world;
    CaseObject india;
    ArrayList <CaseObject> states;
    ArrayList<String> statesArray;

    ArrayAdapter<String> stateAdapter;
    boolean iscompleted=false;

    //for test
    Button btnSymptoms, btnDisease, btnTravel, btnState, btnSubmit;
    TextView tvInfection;
    boolean[] checkSymptoms;
    boolean[] checkDisease;
    boolean[] checkTravel;
    TextView tvSymptoms,tvDiseases, tvTravel;
    Spinner spnStateTest;
    ArrayList<Integer> mSymptoms = new ArrayList<>();
    ArrayList<Integer> mDiseases = new ArrayList<>();
    ArrayList<Integer> mTravel = new ArrayList<>();
    ArrayAdapter<String> statetestAdapter;


    FragmentManager fragmentManager;
    FragCases fragcases;
    FragTest fragtest;
    FragPrevention fragprevention;

    final int totalPoints=69;
    int obtainedPoints;
    String status;


    // prevention
    TextView tvTips;
    Button btnCall, btnWhoHelp, btnIndiaHelp;
    Button btnImmunity, btnMakeMask, btnSuperheros, btnVirendra;

    String[] tips = {"RISK LEVEL    :  SAFE \n 1. Maintain Social Distance\n2. Take Self Assessment Test\n3. Avoid Social Gathering",
    "RISK LEVEL    :  MODERATE \n 2. Home Quarantine \n2. Maintain Social Distance\n3. Take Self Assessment Test\n4. Avoid Social Gathering",
    "RISK LEVEL    :  CRITICAL \n 1. Call the Helpline number for Home Checkup\n2. Home Quarantine 2. Take Self Assessment Test\n3. Avoid Social Gathering"};

    TextView tvShowText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        states = new ArrayList<>();
        statesArray = new ArrayList<>();

        tvShowText = findViewById(R.id.tvShowText);

        spnStates = findViewById(R.id.spnStates);
        pcChart = findViewById(R.id.chart);
        tvResults = findViewById(R.id.tvResults);
        tvName = findViewById(R.id.tvName);
        btnIndia = findViewById(R.id.btnIndia);
        btnWorld = findViewById(R.id.btnWorld);
        tvTemp = findViewById(R.id.tvTemp);

        spnStates.setVisibility(View.INVISIBLE);
        tvShowText.setVisibility(View.INVISIBLE);
        pcChart.setVisibility(View.INVISIBLE);
        tvResults.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.INVISIBLE);
        btnIndia.setVisibility(View.INVISIBLE);
        btnWorld.setVisibility(View.INVISIBLE);

        tvTemp.setVisibility(View.INVISIBLE);
        btnCases = findViewById(R.id.btnCases);
        btnTest = findViewById(R.id.btnTest);
        btnPrevention = findViewById(R.id.btnPrevention);

        fragmentManager = this.getSupportFragmentManager();
        fragcases =  (FragCases) fragmentManager.findFragmentById(R.id.fragcases);
        fragtest = (FragTest) fragmentManager.findFragmentById(R.id.fragtest);
        fragprevention = (FragPrevention) fragmentManager.findFragmentById(R.id.fragprevention);

        tvTips = findViewById(R.id.tvTips);
        btnCall = findViewById(R.id.btnCall);
        btnWhoHelp=findViewById(R.id.btnWhoHelp);
        btnIndiaHelp=findViewById(R.id.btnIndiaHelp);

        btnImmunity = findViewById(R.id.btnImmunity);
        btnMakeMask = findViewById(R.id.btnMakeMask);
        btnSuperheros = findViewById(R.id.btnSuperhero);
        btnVirendra = findViewById(R.id.btnVirendra);

        btnImmunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/i6Nco0TQt_g"));
                startActivity(intent);

            }
        });

        btnMakeMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/YlafvtwzSho"));
                startActivity(intent);

            }
        });

        btnSuperheros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/He6jasd44V0"));
                startActivity(intent);

            }
        });

        btnVirendra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/kLxTPzC7lEI"));
                startActivity(intent);

            }
        });


        fragmentManager.beginTransaction()
                .show(fragcases)
                .hide(fragtest)
                .hide(fragprevention)
                .commit();

        btnCases.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnPrevention.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        btnTest.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        btnCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCases.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnPrevention.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnTest.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

                fragmentManager.beginTransaction()
                        .show(fragcases)
                        .hide(fragtest)
                        .hide(fragprevention)
                        .commit();


            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCases.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnPrevention.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnTest.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                fragmentManager.beginTransaction()
                        .hide(fragcases)
                        .show(fragtest)
                        .hide(fragprevention)
                        .commit();

            }
        });

        btnPrevention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCases.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnPrevention.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnTest.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

                fragmentManager.beginTransaction()
                        .hide(fragcases)
                        .hide(fragtest)
                        .show(fragprevention)
                        .commit();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1075"));
                startActivity(intent);
            }
        });

        btnWhoHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.who.int/emergencies/diseases/novel-coronavirus-2019"));
                startActivity(intent);
            }
        });

        btnIndiaHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mygov.in/covid-19"));
                startActivity(intent);
            }
        });









        //test
        btnSymptoms = findViewById(R.id.btnSymptoms);
        btnDisease = findViewById(R.id.btnDiseases);
        btnTravel = findViewById(R.id.btnTravel);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvInfection = findViewById(R.id.tvInfection);
        tvSymptoms = findViewById(R.id.tvSymptoms);
        tvDiseases = findViewById(R.id.tvDiseases);
        tvTravel = findViewById(R.id.tvTravel);
        spnStateTest = findViewById(R.id.spnStateTest);

        checkSymptoms = new boolean[3];
        checkDisease = new boolean[4];
        checkTravel = new boolean[3];

        tvInfection.setVisibility(View.GONE);

        btnSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Are you expereincing any of these symptoms");
                mBuilder.setMultiChoiceItems(getResources().getStringArray(R.array.symptoms), checkSymptoms, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if(isChecked)
                        {
                            if(!mSymptoms.contains(which))
                                mSymptoms.add(which);
                        }
                        else
                        {
                            if(mSymptoms.contains(which))
                                mSymptoms.remove(new Integer(which));
                        }

                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String item="";
                        for(int i=0;i<mSymptoms.size();i++)
                        {
                            item=item + getResources().getStringArray(R.array.symptoms)[mSymptoms.get(i)];
                            item+="\n";
                        }
                        tvSymptoms.setText(item);

                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i=0;i<checkSymptoms.length;i++)
                        {
                            checkSymptoms[i]=false;
                        }
                        mSymptoms.clear();
                        tvSymptoms.setText("");
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        btnDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Have you ever had any of these Diseases");
                mBuilder.setMultiChoiceItems(getResources().getStringArray(R.array.disease), checkDisease, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if(isChecked)
                        {
                            if(!mDiseases.contains(which))
                                mDiseases.add(which);
                        }
                        else
                        {
                            if(mDiseases.contains(which))
                                mDiseases.remove(new Integer(which));
                        }

                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String item="";
                        for(int i=0;i<mDiseases.size();i++)
                        {
                            item=item + getResources().getStringArray(R.array.disease)[mDiseases.get(i)];
                            item+="\n";
                        }
                        tvDiseases.setText(item);

                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i=0;i<checkDisease.length;i++)
                        {
                            checkDisease[i]=false;
                        }
                        mDiseases.clear();
                        tvDiseases.setText("");
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();


            }
        });

        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Your Travel History ? ");
                mBuilder.setMultiChoiceItems(getResources().getStringArray(R.array.travel_history), checkTravel, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if(isChecked)
                        {
                            if(!mTravel.contains(which))
                                mTravel.add(which);
                        }
                        else
                        {
                            if(mTravel.contains(which))
                                mTravel.remove(new Integer(which));
                        }

                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String item="";
                        for(int i=0;i<mTravel.size();i++)
                        {
                            item=item + getResources().getStringArray(R.array.travel_history)[mTravel.get(i)];
                            item+="\n";
                        }
                        tvTravel.setText(item);

                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i=0;i<checkTravel.length;i++)
                        {
                            checkTravel[i]=false;
                        }
                        mTravel.clear();
                        tvTravel.setText("");
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });

        String temp2[]={"Not Indian","Maharastra","Gujarat","Delhi","Rajasthan","Madhya Pradesh","Tamil Nadu","Uttar Pradesh"
                ,"Andhra Pradesh","Telangana","West Bengal", "Jammu and Kashmir","Karnataka","Kerala","Punjab","Bihar","Haryana"
                ,"Odhisha","Jharkhand","Chandigarh","Uttrakhand","Assam","Himachal Pradesh","Chhattisgarh"
                ,"Andaman and Nicobar Islands", "Ladakh", "Meghalaya","Puducherry","Goa","Manipur","Tripura",
                "Mizoram", "Arunachal Pradesh", "Nagaland","Dadra and Nagar Haveli","Daman and Diu","Lakshadweep","Sikkim"};

        statetestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temp2);
        statetestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnStateTest.setAdapter(statetestAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtainedPoints = 0;

                obtainedPoints+=mSymptoms.size();
                obtainedPoints+=mDiseases.size()*4;
                obtainedPoints+=mTravel.size()*10;

                int selected=spnStateTest.getSelectedItemPosition();

                if(selected!=0)
                {
                    String strCases = states.get(selected).getTotal_cases();

                    String temp="";
                    for(int i=0;i<strCases.length();i++)
                    {
                        if(strCases.charAt(i)!=',')
                            temp=temp+strCases.charAt(i);
                    }
                    int intCases = Integer.parseInt(temp);

                    obtainedPoints+= (intCases*20)/8000.0;

                }
                else
                    obtainedPoints+=9;

                if(obtainedPoints<=10) status="safe";
                else if(obtainedPoints<=35) status="moderate";
                else status="critical";

                tvInfection.setText("RISK LEVEL   : "+status);
                if(status=="safe")
                {tvInfection.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    tvTips.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                tvTips.setText(tips[0]);}
                else if(status=="moderate")
                {tvInfection.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    tvTips.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    tvTips.setText(tips[1]);
                }
                else
                {tvInfection.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    tvTips.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                tvTips.setText(tips[2]);
                }

                tvInfection.setVisibility(View.VISIBLE);


            }
        });


        //statesArray.add("adasdads");

        String temp[]={"India Total","Maharastra","Gujarat","Delhi","Rajasthan","Madhya Pradesh","Tamil Nadu","Uttar Pradesh"
        ,"Andhra Pradesh","Telangana","West Bengal", "Jammu and Kashmir","Karnataka","Kerala","Punjab","Bihar","Haryana"
                ,"Odhisha","Jharkhand","Chandigarh","Uttrakhand","Assam","Himachal Pradesh","Chhattisgarh"
                ,"Andaman and Nicobar Islands", "Ladakh", "Meghalaya","Puducherry","Goa","Manipur","Tripura",
                "Mizoram", "Arunachal Pradesh", "Nagaland","Dadra and Nagar Haveli","Daman and Diu","Lakshadweep","Sikkim"};

        new getDataInBackground().execute();

        stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, temp);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spnStates.setAdapter(stateAdapter);

        spnStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(iscompleted==true) {
                    setup(states.get(position));
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Still Loading !!! Check your connectivity !!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnIndia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(responseIndia)
                setIndia();
                else
                    Toast.makeText(MainActivity.this, "Unable to load data !!! Check connectivity", Toast.LENGTH_SHORT).show();

            }
        });

        btnWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(responseWorld)
                setWorld();
                else
                    Toast.makeText(MainActivity.this, "Unable to load data !!! Check connectivity", Toast.LENGTH_SHORT).show();
            }
        });



    }
    boolean responseIndia=false, responseWorld=false;
    public class getDataInBackground extends AsyncTask<Integer, Void, Exception>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Getting Data !!! ");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {


            OkHttpClient clientWorldData = new OkHttpClient();

            Request requestWorldData = new Request.Builder()
                    .url("https://corona-virus-world-and-india-data.p.rapidapi.com/api")
                    .get()
                    .addHeader("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "8b8ddbb109msh4db4bfdf9a6f720p1af29bjsn52e319679d3f")
                    .build();

            clientWorldData.newCall(requestWorldData).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    responseWorld=response.isSuccessful();
                    if(response.isSuccessful())
                    {
                        final String myResponse = response.body().string();

                        try {
                            JSONObject json = new JSONObject(myResponse);

                            String name="World";
                            String total_cases = (json.getJSONObject("world_total").getString("total_cases"));
                            String total_deaths = (json.getJSONObject("world_total").getString("total_deaths"));
                            String total_recovered = (json.getJSONObject("world_total").getString("total_recovered"));
                            String new_cases = (json.getJSONObject("world_total").getString("new_cases"));
                            String new_deaths = (json.getJSONObject("world_total").getString("new_deaths"));

                            world = new CaseObject(name, total_cases,total_deaths,total_recovered,new_cases,new_deaths);

                        }
                        catch (JSONException e)
                        {
                           exception=e;
                        }

                    }

                }
            });

            try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }


            OkHttpClient clientIndiaData = new OkHttpClient();

            Request requestIndiaData = new Request.Builder()
                    .url("https://corona-virus-world-and-india-data.p.rapidapi.com/api_india")
                    .get()
                    .addHeader("x-rapidapi-host", "corona-virus-world-and-india-data.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "8b8ddbb109msh4db4bfdf9a6f720p1af29bjsn52e319679d3f")
                    .build();

            clientIndiaData.newCall(requestIndiaData).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    responseIndia = response.isSuccessful();
                    if(response.isSuccessful()) {

                        final String myResponse = response.body().string();

                        try {

                            JSONObject json = new JSONObject(myResponse);

                            String name="India Total";
                            String total_cases = (json.getJSONObject("total_values").getString("confirmed"));
                            String total_deaths = (json.getJSONObject("total_values").getString("deaths"));
                            String total_recovered =(json.getJSONObject("total_values").getString("recovered"));
                            String new_cases = (json.getJSONObject("total_values").getString("deltaconfirmed"));
                            String new_deaths = (json.getJSONObject("total_values").getString("deltadeaths"));

                            india = new CaseObject(name, total_cases,total_deaths,total_recovered,new_cases,new_deaths);

                            JSONObject stateobj = new JSONObject(myResponse).getJSONObject("state_wise");

                            states.add(india);statesArray.add("India Total");

                            Iterator<String> keys = stateobj.keys();

                            while(keys.hasNext())
                            {
                                String key=keys.next();

                                JSONObject innerobj = stateobj.getJSONObject(key);

                                name = key;
                                total_cases = (innerobj.getString("confirmed"));
                                total_deaths =  (innerobj.getString("deaths"));
                                total_recovered =  (innerobj.getString("recovered"));
                                new_cases = (innerobj.getString("deltaconfirmed"));
                                new_deaths = (innerobj.getString("deltadeaths"));

                                states.add(new CaseObject(name, total_cases,total_deaths,total_recovered,new_cases,new_deaths));
                                statesArray.add(name);
                            }
                        }
                        catch(JSONException e)
                        {
                           exception=e;
                        }
                    }

                }
            });


            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);

            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException f) {
                f.printStackTrace();
            }

            progressDialog.dismiss();

            if(responseWorld)
            setWorld();
            else
                Toast.makeText(MainActivity.this, "Unable to load Data !!! Check Connectivity", Toast.LENGTH_SHORT).show();

            pcChart.setVisibility(View.VISIBLE);
            tvResults.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            btnIndia.setVisibility(View.VISIBLE);
            btnWorld.setVisibility(View.VISIBLE);

            iscompleted=true;
        }
    }

    public void setIndia()
    {
        btnIndia.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        btnWorld.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        spnStates.setVisibility(View.VISIBLE);
        tvShowText.setVisibility(View.VISIBLE);

        setup(states.get(0));
    }

    public void setWorld()
    {
        btnWorld.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        btnIndia.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        spnStates.setVisibility(View.GONE);
        tvShowText.setVisibility(View.GONE);

        setup(world);

    }

    private void setup(CaseObject obj)
    {

        tvName.setText(obj.getName());

        List<PieEntry> pieEntries = new ArrayList<>();

        String strTotal=obj.getTotal_cases();
        String strRecovered = obj.getTotal_recovered();
        String strDeaths = obj.getTotal_deaths();

        int total,recovered,deaths;

        String temp="";
        for(int i=0;i<strTotal.length();i++)
        {
            if(strTotal.charAt(i)!=',')
            temp=temp+strTotal.charAt(i);
        }
        total = Integer.parseInt(temp);

        temp="";
        for(int i=0;i<strRecovered.length();i++)
        {
            if(strRecovered.charAt(i)!=',')
                temp=temp+strRecovered.charAt(i);
        }
        recovered = Integer.parseInt(temp);

        temp="";
        for(int i=0;i<strDeaths.length();i++)
        {
            if(strDeaths.charAt(i)!=',')
                temp=temp+strDeaths.charAt(i);
        }
        deaths = Integer.parseInt(temp);

        pieEntries.add(new PieEntry(total, "Total Cases"));
        pieEntries.add(new PieEntry(recovered, "Recovered"));
        pieEntries.add(new PieEntry(deaths, "Deaths"));

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(false);
        dataSet.setLabel(obj.getName());
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);

        pcChart.setData(data);
        pcChart.setDescription(null);
        pcChart.animateY(1000);



        String result =
                "              Total Cases           :    "+obj.getTotal_cases()+
                "\n              Total Deaths          :    "+obj.getTotal_deaths()+
                "\n              Total Recovered    :    "+obj.getTotal_recovered()+
                "\n              New cases             :    "+obj.getNew_cases()+
                "\n              New Deaths           :    "+obj.getNew_deaths();

        tvResults.setText(result);
        pcChart.invalidate();
    }


}
