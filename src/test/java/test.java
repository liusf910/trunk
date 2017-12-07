import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.etms.EtmsTraceGetRequest;
import com.jd.open.api.sdk.response.etms.EtmsTraceGetResponse;

/**
 * @author HuaXin Team
 * @version 1.0.0
 */
public class test {
    public static void main(String[] args) {
        JdClient client = new DefaultJdClient("http://gw.api.360buy.com/routerjson", "55640d2c-fe44-4a1d-8844-4cb38c0b5712", "9C0C22FDB513892735D16D633C979D99", "210c765de3c94c3ab1bba221477e3f64");
        EtmsTraceGetRequest request = new EtmsTraceGetRequest();
        request.setWaybillCode("VB38809741191");
        EtmsTraceGetResponse response;
        try {
            response = client.execute(request);
            System.out.println(response);
        } catch (JdException e) {
            throw new RuntimeException("京东跟踪信息doPost失败".concat(e.getMessage()));
        }

        }
    }

