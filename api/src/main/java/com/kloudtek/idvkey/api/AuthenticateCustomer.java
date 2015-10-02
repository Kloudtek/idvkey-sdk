/*
 * Copyright (c) 2015 Kloudtek Ltd
 */

package com.kloudtek.idvkey.api;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yannick on 27/08/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface AuthenticateCustomer {
}
