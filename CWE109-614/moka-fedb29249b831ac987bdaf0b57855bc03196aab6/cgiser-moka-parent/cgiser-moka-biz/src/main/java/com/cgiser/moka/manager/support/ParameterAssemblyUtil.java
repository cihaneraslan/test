package com.cgiser.moka.manager.support;

import java.util.Iterator;
import java.util.Map;

public class ParameterAssemblyUtil {

	 
    public static String constructUrl(String url, Map<String, String> parameters) {
        final StringBuffer buffer = new StringBuffer(parameters.size() * 10 + url.length() + 1);
        int i = 0;
        synchronized (buffer) {
            buffer.append(url);
            /*
             * if (!url.endsWith("/")) { buffer.append("/"); }
             * buffer.append(AppAgentClientConstant.APPAGENT_ACTION_NAME);
             */
            for (final Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                buffer.append(i++ == 0 ? "?" : "&");
                final Map.Entry entry = (Map.Entry) iter.next();
                final String key = (String) entry.getKey();
                final String value = (String) entry.getValue();
                if (value != null) {
                    buffer.append(key);
                    buffer.append("=");
                    buffer.append(value);
                }
            }

            return buffer.toString();
        }
    }

}
