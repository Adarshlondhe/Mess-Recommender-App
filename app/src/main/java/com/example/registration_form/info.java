package com.example.registration_form;

public class info {
    public String mess ;
    public String add1 ;
    public String add2 ;
    public String cuisine ;
    public String description ;
    public String city ;
    public String phone;
    public String state ;
    public String latitude;
    public String longitude;

    public info(){

    }

    public void setMess(String Mess){
        mess = Mess;
    }
    public String getMess(){ return mess;}

    public void setAdd1(String Add1){
        add1 = Add1;
    }
    public String getAdd1(){ return add1;}

    public void setAdd2(String Add2){
        add2 = Add2;
    }
    public String getAdd2(){ return add2;}

    public void setCuisine(String Cuisine){
        cuisine = Cuisine;
    }
    public String getCuisine(){ return cuisine;}

    public void setDescription(String Description){
        description = Description;
    }
    public String getDescription(){ return description;}

    public void setCity(String City){
        city = City;
    }
    public String getCity(){ return city;}

    public void setState(String State){
        state = State;
    }
    public String getState(){ return state;}

    public void setPhone(String Phone){
        phone = Phone;
    }
    public String getPhone(){ return phone;}

    public void setLatitude(String Latitude){ latitude = Latitude; }
    public String getLatitude(){ return latitude;}

    public void setLongitude(String Longitude){ longitude = Longitude; }
    public String getLongitude(){ return longitude;}

    public String getDetails(){
        String s = "Mess name - "+ mess +"\nAddress- "+add1+","+add2+","+city+"\nCuisine - "+cuisine+
                "\nDescription - "+description+"\nPhone no.- "+phone+"\nCity - "+city;
        return s;
    }
}
