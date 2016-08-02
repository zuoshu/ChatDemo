package oneguy.com.chatdemo;

/**
 * Created by ZuoShu on 16/8/2.
 */
public class Account {
    private String accid;
    private String token;

    public Account(String accid, String token) {
        this.accid = accid;
        this.token = token;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
