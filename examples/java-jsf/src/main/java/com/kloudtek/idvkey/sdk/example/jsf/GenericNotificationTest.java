package com.kloudtek.idvkey.sdk.example.jsf;

import com.kloudtek.idvkey.api.GenericNotificationRequest;
import com.kloudtek.idvkey.sdk.IDVKeyAPIClient;
import com.kloudtek.util.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * Created by yannick on 3/6/16.
 */
@Component
@Scope("request")
public class GenericNotificationTest {
    public static final String LOREMIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In euismod, quam eget rutrum finibus, leo tortor egestas ligula, sit amet lacinia nisi turpis id sapien. Ut eu felis tincidunt, tristique lorem ac, tempus sapien. Quisque quam velit, lacinia in nisl sed, volutpat pharetra enim. In scelerisque auctor nisl vel fermentum. Donec nisl enim, dignissim sed congue ut, luctus mattis diam. Integer tempor risus eu ornare egestas. Nam sed elit massa. Nullam vitae leo nisi.";
    @Autowired
    private transient IDVKeyAPIClient apiClient;
    @Value("${websiteId}")
    private transient String websiteId;
    @Autowired
    private transient UserCtx userCtx;

    public void send() throws IOException {
        URL dummyUrl = new URL("http://dummy");
        GenericNotificationRequest.Action action1 = new GenericNotificationRequest.Action("drive", "Drive", "green", "Are you sure you want to drive ?", "Have a good drive", GenericNotificationRequest.Location.MAIN_BUTTON);
        GenericNotificationRequest.Action action2 = new GenericNotificationRequest.Action("walk", "Walk", "yellow", "Are you sure you want to walk ?", "Have a good walk", GenericNotificationRequest.Location.MAIN_BUTTON);
        apiClient.sendGenericNotification(websiteId, new GenericNotificationRequest(userCtx.getLinkedUserRef(), dummyUrl,
                "Test notification", LOREMIPSUM, null, null, action1, action2));
        JSFUtils.addInfoMessage(null, "Sent notification");
    }
}
