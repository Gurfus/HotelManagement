package ferranechaves.ioc.HolidayManagement;

import android.net.Uri;



public class Factura {


    public  String url,userId;

    public Factura(String url, String userId){
       this.url = url;
       this.userId = userId;
   }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
