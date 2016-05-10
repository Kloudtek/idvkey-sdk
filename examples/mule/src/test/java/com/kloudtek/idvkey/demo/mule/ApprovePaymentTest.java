package com.kloudtek.idvkey.demo.mule;

import com.kloudtek.muletesthelper.MuleHttpHelper;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;
import org.mule.util.IOUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yannick on 9/5/16.
 */
public class ApprovePaymentTest extends FunctionalTestCase {
    @Override
    protected String getConfigFile() {
        return "idvkey_mule_demo.xml";
    }

    @Test
    public void clientTestCase() throws Exception {
        String payload = IOUtils.getResourceAsString("payment1.json", ApprovePaymentTest.class);
        MuleMessage result = MuleHttpHelper.sendJsonPost(muleContext, "http://localhost:8081", payload);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        System.out.println(result.getPayloadAsString());
    }
}
