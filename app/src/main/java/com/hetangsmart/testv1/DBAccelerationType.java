package com.hetangsmart.testv1;

/**
 * Created by jasonbu on 2015/11/13.
 */
public class DBAccelerationType {
    public int stamp;
    public  int x,y,z;
    public  DBAccelerationType(){
        this.stamp = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public  DBAccelerationType(int stamp,int x,int y,int z){
        this.stamp = stamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Boolean IsValid()
    {
        if((this.x == 0)
            &&(this.y == 0)
            &&(this.z == 0)){
            return false;
        }else{
            return true;
        }
    }
}
