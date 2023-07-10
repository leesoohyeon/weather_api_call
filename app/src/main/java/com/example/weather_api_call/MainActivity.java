package com.example.weather_api_call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private double wido;
    private double kyungdo;
    private String rw_lat;
    private String rw_lng;

    private String Pop =""; // 강수확률 %
    private String Pty =""; // 강수형태
    private String Reh =""; // 습도 %
    private String Sky =""; // 하늘상태
    private String cSky = ""; //하늘상태(코드값)
    private String Tmp =""; // 1시간 기온 ℃
    private String Tmn =""; // 일 최저기온 ℃
    private String Tmx =""; // 일 최고기온 ℃
    private String Wsd =""; // 풍속 m/s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //위도 경도 가져오기
        getLocation();

        //위도 경도로 현재 위치 주소 가져오기 Kakao Rest Api 호출
        getAdressAPI(kyungdo,wido);

        // 위도, 경도 날짜 불러오는 좌표로 변환
        convertGRID_GPS(wido,kyungdo);

        String nTime = new Util().getbaseTime();
        String nDay = new Util().getNowDay();

        //공공데이터 포털 api 호출
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    api_call(nTime, nDay, rw_lng, rw_lat);
                    Thread.sleep(5000);
                }catch (IOException | JSONException | InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void convertGRID_GPS(double lat_x, double lng_y){
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기준점 Y좌표(GRID)
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        double ra = Math.tan(Math.PI * 0.25 + (lat_x) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng_y * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        int a = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int b = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        rw_lat = String.valueOf(a);
        rw_lng = String.valueOf(b);
    }

    public void getAdressAPI(double kyungdo, double wido){

        Retrofit kakaRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dapi.kakao.com/")
                .build();

        Retrofit_Service retrofitAPI = kakaRetro.create(Retrofit_Service.class);

        retrofitAPI.serchAddressList(String.valueOf(kyungdo),String.valueOf(wido),BuildConfig.KAKAO_KEY).enqueue(new Callback<KakaoData>() {

            @Override
            public void onResponse(Call<KakaoData> call, Response<KakaoData> response) {
                if(response.isSuccessful()){
                    TextView AreaText = findViewById(R.id.Location);
                    AreaText.setText(response.body().documentList.get(0).getAddress_name());
                }
            }

            @Override
            public void onFailure(Call<KakaoData> call, Throwable t) {
                Log.d("SuccessfulResult", "실패");

            }
        });
    }

    public void getLocation(){
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
                String provider = location.getProvider();  // 위치정보
                double longitude = location.getLongitude(); // 위도
                double latitude = location.getLatitude(); // 경도
                double altitude = location.getAltitude(); // 고도
                kyungdo = longitude;
                wido = latitude;
            } public void onStatusChanged(String provider, int status, Bundle extras) {

            } public void onProviderEnabled(String provider) {

            } public void onProviderDisabled(String provider) {

            }
        };

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }else{

            // 가장최근 위치정보 가져오기

//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) ;
//            Log.d("LocationResult", String.valueOf(dd));
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();
                kyungdo = longitude;
                wido = latitude;

            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
        }
    }

    public void api_call(String nTime, String nDay, String rw_lng, String rw_lat) throws IOException, JSONException{
        String endPoint = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = BuildConfig.Weather_key;
        String pageNo = "1";
        String numOfRows = "40";
        String baseDate = nDay;
        String baseTime = nTime;
        String nx = rw_lng; // rw_lng
        String ny = rw_lat; //rw_lat

        String s = endPoint+"?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"&dataType=JSON"
                + "&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bufferedReader = null;
        if(conn.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            //connection error :(
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
        conn.disconnect();
        JSONObject mainObject = new JSONObject(result);
        JSONArray itemArray = mainObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        ArrayList<JSONObject> itemList = new ArrayList();
        String NowTime = new Util().getFcstTime();
        NowTime = NowTime.substring(0,2);

        for(int i=0; i<itemArray.length(); i++){
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");
            String value = item.getString("fcstValue");
            String fcstTime = item.getString("fcstTime").substring(0,2);
            if(category.equals("POP")){
                Pop = value;
            }else if(category.equals("PTY")&&NowTime.equals(fcstTime)){
                Pty = value;
            }else if(category.equals("REH")&&NowTime.equals(fcstTime)){
                Reh = value;
            }else if(category.equals("SKY")&&NowTime.equals(fcstTime)){
                Sky = new Util().getSkyState(value);
                cSky = value;
            }else if(category.equals("TMP")&&NowTime.equals(fcstTime)){
                Tmp = value;
            }else if(category.equals("TMN")&&NowTime.equals(fcstTime)){
                Tmn = value;
            }else if(category.equals("TMX")&&NowTime.equals(fcstTime)){
                Tmx = value;
            }else if(category.equals("WSD")&&NowTime.equals(fcstTime)){
                Wsd = value;
            }
            itemList.add(item);
        }
        String clothesText = new Util().getClothes(Tmp);
        final Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView textPop = findViewById(R.id.Pop);
                TextView textSky = findViewById(R.id.Sky);
                TextView textTmp = findViewById(R.id.Tmp);
                TextView textWsd = findViewById(R.id.Wsd);
                TextView textCloehes = findViewById(R.id.clothes);
                ImageView weatherImg = findViewById(R.id.WeatherImg);
                LinearLayout main_bg = findViewById(R.id.main_layout);
                textPop.setText(Pop+"%");
                textSky.setText(Sky);
                textTmp.setText(Tmp+"°C");
                textWsd.setText(Wsd+"m/s");
                textCloehes.setText(" - "+clothesText);

                if(Pty.equals("0") && cSky.equals("1")){

                    // pty(강수형태) : 없음(0) and sky(하늘상태) : 맑음(1)
                    weatherImg.setImageResource(R.drawable.sun);
                    main_bg.setBackgroundResource(R.drawable.sun_bg2);
                }else if(Pty.equals("0") && cSky.equals("4")){

                    // pty(강수형태) : 없음(0) and sky(하늘상태) : 흐림(4)
                    weatherImg.setImageResource(R.drawable.blur1);
                    main_bg.setBackgroundResource(R.drawable.blur_bg2);
                }else if(Pty.equals("2") || Pty.equals("4")){

                    // pty(강수형태) : 비/눈(2) , 소나기(4)
                    weatherImg.setImageResource(R.drawable.rain);
                    main_bg.setBackgroundResource(R.drawable.rain_bg2);
                }else if(Pty.equals("3")){

                    // pty(강수형태) : 눈(3)
                    weatherImg.setImageResource(R.drawable.snow);
                    main_bg.setBackgroundResource(R.drawable.snow_bg1);
                }
            }
        });


    }
}