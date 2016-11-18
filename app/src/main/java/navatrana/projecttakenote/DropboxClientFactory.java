package navatrana.projecttakenote;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by Vamsee on 8/14/2016.
 */
public class DropboxClientFactory {

    private static DbxClientV2 sDbxClient;

    public static void init(String accesstoken){
        if (sDbxClient == null){
            DbxRequestConfig config = DbxRequestConfig.newBuilder("learnaccess")
                    .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                    .build();

            sDbxClient = new DbxClientV2(config, accesstoken);
        }
    }

    public static DbxClientV2 getClient(){
        if (sDbxClient == null){
            throw new IllegalStateException("Client not initialized");
        }
        return sDbxClient;
    }
}
