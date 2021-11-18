package controller;

public class ValueConverter {

    public static int fromFalltypToInt(String s){
        if(s.equals("ambulant")){
            return 1;
        }
        else{
            return 2;
        }
    }
}
