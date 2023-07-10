package com.example.weather_api_call;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Document;

import java.util.List;

public class KakaoData {
    @SerializedName("documents")
    public List<Document> documentList;

    public static class Document{
        @SerializedName("region_type")
        private String region_type;
        @SerializedName("address_name")
        private String address_name;
        @SerializedName("x")
        private float x;
        @SerializedName("y")
        private float y;

        public String getRegion_type() {
            return region_type;
        }

        public void setRegion_type(String region_type) {
            this.region_type = region_type;
        }

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
