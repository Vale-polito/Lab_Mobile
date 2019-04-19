package com.example.lab2firebase;

public class savedaysandhours {
        public String Weekday;
        public String hourfrom,hourto,hourfrom1,hourto1;
        public savedaysandhours(){

        }

        public savedaysandhours(String weekday, String hourfrom, String hourto, String hourfrom1, String hourto1){
            Weekday = weekday;
            this.hourfrom = hourfrom;
            this.hourto = hourto;
            this.hourfrom1 = hourfrom1;
            this.hourto1 = hourto1;

        }
    }

