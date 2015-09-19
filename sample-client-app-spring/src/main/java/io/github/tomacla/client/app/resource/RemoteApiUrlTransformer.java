package io.github.tomacla.client.app.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.ResourceTransformerSupport;
import org.springframework.web.servlet.resource.TransformedResource;

public class RemoteApiUrlTransformer extends ResourceTransformerSupport {

    private String remoteApiUrl;
    
    public RemoteApiUrlTransformer(String remoteApiUrl) {
	this.remoteApiUrl = remoteApiUrl;
	if(this.remoteApiUrl.endsWith("/")) {
	    this.remoteApiUrl = this.remoteApiUrl.substring(0, this.remoteApiUrl.length() - 1);
	}
    }
    
    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
	    throws IOException {

	resource = transformerChain.transform(request, resource);
	
	InputStream is = resource.getInputStream();

	StringWriter contentWriter = new StringWriter();
	BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
	String buf = null;
	while ((buf = rdr.readLine()) != null) {
	    contentWriter.write(buf.replaceAll("::REMOTE_API_URL::", remoteApiUrl));
	    contentWriter.write("\n");
	}

	return new TransformedResource(resource, contentWriter.toString().getBytes("UTF-8"));
	
    }

}