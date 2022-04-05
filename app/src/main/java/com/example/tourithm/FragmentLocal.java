package com.example.tourithm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentLocal extends Fragment implements View.OnClickListener {
    MainScreen mainScreen;

    ListView listview1, listview2;
    ArrayList<localitem> arrayList;
    ArrayList<localitem> arrayList2;
    List city = new ArrayList();
    List gun = new ArrayList();
    List cityresult, gunresult;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainScreen = (MainScreen) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainScreen = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.localchoice, container, false);

        listview1 = viewGroup.findViewById(R.id.locallist1);
        listview2 = viewGroup.findViewById(R.id.locallist2);
        listview1.setSelector(R.drawable.arrow);
        arrayList = new ArrayList<>();

        new FragmentLocal.Select_local_Request().execute();

        // 리스트뷰 아이템 클릭 시
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] gunarr;

                List result = new ArrayList();
                arrayList2 = new ArrayList<>();

                for(int i=0; i<gunresult.size(); i++) {
                    gunarr = gunresult.get(i).toString().split(" ");
                    if(gunarr[0].equals(cityresult.get(position).toString())) {
                        result.add(gunarr[0] + " " + gunarr[1]);
                    }
                }

                // 리스트 아이템 데이터 삽입
                for(int i=0; i<result.size(); i++) {
                    localitem item = new localitem();
                    item.setLocal(result.get(i).toString());
                    arrayList2.add(item);
                }

                localAdapter localAdapter2 = new localAdapter(getActivity(), arrayList2);
                Log.d("[아이템 선택]", cityresult.get(position).toString() + " 선택됨");
                listview2.setAdapter(localAdapter2);
            }
        });

        return viewGroup;
    }

    // 리스트 중복 제거 클래스
    public class Deduplication_List {
        public ArrayList<String> List(List<String> list) {
            ArrayList<String> resultlist = new ArrayList<>();
            for(int i=0; i<list.size(); i++) {
                if(!resultlist.contains(list.get(i))){
                    resultlist.add(list.get(i));
                }
            }
            return (resultlist);
        }
    }

    class Select_local_Request extends AsyncTask<String, Integer, String> {
        String result = null;
        @Override
        protected String doInBackground(String... rurls) {
            try {
                URL url = new URL("https://idox23.cafe24.com/Tourithm/PlaceData_result.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                } else {
                    result = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String _result) {
            try {
                JSONObject root = new JSONObject(_result);
                JSONArray results = new JSONArray(root.getString("results"));

                // 리스트 중복 제거 클래스 선언
                FragmentLocal.Deduplication_List de_list = new FragmentLocal.Deduplication_List();

                for (int index = 0; index < results.length(); index++) {
                    JSONObject Content = results.getJSONObject(index);
                    String local = Content.getString("add_road");
                    String[] local_arr = local.split(" ");

                    city.add(local_arr[0]);
                    gun.add(local_arr[0] + " " + local_arr[1]);
                }

                // 중복 제거
                cityresult = de_list.List(city);
                gunresult = de_list.List(gun);

                Log.d("[arrayList]", gunresult.toString());

                // 중복 제거된 데이터 삽입
                for(int i=0; i<cityresult.size(); i++) {
                    localitem item = new localitem();
                    item.setLocal(cityresult.get(i).toString());
                    arrayList.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            localAdapter localAdapter = new localAdapter(getActivity(), arrayList);
            listview1.setAdapter(localAdapter);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
