package io.github.tomacla.common.security.filter;

import javax.servlet.Filter;

import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

public interface FilterPosition {

    public static final Class<? extends Filter> FIRST = ChannelProcessingFilter.class;
    public static final Class<? extends Filter> LAST = SwitchUserFilter.class;
    public static final Class<? extends Filter> PRE_AUTH = AbstractPreAuthenticatedProcessingFilter.class;
    
    
}
